package edu.dao;

import edu.error.ApplicationException;
import edu.model.UserSession;

public interface SessionDao {
    void addNewSession(UserSession userSession) throws ApplicationException;
    void deleteUserOldSession(String currTime, int timeToLive) throws ApplicationException;
    void deleteAllFromSessionTable() throws ApplicationException;
    Integer deleteSession(String sessionId) throws ApplicationException;
    UserSession findSessionBySessionId(String sessionId) throws ApplicationException;
    UserSession findSessionByUserId(long userId) throws ApplicationException;
}
