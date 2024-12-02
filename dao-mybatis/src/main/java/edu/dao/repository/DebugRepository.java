package edu.dao.repository;


public interface DebugRepository {
    void clearDataBase();
    void clearTripTable();
    void clearUserTable();
    void clearActiveSessionTable();
    void clearBusTable();
    void clearStationTable();

}