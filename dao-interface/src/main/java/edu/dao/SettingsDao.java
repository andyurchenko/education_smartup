package edu.dao;

import edu.error.ApplicationException;
import edu.model.additional.UserType;

public interface SettingsDao {
    UserType getUserTypeBySessionId(String sessionId) throws ApplicationException;
}
