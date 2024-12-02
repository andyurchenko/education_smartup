package edu.controller.e2e;

import edu.configuration.ApplicationConfig;
import edu.dto.error.DtoErrorResponse;
import edu.dto.error.DtoErrorResponseElement;
import edu.dto.trip.DtoSchedule;
import edu.dto.trip.DtoTripRequest;
import edu.dto.trip.DtoTripResponse;
import edu.dto.trip.bus.DtoBusRequest;
import edu.error.ApplicationErrorDetails;
import org.junit.jupiter.api.BeforeAll;
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
import org.springframework.http.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TripControllerWithRunningServerTest extends BaseTest{
    private final String FROM_STATION_NAME = "station_name_ONE";
    private final String TO_STATION_NAME = "station_name_TWO";
    private final String BUS_NAME_ONE = "bus_brand_name_ONE";
    private final int PLACE_COUNT_OF_BUS_NAME_ONE = 3;
    private String ADMIN_SESSION_ID;

    @Autowired
    protected TripControllerWithRunningServerTest(@LocalServerPort Integer port, TestRestTemplate testRestTemplate, ApplicationConfig appConfig) {
        super(port, testRestTemplate, appConfig);
    }

    @BeforeAll
    void clearDataBaseAndAddTestData() {
        testRestTemplate
                .exchange(
                        BASE_URL + PORT + DEBUG_URL,
                        HttpMethod.DELETE,
                        null,
                        String.class
                );

        ADMIN_SESSION_ID = addNewAdminAndGetItsSession();
        addNewNewStationToDb(FROM_STATION_NAME, ADMIN_SESSION_ID);
        addNewNewStationToDb(TO_STATION_NAME, ADMIN_SESSION_ID);
        addNewBusToDb(new DtoBusRequest(BUS_NAME_ONE, PLACE_COUNT_OF_BUS_NAME_ONE), ADMIN_SESSION_ID);
    }

    @BeforeEach
    void clearTripTableFroTests() {
        testRestTemplate
                .exchange(
                        BASE_URL + PORT + DEBUG_URL + "/table/trip",
                        HttpMethod.DELETE,
                        null,
                        String.class
                );
    }
    @Test
    void addNewTripWithScheduleEvenPositive() {
        DtoTripRequest dtoTripRequest = createDefaultDtoTripRequestWithSchedule();
        dtoTripRequest.setSchedule(new DtoSchedule("2000-01-01", "2000-01-10", "even"));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.COOKIE, createHeaderForSessionId(ADMIN_SESSION_ID));
        ResponseEntity<DtoTripResponse> responseEntity = testRestTemplate.exchange(
                BASE_URL + PORT + API_TRIPS_URL,
                HttpMethod.POST,
                new HttpEntity<>(dtoTripRequest, httpHeaders),
                DtoTripResponse.class
        );

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        DtoTripResponse dtoTripResponse = responseEntity.getBody();
        assertNotNull(dtoTripResponse);
        assertNotEquals(0, dtoTripResponse.getTripId());
        assertEquals(5, dtoTripResponse.getDates().size());
        assertEquals("2000-01-02", dtoTripResponse.getDates().get(0));
        assertEquals("2000-01-04", dtoTripResponse.getDates().get(1));
        assertEquals("2000-01-06", dtoTripResponse.getDates().get(2));
        assertEquals("2000-01-08", dtoTripResponse.getDates().get(3));
        assertEquals("2000-01-10", dtoTripResponse.getDates().get(4));
    }

    @Test
    void addNewTripWithScheduleOddPositive() {
        DtoTripRequest dtoTripRequest = createDefaultDtoTripRequestWithSchedule();
        dtoTripRequest.setSchedule(new DtoSchedule("2000-01-01", "2000-01-10", "odd"));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.COOKIE, createHeaderForSessionId(ADMIN_SESSION_ID));
        ResponseEntity<DtoTripResponse> responseEntity = testRestTemplate.exchange(
                BASE_URL + PORT + API_TRIPS_URL,
                HttpMethod.POST,
                new HttpEntity<>(dtoTripRequest, httpHeaders),
                DtoTripResponse.class
        );

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        DtoTripResponse dtoTripResponse = responseEntity.getBody();
        assertNotNull(dtoTripResponse);
        assertNotEquals(0, dtoTripResponse.getTripId());
        assertEquals(5, dtoTripResponse.getDates().size());
        assertEquals("2000-01-01", dtoTripResponse.getDates().get(0));
        assertEquals("2000-01-03", dtoTripResponse.getDates().get(1));
        assertEquals("2000-01-05", dtoTripResponse.getDates().get(2));
        assertEquals("2000-01-07", dtoTripResponse.getDates().get(3));
        assertEquals("2000-01-09", dtoTripResponse.getDates().get(4));
    }

    @Test
    void addNewTripWithScheduleDailyPositive() {
        DtoTripRequest dtoTripRequest = createDefaultDtoTripRequestWithSchedule();
        dtoTripRequest.setSchedule(new DtoSchedule("2000-01-01", "2000-01-05", "daily"));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.COOKIE, createHeaderForSessionId(ADMIN_SESSION_ID));
        ResponseEntity<DtoTripResponse> responseEntity = testRestTemplate.exchange(
                BASE_URL + PORT + API_TRIPS_URL,
                HttpMethod.POST,
                new HttpEntity<>(dtoTripRequest, httpHeaders),
                DtoTripResponse.class
        );

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        DtoTripResponse dtoTripResponse = responseEntity.getBody();
        assertNotNull(dtoTripResponse);
        assertNotEquals(0, dtoTripResponse.getTripId());
        assertEquals(5, dtoTripResponse.getDates().size());
        assertEquals("2000-01-01", dtoTripResponse.getDates().get(0));
        assertEquals("2000-01-02", dtoTripResponse.getDates().get(1));
        assertEquals("2000-01-03", dtoTripResponse.getDates().get(2));
        assertEquals("2000-01-04", dtoTripResponse.getDates().get(3));
        assertEquals("2000-01-05", dtoTripResponse.getDates().get(4));
    }

    @Test
    void addNewTripWithScheduleNumberOfDaysPositive() {
        DtoTripRequest dtoTripRequest = createDefaultDtoTripRequestWithSchedule();
        dtoTripRequest.setSchedule(new DtoSchedule("2000-01-01", "2000-01-05", "1,4,5"));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.COOKIE, createHeaderForSessionId(ADMIN_SESSION_ID));
        ResponseEntity<DtoTripResponse> responseEntity = testRestTemplate.exchange(
                BASE_URL + PORT + API_TRIPS_URL,
                HttpMethod.POST,
                new HttpEntity<>(dtoTripRequest, httpHeaders),
                DtoTripResponse.class
        );

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        DtoTripResponse dtoTripResponse = responseEntity.getBody();
        assertNotNull(dtoTripResponse);
        assertNotEquals(0, dtoTripResponse.getTripId());
        assertEquals(3, dtoTripResponse.getDates().size());
        assertEquals("2000-01-01", dtoTripResponse.getDates().get(0));
        assertEquals("2000-01-04", dtoTripResponse.getDates().get(1));
        assertEquals("2000-01-05", dtoTripResponse.getDates().get(2));
    }

    @Test
    void addNewTripWithDatesPositive() {
        DtoTripRequest dtoTripRequest = createDefaultDtoTripRequestWithDates();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.COOKIE, createHeaderForSessionId(ADMIN_SESSION_ID));
        ResponseEntity<DtoTripResponse> responseEntity = testRestTemplate.exchange(
                BASE_URL + PORT + API_TRIPS_URL,
                HttpMethod.POST,
                new HttpEntity<>(dtoTripRequest, httpHeaders),
                DtoTripResponse.class
        );

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        DtoTripResponse dtoTripResponse = responseEntity.getBody();
        assertNotNull(dtoTripResponse);
        assertNotEquals(0, dtoTripResponse.getTripId());
        assertEquals(dtoTripRequest.getDates().get(0), dtoTripResponse.getDates().get(0));
        assertEquals(dtoTripRequest.getDates().get(1), dtoTripResponse.getDates().get(1));
        assertEquals(dtoTripRequest.getDates().get(2), dtoTripResponse.getDates().get(2));
    }

    @ParameterizedTest
    @MethodSource("generateIncorrectDtoRegisterNewTripRequests")
    void addNewTripNegative(DtoTripRequest dtoTripRequest, DtoErrorResponse dtoErrorResponse) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.COOKIE, createHeaderForSessionId(ADMIN_SESSION_ID));
        ResponseEntity<DtoErrorResponse> responseEntity = testRestTemplate.exchange(
                BASE_URL + PORT + API_TRIPS_URL,
                HttpMethod.POST,
                new HttpEntity<>(dtoTripRequest, httpHeaders),
                DtoErrorResponse.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        DtoErrorResponse dtoErrorResponseFromServer = responseEntity.getBody();
        assertNotNull(dtoErrorResponseFromServer);
        List<DtoErrorResponseElement> errors = dtoErrorResponseFromServer.getErrors();
        assertNotNull(errors);
        assertEquals(1, errors.size());
        DtoErrorResponseElement element = errors.get(0);
        assertEquals(dtoErrorResponse.getErrors().get(0).getErrorCode(), element.getErrorCode());
        assertEquals(dtoErrorResponse.getErrors().get(0).getMessage(), element.getMessage());
        assertEquals(dtoErrorResponse.getErrors().get(0).getField(), element.getField());

    }

    @Test
    void updateTripWithScheduleByIdPositive() {
        DtoTripRequest dtoTripRequest = createDefaultDtoTripRequestWithSchedule();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.COOKIE, createHeaderForSessionId(ADMIN_SESSION_ID));
        ResponseEntity<DtoTripResponse> responseEntity = testRestTemplate.exchange(
                BASE_URL + PORT + API_TRIPS_URL,
                HttpMethod.POST,
                new HttpEntity<>(dtoTripRequest, httpHeaders),
                DtoTripResponse.class
        );

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        DtoTripResponse dtoTripResponse = responseEntity.getBody();
        assertNotNull(dtoTripResponse);
        assertNotEquals(0, dtoTripResponse.getTripId());

        long tripId = dtoTripResponse.getTripId();

        dtoTripRequest.setStart("23:00");
        dtoTripRequest.setDuration("42:00");
        DtoSchedule dtoSchedule = new DtoSchedule("2030-01-01", "2030-01-10", "daily");
        dtoTripRequest.setSchedule(dtoSchedule);
        responseEntity = testRestTemplate.exchange(
                BASE_URL + PORT + API_TRIPS_URL + "/" + dtoTripResponse.getTripId(),
                HttpMethod.PUT,
                new HttpEntity<>(dtoTripRequest, httpHeaders),
                DtoTripResponse.class
        );

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        dtoTripResponse = responseEntity.getBody();
        assertNotNull(dtoTripResponse);
        assertEquals(tripId, dtoTripResponse.getTripId());
        assertEquals(10, dtoTripResponse.getDates().size());
        assertEquals("2030-01-01", dtoTripResponse.getDates().get(0));
        assertEquals("2030-01-02", dtoTripResponse.getDates().get(1));
        assertEquals("2030-01-03", dtoTripResponse.getDates().get(2));
        assertEquals("2030-01-04", dtoTripResponse.getDates().get(3));
        assertEquals("2030-01-05", dtoTripResponse.getDates().get(4));
        assertEquals("2030-01-06", dtoTripResponse.getDates().get(5));
        assertEquals("2030-01-07", dtoTripResponse.getDates().get(6));
        assertEquals("2030-01-08", dtoTripResponse.getDates().get(7));
        assertEquals("2030-01-09", dtoTripResponse.getDates().get(8));
        assertEquals("2030-01-10", dtoTripResponse.getDates().get(9));
    }

    @Test
    void approveTripAndGetItByIdPositive() {
        DtoTripRequest dtoTripRequest = createDefaultDtoTripRequestWithSchedule();
        dtoTripRequest.setSchedule(new DtoSchedule("2000-01-01", "2000-01-10", "even"));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.COOKIE, createHeaderForSessionId(ADMIN_SESSION_ID));
        ResponseEntity<DtoTripResponse> responseEntity = testRestTemplate.exchange(
                BASE_URL + PORT + API_TRIPS_URL,
                HttpMethod.POST,
                new HttpEntity<>(dtoTripRequest, httpHeaders),
                DtoTripResponse.class
        );

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        DtoTripResponse dtoTripResponse = responseEntity.getBody();
        assertNotNull(dtoTripResponse);
        long tripId = dtoTripResponse.getTripId();
        assertNotEquals(0, tripId);
        ResponseEntity<String> responseEntityApproveTrip = testRestTemplate.exchange(
                BASE_URL + PORT + API_TRIPS_URL + "/" + tripId + "/approve",
                HttpMethod.PUT,
                new HttpEntity<>(httpHeaders),
                String.class
        );

        assertEquals(HttpStatus.OK, responseEntityApproveTrip.getStatusCode());
        assertEquals("{}", responseEntityApproveTrip.getBody());

        responseEntity = testRestTemplate.exchange(
                BASE_URL + PORT + API_TRIPS_URL + "/" + tripId,
                HttpMethod.GET,
                new HttpEntity<>(dtoTripRequest, httpHeaders),
                DtoTripResponse.class
        );
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        dtoTripResponse = responseEntity.getBody();
        assertNotNull(dtoTripResponse);
        assertTrue(dtoTripResponse.getApproved());

    }

    @Test
    void getTripByIdNegative() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.COOKIE, createHeaderForSessionId(ADMIN_SESSION_ID));
        ResponseEntity<DtoErrorResponse> responseEntity = testRestTemplate.exchange(
                BASE_URL + PORT + API_TRIPS_URL + "/" + "0",
                HttpMethod.GET,
                new HttpEntity<>(httpHeaders),
                DtoErrorResponse.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        DtoErrorResponse dtoErrorResponse = responseEntity.getBody();
        assertNotNull(dtoErrorResponse);
        List<DtoErrorResponseElement> errors = dtoErrorResponse.getErrors();
        assertEquals(1, errors.size());
        assertEquals(ApplicationErrorDetails.TRIP_ID_NOT_FOUND.toString(), errors.get(0).getErrorCode());
        assertEquals(ApplicationErrorDetails.TRIP_ID_NOT_FOUND.getField(), errors.get(0).getField());
    }

    private Stream<Arguments> generateIncorrectDtoRegisterNewTripRequests() {
        var dtoEmptyPrice = createDefaultDtoTripRequestWithSchedule();
        dtoEmptyPrice.setPrice(" ");
        var dtoNullPrice = createDefaultDtoTripRequestWithSchedule();
        dtoNullPrice.setPrice(null);
        var dtoErrorEmptyOrNullPrice = new DtoErrorResponse();
        dtoErrorEmptyOrNullPrice.addError(
                new DtoErrorResponseElement(
                        ApplicationErrorDetails.VIOLATION_PRICE_EMPTY_OR_PATTERN_CONSTRAINT.toString(),
                        ApplicationErrorDetails.VIOLATION_PRICE_EMPTY_OR_PATTERN_CONSTRAINT.getField(),
                ApplicationErrorDetails.VIOLATION_PRICE_EMPTY_OR_PATTERN_CONSTRAINT.getMessage()
                )
        );

        var dtoEmptyBusName = createDefaultDtoTripRequestWithSchedule();
        dtoEmptyBusName.setBusName(" ");
        var dtoNullBusName = createDefaultDtoTripRequestWithSchedule();
        dtoNullBusName.setBusName(null);
        var dtoErrorEmptyOrNullBusName = new DtoErrorResponse();
        dtoErrorEmptyOrNullBusName.addError(
                new DtoErrorResponseElement(
                        ApplicationErrorDetails.VIOLATION_BUS_NAME_EMPTY_CONSTRAINT.toString(),
                        ApplicationErrorDetails.VIOLATION_BUS_NAME_EMPTY_CONSTRAINT.getField(),
                        ApplicationErrorDetails.VIOLATION_BUS_NAME_EMPTY_CONSTRAINT.getMessage()
                )
        );

        var dtoEmptyFromStation = createDefaultDtoTripRequestWithSchedule();
        dtoEmptyFromStation.setFromStation(" ");
        var dtoNullFromStation = createDefaultDtoTripRequestWithSchedule();
        dtoNullFromStation.setFromStation(null);
        var dtoErrorEmptyOrNullFromStation = new DtoErrorResponse();
        dtoErrorEmptyOrNullFromStation.addError(
                new DtoErrorResponseElement(
                        ApplicationErrorDetails.VIOLATION_STATION_EMPTY_CONSTRAINT.toString(),
                        ApplicationErrorDetails.VIOLATION_STATION_EMPTY_CONSTRAINT.getField(),
                        ApplicationErrorDetails.VIOLATION_STATION_EMPTY_CONSTRAINT.getMessage()
                )
        );

        var dtoEmptyToStation = createDefaultDtoTripRequestWithSchedule();
        dtoEmptyToStation.setToStation(" ");
        var dtoNullToStation = createDefaultDtoTripRequestWithSchedule();
        dtoNullToStation.setToStation(null);
        var dtoErrorEmptyOrNullToStation = new DtoErrorResponse();
        dtoErrorEmptyOrNullToStation.addError(
                new DtoErrorResponseElement(
                        ApplicationErrorDetails.VIOLATION_STATION_EMPTY_CONSTRAINT.toString(),
                        ApplicationErrorDetails.VIOLATION_STATION_EMPTY_CONSTRAINT.getField(),
                        ApplicationErrorDetails.VIOLATION_STATION_EMPTY_CONSTRAINT.getMessage()
                )
        );

        var dtoEmptyStart = createDefaultDtoTripRequestWithSchedule();
        dtoEmptyStart.setStart(" ");
        var dtoNullStart = createDefaultDtoTripRequestWithSchedule();
        dtoNullStart.setStart(null);
        var dtoWrongPatternStart1 = createDefaultDtoTripRequestWithSchedule();
        dtoWrongPatternStart1.setStart("13-30");
        var dtoWrongPatternStart2 = createDefaultDtoTripRequestWithSchedule();
        dtoWrongPatternStart2.setStart("25:15");
        var dtoWrongPatternStart3 = createDefaultDtoTripRequestWithSchedule();
        dtoWrongPatternStart3.setStart("15:65");
        var dtoWrongPatternStart4 = createDefaultDtoTripRequestWithSchedule();
        dtoWrongPatternStart4.setStart("-15:15");
        var dtoErrorStart = new DtoErrorResponse();
        dtoErrorStart.addError(
                new DtoErrorResponseElement(
                        ApplicationErrorDetails.VIOLATION_START_TIME_EMPTY_OR_PATTERN_CONSTRAINT.toString(),
                        ApplicationErrorDetails.VIOLATION_START_TIME_EMPTY_OR_PATTERN_CONSTRAINT.getField(),
                        ApplicationErrorDetails.VIOLATION_START_TIME_EMPTY_OR_PATTERN_CONSTRAINT.getMessage()
                )
        );

        var dtoEmptyDuration = createDefaultDtoTripRequestWithSchedule();
        dtoEmptyDuration.setDuration(" ");
        var dtoNullDuration = createDefaultDtoTripRequestWithSchedule();
        dtoNullDuration.setDuration(null);
        var dtoWrongPatternDuration1 = createDefaultDtoTripRequestWithSchedule();
        dtoWrongPatternDuration1.setDuration("13-30");
        var dtoWrongPatternDuration2 = createDefaultDtoTripRequestWithSchedule();
        dtoWrongPatternDuration2.setDuration("25:65");
        var dtoWrongPatternDuration3 = createDefaultDtoTripRequestWithSchedule();
        dtoWrongPatternDuration3.setDuration("-15:5");
        var dtoWrongPatternDuration4 = createDefaultDtoTripRequestWithSchedule();
        dtoWrongPatternDuration4.setDuration("15:015");
        var dtoErrorDuration = new DtoErrorResponse();
        dtoErrorDuration.addError(
                new DtoErrorResponseElement(
                        ApplicationErrorDetails.VIOLATION_DURATION_TIME_EMPTY_OR_PATTERN_CONSTRAINT.toString(),
                        ApplicationErrorDetails.VIOLATION_DURATION_TIME_EMPTY_OR_PATTERN_CONSTRAINT.getField(),
                        ApplicationErrorDetails.VIOLATION_DURATION_TIME_EMPTY_OR_PATTERN_CONSTRAINT.getMessage()
                )
        );


        var dtoNullFromDate = createDefaultDtoTripRequestWithSchedule();
        var dtoSchedule = dtoNullFromDate.getSchedule();
        dtoSchedule.setFromDate(null);
        var dtoEmptyFromDate = createDefaultDtoTripRequestWithSchedule();
        dtoSchedule = dtoEmptyFromDate.getSchedule();
        dtoSchedule.setFromDate(" ");
        var dtoWrongPatternFromDate1 = createDefaultDtoTripRequestWithSchedule();
        dtoSchedule = dtoWrongPatternFromDate1.getSchedule();
        dtoSchedule.setFromDate("2024:08:20");
        var dtoWrongPatternFromDate2 = createDefaultDtoTripRequestWithSchedule();
        dtoSchedule = dtoWrongPatternFromDate2.getSchedule();
        dtoSchedule.setFromDate("2024-08-32");
        var dtoWrongPatternFromDate3 = createDefaultDtoTripRequestWithSchedule();
        dtoSchedule = dtoWrongPatternFromDate3.getSchedule();
        dtoSchedule.setFromDate("2024-13-01");
        var dtoWrongPatternFromDate4 = createDefaultDtoTripRequestWithSchedule();
        dtoSchedule = dtoWrongPatternFromDate4.getSchedule();
        dtoSchedule.setFromDate("24-12-01");
        var dtoWrongPatternFromDate5 = createDefaultDtoTripRequestWithSchedule();
        dtoSchedule = dtoWrongPatternFromDate5.getSchedule();
        dtoSchedule.setFromDate("24-12-1");
        var dtoWrongPatternFromDate6 = createDefaultDtoTripRequestWithSchedule();
        dtoSchedule = dtoWrongPatternFromDate6.getSchedule();
        dtoSchedule.setFromDate("24-2-01");
        var dtoErrorFromDate = new DtoErrorResponse();
        dtoErrorFromDate.addError(
                new DtoErrorResponseElement(
                        ApplicationErrorDetails.VIOLATION_DATE_PATTERN_OR_EMPTY_CONSTRAINT.toString(),
                        ApplicationErrorDetails.VIOLATION_DATE_PATTERN_OR_EMPTY_CONSTRAINT.getField(),
                        ApplicationErrorDetails.VIOLATION_DATE_PATTERN_OR_EMPTY_CONSTRAINT.getMessage()
                )
        );

        var dtoNullToDate = createDefaultDtoTripRequestWithSchedule();
        dtoSchedule = dtoNullToDate.getSchedule();
        dtoSchedule.setFromDate(null);
        var dtoEmptyToDate = createDefaultDtoTripRequestWithSchedule();
        dtoSchedule = dtoEmptyToDate.getSchedule();
        dtoSchedule.setFromDate(" ");
        var dtoWrongPatternToDate1 = createDefaultDtoTripRequestWithSchedule();
        dtoSchedule = dtoWrongPatternToDate1.getSchedule();
        dtoSchedule.setFromDate("2024:08:20");
        var dtoWrongPatternToDate2 = createDefaultDtoTripRequestWithSchedule();
        dtoSchedule = dtoWrongPatternToDate2.getSchedule();
        dtoSchedule.setFromDate("2024-08-32");
        var dtoWrongPatternToDate3 = createDefaultDtoTripRequestWithSchedule();
        dtoSchedule = dtoWrongPatternToDate3.getSchedule();
        dtoSchedule.setFromDate("2024-13-01");
        var dtoWrongPatternToDate4 = createDefaultDtoTripRequestWithSchedule();
        dtoSchedule = dtoWrongPatternToDate4.getSchedule();
        dtoSchedule.setFromDate("24-12-01");
        var dtoWrongPatternToDate5 = createDefaultDtoTripRequestWithSchedule();
        dtoSchedule = dtoWrongPatternToDate5.getSchedule();
        dtoSchedule.setFromDate("24-12-1");
        var dtoWrongPatternToDate6 = createDefaultDtoTripRequestWithSchedule();
        dtoSchedule = dtoWrongPatternToDate6.getSchedule();
        dtoSchedule.setFromDate("24-2-01");
        var dtoErrorToDate = new DtoErrorResponse();
        dtoErrorToDate.addError(
                new DtoErrorResponseElement(
                        ApplicationErrorDetails.VIOLATION_DATE_PATTERN_OR_EMPTY_CONSTRAINT.toString(),
                        ApplicationErrorDetails.VIOLATION_DATE_PATTERN_OR_EMPTY_CONSTRAINT.getField(),
                        ApplicationErrorDetails.VIOLATION_DATE_PATTERN_OR_EMPTY_CONSTRAINT.getMessage()
                )
        );

        var dtoNullPeriod = createDefaultDtoTripRequestWithSchedule();
        dtoSchedule = dtoNullPeriod.getSchedule();
        dtoSchedule.setPeriod(null);
        var dtoEmptyPeriod = createDefaultDtoTripRequestWithSchedule();
        dtoSchedule = dtoEmptyPeriod.getSchedule();
        dtoSchedule.setPeriod(" ");
        var dtoWrongPatternPeriod1 = createDefaultDtoTripRequestWithSchedule();
        dtoSchedule = dtoWrongPatternPeriod1.getSchedule();
        dtoSchedule.setPeriod("dayli");
        var dtoWrongPatternPeriod2 = createDefaultDtoTripRequestWithSchedule();
        dtoSchedule = dtoWrongPatternPeriod2.getSchedule();
        dtoSchedule.setPeriod("mon, tue, mon, wed");
        var dtoWrongPatternPeriod3 = createDefaultDtoTripRequestWithSchedule();
        dtoSchedule = dtoWrongPatternPeriod3.getSchedule();
        dtoSchedule.setPeriod("mon, tue wed");
        var dtoWrongPatternPeriod4 = createDefaultDtoTripRequestWithSchedule();
        dtoSchedule = dtoWrongPatternPeriod4.getSchedule();
        dtoSchedule.setPeriod("1, 2, 2, 3, 4, 5");
        var dtoWrongPatternPeriod5 = createDefaultDtoTripRequestWithSchedule();
        dtoSchedule = dtoWrongPatternPeriod5.getSchedule();
        dtoSchedule.setPeriod("mon, tuewed");
        var dtoErrorWrongPatternPeriod = new DtoErrorResponse();
        dtoErrorWrongPatternPeriod.addError(
                new DtoErrorResponseElement(
                        ApplicationErrorDetails.VIOLATION_PERIOD_EMPTY_OR_PATTERN_CONSTRAINT.toString(),
                        ApplicationErrorDetails.VIOLATION_PERIOD_EMPTY_OR_PATTERN_CONSTRAINT.getField(),
                        ApplicationErrorDetails.VIOLATION_PERIOD_EMPTY_OR_PATTERN_CONSTRAINT.getMessage()
                )
        );

        var dtoWrongDates = createDefaultDtoTripRequestWithSchedule();
        dtoSchedule = dtoWrongDates.getSchedule();
        dtoSchedule.setFromDate("2024-08-20");
        dtoSchedule.setToDate("2024-08-15");
        var dtoErrorWrongDates = new DtoErrorResponse();
        dtoErrorWrongDates.addError(
                new DtoErrorResponseElement(
                        ApplicationErrorDetails.TO_DATE_BEFORE_FROM_DATE_IN_SCHEDULE.toString(),
                        ApplicationErrorDetails.TO_DATE_BEFORE_FROM_DATE_IN_SCHEDULE.getField(),
                        ApplicationErrorDetails.TO_DATE_BEFORE_FROM_DATE_IN_SCHEDULE.getMessage()
                )
        );

        var dtoScheduleAndDatesTogether = createDefaultDtoTripRequestWithSchedule();
        dtoScheduleAndDatesTogether.setDates(Arrays.asList("2025-08-20", "2025-08-22", "2025-08-24"));
        var dtoErrorScheduleAndDatesTogether = new DtoErrorResponse();
        dtoErrorScheduleAndDatesTogether.addError(
                new DtoErrorResponseElement(
                        ApplicationErrorDetails.VIOLATION_SCHEDULE_AND_DATES_TOGETHER_PRESENT_OR_EMPTY_CONSTRAINT.toString(),
                        ApplicationErrorDetails.VIOLATION_SCHEDULE_AND_DATES_TOGETHER_PRESENT_OR_EMPTY_CONSTRAINT.getField(),
                        ApplicationErrorDetails.VIOLATION_SCHEDULE_AND_DATES_TOGETHER_PRESENT_OR_EMPTY_CONSTRAINT.getMessage()
                )
        );

        var dtoWongDatesPattern1 = createDefaultDtoTripRequestWithDates();
        dtoWongDatesPattern1.getDates().add("25-01-01");
        var dtoWongDatesPattern2 = createDefaultDtoTripRequestWithDates();
        dtoWongDatesPattern2.getDates().add("2025-13-01");
        var dtoWongDatesPattern3 = createDefaultDtoTripRequestWithDates();
        dtoWongDatesPattern3.getDates().add("2025-01-32");
        var dtoWongDatesPattern4 = createDefaultDtoTripRequestWithDates();
        dtoWongDatesPattern4.getDates().add("2025-01-1");
        var dtoWongDatesPattern5 = createDefaultDtoTripRequestWithDates();
        dtoWongDatesPattern5.getDates().add("2025-1-01");
        var dtoWongDatesPattern6 = createDefaultDtoTripRequestWithDates();
        dtoWongDatesPattern6.getDates().add("2025:01:01");
        var dtoWongDatesPattern7 = createDefaultDtoTripRequestWithDates();
        dtoWongDatesPattern7.getDates().add("2025-01-101");
        var dtoWongDatesPattern8 = createDefaultDtoTripRequestWithDates();
        dtoWongDatesPattern8.getDates().add("2025-101-11");
        var dtoWongDatesPattern9 = createDefaultDtoTripRequestWithDates();
        dtoWongDatesPattern9.getDates().add("22025-10-11");
        var dtoErrorWongDates = new DtoErrorResponse();
        dtoErrorWongDates.addError(
                new DtoErrorResponseElement(
                        ApplicationErrorDetails.VIOLATION_DATE_PATTERN_OR_EMPTY_CONSTRAINT.toString(),
                        ApplicationErrorDetails.VIOLATION_DATE_PATTERN_OR_EMPTY_CONSTRAINT.getField(),
                        ApplicationErrorDetails.VIOLATION_DATE_PATTERN_OR_EMPTY_CONSTRAINT.getMessage()
                )
        );

        var dtoWongDatesPattern10 = createDefaultDtoTripRequestWithDates();
        dtoWongDatesPattern10.setDates(null);
        var dtoErrorWongDates2 = new DtoErrorResponse();
        dtoErrorWongDates2.addError(
                new DtoErrorResponseElement(
                        ApplicationErrorDetails.VIOLATION_SCHEDULE_AND_DATES_TOGETHER_PRESENT_OR_EMPTY_CONSTRAINT.toString(),
                        ApplicationErrorDetails.VIOLATION_SCHEDULE_AND_DATES_TOGETHER_PRESENT_OR_EMPTY_CONSTRAINT.getField(),
                        ApplicationErrorDetails.VIOLATION_SCHEDULE_AND_DATES_TOGETHER_PRESENT_OR_EMPTY_CONSTRAINT.getMessage()
                )
        );

        var dtoWongDatesPattern11 = createDefaultDtoTripRequestWithDates();
        dtoWongDatesPattern11.getDates().clear();
        var dtoErrorWongDates3 = new DtoErrorResponse();
        dtoErrorWongDates3.addError(
                new DtoErrorResponseElement(
                        ApplicationErrorDetails.VIOLATION_DATE_PATTERN_OR_EMPTY_CONSTRAINT.toString(),
                        ApplicationErrorDetails.VIOLATION_DATE_PATTERN_OR_EMPTY_CONSTRAINT.getField(),
                        ApplicationErrorDetails.VIOLATION_DATE_PATTERN_OR_EMPTY_CONSTRAINT.getMessage()
                )
        );

        return Stream.of(
                Arguments.of(dtoEmptyPrice, dtoErrorEmptyOrNullPrice),
                Arguments.of(dtoNullPrice, dtoErrorEmptyOrNullPrice),

                Arguments.of(dtoEmptyBusName, dtoErrorEmptyOrNullBusName),
                Arguments.of(dtoNullBusName, dtoErrorEmptyOrNullBusName),

                Arguments.of(dtoEmptyFromStation, dtoErrorEmptyOrNullFromStation),
                Arguments.of(dtoNullFromStation, dtoErrorEmptyOrNullFromStation),
                Arguments.of(dtoEmptyToStation, dtoErrorEmptyOrNullToStation),
                Arguments.of(dtoNullToStation, dtoErrorEmptyOrNullToStation),

                Arguments.of(dtoEmptyStart, dtoErrorStart),
                Arguments.of(dtoNullStart, dtoErrorStart),
                Arguments.of(dtoWrongPatternStart1, dtoErrorStart),
                Arguments.of(dtoWrongPatternStart2, dtoErrorStart),
                Arguments.of(dtoWrongPatternStart3, dtoErrorStart),
                Arguments.of(dtoWrongPatternStart4, dtoErrorStart),

                Arguments.of(dtoEmptyDuration, dtoErrorDuration),
                Arguments.of(dtoNullDuration, dtoErrorDuration),
                Arguments.of(dtoWrongPatternDuration1, dtoErrorDuration),
                Arguments.of(dtoWrongPatternDuration2, dtoErrorDuration),
                Arguments.of(dtoWrongPatternDuration3, dtoErrorDuration),
                Arguments.of(dtoWrongPatternDuration4, dtoErrorDuration),

                Arguments.of(dtoNullFromDate, dtoErrorFromDate),
                Arguments.of(dtoEmptyFromDate, dtoErrorFromDate),
                Arguments.of(dtoWrongPatternFromDate1, dtoErrorFromDate),
                Arguments.of(dtoWrongPatternFromDate2, dtoErrorFromDate),
                Arguments.of(dtoWrongPatternFromDate3, dtoErrorFromDate),
                Arguments.of(dtoWrongPatternFromDate4, dtoErrorFromDate),
                Arguments.of(dtoWrongPatternFromDate5, dtoErrorFromDate),
                Arguments.of(dtoWrongPatternFromDate6, dtoErrorFromDate),

                Arguments.of(dtoNullToDate, dtoErrorToDate),
                Arguments.of(dtoEmptyToDate, dtoErrorToDate),
                Arguments.of(dtoWrongPatternToDate1, dtoErrorToDate),
                Arguments.of(dtoWrongPatternToDate2, dtoErrorToDate),
                Arguments.of(dtoWrongPatternToDate3, dtoErrorToDate),
                Arguments.of(dtoWrongPatternToDate4, dtoErrorToDate),
                Arguments.of(dtoWrongPatternToDate5, dtoErrorToDate),
                Arguments.of(dtoWrongPatternToDate6, dtoErrorToDate),

                Arguments.of(dtoNullPeriod, dtoErrorWrongPatternPeriod),
                Arguments.of(dtoEmptyPeriod, dtoErrorWrongPatternPeriod),
                Arguments.of(dtoWrongPatternPeriod1, dtoErrorWrongPatternPeriod),
                Arguments.of(dtoWrongPatternPeriod2, dtoErrorWrongPatternPeriod),
                Arguments.of(dtoWrongPatternPeriod3, dtoErrorWrongPatternPeriod),
                Arguments.of(dtoWrongPatternPeriod4, dtoErrorWrongPatternPeriod),
                Arguments.of(dtoWrongPatternPeriod5, dtoErrorWrongPatternPeriod),

                Arguments.of(dtoWrongDates, dtoErrorWrongDates),

                Arguments.of(dtoScheduleAndDatesTogether, dtoErrorScheduleAndDatesTogether),

                Arguments.of(dtoWongDatesPattern1, dtoErrorWongDates),
                Arguments.of(dtoWongDatesPattern2, dtoErrorWongDates),
                Arguments.of(dtoWongDatesPattern3, dtoErrorWongDates),
                Arguments.of(dtoWongDatesPattern4, dtoErrorWongDates),
                Arguments.of(dtoWongDatesPattern5, dtoErrorWongDates),
                Arguments.of(dtoWongDatesPattern6, dtoErrorWongDates),
                Arguments.of(dtoWongDatesPattern7, dtoErrorWongDates),
                Arguments.of(dtoWongDatesPattern8, dtoErrorWongDates),
                Arguments.of(dtoWongDatesPattern9, dtoErrorWongDates),

                Arguments.of(dtoWongDatesPattern10, dtoErrorWongDates2),
                Arguments.of(dtoWongDatesPattern11, dtoErrorWongDates3)
        );
    }

    private DtoTripRequest createDefaultDtoTripRequestWithSchedule() {
        DtoTripRequest dto = new DtoTripRequest();
        dto.setSchedule(new DtoSchedule("2024-01-01", "2024-12-31", "daily"));
        dto.setBusName(BUS_NAME_ONE);
        dto.setPrice("42.42");
        dto.setFromStation(FROM_STATION_NAME);
        dto.setToStation(TO_STATION_NAME);
        dto.setStart("00:00");
        dto.setDuration("42:00");

        return dto;
    }

    private DtoTripRequest createDefaultDtoTripRequestWithDates() {
        DtoTripRequest dto = new DtoTripRequest();
        dto.setBusName(BUS_NAME_ONE);
        dto.setPrice("42.42");
        dto.setFromStation(FROM_STATION_NAME);
        dto.setToStation(TO_STATION_NAME);
        dto.setStart("00:00");
        dto.setDuration("42:00");
        ArrayList<String> list = new ArrayList<>();
        list.add("2000-01-01");
        list.add("2000-01-02");
        list.add("2000-01-03");
        dto.setDates(list);

        return dto;
    }
}