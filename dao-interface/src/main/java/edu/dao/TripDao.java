package edu.dao;

import edu.error.ApplicationException;
import edu.model.Trip;
import edu.model.additional.Schedule;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface TripDao {
    void addNewTrip(Trip trip) throws ApplicationException;
    void deleteTripById(long tripId) throws ApplicationException;
    Trip getTripById(long tripId) throws ApplicationException;
    List<Trip> getTrips(Map<String, String> params) throws ApplicationException;
    Schedule getTripScheduleByTripId(long tripId) throws ApplicationException;
    List<LocalDate> getTripDatesByTripId(long tripId) throws ApplicationException;
    void deleteTripScheduleByTripId(long tripId) throws ApplicationException;
    void deleteTripDatesByTripId(long tripId) throws ApplicationException;
    void updateTrip(Trip trip) throws ApplicationException;
    void updateTripSetApproved(long tripId, boolean approvedValue) throws ApplicationException;
    Integer getFreeSeatsCountOfTrip(long tripId, LocalDate date) throws ApplicationException;

}
