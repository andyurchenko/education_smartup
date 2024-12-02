package edu.controller;

import edu.configuration.ApplicationConfig;
import edu.dto.administrator.DtoRegisterNewAdministratorRequest;
import edu.dto.administrator.DtoRegisterNewAdministratorResponse;
import edu.dto.administrator.DtoUpdateAdministratorRequest;
import edu.dto.administrator.DtoUpdateAdministratorResponse;
import edu.dto.client.DtoGetClientInfoResponse;
import edu.error.*;
import edu.service.AdminService;
import edu.service.DebugService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AdminController {
    private final AdminService adminService;
    private final ApplicationConfig applicationConfig;
    private final DebugService debugService;

    @Autowired
    public AdminController(AdminService adminService, ApplicationConfig applicationConfig, DebugService debugService) {
        this.adminService = adminService;
        this.applicationConfig = applicationConfig;
        this.debugService = debugService;
    }

    @RequestMapping(
            path = "/api/admins",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public DtoRegisterNewAdministratorResponse registerNewAdministrator(@Valid @RequestBody DtoRegisterNewAdministratorRequest dtoRequest, HttpServletResponse httpServletResponse) throws ApplicationException {
        Pair<DtoRegisterNewAdministratorResponse, String> response =  adminService.registerNewAdministratorAndStartNewAdminSession(dtoRequest);
        Cookie cookie = new Cookie(applicationConfig.getJavaSessionIdName(), response.getRight());
        cookie.setMaxAge(applicationConfig.getUserIdleTimeout());
        httpServletResponse.addCookie(cookie);

        return response.getLeft();
    }

    @RequestMapping(
            path = "/api/clients",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<DtoGetClientInfoResponse> getAllClients(@CookieValue(name = "${java_session_id_name}", required = false) String sessionId) throws ApplicationException {
        return adminService.getAllClients(sessionId);
    }

    @RequestMapping(
            path = "/api/admins",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public DtoUpdateAdministratorResponse updateAdministratorAccount(@CookieValue(name = "${java_session_id_name}", required = false) String sessionsId, @Valid @RequestBody DtoUpdateAdministratorRequest dtoRequest) throws ApplicationException {
        return adminService.updateAdministratorInfo(sessionsId, dtoRequest);
    }
}
