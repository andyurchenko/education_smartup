package edu.dao.repository;

import edu.model.Order;
import edu.model.additional.TripDate;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OrderRepository {
    void insertOrder(@Param("ORDER") Order order, @Param("TRIP_DATE") TripDate tripDate);
    Long selectTripDateIdOfOrderByOrderId(@Param("ORDER_ID") long orderId);
    Integer deleteOrderById(@Param("ORDER_ID") long orderId);
    Order getOrderById(@Param("ORDER_ID") long orderId);
    Integer getNumberOfPassengersInOrder(@Param("ORDER_ID") long orderId);
    List<Order> getOrdersByParams(@Param("SEARCH_PARAMS") Map<String, String> params);
    Long selectClientIdOfOrder(@Param("ORDER_ID") long orderId);
}
