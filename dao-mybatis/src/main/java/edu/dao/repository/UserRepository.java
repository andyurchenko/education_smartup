package edu.dao.repository;

import edu.model.User;
import edu.model.additional.UserType;
import org.apache.ibatis.annotations.Param;

public interface UserRepository {
    User getUserById(@Param("ID") long id);
    User getUserBySessionId(@Param("SESSION_ID") String sessionId);
    void insertNewUser(@Param("USER") User user);
    void deleteAllFromUserTable();
    void deleteUserById(@Param("USER_ID") long id);
    UserType getUserTypeBySessionId(@Param("SESSION_ID") String sessionId);
    User getUserByLoginAndPasswordIfActive(@Param("LOGIN") String login, @Param("PASSWORD") String password);
    void inactivateUser(@Param("USER_ID") long userId);
    void activateUser(@Param("USER_ID") long userId);
}
