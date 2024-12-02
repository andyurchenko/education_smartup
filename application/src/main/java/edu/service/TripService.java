package edu.service;

import edu.dao.BusDao;
import edu.dao.StationDao;
import edu.dao.TripDao;
import edu.dao.UserDao;
import edu.dto.trip.DtoTripRequest;
import edu.dto.trip.DtoTripResponse;
import edu.error.ApplicationErrorDetails;
import edu.error.ApplicationException;
import edu.model.*;
import edu.model.additional.Schedule;
import edu.model.additional.Station;
import edu.model.additional.TripDate;
import edu.util.mapper.ScheduleMapper;
import edu.util.mapper.TripMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TripService extends ServiceBase {
    private final TripDao tripDao;
    private final BusDao busDao;
    private final StationDao stationDao;
    private final ScheduleMapper scheduleMapper;
    private final TripMapper tripMapper;

    @Autowired
    public TripService(TripDao tripDao, UserDao userDao, BusDao busDao, StationDao stationDao, ScheduleMapper scheduleMapper, TripMapper tripMapper) {
        super(userDao);
        this.tripDao = tripDao;
        this.busDao = busDao;
        this.stationDao = stationDao;
        this.scheduleMapper = scheduleMapper;
        this.tripMapper = tripMapper;
    }

    public DtoTripResponse addNewTrip(String sessionId, DtoTripRequest dtoRequest) throws ApplicationException {
        checkIfSessionIdBelongsToAdministrator(sessionId);
        Trip trip = tripMapper.toModel(dtoRequest);
        Bus bus = busDao.getBusByBusBrandName(dtoRequest.getBusName());
        trip.setBus(bus);
        Station fromStation = stationDao.getStationByName(dtoRequest.getFromStation());
        trip.setFromStation(fromStation);
        Station toStation = stationDao.getStationByName(dtoRequest.getToStation());
        trip.setToStation(toStation);

        if (trip.getSchedule() != null) {
            trip.setTripDates(
                    createTripDatesFromSchedule(trip.getSchedule(), trip)
            );
            tripDao.addNewTrip(trip);

            return tripMapper.toDtoForAdmins(trip);
        }

        if (dtoRequest.getDates() != null) {
            trip.setTripDates(
                    createTripDatesFromStringList(dtoRequest.getDates(), trip)
            );
            tripDao.addNewTrip(trip);

            return tripMapper.toDtoForAdmins(trip);
        }

        throw new ApplicationException(ApplicationErrorDetails.NO_SCHEDULE_INFO_IN_REQUEST, HttpStatus.BAD_REQUEST);
    }

    public void deleteTripById(String sessionId, long tripId) throws ApplicationException {
        checkIfSessionIdBelongsToAdministrator(sessionId);
        tripDao.deleteTripById(tripId);
    }

    public DtoTripResponse getTripById(String sessionId, long tripId) throws ApplicationException {
        checkIfSessionIdBelongsToAdministrator(sessionId);
        Trip trip = tripDao.getTripById(tripId);
        if (trip == null) {
            Map<String, Object> params = new HashMap<>();
            params.put("TRIP_ID", tripId);
            throw new ApplicationException(ApplicationErrorDetails.TRIP_ID_NOT_FOUND, params, HttpStatus.BAD_REQUEST);
        }
        trip.setSchedule(tripDao.getTripScheduleByTripId(tripId));

        return tripMapper.toDtoForAdmins(trip);
    }

    public List<DtoTripResponse> getTripsByParams(String sessionId, Map<String, String> params) throws ApplicationException {
        checkIfSessionIsValid(sessionId);
        checkParams(params);
        User user = userDao.getUserBySessionId(sessionId);

        List<Trip> trips;
        if (user instanceof Client) {
            params.put("approved", "yes");
            trips = tripDao.getTrips(params);
            return tripMapper.toDtoListForClients(trips);
        } else {
            trips = tripDao.getTrips(params);
            return tripMapper.toDtoListForAdmins(trips);
        }
    }

    public DtoTripResponse updateTripById(String sessionId, long tripId, DtoTripRequest dtoRequest) throws ApplicationException {
        checkIfSessionIdBelongsToAdministrator(sessionId);
        Trip trip = tripDao.getTripById(tripId);
        if (trip == null) {
            Map<String, Object> params = new HashMap<>();
            params.put("TRIP_ID", tripId);
            throw new ApplicationException(ApplicationErrorDetails.TRIP_ID_NOT_FOUND, params, HttpStatus.BAD_REQUEST);
        }
        tripMapper.updateModelFromDto(trip, dtoRequest);

        Schedule schedule = scheduleMapper.toModel(dtoRequest.getSchedule());
        if (isScheduleAndDatesBothPresentOrNull(schedule, dtoRequest.getDates())) {
            throw new ApplicationException(ApplicationErrorDetails.VIOLATION_SCHEDULE_AND_DATES_TOGETHER_PRESENT_OR_EMPTY_CONSTRAINT, HttpStatus.BAD_REQUEST);
        }
        if (schedule != null) {
            trip.setTripDates(
                    createTripDatesFromSchedule(schedule, trip)
            );
            trip.getSchedule().setFromDate(
                    schedule.getFromDate()
            );
            trip.getSchedule().setToDate(
                    schedule.getToDate()
            );
            trip.getSchedule().setPeriod(
                    schedule.getPeriod()
            );
        }
        if (dtoRequest.getDates() != null) {
            trip.setTripDates(
                    createTripDatesFromStringList(dtoRequest.getDates(), trip)
            );
        }

        Bus bus = busDao.getBusByBusBrandName(dtoRequest.getBusName());
        if (bus == null) {
            Map<String, Object> params = new HashMap<>();
            params.put("BUS_NAME", dtoRequest.getBusName());
            throw new ApplicationException(ApplicationErrorDetails.BUS_NAME_NOT_FOUND, params, HttpStatus.BAD_REQUEST);
        }
        trip.setBus(bus);

        Station fromStation = stationDao.getStationByName(dtoRequest.getFromStation());
        if (fromStation == null) {
            Map<String, Object> params = new HashMap<>();
            params.put("FROM_STATION_NAME", dtoRequest.getFromStation());
            throw new ApplicationException(ApplicationErrorDetails.FROM_STATION_NAME_NOT_FOUND, params, HttpStatus.BAD_REQUEST);
        }
        trip.setFromStation(fromStation);

        Station toStation = stationDao.getStationByName(dtoRequest.getToStation());
        if (toStation == null) {
            Map<String, Object> params = new HashMap<>();
            params.put("TO_STATION_NAME", dtoRequest.getToStation());
            throw new ApplicationException(ApplicationErrorDetails.TO_STATION_NAME_NOT_FOUND, params, HttpStatus.BAD_REQUEST);
        }
        trip.setToStation(toStation);

        tripDao.updateTrip(trip);

        return tripMapper.toDtoForAdmins(trip);
    }

    public void setTripApproved(String sessionId, long tripId, boolean approvedValue) throws ApplicationException {
        checkIfSessionIdBelongsToAdministrator(sessionId);
        tripDao.updateTripSetApproved(tripId, approvedValue);
    }

    private List<TripDate> createTripDatesFromSchedule(Schedule schedule, Trip trip) throws ApplicationException {
        String period = schedule.getPeriod();
        LocalDate fromDate = schedule.getFromDate();
        LocalDate toDate = schedule.getToDate();
        toDate = toDate.plusDays(1);


        if (toDate.isBefore(fromDate)) {
            throw new ApplicationException(ApplicationErrorDetails.TO_DATE_BEFORE_FROM_DATE_IN_SCHEDULE, HttpStatus.BAD_REQUEST);
        }

        period = period.replaceAll("\\s", "").toLowerCase();

        if (period.equals("daily")) {
            List<LocalDate> datesBetween =  fromDate.datesUntil(toDate).toList();

            return createTripDates(datesBetween, trip);
        }

        if (period.equals("even")) {
            List<LocalDate> datesBetween = new ArrayList<>(fromDate.datesUntil(toDate).toList());
            datesBetween.removeIf(item -> (item.getDayOfMonth() % 2) != 0);

            return createTripDates(datesBetween, trip);
        }

        if (period.equals("odd")) {
            List<LocalDate> datesBetween = new ArrayList<>(fromDate.datesUntil(toDate).toList());
            datesBetween.removeIf(item -> (item.getDayOfMonth() % 2) == 0);

            return createTripDates(datesBetween, trip);
        }

        if (period.matches("^(([1-2][0-9])|(3[0-1])|(0[1-9])|([1-9](?!\\d))|,)+$")) {
            List<Integer> dOfm = new ArrayList<>(Arrays.stream(period.split(","))
                    .map(Integer::valueOf)
                    .toList()
            );

            if (dOfm.size() != dOfm.stream().distinct().toList().size()) {
                throw new ApplicationException(ApplicationErrorDetails.VIOLATION_PERIOD_EMPTY_OR_PATTERN_CONSTRAINT, HttpStatus.BAD_REQUEST);
            }

            List<LocalDate> datesBetween = new ArrayList<>(fromDate.datesUntil(toDate).toList());
            datesBetween.removeIf(
                    (date) -> !dOfm.contains(date.getDayOfMonth())

            );

            return createTripDates(datesBetween, trip);
        }

        if (period.matches("^((sun(?=,|\\s))|(mon(?=,|\\s))|(tue(?=,|\\s))|(wed(?=,|\\s))|(thu(?=,|\\s))|(fri(?=,|\\s))|(sat(?=,|\\s))|,)+(sun|mon|tue|wed|thu|fri|sat)$")) {
            period = period.replaceAll("mon", "MONDAY");
            period = period.replaceAll("tue", "TUESDAY");
            period = period.replaceAll("wed", "WEDNESDAY");
            period = period.replaceAll("thu", "THURSDAY");
            period = period.replaceAll("fri", "FRIDAY");
            period = period.replaceAll("sun", "SUNDAY");
            period = period.replaceAll("sat", "SATURDAY");
            List<String> dOfWeek = new ArrayList<>(
                    Arrays.stream(period.split(","))
                            .toList()
            );

            if (dOfWeek.size() != dOfWeek.stream().distinct().toList().size()) {
                throw new ApplicationException(ApplicationErrorDetails.VIOLATION_PERIOD_EMPTY_OR_PATTERN_CONSTRAINT, HttpStatus.BAD_REQUEST);
            }

            List<LocalDate> datesBetween = new ArrayList<>(fromDate.datesUntil(toDate).toList());
            datesBetween.removeIf(
                    date -> {
                        for (String d : dOfWeek) {
                            if (date.getDayOfWeek() == DayOfWeek.valueOf(d)) {
                                return false;
                            }
                        }
                        return true;
                    }
            );

            return createTripDates(datesBetween, trip);
        }

        throw new ApplicationException(ApplicationErrorDetails.WRONG_SCHEDULE_INFO_IN_REQUEST, HttpStatus.BAD_REQUEST);
    }

    private List<TripDate> createTripDatesFromStringList(List<String> datesFromDto, Trip trip) throws ApplicationException {
        List<LocalDate> datesBetween;
        if (datesFromDto.isEmpty()) {
            throw new ApplicationException(ApplicationErrorDetails.VIOLATION_DATE_PATTERN_OR_EMPTY_CONSTRAINT, HttpStatus.BAD_REQUEST);
        }
        try {
            datesBetween = datesFromDto.stream().map((LocalDate::parse)).toList();
        } catch (RuntimeException e) {
            throw new ApplicationException(ApplicationErrorDetails.VIOLATION_DATE_PATTERN_OR_EMPTY_CONSTRAINT, HttpStatus.BAD_REQUEST);
        }

        return createTripDates(datesBetween, trip);
    }

    private boolean isScheduleAndDatesBothPresentOrNull(Schedule schedule, List<String> dates) {
        return (schedule != null && dates != null) || (schedule == null && dates == null);
    }

    private List<TripDate> createTripDates(List<LocalDate> localDates, Trip trip) {
        List<TripDate> tripDates = new ArrayList<>();
        for (LocalDate date : localDates) {
            tripDates.add(new TripDate(date, trip));
        }

        return tripDates;
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
