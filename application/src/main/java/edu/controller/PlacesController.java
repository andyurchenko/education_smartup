package edu.controller;

import edu.dto.place.DtoPlaceRequest;
import edu.dto.place.DtoPlaceResponse;
import edu.error.ApplicationException;
import edu.service.PlaceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PlacesController {
    private final PlaceService placeService;

    @Autowired
    public PlacesController(PlaceService placeService) {
        this.placeService = placeService;
    }

    @RequestMapping(
            path = "/api/places/{ORDER_ID}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<String> getFreePassengerSeats(
            @CookieValue(name = "${java_session_id_name}", required = false) String sessionId,
            @PathVariable("ORDER_ID") long orderId
    ) throws ApplicationException {
        return placeService.gerFreePassengerSeats(sessionId, orderId);
    }

    @RequestMapping(
            path = "/api/places",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public DtoPlaceResponse bookSeat(
            @CookieValue(name = "${java_session_id_name}", required = false) String sessionId,
            @Valid @RequestBody DtoPlaceRequest dtoPlaceRequest
    ) throws ApplicationException {
        return placeService.bookSeat(sessionId, dtoPlaceRequest);
    }
}
