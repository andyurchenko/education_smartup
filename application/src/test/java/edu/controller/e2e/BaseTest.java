package edu.controller.e2e;

import edu.configuration.ApplicationConfig;
import edu.dto.administrator.DtoRegisterNewAdministratorResponse;
import edu.dto.client.DtoRegisterNewClientResponse;
import edu.dto.order.DtoOrderRequest;
import edu.dto.order.DtoOrderResponse;
import edu.dto.trip.DtoTripRequest;
import edu.dto.trip.bus.DtoBusRequest;
import edu.dto.trip.bus.DtoBusResponse;
import edu.dto.trip.station.DtoStationRequest;
import edu.dto.trip.station.DtoStationResponse;
import edu.util.AdminTestData;
import edu.util.ClientTestData;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

public abstract class BaseTest {
    protected final Integer PORT;
    protected final TestRestTemplate testRestTemplate;

    protected final ApplicationConfig appConfig;
    protected final String BASE_URL = "http://127.0.0.1:";
    protected final String DEBUG_URL = "/debug/db";
    protected final String API_ADMINS_URL = "/api/admins";
    protected final String API_CLIENTS_URL = "/api/clients";
    protected final String API_TRIPS_URL = "/api/trips";
    protected final String API_STATIONS_URL = "/api/stations";
    protected final String API_BUSES_URL = "/api/buses";
    protected final String API_ORDERS_URL = "/api/orders";
    protected final String API_PLACES_URL = "/api/places";
    protected final String API_SETTINGS_URL = "/api/settings";
    protected final String API_SESSIONS_URL = "/api/sessions";
    protected final String API_ACCOUNTS_URL = "/api/accounts";



    protected BaseTest(Integer port, TestRestTemplate testRestTemplate, ApplicationConfig appConfig) {
        PORT = port;
        this.testRestTemplate = testRestTemplate;
        this.appConfig = appConfig;
    }

    protected String createHeaderForSessionId(String sessionId) {
        return appConfig.getJavaSessionIdName() + "=" + sessionId;
    }

    protected String addNewAdminAndGetItsSession() {
        var dto = AdminTestData.createValidRegisterNewAdministratorDtoRequest();
        ResponseEntity<DtoRegisterNewAdministratorResponse> responseEntity = testRestTemplate
                .exchange(
                        BASE_URL + PORT + API_ADMINS_URL,
                        HttpMethod.POST,
                        new HttpEntity<>(dto),
                        DtoRegisterNewAdministratorResponse.class
                );
        String cookie = responseEntity.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        assertNotNull(cookie);
        assertFalse(cookie.isEmpty());

        return getSessionIdFromCookie(cookie);
    }

    protected String addAdminToDbAndGetItsSessionId(String suffix) {
        var request = AdminTestData.createValidRegisterNewAdministratorDtoRequest(suffix);
        var httpEntity = new HttpEntity<>(request);
        ResponseEntity<DtoRegisterNewAdministratorResponse> responseEntity = testRestTemplate
                .exchange(
                        BASE_URL + PORT + API_ADMINS_URL,
                        HttpMethod.POST,
                        httpEntity,
                        DtoRegisterNewAdministratorResponse.class
                );
        HttpHeaders headers = responseEntity.getHeaders();
        String cookie = headers.getFirst(HttpHeaders.SET_COOKIE);
        assertNotNull(cookie);
        Pattern pattern = Pattern.compile("(?<cookieName>" + appConfig.getJavaSessionIdName() + ")=(?<cookieValue>[a-zA-Z0-9-]+);");
        Matcher matcher = pattern.matcher(cookie);
        assertTrue(matcher.find());
        assertEquals(appConfig.getJavaSessionIdName(), matcher.group("cookieName"));

        return matcher.group("cookieValue");
    }

    protected String addNewClientAndGetItsSession(String suffix) {
        var request = ClientTestData.createValidRegisterNewClientDtoRequest(suffix);
        ResponseEntity<DtoRegisterNewClientResponse> responseEntity = testRestTemplate
                .postForEntity(BASE_URL + PORT + API_CLIENTS_URL, request, DtoRegisterNewClientResponse.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        var dtoResponse = responseEntity.getBody();
        assertNotNull(dtoResponse);
        String cookie = responseEntity.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        assertNotNull(cookie);
        assertFalse(cookie.isEmpty());

        return getSessionIdFromCookie(cookie);
    }

    protected String getSessionIdFromCookie(String cookie) {
        Pattern pattern = Pattern.compile("(?<cookieName>" + appConfig.getJavaSessionIdName() + ")=(?<cookieValue>[a-zA-Z0-9-]+)");
        Matcher matcher = pattern.matcher(cookie);
        assertTrue(matcher.find());
        assertEquals(appConfig.getJavaSessionIdName(), matcher.group("cookieName"));
        String sessionId = matcher.group("cookieValue");
        //TIP JAVASESSIONID=d926097b-9678-4b5f-9081-955ba424b1a1; Max-Age=120; Expires=Thu, 18 Jul 2024 04:00:30 GMT
        assertTrue(sessionId.matches("[a-zA-Z0-9-]{36}"));

        return sessionId;
    }

    protected void addNewBusToDb(DtoBusRequest dtoBusRequest, String adminSessionId) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.COOKIE, createHeaderForSessionId(adminSessionId));
        ResponseEntity<DtoBusResponse> responseEntity = testRestTemplate
                .exchange(
                        BASE_URL + PORT + API_BUSES_URL,
                        HttpMethod.POST,
                        new HttpEntity<>(dtoBusRequest, httpHeaders),
                        DtoBusResponse.class
                );
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        DtoBusResponse dtoResponse = responseEntity.getBody();
        assertNotNull(dtoResponse);
        assertEquals(dtoBusRequest.getBusName(), dtoResponse.getBusName());
    }

    protected void addNewNewStationToDb(String stationName, String adminSessionId) {
        DtoStationRequest dtoRequest = new DtoStationRequest(stationName);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.COOKIE, createHeaderForSessionId(adminSessionId));
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

    protected Long addNewTripAndGetItId(DtoTripRequest dto, String adminSessionId) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.COOKIE, createHeaderForSessionId(adminSessionId));
        ResponseEntity<DtoOrderResponse> responseEntity = testRestTemplate.exchange(
                BASE_URL + PORT + API_TRIPS_URL,
                HttpMethod.POST,
                new HttpEntity<>(dto, httpHeaders),
                DtoOrderResponse.class
        );

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        DtoOrderResponse dtoTripResponse = responseEntity.getBody();
        assertNotNull(dtoTripResponse);

        return dtoTripResponse.getTripId();
    }

    protected long addNewOrderToTripAndGetItId(DtoOrderRequest dtoOrderRequest, String clientSessionId) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.COOKIE, createHeaderForSessionId(clientSessionId));
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

    protected void approveTripById(long tripId, String adminSessionId) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.COOKIE, createHeaderForSessionId(adminSessionId));
        ResponseEntity<String> responseEntity = testRestTemplate.exchange(
                BASE_URL + PORT + API_TRIPS_URL + "/" + tripId + "/approve",
                HttpMethod.PUT,
                new HttpEntity<>(null, httpHeaders),
                String.class
        );

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        String response = responseEntity.getBody();
        assertNotNull(response);
        assertEquals("{}", response);
    }

    protected long addNewStationAndGetItsId(String name, String adminSessionId) {
        DtoStationRequest dtoRequest = new DtoStationRequest(name);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.COOKIE, createHeaderForSessionId(adminSessionId));
        ResponseEntity<DtoStationResponse> responseEntity = testRestTemplate.exchange(
                BASE_URL + PORT + API_STATIONS_URL,
                HttpMethod.POST,
                new HttpEntity<>(dtoRequest, httpHeaders),
                DtoStationResponse.class
        );
        DtoStationResponse dtoResponse = responseEntity.getBody();
        assertNotNull(dtoResponse);
        assertNotEquals(0, dtoResponse.getId());

        return dtoResponse.getId();
    }
}
