package edu.controller;

import edu.dto.order.DtoOrderRequest;
import edu.dto.order.DtoOrderResponse;
import edu.error.ApplicationException;
import edu.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @RequestMapping(
            path = "/api/orders",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public DtoOrderResponse makeOrder(@CookieValue(name = "${java_session_id_name}", required = false) String sessionId, @Valid @RequestBody DtoOrderRequest dtoOrderRequest) throws ApplicationException {
        return orderService.makeOrder(sessionId, dtoOrderRequest);
    }

    @RequestMapping(
            path = "/api/orders/{ORDER_ID}",
            method = RequestMethod.DELETE,
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.ALL_VALUE
    )
    public String deleteOrderById(@CookieValue(name = "${java_session_id_name}", required = false) String sessionId, @PathVariable("ORDER_ID") long orderId) throws ApplicationException {
        orderService.deleteOrder(sessionId, orderId);

        return "{}";
    }

    @RequestMapping(
            path = "/api/orders",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<DtoOrderResponse> getOrdersByParams(@CookieValue(name = "${java_session_id_name}", required = false) String sessionId, @RequestParam Map<String, String> params) throws ApplicationException {
        return orderService.getOrdersByParams(sessionId, params);
    }
}
