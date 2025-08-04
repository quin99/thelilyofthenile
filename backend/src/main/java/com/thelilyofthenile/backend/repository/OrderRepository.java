package com.thelilyofthenile.backend.repository;

import com.thelilyofthenile.backend.model.Order;
import com.thelilyofthenile.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.time.LocalDateTime;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}
