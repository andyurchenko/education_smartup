package edu.controller.e2e;

import edu.configuration.ApplicationConfig;
import edu.util.AdminTestData;
import edu.util.ClientTestData;
import edu.dto.administrator.DtoRegisterNewAdministratorRequest;
import edu.dto.administrator.DtoRegisterNewAdministratorResponse;
import edu.dto.administrator.DtoUpdateAdministratorRequest;
import edu.dto.administrator.DtoUpdateAdministratorResponse;
import edu.dto.client.DtoGetClientInfoResponse;
import edu.dto.client.DtoRegisterNewClientRequest;
import edu.dto.client.DtoRegisterNewClientResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AdminControllerWithRunningServerTest extends BaseTest {
    @Autowired
    protected AdminControllerWithRunningServerTest(@LocalServerPort Integer port, TestRestTemplate testRestTemplate, ApplicationConfig appConfig) {
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
    void registerNewAdmin() {
        var request = AdminTestData.createValidRegisterNewAdministratorDtoRequest();
        DtoRegisterNewAdministratorResponse response = testRestTemplate
                .postForObject(
                        BASE_URL + PORT + API_ADMINS_URL,
                        request, DtoRegisterNewAdministratorResponse.class
                );
        assertEquals(AdminTestData.DEFAULT_VALID_FIRST_NAME, response.getFirstName());
        assertEquals(AdminTestData.DEFAULT_VALID_LAST_NAME, response.getLastName());
        assertEquals(AdminTestData.DEFAULT_VALID_PATRONYMIC, response.getPatronymic());
        assertEquals(AdminTestData.DEFAULT_VALID_POSITION, response.getPosition());
        assertEquals(AdminTestData.USER_TYPE, response.getUserType());
    }

    @Test
    void registerNewAdminCheckCookie() {
        var request = AdminTestData.createValidRegisterNewAdministratorDtoRequest();
        HttpEntity<DtoRegisterNewAdministratorRequest> httpEntity = new HttpEntity<>(request);
        ResponseEntity<DtoRegisterNewAdministratorResponse> responseEntity = testRestTemplate
                .postForEntity(BASE_URL + PORT + API_ADMINS_URL, httpEntity, DtoRegisterNewAdministratorResponse.class);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<String> cookies = responseEntity.getHeaders().get(HttpHeaders.SET_COOKIE);
        assertNotNull(cookies);
        assertFalse(cookies.isEmpty());
        String cookie = cookies.get(0);
        Pattern pattern = Pattern.compile("(?<cookieName>" + appConfig.getJavaSessionIdName() + ")=(?<cookieValue>[a-zA-Z0-9-]+);");
        Matcher matcher = pattern.matcher(cookie);
        assertTrue(matcher.find());
        assertEquals(appConfig.getJavaSessionIdName(), matcher.group("cookieName"));
        String sessionIdValue = matcher.group("cookieValue");
        //TIP JAVASESSIONID=d926097b-9678-4b5f-9081-955ba424b1a1; Max-Age=120; Expires=Thu, 18 Jul 2024 04:00:30 GMT
        assertTrue(sessionIdValue.matches("[a-zA-Z0-9-]{36}"));
    }


    @Test
    void getAllClientsTest() {
        String clientSuffix_Yu = "Ю";
        addClientToDb(clientSuffix_Yu);
        String clientSuffix_Ya = "Я";
        addClientToDb(clientSuffix_Ya);
        String clientSuffix_Yi = "Й";
        addClientToDb(clientSuffix_Yi);

        String adminSuffix_Yu = "Ю";
        String adminSessionId = addAdminToDbAndGetItsSessionId(adminSuffix_Yu);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.COOKIE, createHeaderForSessionId(adminSessionId));
        HttpEntity<Object> httpEntity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<DtoGetClientInfoResponse[]> responseEntity = testRestTemplate
                .exchange(
                        BASE_URL + PORT + API_CLIENTS_URL,
                        HttpMethod.GET,
                        httpEntity,
                        DtoGetClientInfoResponse[].class
                );
        DtoGetClientInfoResponse[] clientInfoResponses = responseEntity.getBody();
        assertNotNull(clientInfoResponses);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<DtoGetClientInfoResponse> dtoList = new ArrayList<>(Arrays.stream(clientInfoResponses).sorted((o1, o2) -> (int) (o1.getId() - o2.getId())).toList());
        assertNotNull(dtoList);
        assertEquals(3, dtoList.size());

        assertEquals(ClientTestData.DEFAULT_VALID_FIRST_NAME + clientSuffix_Yu, dtoList.get(0).getFirstName());
        assertEquals(ClientTestData.DEFAULT_VALID_LAST_NAME + clientSuffix_Yu, dtoList.get(0).getLastName());
        assertEquals(ClientTestData.DEFAULT_VALID_PATRONYMIC + clientSuffix_Yu, dtoList.get(0).getPatronymic());
        assertEquals(ClientTestData.DEFAULT_VALID_PHONE.replaceAll(appConfig.getPhoneCutPattern(), ""), dtoList.get(0).getPhone());
        assertEquals(ClientTestData.DEFAULT_VALID_EMAIL, dtoList.get(0).getEmail());
        assertEquals(ClientTestData.USER_TYPE, dtoList.get(0).getUserType());
        assertTrue(dtoList.get(0).getId() > 0);

        assertEquals(ClientTestData.DEFAULT_VALID_FIRST_NAME + clientSuffix_Ya, dtoList.get(1).getFirstName());
        assertEquals(ClientTestData.DEFAULT_VALID_LAST_NAME + clientSuffix_Ya, dtoList.get(1).getLastName());
        assertEquals(ClientTestData.DEFAULT_VALID_PATRONYMIC + clientSuffix_Ya, dtoList.get(1).getPatronymic());
        assertEquals(ClientTestData.DEFAULT_VALID_PHONE.replaceAll(appConfig.getPhoneCutPattern(), ""), dtoList.get(1).getPhone());
        assertEquals(ClientTestData.DEFAULT_VALID_EMAIL, dtoList.get(1).getEmail());
        assertEquals(ClientTestData.USER_TYPE, dtoList.get(1).getUserType());
        assertTrue(dtoList.get(1).getId() > 0);

        assertEquals(ClientTestData.DEFAULT_VALID_FIRST_NAME + clientSuffix_Yi, dtoList.get(2).getFirstName());
        assertEquals(ClientTestData.DEFAULT_VALID_LAST_NAME + clientSuffix_Yi, dtoList.get(2).getLastName());
        assertEquals(ClientTestData.DEFAULT_VALID_PATRONYMIC + clientSuffix_Yi, dtoList.get(2).getPatronymic());
        assertEquals(ClientTestData.DEFAULT_VALID_PHONE.replaceAll(appConfig.getPhoneCutPattern(), ""), dtoList.get(2).getPhone());
        assertEquals(ClientTestData.DEFAULT_VALID_EMAIL, dtoList.get(2).getEmail());
        assertEquals(ClientTestData.USER_TYPE, dtoList.get(2).getUserType());
        assertTrue(dtoList.get(2).getId() > 0);

    }

    @Test
    void updateAdministratorAccount() {
        String adminSuffix_Ya = "Я";
        String sessionId = addAdminToDbAndGetItsSessionId(adminSuffix_Ya);
        assertNotNull(sessionId);
        var dtoUpdateRequest = AdminTestData.createValidUpdateAdministratorDtoRequest(adminSuffix_Ya);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.COOKIE, createHeaderForSessionId(sessionId));
        HttpEntity<DtoUpdateAdministratorRequest> httpEntity = new HttpEntity<>(dtoUpdateRequest, httpHeaders);
        ResponseEntity<DtoUpdateAdministratorResponse> responseEntity = testRestTemplate
                .exchange(
                        BASE_URL + PORT + API_ADMINS_URL,
                        HttpMethod.PUT,
                        httpEntity,
                        DtoUpdateAdministratorResponse.class
                );
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        DtoUpdateAdministratorResponse dtoResponseFromServer = responseEntity.getBody();
        assertNotNull(dtoResponseFromServer);
        assertEquals(AdminTestData.NEW_VALID_FIRST_NAME + adminSuffix_Ya, dtoResponseFromServer.getFirstName());
        assertEquals(AdminTestData.NEW_VALID_LAST_NAME + adminSuffix_Ya, dtoResponseFromServer.getLastName());
        assertEquals(AdminTestData.NEW_VALID_PATRONYMIC + adminSuffix_Ya, dtoResponseFromServer.getPatronymic());
        assertEquals(AdminTestData.NEW_VALID_POSITION + adminSuffix_Ya, dtoResponseFromServer.getPosition());
    }


    private void addClientToDb(String suffix) {
        var dtoRequest = ClientTestData.createValidRegisterNewClientDtoRequest(suffix);
        HttpEntity<DtoRegisterNewClientRequest> httpEntity = new HttpEntity<>(dtoRequest);
        testRestTemplate
                .exchange(
                        BASE_URL + PORT + API_CLIENTS_URL,
                        HttpMethod.POST,
                        httpEntity,
                        DtoRegisterNewClientResponse.class

                );
    }
}