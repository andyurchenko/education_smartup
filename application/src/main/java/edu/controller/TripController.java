package edu.controller;

import edu.dto.trip.DtoTripRequest;
import edu.dto.trip.DtoTripResponse;
import edu.error.ApplicationException;
import edu.service.TripService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class TripController {
    private final TripService tripService;

    @Autowired
    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @RequestMapping (
            path = "/api/trips",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public DtoTripResponse addNewTrip(@CookieValue(name = "${java_session_id_name}", required = false) String sessionId, @Valid @RequestBody DtoTripRequest dtoRequest) throws ApplicationException {
        return tripService.addNewTrip(sessionId, dtoRequest);
    }

    @RequestMapping(
            path = "/api/trips/{TRIP_ID}",
            method = RequestMethod.DELETE,
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public String deleteTripById(@CookieValue(name = "${java_session_id_name}", required = false) String sessionId, @PathVariable("TRIP_ID") long tripId) throws ApplicationException {
        tripService.deleteTripById(sessionId, tripId);

        return "{}";
    }

    @RequestMapping(
            path = "/api/trips/{TRIP_ID}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public DtoTripResponse getTripById(@CookieValue(name = "${java_session_id_name}", required = false) String sessionId, @PathVariable("TRIP_ID") long tripId) throws ApplicationException {
        return tripService.getTripById(sessionId, tripId);
    }

    @RequestMapping(
            path = "/api/trips",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<DtoTripResponse> getTripsByParams(@CookieValue(name = "${java_session_id_name}", required = false) String sessionId, @RequestParam Map<String, String> params) throws ApplicationException {
        return tripService.getTripsByParams(sessionId, params);
    }

    @RequestMapping(
            path = "/api/trips/{TRIP_ID}",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public DtoTripResponse updateTripById(@CookieValue(name = "${java_session_id_name}", required = false) String sessionId, @PathVariable("TRIP_ID") long tripId, @Valid @RequestBody DtoTripRequest dtoTripRequest) throws ApplicationException {
        return tripService.updateTripById(sessionId, tripId, dtoTripRequest);
    }

    @RequestMapping(
            path = "/api/trips/{TRIP_ID}/approve",
            method = RequestMethod.PUT,
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.ALL_VALUE
    )
    public String approveTrip(@CookieValue(name = "${java_session_id_name}", required = false) String sessionId, @PathVariable("TRIP_ID") long tripId) throws ApplicationException {
        tripService.setTripApproved(sessionId, tripId, true);

        return "{}";
    }

}
