package edu.dao.repository;

import edu.model.additional.TripDate;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface PassengerSeatsRepository {
    Integer decrementPassengersSeatsCounter(@Param("TRIP_DATE_ID") long tripDateId, @Param("SEATS_TO_BOOK") int seatsNumber);
    Integer incrementPassengersSeatsCounter(@Param("TRIP_DATE_ID") long tripDateId, @Param("SEATS_TO_FREE") int seatsNumber);
    Integer bookPassengersSeatInBus(@Param("ORDER_ID") long orderId, @Param("PASSENGER_ID") long passengerId, @Param("SEAT_POSITION_TO_BOOK") int seatPosition);
    void insertCountOfSeatsInBus(@Param("TRIP_DATES") List<TripDate> tripDates, @Param("BUS_PASSENGER_SEATS_NUMBER") int seatsNumber);
    void insertPassengerSeatsPositionNumberInBus(@Param("map") Map<Long, List<Integer>> positions);
    List<Integer> selectFreePassengerSeatsByOrderId(@Param("ORDER_ID") long orderId);
    Integer selectPassengerSeatNumberByPassengerId(@Param("ORDER_ID") long orderId, @Param("PASSENGER_ID") long passengerId);
    void freePassengerSeatTakenBefore(@Param("ORDER_ID") long orderId, @Param("PASSENGER_ID") long passengerId, @Param("SEAT_POSITION_TO_FREE") int seatNumber);
}
