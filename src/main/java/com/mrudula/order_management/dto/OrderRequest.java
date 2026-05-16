package com.mrudula.order_management.dto;

import lombok.Data;

@Data
public class OrderRequest {
    private Long productId;
    private int quantity;
}
