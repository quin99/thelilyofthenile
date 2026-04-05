package com.thelilyofthenile.backend.repository;

import com.thelilyofthenile.backend.model.CartItem;
import com.thelilyofthenile.backend.model.Customer;
import com.thelilyofthenile.backend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCustomer(Customer customer);
    Optional<CartItem> findByCustomerAndProduct(Customer customer, Product product);
}
