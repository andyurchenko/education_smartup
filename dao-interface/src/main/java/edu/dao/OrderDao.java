package edu.dao;

import edu.error.ApplicationException;
import edu.model.Order;
import edu.model.additional.TripDate;

import java.util.List;
import java.util.Map;

public interface OrderDao {
    void addNewOrder(Order order, TripDate tripDate) throws ApplicationException;
    Long getTripDateIdOfOrderByOrderId(long orderId) throws ApplicationException;
    Order getOrderById(long orderId) throws ApplicationException;
    void deleteOrder(long orderId) throws ApplicationException;
    List<Order> getOrdersByParams(Map<String, String> params) throws ApplicationException;
    Long getClientIdOfOrder(long orderId) throws ApplicationException;
}
