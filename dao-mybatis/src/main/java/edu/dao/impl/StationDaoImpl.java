package edu.dao.impl;

import edu.dao.StationDao;
import edu.dao.repository.StationRepository;
import edu.dao.util.SqlSessionBuilder;
import edu.error.ApplicationErrorDetails;
import edu.error.ApplicationException;
import edu.model.additional.Station;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@Repository
public class StationDaoImpl extends Dao implements StationDao {
    private final SqlSessionBuilder sqlSessionBuilder;

    public StationDaoImpl(SqlSessionBuilder sqlSessionBuilder) {
        super(LoggerFactory.getLogger(StationDaoImpl.class));
        this.sqlSessionBuilder = sqlSessionBuilder;
    }

    @Override
    public Station getStationByName(String name) throws ApplicationException {
        try (SqlSession sqlSession = sqlSessionBuilder.openSqlSession()) {
            try {
                StationRepository stationRepository = sqlSession.getMapper(StationRepository.class);

                return stationRepository.selectStationByName(name);

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
    public Station getStationById(long stationId) throws ApplicationException {
        try (SqlSession sqlSession = sqlSessionBuilder.openSqlSession()) {
            try {
                StationRepository stationRepository = sqlSession.getMapper(StationRepository.class);

                return stationRepository.selectStationById(stationId);

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
    public void addNewStation(Station station) throws ApplicationException {
        try (SqlSession sqlSession = sqlSessionBuilder.openSqlSession()) {
            try {
                StationRepository stationRepository = sqlSession.getMapper(StationRepository.class);
                stationRepository.insertStation(station);
                sqlSession.commit();
            } catch (RuntimeException e) {
                sqlSession.rollback();
                logErrors(e);

                if (e instanceof PersistenceException) {
                    if (e.getCause() instanceof SQLIntegrityConstraintViolationException) {
                        Map<String, Object> params = new HashMap<>();
                        params.put("STATION_NAME", station.getName());
                        throw new ApplicationException(
                                ApplicationErrorDetails.STATION_NAME_ALREADY_EXISTS,
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
    public void deleteStationById(long stationId) throws ApplicationException {
        try (SqlSession sqlSession = sqlSessionBuilder.openSqlSession()) {
            try {
                StationRepository stationRepository = sqlSession.getMapper(StationRepository.class);
                stationRepository.deleteStationById(stationId);
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
    public void deleteStationByName(String stationName) throws ApplicationException {
        try (SqlSession sqlSession = sqlSessionBuilder.openSqlSession()) {
            try {
                StationRepository stationRepository = sqlSession.getMapper(StationRepository.class);
                stationRepository.deleteStationByName(stationName);
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
    public void updateStation(Station station) throws ApplicationException {
        try (SqlSession sqlSession = sqlSessionBuilder.openSqlSession()) {
            try {
                StationRepository stationRepository = sqlSession.getMapper(StationRepository.class);
                if (stationRepository.selectStationById(station.getId()) == null) {
                    Map<String, Object> params = new HashMap<>();
                    params.put("STATION_ID", station.getName());
                    throw new ApplicationException(ApplicationErrorDetails.STATION_ID_NOT_FOUND, params, HttpStatus.NOT_FOUND);
                }

                stationRepository.updateStation(station);
                sqlSession.commit();
            } catch (RuntimeException e) {
                sqlSession.rollback();
                logErrors(e);

                if (e instanceof PersistenceException) {
                    if (e.getCause() instanceof SQLIntegrityConstraintViolationException) {
                        Map<String, Object> params = new HashMap<>();
                        params.put("STATION_NAME", station.getName());
                        throw new ApplicationException(
                                ApplicationErrorDetails.STATION_NAME_ALREADY_EXISTS,
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
}
