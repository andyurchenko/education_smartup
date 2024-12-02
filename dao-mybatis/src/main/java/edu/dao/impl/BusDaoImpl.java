package edu.dao.impl;

import edu.dao.BusDao;
import edu.dao.repository.BusRepository;
import edu.dao.util.SqlSessionBuilder;
import edu.error.ApplicationErrorDetails;
import edu.error.ApplicationException;
import edu.model.Bus;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class BusDaoImpl extends Dao implements BusDao {
    private final SqlSessionBuilder sqlSessionBuilder;
    @Autowired
    public BusDaoImpl(SqlSessionBuilder sqlSessionBuilder) {
        super(LoggerFactory.getLogger(BusDaoImpl.class));
        this.sqlSessionBuilder = sqlSessionBuilder;
    }

    @Override
    public void addNewBusToDB(Bus bus) throws ApplicationException {
        try (SqlSession sqlSession = sqlSessionBuilder.openSqlSession()) {
            try {
                BusRepository busRepository = sqlSession.getMapper(BusRepository.class);
                busRepository.insertBus(bus);
                sqlSession.commit();
            } catch (RuntimeException e) {
                sqlSession.rollback();
                logErrors(e);

                if (e instanceof PersistenceException) {
                    if (e.getCause() instanceof SQLIntegrityConstraintViolationException) {
                        Map<String, Object> params = new HashMap<>();
                        params.put("BUS_BRAND_NAME", bus.getBrandName());
                        throw new ApplicationException(
                                ApplicationErrorDetails.BUS_BRAND_NAME_ALREADY_EXISTS,
                                params,
                                HttpStatus.INTERNAL_SERVER_ERROR
                        );
                    }

                    throw new ApplicationException(ApplicationErrorDetails.INNER_DATA_BASE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
                }

                throw new ApplicationException(ApplicationErrorDetails.UNKNOWN_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @Override
    public List<Bus> selectAllBuses() throws ApplicationException {
        try (SqlSession sqlSession = sqlSessionBuilder.openSqlSession()) {
            try {
                BusRepository busRepository = sqlSession.getMapper(BusRepository.class);
                return busRepository.selectAllBuses();
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
    public Bus getBusByBusBrandName(String busBrandName) throws ApplicationException {
        try (SqlSession sqlSession = sqlSessionBuilder.openSqlSession()) {
            try {
                BusRepository busRepository = sqlSession.getMapper(BusRepository.class);

                return busRepository.selectBusByBrandName(busBrandName);
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
    public Bus getBusByBusById(long busId) throws ApplicationException {
        try (SqlSession sqlSession = sqlSessionBuilder.openSqlSession()) {
            try {
                BusRepository busRepository = sqlSession.getMapper(BusRepository.class);

                return busRepository.selectBusById(busId);

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
    public void deleteBusById(long busId) throws ApplicationException {
        try (SqlSession sqlSession = sqlSessionBuilder.openSqlSession()) {
            try {
                BusRepository busRepository = sqlSession.getMapper(BusRepository.class);
                busRepository.deleteBusById(busId);
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
    public void deleteBusByBrandName(String busBrand) throws ApplicationException {
        try (SqlSession sqlSession = sqlSessionBuilder.openSqlSession()) {
            try {
                BusRepository busRepository = sqlSession.getMapper(BusRepository.class);
                busRepository.deleteBusByBrandName(busBrand);
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
    public void updateBus(Bus bus) throws ApplicationException {
        try (SqlSession sqlSession = sqlSessionBuilder.openSqlSession()) {
            try {
                BusRepository busRepository = sqlSession.getMapper(BusRepository.class);
                busRepository.updateBus(bus);
                sqlSession.commit();
            } catch (RuntimeException e) {
                sqlSession.rollback();
                logErrors(e);

                if (e instanceof PersistenceException) {
                    if (e.getCause() instanceof SQLIntegrityConstraintViolationException) {
                        Map<String, Object> params = new HashMap<>();
                        params.put("BUS_BRAND_NAME", bus.getBrandName());
                        throw new ApplicationException(ApplicationErrorDetails.BUS_BRAND_NAME_ALREADY_EXISTS, params, HttpStatus.INTERNAL_SERVER_ERROR);
                    }

                    throw new ApplicationException(ApplicationErrorDetails.INNER_DATA_BASE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
                }

                throw new ApplicationException(ApplicationErrorDetails.UNKNOWN_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }
}
