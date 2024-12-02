package edu.controller.e2e;

import edu.configuration.ApplicationConfig;
import edu.util.ClientTestData;
import edu.dto.client.DtoRegisterNewClientResponse;
import edu.dto.client.DtoUpdateClientRequest;
import edu.dto.client.DtoUpdateClientResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ClientControllerWithRunningServerTest extends BaseTest {
    @Autowired
    protected ClientControllerWithRunningServerTest(@LocalServerPort Integer port, TestRestTemplate testRestTemplate, ApplicationConfig appConfig) {
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
    void registerNewClient() {
        var request = ClientTestData.createValidRegisterNewClientDtoRequest();
        ResponseEntity<DtoRegisterNewClientResponse> responseEntity = testRestTemplate
                .postForEntity(BASE_URL + PORT + API_CLIENTS_URL, request, DtoRegisterNewClientResponse.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        var dtoResponse = responseEntity.getBody();
        assertNotNull(dtoResponse);
        assertEquals(ClientTestData.DEFAULT_VALID_FIRST_NAME, dtoResponse.getFirstName());
        assertEquals(ClientTestData.DEFAULT_VALID_LAST_NAME, dtoResponse.getLastName());
        assertEquals(ClientTestData.DEFAULT_VALID_PATRONYMIC, dtoResponse.getPatronymic());
        assertEquals(ClientTestData.DEFAULT_VALID_EMAIL, dtoResponse.getEmail());
        assertEquals(ClientTestData.DEFAULT_VALID_PHONE.replaceAll(appConfig.getPhoneCutPattern(), ""), dtoResponse.getPhone());
        assertEquals(ClientTestData.USER_TYPE, dtoResponse.getUserType());
        assertTrue(dtoResponse.getId() > 0);
        List<String> cookies = responseEntity.getHeaders().get(HttpHeaders.SET_COOKIE);

        assertNotNull(cookies);
        assertFalse(cookies.isEmpty());
        String cookie = cookies.get(0);
        Pattern pattern = Pattern.compile("(?<cookieName>" + appConfig.getJavaSessionIdName() + ")=(?<cookieValue>[a-zA-Z0-9-]+)");
        Matcher matcher = pattern.matcher(cookie);
        assertTrue(matcher.find());
        assertEquals(appConfig.getJavaSessionIdName(), matcher.group("cookieName"));
        String sessionIdValue = matcher.group("cookieValue");
        //TIP JAVASESSIONID=d926097b-9678-4b5f-9081-955ba424b1a1; Max-Age=120; Expires=Thu, 18 Jul 2024 04:00:30 GMT
        assertTrue(sessionIdValue.matches("[a-zA-Z0-9-]{36}"));
    }

    @Test
    void updateClientAccount() {
        var createClientDtoRequest = ClientTestData.createValidRegisterNewClientDtoRequest();
        ResponseEntity<DtoRegisterNewClientResponse> createClientResponseEntity = testRestTemplate
                .postForEntity(BASE_URL + PORT + API_CLIENTS_URL, createClientDtoRequest, DtoRegisterNewClientResponse.class);

        List<String> cookies = createClientResponseEntity.getHeaders().get(HttpHeaders.SET_COOKIE);
        assertNotNull(cookies);
        assertFalse(cookies.isEmpty());
        String cookie = cookies.get(0);
        Pattern pattern = Pattern.compile("(?<cookieName>" + appConfig.getJavaSessionIdName() + ")=(?<cookieValue>[a-zA-Z0-9-]+)");
        Matcher matcher = pattern.matcher(cookie);
        assertTrue(matcher.find());
        assertEquals(appConfig.getJavaSessionIdName(), matcher.group("cookieName"));
        String clientSessionId = matcher.group("cookieValue");
        //TIP JAVASESSIONID=d926097b-9678-4b5f-9081-955ba424b1a1; Max-Age=120; Expires=Thu, 18 Jul 2024 04:00:30 GMT
        assertTrue(clientSessionId.matches("[a-zA-Z0-9-]{36}"));


        var updateClientDtoRequest = ClientTestData.createValidUpdateClientDtoRequest();
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add(HttpHeaders.COOKIE, createHeaderForSessionId(clientSessionId));
        HttpEntity<DtoUpdateClientRequest> httpEntity = new HttpEntity<>(updateClientDtoRequest, requestHeaders);
        ResponseEntity<DtoUpdateClientResponse> updateClientResponseEntity = testRestTemplate
                .exchange(
                        BASE_URL + PORT + API_CLIENTS_URL,
                        HttpMethod.PUT,
                        httpEntity,
                        DtoUpdateClientResponse.class
                );

        var dtoResponse = updateClientResponseEntity.getBody();
        assertNotNull(dtoResponse);
        assertEquals(ClientTestData.NEW_VALID_FIRST_NAME, dtoResponse.getFirstName());
        assertEquals(ClientTestData.NEW_VALID_LAST_NAME, dtoResponse.getLastName());
        assertEquals(ClientTestData.NEW_VALID_PATRONYMIC, dtoResponse.getPatronymic());
        assertEquals(ClientTestData.NEW_VALID_EMAIL, dtoResponse.getEmail());
        assertEquals(ClientTestData.NEW_VALID_PHONE.replaceAll(appConfig.getPhoneCutPattern(), ""), dtoResponse.getPhone());
    }
}