package com.mrudula.order_management.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderResponse {
    private Long orderId;
    private String productName;
    private int quantity;
    private double totalPrice;
    private String status;
    private LocalDateTime createdAt;
}
