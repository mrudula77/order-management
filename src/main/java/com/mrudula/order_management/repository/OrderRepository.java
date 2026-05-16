package com.mrudula.order_management.repository;

import com.mrudula.order_management.model.Order;
import com.mrudula.order_management.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // get all orders of a specific user
    List<Order> findByUser(User user);

    // get all orders of a specific product
    List<Order> findByProductId(Long productId);

    // custom query — total revenue
    @Query("SELECT SUM(o.totalPrice) FROM Order o")
    Double getTotalRevenue();

    // orders by status
    @Query("SELECT o FROM Order o WHERE o.status = :status")
    List<Order> findByStatus(@Param("status") Order.OrderStatus status);
}
