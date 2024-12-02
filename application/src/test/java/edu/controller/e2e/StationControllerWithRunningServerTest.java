package edu.controller.e2e;

import edu.configuration.ApplicationConfig;
import edu.dto.error.DtoErrorResponse;
import edu.dto.error.DtoErrorResponseElement;
import edu.dto.trip.station.DtoStationRequest;
import edu.dto.trip.station.DtoStationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StationControllerWithRunningServerTest extends BaseTest {
    @Autowired
    protected StationControllerWithRunningServerTest(@LocalServerPort Integer port, TestRestTemplate testRestTemplate, ApplicationConfig appConfig) {
        super(port, testRestTemplate, appConfig);
    }

    @BeforeEach
    void clearDataBase() {
        testRestTemplate
                .exchange(
                        BASE_URL + PORT + DEBUG_URL,
                        HttpMethod.DELETE,
                        null,
                        String.class
                );
    }
    @Test
    void createNewStation_POSITIVE() {
        String sessionId = addNewAdminAndGetItsSession();
        String stationName = "test_station_name_1";
        DtoStationRequest dtoRequest = new DtoStationRequest(stationName);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.COOKIE, createHeaderForSessionId(sessionId));
        ResponseEntity<DtoStationResponse> response = testRestTemplate.exchange(
                BASE_URL + PORT + API_STATIONS_URL,
                HttpMethod.POST,
                new HttpEntity<>(dtoRequest, httpHeaders),
                DtoStationResponse.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        DtoStationResponse dtoResponse = response.getBody();
        assertNotNull(dtoResponse);
        assertNotEquals(0, dtoResponse.getId());
        assertEquals(stationName, dtoResponse.getName());
    }

    @Test
    void createNewStationDuplicateNameNegative() {
        String sessionId = addNewAdminAndGetItsSession();
        String stationName = "test_station_name_1";
        DtoStationRequest dtoRequest = new DtoStationRequest(stationName);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.COOKIE, createHeaderForSessionId(sessionId));
        ResponseEntity<DtoStationResponse> responseEntity = testRestTemplate.exchange(
                BASE_URL + PORT + API_STATIONS_URL,
                HttpMethod.POST,
                new HttpEntity<>(dtoRequest, httpHeaders),
                DtoStationResponse.class
        );
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        DtoStationResponse dtoResponse = responseEntity.getBody();
        assertNotNull(dtoResponse);
        assertNotEquals(0, dtoResponse.getId());
        assertEquals(stationName, dtoResponse.getName());

        ResponseEntity<DtoErrorResponse> responseEntityError = testRestTemplate.exchange(
                BASE_URL + PORT + API_STATIONS_URL,
                HttpMethod.POST,
                new HttpEntity<>(dtoRequest, httpHeaders),
                DtoErrorResponse.class
        );

        assertEquals(HttpStatus.CONFLICT, responseEntityError.getStatusCode());
        DtoErrorResponse dtoErrorResponse = responseEntityError.getBody();
        assertNotNull(dtoErrorResponse);
        List<DtoErrorResponseElement> errorResponseElements = dtoErrorResponse.getErrors();
        assertEquals(1, errorResponseElements.size());
        assertEquals("STATION_NAME_ALREADY_EXISTS", errorResponseElements.get(0).getErrorCode());
        assertEquals("name", errorResponseElements.get(0).getField());
        assertEquals("Station name [test_station_name_1] already exists.", errorResponseElements.get(0).getMessage());
    }



    @Test
    void getStationByIdPositive() {
        String adminSessionId = addNewAdminAndGetItsSession();
        String stationName = "new_station";
        long stationId = addNewStationAndGetItsId(stationName, adminSessionId);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.COOKIE, createHeaderForSessionId(adminSessionId));
        ResponseEntity<DtoStationResponse> responseEntity = testRestTemplate.exchange(
                BASE_URL + PORT + API_STATIONS_URL + "/" + stationId,
                HttpMethod.GET,
                new HttpEntity<>(httpHeaders),
                DtoStationResponse.class
        );
        DtoStationResponse dtoResponse = responseEntity.getBody();
        assertNotNull(dtoResponse);
        assertEquals(stationId, dtoResponse.getId());
        assertEquals(stationName, dtoResponse.getName());
    }

    @Test
    void getStationByIdNegative() {
        String adminSessionId = addNewAdminAndGetItsSession();
        long stationId = 0;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.COOKIE, createHeaderForSessionId(adminSessionId));
        ResponseEntity<DtoStationResponse> responseEntity = testRestTemplate.exchange(
                BASE_URL + PORT + API_STATIONS_URL + "/" + stationId,
                HttpMethod.GET,
                new HttpEntity<>(httpHeaders),
                DtoStationResponse.class
        );
        DtoStationResponse dtoResponse = responseEntity.getBody();
        assertNull(dtoResponse);
    }

    @Test
    void deleteStationById() {
        String adminSessionId = addNewAdminAndGetItsSession();
        String stationName = "new_station";
        long stationId = addNewStationAndGetItsId(stationName, adminSessionId);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.COOKIE, createHeaderForSessionId(adminSessionId));
        ResponseEntity<String> responseEntityDelete = testRestTemplate.exchange(
                BASE_URL + PORT + API_STATIONS_URL + "/" + stationId,
                HttpMethod.DELETE,
                new HttpEntity<>(httpHeaders),
                String.class
        );
        String response = responseEntityDelete.getBody();
        assertNotNull(response);
        assertEquals("{}", response);
        ResponseEntity<DtoStationResponse> responseEntityGet = testRestTemplate.exchange(
                BASE_URL + PORT + API_STATIONS_URL + "/" + stationId,
                HttpMethod.GET,
                new HttpEntity<>(httpHeaders),
                DtoStationResponse.class
        );
        DtoStationResponse dtoResponse = responseEntityGet.getBody();
        assertNull(dtoResponse);
    }

    @Test
    void updateStationPositive() {
        String adminSessionId = addNewAdminAndGetItsSession();
        String stationName = "old_station";
        long stationId = addNewStationAndGetItsId(stationName, adminSessionId);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.COOKIE, createHeaderForSessionId(adminSessionId));
        String newStationName = "new_station";
        DtoStationRequest dtoStationRequest = new DtoStationRequest(newStationName);
        ResponseEntity<DtoStationResponse> responseEntityDelete = testRestTemplate.exchange(
                BASE_URL + PORT + API_STATIONS_URL + "/" + stationId,
                HttpMethod.PUT,
                new HttpEntity<>(dtoStationRequest, httpHeaders),
                DtoStationResponse.class
        );
        DtoStationResponse dtoResponse = responseEntityDelete.getBody();
        assertNotNull(dtoResponse);
        assertEquals(newStationName, dtoResponse.getName());
    }

    @Test
    void updateStationDuplicateNameNegative() {
        String adminSessionId = addNewAdminAndGetItsSession();
        String stationNameOne = "station_name_ONE";
        long stationOneId = addNewStationAndGetItsId(stationNameOne, adminSessionId);
        String stationNameTwo = "station_name_TWO";
        long stationTwoId = addNewStationAndGetItsId(stationNameTwo, adminSessionId);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.COOKIE, createHeaderForSessionId(adminSessionId));
        DtoStationRequest dtoStationRequest = new DtoStationRequest(stationNameTwo);
        ResponseEntity<DtoErrorResponse> responseEntityDelete = testRestTemplate.exchange(
                BASE_URL + PORT + API_STATIONS_URL + "/" + stationOneId,
                HttpMethod.PUT,
                new HttpEntity<>(dtoStationRequest, httpHeaders),
                DtoErrorResponse.class
        );
        DtoErrorResponse dtoErrorResponse = responseEntityDelete.getBody();
        assertNotNull(dtoErrorResponse);
        List<DtoErrorResponseElement> errorResponseElements = dtoErrorResponse.getErrors();
        assertEquals(1, errorResponseElements.size());
        assertEquals("STATION_NAME_ALREADY_EXISTS", errorResponseElements.get(0).getErrorCode());
        assertEquals("name", errorResponseElements.get(0).getField());
        assertEquals("Station name [station_name_TWO] already exists.", errorResponseElements.get(0).getMessage());
    }
}