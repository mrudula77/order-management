package com.mrudula.order_management.service;

import com.mrudula.order_management.dto.OrderRequest;
import com.mrudula.order_management.dto.OrderResponse;
import com.mrudula.order_management.mapper.OrderMapper;
import com.mrudula.order_management.model.Order;
import com.mrudula.order_management.model.Product;
import com.mrudula.order_management.model.User;
import com.mrudula.order_management.repository.OrderRepository;
import com.mrudula.order_management.repository.ProductRepository;
import com.mrudula.order_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderMapper orderMapper;

    // Place order (User)
    @Transactional
    public OrderResponse placeOrder(OrderRequest request, String email) {

        // Get user from email (extracted from JWT)
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Get product
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Stock validation ✅
        if (product.getStock() < request.getQuantity()) {
            throw new RuntimeException(
                    "Insufficient stock. Available: " + product.getStock());
        }

        // Reduce stock
        product.setStock(product.getStock() - request.getQuantity());
        productRepository.save(product);

        // Create order
        Order order = new Order();
        order.setUser(user);
        order.setProduct(product);
        order.setQuantity(request.getQuantity());
        order.setTotalPrice(product.getPrice() * request.getQuantity());
        order.setStatus(Order.OrderStatus.PENDING);

        Order saved = orderRepository.save(order);
        return orderMapper.toOrderResponse(saved);
    }

    // Get my orders (User)
    public List<OrderResponse> getMyOrders(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return orderRepository.findByUser(user)
                .stream()
                .map(orderMapper::toOrderResponse)
                .collect(Collectors.toList());
    }

    // Get all orders (Admin)
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(orderMapper::toOrderResponse)
                .collect(Collectors.toList());
    }

    // Update order status (Admin)
    public OrderResponse updateStatus(Long orderId,
                                      String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(Order.OrderStatus.valueOf(status.toUpperCase()));
        Order saved = orderRepository.save(order);
        return orderMapper.toOrderResponse(saved);
    }

    // Cancel order (User — only if PENDING)
    @Transactional
    public OrderResponse cancelOrder(Long orderId, String email) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Check if order belongs to this user
        if (!order.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Not authorized to cancel this order");
        }

        // Only PENDING orders can be cancelled
        if (order.getStatus() != Order.OrderStatus.PENDING) {
            throw new RuntimeException(
                    "Only PENDING orders can be cancelled");
        }

        // Restore stock
        Product product = order.getProduct();
        product.setStock(product.getStock() + order.getQuantity());
        productRepository.save(product);

        order.setStatus(Order.OrderStatus.CANCELLED);
        Order saved = orderRepository.save(order);
        return orderMapper.toOrderResponse(saved);
    }
}
