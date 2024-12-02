package edu.dao;

import edu.error.ApplicationException;
import edu.model.Bus;

import java.util.List;

public interface BusDao {
    void addNewBusToDB(Bus bus) throws ApplicationException;
    List<Bus> selectAllBuses() throws ApplicationException;
    Bus getBusByBusBrandName(String busBrandName) throws ApplicationException;
    Bus getBusByBusById(long busId) throws ApplicationException;
    void deleteBusById(long busId) throws ApplicationException;
    void deleteBusByBrandName(String busBrand) throws ApplicationException;
    void updateBus(Bus bus) throws ApplicationException;
}
