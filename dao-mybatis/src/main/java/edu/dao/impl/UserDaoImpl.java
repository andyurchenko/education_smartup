package edu.dao.impl;

import edu.dao.UserDao;
import edu.dao.repository.SessionRepository;
import edu.dao.repository.UserRepository;
import edu.dao.util.SqlSessionBuilder;
import edu.error.*;
import edu.model.User;
import edu.model.UserSession;
import edu.util.UserSessionBuilder;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UserDaoImpl extends Dao implements UserDao {
    private final SqlSessionBuilder sqlSessionBuilder;
    private final UserSessionBuilder userSessionBuilder;

    @Autowired
    public UserDaoImpl(SqlSessionBuilder sqlSessionBuilder, UserSessionBuilder userSessionBuilder) {
        super(LoggerFactory.getLogger(UserDaoImpl.class));
        this.sqlSessionBuilder = sqlSessionBuilder;
        this.userSessionBuilder = userSessionBuilder;
    }

    @Override
    public User getUserById(long id) throws ApplicationException {
        try (SqlSession sqlSession = sqlSessionBuilder.openSqlSession()) {
            try {
                UserRepository repository = sqlSession.getMapper(UserRepository.class);

                return repository.getUserById(id);

            } catch (RuntimeException e) {
                logErrors(e);

                if (e instanceof PersistenceException) {
                    throw new ApplicationException(ApplicationErrorDetails.INNER_DATA_BASE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
                }

                throw new ApplicationException(ApplicationErrorDetails.UNKNOWN_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @Override
    public User getUserBySessionId(String sessionId) throws ApplicationException {
        try (SqlSession sqlSession = sqlSessionBuilder.openSqlSession()) {
            User user;
            try {
                UserRepository userRepository = sqlSession.getMapper(UserRepository.class);
                user = userRepository.getUserBySessionId(sessionId);
            } catch (RuntimeException e) {
                logErrors(e);
                if (e instanceof PersistenceException) {
                    throw new ApplicationException(ApplicationErrorDetails.INNER_DATA_BASE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
                }

                throw new ApplicationException(ApplicationErrorDetails.UNKNOWN_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            if (user == null) {
                Map<String, Object> params = new HashMap<>();
                params.put("SESSION_ID", sessionId);
                throw new ApplicationException(ApplicationErrorDetails.SESSION_ID_NOT_FOUND, params, HttpStatus.NOT_FOUND);
            }

            return user;
        }
    }

    @Override
    public Pair<User, String> getUserByLoginAndPasswordIfActive(String login, String password) throws ApplicationException {
        try (SqlSession sqlSession = sqlSessionBuilder.openSqlSession()) {
            try {
                UserRepository userRepository = sqlSession.getMapper(UserRepository.class);
                User user = userRepository.getUserByLoginAndPasswordIfActive(login, password);

                if (user == null) {
                    throw new ApplicationException(ApplicationErrorDetails.LOGIN_OR_PASSWORD_INCORRECT, HttpStatus.BAD_REQUEST);
                }

                SessionRepository sessionRepository = sqlSession.getMapper(SessionRepository.class);
                sessionRepository.deleteUserSessionByUserId(user.getId());
                UserSession userSession = userSessionBuilder.buildNewSessionForUser(user);
                sessionRepository.insertNewSession(userSession);
                sqlSession.commit();

                return new ImmutablePair<>(user, userSession.getSessionId());

            } catch (RuntimeException e) {
                sqlSession.rollback();
                logErrors(e);

                if (e instanceof PersistenceException) {
                    throw new ApplicationException(ApplicationErrorDetails.INNER_DATA_BASE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
                }

                throw new ApplicationException(ApplicationErrorDetails.UNKNOWN_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @Override
    public void addNewUser(User user) throws ApplicationException {
        try (SqlSession sqlSession = sqlSessionBuilder.openSqlSession()) {
            try {
                UserRepository repository = sqlSession.getMapper(UserRepository.class);
                repository.insertNewUser(user);
                sqlSession.commit();
            } catch (RuntimeException e) {
                sqlSession.rollback();
                logErrors(e);

                if (e instanceof PersistenceException) {
                    throw new ApplicationException(ApplicationErrorDetails.LOGIN_ALREADY_EXISTS, HttpStatus.CONFLICT);
                }

                throw new ApplicationException(ApplicationErrorDetails.UNKNOWN_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);

            }
        }
    }

    @Override
    public void deleteAllFromUserTable() throws ApplicationException {
        try (SqlSession sqlSession = sqlSessionBuilder.openSqlSession()) {
            try {
                UserRepository repository = sqlSession.getMapper(UserRepository.class);
                repository.deleteAllFromUserTable();
                sqlSession.commit();
            } catch (RuntimeException e) {
                sqlSession.rollback();
                logErrors(e);

                if (e instanceof PersistenceException) {
                    throw new ApplicationException(ApplicationErrorDetails.INNER_DATA_BASE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
                }

                throw new ApplicationException(ApplicationErrorDetails.UNKNOWN_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @Override
    public void deleteUserById(long id) throws ApplicationException {
        try (SqlSession sqlSession = sqlSessionBuilder.openSqlSession()) {
            try {
                UserRepository userRepository = sqlSession.getMapper(UserRepository.class);
                userRepository.deleteUserById(id);
                sqlSession.commit();
            } catch (RuntimeException e) {
                sqlSession.rollback();
                logErrors(e);

                if (e instanceof PersistenceException) {
                    throw new ApplicationException(ApplicationErrorDetails.INNER_DATA_BASE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
                }

                throw new ApplicationException(ApplicationErrorDetails.UNKNOWN_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @Override
    public void inactivateUserById(long id) throws ApplicationException {
        SqlSession sqlSession = sqlSessionBuilder.openSqlSession();
        try {
            UserRepository userRepository = sqlSession.getMapper(UserRepository.class);
            userRepository.inactivateUser(id);
            sqlSession.commit();
        } catch (RuntimeException e) {
            sqlSession.rollback();
            sqlSession.close();
            logErrors(e);

            if (e instanceof PersistenceException) {
                throw new ApplicationException(ApplicationErrorDetails.INNER_DATA_BASE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            throw new ApplicationException(ApplicationErrorDetails.UNKNOWN_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);

        }

        sqlSession.close();
    }

    @Override
    public void activateUserById(long id) throws ApplicationException {
        try (SqlSession sqlSession = sqlSessionBuilder.openSqlSession()) {
            try {
                UserRepository userRepository = sqlSession.getMapper(UserRepository.class);
                userRepository.activateUser(id);
                sqlSession.commit();
            } catch (RuntimeException e) {
                sqlSession.rollback();
                logErrors(e);

                if (e instanceof PersistenceException) {
                    throw new ApplicationException(ApplicationErrorDetails.INNER_DATA_BASE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
                }

                throw new ApplicationException(ApplicationErrorDetails.UNKNOWN_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }
}
