package edu.dao;

import edu.error.ApplicationException;

import java.util.List;

public interface PlaceDao {
    List<Integer> gerFreePassengerSeatsByOrderId(long orderId) throws ApplicationException;
    Integer getPassengerSeatNumberByPassengerId(long orderId, long passengerId) throws ApplicationException;
    Integer bookPassengerSeat(long orderId, long passengerId, int seatNumber) throws ApplicationException;
}
