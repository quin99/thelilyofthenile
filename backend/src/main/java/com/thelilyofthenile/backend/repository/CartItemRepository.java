package com.thelilyofthenile.backend.repository;

import com.thelilyofthenile.backend.model.Product;
import com.thelilyofthenile.backend.model.User;
import com.thelilyofthenile.backend.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Map;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUser(User user);
    Optional<CartItem> findByUserAndProduct(User user, Product product);
}
