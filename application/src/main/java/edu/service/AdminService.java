package edu.service;

import edu.dao.AdministratorDao;
import edu.dao.ClientDao;
import edu.dao.UserDao;
import edu.dto.administrator.DtoRegisterNewAdministratorRequest;
import edu.dto.administrator.DtoRegisterNewAdministratorResponse;
import edu.dto.administrator.DtoUpdateAdministratorRequest;
import edu.dto.administrator.DtoUpdateAdministratorResponse;
import edu.dto.client.DtoGetClientInfoResponse;
import edu.error.*;
import edu.model.Administrator;
import edu.model.Client;
import edu.model.User;
import edu.model.UserSession;
import edu.util.mapper.AdministratorMapper;
import edu.util.mapper.ClientMapper;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminService extends ServiceBase {
    private final AdministratorMapper administratorMapper;
    private final AdministratorDao administratorDao;
    private final ClientDao clientDao;
    private final ClientMapper clientMapper;
    @Autowired
    public AdminService(AdministratorMapper administratorMapper, AdministratorDao administratorDao, ClientDao clientDao, UserDao userDao, ClientMapper clientMapper) {
        super(userDao);
        this.administratorMapper = administratorMapper;
        this.administratorDao = administratorDao;
        this.clientDao = clientDao;
        this.clientMapper = clientMapper;
    }

    public Pair<DtoRegisterNewAdministratorResponse, String> registerNewAdministratorAndStartNewAdminSession(DtoRegisterNewAdministratorRequest dtoRequest) throws ApplicationException {
        Administrator administrator = administratorMapper.toModelFromRegisterDto(dtoRequest);
        Pair<Administrator, UserSession> pairAdminUserSession = administratorDao.addNewAdminAndStartNewAdminSession(administrator);
        var dtoResponse = administratorMapper.toDtoRegisterResponse(pairAdminUserSession.getLeft());
        String userSession = pairAdminUserSession.getRight().getSessionId();

        return new ImmutablePair<>(dtoResponse, userSession);
    }

    public DtoRegisterNewAdministratorResponse getAdministratorById(long id) throws ApplicationException {
        Administrator administrator = administratorDao.getAdministratorById(id);

        return administratorMapper.toDtoRegisterResponse(administrator);
    }

    public List<DtoGetClientInfoResponse> getAllClients(String sessionId) throws ApplicationException {
        checkIfSessionIdBelongsToAdministrator(sessionId);
        List<Client> clients = clientDao.getAllClients();

        return clientMapper.toDtoGetClientListResponse(clients);
    }

    public DtoUpdateAdministratorResponse updateAdministratorInfo(String sessionId, DtoUpdateAdministratorRequest dtoRequest) throws ApplicationException {
        checkIfSessionIdBelongsToAdministrator(sessionId);
        User user = userDao.getUserBySessionId(sessionId);
        if (user == null) {
            Map<String, Object> params = new HashMap<>();
            params.put("SESSION_ID", sessionId);
            throw new ApplicationException(ApplicationErrorDetails.SESSION_ID_NOT_FOUND, params, HttpStatus.NOT_FOUND);
        }

        if (!(user instanceof Administrator)) {
            throw new ApplicationException(ApplicationErrorDetails.NOT_ALLOWED_REQUEST, HttpStatus.METHOD_NOT_ALLOWED);
        }
        Administrator administrator = updateAdministratorWithNewInfo((Administrator) user, dtoRequest);
        administratorDao.updateAdministratorInfo(administrator);

        return administratorMapper.toDtoUpdateInfoResponse(administrator);
    }

    private Administrator updateAdministratorWithNewInfo(Administrator administrator, DtoUpdateAdministratorRequest dtoRequest) {
        administrator.setFirstName(dtoRequest.getFirstName());
        administrator.setLastName(dtoRequest.getLastName());
        administrator.setPatronymic(dtoRequest.getPatronymic());
        administrator.setPosition(dtoRequest.getPosition());
        administrator.setPassword(dtoRequest.getNewPassword());

        return administrator;
    }
}
