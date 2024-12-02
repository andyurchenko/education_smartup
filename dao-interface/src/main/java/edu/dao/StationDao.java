package edu.dao;

import edu.error.ApplicationException;
import edu.model.additional.Station;

public interface StationDao {
    Station getStationByName(String stationName) throws ApplicationException;
    Station getStationById(long stationId) throws ApplicationException;
    void addNewStation(Station station) throws ApplicationException;
    void deleteStationById(long stationId) throws ApplicationException;
    void deleteStationByName(String stationName) throws ApplicationException;
    void updateStation(Station station) throws ApplicationException;
}
