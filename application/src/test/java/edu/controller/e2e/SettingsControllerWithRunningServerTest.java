package edu.controller.e2e;

import edu.configuration.ApplicationConfig;
import edu.dto.settings.DtoAdminSettingsResponse;
import edu.dto.settings.DtoClientSettingsResponse;
import edu.dto.settings.DtoNotLoggedSettingsResponse;
import edu.util.config.ValidationConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SettingsControllerWithRunningServerTest extends BaseTest {
    @Autowired
    private ValidationConfig validationConfig;
    @Autowired
    protected SettingsControllerWithRunningServerTest(@LocalServerPort Integer port, TestRestTemplate testRestTemplate, ApplicationConfig appConfig) {
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
    void getServerSettingsDefault() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.COOKIE, createHeaderForSessionId("not_existing_session_id"));
        HttpEntity<?> requestEntity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<DtoNotLoggedSettingsResponse> responseEntity = testRestTemplate
                .exchange(
                        BASE_URL + PORT + API_SETTINGS_URL,
                        HttpMethod.GET,
                        requestEntity,
                        DtoNotLoggedSettingsResponse.class
                );
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        DtoNotLoggedSettingsResponse dtoResponse = responseEntity.getBody();
        assertNotNull(dtoResponse);
        assertEquals(validationConfig.getMaxStringLength(), dtoResponse.getMaxStringLength());
        assertEquals(validationConfig.getMinPasswordLength(), dtoResponse.getMinPasswordLength());
    }

    @Test
    void getServerSettingsForAdmin() {
        String adminSuffix_Ya = "Я";
        String adminSessionId = addAdminToDbAndGetItsSessionId(adminSuffix_Ya);
        assertNotNull(adminSessionId);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.COOKIE, createHeaderForSessionId(adminSessionId));
        HttpEntity<?> requestEntity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<DtoAdminSettingsResponse> responseEntity = testRestTemplate
                .exchange(
                        BASE_URL + PORT + API_SETTINGS_URL,
                        HttpMethod.GET,
                        requestEntity,
                        DtoAdminSettingsResponse.class
                );
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        DtoAdminSettingsResponse dtoResponse = responseEntity.getBody();
        assertNotNull(dtoResponse);
        assertEquals(validationConfig.getEmailPattern(), dtoResponse.getEmailPattern());
        assertEquals(validationConfig.getLoginPattern(), dtoResponse.getLoginPattern());
        assertEquals(validationConfig.getFullNamePattern(), dtoResponse.getFullNamePattern());
        assertEquals(validationConfig.getPhonePattern(), dtoResponse.getPhonePattern());
        assertEquals(validationConfig.getMaxStringLength(), dtoResponse.getMaxStringLength());
        assertEquals(validationConfig.getMinPasswordLength(), dtoResponse.getMinPasswordLength());
    }

    @Test
    void getServerSettingsForClient() {
        String clientSuffix_Ya = "Я"; //TIP only russian letters
        String clientSessionId = addNewClientAndGetItsSession(clientSuffix_Ya);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.COOKIE, createHeaderForSessionId(clientSessionId));
        HttpEntity<Object> requestEntity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<DtoClientSettingsResponse> responseEntity = testRestTemplate
                .exchange(
                        BASE_URL + PORT + API_SETTINGS_URL,
                        HttpMethod.GET,
                        requestEntity,
                        DtoClientSettingsResponse.class
                );
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        DtoClientSettingsResponse dtoResponse = responseEntity.getBody();
        assertNotNull(dtoResponse);
        assertEquals(validationConfig.getMaxStringLength(), dtoResponse.getMaxStringLength());
        assertEquals(validationConfig.getMinPasswordLength(), dtoResponse.getMinPasswordLength());
    }


}