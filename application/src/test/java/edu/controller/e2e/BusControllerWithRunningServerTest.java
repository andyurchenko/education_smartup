package edu.controller.e2e;

import edu.configuration.ApplicationConfig;
import edu.dto.trip.bus.DtoBusRequest;
import edu.dto.trip.bus.DtoBusResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class BusControllerWithRunningServerTest extends BaseTest {
    private final String BUS_NAME_ONE = "bus_brand_name_ONE";
    private final int PLACE_COUNT_OF_BUS_NAME_ONE = 3;
    private String ADMIN_SESSION_ID;
    @Autowired
    protected BusControllerWithRunningServerTest(@LocalServerPort Integer port, TestRestTemplate testRestTemplate, ApplicationConfig appConfig) {
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
        addNewBusToDb(new DtoBusRequest(BUS_NAME_ONE, PLACE_COUNT_OF_BUS_NAME_ONE), ADMIN_SESSION_ID);
    }
    @Test
    void getAllBuses() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.COOKIE, createHeaderForSessionId(ADMIN_SESSION_ID));
        ResponseEntity<List<DtoBusResponse>> responseEntity = testRestTemplate
                .exchange(
                        BASE_URL + PORT + API_BUSES_URL,
                        HttpMethod.GET,
                        new HttpEntity<>(httpHeaders),
                        new ParameterizedTypeReference<>() {}
                );
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<DtoBusResponse> dtoResponse = responseEntity.getBody();
        assertNotNull(dtoResponse);
        assertEquals(1, dtoResponse.size());
        assertEquals(BUS_NAME_ONE, dtoResponse.get(0).getBusName());
    }
}