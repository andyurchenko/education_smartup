package edu.controller;

import edu.error.ApplicationException;
import edu.service.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SettingsController {
    private final SettingsService settingsService;

    @Autowired
    public SettingsController(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    @RequestMapping(
            path = "/api/settings",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getServerSettings(@CookieValue(name = "${java_session_id_name}", required = false) String sessionId) throws ApplicationException {
        return new ResponseEntity<>(settingsService.getServerSettings(sessionId), HttpStatus.OK);
    }
}
