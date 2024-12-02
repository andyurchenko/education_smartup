package edu.controller.e2e;

import edu.configuration.ApplicationConfig;
import edu.util.AdminTestData;
import edu.util.ClientTestData;
import edu.dto.administrator.DtoAdministratorResponse;
import edu.dto.administrator.DtoRegisterNewAdministratorRequest;
import edu.dto.administrator.DtoRegisterNewAdministratorResponse;
import edu.dto.client.DtoClientResponse;
import edu.dto.client.DtoGetClientInfoResponse;
import edu.dto.client.DtoRegisterNewClientResponse;
import edu.dto.debug.DtoBooleanResponse;
import edu.dto.debug.DtoIsSessionActiveRequest;
import edu.dto.error.DtoErrorResponse;
import edu.dto.error.DtoErrorResponseElement;
import edu.dto.user.DtoLogin;
import edu.error.ApplicationErrorDetails;
import edu.error.ErrorResponseElement;
import edu.util.mapper.ErrorElementMapper;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerWithRunningServerTest extends BaseTest {
    @Autowired
    private ErrorElementMapper errorElementMapper;

    private final String EMPTY_JSON_RESPONSE = "{}";

    @Autowired
    protected UserControllerWithRunningServerTest(@LocalServerPort Integer port, TestRestTemplate testRestTemplate, ApplicationConfig appConfig) {
        super(port, testRestTemplate, appConfig);
    }

    @AfterEach
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
    void loginForClient() {
        String clientSuffix_Yu = "Ю";
        addNewClientAndGetItsSession(clientSuffix_Yu);

        DtoLogin dtoLogin = new DtoLogin(ClientTestData.DEFAULT_VALID_LOGIN + clientSuffix_Yu, ClientTestData.DEFAULT_VALID_PASSWORD + clientSuffix_Yu);
        ResponseEntity<DtoClientResponse> responseEntity = testRestTemplate
                .exchange(
                        BASE_URL + PORT + API_SESSIONS_URL,
                        HttpMethod.POST,
                        new HttpEntity<>(dtoLogin),
                        DtoClientResponse.class
                );
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        DtoClientResponse dtoResponse = responseEntity.getBody();
        assertNotNull(dtoResponse);
        assertEquals(ClientTestData.DEFAULT_VALID_FIRST_NAME + clientSuffix_Yu, dtoResponse.getFirstName());
        assertEquals(ClientTestData.DEFAULT_VALID_LAST_NAME + clientSuffix_Yu, dtoResponse.getLastName());
        assertEquals(ClientTestData.DEFAULT_VALID_PATRONYMIC + clientSuffix_Yu, dtoResponse.getPatronymic());
        assertEquals(ClientTestData.DEFAULT_VALID_EMAIL, dtoResponse.getEmail());
        assertEquals(ClientTestData.DEFAULT_VALID_PHONE.replaceAll(appConfig.getPhoneCutPattern(), ""), dtoResponse.getPhone());
        assertEquals(ClientTestData.USER_TYPE, dtoResponse.getUserType());
        assertTrue(dtoResponse.getId() > 0);

        HttpHeaders httpHeaders = responseEntity.getHeaders();
        String cookieValue = httpHeaders.getFirst(HttpHeaders.SET_COOKIE);
        assertNotNull(cookieValue);
        assertTrue(cookieValue.matches(appConfig.getJavaSessionIdName() + "=[a-zA-Z0-9]{8}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{12}"));
    }

    @Test
    void loginForAdmin() {
        String adminSuffix_Yu = "Ю";
        addNewAdminToDb(adminSuffix_Yu);

        DtoLogin dtoLogin = new DtoLogin(AdminTestData.DEFAULT_VALID_LOGIN + adminSuffix_Yu, AdminTestData.DEFAULT_VALID_PASSWORD + adminSuffix_Yu);
        ResponseEntity<DtoAdministratorResponse> responseEntity = testRestTemplate
                .exchange(
                        BASE_URL + PORT + API_SESSIONS_URL,
                        HttpMethod.POST,
                        new HttpEntity<>(dtoLogin),
                        DtoAdministratorResponse.class
                );
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        DtoAdministratorResponse dtoResponse = responseEntity.getBody();
        assertNotNull(dtoResponse);
        assertEquals(AdminTestData.DEFAULT_VALID_FIRST_NAME + adminSuffix_Yu, dtoResponse.getFirstName());
        assertEquals(AdminTestData.DEFAULT_VALID_LAST_NAME + adminSuffix_Yu, dtoResponse.getLastName());
        assertEquals(AdminTestData.DEFAULT_VALID_PATRONYMIC + adminSuffix_Yu, dtoResponse.getPatronymic());
        assertEquals(AdminTestData.DEFAULT_VALID_POSITION + adminSuffix_Yu, dtoResponse.getPosition());
        assertEquals(AdminTestData.USER_TYPE, dtoResponse.getUserType());
        assertTrue(dtoResponse.getId() > 0);

        HttpHeaders httpHeaders = responseEntity.getHeaders();
        String cookieValue = httpHeaders.getFirst(HttpHeaders.SET_COOKIE);
        assertNotNull(cookieValue);
        assertTrue(cookieValue.matches(appConfig.getJavaSessionIdName() + "=[a-zA-Z0-9]{8}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{12}"));
    }

    @Test
    void logoutForClient() {
        String clientSuffix_Yu = "Ю";
        String userSessionId = addNewClientAndGetItsSession(clientSuffix_Yu);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.COOKIE, createHeaderForSessionId(userSessionId));
        ResponseEntity<String> responseEntity = testRestTemplate
                .exchange(
                        BASE_URL + PORT + API_SESSIONS_URL,
                        HttpMethod.DELETE,
                        new HttpEntity<>(null, httpHeaders),
                        String.class
                );
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        String emptyJson = responseEntity.getBody();
        assertNotNull(emptyJson);
        assertEquals(EMPTY_JSON_RESPONSE, emptyJson);

        ResponseEntity<DtoBooleanResponse> sessionResponseEntity = testRestTemplate
                .exchange(
                        BASE_URL + PORT + "/debug/sessions/" + userSessionId + "/status",
                        HttpMethod.GET,
                        new HttpEntity<>(new DtoIsSessionActiveRequest(userSessionId)),
                        DtoBooleanResponse.class
                );

        DtoBooleanResponse dtoResponse = sessionResponseEntity.getBody();
        assertNotNull(dtoResponse);
        assertFalse(dtoResponse.isValue());
    }

    @Test
    void logoutForAdministrator() {
        String adminSuffix_Yu = "Ю";
        String userSessionId = addNewAdminToDbAndGetItsSessionId(adminSuffix_Yu);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.COOKIE, createHeaderForSessionId(userSessionId));
        ResponseEntity<String> responseEntity = testRestTemplate
                .exchange(
                        BASE_URL + PORT + API_SESSIONS_URL,
                        HttpMethod.DELETE,
                        new HttpEntity<>(null, httpHeaders),
                        String.class
                );
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        String emptyJson = responseEntity.getBody();
        assertNotNull(emptyJson);
        assertEquals(EMPTY_JSON_RESPONSE, emptyJson);

        ResponseEntity<DtoBooleanResponse> sessionResponseEntity = testRestTemplate
                .exchange(
                        BASE_URL + PORT + "/debug/sessions/" + userSessionId + "/status",
                        HttpMethod.GET,
                        new HttpEntity<>(new DtoIsSessionActiveRequest(userSessionId)),
                        DtoBooleanResponse.class
                );

        DtoBooleanResponse dtoResponse = sessionResponseEntity.getBody();
        assertNotNull(dtoResponse);
        assertFalse(dtoResponse.isValue());
    }

    @Test
    void deleteAccountForClient() {
        String clientSuffix_Yu = "Ю";
        var pairOfDtoAndSession = addNewClientAndGetDtoAndSession(clientSuffix_Yu);
        assertNotNull(pairOfDtoAndSession);
        assertNotNull(pairOfDtoAndSession.getLeft());
        assertNotNull(pairOfDtoAndSession.getRight());

        var dtoClientResponse = pairOfDtoAndSession.getLeft();
        String userSessionId = pairOfDtoAndSession.getRight();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.COOKIE, createHeaderForSessionId(userSessionId));
        ResponseEntity<String> responseEntityDeleteAccout = testRestTemplate
                .exchange(
                        BASE_URL + PORT + API_ACCOUNTS_URL,
                        HttpMethod.DELETE,
                        new HttpEntity<>(null, httpHeaders),
                        String.class
                );
        assertNotNull(responseEntityDeleteAccout);
        assertEquals(HttpStatus.OK, responseEntityDeleteAccout.getStatusCode());
        String responseJson = responseEntityDeleteAccout.getBody();
        assertNotNull(responseJson);
        assertEquals(EMPTY_JSON_RESPONSE, responseJson);

        var responseEntity = testRestTemplate
                .exchange(
                        BASE_URL + PORT + "/debug/users/" + dtoClientResponse.getId() + "/status",
                        HttpMethod.GET,
                        new HttpEntity<>(null),
                        DtoBooleanResponse.class
                );

        DtoBooleanResponse dtoBooleanResponse = responseEntity.getBody();
        assertNotNull(dtoBooleanResponse);
        assertFalse(dtoBooleanResponse.isValue());
    }

    @Test
    void deleteAccountForAdminPositive() {
        String adminSuffix_Yu = "Ю";
        addNewAdminToDb(adminSuffix_Yu);
        String adminSuffix_Ya = "Я";
        var pairOfDtoAndSession = addNewAdminAndGetDtoAndSession(adminSuffix_Ya);
        assertNotNull(pairOfDtoAndSession);
        assertNotNull(pairOfDtoAndSession.getLeft());
        assertNotNull(pairOfDtoAndSession.getRight());

        var dtoAdminResponse = pairOfDtoAndSession.getLeft();
        String userSessionId = pairOfDtoAndSession.getRight();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.COOKIE, createHeaderForSessionId(userSessionId));
        ResponseEntity<String> responseEntityDeleteAccount = testRestTemplate
                .exchange(
                        BASE_URL + PORT + API_ACCOUNTS_URL,
                        HttpMethod.DELETE,
                        new HttpEntity<>(null, httpHeaders),
                        String.class
                );
        assertNotNull(responseEntityDeleteAccount);
        assertEquals(HttpStatus.OK, responseEntityDeleteAccount.getStatusCode());
        String responseJson = responseEntityDeleteAccount.getBody();
        assertNotNull(responseJson);
        assertEquals(EMPTY_JSON_RESPONSE, responseJson);

        var responseEntity = testRestTemplate
                .exchange(
                        BASE_URL + PORT + "/debug/users/" + dtoAdminResponse.getId() + "/status",
                        HttpMethod.GET,
                        new HttpEntity<>(null),
                        DtoBooleanResponse.class
                );

        DtoBooleanResponse dtoBooleanResponse = responseEntity.getBody();
        assertNotNull(dtoBooleanResponse);
        assertFalse(dtoBooleanResponse.isValue());
    }

    @Test
    void deleteAccountForAdminNegative() {
        String adminSuffix_Ya = "Я";
        var pairOfDtoAndSession = addNewAdminAndGetDtoAndSession(adminSuffix_Ya);
        assertNotNull(pairOfDtoAndSession);
        assertNotNull(pairOfDtoAndSession.getLeft());
        assertNotNull(pairOfDtoAndSession.getRight());
        DtoRegisterNewAdministratorResponse administrator = pairOfDtoAndSession.getLeft();
        String userSessionId = pairOfDtoAndSession.getRight();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.COOKIE, createHeaderForSessionId(userSessionId));
        ResponseEntity<DtoErrorResponse> responseEntityToDeleteUser = testRestTemplate
                .exchange(
                        BASE_URL + PORT + API_ACCOUNTS_URL,
                        HttpMethod.DELETE,
                        new HttpEntity<>(null, httpHeaders),
                        DtoErrorResponse.class
                );
        assertNotNull(responseEntityToDeleteUser);
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, responseEntityToDeleteUser.getStatusCode());
        DtoErrorResponse dtoErrorResponseFromServer = responseEntityToDeleteUser.getBody();
        assertNotNull(dtoErrorResponseFromServer);
        ErrorResponseElement errorResponseElement = new ErrorResponseElement(ApplicationErrorDetails.CAN_NOT_DELETE_LAST_ADMIN);
        DtoErrorResponse dto = new DtoErrorResponse();
        DtoErrorResponseElement dtoElement = errorElementMapper.toDto(errorResponseElement);
        dto.addError(dtoElement);
        assertEquals(dto, dtoErrorResponseFromServer);

        var responseEntity = testRestTemplate
                .exchange(
                        BASE_URL + PORT + "/debug/users/" + administrator.getId() + "/status",
                        HttpMethod.GET,
                        new HttpEntity<>(null),
                        DtoBooleanResponse.class
                );

        DtoBooleanResponse dtoBooleanResponse = responseEntity.getBody();
        assertNotNull(dtoBooleanResponse);
        assertTrue(dtoBooleanResponse.isValue());
    }

    @Test
    void getUserInfoBySessionIdForClient() {
        String clientSuffix_Yu = "Ю";
        String sessionId = addNewClientAndGetItsSession(clientSuffix_Yu);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.COOKIE, createHeaderForSessionId(sessionId));
        HttpEntity<Object> httpEntity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<DtoGetClientInfoResponse> responseEntity = testRestTemplate
                .exchange(
                        BASE_URL + PORT + API_ACCOUNTS_URL,
                        HttpMethod.GET,
                        httpEntity,
                        DtoGetClientInfoResponse.class
                );
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        DtoGetClientInfoResponse dtoResponse = responseEntity.getBody();
        assertNotNull(dtoResponse);
        assertEquals(ClientTestData.DEFAULT_VALID_FIRST_NAME + clientSuffix_Yu, dtoResponse.getFirstName());
        assertEquals(ClientTestData.DEFAULT_VALID_LAST_NAME + clientSuffix_Yu, dtoResponse.getLastName());
        assertEquals(ClientTestData.DEFAULT_VALID_PATRONYMIC + clientSuffix_Yu, dtoResponse.getPatronymic());
        assertEquals(ClientTestData.DEFAULT_VALID_EMAIL, dtoResponse.getEmail());
        assertEquals(ClientTestData.DEFAULT_VALID_PHONE.replaceAll(appConfig.getPhoneCutPattern(), ""), dtoResponse.getPhone());
        assertEquals(ClientTestData.USER_TYPE, dtoResponse.getUserType());
        assertTrue(dtoResponse.getId() > 0);
    }

    @Test
    void getUserInfoBySessionIdForAdministrator() {
        String adminSuffix_Yu = "Ю";
        String sessionId = addNewAdminToDbAndGetItsSessionId(adminSuffix_Yu);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.COOKIE, createHeaderForSessionId(sessionId));
        HttpEntity<Object> httpEntity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<DtoAdministratorResponse> responseEntity = testRestTemplate
                .exchange(
                        BASE_URL + PORT + API_ACCOUNTS_URL,
                        HttpMethod.GET,
                        httpEntity,
                        DtoAdministratorResponse.class
                );
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        DtoAdministratorResponse dtoResponse = responseEntity.getBody();
        assertNotNull(dtoResponse);
        assertEquals(AdminTestData.DEFAULT_VALID_FIRST_NAME + adminSuffix_Yu, dtoResponse.getFirstName());
        assertEquals(AdminTestData.DEFAULT_VALID_LAST_NAME + adminSuffix_Yu, dtoResponse.getLastName());
        assertEquals(AdminTestData.DEFAULT_VALID_PATRONYMIC + adminSuffix_Yu, dtoResponse.getPatronymic());
        assertEquals(AdminTestData.DEFAULT_VALID_POSITION + adminSuffix_Yu, dtoResponse.getPosition());
        assertEquals(AdminTestData.USER_TYPE, dtoResponse.getUserType());
        assertTrue(dtoResponse.getId() > 0);
    }

    private Pair<DtoRegisterNewAdministratorResponse, String> addNewAdminAndGetDtoAndSession(String suffix) {
        var dto = AdminTestData.createValidRegisterNewAdministratorDtoRequest(suffix);
        var responseEntity = testRestTemplate
                .exchange(
                        BASE_URL + PORT + API_ADMINS_URL,
                        HttpMethod.POST,
                        new HttpEntity<>(dto),
                        DtoRegisterNewAdministratorResponse.class
                );
        DtoRegisterNewAdministratorResponse dtoResponse = responseEntity.getBody();
        assertNotNull(dtoResponse);
        String cookie = responseEntity.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        assertNotNull(cookie);
        assertFalse(cookie.isEmpty());
        Pattern pattern = Pattern.compile("(?<cookieName>" + appConfig.getJavaSessionIdName() + ")=(?<cookieValue>[a-zA-Z0-9-]+)");
        Matcher matcher = pattern.matcher(cookie);
        assertTrue(matcher.find());
        assertEquals(appConfig.getJavaSessionIdName(), matcher.group("cookieName"));
        String sessionId = matcher.group("cookieValue");
        //TIP JAVASESSIONID=d926097b-9678-4b5f-9081-955ba424b1a1; Max-Age=120; Expires=Thu, 18 Jul 2024 04:00:30 GMT
        assertTrue(sessionId.matches("[a-zA-Z0-9-]{36}"));

        return new ImmutablePair<>(dtoResponse, sessionId);
    }

    private String addNewAdminToDbAndGetItsSessionId(String suffix) {
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

    private void addNewAdminToDb(String suffix) {
        var dtoRequest = AdminTestData.createValidRegisterNewAdministratorDtoRequest(suffix);
        testRestTemplate
                .exchange(
                        BASE_URL + PORT + API_ADMINS_URL,
                        HttpMethod.POST,
                        new HttpEntity<>(dtoRequest),
                        DtoRegisterNewAdministratorRequest.class
                );
    }

    private Pair<DtoRegisterNewClientResponse, String> addNewClientAndGetDtoAndSession(String suffix) {
        var dto = ClientTestData.createValidRegisterNewClientDtoRequest(suffix);
        var responseEntity = testRestTemplate
                .exchange(
                        BASE_URL + PORT + API_CLIENTS_URL,
                        HttpMethod.POST,
                        new HttpEntity<>(dto),
                        DtoRegisterNewClientResponse.class
                );
        DtoRegisterNewClientResponse dtoResponse = responseEntity.getBody();
        assertNotNull(dtoResponse);
        String cookie = responseEntity.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        assertNotNull(cookie);
        assertFalse(cookie.isEmpty());
        Pattern pattern = Pattern.compile("(?<cookieName>" + appConfig.getJavaSessionIdName() + ")=(?<cookieValue>[a-zA-Z0-9-]+)");
        Matcher matcher = pattern.matcher(cookie);
        assertTrue(matcher.find());
        assertEquals(appConfig.getJavaSessionIdName(), matcher.group("cookieName"));
        String sessionId = matcher.group("cookieValue");
        //TIP JAVASESSIONID=d926097b-9678-4b5f-9081-955ba424b1a1; Max-Age=120; Expires=Thu, 18 Jul 2024 04:00:30 GMT
        assertTrue(sessionId.matches("[a-zA-Z0-9-]{36}"));

        return new ImmutablePair<>(dtoResponse, sessionId);
    }
}