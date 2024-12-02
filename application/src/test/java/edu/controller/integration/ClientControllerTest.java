package edu.controller.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.configuration.ApplicationConfig;
import edu.util.ClientTestData;
import edu.dto.client.DtoRegisterNewClientRequest;
import edu.dto.client.DtoRegisterNewClientResponse;
import edu.dto.client.DtoUpdateClientRequest;
import edu.dto.client.DtoUpdateClientResponse;
import edu.dto.error.DtoErrorResponse;
import edu.dto.error.DtoErrorResponseElement;
import edu.error.ApplicationErrorDetails;
import edu.error.ErrorResponseElement;
import edu.service.ClientService;
import edu.util.mapper.ErrorElementMapper;
import jakarta.servlet.http.Cookie;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class ClientControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private ApplicationConfig applicationConfig;
    @MockBean
    private ClientService clientService;
    @Autowired
    private ErrorElementMapper errorElementMapper;
    private final String string51CharactersLong = "ццццццццццццццццццццццццццццццццццццццццццццццццццц";
    private final int OK = 200;
    private final int BAD_REQUEST = 400;

    @Test
    void registerNewClientPositive() throws Exception {
        var dtoRequest = ClientTestData.createValidRegisterNewClientDtoRequest();
        var dtoResponse = ClientTestData.createValidRegisterNewClientDtoResponse();
        String sessionId = UUID.randomUUID().toString();
        Pair<DtoRegisterNewClientResponse, String> pairResponseFromService = new ImmutablePair<>(dtoResponse, sessionId);
        Mockito
                .when(
                        clientService.registerNewClient(Mockito.eq(dtoRequest))
                )
                .thenReturn(pairResponseFromService);

        MvcResult mvcResult = mockMvc
                .perform(
                    post("/api/clients")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(dtoRequest))
                )
                .andReturn();

        assertNotNull(mvcResult);
        assertEquals(OK, mvcResult.getResponse().getStatus());
        Mockito.verify(clientService, Mockito.times(1)).registerNewClient(Mockito.eq(dtoRequest));
        Cookie cookieFromServer = mvcResult.getResponse().getCookie(applicationConfig.getJavaSessionIdName());
        assertNotNull(cookieFromServer);
        assertEquals(sessionId, cookieFromServer.getValue());
    }

    @ParameterizedTest
    @MethodSource("generateIncorrectDtoRegisterNewClientRequests")
    void registerNewClientNegative(DtoRegisterNewClientRequest dtoRequest, DtoErrorResponse dtoErrorResponse) throws Exception {
        MvcResult mvcResult = mockMvc
                .perform(
                        post("/api/clients")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dtoRequest))
                )
                .andReturn();

        assertNotNull(mvcResult);
        assertEquals(BAD_REQUEST, mvcResult.getResponse().getStatus());
        DtoErrorResponse errorResponseFromServer = mapper.readValue(mvcResult.getResponse().getContentAsString(), DtoErrorResponse.class);
        assertEquals(dtoErrorResponse.getErrors().size(), errorResponseFromServer.getErrors().size());
        List<DtoErrorResponseElement> standardErrors = dtoErrorResponse.getErrors();
        standardErrors.sort(Comparator.comparing(DtoErrorResponseElement::getErrorCode));
        List<DtoErrorResponseElement> errorsFromServer = errorResponseFromServer.getErrors();
        errorsFromServer.sort(Comparator.comparing(DtoErrorResponseElement::getErrorCode));
        assertEquals(standardErrors, errorsFromServer);
    }

    @Test
    void updateClientAccountPositive() throws Exception {
        var dtoRequest = ClientTestData.createValidUpdateClientDtoRequest();
        var dtoResponse =  ClientTestData.createValidUpdateClientDtoResponse();
        String sessionId = "active_session_id";

        Mockito
                .when(
                        clientService.updateClientInfo(Mockito.eq(sessionId), Mockito.eq(dtoRequest))
                )
                .thenReturn(dtoResponse);

        String s = mapper.writeValueAsString(dtoRequest);
        MvcResult mvcResult = mockMvc
                .perform(
                        put("/api/clients")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(mapper.writeValueAsString(dtoRequest))
                                .accept(MediaType.APPLICATION_JSON)
                                .cookie(new Cookie(applicationConfig.getJavaSessionIdName(), sessionId))
                                .characterEncoding("UTF-8")
                )
                .andReturn();

        assertEquals(OK, mvcResult.getResponse().getStatus());
        Mockito
                .verify(clientService, Mockito.times(1))
                .updateClientInfo(Mockito.eq(sessionId), Mockito.eq(dtoRequest));

        var dtoResponseFromServer = mapper.readValue(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8), DtoUpdateClientResponse.class);
        assertEquals(ClientTestData.DEFAULT_VALID_FIRST_NAME, dtoResponseFromServer.getFirstName());
        assertEquals(ClientTestData.DEFAULT_VALID_LAST_NAME, dtoResponseFromServer.getLastName());
        assertEquals(ClientTestData.DEFAULT_VALID_PATRONYMIC, dtoResponseFromServer.getPatronymic());
        assertEquals(ClientTestData.DEFAULT_VALID_EMAIL, dtoResponseFromServer.getEmail());
        assertEquals(ClientTestData.DEFAULT_VALID_PHONE, dtoResponseFromServer.getPhone());
        assertEquals(ClientTestData.USER_TYPE, dtoResponseFromServer.getUserType());
    }

    @ParameterizedTest
    @MethodSource("generateIncorrectDtoUpdateClientRequests")
    void updateClientAccountNegative(DtoUpdateClientRequest dtoRequest, DtoErrorResponse dtoErrorResponse) throws Exception {
        MvcResult mvcResult = mockMvc
                .perform(
                    put("/api/clients")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dtoRequest))
                        .cookie(new Cookie(applicationConfig.getJavaSessionIdName(), UUID.randomUUID().toString()))
                )
                .andReturn();

        assertNotNull(mvcResult);
        assertEquals(BAD_REQUEST, mvcResult.getResponse().getStatus());
        DtoErrorResponse errorResponseFromServer = mapper.readValue(mvcResult.getResponse().getContentAsString(), DtoErrorResponse.class);
        assertEquals(dtoErrorResponse.getErrors().size(), errorResponseFromServer.getErrors().size());
        List<DtoErrorResponseElement> standardErrors = dtoErrorResponse.getErrors();
        standardErrors.sort(Comparator.comparing(DtoErrorResponseElement::getErrorCode));
        List<DtoErrorResponseElement> errorsFromServer = errorResponseFromServer.getErrors();
        errorsFromServer.sort(Comparator.comparing(DtoErrorResponseElement::getErrorCode));
        assertEquals(standardErrors, errorsFromServer);
    }

    private Stream<Arguments> generateIncorrectDtoRegisterNewClientRequests() {
        var nullFirstName = ClientTestData.createValidRegisterNewClientDtoRequest();
        nullFirstName.setFirstName(null);
        var dtoErrorResponseNullFirstName = new DtoErrorResponse();
        dtoErrorResponseNullFirstName.addError(
                errorElementMapper.toDto(new ErrorResponseElement(ApplicationErrorDetails.VIOLATION_FIRST_NAME_EMPTY_CONSTRAINT))
        );

        var tooLongFirstName = ClientTestData.createValidRegisterNewClientDtoRequest();
        tooLongFirstName.setFirstName(string51CharactersLong);
        var dtoErrorResponseTooLongFirstName = new DtoErrorResponse();
        dtoErrorResponseTooLongFirstName.addError(
                errorElementMapper.toDto(new ErrorResponseElement(ApplicationErrorDetails.VIOLATION_FIRST_NAME_MAX_LENGTH_CONSTRAINT))
        );

        var patternErrorFirstName = ClientTestData.createValidRegisterNewClientDtoRequest();
        patternErrorFirstName.setFirstName("Ivan42");
        var dtoErrorResponsePatternErrorFirstName = new DtoErrorResponse();
        dtoErrorResponsePatternErrorFirstName.addError(
                errorElementMapper.toDto(new ErrorResponseElement(ApplicationErrorDetails.VIOLATION_FIRST_NAME_PATTERN_CONSTRAINT))
        );

        var nullLastName = ClientTestData.createValidRegisterNewClientDtoRequest();
        nullLastName.setLastName(null);
        var dtoErrorResponseNullLastName = new DtoErrorResponse();
        dtoErrorResponseNullLastName.addError(
                errorElementMapper.toDto(new ErrorResponseElement(ApplicationErrorDetails.VIOLATION_LAST_NAME_EMPTY_CONSTRAINT))
        );

        var tooLongLastName = ClientTestData.createValidRegisterNewClientDtoRequest();
        tooLongLastName.setLastName(string51CharactersLong);
        var dtoErrorResponseTooLongLastName = new DtoErrorResponse();
        dtoErrorResponseTooLongLastName.addError(
                errorElementMapper.toDto(new ErrorResponseElement(ApplicationErrorDetails.VIOLATION_LAST_NAME_MAX_LENGTH_CONSTRAINT))
        );

        var patternErrorLastName = ClientTestData.createValidRegisterNewClientDtoRequest();
        patternErrorLastName.setLastName("Ivanov42");
        var dtoErrorResponsePatternErrorLastName = new DtoErrorResponse();
        dtoErrorResponsePatternErrorLastName.addError(
                errorElementMapper.toDto(new ErrorResponseElement(ApplicationErrorDetails.VIOLATION_LAST_NAME_PATTERN_CONSTRAINT))
        );

        var tooLongPatronymic =  ClientTestData.createValidRegisterNewClientDtoRequest();
        tooLongPatronymic.setPatronymic(string51CharactersLong);
        var dtoErrorResponseTooLongPatronymic = new DtoErrorResponse();
        dtoErrorResponseTooLongPatronymic.addError(
                errorElementMapper.toDto(new ErrorResponseElement(ApplicationErrorDetails.VIOLATION_PATRONYMIC_MAX_LENGTH_CONSTRAINT))
        );

        var patternErrorPatronymic = ClientTestData.createValidRegisterNewClientDtoRequest();
        patternErrorPatronymic.setPatronymic("Ivanovich42");
        var dtoErrorResponsePatternErrorPatronymic = new DtoErrorResponse();
        dtoErrorResponsePatternErrorPatronymic.addError(
                errorElementMapper.toDto(new ErrorResponseElement(ApplicationErrorDetails.VIOLATION_PATRONYMIC_PATTERN_CONSTRAINT))
        );

        var nullLogin = ClientTestData.createValidRegisterNewClientDtoRequest();
        nullLogin.setLogin(null);
        var dtoErrorResponseNullLogin = new DtoErrorResponse();
        dtoErrorResponseNullLogin.addError(
                errorElementMapper.toDto(new ErrorResponseElement(ApplicationErrorDetails.VIOLATION_LOGIN_EMPTY_CONSTRAINT))
        );

        var tooLongLogin = ClientTestData.createValidRegisterNewClientDtoRequest();
        tooLongLogin.setLogin(string51CharactersLong);
        var dtoErrorResponseTooLongLogin = new DtoErrorResponse();
        dtoErrorResponseTooLongLogin.addError(
                errorElementMapper.toDto(new ErrorResponseElement(ApplicationErrorDetails.VIOLATION_LOGIN_MAX_LENGTH_CONSTRAINT))
        );

        var patternErrorLogin = ClientTestData.createValidRegisterNewClientDtoRequest();
        patternErrorLogin.setLogin("___%$&^");
        var dtoErrorResponsePatternErrorLogin = new DtoErrorResponse();
        dtoErrorResponsePatternErrorLogin.addError(
                errorElementMapper.toDto(new ErrorResponseElement(ApplicationErrorDetails.VIOLATION_LOGIN_PATTERN_CONSTRAINT))
        );

        var nullPassword = ClientTestData.createValidRegisterNewClientDtoRequest();
        nullPassword.setPassword(null);
        var dtoErrorResponseNullPassword = new DtoErrorResponse();
        dtoErrorResponseNullPassword.addError(
                errorElementMapper.toDto(new ErrorResponseElement(ApplicationErrorDetails.VIOLATION_PASSWORD_EMPTY_CONSTRAINT))
        );

        var tooShortPassword = ClientTestData.createValidRegisterNewClientDtoRequest();
        tooShortPassword.setPassword("x");
        var dtoErrorResponseTooShortPassword = new DtoErrorResponse();
        dtoErrorResponseTooShortPassword.addError(
                errorElementMapper.toDto(new ErrorResponseElement(ApplicationErrorDetails.VIOLATION_PASSWORD_MIN_LENGTH_CONSTRAINT))
        );

        return Stream.of(
                Arguments.of(nullFirstName, dtoErrorResponseNullFirstName),
                Arguments.of(tooLongFirstName, dtoErrorResponseTooLongFirstName),
                Arguments.of(patternErrorFirstName, dtoErrorResponsePatternErrorFirstName),
                Arguments.of(nullLastName, dtoErrorResponseNullLastName),
                Arguments.of(tooLongLastName, dtoErrorResponseTooLongLastName),
                Arguments.of(patternErrorLastName, dtoErrorResponsePatternErrorLastName),
                Arguments.of(tooLongPatronymic, dtoErrorResponseTooLongPatronymic),
                Arguments.of(patternErrorPatronymic, dtoErrorResponsePatternErrorPatronymic),
                Arguments.of(nullLogin, dtoErrorResponseNullLogin),
                Arguments.of(tooLongLogin, dtoErrorResponseTooLongLogin),
                Arguments.of(patternErrorLogin, dtoErrorResponsePatternErrorLogin),
                Arguments.of(nullPassword, dtoErrorResponseNullPassword),
                Arguments.of(tooShortPassword, dtoErrorResponseTooShortPassword)
        );
    }

    private Stream<Arguments> generateIncorrectDtoUpdateClientRequests() {
        var nullFirstName = ClientTestData.createValidUpdateClientDtoRequest();
        nullFirstName.setFirstName(null);
        var dtoErrorResponseNullFirstName = new DtoErrorResponse();
        dtoErrorResponseNullFirstName.addError(
                errorElementMapper.toDto(new ErrorResponseElement(ApplicationErrorDetails.VIOLATION_FIRST_NAME_EMPTY_CONSTRAINT))
        );

        var tooLongFirstName = ClientTestData.createValidUpdateClientDtoRequest();
        tooLongFirstName.setFirstName(string51CharactersLong);
        var dtoErrorResponseTooLongFirstName = new DtoErrorResponse();
        dtoErrorResponseTooLongFirstName.addError(
                errorElementMapper.toDto(new ErrorResponseElement(ApplicationErrorDetails.VIOLATION_FIRST_NAME_MAX_LENGTH_CONSTRAINT))
        );

        var patternErrorFirstName = ClientTestData.createValidUpdateClientDtoRequest();
        patternErrorFirstName.setFirstName("Ivan42");
        var dtoErrorResponsePatternErrorFirstName = new DtoErrorResponse();
        dtoErrorResponsePatternErrorFirstName.addError(
                errorElementMapper.toDto(new ErrorResponseElement(ApplicationErrorDetails.VIOLATION_FIRST_NAME_PATTERN_CONSTRAINT))
        );

        var nullLastName = ClientTestData.createValidUpdateClientDtoRequest();
        nullLastName.setLastName(null);
        var dtoErrorResponseNullLastName = new DtoErrorResponse();
        dtoErrorResponseNullLastName.addError(
                errorElementMapper.toDto(new ErrorResponseElement(ApplicationErrorDetails.VIOLATION_LAST_NAME_EMPTY_CONSTRAINT))
        );

        var tooLongLastName = ClientTestData.createValidUpdateClientDtoRequest();
        tooLongLastName.setLastName(string51CharactersLong);
        var dtoErrorResponseTooLongLastName = new DtoErrorResponse();
        dtoErrorResponseTooLongLastName.addError(
                errorElementMapper.toDto(new ErrorResponseElement(ApplicationErrorDetails.VIOLATION_LAST_NAME_MAX_LENGTH_CONSTRAINT))
        );

        var patternErrorLastName = ClientTestData.createValidUpdateClientDtoRequest();
        patternErrorLastName.setLastName("Ivanov42");
        var dtoErrorResponsePatternErrorLastName = new DtoErrorResponse();
        dtoErrorResponsePatternErrorLastName.addError(
                errorElementMapper.toDto(new ErrorResponseElement(ApplicationErrorDetails.VIOLATION_LAST_NAME_PATTERN_CONSTRAINT))
        );

        var tooLongPatronymic = ClientTestData.createValidUpdateClientDtoRequest();
        tooLongPatronymic.setPatronymic(string51CharactersLong);
        var dtoErrorResponseTooLongPatronymic = new DtoErrorResponse();
        dtoErrorResponseTooLongPatronymic.addError(
                errorElementMapper.toDto(new ErrorResponseElement(ApplicationErrorDetails.VIOLATION_PATRONYMIC_MAX_LENGTH_CONSTRAINT))
        );

        var patternErrorPatronymic = ClientTestData.createValidUpdateClientDtoRequest();
        patternErrorPatronymic.setPatronymic("Ivanovich42");
        var dtoErrorResponsePatternErrorPatronymic = new DtoErrorResponse();
        dtoErrorResponsePatternErrorPatronymic.addError(
                errorElementMapper.toDto(new ErrorResponseElement(ApplicationErrorDetails.VIOLATION_PATRONYMIC_PATTERN_CONSTRAINT))
        );

        var nullOldPassword = ClientTestData.createValidUpdateClientDtoRequest();
        nullOldPassword.setOldPassword(null);
        var dtoErrorResponseNullOldPassword = new DtoErrorResponse();
        dtoErrorResponseNullOldPassword.addError(
                errorElementMapper.toDto(new ErrorResponseElement(ApplicationErrorDetails.VIOLATION_PASSWORD_EMPTY_CONSTRAINT))
        );

        var tooShortOldPassword = ClientTestData.createValidUpdateClientDtoRequest();
        tooShortOldPassword.setOldPassword("y");
        var dtoErrorResponseTooShortOldPassword = new DtoErrorResponse();
        dtoErrorResponseTooShortOldPassword.addError(
                errorElementMapper.toDto(new ErrorResponseElement(ApplicationErrorDetails.VIOLATION_PASSWORD_MIN_LENGTH_CONSTRAINT))
        );

        var nullNewPassword = ClientTestData.createValidUpdateClientDtoRequest();
        nullNewPassword.setNewPassword(null);
        var dtoErrorResponseNullNewPassword = new DtoErrorResponse();
        dtoErrorResponseNullNewPassword.addError(
                errorElementMapper.toDto(new ErrorResponseElement(ApplicationErrorDetails.VIOLATION_PASSWORD_EMPTY_CONSTRAINT))
        );

        var tooShortNewPassword = ClientTestData.createValidUpdateClientDtoRequest();
        tooShortNewPassword.setNewPassword("x");
        var dtoErrorResponseTooShortNewPassword = new DtoErrorResponse();
        dtoErrorResponseTooShortNewPassword.addError(
                errorElementMapper.toDto(new ErrorResponseElement(ApplicationErrorDetails.VIOLATION_PASSWORD_MIN_LENGTH_CONSTRAINT))
        );

        var nullEmail = ClientTestData.createValidUpdateClientDtoRequest();
        nullEmail.setEmail(null);
        var dtoErrorResponseNullEmail = new DtoErrorResponse();
        dtoErrorResponseNullEmail.addError(
                errorElementMapper.toDto(new ErrorResponseElement(ApplicationErrorDetails.VIOLATION_EMAIL_EMPTY_CONSTRAINT))
        );

        var patternErrorEmail = ClientTestData.createValidUpdateClientDtoRequest();
        patternErrorEmail.setEmail("not_email");
        var dtoErrorResponsePatternErrorEmail = new DtoErrorResponse();
        dtoErrorResponsePatternErrorEmail.addError(
                errorElementMapper.toDto(new ErrorResponseElement(ApplicationErrorDetails.VIOLATION_EMAIL_PATTERN_CONSTRAINT))
        );

        var nullPhone = ClientTestData.createValidUpdateClientDtoRequest();
        nullPhone.setPhone(null);
        var dtoErrorResponseNullPhone = new DtoErrorResponse();
        dtoErrorResponseNullPhone.addError(
                errorElementMapper.toDto(new ErrorResponseElement(ApplicationErrorDetails.VIOLATION_PHONE_EMPTY_CONSTRAINT))
        );

        var patternErrorPhone = ClientTestData.createValidUpdateClientDtoRequest();
        patternErrorPhone.setPhone("not_phone");
        var dtoErrorResponsePatternErrorPhone = new DtoErrorResponse();
        dtoErrorResponsePatternErrorPhone.addError(
                errorElementMapper.toDto(new ErrorResponseElement(ApplicationErrorDetails.VIOLATION_PHONE_PATTERN_CONSTRAINT))
        );


        return Stream.of(
                Arguments.of(nullFirstName, dtoErrorResponseNullFirstName),
                Arguments.of(tooLongFirstName, dtoErrorResponseTooLongFirstName),
                Arguments.of(patternErrorFirstName, dtoErrorResponsePatternErrorFirstName),
                Arguments.of(nullLastName, dtoErrorResponseNullLastName),
                Arguments.of(tooLongLastName, dtoErrorResponseTooLongLastName),
                Arguments.of(patternErrorLastName, dtoErrorResponsePatternErrorLastName),
                Arguments.of(tooLongPatronymic, dtoErrorResponseTooLongPatronymic),
                Arguments.of(patternErrorPatronymic, dtoErrorResponsePatternErrorPatronymic),
                Arguments.of(nullOldPassword, dtoErrorResponseNullOldPassword),
                Arguments.of(tooShortOldPassword, dtoErrorResponseTooShortOldPassword),
                Arguments.of(nullNewPassword, dtoErrorResponseNullNewPassword),
                Arguments.of(tooShortNewPassword, dtoErrorResponseTooShortNewPassword),
                Arguments.of(nullPhone, dtoErrorResponseNullPhone),
                Arguments.of(patternErrorPhone, dtoErrorResponsePatternErrorPhone),
                Arguments.of(nullEmail, dtoErrorResponseNullEmail),
                Arguments.of(patternErrorEmail, dtoErrorResponsePatternErrorEmail)
        );
    }
}