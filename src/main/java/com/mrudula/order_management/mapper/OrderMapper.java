package com.mrudula.order_management.mapper;

import com.mrudula.order_management.dto.OrderResponse;
import com.mrudula.order_management.model.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public OrderResponse toOrderResponse(Order order) {
        OrderResponse res = new OrderResponse();
        res.setOrderId(order.getId());
        res.setProductName(order.getProduct().getName());
        res.setQuantity(order.getQuantity());
        res.setTotalPrice(order.getTotalPrice());
        res.setStatus(order.getStatus().name());
        res.setCreatedAt(order.getCreatedAt());
        return res;
    }
}
