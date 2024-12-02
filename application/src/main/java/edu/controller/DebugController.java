package edu.controller;

import edu.dao.AdministratorDao;
import edu.dto.administrator.DtoRegisterNewAdministratorResponse;
import edu.dto.debug.DtoBooleanResponse;
import edu.error.ApplicationException;
import edu.service.AdminService;
import edu.service.DebugService;
import edu.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class DebugController {
    private final AdminService adminService;
    private final AdministratorDao administratorDao;
    private final DebugService debugService;
    private final SessionService sessionService;
    @Autowired
    public DebugController(AdminService adminService, AdministratorDao administratorDao, DebugService debugService, SessionService sessionService) {
        this.adminService = adminService;
        this.administratorDao = administratorDao;
        this.debugService = debugService;
        this.sessionService = sessionService;
    }

    @RequestMapping(
            path = "/test/cookie/",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public String getCookieValue(@CookieValue(name = "${java_session_id_name}", required = false) String sessionId) {
        return sessionId;
    }

    @RequestMapping(
            path = "/debug/db",
            method = RequestMethod.DELETE,
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.ALL_VALUE
    )
    public String clearDataBase() throws ApplicationException {
        debugService.clearDataBase();

        return "{}";
    }

    @RequestMapping(
            path = "/debug/db/table/trip",
            method = RequestMethod.DELETE,
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.ALL_VALUE
    )
    public String clearTripTable() throws ApplicationException {
        debugService.clearTripTable();

        return "{}";
    }

    @RequestMapping(
            path = "/debug/db/table/station",
            method = RequestMethod.DELETE,
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.ALL_VALUE
    )
    public String clearStationTable() throws ApplicationException {
        debugService.clearStationTable();

        return "{}";
    }

    @RequestMapping(
            path = "/debug/db/table/active-session",
            method = RequestMethod.DELETE,
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.ALL_VALUE
    )
    public String clearActiveSessionTable() throws ApplicationException {
        debugService.clearActiveSessionTable();

        return "{}";
    }

    @RequestMapping(
            path = "/debug/db/table/user",
            method = RequestMethod.DELETE,
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.ALL_VALUE
    )
    public String clearUserTable() throws ApplicationException {
        debugService.clearUserTable();

        return "{}";
    }

    @RequestMapping(
            path = "/debug/db/table/bus",
            method = RequestMethod.DELETE,
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.ALL_VALUE
    )
    public String clearBusTable() throws ApplicationException {
        debugService.clearBusTable();

        return "{}";
    }

    @RequestMapping(
            path = "/debug/sessions/{SESSION_ID}/status",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public DtoBooleanResponse isSessionActive(@PathVariable("SESSION_ID") String sessionId) throws ApplicationException {
        return sessionService.isSessionActive(sessionId);
    }

    @RequestMapping(
            path = "/debug/users/{USER_ID}/status",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public DtoBooleanResponse isUserAccountActive(@PathVariable("USER_ID") long userId) throws ApplicationException {
        return debugService.isUserAccountActive(userId);
    }

    @RequestMapping(
            path = "/test-api/admins/{ID}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public DtoRegisterNewAdministratorResponse getAdministratorById_TEMP(@PathVariable("ID") long id) throws ApplicationException {
        return adminService.getAdministratorById(id);
    }

    @RequestMapping(
            path = "/test-api/admins/count",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Integer getAdministratorById_TEMP() throws ApplicationException {
        return administratorDao.getAdministratorCount();
    }
}
