package edu.controller;

import edu.dto.trip.bus.DtoBusRequest;
import edu.dto.trip.bus.DtoBusResponse;
import edu.dto.trip.bus.DtoBusWithIdResponse;
import edu.error.ApplicationException;
import edu.service.BusService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BusController {
    private final BusService busService;

    @Autowired
    public BusController(BusService busService) {
        this.busService = busService;
    }

    @RequestMapping(
            path = "/api/buses",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public DtoBusWithIdResponse addNewBus(@CookieValue(name = "${java_session_id_name}", required = false) String sessionId, @Valid @RequestBody DtoBusRequest dtoRequest) throws ApplicationException {
        return busService.addNewBus(sessionId, dtoRequest);
    }

    @RequestMapping(
            path = "/api/buses",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<DtoBusResponse> getAllBuses(@CookieValue(name = "${java_session_id_name}", required = false) String sessionId) throws ApplicationException {
        return busService.getAllBuses(sessionId);
    }
}
