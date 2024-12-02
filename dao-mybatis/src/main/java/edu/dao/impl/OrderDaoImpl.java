package edu.dao.impl;

import edu.dao.OrderDao;
import edu.dao.repository.OrderRepository;
import edu.dao.repository.PassengerRepository;
import edu.dao.repository.PassengerSeatsRepository;
import edu.dao.util.SqlSessionBuilder;
import edu.error.ApplicationErrorDetails;
import edu.error.ApplicationException;
import edu.model.Order;
import edu.model.additional.TripDate;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class OrderDaoImpl extends Dao implements OrderDao {
    private final SqlSessionBuilder sqlSessionBuilder;

    @Autowired
    public OrderDaoImpl(SqlSessionBuilder sqlSessionBuilder) {
        super(LoggerFactory.getLogger(OrderDaoImpl.class));
        this.sqlSessionBuilder = sqlSessionBuilder;
    }

    @Override
    public void addNewOrder(Order order, TripDate tripDate) throws ApplicationException {
        try (SqlSession sqlSession = sqlSessionBuilder.openSqlSession()) {
            try {
                PassengerSeatsRepository passengerSeatsRepository = sqlSession.getMapper(PassengerSeatsRepository.class);
                if (passengerSeatsRepository.decrementPassengersSeatsCounter(tripDate.getId(), order.getPassengers().size()) == 0) {
                    sqlSession.rollback();
                    throw new ApplicationException(ApplicationErrorDetails.NOT_ENOUGH_FREE_PASSENGER_SEATS, HttpStatus.CONFLICT);
                }
                OrderRepository orderRepository = sqlSession.getMapper(OrderRepository.class);
                orderRepository.insertOrder(order, tripDate);
                PassengerRepository passengerRepository = sqlSession.getMapper(PassengerRepository.class);
                passengerRepository.insertPassengers(order.getId(), order.getPassengers());
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
    public Long getTripDateIdOfOrderByOrderId(long orderId) throws ApplicationException {
        try (SqlSession sqlSession = sqlSessionBuilder.openSqlSession()) {
            try {
                OrderRepository orderRepository = sqlSession.getMapper(OrderRepository.class);
                return orderRepository.selectTripDateIdOfOrderByOrderId(orderId);
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
    public Order getOrderById(long orderId) throws ApplicationException {
        try (SqlSession sqlSession = sqlSessionBuilder.openSqlSession()) {
            try {
                OrderRepository orderRepository = sqlSession.getMapper(OrderRepository.class);
                return orderRepository.getOrderById(orderId);
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
    public void deleteOrder(long orderId) throws ApplicationException {
        try (SqlSession sqlSession = sqlSessionBuilder.openSqlSession()) {
            try {
                OrderRepository orderRepository = sqlSession.getMapper(OrderRepository.class);
                Integer numberOfPassengers = orderRepository.getNumberOfPassengersInOrder(orderId);
                PassengerSeatsRepository passengerSeatsRepository = sqlSession.getMapper(PassengerSeatsRepository.class);
                Long tripDateId = orderRepository.selectTripDateIdOfOrderByOrderId(orderId);
                int deletedRows = orderRepository.deleteOrderById(orderId);
                if (deletedRows != 1) {
                    logger
                            .atError()
                            .log("Number of deleted rows when trying to delete order with ID [{}] is not equal to 1 but be equals to {}", orderId, deletedRows);
                    sqlSession.rollback();
                    throw new ApplicationException(ApplicationErrorDetails.INNER_DATA_BASE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
                }
                passengerSeatsRepository.incrementPassengersSeatsCounter(tripDateId, numberOfPassengers);
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
    public List<Order> getOrdersByParams(Map<String, String> params) throws ApplicationException {
        try (SqlSession sqlSession = sqlSessionBuilder.openSqlSession()) {
            try {
                OrderRepository orderRepository = sqlSession.getMapper(OrderRepository.class);

                return orderRepository.getOrdersByParams(params);
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
    public Long getClientIdOfOrder(long orderId) throws ApplicationException {
        try (SqlSession sqlSession = sqlSessionBuilder.openSqlSession()) {
            try {
                OrderRepository orderRepository = sqlSession.getMapper(OrderRepository.class);

                return orderRepository.selectClientIdOfOrder(orderId);
            } catch (RuntimeException e) {
                logErrors(e);

                if (e instanceof PersistenceException) {
                    throw new ApplicationException(ApplicationErrorDetails.INNER_DATA_BASE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
                }

                throw new ApplicationException(ApplicationErrorDetails.UNKNOWN_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }
}
