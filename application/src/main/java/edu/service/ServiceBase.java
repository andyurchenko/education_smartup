package edu.service;

import edu.dao.UserDao;
import edu.error.ApplicationErrorDetails;
import edu.error.ApplicationException;
import edu.model.Administrator;
import edu.model.Client;
import edu.model.User;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class ServiceBase {
    protected final UserDao userDao;

    public ServiceBase(UserDao userDao) {
        this.userDao = userDao;
    }
    protected Client getClientBySession(String sessionId) throws ApplicationException {
        User client = userDao.getUserBySessionId(sessionId);

        if (client == null) {
            Map<String, Object> params = new HashMap<>();
            params.put("SESSION_ID", sessionId);
            throw new ApplicationException(ApplicationErrorDetails.SESSION_ID_NOT_FOUND, params, HttpStatus.NOT_FOUND);
        }

        if (client instanceof Administrator) {
            Map<String, Object> params = new HashMap<>();
            params.put("SESSION_ID", sessionId);
            throw new ApplicationException(ApplicationErrorDetails.INCORRECT_USER_TYPE, params, HttpStatus.CONFLICT);
        }

        return (Client) client;
    }

    protected void checkIfSessionIsValid(String sessionId) throws ApplicationException {
        checkIfSessionIdPresentsInCookies(sessionId);
        User user = userDao.getUserBySessionId(sessionId);
        if (user == null) {
            Map<String, Object> params = new HashMap<>();
            params.put("SESSION_ID", sessionId);
            throw new ApplicationException(ApplicationErrorDetails.SESSION_ID_NOT_FOUND, params, HttpStatus.NOT_FOUND);
        }
    }
    protected void checkIfSessionIdBelongsToAdministrator(String sessionId) throws ApplicationException {
        checkIfSessionIdPresentsInCookies(sessionId);
        User user = userDao.getUserBySessionId(sessionId);
        if (user == null) {
            Map<String, Object> params = new HashMap<>();
            params.put("SESSION_ID", sessionId);
            throw new ApplicationException(ApplicationErrorDetails.SESSION_ID_NOT_FOUND, params, HttpStatus.NOT_FOUND);
        }

        if (! (user instanceof Administrator)) {
            throw new ApplicationException(ApplicationErrorDetails.NOT_ALLOWED_REQUEST, HttpStatus.METHOD_NOT_ALLOWED);

        }
    }

    private void checkIfSessionIdPresentsInCookies(String sessionId) throws ApplicationException {
        if (sessionId == null || sessionId.isBlank()) {
            Map<String, Object> params = new HashMap<>();
            params.put("SESSION_ID", sessionId);
            throw new ApplicationException(ApplicationErrorDetails.SESSION_ID_NOT_PRESENT, params, HttpStatus.BAD_REQUEST);
        }
    }
}
