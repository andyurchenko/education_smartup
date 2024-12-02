package edu.dao.repository;

import edu.model.additional.UserType;
import org.apache.ibatis.annotations.Param;

public interface SettingsRepository {
    UserType getUserTypeBySessionId(@Param("SESSION_ID") String sessionId);
}
