package edu.controller.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.configuration.ApplicationConfig;
import edu.util.AdminTestData;
import edu.dto.administrator.DtoRegisterNewAdministratorRequest;
import edu.dto.administrator.DtoRegisterNewAdministratorResponse;
import edu.dto.administrator.DtoUpdateAdministratorRequest;
import edu.dto.administrator.DtoUpdateAdministratorResponse;
import edu.dto.error.DtoErrorResponse;
import edu.dto.error.DtoErrorResponseElement;
import edu.error.ApplicationErrorDetails;
import edu.error.ErrorResponseElement;
import edu.service.AdminService;
import edu.util.mapper.ErrorElementMapper;
import jakarta.servlet.http.Cookie;
import org.apache.commons.lang3.tuple.MutablePair;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class AdminControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private ApplicationConfig applicationConfig;
    @MockBean
    private AdminService adminService;
    @Autowired
    private ErrorElementMapper errorElementMapper;
    private final String string51CharactersLong = "ццццццццццццццццццццццццццццццццццццццццццццццццццц";
    private final int OK = 200;
    private final int BAD_REQUEST = 400;

    @Test
    void registerNewAdministratorWithCorrectDtoPositive() throws Exception {
        var dtoRequest = AdminTestData.createValidRegisterNewAdministratorDtoRequest();
        var dtoResponse =  AdminTestData.createValidRegisterAdministratorDtoResponse();
        String sessionId = "active_session_id";
        Pair<DtoRegisterNewAdministratorResponse, String> response = new MutablePair<>(dtoResponse, sessionId);

        Mockito
                .when(adminService.registerNewAdministratorAndStartNewAdminSession(Mockito.eq(dtoRequest)))
                .thenReturn(response);

        MvcResult mvcResult = mockMvc
                .perform(
                        post("/api/admins")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(mapper.writeValueAsString(dtoRequest))
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andReturn();

        assertEquals(OK, mvcResult.getResponse().getStatus());
        Mockito
                .verify(adminService, Mockito.times(1))
                .registerNewAdministratorAndStartNewAdminSession(Mockito.eq(dtoRequest));

        Cookie cookieFromServer = mvcResult.getResponse().getCookie(applicationConfig.getJavaSessionIdName());
        assertNotNull(cookieFromServer);
        assertEquals(sessionId, cookieFromServer.getValue());
    }

    @ParameterizedTest
    @MethodSource("generateIncorrectDtoRegisterNewAdminRequests")
    void registerNewAdministratorWithIncorrectDtoNegative(DtoRegisterNewAdministratorRequest dtoRequest, DtoErrorResponse dtoErrorResponse) throws Exception {
        MvcResult mvcResult = mockMvc
                .perform(
                    post("/api/admins")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dtoRequest))
                ).andReturn();

        assertEquals(BAD_REQUEST, mvcResult.getResponse().getStatus());
        DtoErrorResponse responseFromServer = mapper.readValue(mvcResult.getResponse().getContentAsString(), DtoErrorResponse.class);
        assertEquals(dtoErrorResponse.getErrors().size(), responseFromServer.getErrors().size());
        List<DtoErrorResponseElement> standardErrors = dtoErrorResponse.getErrors();
        standardErrors.sort(Comparator.comparing(DtoErrorResponseElement::getErrorCode));
        List<DtoErrorResponseElement> errorsFromServer = responseFromServer.getErrors();
        errorsFromServer.sort(Comparator.comparing(DtoErrorResponseElement::getErrorCode));
        assertEquals(standardErrors, errorsFromServer);
    }

    @Test
    void updateNewAdministratorWithCorrectDtoPositive() throws Exception {
        var dtoRequest = AdminTestData.createValidUpdateAdministratorDtoRequest();
        var dtoResponse =  AdminTestData.createValidUpdateAdministratorDtoResponse();
        String sessionId = "active_session_id";

        Mockito
                .when(
                        adminService.updateAdministratorInfo(Mockito.eq(sessionId), Mockito.eq(dtoRequest))
                )
                .thenReturn(dtoResponse);

        MvcResult mvcResult = mockMvc
                .perform(
                        put("/api/admins")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(mapper.writeValueAsString(dtoRequest))
                                .accept(MediaType.APPLICATION_JSON)
                                .cookie(new Cookie(applicationConfig.getJavaSessionIdName(), sessionId))
                                .characterEncoding("UTF-8")
                )
                .andReturn();

        assertEquals(OK, mvcResult.getResponse().getStatus());
        Mockito
                .verify(adminService, Mockito.times(1))
                .updateAdministratorInfo(Mockito.eq(sessionId), Mockito.eq(dtoRequest));

        var dtoResponseFromServer = mapper.readValue(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8), DtoUpdateAdministratorResponse.class);
        assertEquals(AdminTestData.DEFAULT_VALID_FIRST_NAME, dtoResponseFromServer.getFirstName());
        assertEquals(AdminTestData.DEFAULT_VALID_LAST_NAME, dtoResponseFromServer.getLastName());
        assertEquals(AdminTestData.DEFAULT_VALID_PATRONYMIC, dtoResponseFromServer.getPatronymic());
        assertEquals(AdminTestData.DEFAULT_VALID_POSITION, dtoResponseFromServer.getPosition());
        assertEquals(AdminTestData.USER_TYPE, dtoResponseFromServer.getUserType());
    }

    @ParameterizedTest
    @MethodSource("generateIncorrectDtoUpdateAdminRequests")
    void updateAdministratorWithIncorrectDtoNegative(DtoUpdateAdministratorRequest dtoInvalidRequest, DtoErrorResponse dtoErrorResponse) throws Exception {
        MvcResult mvcResult = mockMvc
                .perform(
                    put("/api/admins")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dtoInvalidRequest))
                        .cookie(new Cookie(applicationConfig.getJavaSessionIdName(), UUID.randomUUID().toString()))
                )
                .andReturn();

        assertEquals(BAD_REQUEST, mvcResult.getResponse().getStatus());
        DtoErrorResponse responseFromServer = mapper.readValue(mvcResult.getResponse().getContentAsString(), DtoErrorResponse.class);
        assertEquals(dtoErrorResponse.getErrors().size(), responseFromServer.getErrors().size());
        List<DtoErrorResponseElement> standardErrors = dtoErrorResponse.getErrors();
        standardErrors.sort(Comparator.comparing(DtoErrorResponseElement::getErrorCode));
        List<DtoErrorResponseElement> errorsFromServer = responseFromServer.getErrors();
        errorsFromServer.sort(Comparator.comparing(DtoErrorResponseElement::getErrorCode));
        assertEquals(standardErrors, errorsFromServer);
    }

    private Stream<Arguments> generateIncorrectDtoRegisterNewAdminRequests() {
        var nullFirstName =  AdminTestData.createValidRegisterNewAdministratorDtoRequest();
        nullFirstName.setFirstName(null);
        var dtoErrorResponseNullFirstName = new DtoErrorResponse();
        dtoErrorResponseNullFirstName.addError(
                errorElementMapper.toDto(new ErrorResponseElement(ApplicationErrorDetails.VIOLATION_FIRST_NAME_EMPTY_CONSTRAINT))
        );

        var tooLongFirstName =  AdminTestData.createValidRegisterNewAdministratorDtoRequest();
        tooLongFirstName.setFirstName(string51CharactersLong);
        var dtoErrorResponseTooLongFirstName = new DtoErrorResponse();
        var dtoErrorElement = errorElementMapper.toDto(new ErrorResponseElement(ApplicationErrorDetails.VIOLATION_FIRST_NAME_MAX_LENGTH_CONSTRAINT));
        dtoErrorResponseTooLongFirstName.addError(dtoErrorElement);

        var patternErrorFirstName =  AdminTestData.createValidRegisterNewAdministratorDtoRequest();
        patternErrorFirstName.setFirstName("Ivan42");
        var dtoErrorResponsePatternErrorFirstName = new DtoErrorResponse();
        dtoErrorResponsePatternErrorFirstName.addError(
                errorElementMapper.toDto(new ErrorResponseElement(ApplicationErrorDetails.VIOLATION_FIRST_NAME_PATTERN_CONSTRAINT))
        );

        var nullLastName =  AdminTestData.createValidRegisterNewAdministratorDtoRequest();
        nullLastName.setLastName(null);
        var dtoErrorResponseNullLastName = new DtoErrorResponse();
        dtoErrorResponseNullLastName.addError(
                errorElementMapper.toDto(new ErrorResponseElement(ApplicationErrorDetails.VIOLATION_LAST_NAME_EMPTY_CONSTRAINT))
        );

        var tooLongLastName =  AdminTestData.createValidRegisterNewAdministratorDtoRequest();
        tooLongLastName.setLastName(string51CharactersLong);
        var dtoErrorResponseTooLongLastName = new DtoErrorResponse();
        dtoErrorResponseTooLongLastName.addError(
                errorElementMapper.toDto(new ErrorResponseElement(ApplicationErrorDetails.VIOLATION_LAST_NAME_MAX_LENGTH_CONSTRAINT))
        );

        var patternErrorLastName =  AdminTestData.createValidRegisterNewAdministratorDtoRequest();
        patternErrorLastName.setLastName("Ivanov42");
        var dtoErrorResponsePatternErrorLastName = new DtoErrorResponse();
        dtoErrorResponsePatternErrorLastName.addError(
                errorElementMapper.toDto(new ErrorResponseElement(ApplicationErrorDetails.VIOLATION_LAST_NAME_PATTERN_CONSTRAINT))
        );

        var tooLongPatronymic =  AdminTestData.createValidRegisterNewAdministratorDtoRequest();
        tooLongPatronymic.setPatronymic(string51CharactersLong);
        var dtoErrorResponseTooLongPatronymic = new DtoErrorResponse();
        dtoErrorResponseTooLongPatronymic.addError(
                errorElementMapper.toDto(new ErrorResponseElement(ApplicationErrorDetails.VIOLATION_PATRONYMIC_MAX_LENGTH_CONSTRAINT))
                );

        var patternErrorPatronymic =  AdminTestData.createValidRegisterNewAdministratorDtoRequest();
        patternErrorPatronymic.setPatronymic("Ivanovich42");
        var dtoErrorResponsePatternErrorPatronymic = new DtoErrorResponse();
        dtoErrorResponsePatternErrorPatronymic.addError(
                errorElementMapper.toDto(new ErrorResponseElement(ApplicationErrorDetails.VIOLATION_PATRONYMIC_PATTERN_CONSTRAINT))
        );

        var nullPosition =  AdminTestData.createValidRegisterNewAdministratorDtoRequest();
        nullPosition.setPosition(null);
        var dtoErrorResponseNullPosition = new DtoErrorResponse();
        dtoErrorResponseNullPosition.addError(
                errorElementMapper.toDto(new ErrorResponseElement(ApplicationErrorDetails.VIOLATION_POSITION_EMPTY_CONSTRAINT))
        );

        var tooLongPosition =  AdminTestData.createValidRegisterNewAdministratorDtoRequest();
        tooLongPosition.setPosition(string51CharactersLong);
        var dtoErrorResponseTooLongPosition = new DtoErrorResponse();
        dtoErrorResponseTooLongPosition.addError(
                errorElementMapper.toDto(new ErrorResponseElement(ApplicationErrorDetails.VIOLATION_POSITION_MAX_LENGTH_CONSTRAINT))
        );

        var nullLogin =  AdminTestData.createValidRegisterNewAdministratorDtoRequest();
        nullLogin.setLogin(null);
        var dtoErrorResponseNullLogin = new DtoErrorResponse();
        dtoErrorResponseNullLogin.addError(
                errorElementMapper.toDto(new ErrorResponseElement(ApplicationErrorDetails.VIOLATION_LOGIN_EMPTY_CONSTRAINT))
        );

        var tooLongLogin =  AdminTestData.createValidRegisterNewAdministratorDtoRequest();
        tooLongLogin.setLogin(string51CharactersLong);
        var dtoErrorResponseTooLongLogin = new DtoErrorResponse();
        dtoErrorResponseTooLongLogin.addError(
                errorElementMapper.toDto(new ErrorResponseElement(ApplicationErrorDetails.VIOLATION_LOGIN_MAX_LENGTH_CONSTRAINT))
        );

        var patternErrorLogin =  AdminTestData.createValidRegisterNewAdministratorDtoRequest();
        patternErrorLogin.setLogin("___%$&^");
        var dtoErrorResponsePatternErrorLogin = new DtoErrorResponse();
        dtoErrorResponsePatternErrorLogin.addError(
                errorElementMapper.toDto(new ErrorResponseElement(ApplicationErrorDetails.VIOLATION_LOGIN_PATTERN_CONSTRAINT))
        );

        var nullPassword =  AdminTestData.createValidRegisterNewAdministratorDtoRequest();
        nullPassword.setPassword(null);
        var dtoErrorResponseNullPassword = new DtoErrorResponse();
        dtoErrorResponseNullPassword.addError(
                errorElementMapper.toDto(new ErrorResponseElement(ApplicationErrorDetails.VIOLATION_PASSWORD_EMPTY_CONSTRAINT))
        );

        var tooShortPassword =  AdminTestData.createValidRegisterNewAdministratorDtoRequest();
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
                Arguments.of(nullPosition, dtoErrorResponseNullPosition),
                Arguments.of(tooLongPosition, dtoErrorResponseTooLongPosition),
                Arguments.of(nullLogin, dtoErrorResponseNullLogin),
                Arguments.of(tooLongLogin, dtoErrorResponseTooLongLogin),
                Arguments.of(patternErrorLogin, dtoErrorResponsePatternErrorLogin),
                Arguments.of(nullPassword, dtoErrorResponseNullPassword),
                Arguments.of(tooShortPassword, dtoErrorResponseTooShortPassword)
        );
    }

    private Stream<Arguments> generateIncorrectDtoUpdateAdminRequests() {
        var nullFirstName =  AdminTestData.createValidUpdateAdministratorDtoRequest();
        nullFirstName.setFirstName(null);
        var dtoErrorResponseNullFirstName = new DtoErrorResponse();
        dtoErrorResponseNullFirstName.addError(
                errorElementMapper.toDto(new ErrorResponseElement(ApplicationErrorDetails.VIOLATION_FIRST_NAME_EMPTY_CONSTRAINT))
        );

        var tooLongFirstName =  AdminTestData.createValidUpdateAdministratorDtoRequest();
        tooLongFirstName.setFirstName(string51CharactersLong);
        var dtoErrorResponseTooLongFirstName = new DtoErrorResponse();
        dtoErrorResponseTooLongFirstName.addError(
                errorElementMapper.toDto(new ErrorResponseElement(ApplicationErrorDetails.VIOLATION_FIRST_NAME_MAX_LENGTH_CONSTRAINT))
        );

        var patternErrorFirstName =  AdminTestData.createValidUpdateAdministratorDtoRequest();
        patternErrorFirstName.setFirstName("Ivan42");
        var dtoErrorResponsePatternErrorFirstName = new DtoErrorResponse();
        dtoErrorResponsePatternErrorFirstName.addError(
                errorElementMapper.toDto(new ErrorResponseElement(ApplicationErrorDetails.VIOLATION_FIRST_NAME_PATTERN_CONSTRAINT))
        );

        var nullLastName =  AdminTestData.createValidUpdateAdministratorDtoRequest();
        nullLastName.setLastName(null);
        var dtoErrorResponseNullLastName = new DtoErrorResponse();
        dtoErrorResponseNullLastName.addError(
                errorElementMapper.toDto(new ErrorResponseElement(ApplicationErrorDetails.VIOLATION_LAST_NAME_EMPTY_CONSTRAINT))
        );

        var tooLongLastName =  AdminTestData.createValidUpdateAdministratorDtoRequest();
        tooLongLastName.setLastName(string51CharactersLong);
        var dtoErrorResponseTooLongLastName = new DtoErrorResponse();
        dtoErrorResponseTooLongLastName.addError(
                errorElementMapper.toDto(new ErrorResponseElement(ApplicationErrorDetails.VIOLATION_LAST_NAME_MAX_LENGTH_CONSTRAINT))
        );

        var patternErrorLastName =  AdminTestData.createValidUpdateAdministratorDtoRequest();
        patternErrorLastName.setLastName("Ivanov42");
        var dtoErrorResponsePatternErrorLastName = new DtoErrorResponse();
        dtoErrorResponsePatternErrorLastName.addError(
                errorElementMapper.toDto(new ErrorResponseElement(ApplicationErrorDetails.VIOLATION_LAST_NAME_PATTERN_CONSTRAINT))
        );

        var tooLongPatronymic =  AdminTestData.createValidUpdateAdministratorDtoRequest();
        tooLongPatronymic.setPatronymic(string51CharactersLong);
        var dtoErrorResponseTooLongPatronymic = new DtoErrorResponse();
        dtoErrorResponseTooLongPatronymic.addError(
                errorElementMapper.toDto(new ErrorResponseElement(ApplicationErrorDetails.VIOLATION_PATRONYMIC_MAX_LENGTH_CONSTRAINT))
        );

        var patternErrorPatronymic =  AdminTestData.createValidUpdateAdministratorDtoRequest();
        patternErrorPatronymic.setPatronymic("Ivanovich42");
        var dtoErrorResponsePatternErrorPatronymic = new DtoErrorResponse();
        dtoErrorResponsePatternErrorPatronymic.addError(
                errorElementMapper.toDto(new ErrorResponseElement(ApplicationErrorDetails.VIOLATION_PATRONYMIC_PATTERN_CONSTRAINT))
        );

        var nullPosition =  AdminTestData.createValidUpdateAdministratorDtoRequest();
        nullPosition.setPosition(null);
        var dtoErrorResponseNullPosition = new DtoErrorResponse();
        dtoErrorResponseNullPosition.addError(
                errorElementMapper.toDto(new ErrorResponseElement(ApplicationErrorDetails.VIOLATION_POSITION_EMPTY_CONSTRAINT))
        );

        var tooLongPosition =  AdminTestData.createValidUpdateAdministratorDtoRequest();
        tooLongPosition.setPosition(string51CharactersLong);
        var dtoErrorResponseTooLongPosition = new DtoErrorResponse();
        dtoErrorResponseTooLongPosition.addError(
                errorElementMapper.toDto(new ErrorResponseElement(ApplicationErrorDetails.VIOLATION_POSITION_MAX_LENGTH_CONSTRAINT))
        );

        var nullOldPassword =  AdminTestData.createValidUpdateAdministratorDtoRequest();
        nullOldPassword.setOldPassword(null);
        var dtoErrorResponseNullOldPassword = new DtoErrorResponse();
        dtoErrorResponseNullOldPassword.addError(
                errorElementMapper.toDto(new ErrorResponseElement(ApplicationErrorDetails.VIOLATION_PASSWORD_EMPTY_CONSTRAINT))
        );

        var tooShortOldPassword =  AdminTestData.createValidUpdateAdministratorDtoRequest();
        tooShortOldPassword.setOldPassword("y");
        var dtoErrorResponseTooShortOldPassword = new DtoErrorResponse();
        dtoErrorResponseTooShortOldPassword.addError(
                errorElementMapper.toDto(new ErrorResponseElement(ApplicationErrorDetails.VIOLATION_PASSWORD_MIN_LENGTH_CONSTRAINT))
        );

        var nullNewPassword =  AdminTestData.createValidUpdateAdministratorDtoRequest();
        nullNewPassword.setNewPassword(null);
        var dtoErrorResponseNullNewPassword = new DtoErrorResponse();
        dtoErrorResponseNullNewPassword.addError(
                errorElementMapper.toDto(new ErrorResponseElement(ApplicationErrorDetails.VIOLATION_PASSWORD_EMPTY_CONSTRAINT))
        );

        var tooShortNewPassword =  AdminTestData.createValidUpdateAdministratorDtoRequest();
        tooShortNewPassword.setNewPassword("x");
        var dtoErrorResponseTooShortNewPassword = new DtoErrorResponse();
        dtoErrorResponseTooShortNewPassword.addError(
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
                Arguments.of(nullPosition, dtoErrorResponseNullPosition),
                Arguments.of(tooLongPosition, dtoErrorResponseTooLongPosition),
                Arguments.of(nullOldPassword, dtoErrorResponseNullOldPassword),
                Arguments.of(tooShortOldPassword, dtoErrorResponseTooShortOldPassword),
                Arguments.of(nullNewPassword, dtoErrorResponseNullNewPassword),
                Arguments.of(tooShortNewPassword, dtoErrorResponseTooShortNewPassword)
        );
    }
}
