package edu.dao.impl;

import edu.dao.DebugDao;
import edu.dao.repository.DebugRepository;
import edu.dao.util.SqlSessionBuilder;
import edu.error.*;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

@Repository
public class DebugDaoImpl extends Dao implements DebugDao {
    private final SqlSessionBuilder sqlSessionBuilder;

    @Autowired
    public DebugDaoImpl(SqlSessionBuilder sqlSessionBuilder) {
        super(LoggerFactory.getLogger(DebugDaoImpl.class));
        this.sqlSessionBuilder = sqlSessionBuilder;
    }

    @Override
    public void clearDataBase() throws ApplicationException {
        try (SqlSession sqlSession = sqlSessionBuilder.openSqlSession()) {
            try {
                DebugRepository debugRepository = sqlSession.getMapper(DebugRepository.class);
                debugRepository.clearDataBase();
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
    public void clearTripTable() throws ApplicationException {
        try (SqlSession sqlSession = sqlSessionBuilder.openSqlSession()) {
            try {
                DebugRepository debugRepository = sqlSession.getMapper(DebugRepository.class);
                debugRepository.clearTripTable();
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
    public void clearUserTable() throws ApplicationException {
        try (SqlSession sqlSession = sqlSessionBuilder.openSqlSession()) {
            try {
                DebugRepository debugRepository = sqlSession.getMapper(DebugRepository.class);
                debugRepository.clearUserTable();
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
    public void clearActiveSessionTable() throws ApplicationException {
        try (SqlSession sqlSession = sqlSessionBuilder.openSqlSession()) {
            try {
                DebugRepository debugRepository = sqlSession.getMapper(DebugRepository.class);
                debugRepository.clearActiveSessionTable();
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
    public void clearBusTable() throws ApplicationException {
        try (SqlSession sqlSession = sqlSessionBuilder.openSqlSession()) {
            try {
                DebugRepository debugRepository = sqlSession.getMapper(DebugRepository.class);
                debugRepository.clearBusTable();
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
    public void clearStationTable() throws ApplicationException {
        try (SqlSession sqlSession = sqlSessionBuilder.openSqlSession()) {
            try {
                DebugRepository debugRepository = sqlSession.getMapper(DebugRepository.class);
                debugRepository.clearStationTable();
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
