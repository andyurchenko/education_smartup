package edu.dao;

import edu.error.ApplicationException;
import edu.model.additional.Passenger;

public interface PassengerDao {
    Passenger getPassengerByFullNameAndPassportAndOrderId(String firstName, String lastName, String passport, Long orderId) throws ApplicationException;
}
