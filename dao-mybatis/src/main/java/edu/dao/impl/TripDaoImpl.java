package edu.dao.impl;

import edu.dao.TripDao;
import edu.dao.repository.PassengerSeatsRepository;
import edu.dao.repository.TripRepository;
import edu.dao.util.SqlSessionBuilder;
import edu.error.ApplicationErrorDetails;
import edu.error.ApplicationException;
import edu.model.Trip;
import edu.model.additional.Schedule;
import edu.model.additional.TripDate;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TripDaoImpl extends  Dao implements TripDao {
    private final SqlSessionBuilder sqlSessionBuilder;

    @Autowired
    public TripDaoImpl(SqlSessionBuilder sqlSessionBuilder) {
        super(LoggerFactory.getLogger(TripDaoImpl.class));
        this.sqlSessionBuilder = sqlSessionBuilder;
    }


    @Override
    public void addNewTrip(Trip trip) throws ApplicationException {
        try (SqlSession sqlSession = sqlSessionBuilder.openSqlSession()) {
            try {
                TripRepository tripRepository = sqlSession.getMapper(TripRepository.class);
                tripRepository.insertTrip(trip);
                tripRepository.insertTripDates(trip.getId(), trip.getTripDates());
                Schedule schedule = trip.getSchedule();
                if (schedule != null) {
                    tripRepository.insertTripSchedule(trip.getId(), schedule);
                }
                PassengerSeatsRepository passengerSeatsRepository = sqlSession.getMapper(PassengerSeatsRepository.class);
                passengerSeatsRepository.insertCountOfSeatsInBus(trip.getTripDates(), trip.getBus().getPlaceCount());
                var map = createPassengerSeatsPositionNumberMapForTripDates(trip.getTripDates(), trip.getBus().getPlaceCount());
                passengerSeatsRepository.insertPassengerSeatsPositionNumberInBus(map);
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
    public void deleteTripById(long tripId) throws ApplicationException {
        try (SqlSession sqlSession = sqlSessionBuilder.openSqlSession()) {
            try {
                TripRepository tripRepository = sqlSession.getMapper(TripRepository.class);
                tripRepository.deleteTripById(tripId);
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
    public Trip getTripById(long tripId) throws ApplicationException {
        try (SqlSession sqlSession = sqlSessionBuilder.openSqlSession()) {
            try {
                TripRepository tripRepository = sqlSession.getMapper(TripRepository.class);

                return tripRepository.selectTripById(tripId);

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
    public List<Trip> getTrips(Map<String, String> params) throws ApplicationException {
        try (SqlSession sqlSession = sqlSessionBuilder.openSqlSession()) {
            try {
                TripRepository tripRepository = sqlSession.getMapper(TripRepository.class);

                return tripRepository.selectTripsByParams(params);

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
    public Schedule getTripScheduleByTripId(long tripId) throws ApplicationException {
        try (SqlSession sqlSession = sqlSessionBuilder.openSqlSession()) {
            try {
                TripRepository tripRepository = sqlSession.getMapper(TripRepository.class);
                return tripRepository.selectTripScheduleByTripId(tripId);

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
    public List<LocalDate> getTripDatesByTripId(long tripId) throws ApplicationException {
        try (SqlSession sqlSession = sqlSessionBuilder.openSqlSession()) {
            try {
                TripRepository tripRepository = sqlSession.getMapper(TripRepository.class);
                return tripRepository.selectTripDatesByTripId(tripId);

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
    public void deleteTripScheduleByTripId(long tripId) throws ApplicationException {
        try (SqlSession sqlSession = sqlSessionBuilder.openSqlSession()) {
            try {
                TripRepository tripRepository = sqlSession.getMapper(TripRepository.class);
                tripRepository.deleteTripScheduleByTripId(tripId);
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
    public void deleteTripDatesByTripId(long tripId) throws ApplicationException {
        try (SqlSession sqlSession = sqlSessionBuilder.openSqlSession()) {
            try {
                TripRepository tripRepository = sqlSession.getMapper(TripRepository.class);
                tripRepository.deleteTripDatesByTripId(tripId);
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
    public void updateTrip(Trip trip) throws ApplicationException {
        try (SqlSession sqlSession = sqlSessionBuilder.openSqlSession()) {
            try {
                TripRepository tripRepository = sqlSession.getMapper(TripRepository.class);
                tripRepository.deleteTripDatesByTripId(trip.getId());
                tripRepository.deleteTripScheduleByTripId(trip.getId());
                tripRepository.updateTrip(trip);
                tripRepository.insertTripDates(trip.getId(), trip.getTripDates());
                Schedule schedule = trip.getSchedule();
                if (schedule != null) {
                    tripRepository.insertTripSchedule(trip.getId(), schedule);
                }
                PassengerSeatsRepository passengerSeatsRepository = sqlSession.getMapper(PassengerSeatsRepository.class);
                passengerSeatsRepository.insertCountOfSeatsInBus(trip.getTripDates(), trip.getBus().getPlaceCount());

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
    public void updateTripSetApproved(long tripId, boolean approvedValue) throws ApplicationException {
        try (SqlSession sqlSession = sqlSessionBuilder.openSqlSession()) {
            try {
                TripRepository tripRepository = sqlSession.getMapper(TripRepository.class);
                tripRepository.updateTripSetApproved(tripId, approvedValue);
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
    public Integer getFreeSeatsCountOfTrip(long tripId, LocalDate date) throws ApplicationException {
        try (SqlSession sqlSession = sqlSessionBuilder.openSqlSession()) {
            try {
                TripRepository tripRepository = sqlSession.getMapper(TripRepository.class);
                return tripRepository.getFreeSeatsCounterByTripIdAndDate(tripId);
            } catch (RuntimeException e) {
                logErrors(e);

                if (e instanceof PersistenceException) {
                    throw new ApplicationException(ApplicationErrorDetails.INNER_DATA_BASE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
                }

                throw new ApplicationException(ApplicationErrorDetails.UNKNOWN_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    private Map<Long, List<Integer>> createPassengerSeatsPositionNumberMapForTripDates(List<TripDate> tripDates, int busCapacity) {
        Map<Long, List<Integer>> map = new HashMap<>();
        for (TripDate tripDate : tripDates) {
            List<Integer> list = new ArrayList<>();
            for (int i = 1; i <= busCapacity; i++) {
                list.add(i);
            }
            map.put(tripDate.getId(), list);
        }

        return map;
    }
}
