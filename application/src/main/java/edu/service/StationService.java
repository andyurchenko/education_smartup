package edu.service;

import edu.dao.StationDao;
import edu.dao.UserDao;
import edu.dto.trip.station.DtoStationRequest;
import edu.dto.trip.station.DtoStationResponse;
import edu.error.ApplicationErrorDetails;
import edu.error.ApplicationException;
import edu.model.additional.Station;
import edu.util.mapper.StationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class StationService extends ServiceBase {
    private final StationDao stationDao;
    private final StationMapper stationMapper;

    @Autowired
    public StationService(StationDao stationDao, StationMapper stationMapper, UserDao userDao) {
        super(userDao);
        this.stationDao = stationDao;
        this.stationMapper = stationMapper;
    }

    public DtoStationResponse addNewStation(String sessionId, DtoStationRequest dtoRequest) throws ApplicationException {
        checkIfSessionIdBelongsToAdministrator(sessionId);
        Station station = stationMapper.toModel(dtoRequest);
        stationDao.addNewStation(station);

        return stationMapper.toDto(station);
    }

    public DtoStationResponse getStationById(String sessionId, long stationId) throws ApplicationException {
        checkIfSessionIdBelongsToAdministrator(sessionId);
        Station station = stationDao.getStationById(stationId);

        return stationMapper.toDto(station);
    }

    public DtoStationResponse getStationByName(String sessionId, String stationName) throws ApplicationException {
        checkIfSessionIdBelongsToAdministrator(sessionId);
        Station station = stationDao.getStationByName(stationName);

        return stationMapper.toDto(station);
    }

    public void deleteStationByName(String sessionId, String stationName) throws ApplicationException {
        checkIfSessionIdBelongsToAdministrator(sessionId);
        stationDao.deleteStationByName(stationName);
    }

    public void deleteStationById(String sessionId, long stationId) throws ApplicationException {
        checkIfSessionIdBelongsToAdministrator(sessionId);
        stationDao.deleteStationById(stationId);
    }

    public DtoStationResponse updateStation(String sessionId, long stationId, DtoStationRequest dtoRequest) throws ApplicationException {
        checkIfSessionIdBelongsToAdministrator(sessionId);
        Station station = stationDao.getStationById(stationId);
        if (station == null) {
            Map<String, Object> params = new HashMap<>();
            params.put("STATION_ID", stationId);
            throw new ApplicationException(ApplicationErrorDetails.STATION_ID_NOT_FOUND, params, HttpStatus.BAD_REQUEST);
        }
        stationMapper.updateModelFromDto(station, dtoRequest);
        stationDao.updateStation(station);

        return stationMapper.toDto(station);
    }
}
