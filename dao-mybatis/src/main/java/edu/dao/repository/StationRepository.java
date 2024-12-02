package edu.dao.repository;

import edu.model.additional.Station;
import org.apache.ibatis.annotations.Param;

public interface StationRepository {
    Station selectStationByName(@Param("STATION_NAME") String name);
    Station selectStationById(@Param("STATION_ID") long stationId);
    void insertStation(@Param("STATION") Station station);
    void deleteStationById(@Param("STATION_ID") long stationId);
    void deleteStationByName(@Param("STATION_NAME") String stationName);
    void updateStation(@Param("STATION") Station station);
}
