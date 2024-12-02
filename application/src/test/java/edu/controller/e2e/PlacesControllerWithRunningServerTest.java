package edu.controller.e2e;

import edu.configuration.ApplicationConfig;
import edu.dto.error.DtoErrorResponse;
import edu.dto.error.DtoErrorResponseElement;
import edu.dto.order.DtoOrderRequest;
import edu.dto.order.DtoPassenger;
import edu.dto.place.DtoPlaceRequest;
import edu.dto.place.DtoPlaceResponse;
import edu.dto.trip.DtoSchedule;
import edu.dto.trip.DtoTripRequest;
import edu.dto.trip.bus.DtoBusRequest;
import edu.error.ApplicationErrorDetails;
import edu.util.PlaceTestData;
import org.apache.commons.text.StringSubstitutor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PlacesControllerWithRunningServerTest extends BaseTest {
    private final String FROM_STATION_NAME = "station_name_ONE";
    private final String TO_STATION_NAME = "station_name_TWO";
    private final String BUS_NAME_ONE = "bus_brand_name_ONE";
    private final String PASSENGER_FIRST_NAME_1 = "firstNamePassenger_1";
    private final String PASSENGER_LAST_NAME_1 = "lastNamePassenger_1";
    private final String PASSENGER_PASSPORT_1 = "passportPassenger_1";
    private final int PASSENGER_SEAT_1 = 1;
    private final String PASSENGER_FIRST_NAME_2 = "firstNamePassenger_2";
    private final String PASSENGER_LAST_NAME_2 = "lastNamePassenger_2";
    private final String PASSENGER_PASSPORT_2 = "passportPassenger_2";
    private final int PLACE_COUNT_OF_BUS_NAME_ONE = 3;
    private String ADMIN_SESSION_ID;
    private String FIRST_CLIENT_SESSION_ID;
    private String SECOND_CLIENT_SESSION_ID;
    private final String BUS_PLACE_PATTERN;
    private Long TRIP_ID;
    private Long ORDER_ID;
    @Autowired
    public PlacesControllerWithRunningServerTest(@LocalServerPort Integer port, TestRestTemplate testRestTemplate, ApplicationConfig appConfig) {
        super(port, testRestTemplate, appConfig);
        BUS_PLACE_PATTERN = appConfig.getBusPlacePattern();
    }

    @BeforeEach
    void clearDataBaseAndAddTestData() {
        testRestTemplate
                .exchange(
                        BASE_URL + PORT + DEBUG_URL,
                        HttpMethod.DELETE,
                        null,
                        String.class
                );

        ADMIN_SESSION_ID = addNewAdminAndGetItsSession();
        FIRST_CLIENT_SESSION_ID = addNewClientAndGetItsSession("один");
        SECOND_CLIENT_SESSION_ID = addNewClientAndGetItsSession("два");

        addNewNewStationToDb(FROM_STATION_NAME, ADMIN_SESSION_ID);
        addNewNewStationToDb(TO_STATION_NAME, ADMIN_SESSION_ID);
        addNewBusToDb(new DtoBusRequest(BUS_NAME_ONE, PLACE_COUNT_OF_BUS_NAME_ONE), ADMIN_SESSION_ID);
        DtoTripRequest dtoTripRequest = new DtoTripRequest();
        dtoTripRequest.setBusName(BUS_NAME_ONE);
        dtoTripRequest.setPrice("10.00");
        dtoTripRequest.setFromStation(FROM_STATION_NAME);
        dtoTripRequest.setToStation(TO_STATION_NAME);
        dtoTripRequest.setStart("12:00");
        dtoTripRequest.setDuration("05:00");
        dtoTripRequest.setSchedule(new DtoSchedule("2000-01-01", "2000-01-10", "daily"));
        TRIP_ID = addNewTripAndGetItId(dtoTripRequest, ADMIN_SESSION_ID);
        approveTripById(TRIP_ID, ADMIN_SESSION_ID);

        DtoOrderRequest dtoOrderRequest = new DtoOrderRequest();
        dtoOrderRequest.setTripId(TRIP_ID);
        dtoOrderRequest.setDate("2000-01-01");
        List<DtoPassenger> passengerList = new ArrayList<>();
        passengerList.add(new DtoPassenger(PASSENGER_FIRST_NAME_1, PASSENGER_LAST_NAME_1, PASSENGER_PASSPORT_1));
        passengerList.add(new DtoPassenger(PASSENGER_FIRST_NAME_2, PASSENGER_LAST_NAME_2, PASSENGER_PASSPORT_2));
        dtoOrderRequest.setPassengers(passengerList);
        ORDER_ID = addNewOrderToTripAndGetItId(dtoOrderRequest, FIRST_CLIENT_SESSION_ID);
    }

    @Test
    void getFreePassengerPositive() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.COOKIE, createHeaderForSessionId(FIRST_CLIENT_SESSION_ID));
        ResponseEntity<List<String>> errorEntityResponse = testRestTemplate.exchange(
                BASE_URL + PORT + API_PLACES_URL + "/" + ORDER_ID,
                HttpMethod.GET,
                new HttpEntity<>(null, httpHeaders),
                new ParameterizedTypeReference<>() {}
        );

        assertEquals(HttpStatus.OK, errorEntityResponse.getStatusCode());
        assertNotNull(errorEntityResponse);
        List<String> placeList = errorEntityResponse.getBody();
        assertNotNull(placeList);
        assertEquals(3, placeList.size());
    }
    @Test
    void getFreePassengerSeatsWithWrongSessionIdNegative() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.COOKIE, createHeaderForSessionId("NOT_EXISTING_SESSION_ID"));
        ResponseEntity<DtoErrorResponse> errorEntityResponse = testRestTemplate.exchange(
                BASE_URL + PORT + API_PLACES_URL + "/" + ORDER_ID,
                HttpMethod.GET,
                new HttpEntity<>(null, httpHeaders),
                DtoErrorResponse.class
        );

        assertEquals(HttpStatus.NOT_FOUND, errorEntityResponse.getStatusCode());
        assertNotNull(errorEntityResponse);
        DtoErrorResponse dtoErrorResponseFromServer = errorEntityResponse.getBody();
        assertNotNull(dtoErrorResponseFromServer);
        assertNotNull(dtoErrorResponseFromServer.getErrors());
        assertEquals(1, dtoErrorResponseFromServer.getErrors().size());
        assertEquals(ApplicationErrorDetails.SESSION_ID_NOT_FOUND.toString(), dtoErrorResponseFromServer.getErrors().get(0).getErrorCode());
        assertEquals(ApplicationErrorDetails.SESSION_ID_NOT_FOUND.getField(), dtoErrorResponseFromServer.getErrors().get(0).getField());
        Map<String, String> params = new HashMap<>();
        params.put("SESSION_ID", "NOT_EXISTING_SESSION_ID");
        String errorMessage = StringSubstitutor.replace(ApplicationErrorDetails.SESSION_ID_NOT_FOUND.getMessage(), params);
        assertEquals(errorMessage, dtoErrorResponseFromServer.getErrors().get(0).getMessage());
    }

    @Test
    void getFreePassengerSeatsWithNotExistingOrderIdNegative() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.COOKIE, createHeaderForSessionId(FIRST_CLIENT_SESSION_ID));
        ResponseEntity<DtoErrorResponse> errorEntityResponse = testRestTemplate.exchange(
                BASE_URL + PORT + API_PLACES_URL + "/" + "0",
                HttpMethod.GET,
                new HttpEntity<>(null, httpHeaders),
                DtoErrorResponse.class
        );

        assertEquals(HttpStatus.NOT_FOUND, errorEntityResponse.getStatusCode());
        assertNotNull(errorEntityResponse);
        DtoErrorResponse dtoErrorResponseFromServer = errorEntityResponse.getBody();
        assertNotNull(dtoErrorResponseFromServer);
        assertNotNull(dtoErrorResponseFromServer.getErrors());
        assertEquals(1, dtoErrorResponseFromServer.getErrors().size());
        assertEquals(ApplicationErrorDetails.ORDER_ID_NOT_FOUND.toString(), dtoErrorResponseFromServer.getErrors().get(0).getErrorCode());
        assertEquals(ApplicationErrorDetails.ORDER_ID_NOT_FOUND.getField(), dtoErrorResponseFromServer.getErrors().get(0).getField());
        Map<String, Object> params = new HashMap<>();
        params.put("ORDER_ID", "0");
        String errorMessage = StringSubstitutor.replace(ApplicationErrorDetails.ORDER_ID_NOT_FOUND.getMessage(), params);
        assertEquals(errorMessage, dtoErrorResponseFromServer.getErrors().get(0).getMessage());
    }

    @Test
    void getFreePassengerSeatsWithOrderIdAndAnotherClientSessionIdAndNegative() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.COOKIE, createHeaderForSessionId(SECOND_CLIENT_SESSION_ID));
        ResponseEntity<DtoErrorResponse> errorEntityResponse = testRestTemplate.exchange(
                BASE_URL + PORT + API_PLACES_URL + "/" + ORDER_ID,
                HttpMethod.GET,
                new HttpEntity<>(null, httpHeaders),
                DtoErrorResponse.class
        );

        assertEquals(HttpStatus.NOT_FOUND, errorEntityResponse.getStatusCode());
        assertNotNull(errorEntityResponse);
        DtoErrorResponse dtoErrorResponseFromServer = errorEntityResponse.getBody();
        assertNotNull(dtoErrorResponseFromServer);
        assertNotNull(dtoErrorResponseFromServer.getErrors());
        assertEquals(1, dtoErrorResponseFromServer.getErrors().size());
        assertEquals(ApplicationErrorDetails.ORDER_ID_NOT_FOUND.toString(), dtoErrorResponseFromServer.getErrors().get(0).getErrorCode());
        assertEquals(ApplicationErrorDetails.ORDER_ID_NOT_FOUND.getField(), dtoErrorResponseFromServer.getErrors().get(0).getField());
        Map<String, Object> params = new HashMap<>();
        params.put("ORDER_ID", ORDER_ID);
        String errorMessage = StringSubstitutor.replace(ApplicationErrorDetails.ORDER_ID_NOT_FOUND.getMessage(), params);
        assertEquals(errorMessage, dtoErrorResponseFromServer.getErrors().get(0).getMessage());
    }

    @ParameterizedTest
    @MethodSource("generateInvalidDtoPlaceRequest")
    void bookSeatWithInvalidDtoRequestNegative(DtoPlaceRequest dtoRequest, DtoErrorResponse dtoResponse) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.COOKIE, createHeaderForSessionId(FIRST_CLIENT_SESSION_ID));
        ResponseEntity<DtoErrorResponse> responseEntity = testRestTemplate.exchange(
                BASE_URL + PORT + API_PLACES_URL,
                HttpMethod.POST,
                new HttpEntity<>(dtoRequest, httpHeaders),
                new ParameterizedTypeReference<>() {}
        );

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        DtoErrorResponse errorResponseFromServer = responseEntity.getBody();
        assertNotNull(errorResponseFromServer);
        List<DtoErrorResponseElement> errorList = errorResponseFromServer.getErrors();
        assertNotNull(errorList);
        assertEquals(1, errorList.size());
        assertEquals(dtoResponse.getErrors().get(0).getErrorCode(), errorList.get(0).getErrorCode());
        assertEquals(dtoResponse.getErrors().get(0).getField(), errorList.get(0).getField());
        assertEquals(dtoResponse.getErrors().get(0).getMessage(), errorList.get(0).getMessage());
    }

    @Test
    void bookSeatWithNotExistingSessionIdNegative() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.COOKIE, createHeaderForSessionId("NOT_EXISTING_SESSION_ID"));
        ResponseEntity<DtoErrorResponse> entityResponse = testRestTemplate.exchange(
                BASE_URL + PORT + API_PLACES_URL,
                HttpMethod.POST,
                new HttpEntity<>(PlaceTestData.createValidDtoPlaceRequest(), httpHeaders),
                new ParameterizedTypeReference<>() {}
        );

        assertEquals(HttpStatus.NOT_FOUND, entityResponse.getStatusCode());
        assertNotNull(entityResponse);
        DtoErrorResponse dtoErrorResponseFromServer = entityResponse.getBody();
        assertNotNull(dtoErrorResponseFromServer);
        assertNotNull(dtoErrorResponseFromServer.getErrors());
        assertEquals(1, dtoErrorResponseFromServer.getErrors().size());
        assertEquals(ApplicationErrorDetails.SESSION_ID_NOT_FOUND.toString(), dtoErrorResponseFromServer.getErrors().get(0).getErrorCode());
        assertEquals(ApplicationErrorDetails.SESSION_ID_NOT_FOUND.getField(), dtoErrorResponseFromServer.getErrors().get(0).getField());
        Map<String, String> params = new HashMap<>();
        params.put("SESSION_ID", "NOT_EXISTING_SESSION_ID");
        String errorMessage = StringSubstitutor.replace(ApplicationErrorDetails.SESSION_ID_NOT_FOUND.getMessage(), params);
        assertEquals(errorMessage, dtoErrorResponseFromServer.getErrors().get(0).getMessage());
    }

    @Test
    void bookSeatWithNotExistingOrderIdNegative() {
        DtoPlaceRequest dtoPlaceRequest = PlaceTestData.createValidDtoPlaceRequest();
        Long orderId = 0L;
        dtoPlaceRequest.setOrderId(orderId);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.COOKIE, createHeaderForSessionId(FIRST_CLIENT_SESSION_ID));
        ResponseEntity<DtoErrorResponse> entityResponse = testRestTemplate.exchange(
                BASE_URL + PORT + API_PLACES_URL,
                HttpMethod.POST,
                new HttpEntity<>(dtoPlaceRequest, httpHeaders),
                new ParameterizedTypeReference<>() {}
        );

        assertEquals(HttpStatus.NOT_FOUND, entityResponse.getStatusCode());
        assertNotNull(entityResponse);
        DtoErrorResponse dtoErrorResponseFromServer = entityResponse.getBody();
        assertNotNull(dtoErrorResponseFromServer);
        assertNotNull(dtoErrorResponseFromServer.getErrors());
        assertEquals(1, dtoErrorResponseFromServer.getErrors().size());
        assertEquals(ApplicationErrorDetails.ORDER_ID_NOT_FOUND.toString(), dtoErrorResponseFromServer.getErrors().get(0).getErrorCode());
        assertEquals(ApplicationErrorDetails.ORDER_ID_NOT_FOUND.getField(), dtoErrorResponseFromServer.getErrors().get(0).getField());
        Map<String, Object> params = new HashMap<>();
        params.put("ORDER_ID", orderId);
        String errorMessage = StringSubstitutor.replace(ApplicationErrorDetails.ORDER_ID_NOT_FOUND.getMessage(), params);
        assertEquals(errorMessage, dtoErrorResponseFromServer.getErrors().get(0).getMessage());
    }

    @Test
    void bookSeatWithNotExistingPassengerNegative() {
        DtoPlaceRequest dtoPlaceRequest = createDtoPlaceRequestForFirstPassenger(1);
        dtoPlaceRequest.setPassport("wrong_passport");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.COOKIE, createHeaderForSessionId(FIRST_CLIENT_SESSION_ID));
        ResponseEntity<DtoErrorResponse> entityResponse = testRestTemplate.exchange(
                BASE_URL + PORT + API_PLACES_URL,
                HttpMethod.POST,
                new HttpEntity<>(dtoPlaceRequest, httpHeaders),
                new ParameterizedTypeReference<>() {}
        );

        assertEquals(HttpStatus.NOT_FOUND, entityResponse.getStatusCode());
        assertNotNull(entityResponse);
        DtoErrorResponse dtoErrorResponseFromServer = entityResponse.getBody();
        assertNotNull(dtoErrorResponseFromServer);
        assertNotNull(dtoErrorResponseFromServer.getErrors());
        assertEquals(1, dtoErrorResponseFromServer.getErrors().size());
        assertEquals(ApplicationErrorDetails.PASSENGER_NOT_FOUND.toString(), dtoErrorResponseFromServer.getErrors().get(0).getErrorCode());
        assertEquals(ApplicationErrorDetails.PASSENGER_NOT_FOUND.getField(), dtoErrorResponseFromServer.getErrors().get(0).getField());
        assertEquals(ApplicationErrorDetails.PASSENGER_NOT_FOUND.getMessage(), dtoErrorResponseFromServer.getErrors().get(0).getMessage());
    }

    @Test
    void bookSeatPositive() {
        DtoPlaceRequest dtoPlaceRequest = createDtoPlaceRequestForFirstPassenger(PASSENGER_SEAT_1);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.COOKIE, createHeaderForSessionId(FIRST_CLIENT_SESSION_ID));
        ResponseEntity<DtoPlaceResponse> entityResponse = testRestTemplate.exchange(
                BASE_URL + PORT + API_PLACES_URL,
                HttpMethod.POST,
                new HttpEntity<>(dtoPlaceRequest, httpHeaders),
                new ParameterizedTypeReference<>() {}
        );

        assertEquals(HttpStatus.OK, entityResponse.getStatusCode());
        assertNotNull(entityResponse);
        DtoPlaceResponse dtoPlaceResponse = entityResponse.getBody();
        assertNotNull(dtoPlaceResponse);
        assertEquals(ORDER_ID, dtoPlaceResponse.getOrderId());
        assertEquals(PASSENGER_SEAT_1, dtoPlaceResponse.getPlace());
        assertEquals(PASSENGER_FIRST_NAME_1, dtoPlaceResponse.getFirstName());
        assertEquals(PASSENGER_LAST_NAME_1, dtoPlaceResponse.getLastName());
        assertEquals(PASSENGER_PASSPORT_1, dtoPlaceResponse.getPassport());
    }

    @Test
    void bookSeatWithBookingTheSameSeatNegative() {
        DtoPlaceRequest dtoPlaceRequest = createDtoPlaceRequestForFirstPassenger(PASSENGER_SEAT_1);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.COOKIE, createHeaderForSessionId(FIRST_CLIENT_SESSION_ID));
        ResponseEntity<DtoPlaceResponse> entityResponse = testRestTemplate.exchange(
                BASE_URL + PORT + API_PLACES_URL,
                HttpMethod.POST,
                new HttpEntity<>(dtoPlaceRequest, httpHeaders),
                new ParameterizedTypeReference<>() {}
        );

        assertEquals(HttpStatus.OK, entityResponse.getStatusCode());
        assertNotNull(entityResponse);
        DtoPlaceResponse dtoPlaceResponse = entityResponse.getBody();
        assertNotNull(dtoPlaceResponse);
        assertEquals(ORDER_ID, dtoPlaceResponse.getOrderId());
        assertEquals(PASSENGER_SEAT_1, dtoPlaceResponse.getPlace());
        assertEquals(PASSENGER_FIRST_NAME_1, dtoPlaceResponse.getFirstName());
        assertEquals(PASSENGER_LAST_NAME_1, dtoPlaceResponse.getLastName());
        assertEquals(PASSENGER_PASSPORT_1, dtoPlaceResponse.getPassport());

        ResponseEntity<DtoErrorResponse> responseErrorEntity = testRestTemplate.exchange(
                BASE_URL + PORT + API_PLACES_URL,
                HttpMethod.POST,
                new HttpEntity<>(dtoPlaceRequest, httpHeaders),
                new ParameterizedTypeReference<>() {}
        );
        assertEquals(HttpStatus.CONFLICT, responseErrorEntity.getStatusCode());
        assertNotNull(responseErrorEntity);
        DtoErrorResponse dtoErrorResponse = responseErrorEntity.getBody();
        assertNotNull(dtoErrorResponse);
        assertNotNull(dtoErrorResponse.getErrors());
        assertEquals(1, dtoErrorResponse.getErrors().size());
        assertEquals(ApplicationErrorDetails.TRYING_TO_BOOK_SAME_SEAT.toString(), dtoErrorResponse.getErrors().get(0).getErrorCode());
        assertEquals(ApplicationErrorDetails.TRYING_TO_BOOK_SAME_SEAT.getMessage(), dtoErrorResponse.getErrors().get(0).getMessage());
        assertEquals(ApplicationErrorDetails.TRYING_TO_BOOK_SAME_SEAT.getField(), dtoErrorResponse.getErrors().get(0).getField());
    }

    @Test
    void bookSeatWithSeatAlreadyTakenNegative() {
        DtoPlaceRequest dtoPlaceRequest = createDtoPlaceRequestForFirstPassenger(PASSENGER_SEAT_1);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.COOKIE, createHeaderForSessionId(FIRST_CLIENT_SESSION_ID));
        ResponseEntity<DtoPlaceResponse> entityResponse = testRestTemplate.exchange(
                BASE_URL + PORT + API_PLACES_URL,
                HttpMethod.POST,
                new HttpEntity<>(dtoPlaceRequest, httpHeaders),
                new ParameterizedTypeReference<>() {}
        );

        assertEquals(HttpStatus.OK, entityResponse.getStatusCode());
        assertNotNull(entityResponse);
        DtoPlaceResponse dtoPlaceResponse = entityResponse.getBody();
        assertNotNull(dtoPlaceResponse);
        assertEquals(ORDER_ID, dtoPlaceResponse.getOrderId());
        assertEquals(PASSENGER_SEAT_1, dtoPlaceResponse.getPlace());
        assertEquals(PASSENGER_FIRST_NAME_1, dtoPlaceResponse.getFirstName());
        assertEquals(PASSENGER_LAST_NAME_1, dtoPlaceResponse.getLastName());
        assertEquals(PASSENGER_PASSPORT_1, dtoPlaceResponse.getPassport());

        dtoPlaceRequest = createDtoPlaceRequestForSecondPassenger(PASSENGER_SEAT_1);
        ResponseEntity<DtoErrorResponse> responseErrorEntity = testRestTemplate.exchange(
                BASE_URL + PORT + API_PLACES_URL,
                HttpMethod.POST,
                new HttpEntity<>(dtoPlaceRequest, httpHeaders),
                new ParameterizedTypeReference<>() {}
        );
        assertEquals(HttpStatus.CONFLICT, responseErrorEntity.getStatusCode());
        assertNotNull(responseErrorEntity);
        DtoErrorResponse dtoErrorResponse = responseErrorEntity.getBody();
        assertNotNull(dtoErrorResponse);
        assertNotNull(dtoErrorResponse.getErrors());
        assertEquals(1, dtoErrorResponse.getErrors().size());
        assertEquals(ApplicationErrorDetails.SEAT_NUMBER_TAKEN_ALREADY.toString(), dtoErrorResponse.getErrors().get(0).getErrorCode());
        assertEquals(ApplicationErrorDetails.SEAT_NUMBER_TAKEN_ALREADY.getMessage(), dtoErrorResponse.getErrors().get(0).getMessage());
        assertEquals(ApplicationErrorDetails.SEAT_NUMBER_TAKEN_ALREADY.getField(), dtoErrorResponse.getErrors().get(0).getField());
    }

    private Stream<Arguments> generateInvalidDtoPlaceRequest() {
        DtoPlaceRequest firstNameRequest = PlaceTestData.createValidDtoPlaceRequest();
        firstNameRequest.setFirstName(null);
        DtoErrorResponse firstNameResponse = new DtoErrorResponse();
        firstNameResponse.addError(
                new DtoErrorResponseElement(
                        ApplicationErrorDetails.VIOLATION_FIRST_NAME_EMPTY_CONSTRAINT.toString(),
                        ApplicationErrorDetails.VIOLATION_FIRST_NAME_EMPTY_CONSTRAINT.getField(),
                        ApplicationErrorDetails.VIOLATION_FIRST_NAME_EMPTY_CONSTRAINT.getMessage()
                )
        );

        DtoPlaceRequest lastNameRequest = PlaceTestData.createValidDtoPlaceRequest();
        lastNameRequest.setLastName(null);
        DtoErrorResponse lastNameResponse = new DtoErrorResponse();
        lastNameResponse.addError(
                new DtoErrorResponseElement(
                        ApplicationErrorDetails.VIOLATION_LAST_NAME_EMPTY_CONSTRAINT.toString(),
                        ApplicationErrorDetails.VIOLATION_LAST_NAME_EMPTY_CONSTRAINT.getField(),
                        ApplicationErrorDetails.VIOLATION_LAST_NAME_EMPTY_CONSTRAINT.getMessage()
                )
        );

        DtoPlaceRequest passportRequest = PlaceTestData.createValidDtoPlaceRequest();
        passportRequest.setPassport(null);
        DtoErrorResponse passportResponse = new DtoErrorResponse();
        passportResponse.addError(
                new DtoErrorResponseElement(
                        ApplicationErrorDetails.VIOLATION_PASSPORT_EMPTY_CONSTRAINT.toString(),
                        ApplicationErrorDetails.VIOLATION_PASSPORT_EMPTY_CONSTRAINT.getField(),
                        ApplicationErrorDetails.VIOLATION_PASSPORT_EMPTY_CONSTRAINT.getMessage()
                )
        );

        return Stream.of(
                Arguments.of(firstNameRequest, firstNameResponse),
                Arguments.of(lastNameRequest, lastNameResponse),
                Arguments.of(passportRequest, passportResponse)
        );
    }

    private DtoPlaceRequest createDtoPlaceRequestForFirstPassenger(int seatNumber) {
        DtoPlaceRequest dtoPlaceRequest = new DtoPlaceRequest();
        dtoPlaceRequest.setOrderId(ORDER_ID);
        dtoPlaceRequest.setPlace(seatNumber);
        dtoPlaceRequest.setFirstName(PASSENGER_FIRST_NAME_1);
        dtoPlaceRequest.setLastName(PASSENGER_LAST_NAME_1);
        dtoPlaceRequest.setPassport(PASSENGER_PASSPORT_1);

        return dtoPlaceRequest;
    }

    private DtoPlaceRequest createDtoPlaceRequestForSecondPassenger(int seatNumber) {
        DtoPlaceRequest dtoPlaceRequest = new DtoPlaceRequest();
        dtoPlaceRequest.setOrderId(ORDER_ID);
        dtoPlaceRequest.setPlace(seatNumber);
        dtoPlaceRequest.setFirstName(PASSENGER_FIRST_NAME_2);
        dtoPlaceRequest.setLastName(PASSENGER_LAST_NAME_2);
        dtoPlaceRequest.setPassport(PASSENGER_PASSPORT_2);

        return dtoPlaceRequest;
    }

}