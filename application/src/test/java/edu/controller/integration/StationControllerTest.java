package edu.controller.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.configuration.ApplicationConfig;
import edu.dto.error.DtoErrorResponse;
import edu.dto.error.DtoErrorResponseElement;
import edu.dto.trip.station.DtoStationRequest;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class StationControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ApplicationConfig appConfig;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private ApplicationConfig applicationConfig;

    @ParameterizedTest
    @MethodSource("generateIncorrectDtoRegisterNewStationRequests")
    void createNewStation_EMPTY_NAME_NEGATIVE(DtoStationRequest dtoRequest, DtoErrorResponse dtoErrorResponse) throws Exception {
        MvcResult mvcResult = mvc
                .perform(
                        post("/api/stations")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dtoRequest))
                                .cookie(new Cookie(applicationConfig.getJavaSessionIdName(), UUID.randomUUID().toString()))
                )
                .andReturn();

        assertEquals(HttpStatus.BAD_REQUEST, HttpStatus.valueOf(mvcResult.getResponse().getStatus()));
        DtoErrorResponse responseFromServer = mapper.readValue(mvcResult.getResponse().getContentAsString(), DtoErrorResponse.class);
        List<DtoErrorResponseElement> errorResponseElements = responseFromServer.getErrors();
        assertEquals(1, errorResponseElements.size());
        assertEquals("VIOLATION_STATION_EMPTY_CONSTRAINT", errorResponseElements.get(0).getErrorCode());
        assertEquals("station", errorResponseElements.get(0).getField());
        assertEquals("Please, provide a station name.", errorResponseElements.get(0).getMessage());
    }

    private Stream<Arguments> generateIncorrectDtoRegisterNewStationRequests() {
        DtoStationRequest nullStationName = new DtoStationRequest(null);
        DtoStationRequest emptyStationName = new DtoStationRequest("");
        DtoStationRequest spacesInStationName = new DtoStationRequest("    ");

        DtoErrorResponse dtoResponse = new DtoErrorResponse();
        DtoErrorResponseElement errorElement = new DtoErrorResponseElement(
                "VIOLATION_STATION_EMPTY_CONSTRAINT",
                "station",
                "Please, provide a station name."
        );
        dtoResponse.addError(errorElement);

        return Stream.of(
                Arguments.of(nullStationName, dtoResponse),
                Arguments.of(emptyStationName, dtoResponse),
                Arguments.of(spacesInStationName, dtoResponse)
        );
    }
}
