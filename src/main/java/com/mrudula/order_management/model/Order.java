package com.mrudula.order_management.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // which user placed this order
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // which product ordered
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private double totalPrice; // quantity * product price

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING;

    public enum OrderStatus {
        PENDING, CONFIRMED, DELIVERED, CANCELLED
    }

    private LocalDateTime createdAt = LocalDateTime.now();
}
