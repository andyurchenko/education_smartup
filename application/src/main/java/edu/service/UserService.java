package edu.service;

import edu.dao.AdministratorDao;
import edu.dao.SessionDao;
import edu.dao.UserDao;
import edu.dto.user.DtoLogin;
import edu.error.*;
import edu.model.Administrator;
import edu.model.Client;
import edu.model.User;
import edu.util.mapper.AdministratorMapper;
import edu.util.mapper.ClientMapper;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService extends ServiceBase {
    private final SessionDao sessionDao;
    private final AdministratorDao administratorDao;
    private final AdministratorMapper administratorMapper;
    private final ClientMapper clientMapper;
    private final int MIN_NUMBER_ACTIVE_ADMIN_ACCOUNTS = 2;

    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    public UserService(UserDao userDao, SessionDao sessionDao, AdministratorDao administratorDao, AdministratorMapper administratorMapper, ClientMapper clientMapper) {
        super(userDao);
        this.sessionDao = sessionDao;
        this.administratorDao = administratorDao;
        this.administratorMapper = administratorMapper;
        this.clientMapper = clientMapper;
    }

    public Pair<?, String> login(DtoLogin dtoRequest) throws ApplicationException {
        Pair<User, String> userAndSession = userDao.getUserByLoginAndPasswordIfActive(dtoRequest.getLogin(), dtoRequest.getPassword());
        User user = userAndSession.getLeft();
        if (user instanceof Administrator) {
            return new ImmutablePair<>(
                    administratorMapper.toDtoAdministratorResponse((Administrator) user),
                    userAndSession.getRight()
            );
        }

        if (user instanceof Client) {
            return new ImmutablePair<>(
                    clientMapper.toDtoLoginResponse((Client) user),
                    userAndSession.getRight()
            );
        }

        logger
                .atError()
                .setMessage("User from DB after login operation is not of an allowed type and have class - {} ")
                .addArgument(user.getClass())
                .log();

        throw new ApplicationException(ApplicationErrorDetails.UNKNOWN_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public void logout(String sessionId) throws ApplicationException {
        checkIfSessionIsValid(sessionId);
        if (sessionDao.deleteSession(sessionId) == 0) {
            Map<String, Object> params = new HashMap<>();
            params.put("SESSION_ID", sessionId);
            throw new ApplicationException(ApplicationErrorDetails.SESSION_ID_NOT_FOUND, params, HttpStatus.NOT_FOUND);
        }
    }

    public void deleteAccount(String sessionId) throws ApplicationException {
        checkIfSessionIsValid(sessionId);
        User userToDelete = userDao.getUserBySessionId(sessionId);
        if (userToDelete == null) {
            Map<String, Object> params = new HashMap<>();
            params.put("SESSION_ID", sessionId);
            throw new ApplicationException(ApplicationErrorDetails.SESSION_ID_NOT_FOUND, params, HttpStatus.NOT_FOUND);
        }

        if ((userToDelete instanceof Administrator) && isNotEnoughAdminsOnServer()) {
            throw new ApplicationException(
                    ApplicationErrorDetails.CAN_NOT_DELETE_LAST_ADMIN,
                    HttpStatus.METHOD_NOT_ALLOWED
            );

        }

        userDao.deleteUserById(userToDelete.getId());
    }

    public void deactivateUserAccount(String sessionId) throws ApplicationException {
        checkIfSessionIsValid(sessionId);
        User userToDelete = userDao.getUserBySessionId(sessionId);
        if (userToDelete == null) {
            Map<String, Object> params = new HashMap<>();
            params.put("SESSION_ID", sessionId);
            throw new ApplicationException(ApplicationErrorDetails.SESSION_ID_NOT_FOUND, params, HttpStatus.NOT_FOUND);
        }

        if ((userToDelete instanceof Administrator) && isNotEnoughAdminsOnServer()) {
            throw new ApplicationException(ApplicationErrorDetails.CAN_NOT_DELETE_LAST_ADMIN, HttpStatus.METHOD_NOT_ALLOWED);
        }

        userDao.inactivateUserById(userToDelete.getId());
    }

    public Object getUserBySessionId(String sessionId) throws ApplicationException {
        checkIfSessionIsValid(sessionId);
        User user = userDao.getUserBySessionId(sessionId);

        if (user instanceof Administrator) {
            return administratorMapper.toDtoGetInfoResponse((Administrator) user);
        }

        if (user instanceof Client) {
            return clientMapper.toDtoGetInfoResponse((Client) user);
        }

        throw new ApplicationException(ApplicationErrorDetails.SESSION_ID_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    private boolean isNotEnoughAdminsOnServer() throws ApplicationException {
        return administratorDao.getAdministratorCount() < MIN_NUMBER_ACTIVE_ADMIN_ACCOUNTS;
    }

}
