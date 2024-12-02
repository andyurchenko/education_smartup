package edu.dao;

import edu.error.ApplicationException;
import edu.model.User;
import org.apache.commons.lang3.tuple.Pair;

public interface UserDao {
    User getUserById(long id) throws ApplicationException;
    User getUserBySessionId(String sessionId) throws ApplicationException;
    Pair<User, String> getUserByLoginAndPasswordIfActive(String login, String password) throws ApplicationException;
    void addNewUser(User user) throws ApplicationException;
    void deleteAllFromUserTable() throws ApplicationException;

    void deleteUserById(long id) throws ApplicationException;
    void inactivateUserById(long id) throws ApplicationException;

    void activateUserById(long id) throws ApplicationException;

}
