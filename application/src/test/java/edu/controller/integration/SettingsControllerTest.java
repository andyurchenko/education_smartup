package edu.controller.integration;

import edu.dto.settings.DtoNotLoggedSettingsResponse;
import edu.service.SettingsService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@SpringBootTest
@AutoConfigureMockMvc
class SettingsControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private SettingsService settingsService;
    private final int OK = 200;

    @Test
    void getServerSettings() throws Exception {
        String cookieName = "JAVASESSIONID";
        String sessionId = "session_id";
        DtoNotLoggedSettingsResponse dtoResponse = new DtoNotLoggedSettingsResponse(1, 2);
        Cookie cookie = new Cookie(cookieName, sessionId);
        Mockito
                .when(settingsService.getServerSettings(sessionId))
                .thenReturn(dtoResponse);

        MvcResult mvcResult = mvc
                .perform(
                        get("/api/settings")
                                .accept(MediaType.APPLICATION_JSON)
                                .cookie(cookie)
                )
                .andReturn();

        assertEquals(OK, mvcResult.getResponse().getStatus());
        Mockito.verify(settingsService, Mockito.times(1)).getServerSettings(Mockito.eq(sessionId));
    }
}