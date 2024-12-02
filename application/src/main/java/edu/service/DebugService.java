package edu.service;

import edu.dao.DebugDao;
import edu.dao.UserDao;
import edu.dto.debug.DtoBooleanResponse;
import edu.error.ApplicationException;
import edu.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DebugService {
    private final DebugDao debugDao;
    private final UserDao userDao;

    @Autowired
    public DebugService(DebugDao debugDao, UserDao userDao) {
        this.debugDao = debugDao;
        this.userDao = userDao;
    }

    public void clearDataBase() throws ApplicationException {
        debugDao.clearDataBase();
    }

    public void clearTripTable() throws ApplicationException {
        debugDao.clearTripTable();
    }

    public void clearBusTable() throws ApplicationException {
        debugDao.clearBusTable();
    }

    public void clearStationTable() throws ApplicationException {
        debugDao.clearStationTable();
    }

    public void clearUserTable() throws ApplicationException {
        debugDao.clearUserTable();
    }

    public void clearActiveSessionTable() throws ApplicationException {
        debugDao.clearActiveSessionTable();
    }

    public DtoBooleanResponse isUserAccountActive(long userId) throws ApplicationException {
        User user = userDao.getUserById(userId);
        if (user != null && user.isAccountActive()) {
            return new DtoBooleanResponse(true);
        }

        return new DtoBooleanResponse(false);
    }
}
