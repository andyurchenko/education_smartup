package edu.dao;

import edu.error.ApplicationException;

public interface DebugDao {
    void clearDataBase() throws ApplicationException;

    void clearTripTable() throws ApplicationException;
    void clearUserTable() throws ApplicationException;
    void clearActiveSessionTable() throws ApplicationException;
    void clearBusTable() throws ApplicationException;
    void clearStationTable() throws ApplicationException;
}
