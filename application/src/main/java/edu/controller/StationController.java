package edu.controller;

import edu.dto.trip.station.DtoStationRequest;
import edu.dto.trip.station.DtoStationResponse;
import edu.error.ApplicationException;
import edu.service.StationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class StationController {
    private final StationService stationService;

    @Autowired
    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @RequestMapping(
            path = "/api/stations",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public DtoStationResponse createNewStation(@CookieValue(name = "${java_session_id_name}", required = false) String sessionId, @Valid @RequestBody DtoStationRequest dtoRequest) throws ApplicationException {
        return stationService.addNewStation(sessionId, dtoRequest);
    }

    @RequestMapping(
            path = "/api/stations/{ID}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public DtoStationResponse getStationById(@CookieValue(name = "${java_session_id_name}", required = false) String sessionId, @PathVariable("ID") long stationId) throws ApplicationException {
        return stationService.getStationById(sessionId, stationId);
    }

    @RequestMapping(
            path = "/api/stations/{ID}",
            method = RequestMethod.DELETE,
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.ALL_VALUE
    )
    public String deleteStationById(@CookieValue(name = "${java_session_id_name}", required = false) String sessionId, @PathVariable("ID") long stationId) throws ApplicationException {
        stationService.deleteStationById(sessionId, stationId);

        return "{}";
    }

    @RequestMapping(
            path = "/api/stations/{ID}",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public DtoStationResponse updateStation(@CookieValue(name = "${java_session_id_name}", required = false) String sessionId, @Valid @RequestBody DtoStationRequest dtoRequest, @PathVariable("ID") long stationId) throws ApplicationException {
        return stationService.updateStation(sessionId, stationId, dtoRequest);
    }
}
