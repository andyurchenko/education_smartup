package edu.dao.repository;

import edu.model.Bus;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BusRepository {
    void insertBus(@Param("BUS") Bus bus);
    List<Bus> selectAllBuses();
    Bus selectBusByBrandName(@Param("BRAND_NAME") String busBrand);
    Bus selectBusById(@Param("BUS_ID") long busId);

    void deleteBusById(@Param("BUS_ID") long busId);

    void deleteBusByBrandName(@Param("BRAND_NAME") String busBrand);

    void updateBus(@Param("BUS") Bus bus);
}
