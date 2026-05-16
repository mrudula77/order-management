package com.mrudula.order_management.service;

import com.mrudula.order_management.model.Product;
import com.mrudula.order_management.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // Add product (Admin only)
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    // Get all products with pagination
    public Page<Product> getAllProducts(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size,
                Sort.by(sortBy).ascending());
        return productRepository.findAll(pageable);
    }

    // Search products by name
    public Page<Product> searchProducts(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository
                .findByNameContainingIgnoreCase(keyword, pageable);
    }

    // Get product by id
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Product not found with id: " + id));
    }

    // Update product (Admin only)
    public Product updateProduct(Long id, Product updatedProduct) {
        Product existing = getProductById(id);
        existing.setName(updatedProduct.getName());
        existing.setDescription(updatedProduct.getDescription());
        existing.setPrice(updatedProduct.getPrice());
        existing.setStock(updatedProduct.getStock());
        return productRepository.save(existing);
    }

    // Delete product (Admin only)
    public String deleteProduct(Long id) {
        getProductById(id); // throws if not found
        productRepository.deleteById(id);
        return "Product deleted successfully";
    }
}
