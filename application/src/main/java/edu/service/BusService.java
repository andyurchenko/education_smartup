package edu.service;

import edu.dao.BusDao;
import edu.dao.UserDao;
import edu.dto.trip.bus.DtoBusRequest;
import edu.dto.trip.bus.DtoBusResponse;
import edu.dto.trip.bus.DtoBusWithIdResponse;
import edu.error.ApplicationException;
import edu.model.Bus;
import edu.util.mapper.BusMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BusService extends ServiceBase {
    private final BusMapper busMapper;
    private final BusDao busDao;

    @Autowired
    public BusService(BusMapper busMapper, UserDao userDao, BusDao busDao) {
        super(userDao);
        this.busMapper = busMapper;
        this.busDao = busDao;
    }

    public DtoBusWithIdResponse addNewBus(String sessionId, DtoBusRequest dtoBusRequest) throws ApplicationException {
        checkIfSessionIdBelongsToAdministrator(sessionId);
        Bus bus = busMapper.toModel(dtoBusRequest);
        busDao.addNewBusToDB(bus);

        return busMapper.toDtoWithId(bus);
    }

    public List<DtoBusResponse> getAllBuses(String sessionId) throws ApplicationException {
        checkIfSessionIdBelongsToAdministrator(sessionId);
        List<Bus> busListFromDb = busDao.selectAllBuses();

        return busMapper.toDtoList(busListFromDb);
    }
}
