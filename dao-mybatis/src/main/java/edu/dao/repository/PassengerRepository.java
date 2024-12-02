package edu.dao.repository;

import edu.model.additional.Passenger;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PassengerRepository {
    void insertPassengers(@Param("ORDER_ID")long id, @Param("list") List<Passenger> passengers);
    Passenger selectPassengerByFullNameAndPassport(@Param("FIRST_NAME") String firstName, @Param("LAST_NAME") String lastName, @Param("PASSPORT") String passport, @Param("ORDER_ID") long orderId);
}
