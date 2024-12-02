package edu.controller;

import edu.configuration.ApplicationConfig;
import edu.dto.user.DtoLogin;
import edu.error.*;
import edu.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    private final UserService userService;
    private final ApplicationConfig applicationConfig;
    @Autowired
    public UserController(UserService userService, ApplicationConfig applicationConfig) {
        this.userService = userService;
        this.applicationConfig = applicationConfig;
    }

    @RequestMapping(
            path = "/api/sessions",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> login(@Valid @RequestBody DtoLogin dtoLogin, HttpServletResponse httpServletResponse) throws ApplicationException {
        Pair<?, String> userAndSession = userService.login(dtoLogin);
        Cookie cookie = new Cookie(applicationConfig.getJavaSessionIdName(), userAndSession.getRight());
        httpServletResponse.addCookie(cookie);

        return new ResponseEntity<>(userAndSession.getLeft(), HttpStatus.OK);
    }

    @RequestMapping(
            path = "/api/sessions",
            method = RequestMethod.DELETE,
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public String logout(@CookieValue(name = "${java_session_id_name}", required = false) String sessionId) throws ApplicationException {
        userService.logout(sessionId);

        return "{}";
    }

    @RequestMapping(
            path = "/api/accounts",
            method = RequestMethod.DELETE,
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public String deleteAccount(@CookieValue(name = "${java_session_id_name}", required = false) String sessionId) throws ApplicationException {
        userService.deactivateUserAccount(sessionId);

        return "{}";
    }

    @RequestMapping(
            path = "/api/accounts",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getUserInfoBySessionId(@CookieValue(name = "${java_session_id_name}", required = false) String sessionId) throws ApplicationException {
        return new ResponseEntity<>(
                userService.getUserBySessionId(sessionId),
                HttpStatus.OK
        );
    }
}
