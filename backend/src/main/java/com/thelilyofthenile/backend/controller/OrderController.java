package com.thelilyofthenile.backend.controller;

import com.thelilyofthenile.backend.dto.OrderResponseDTO;
import com.thelilyofthenile.backend.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/place")
    public ResponseEntity<OrderResponseDTO> placeOrder(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(orderService.placeOrder(userDetails.getUsername()));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getOrders(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(orderService.getOrders(userDetails.getUsername()));
    }
}
