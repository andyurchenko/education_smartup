package edu.service;

import edu.dao.OrderDao;
import edu.dao.TripDao;
import edu.dao.UserDao;
import edu.dto.order.DtoOrderRequest;
import edu.dto.order.DtoOrderResponse;
import edu.error.ApplicationErrorDetails;
import edu.error.ApplicationException;
import edu.model.Client;
import edu.model.Order;
import edu.model.Trip;
import edu.model.User;
import edu.model.additional.TripDate;
import edu.util.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class OrderService extends ServiceBase {
    private final OrderDao orderDao;
    private final TripDao tripDao;
    private final OrderMapper orderMapper;

    @Autowired
    public OrderService(OrderDao orderDao, UserDao userDao, TripDao tripDao, OrderMapper orderMapper) {
        super(userDao);
        this.orderDao = orderDao;
        this.tripDao = tripDao;
        this.orderMapper = orderMapper;
    }

    public DtoOrderResponse makeOrder(String sessionId, DtoOrderRequest dtoOrderRequest) throws ApplicationException {
        Client client = getClientBySession(sessionId);
        Trip trip = tripDao.getTripById(dtoOrderRequest.getTripId());
        if (trip == null) {
            Map<String, Object> params = new HashMap<>();
            params.put("TRIP_ID", dtoOrderRequest.getTripId());
            throw new ApplicationException(ApplicationErrorDetails.TRIP_ID_NOT_FOUND, params, HttpStatus.BAD_REQUEST);
        }

        if (!trip.isApproved()) {
            Map<String, Object> params = new HashMap<>();
            params.put("TRIP_ID", dtoOrderRequest.getTripId());
            throw new ApplicationException(ApplicationErrorDetails.TRIP_IS_NOT_APPROVED, params, HttpStatus.CONFLICT);
        }

        Order order = orderMapper.toModel(dtoOrderRequest, trip, client);
        TripDate tripDate = getTripDateIfPresents(trip, order);
        if (tripDate == null) {
            Map<String, Object> params = new HashMap<>();
            params.put("TRIP_DATE", dtoOrderRequest.getTripId());
            throw new ApplicationException(ApplicationErrorDetails.TRIP_DATE_NOT_FOUND, params, HttpStatus.BAD_REQUEST);
        }

        if (tripDate.getFreePassengerSeatsCounter() < order.getPassengers().size()) {
            throw new ApplicationException(ApplicationErrorDetails.NOT_ENOUGH_FREE_PASSENGER_SEATS, HttpStatus.CONFLICT);
        }

        BigDecimal totalPrice = trip.getPrice().multiply(BigDecimal.valueOf(order.getPassengers().size()));
        order.setTotalPrice(totalPrice);
        order.setTripDate(tripDate);
        orderDao.addNewOrder(order, tripDate);

        return orderMapper.toDto(order);
    }

    public void deleteOrder(String sessionId, long orderId) throws ApplicationException {
        User user = userDao.getUserBySessionId(sessionId);
        if (user == null) {
            Map<String, Object> params = new HashMap<>();
            params.put("SESSION_ID", sessionId);
            throw new ApplicationException(ApplicationErrorDetails.SESSION_ID_NOT_FOUND, params, HttpStatus.NOT_FOUND);
        }

        Long clientIdOfOrder = orderDao.getClientIdOfOrder(orderId);
        if (clientIdOfOrder == null) {
            Map<String, Object> params = new HashMap<>();
            params.put("ORDER_ID", orderId);
            throw new ApplicationException(ApplicationErrorDetails.ORDER_ID_NOT_FOUND, params, HttpStatus.NOT_FOUND);
        }

        if (clientIdOfOrder != user.getId()) {
            Map<String, Object> params = new HashMap<>();
            params.put("ORDER_ID", orderId);
            throw new ApplicationException(ApplicationErrorDetails.ORDER_ID_NOT_FOUND, params, HttpStatus.NOT_FOUND);
        }

        orderDao.deleteOrder(orderId);
    }

    private TripDate getTripDateIfPresents(Trip trip, Order order) {
        for (TripDate tripDate : trip.getTripDates()) {
            if (tripDate.getDate().isEqual(order.getTripDate().getDate())) {
                return tripDate;
            }
        }

        return null;
    }

    public List<DtoOrderResponse> getOrdersByParams(String sessionId, Map<String, String> params) throws ApplicationException {
        checkIfSessionIsValid(sessionId);
        checkParams(params);
        List<Order> orders = orderDao.getOrdersByParams(params);

        return orderMapper.toDtoList(orders);
    }

    private void checkParams(Map<String, String> params) throws ApplicationException {
        String param = params.get("fromDate");
        if (param != null) {
            Pattern pattern = Pattern.compile("^\\d{4}-((0[1-9])|(1[0-2]))-([012][0-9]|3[0-1])$");
            Matcher matcher = pattern.matcher(param);
            if (!matcher.matches()) {
                throw new ApplicationException(ApplicationErrorDetails.VIOLATION_DATE_PATTERN_OR_EMPTY_CONSTRAINT, HttpStatus.BAD_REQUEST);
            }
        }

        param = params.get("toDate");
        if (param != null) {
            Pattern pattern = Pattern.compile("^\\d{4}-((0[1-9])|(1[0-2]))-([012][0-9]|3[0-1])$");
            Matcher matcher = pattern.matcher(param);
            if (!matcher.matches()) {
                throw new ApplicationException(ApplicationErrorDetails.VIOLATION_DATE_PATTERN_OR_EMPTY_CONSTRAINT, HttpStatus.BAD_REQUEST);
            }
        }
    }
}
