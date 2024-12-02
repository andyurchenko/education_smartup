package edu.dao.repository;

import edu.model.Trip;
import edu.model.additional.Schedule;
import edu.model.additional.TripDate;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface TripRepository {
    Trip selectTripById(@Param("TRIP_ID") long tripId);
    List<Trip> selectTripsByParams(@Param("SEARCH_PARAMS") Map<String, String> params);
    void insertTrip(@Param("TRIP") Trip trip);
    void insertTripDates(@Param("TRIP_ID") long tripId, @Param ("list") List<TripDate> tripDates);
    void deleteTripById(@Param("TRIP_ID") long tripId);
    void insertTripSchedule(@Param("TRIP_ID") long tripId, @Param("SCHEDULE") Schedule schedule);
    Schedule selectTripScheduleByTripId(@Param("TRIP_ID") long tripId);
    List<LocalDate> selectTripDatesByTripId(@Param("TRIP_ID") long tripId);
    void deleteTripDatesByTripId(@Param("TRIP_ID") long tripId);
    void deleteTripScheduleByTripId(@Param("TRIP_ID") long tripId);
    void updateTrip(@Param("TRIP") Trip trip);
    void updateTripSetApproved(@Param("TRIP_ID") long tripId, @Param("APPROVED_VALUE") boolean approvedValue);
    Integer getFreeSeatsCounterByTripIdAndDate(@Param("TRIP_DATE_ID") long tripId);

}
