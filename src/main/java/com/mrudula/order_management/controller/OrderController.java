package com.mrudula.order_management.controller;

import com.mrudula.order_management.dto.OrderRequest;
import com.mrudula.order_management.dto.OrderResponse;
import com.mrudula.order_management.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/orders")
@Tag(name = "Orders")
@SecurityRequirement(name = "bearerAuth")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // Helper — get email from JWT via SecurityContext
    private String getCurrentUserEmail() {
        Authentication auth = SecurityContextHolder
                .getContext()
                .getAuthentication();
        return auth.getName(); // returns email
    }

    // ── USER endpoints ──

    @Operation(summary = "Place an order")
    @PostMapping("/place")
    public ResponseEntity<OrderResponse> placeOrder(
            @RequestBody OrderRequest request) {
        return ResponseEntity.ok(
                orderService.placeOrder(request, getCurrentUserEmail()));
    }

    @Operation(summary = "Get my orders")
    @GetMapping("/my")
    public ResponseEntity<List<OrderResponse>> getMyOrders() {
        return ResponseEntity.ok(
                orderService.getMyOrders(getCurrentUserEmail()));
    }

    @Operation(summary = "Cancel my order (only if PENDING)")
    @PutMapping("/{id}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                orderService.cancelOrder(id, getCurrentUserEmail()));
    }

    // ── ADMIN endpoints ──

    @Operation(summary = "Get all orders (Admin only)")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @Operation(summary = "Update order status (Admin only)")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/status/{id}")
    public ResponseEntity<OrderResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        return ResponseEntity.ok(
                orderService.updateStatus(id, status));
    }
}