package edu.dao.impl;

import edu.dao.PlaceDao;
import edu.dao.repository.PassengerSeatsRepository;
import edu.dao.util.SqlSessionBuilder;
import edu.error.ApplicationErrorDetails;
import edu.error.ApplicationException;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PlaceDaoImpl extends Dao implements PlaceDao {
    private final SqlSessionBuilder sqlSessionBuilder;


    public PlaceDaoImpl(SqlSessionBuilder sqlSessionBuilder) {
        super(LoggerFactory.getLogger(PlaceDaoImpl.class));
        this.sqlSessionBuilder = sqlSessionBuilder;
    }

    @Override
    public List<Integer> gerFreePassengerSeatsByOrderId(long orderId) throws ApplicationException {
        try (SqlSession sqlSession = sqlSessionBuilder.openSqlSession()) {
            try {
                PassengerSeatsRepository passengerSeatsRepository = sqlSession.getMapper(PassengerSeatsRepository.class);

                return passengerSeatsRepository.selectFreePassengerSeatsByOrderId(orderId);

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
    public Integer getPassengerSeatNumberByPassengerId(long orderId, long passengerId) throws ApplicationException {
        try (SqlSession sqlSession = sqlSessionBuilder.openSqlSession()) {
            try {
                PassengerSeatsRepository passengerSeatsRepository = sqlSession.getMapper(PassengerSeatsRepository.class);

                return passengerSeatsRepository.selectPassengerSeatNumberByPassengerId(orderId, passengerId);

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
    public Integer bookPassengerSeat(long orderId, long passengerId, int seatToBook) throws ApplicationException {
        try (SqlSession sqlSession = sqlSessionBuilder.openSqlSession()) {
            try {
                PassengerSeatsRepository passengerSeatsRepository = sqlSession.getMapper(PassengerSeatsRepository.class);
                Integer seatIfBookedBefore = passengerSeatsRepository.selectPassengerSeatNumberByPassengerId(orderId, passengerId);

                if (passengerSeatsRepository.bookPassengersSeatInBus(orderId, passengerId, seatToBook) == FAILURE) {
                    sqlSession.rollback();
                    throw new ApplicationException(ApplicationErrorDetails.SEAT_NUMBER_TAKEN_ALREADY, HttpStatus.CONFLICT);
                }

                if (seatIfBookedBefore != null && seatIfBookedBefore != 0) {
                    passengerSeatsRepository.freePassengerSeatTakenBefore(orderId, passengerId, seatIfBookedBefore);
                }
                sqlSession.commit();

                return seatToBook;

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
