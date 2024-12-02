package edu.dao.impl;

import edu.dao.AdministratorDao;
import edu.dao.repository.AdministratorRepository;
import edu.dao.repository.SessionRepository;
import edu.dao.repository.UserRepository;
import edu.dao.util.SqlSessionBuilder;
import edu.error.*;
import edu.model.Administrator;
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
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@Repository
public class AdministratorDaoImpl extends Dao implements AdministratorDao {
    private final SqlSessionBuilder sqlSessionBuilder;
    private final UserSessionBuilder userSessionBuilder;

    @Autowired
    public AdministratorDaoImpl(SqlSessionBuilder sqlSessionBuilder, UserSessionBuilder userSessionBuilder) {
        super(LoggerFactory.getLogger(AdministratorDaoImpl.class));
        this.sqlSessionBuilder = sqlSessionBuilder;
        this.userSessionBuilder = userSessionBuilder;
    }

    @Override
    public Administrator getAdministratorById(long id) throws ApplicationException {
        try (SqlSession session = sqlSessionBuilder.openSqlSession()) {
            try {
                AdministratorRepository repository = session.getMapper(AdministratorRepository.class);

                return repository.getAdministratorById(id);

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
    public Pair<Administrator, UserSession> addNewAdminAndStartNewAdminSession(Administrator administrator) throws ApplicationException {
        try (SqlSession sqlSession = sqlSessionBuilder.openSqlSession()) {
            try {
                UserRepository userRepository = sqlSession.getMapper(UserRepository.class);
                userRepository.insertNewUser(administrator);
                AdministratorRepository adminRepository = sqlSession.getMapper(AdministratorRepository.class);
                adminRepository.insertNewAdministrator(administrator);
                SessionRepository sessionRepository = sqlSession.getMapper(SessionRepository.class);
                UserSession userSession = userSessionBuilder.buildNewSessionForUser(administrator);
                sessionRepository.insertNewSession(userSession);
                sqlSession.commit();

                return new ImmutablePair<>(administrator, userSession);

            } catch (RuntimeException e) {
                sqlSession.rollback();
                logErrors(e);

                if (e instanceof PersistenceException) {
                    if (e.getCause() instanceof SQLIntegrityConstraintViolationException) {
                        Map<String, Object> params = new HashMap<>();
                        params.put("LOGIN", administrator.getLogin());
                        throw new ApplicationException(
                                ApplicationErrorDetails.LOGIN_ALREADY_EXISTS,
                                params,
                                HttpStatus.CONFLICT
                        );
                    }

                    throw new ApplicationException(ApplicationErrorDetails.INNER_DATA_BASE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
                }

                throw new ApplicationException(ApplicationErrorDetails.UNKNOWN_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }



    @Override
    public void deleteAllFromAdministratorTable() throws ApplicationException {
        try (SqlSession sqlSession = sqlSessionBuilder.openSqlSession()) {
            try {
                AdministratorRepository repository = sqlSession.getMapper(AdministratorRepository.class);
                repository.deleteAllFromAdministratorTable();
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

    public Integer getAdministratorCount() throws ApplicationException {
        Integer adminsCount;
        try (SqlSession sqlSession = sqlSessionBuilder.openSqlSession()) {
            try {
                AdministratorRepository administratorRepository = sqlSession.getMapper(AdministratorRepository.class);
                adminsCount = administratorRepository.getAdministratorCount();
            } catch (RuntimeException e) {
                logErrors(e);
                if (e instanceof PersistenceException) {
                    throw new ApplicationException(ApplicationErrorDetails.INNER_DATA_BASE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
                }

                throw new ApplicationException(ApplicationErrorDetails.UNKNOWN_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return adminsCount;
    }

    @Override
    public void updateAdministratorInfo(Administrator administrator) throws ApplicationException {
        try (SqlSession sqlSession = sqlSessionBuilder.openSqlSession()) {
            try {
                AdministratorRepository administratorRepository = sqlSession.getMapper(AdministratorRepository.class);
                administratorRepository.updateAdministratorInfo(administrator);
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
