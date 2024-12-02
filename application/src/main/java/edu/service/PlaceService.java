package edu.service;

import edu.configuration.ApplicationConfig;
import edu.dao.OrderDao;
import edu.dao.PassengerDao;
import edu.dao.PlaceDao;
import edu.dao.UserDao;
import edu.dto.place.DtoPlaceRequest;
import edu.dto.place.DtoPlaceResponse;
import edu.error.ApplicationErrorDetails;
import edu.error.ApplicationException;
import edu.model.Client;
import edu.model.User;
import edu.model.additional.Passenger;
import edu.util.mapper.PassengerMapper;
import edu.util.mapper.PlaceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PlaceService extends ServiceBase {
    private final PlaceDao placeDao;
    private final PassengerDao passengerDao;
    private final OrderDao orderDao;
    private final PassengerMapper passengerMapper;
    private final PlaceMapper placeMapper;
    private final String BUS_PLACE_PATTERN;
    private final String TICKET_PATTERN;

    @Autowired
    public PlaceService(UserDao userDao, PlaceDao placeDao, PassengerDao passengerDao, OrderDao orderDao, PassengerMapper passengerMapper, PlaceMapper placeMapper, ApplicationConfig applicationConfig) {
        super(userDao);
        this.placeDao = placeDao;
        this.passengerDao = passengerDao;
        this.orderDao = orderDao;
        this.passengerMapper = passengerMapper;
        this.placeMapper = placeMapper;
        BUS_PLACE_PATTERN = applicationConfig.getBusPlacePattern();
        TICKET_PATTERN = applicationConfig.getTicketPattern();
    }

    public List<String> gerFreePassengerSeats(String sessionId, long orderId) throws ApplicationException {
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

        if (user instanceof Client && user.getId() != clientIdOfOrder) {
            Map<String, Object> params = new HashMap<>();
            params.put("ORDER_ID", orderId);
            throw new ApplicationException(ApplicationErrorDetails.ORDER_ID_NOT_FOUND, params, HttpStatus.NOT_FOUND);
        }

        List<Integer> list = placeDao.gerFreePassengerSeatsByOrderId(orderId);
        return list.stream().map(integer -> BUS_PLACE_PATTERN + integer).toList();
    }

    public DtoPlaceResponse bookSeat(String sessionId, DtoPlaceRequest dtoPlaceRequest) throws ApplicationException {
        User user = userDao.getUserBySessionId(sessionId);
        if (user == null) {
            Map<String, Object> params = new HashMap<>();
            params.put("SESSION_ID", sessionId);
            throw new ApplicationException(ApplicationErrorDetails.SESSION_ID_NOT_FOUND, params, HttpStatus.NOT_FOUND);
        }

        Long clientIdOfOrder = orderDao.getClientIdOfOrder(dtoPlaceRequest.getOrderId());
        if (clientIdOfOrder == null) {
            Map<String, Object> params = new HashMap<>();
            params.put("ORDER_ID", dtoPlaceRequest.getOrderId());
            throw new ApplicationException(ApplicationErrorDetails.ORDER_ID_NOT_FOUND, params, HttpStatus.NOT_FOUND);
        }

        Passenger passenger = passengerDao.getPassengerByFullNameAndPassportAndOrderId(
                dtoPlaceRequest.getFirstName(),
                dtoPlaceRequest.getLastName(),
                dtoPlaceRequest.getPassport(),
                dtoPlaceRequest.getOrderId()
        );

        if (passenger == null) {
            throw new ApplicationException(ApplicationErrorDetails.PASSENGER_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        Integer seatNumberIfTickedBefore = placeDao.getPassengerSeatNumberByPassengerId(
                dtoPlaceRequest.getOrderId(),
                passenger.getId()
        );

        if (seatNumberIfTickedBefore != null && seatNumberIfTickedBefore.equals(dtoPlaceRequest.getPlace())) {
            throw new ApplicationException(ApplicationErrorDetails.TRYING_TO_BOOK_SAME_SEAT, HttpStatus.CONFLICT);
        }

        Integer resultOfBooking = placeDao.bookPassengerSeat(
                dtoPlaceRequest.getOrderId(),
                passenger.getId(),
                dtoPlaceRequest.getPlace()
        );

        if (resultOfBooking == 0) {
            throw new ApplicationException(ApplicationErrorDetails.SEAT_NUMBER_TAKEN_ALREADY, HttpStatus.CONFLICT);
        }

        Long tripDateId = orderDao.getTripDateIdOfOrderByOrderId(dtoPlaceRequest.getOrderId());

        return placeMapper.toDtoResponse(
                dtoPlaceRequest,
                generateTicket(tripDateId, dtoPlaceRequest.getPlace())
        );
    }

    private String generateTicket(Long tripDateId, int seatNumber) {
        return String.format(TICKET_PATTERN, tripDateId, seatNumber);
    }
}
