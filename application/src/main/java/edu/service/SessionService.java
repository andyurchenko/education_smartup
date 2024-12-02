package edu.service;

import edu.configuration.ApplicationConfig;
import edu.dao.SessionDao;
import edu.dao.UserDao;
import edu.dto.debug.DtoBooleanResponse;
import edu.dto.debug.DtoIsSessionActiveRequest;
import edu.error.ApplicationException;
import edu.model.User;
import edu.model.UserSession;
import edu.util.DateTimeBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class SessionService extends ServiceBase {
    private final DateTimeBuilder dateTimeBuilder;
    private final ApplicationConfig applicationConfig;
    private final SessionDao sessionDao;

    @Autowired
    public SessionService(DateTimeBuilder dateTimeBuilder, ApplicationConfig applicationConfig, SessionDao sessionDao, UserDao userDao) {
        super(userDao);
        this.dateTimeBuilder = dateTimeBuilder;
        this.applicationConfig = applicationConfig;
        this.sessionDao = sessionDao;
    }

    public String startNewSession(User user) throws ApplicationException {
        UserSession userSession = createSession(user);
        sessionDao.addNewSession(userSession);

        return userSession.getSessionId();
    }

    public void deleteUserOldSession() throws ApplicationException {
        sessionDao.deleteUserOldSession(
                dateTimeBuilder.getCurrentDateTimeAsString(),
                applicationConfig.getUserIdleTimeout()
        );
    }

    public DtoBooleanResponse isSessionActive(String sessionId) throws ApplicationException {
        checkIfSessionIsValid(sessionId);
        if(sessionDao.findSessionBySessionId(sessionId) != null) {
            return new DtoBooleanResponse(true);
        }

        return new DtoBooleanResponse(false);
    }

    public DtoBooleanResponse isSessionActive(DtoIsSessionActiveRequest dtoRequest) throws ApplicationException {
        if(sessionDao.findSessionBySessionId(dtoRequest.getSessionId()) != null) {
            return new DtoBooleanResponse(true);
        }

        return new DtoBooleanResponse(false);
    }

    public boolean isSessionActive(long userId) throws ApplicationException {
        return sessionDao.findSessionByUserId(userId) != null;
    }

    private UserSession createSession(User user) {
        return new UserSession(UUID.randomUUID(), user, dateTimeBuilder.getCurrentDateTimeAsString());
    }
}
