package edu.dao;

import edu.error.ApplicationException;
import edu.model.Administrator;
import edu.model.UserSession;
import org.apache.commons.lang3.tuple.Pair;

public interface AdministratorDao {
    Administrator getAdministratorById(long id) throws ApplicationException;
    Pair<Administrator, UserSession> addNewAdminAndStartNewAdminSession(Administrator administrator) throws ApplicationException;
    void deleteAllFromAdministratorTable() throws ApplicationException;
    Integer getAdministratorCount() throws ApplicationException;
    void updateAdministratorInfo(Administrator administrator) throws ApplicationException;
}
