package edu.controller.e2e;

import edu.configuration.ApplicationConfig;
import edu.dto.error.DtoErrorResponse;
import edu.dto.error.DtoErrorResponseElement;
import edu.dto.order.DtoOrderRequest;
import edu.dto.order.DtoOrderResponse;
import edu.dto.order.DtoPassenger;
import edu.dto.trip.DtoSchedule;
import edu.dto.trip.DtoTripRequest;
import edu.dto.trip.bus.DtoBusRequest;
import edu.error.ApplicationErrorDetails;
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
class OrderControllerWithRunningServerTest extends BaseTest {
    private final String FROM_STATION_NAME = "station_name_ONE";
    private final String TO_STATION_NAME = "station_name_TWO";
    private final String BUS_NAME_ONE = "bus_brand_name_ONE";
    private final int PLACE_COUNT_OF_BUS_NAME_ONE = 3;
    private String ADMIN_SESSION_ID;
    private String FIRST_CLIENT_SESSION_ID;
    private String SECOND_CLIENT_SESSION_ID;
    private Long TRIP_ID;

    @Autowired
    protected OrderControllerWithRunningServerTest(@LocalServerPort Integer port, TestRestTemplate testRestTemplate, ApplicationConfig appConfig) {
        super(port, testRestTemplate, appConfig);
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
    }

    @Test
    void makeOrderPositive() {
        DtoOrderRequest dtoOrderRequest = new DtoOrderRequest();
        dtoOrderRequest.setTripId(TRIP_ID);
        dtoOrderRequest.setDate("2000-01-01");
        List<DtoPassenger> passengerList = new ArrayList<>();
        passengerList.add(new DtoPassenger("firstNamePassenger_1", "lastNamePassenger_1", "passportPassenger_1"));
        passengerList.add(new DtoPassenger("firstNamePassenger_2", "lastNamePassenger_2", "passportPassenger_2"));
        dtoOrderRequest.setPassengers(passengerList);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.COOKIE, createHeaderForSessionId(FIRST_CLIENT_SESSION_ID));
        ResponseEntity<DtoOrderResponse> responseEntity = testRestTemplate.exchange(
                BASE_URL + PORT + API_ORDERS_URL,
                HttpMethod.POST,
                new HttpEntity<>(dtoOrderRequest, httpHeaders),
                DtoOrderResponse.class
        );
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        DtoOrderResponse dtoOrderResponse = responseEntity.getBody();
        assertNotNull(dtoOrderResponse);
        assertNotEquals(0, dtoOrderResponse.getOrderId());
        assertNotNull(dtoOrderResponse.getPassengers());
        assertEquals(2, dtoOrderResponse.getPassengers().size());
        DtoPassenger dtoPassenger_1 = dtoOrderResponse.getPassengers().get(0);
        assertEquals("firstNamePassenger_1", dtoPassenger_1.getFirstName());
        assertEquals("lastNamePassenger_1", dtoPassenger_1.getLastName());
        assertEquals("passportPassenger_1", dtoPassenger_1.getPassport());
        DtoPassenger dtoPassenger_2 = dtoOrderResponse.getPassengers().get(1);
        assertEquals("firstNamePassenger_2", dtoPassenger_2.getFirstName());
        assertEquals("lastNamePassenger_2", dtoPassenger_2.getLastName());
        assertEquals("passportPassenger_2", dtoPassenger_2.getPassport());
        assertEquals("2000-01-01", dtoOrderResponse.getDate());
        assertEquals("20.00", dtoOrderResponse.getTotalPrice());
        assertEquals(FROM_STATION_NAME, dtoOrderResponse.getFromStation());
        assertEquals(TO_STATION_NAME, dtoOrderResponse.getToStation());
        assertEquals(BUS_NAME_ONE, dtoOrderResponse.getBusName());
        assertEquals("05:00", dtoOrderResponse.getDuration());
        assertEquals("10.00", dtoOrderResponse.getPrice());
    }

    @Test
    void makeOrderForNotApprovedTripNegative() {
        DtoTripRequest dtoTripRequest = new DtoTripRequest();
        dtoTripRequest.setBusName(BUS_NAME_ONE);
        dtoTripRequest.setPrice("10.00");
        dtoTripRequest.setFromStation(FROM_STATION_NAME);
        dtoTripRequest.setToStation(TO_STATION_NAME);
        dtoTripRequest.setStart("12:00");
        dtoTripRequest.setDuration("05:00");
        dtoTripRequest.setSchedule(new DtoSchedule("2000-01-01", "2000-01-10", "daily"));
        long tripId = addNewTripAndGetItId(dtoTripRequest, ADMIN_SESSION_ID);

        DtoOrderRequest dtoOrderRequest = new DtoOrderRequest();
        dtoOrderRequest.setTripId(tripId);
        dtoOrderRequest.setDate("2000-01-01");
        List<DtoPassenger> passengerList = new ArrayList<>();
        passengerList.add(new DtoPassenger("firstNamePassenger_1", "lastNamePassenger_1", "passportPassenger_1"));
        dtoOrderRequest.setPassengers(passengerList);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.COOKIE, createHeaderForSessionId(FIRST_CLIENT_SESSION_ID));
        ResponseEntity<DtoErrorResponse> responseEntity = testRestTemplate.exchange(
                BASE_URL + PORT + API_ORDERS_URL,
                HttpMethod.POST,
                new HttpEntity<>(dtoOrderRequest, httpHeaders),
                DtoErrorResponse.class
        );
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        DtoErrorResponse dtoErrorResponseFromServer = responseEntity.getBody();
        assertNotNull(dtoErrorResponseFromServer);
        assertEquals(1, dtoErrorResponseFromServer.getErrors().size());
        Map<String, Long> params = new HashMap<>();
        params.put("TRIP_ID", tripId);
        String errorMessage = StringSubstitutor.replace(ApplicationErrorDetails.TRIP_IS_NOT_APPROVED.getMessage(), params);
        assertEquals(errorMessage, dtoErrorResponseFromServer.getErrors().get(0).getMessage());
        assertEquals(ApplicationErrorDetails.TRIP_IS_NOT_APPROVED.getField(), dtoErrorResponseFromServer.getErrors().get(0).getField());
        assertEquals(ApplicationErrorDetails.TRIP_IS_NOT_APPROVED.toString(), dtoErrorResponseFromServer.getErrors().get(0).getErrorCode());
    }

    @Test
    void makeOrderNotByClientNegative() {
        DtoOrderRequest dtoOrderRequest = new DtoOrderRequest();
        dtoOrderRequest.setTripId(1);
        dtoOrderRequest.setDate("2000-01-01");
        List<DtoPassenger> passengerList = new ArrayList<>();
        passengerList.add(new DtoPassenger("firstNamePassenger_1", "lastNamePassenger_1", "passportPassenger_1"));
        dtoOrderRequest.setPassengers(passengerList);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.COOKIE, createHeaderForSessionId(ADMIN_SESSION_ID));
        ResponseEntity<DtoErrorResponse> responseEntity = testRestTemplate.exchange(
                BASE_URL + PORT + API_ORDERS_URL,
                HttpMethod.POST,
                new HttpEntity<>(dtoOrderRequest, httpHeaders),
                DtoErrorResponse.class
        );
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        DtoErrorResponse dtoErrorResponseFromServer = responseEntity.getBody();
        assertNotNull(dtoErrorResponseFromServer);
        DtoErrorResponse dtoErrorResponse = new DtoErrorResponse();
        dtoErrorResponse.addError(
                new DtoErrorResponseElement(
                        ApplicationErrorDetails.INCORRECT_USER_TYPE.toString(),
                        ApplicationErrorDetails.INCORRECT_USER_TYPE.getField(),
                        ApplicationErrorDetails.INCORRECT_USER_TYPE.getMessage()
                )
        );

        assertEquals(1, dtoErrorResponseFromServer.getErrors().size());
        assertEquals(dtoErrorResponse.getErrors().get(0).getMessage(), dtoErrorResponseFromServer.getErrors().get(0).getMessage());
        assertEquals(dtoErrorResponse.getErrors().get(0).getField(), dtoErrorResponseFromServer.getErrors().get(0).getField());
        assertEquals(dtoErrorResponse.getErrors().get(0).getErrorCode(), dtoErrorResponseFromServer.getErrors().get(0).getErrorCode());
    }

    @Test
    void makeOrderNotExistingTripIdNegative() {
        DtoOrderRequest dtoOrderRequest = new DtoOrderRequest();
        dtoOrderRequest.setTripId(0);
        dtoOrderRequest.setDate("2000-01-01");
        List<DtoPassenger> passengerList = new ArrayList<>();
        passengerList.add(new DtoPassenger("firstNamePassenger_1", "lastNamePassenger_1", "passportPassenger_1"));
        dtoOrderRequest.setPassengers(passengerList);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.COOKIE, createHeaderForSessionId(FIRST_CLIENT_SESSION_ID));
        ResponseEntity<DtoErrorResponse> responseEntity = testRestTemplate.exchange(
                BASE_URL + PORT + API_ORDERS_URL,
                HttpMethod.POST,
                new HttpEntity<>(dtoOrderRequest, httpHeaders),
                DtoErrorResponse.class
        );
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        DtoErrorResponse dtoErrorResponseFromServer = responseEntity.getBody();
        assertNotNull(dtoErrorResponseFromServer);
        DtoErrorResponse dtoErrorResponse = new DtoErrorResponse();
        dtoErrorResponse.addError(
                new DtoErrorResponseElement(
                        ApplicationErrorDetails.TRIP_ID_NOT_FOUND.toString(),
                        ApplicationErrorDetails.TRIP_ID_NOT_FOUND.getField(),
                        ApplicationErrorDetails.TRIP_ID_NOT_FOUND.getMessage()
                )
        );

        assertEquals(1, dtoErrorResponseFromServer.getErrors().size());
        Map<String, String> params = new HashMap<>();
        params.put("TRIP_ID", "0");
        String errorMessage = StringSubstitutor.replace(dtoErrorResponseFromServer.getErrors().get(0).getMessage(), params);
        assertEquals(errorMessage, dtoErrorResponseFromServer.getErrors().get(0).getMessage());
        assertEquals(dtoErrorResponse.getErrors().get(0).getField(), dtoErrorResponseFromServer.getErrors().get(0).getField());
        assertEquals(dtoErrorResponse.getErrors().get(0).getErrorCode(), dtoErrorResponseFromServer.getErrors().get(0).getErrorCode());
    }

    @Test
    void makeOrderNotExistingTripDateNegative() {
        DtoOrderRequest dtoOrderRequest = new DtoOrderRequest();
        dtoOrderRequest.setTripId(TRIP_ID);
        dtoOrderRequest.setDate("3000-01-01");
        List<DtoPassenger> passengerList = new ArrayList<>();
        passengerList.add(new DtoPassenger("firstNamePassenger_1", "lastNamePassenger_1", "passportPassenger_1"));
        dtoOrderRequest.setPassengers(passengerList);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.COOKIE, createHeaderForSessionId(FIRST_CLIENT_SESSION_ID));
        ResponseEntity<DtoErrorResponse> responseEntity = testRestTemplate.exchange(
                BASE_URL + PORT + API_ORDERS_URL,
                HttpMethod.POST,
                new HttpEntity<>(dtoOrderRequest, httpHeaders),
                DtoErrorResponse.class
        );
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        DtoErrorResponse dtoErrorResponseFromServer = responseEntity.getBody();
        assertNotNull(dtoErrorResponseFromServer);
        DtoErrorResponse dtoErrorResponse = new DtoErrorResponse();
        dtoErrorResponse.addError(
                new DtoErrorResponseElement(
                        ApplicationErrorDetails.TRIP_DATE_NOT_FOUND.toString(),
                        ApplicationErrorDetails.TRIP_DATE_NOT_FOUND.getField(),
                        ApplicationErrorDetails.TRIP_DATE_NOT_FOUND.getMessage()
                )
        );


        assertEquals(1, dtoErrorResponseFromServer.getErrors().size());
        Map<String, String> params = new HashMap<>();
        params.put("TRIP_DATE", "3000-01-01");
        String errorMessage = StringSubstitutor.replace(dtoErrorResponseFromServer.getErrors().get(0).getMessage(), params);
        assertEquals(errorMessage, dtoErrorResponseFromServer.getErrors().get(0).getMessage());
        assertEquals(dtoErrorResponse.getErrors().get(0).getField(), dtoErrorResponseFromServer.getErrors().get(0).getField());
        assertEquals(dtoErrorResponse.getErrors().get(0).getErrorCode(), dtoErrorResponseFromServer.getErrors().get(0).getErrorCode());
    }

    @Test
    void makeOrderNotEnoughPassengerSeatsNegative() {
        DtoOrderRequest dtoOrderRequest = new DtoOrderRequest();
        dtoOrderRequest.setTripId(TRIP_ID);
        dtoOrderRequest.setDate("2000-01-01");
        List<DtoPassenger> passengerList = new ArrayList<>();
        passengerList.add(new DtoPassenger("firstNamePassenger_1", "lastNamePassenger_1", "passportPassenger_1"));
        passengerList.add(new DtoPassenger("firstNamePassenger_2", "lastNamePassenger_2", "passportPassenger_2"));
        passengerList.add(new DtoPassenger("firstNamePassenger_3", "lastNamePassenger_3", "passportPassenger_3"));
        passengerList.add(new DtoPassenger("firstNamePassenger_4", "lastNamePassenger_4", "passportPassenger_4"));
        dtoOrderRequest.setPassengers(passengerList);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.COOKIE, createHeaderForSessionId(FIRST_CLIENT_SESSION_ID));
        ResponseEntity<DtoErrorResponse> responseEntity = testRestTemplate.exchange(
                BASE_URL + PORT + API_ORDERS_URL,
                HttpMethod.POST,
                new HttpEntity<>(dtoOrderRequest, httpHeaders),
                DtoErrorResponse.class
        );
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        DtoErrorResponse dtoErrorResponseFromServer = responseEntity.getBody();
        assertNotNull(dtoErrorResponseFromServer);
        DtoErrorResponse dtoErrorResponse = new DtoErrorResponse();
        dtoErrorResponse.addError(
                new DtoErrorResponseElement(
                        ApplicationErrorDetails.NOT_ENOUGH_FREE_PASSENGER_SEATS.toString(),
                        ApplicationErrorDetails.NOT_ENOUGH_FREE_PASSENGER_SEATS.getField(),
                        ApplicationErrorDetails.NOT_ENOUGH_FREE_PASSENGER_SEATS.getMessage()
                )
        );


        assertEquals(1, dtoErrorResponseFromServer.getErrors().size());
        assertEquals(dtoErrorResponse.getErrors().get(0).getMessage(), dtoErrorResponseFromServer.getErrors().get(0).getMessage());
        assertEquals(dtoErrorResponse.getErrors().get(0).getField(), dtoErrorResponseFromServer.getErrors().get(0).getField());
        assertEquals(dtoErrorResponse.getErrors().get(0).getErrorCode(), dtoErrorResponseFromServer.getErrors().get(0).getErrorCode());
    }

    @ParameterizedTest
    @MethodSource("generateWrongDateInfo")
    void makeOrderWrongDatePatternNegative(String date) {
        DtoOrderRequest dtoOrderRequest = new DtoOrderRequest();
        dtoOrderRequest.setTripId(TRIP_ID);
        dtoOrderRequest.setDate(date);
        List<DtoPassenger> passengerList = new ArrayList<>();
        passengerList.add(new DtoPassenger("firstNamePassenger_1", "lastNamePassenger_1", "passportPassenger_1"));
        dtoOrderRequest.setPassengers(passengerList);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.COOKIE, createHeaderForSessionId(FIRST_CLIENT_SESSION_ID));
        ResponseEntity<DtoErrorResponse> responseEntity = testRestTemplate.exchange(
                BASE_URL + PORT + API_ORDERS_URL,
                HttpMethod.POST,
                new HttpEntity<>(dtoOrderRequest, httpHeaders),
                DtoErrorResponse.class
        );
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        DtoErrorResponse dtoErrorResponseFromServer = responseEntity.getBody();
        assertNotNull(dtoErrorResponseFromServer);
        DtoErrorResponse dtoErrorResponse = new DtoErrorResponse();
        dtoErrorResponse.addError(
                new DtoErrorResponseElement(
                        ApplicationErrorDetails.VIOLATION_DATE_PATTERN_OR_EMPTY_CONSTRAINT.toString(),
                        ApplicationErrorDetails.VIOLATION_DATE_PATTERN_OR_EMPTY_CONSTRAINT.getField(),
                        ApplicationErrorDetails.VIOLATION_DATE_PATTERN_OR_EMPTY_CONSTRAINT.getMessage()
                )
        );

        assertEquals(1, dtoErrorResponseFromServer.getErrors().size());
        assertEquals(dtoErrorResponse.getErrors().get(0), dtoErrorResponseFromServer.getErrors().get(0));
    }
    @Test
    void makeOrderWrongSessionIdNegative() {
        DtoOrderRequest dtoOrderRequest = new DtoOrderRequest();
        dtoOrderRequest.setTripId(TRIP_ID);
        dtoOrderRequest.setDate("2000-01-01");
        List<DtoPassenger> passengerList = new ArrayList<>();
        passengerList.add(new DtoPassenger("firstNamePassenger_1", "lastNamePassenger_1", "passportPassenger_1"));
        passengerList.add(new DtoPassenger("firstNamePassenger_2", "lastNamePassenger_2", "passportPassenger_2"));
        dtoOrderRequest.setPassengers(passengerList);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.COOKIE, createHeaderForSessionId("NOT_EXISTING_SESSION_ID"));
        ResponseEntity<DtoErrorResponse> responseEntity = testRestTemplate.exchange(
                BASE_URL + PORT + API_ORDERS_URL,
                HttpMethod.POST,
                new HttpEntity<>(dtoOrderRequest, httpHeaders),
                DtoErrorResponse.class
        );
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        DtoErrorResponse dtoErrorResponse = responseEntity.getBody();
        assertNotNull(dtoErrorResponse);
        assertNotNull(dtoErrorResponse.getErrors());
        assertEquals(1, dtoErrorResponse.getErrors().size());
        assertEquals(ApplicationErrorDetails.SESSION_ID_NOT_FOUND.toString(), dtoErrorResponse.getErrors().get(0).getErrorCode());
        assertEquals(ApplicationErrorDetails.SESSION_ID_NOT_FOUND.getField(), dtoErrorResponse.getErrors().get(0).getField());
        Map<String, String> params = new HashMap<>();
        params.put("SESSION_ID", "NOT_EXISTING_SESSION_ID");
        String errorMessage = StringSubstitutor.replace(ApplicationErrorDetails.SESSION_ID_NOT_FOUND.getMessage(), params);
        assertEquals(errorMessage, dtoErrorResponse.getErrors().get(0).getMessage());
    }

    @ParameterizedTest
    @MethodSource("generateWrongPassengerObjects")
    void makeOrderWithEmptyWrongPassengerInfoNegative(DtoPassenger dtoPassenger, DtoErrorResponse dtoErrorResponse) {
        DtoOrderRequest dtoOrderRequest = new DtoOrderRequest();
        dtoOrderRequest.setTripId(TRIP_ID);
        dtoOrderRequest.setDate("2000-01-01");
        List<DtoPassenger> passengerList = new ArrayList<>();
        passengerList.add(dtoPassenger);
        dtoOrderRequest.setPassengers(passengerList);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.COOKIE, createHeaderForSessionId(FIRST_CLIENT_SESSION_ID));
        ResponseEntity<DtoErrorResponse> responseEntity = testRestTemplate.exchange(
                BASE_URL + PORT + API_ORDERS_URL,
                HttpMethod.POST,
                new HttpEntity<>(dtoOrderRequest, httpHeaders),
                DtoErrorResponse.class
        );
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        DtoErrorResponse dtoErrorResponseFromServer = responseEntity.getBody();
        assertNotNull(dtoErrorResponseFromServer);
        assertEquals(1, dtoErrorResponseFromServer.getErrors().size());
        assertEquals(dtoErrorResponse.getErrors().get(0), dtoErrorResponseFromServer.getErrors().get(0));
    }

    @Test
    void deleteOrderByIdPositive() {
        long orderId = addNewOrderAndGetItId();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.COOKIE, createHeaderForSessionId(FIRST_CLIENT_SESSION_ID));
        ResponseEntity<String> responseEntity = testRestTemplate.exchange(
                BASE_URL + PORT + API_ORDERS_URL +"/"+ orderId,
                HttpMethod.DELETE,
                new HttpEntity<>(null, httpHeaders),
                String.class
        );
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        String responseFromServer = responseEntity.getBody();
        assertNotNull(responseFromServer);
        assertEquals("{}", responseFromServer);
    }

    @Test
    void deleteOrderByIdAndNotByClientNegative() {
        long orderId = addNewOrderAndGetItId();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.COOKIE, createHeaderForSessionId("NOT_SESSION_ID"));
        ResponseEntity<DtoErrorResponse> responseEntity = testRestTemplate.exchange(
                BASE_URL + PORT + API_ORDERS_URL +"/"+ orderId,
                HttpMethod.DELETE,
                new HttpEntity<>(null, httpHeaders),
                DtoErrorResponse.class
        );
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        DtoErrorResponse dtoErrorResponseFromServer = responseEntity.getBody();
        assertNotNull(dtoErrorResponseFromServer);
        DtoErrorResponse dtoErrorResponse = new DtoErrorResponse();
        dtoErrorResponse.addError(
                new DtoErrorResponseElement(
                        ApplicationErrorDetails.SESSION_ID_NOT_FOUND.toString(),
                        ApplicationErrorDetails.SESSION_ID_NOT_FOUND.getField(),
                        ApplicationErrorDetails.SESSION_ID_NOT_FOUND.getMessage()
                )
        );

        assertEquals(1, dtoErrorResponseFromServer.getErrors().size());
        Map<String, String> params = new HashMap<>();
        params.put("SESSION_ID", "NOT_SESSION_ID");
        String errorMessage = StringSubstitutor.replace(dtoErrorResponse.getErrors().get(0).getMessage(), params);
        assertEquals(errorMessage, dtoErrorResponseFromServer.getErrors().get(0).getMessage());
        assertEquals(dtoErrorResponse.getErrors().get(0).getField(), dtoErrorResponseFromServer.getErrors().get(0).getField());
        assertEquals(dtoErrorResponse.getErrors().get(0).getErrorCode(), dtoErrorResponseFromServer.getErrors().get(0).getErrorCode());
    }

    @Test
    void deleteOrderNotExistingIdNegative() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.COOKIE, createHeaderForSessionId(FIRST_CLIENT_SESSION_ID));
        ResponseEntity<DtoErrorResponse> responseEntity = testRestTemplate.exchange(
                BASE_URL + PORT + API_ORDERS_URL +"/"+ "0",
                HttpMethod.DELETE,
                new HttpEntity<>(null, httpHeaders),
                DtoErrorResponse.class
        );
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        DtoErrorResponse dtoErrorResponseFromServer = responseEntity.getBody();
        assertNotNull(dtoErrorResponseFromServer);
        DtoErrorResponse dtoErrorResponse = new DtoErrorResponse();
        dtoErrorResponse.addError(
                new DtoErrorResponseElement(
                        ApplicationErrorDetails.ORDER_ID_NOT_FOUND.toString(),
                        ApplicationErrorDetails.ORDER_ID_NOT_FOUND.getField(),
                        ApplicationErrorDetails.ORDER_ID_NOT_FOUND.getMessage()
                )
        );

        assertEquals(1, dtoErrorResponseFromServer.getErrors().size());
        Map<String, String> params = new HashMap<>();
        params.put("ORDER_ID", "0");
        String errorMessage = StringSubstitutor.replace(dtoErrorResponse.getErrors().get(0).getMessage(), params);
        assertEquals(errorMessage, dtoErrorResponseFromServer.getErrors().get(0).getMessage());
        assertEquals(dtoErrorResponse.getErrors().get(0).getField(), dtoErrorResponseFromServer.getErrors().get(0).getField());
        assertEquals(dtoErrorResponse.getErrors().get(0).getErrorCode(), dtoErrorResponseFromServer.getErrors().get(0).getErrorCode());
    }

    @Test
    void deleteOrderByAnotherClientNegative() {
        long orderId = addNewOrderAndGetItId();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.COOKIE, createHeaderForSessionId(SECOND_CLIENT_SESSION_ID));
        ResponseEntity<DtoErrorResponse> responseEntity = testRestTemplate.exchange(
                BASE_URL + PORT + API_ORDERS_URL +"/"+ orderId,
                HttpMethod.DELETE,
                new HttpEntity<>(null, httpHeaders),
                DtoErrorResponse.class
        );
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        DtoErrorResponse dtoErrorResponseFromServer = responseEntity.getBody();
        assertNotNull(dtoErrorResponseFromServer);
        DtoErrorResponse dtoErrorResponse = new DtoErrorResponse();
        dtoErrorResponse.addError(
                new DtoErrorResponseElement(
                        ApplicationErrorDetails.ORDER_ID_NOT_FOUND.toString(),
                        ApplicationErrorDetails.ORDER_ID_NOT_FOUND.getField(),
                        ApplicationErrorDetails.ORDER_ID_NOT_FOUND.getMessage()
                )
        );

        assertEquals(1, dtoErrorResponseFromServer.getErrors().size());
        Map<String, String> params = new HashMap<>();
        params.put("ORDER_ID", Long.toString(orderId));
        String errorMessage = StringSubstitutor.replace(dtoErrorResponse.getErrors().get(0).getMessage(), params);
        assertEquals(errorMessage, dtoErrorResponseFromServer.getErrors().get(0).getMessage());
        assertEquals(dtoErrorResponse.getErrors().get(0).getField(), dtoErrorResponseFromServer.getErrors().get(0).getField());
        assertEquals(dtoErrorResponse.getErrors().get(0).getErrorCode(), dtoErrorResponseFromServer.getErrors().get(0).getErrorCode());
    }

    @Test
    void makeOrderByPassengersWithDeletingAndMakingOrderAgain() {
        //adding two passenger(1 one 3 places left) - success
        DtoOrderRequest dtoOrderRequest = new DtoOrderRequest();
        dtoOrderRequest.setTripId(TRIP_ID);
        dtoOrderRequest.setDate("2000-01-01");
        List<DtoPassenger> passengerList = new ArrayList<>();
        passengerList.add(new DtoPassenger("firstNamePassenger_1", "lastNamePassenger_1", "passportPassenger_1"));
        passengerList.add(new DtoPassenger("firstNamePassenger_2", "lastNamePassenger_2", "passportPassenger_2"));
        dtoOrderRequest.setPassengers(passengerList);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.COOKIE, createHeaderForSessionId(FIRST_CLIENT_SESSION_ID));
        ResponseEntity<DtoOrderResponse> responseEntity = testRestTemplate.exchange(
                BASE_URL + PORT + API_ORDERS_URL,
                HttpMethod.POST,
                new HttpEntity<>(dtoOrderRequest, httpHeaders),
                DtoOrderResponse.class
        );
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        DtoOrderResponse dtoOrderResponse = responseEntity.getBody();
        assertNotNull(dtoOrderResponse);
        assertNotEquals(0, dtoOrderResponse.getOrderId());
        long orderIdOfTwoPassengers = dtoOrderResponse.getOrderId();
        assertNotNull(dtoOrderResponse.getPassengers());
        assertEquals(2, dtoOrderResponse.getPassengers().size());
        DtoPassenger dtoPassenger_1 = dtoOrderResponse.getPassengers().get(0);
        assertEquals("firstNamePassenger_1", dtoPassenger_1.getFirstName());
        assertEquals("lastNamePassenger_1", dtoPassenger_1.getLastName());
        assertEquals("passportPassenger_1", dtoPassenger_1.getPassport());
        DtoPassenger dtoPassenger_2 = dtoOrderResponse.getPassengers().get(1);
        assertEquals("firstNamePassenger_2", dtoPassenger_2.getFirstName());
        assertEquals("lastNamePassenger_2", dtoPassenger_2.getLastName());
        assertEquals("passportPassenger_2", dtoPassenger_2.getPassport());
        assertEquals("2000-01-01", dtoOrderResponse.getDate());
        assertEquals("20.00", dtoOrderResponse.getTotalPrice());
        assertEquals(FROM_STATION_NAME, dtoOrderResponse.getFromStation());
        assertEquals(TO_STATION_NAME, dtoOrderResponse.getToStation());
        assertEquals(BUS_NAME_ONE, dtoOrderResponse.getBusName());
        assertEquals("05:00", dtoOrderResponse.getDuration());
        assertEquals("10.00", dtoOrderResponse.getPrice());

        //adding third passenger(no places left) - success
        dtoOrderRequest = new DtoOrderRequest();
        dtoOrderRequest.setTripId(TRIP_ID);
        dtoOrderRequest.setDate("2000-01-01");
        passengerList = new ArrayList<>();
        passengerList.add(new DtoPassenger("firstNamePassenger_3", "lastNamePassenger_3", "passportPassenger_3"));
        dtoOrderRequest.setPassengers(passengerList);
        httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.COOKIE, createHeaderForSessionId(FIRST_CLIENT_SESSION_ID));
        responseEntity = testRestTemplate.exchange(
                BASE_URL + PORT + API_ORDERS_URL,
                HttpMethod.POST,
                new HttpEntity<>(dtoOrderRequest, httpHeaders),
                DtoOrderResponse.class
        );
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        dtoOrderResponse = responseEntity.getBody();
        assertNotNull(dtoOrderResponse);
        assertNotEquals(0, dtoOrderResponse.getOrderId());
        assertNotNull(dtoOrderResponse.getPassengers());
        assertEquals(1, dtoOrderResponse.getPassengers().size());
        DtoPassenger dtoPassenger_3 = dtoOrderResponse.getPassengers().get(0);
        assertEquals("firstNamePassenger_3", dtoPassenger_3.getFirstName());
        assertEquals("lastNamePassenger_3", dtoPassenger_3.getLastName());
        assertEquals("passportPassenger_3", dtoPassenger_3.getPassport());
        assertEquals("2000-01-01", dtoOrderResponse.getDate());
        assertEquals("10.00", dtoOrderResponse.getTotalPrice());
        assertEquals(FROM_STATION_NAME, dtoOrderResponse.getFromStation());
        assertEquals(TO_STATION_NAME, dtoOrderResponse.getToStation());
        assertEquals(BUS_NAME_ONE, dtoOrderResponse.getBusName());
        assertEquals("05:00", dtoOrderResponse.getDuration());
        assertEquals("10.00", dtoOrderResponse.getPrice());

        //adding four passenger(still no free places) - failure
        dtoOrderRequest = new DtoOrderRequest();
        dtoOrderRequest.setTripId(TRIP_ID);
        dtoOrderRequest.setDate("2000-01-01");
        passengerList = new ArrayList<>();
        passengerList.add(new DtoPassenger("firstNamePassenger_4", "lastNamePassenger_4", "passportPassenger_4"));
        dtoOrderRequest.setPassengers(passengerList);
        httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.COOKIE, createHeaderForSessionId(FIRST_CLIENT_SESSION_ID));
        ResponseEntity<DtoErrorResponse> responseErrorEntity = testRestTemplate.exchange(
                BASE_URL + PORT + API_ORDERS_URL,
                HttpMethod.POST,
                new HttpEntity<>(dtoOrderRequest, httpHeaders),
                DtoErrorResponse.class
        );
        assertNotNull(responseErrorEntity);
        assertEquals(HttpStatus.CONFLICT, responseErrorEntity.getStatusCode());
        DtoErrorResponse dtoErrorResponseFromServer = responseErrorEntity.getBody();
        assertNotNull(dtoErrorResponseFromServer);
        DtoErrorResponse dtoErrorResponse = new DtoErrorResponse();
        dtoErrorResponse.addError(
                new DtoErrorResponseElement(
                        ApplicationErrorDetails.NOT_ENOUGH_FREE_PASSENGER_SEATS.toString(),
                        ApplicationErrorDetails.NOT_ENOUGH_FREE_PASSENGER_SEATS.getField(),
                        ApplicationErrorDetails.NOT_ENOUGH_FREE_PASSENGER_SEATS.getMessage()
                )
        );
        assertEquals(1, dtoErrorResponseFromServer.getErrors().size());
        assertEquals(dtoErrorResponse.getErrors().get(0).getMessage(), dtoErrorResponseFromServer.getErrors().get(0).getMessage());
        assertEquals(dtoErrorResponse.getErrors().get(0).getField(), dtoErrorResponseFromServer.getErrors().get(0).getField());
        assertEquals(dtoErrorResponse.getErrors().get(0).getErrorCode(), dtoErrorResponseFromServer.getErrors().get(0).getErrorCode());

        //deleting first two passengers(2 of 3 places left again) - success
        httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.COOKIE, createHeaderForSessionId(FIRST_CLIENT_SESSION_ID));
        ResponseEntity<String> responseEntityAfterDeletion = testRestTemplate.exchange(
                BASE_URL + PORT + API_ORDERS_URL +"/"+ orderIdOfTwoPassengers,
                HttpMethod.DELETE,
                new HttpEntity<>(null, httpHeaders),
                String.class
        );
        assertNotNull(responseEntityAfterDeletion);
        assertEquals(HttpStatus.OK, responseEntityAfterDeletion.getStatusCode());
        String responseFromServer = responseEntityAfterDeletion.getBody();
        assertNotNull(responseFromServer);
        assertEquals("{}", responseFromServer);

        //adding another passenger(1 of 3 places left) - success
        dtoOrderRequest = new DtoOrderRequest();
        dtoOrderRequest.setTripId(TRIP_ID);
        dtoOrderRequest.setDate("2000-01-01");
        passengerList = new ArrayList<>();
        passengerList.add(new DtoPassenger("firstNamePassenger_5", "lastNamePassenger_5", "passportPassenger_5"));
        dtoOrderRequest.setPassengers(passengerList);
        httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.COOKIE, createHeaderForSessionId(FIRST_CLIENT_SESSION_ID));
        responseEntity = testRestTemplate.exchange(
                BASE_URL + PORT + API_ORDERS_URL,
                HttpMethod.POST,
                new HttpEntity<>(dtoOrderRequest, httpHeaders),
                DtoOrderResponse.class
        );
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        dtoOrderResponse = responseEntity.getBody();
        assertNotNull(dtoOrderResponse);
        assertNotEquals(0, dtoOrderResponse.getOrderId());
        assertNotNull(dtoOrderResponse.getPassengers());
        assertEquals(1, dtoOrderResponse.getPassengers().size());
        DtoPassenger dtoPassenger_5 = dtoOrderResponse.getPassengers().get(0);
        assertEquals("firstNamePassenger_5", dtoPassenger_5.getFirstName());
        assertEquals("lastNamePassenger_5", dtoPassenger_5.getLastName());
        assertEquals("passportPassenger_5", dtoPassenger_5.getPassport());
        assertEquals("2000-01-01", dtoOrderResponse.getDate());
        assertEquals("10.00", dtoOrderResponse.getTotalPrice());
        assertEquals(FROM_STATION_NAME, dtoOrderResponse.getFromStation());
        assertEquals(TO_STATION_NAME, dtoOrderResponse.getToStation());
        assertEquals(BUS_NAME_ONE, dtoOrderResponse.getBusName());
        assertEquals("05:00", dtoOrderResponse.getDuration());
        assertEquals("10.00", dtoOrderResponse.getPrice());

        //adding two more passengers(1 of 3 places left - not enough) - failure
        dtoOrderRequest = new DtoOrderRequest();
        dtoOrderRequest.setTripId(TRIP_ID);
        dtoOrderRequest.setDate("2000-01-01");
        passengerList = new ArrayList<>();
        passengerList.add(
                new DtoPassenger(
                        "firstNamePassenger_6",
                        "lastNamePassenger_6",
                        "passportPassenger_6")
        );
        passengerList.add(
                new DtoPassenger(
                        "firstNamePassenger_7",
                        "lastNamePassenger_7",
                        "passportPassenger_7")
        );
        dtoOrderRequest.setPassengers(passengerList);
        httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.COOKIE, createHeaderForSessionId(FIRST_CLIENT_SESSION_ID));
        responseErrorEntity = testRestTemplate.exchange(
                BASE_URL + PORT + API_ORDERS_URL,
                HttpMethod.POST,
                new HttpEntity<>(dtoOrderRequest, httpHeaders),
                DtoErrorResponse.class
        );
        assertNotNull(responseErrorEntity);
        assertEquals(HttpStatus.CONFLICT, responseErrorEntity.getStatusCode());
        dtoErrorResponseFromServer = responseErrorEntity.getBody();
        assertNotNull(dtoErrorResponseFromServer);
        dtoErrorResponse = new DtoErrorResponse();
        dtoErrorResponse.addError(
                new DtoErrorResponseElement(
                        ApplicationErrorDetails.NOT_ENOUGH_FREE_PASSENGER_SEATS.toString(),
                        ApplicationErrorDetails.NOT_ENOUGH_FREE_PASSENGER_SEATS.getField(),
                        ApplicationErrorDetails.NOT_ENOUGH_FREE_PASSENGER_SEATS.getMessage()
                )
        );
        assertEquals(1, dtoErrorResponseFromServer.getErrors().size());
        assertEquals(dtoErrorResponse.getErrors().get(0).getMessage(), dtoErrorResponseFromServer.getErrors().get(0).getMessage());
        assertEquals(dtoErrorResponse.getErrors().get(0).getField(), dtoErrorResponseFromServer.getErrors().get(0).getField());
        assertEquals(dtoErrorResponse.getErrors().get(0).getErrorCode(), dtoErrorResponseFromServer.getErrors().get(0).getErrorCode());
    }

    @Test
    void getOrdersByParams() {
        DtoOrderRequest dtoOrderRequest = new DtoOrderRequest();
        dtoOrderRequest.setTripId(TRIP_ID);
        dtoOrderRequest.setDate("2000-01-01");
        List<DtoPassenger> passengerList = new ArrayList<>();
        passengerList.add(new DtoPassenger("firstNamePassenger_1", "lastNamePassenger_1", "passportPassenger_1"));
        dtoOrderRequest.setPassengers(passengerList);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.COOKIE, createHeaderForSessionId(FIRST_CLIENT_SESSION_ID));
        ResponseEntity<DtoOrderResponse> responseEntity = testRestTemplate.exchange(
                BASE_URL + PORT + API_ORDERS_URL,
                HttpMethod.POST,
                new HttpEntity<>(dtoOrderRequest, httpHeaders),
                DtoOrderResponse.class
        );
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        dtoOrderRequest = new DtoOrderRequest();
        dtoOrderRequest.setTripId(TRIP_ID);
        dtoOrderRequest.setDate("2000-01-02");
        passengerList = new ArrayList<>();
        passengerList.add(new DtoPassenger("firstNamePassenger_2", "lastNamePassenger_2", "passportPassenger_2"));
        dtoOrderRequest.setPassengers(passengerList);
        responseEntity = testRestTemplate.exchange(
                BASE_URL + PORT + API_ORDERS_URL,
                HttpMethod.POST,
                new HttpEntity<>(dtoOrderRequest, httpHeaders),
                DtoOrderResponse.class
        );
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        dtoOrderRequest = new DtoOrderRequest();
        dtoOrderRequest.setTripId(TRIP_ID);
        dtoOrderRequest.setDate("2000-01-05");
        passengerList = new ArrayList<>();
        passengerList.add(new DtoPassenger("firstNamePassenger_5", "lastNamePassenger_5", "passportPassenger_5"));
        dtoOrderRequest.setPassengers(passengerList);
        responseEntity = testRestTemplate.exchange(
                BASE_URL + PORT + API_ORDERS_URL,
                HttpMethod.POST,
                new HttpEntity<>(dtoOrderRequest, httpHeaders),
                DtoOrderResponse.class
        );
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        dtoOrderRequest = new DtoOrderRequest();
        dtoOrderRequest.setTripId(TRIP_ID);
        dtoOrderRequest.setDate("2000-01-07");
        passengerList = new ArrayList<>();
        passengerList.add(new DtoPassenger("firstNamePassenger_7", "lastNamePassenger_7", "passportPassenger_7"));
        dtoOrderRequest.setPassengers(passengerList);
        responseEntity = testRestTemplate.exchange(
                BASE_URL + PORT + API_ORDERS_URL,
                HttpMethod.POST,
                new HttpEntity<>(dtoOrderRequest, httpHeaders),
                DtoOrderResponse.class
        );
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        dtoOrderRequest = new DtoOrderRequest();
        dtoOrderRequest.setTripId(TRIP_ID);
        dtoOrderRequest.setDate("2000-01-10");
        passengerList = new ArrayList<>();
        passengerList.add(new DtoPassenger("firstNamePassenger_10", "lastNamePassenger_10", "passportPassenger_7"));
        dtoOrderRequest.setPassengers(passengerList);
        responseEntity = testRestTemplate.exchange(
                BASE_URL + PORT + API_ORDERS_URL,
                HttpMethod.POST,
                new HttpEntity<>(dtoOrderRequest, httpHeaders),
                DtoOrderResponse.class
        );
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        String toStation = "toStation=" + TO_STATION_NAME;
        String fromStation = "fromStation=" + FROM_STATION_NAME;
        String busName = "busName=" + BUS_NAME_ONE;
        String fromDate = "fromDate=";
        String toDate = "toDate=";

        ResponseEntity<List<DtoOrderResponse>> responseEntityFindingByParams = testRestTemplate.exchange(
                BASE_URL + PORT + API_ORDERS_URL + "?" + toStation + "&" + fromStation + "&" + busName,
                HttpMethod.GET,
                new HttpEntity<>(null, httpHeaders),
                new ParameterizedTypeReference<>() {}
        );
        assertNotNull(responseEntityFindingByParams);
        assertEquals(HttpStatus.OK, responseEntityFindingByParams.getStatusCode());
        List<DtoOrderResponse> searchingResultList = responseEntityFindingByParams.getBody();
        assertNotNull(searchingResultList);
        assertEquals(5, searchingResultList.size());

        responseEntityFindingByParams = testRestTemplate.exchange(
                BASE_URL + PORT + API_ORDERS_URL + "?"
                        + toStation
                        + "&" + fromStation
                        + "&" + busName
                        + "&" + fromDate + "2000-01-01"
                        + "&" + toDate + "2000-01-10",
                HttpMethod.GET,
                new HttpEntity<>(null, httpHeaders),
                new ParameterizedTypeReference<>() {}
        );
        assertNotNull(responseEntityFindingByParams);
        assertEquals(HttpStatus.OK, responseEntityFindingByParams.getStatusCode());
        searchingResultList = responseEntityFindingByParams.getBody();
        assertNotNull(searchingResultList);
        assertEquals(5, searchingResultList.size());

        responseEntityFindingByParams = testRestTemplate.exchange(
                BASE_URL + PORT + API_ORDERS_URL + "?"
                        + toStation
                        + "&" + fromStation
                        + "&" + busName
                        + "&" + fromDate + "2000-01-05"
                        + "&" + toDate + "2000-01-10",
                HttpMethod.GET,
                new HttpEntity<>(null, httpHeaders),
                new ParameterizedTypeReference<>() {}
        );
        assertNotNull(responseEntityFindingByParams);
        assertEquals(HttpStatus.OK, responseEntityFindingByParams.getStatusCode());
        searchingResultList = responseEntityFindingByParams.getBody();
        assertNotNull(searchingResultList);
        assertEquals(3, searchingResultList.size());

        responseEntityFindingByParams = testRestTemplate.exchange(
                BASE_URL + PORT + API_ORDERS_URL + "?"
                        + toStation
                        + "&" + fromStation
                        + "&" + busName
                        + "&" + fromDate + "2000-01-02"
                        + "&" + toDate + "2000-01-05",
                HttpMethod.GET,
                new HttpEntity<>(null, httpHeaders),
                new ParameterizedTypeReference<>() {}
        );
        assertNotNull(responseEntityFindingByParams);
        assertEquals(HttpStatus.OK, responseEntityFindingByParams.getStatusCode());
        searchingResultList = responseEntityFindingByParams.getBody();
        assertNotNull(searchingResultList);
        assertEquals(2, searchingResultList.size());

        responseEntityFindingByParams = testRestTemplate.exchange(
                BASE_URL + PORT + API_ORDERS_URL + "?"
                        + toStation
                        + "&" + fromStation
                        + "&" + busName
                        + "&" + fromDate + "2000-01-04"
                        + "&" + toDate + "2000-01-09",
                HttpMethod.GET,
                new HttpEntity<>(null, httpHeaders),
                new ParameterizedTypeReference<>() {}
        );
        assertNotNull(responseEntityFindingByParams);
        assertEquals(HttpStatus.OK, responseEntityFindingByParams.getStatusCode());
        searchingResultList = responseEntityFindingByParams.getBody();
        assertNotNull(searchingResultList);
        assertEquals(2, searchingResultList.size());

        responseEntityFindingByParams = testRestTemplate.exchange(
                BASE_URL + PORT + API_ORDERS_URL + "?"
                        + toStation
                        + "&" + fromStation
                        + "&" + busName
                        + "&" + fromDate + "2000-01-08"
                        + "&" + toDate + "2000-01-09",
                HttpMethod.GET,
                new HttpEntity<>(null, httpHeaders),
                new ParameterizedTypeReference<>() {}
        );
        assertNotNull(responseEntityFindingByParams);
        assertEquals(HttpStatus.OK, responseEntityFindingByParams.getStatusCode());
        searchingResultList = responseEntityFindingByParams.getBody();
        assertNotNull(searchingResultList);
        assertEquals(0, searchingResultList.size());

        responseEntityFindingByParams = testRestTemplate.exchange(
                BASE_URL + PORT + API_ORDERS_URL + "?"
                        + toStation
                        + "&" + fromStation
                        + "&" + busName
                        + "&" + fromDate + "2000-01-08",
                HttpMethod.GET,
                new HttpEntity<>(null, httpHeaders),
                new ParameterizedTypeReference<>() {}
        );
        assertNotNull(responseEntityFindingByParams);
        assertEquals(HttpStatus.OK, responseEntityFindingByParams.getStatusCode());
        searchingResultList = responseEntityFindingByParams.getBody();
        assertNotNull(searchingResultList);
        assertEquals(1, searchingResultList.size());

        responseEntityFindingByParams = testRestTemplate.exchange(
                BASE_URL + PORT + API_ORDERS_URL + "?"
                        + toStation
                        + "&" + fromStation
                        + "&" + busName
                        + "&" + toDate + "2000-01-09",
                HttpMethod.GET,
                new HttpEntity<>(null, httpHeaders),
                new ParameterizedTypeReference<>() {}
        );
        assertNotNull(responseEntityFindingByParams);
        assertEquals(HttpStatus.OK, responseEntityFindingByParams.getStatusCode());
        searchingResultList = responseEntityFindingByParams.getBody();
        assertNotNull(searchingResultList);
        assertEquals(4, searchingResultList.size());

        responseEntityFindingByParams = testRestTemplate.exchange(
                BASE_URL + PORT + API_ORDERS_URL + "?"
                        + toStation
                        + "&" + "fromStation=NOT_EXISTING_STATION"
                        + "&" + busName
                        + "&" + fromDate + "2000-01-08"
                        + "&" + toDate + "2000-01-09",
                HttpMethod.GET,
                new HttpEntity<>(null, httpHeaders),
                new ParameterizedTypeReference<>() {}
        );
        assertNotNull(responseEntityFindingByParams);
        assertEquals(HttpStatus.OK, responseEntityFindingByParams.getStatusCode());
        searchingResultList = responseEntityFindingByParams.getBody();
        assertNotNull(searchingResultList);
        assertEquals(0, searchingResultList.size());

        responseEntityFindingByParams = testRestTemplate.exchange(
                BASE_URL + PORT + API_ORDERS_URL + "?"
                        + "toStation=NOT_EXISTING_STATION"
                        + "&" + fromStation
                        + "&" + busName
                        + "&" + fromDate + "2000-01-08"
                        + "&" + toDate + "2000-01-09",
                HttpMethod.GET,
                new HttpEntity<>(null, httpHeaders),
                new ParameterizedTypeReference<>() {}
        );
        assertNotNull(responseEntityFindingByParams);
        assertEquals(HttpStatus.OK, responseEntityFindingByParams.getStatusCode());
        searchingResultList = responseEntityFindingByParams.getBody();
        assertNotNull(searchingResultList);
        assertEquals(0, searchingResultList.size());

        responseEntityFindingByParams = testRestTemplate.exchange(
                BASE_URL + PORT + API_ORDERS_URL + "?"
                        + toStation
                        + "&" + fromStation
                        + "&" + "NOT_EXISTING_BUS"
                        + "&" + fromDate + "2000-01-08"
                        + "&" + toDate + "2000-01-09",
                HttpMethod.GET,
                new HttpEntity<>(null, httpHeaders),
                new ParameterizedTypeReference<>() {}
        );
        assertNotNull(responseEntityFindingByParams);
        assertEquals(HttpStatus.OK, responseEntityFindingByParams.getStatusCode());
        searchingResultList = responseEntityFindingByParams.getBody();
        assertNotNull(searchingResultList);
        assertEquals(0, searchingResultList.size());

        responseEntityFindingByParams = testRestTemplate.exchange(
                BASE_URL + PORT + API_ORDERS_URL + "?" + busName,
                HttpMethod.GET,
                new HttpEntity<>(null, httpHeaders),
                new ParameterizedTypeReference<>() {}
        );
        assertNotNull(responseEntityFindingByParams);
        assertEquals(HttpStatus.OK, responseEntityFindingByParams.getStatusCode());
        searchingResultList = responseEntityFindingByParams.getBody();
        assertNotNull(searchingResultList);
        assertEquals(5, searchingResultList.size());

        responseEntityFindingByParams = testRestTemplate.exchange(
                BASE_URL + PORT + API_ORDERS_URL + "?" + toStation,
                HttpMethod.GET,
                new HttpEntity<>(null, httpHeaders),
                new ParameterizedTypeReference<>() {}
        );
        assertNotNull(responseEntityFindingByParams);
        assertEquals(HttpStatus.OK, responseEntityFindingByParams.getStatusCode());
        searchingResultList = responseEntityFindingByParams.getBody();
        assertNotNull(searchingResultList);
        assertEquals(5, searchingResultList.size());

        responseEntityFindingByParams = testRestTemplate.exchange(
                BASE_URL + PORT + API_ORDERS_URL + "?" + fromStation,
                HttpMethod.GET,
                new HttpEntity<>(null, httpHeaders),
                new ParameterizedTypeReference<>() {}
        );
        assertNotNull(responseEntityFindingByParams);
        assertEquals(HttpStatus.OK, responseEntityFindingByParams.getStatusCode());
        searchingResultList = responseEntityFindingByParams.getBody();
        assertNotNull(searchingResultList);
        assertEquals(5, searchingResultList.size());

        responseEntityFindingByParams = testRestTemplate.exchange(
                BASE_URL + PORT + API_ORDERS_URL,
                HttpMethod.GET,
                new HttpEntity<>(null, httpHeaders),
                new ParameterizedTypeReference<>() {}
        );
        assertNotNull(responseEntityFindingByParams);
        assertEquals(HttpStatus.OK, responseEntityFindingByParams.getStatusCode());
        searchingResultList = responseEntityFindingByParams.getBody();
        assertNotNull(searchingResultList);
        assertEquals(5, searchingResultList.size());
    }
    @ParameterizedTest
    @MethodSource("generateWrongDatesForMethodToFindByParams")
    void getOrdersByParamsWrongDatePatternNegative(String dateParam) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.COOKIE, createHeaderForSessionId(FIRST_CLIENT_SESSION_ID));
        ResponseEntity<DtoErrorResponse> dtoErrorEntity = testRestTemplate.exchange(
                BASE_URL + PORT + API_ORDERS_URL + "?" + dateParam,
                HttpMethod.GET,
                new HttpEntity<>(null, httpHeaders),
                new ParameterizedTypeReference<>() {}
        );
        assertNotNull(dtoErrorEntity);
        assertEquals(HttpStatus.BAD_REQUEST, dtoErrorEntity.getStatusCode());
        DtoErrorResponse dtoErrorResponseFromServer = dtoErrorEntity.getBody();
        assertNotNull(dtoErrorResponseFromServer);
        assertEquals(1, dtoErrorResponseFromServer.getErrors().size());
        assertEquals(ApplicationErrorDetails.VIOLATION_DATE_PATTERN_OR_EMPTY_CONSTRAINT.getMessage(), dtoErrorResponseFromServer.getErrors().get(0).getMessage());
        assertEquals(ApplicationErrorDetails.VIOLATION_DATE_PATTERN_OR_EMPTY_CONSTRAINT.getField(), dtoErrorResponseFromServer.getErrors().get(0).getField());
        assertEquals(ApplicationErrorDetails.VIOLATION_DATE_PATTERN_OR_EMPTY_CONSTRAINT.toString(), dtoErrorResponseFromServer.getErrors().get(0).getErrorCode());
    }

    private Stream<Arguments> generateWrongDatesForMethodToFindByParams() {
        return Stream.of(
                Arguments.of("fromDate=2000:01:01"),
                Arguments.of("fromDate=2000 01 01"),
                Arguments.of("fromDate=2000_01_01"),
                Arguments.of("fromDate=2000/01/01"),
                Arguments.of("fromDate=200-01-01"),
                Arguments.of("fromDate=2000-1-01"),
                Arguments.of("fromDate=2000-01-1"),
                Arguments.of("fromDate=2000-42-01"),
                Arguments.of("fromDate=2000-01-42"),
                Arguments.of("fromDate=01-01-2000"),
                Arguments.of("fromDate=20-01-01"),

                Arguments.of("toDate=2000:01:01"),
                Arguments.of("toDate=2000 01 01"),
                Arguments.of("toDate=2000_01_01"),
                Arguments.of("toDate=2000/01/01"),
                Arguments.of("toDate=200-01-01"),
                Arguments.of("toDate=2000-1-01"),
                Arguments.of("toDate=2000-01-1"),
                Arguments.of("toDate=2000-42-01"),
                Arguments.of("toDate=2000-01-42"),
                Arguments.of("toDate=01-01-2000"),
                Arguments.of("toDate=20-01-01")
        );
    }

    private Stream<Arguments> generateWrongPassengerObjects() {
        DtoPassenger nullFirstName = new DtoPassenger(null, "lastNamePassenger_1", "passportPassenger_1");
        DtoErrorResponse dtoErrorNullFirstName = new DtoErrorResponse();
        dtoErrorNullFirstName.addError(
                new DtoErrorResponseElement(
                        ApplicationErrorDetails.VIOLATION_FIRST_NAME_EMPTY_CONSTRAINT.toString(),
                        ApplicationErrorDetails.VIOLATION_FIRST_NAME_EMPTY_CONSTRAINT.getField(),
                        ApplicationErrorDetails.VIOLATION_FIRST_NAME_EMPTY_CONSTRAINT.getMessage()
                )
        );

        DtoPassenger emptyFirstName = new DtoPassenger("   ", "lastNamePassenger_1", "passportPassenger_1");
        DtoErrorResponse dtoErrorEmptyFirstName = new DtoErrorResponse();
        dtoErrorEmptyFirstName.addError(
                new DtoErrorResponseElement(
                        ApplicationErrorDetails.VIOLATION_FIRST_NAME_EMPTY_CONSTRAINT.toString(),
                        ApplicationErrorDetails.VIOLATION_FIRST_NAME_EMPTY_CONSTRAINT.getField(),
                        ApplicationErrorDetails.VIOLATION_FIRST_NAME_EMPTY_CONSTRAINT.getMessage()
                )
        );

        DtoPassenger nullLastName = new DtoPassenger("firstNamePassenger_1", null, "passportPassenger_1");
        DtoErrorResponse dtoErrorNullLastName = new DtoErrorResponse();
        dtoErrorNullLastName.addError(
                new DtoErrorResponseElement(
                        ApplicationErrorDetails.VIOLATION_LAST_NAME_EMPTY_CONSTRAINT.toString(),
                        ApplicationErrorDetails.VIOLATION_LAST_NAME_EMPTY_CONSTRAINT.getField(),
                        ApplicationErrorDetails.VIOLATION_LAST_NAME_EMPTY_CONSTRAINT.getMessage()
                )
        );

        DtoPassenger emptyLastName = new DtoPassenger("firstNamePassenger_1", null, "passportPassenger_1");
        DtoErrorResponse dtoErrorEmptyLastName = new DtoErrorResponse();
        dtoErrorEmptyLastName.addError(
                new DtoErrorResponseElement(
                        ApplicationErrorDetails.VIOLATION_LAST_NAME_EMPTY_CONSTRAINT.toString(),
                        ApplicationErrorDetails.VIOLATION_LAST_NAME_EMPTY_CONSTRAINT.getField(),
                        ApplicationErrorDetails.VIOLATION_LAST_NAME_EMPTY_CONSTRAINT.getMessage()
                )
        );

        DtoPassenger nullPassport = new DtoPassenger("firstNamePassenger_1", "lastNamePassenger_1", null);
        DtoErrorResponse dtoErrorNullPassport = new DtoErrorResponse();
        dtoErrorNullPassport.addError(
                new DtoErrorResponseElement(
                        ApplicationErrorDetails.VIOLATION_PASSPORT_EMPTY_CONSTRAINT.toString(),
                        ApplicationErrorDetails.VIOLATION_PASSPORT_EMPTY_CONSTRAINT.getField(),
                        ApplicationErrorDetails.VIOLATION_PASSPORT_EMPTY_CONSTRAINT.getMessage()
                )
        );

        DtoPassenger emptyPassport = new DtoPassenger("firstNamePassenger_1", "lastNamePassenger_1", "  ");
        DtoErrorResponse dtoErrorEmptyPassport = new DtoErrorResponse();
        dtoErrorEmptyPassport.addError(
                new DtoErrorResponseElement(
                        ApplicationErrorDetails.VIOLATION_PASSPORT_EMPTY_CONSTRAINT.toString(),
                        ApplicationErrorDetails.VIOLATION_PASSPORT_EMPTY_CONSTRAINT.getField(),
                        ApplicationErrorDetails.VIOLATION_PASSPORT_EMPTY_CONSTRAINT.getMessage()
                )
        );


        return Stream.of(
                Arguments.of(nullFirstName, dtoErrorNullFirstName),
                Arguments.of(emptyFirstName, dtoErrorEmptyFirstName),
                Arguments.of(nullLastName, dtoErrorNullLastName),
                Arguments.of(emptyLastName, dtoErrorEmptyLastName),
                Arguments.of(nullPassport, dtoErrorNullPassport),
                Arguments.of(emptyPassport, dtoErrorEmptyPassport)
        );
    }

    private Stream<Arguments> generateWrongDateInfo() {
        return Stream.of(
                Arguments.of("2000:01:01"),
                Arguments.of("2000 01 01"),
                Arguments.of("200-01-01"),
                Arguments.of("20000-01-01"),
                Arguments.of("2000-1-01"),
                Arguments.of("2000-01-1"),
                Arguments.of("2000-33-01"),
                Arguments.of("2000-01-33"),
                Arguments.of("20-01-01"),
                Arguments.of("2000.01.01")
        );
    }

    private long addNewOrderAndGetItId() {
        DtoOrderRequest dtoOrderRequest = new DtoOrderRequest();
        dtoOrderRequest.setTripId(TRIP_ID);
        dtoOrderRequest.setDate("2000-01-01");
        List<DtoPassenger> passengerList = new ArrayList<>();
        passengerList.add(new DtoPassenger("firstNamePassenger_1", "lastNamePassenger_1", "passportPassenger_1"));
        passengerList.add(new DtoPassenger("firstNamePassenger_2", "lastNamePassenger_2", "passportPassenger_2"));
        dtoOrderRequest.setPassengers(passengerList);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.COOKIE, createHeaderForSessionId(FIRST_CLIENT_SESSION_ID));
        ResponseEntity<DtoOrderResponse> responseEntity = testRestTemplate.exchange(
                BASE_URL + PORT + API_ORDERS_URL,
                HttpMethod.POST,
                new HttpEntity<>(dtoOrderRequest, httpHeaders),
                DtoOrderResponse.class
        );
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        DtoOrderResponse dtoOrderResponse = responseEntity.getBody();
        assertNotNull(dtoOrderResponse);
        assertNotEquals(0, dtoOrderResponse.getOrderId());

        return dtoOrderResponse.getOrderId();
    }
}