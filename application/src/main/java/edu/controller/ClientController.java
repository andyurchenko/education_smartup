package edu.controller;

import edu.configuration.ApplicationConfig;
import edu.dto.client.*;
import edu.error.*;
import edu.service.ClientService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class ClientController {
    private final ClientService clientService;
    private final ApplicationConfig applicationConfig;

    @Autowired
    public ClientController(ClientService clientService, ApplicationConfig applicationConfig) {
        this.clientService = clientService;
        this.applicationConfig = applicationConfig;
    }

    @RequestMapping(
            path = "/api/clients",
            method = RequestMethod.POST,
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public DtoRegisterNewClientResponse registerNewClient(@Valid @RequestBody DtoRegisterNewClientRequest dtoRequest, HttpServletResponse httpServletResponse) throws ApplicationException {
        var dtoAndCookie = clientService.registerNewClient(dtoRequest);
        Cookie sessionId = new Cookie(applicationConfig.getJavaSessionIdName(), dtoAndCookie.getRight());
        httpServletResponse.addCookie(sessionId);

        return dtoAndCookie.getLeft();
    }

    @RequestMapping(
            path = "/api/clients",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public DtoUpdateClientResponse updateClientAccount(@CookieValue(name = "${java_session_id_name}", required = false) String sessionsId, @Valid @RequestBody DtoUpdateClientRequest dtoRequest) throws ApplicationException {
        return clientService.updateClientInfo(sessionsId, dtoRequest);
    }
}
