package com.thelilyofthenile.backend.repository;

import com.thelilyofthenile.backend.model.Customer;
import com.thelilyofthenile.backend.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomer(Customer customer);
    java.util.Optional<Order> findByPaymentIntentId(String paymentIntentId);
}
