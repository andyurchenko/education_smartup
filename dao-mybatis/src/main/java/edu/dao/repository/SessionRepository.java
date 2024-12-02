package edu.dao.repository;

import edu.model.UserSession;
import org.apache.ibatis.annotations.Param;

public interface SessionRepository {
    void insertNewSession(@Param("SESSION") UserSession userSession);
    void insertNewSessionForTesting(@Param("SESSION_ID") String sessionId, @Param("USER_ID") long userId, @Param("SESSION_CREATION_TIME") String sessionCreationTime);
    void deleteUserOldSession(@Param("TIME_CHECK_AGAINST") String currTime, @Param("TIME_TO_LIVE") int timeToLive);
    void deleteAllFromSessionTable();
    void deleteUserSessionByUserId(@Param("USER_ID") long userId);
    Integer deleteSessionBySessionId(@Param("SESSION_ID") String sessionId);
    UserSession findSessionBySessionId(@Param("SESSION_ID") String sessionId);
    UserSession findSessionByUserId(@Param("USER_ID") long userId);
}