package com.thelilyofthenile.backend.controller;

import com.thelilyofthenile.backend.model.Order;
import com.thelilyofthenile.backend.service.OrderService;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.time.LocalDateTime;


@RestController
@RequestMapping("/api/orders")
@PreAuthorize("hasAuthority('ROLE_USER')")
public class OrderController {

    @Autowired private OrderService orderService;

    @PostMapping("/place")
    public Order placeOrder(@AuthenticationPrincipal UserDetails userDetails) {
        return orderService.placeOrder(userDetails.getUsername());
    }

    @GetMapping
    public List<Order> getOrders(@AuthenticationPrincipal UserDetails userDetails) {
        return orderService.getOrders(userDetails.getUsername());
    }
}
