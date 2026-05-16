package com.mrudula.order_management.repository;

import com.mrudula.order_management.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

        // pagination — Spring auto generates this!
        Page<Product> findAll(Pageable pageable);

        // search by name containing keyword
        Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
