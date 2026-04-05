package com.thelilyofthenile.backend.controller;

import com.thelilyofthenile.backend.model.User;
import com.thelilyofthenile.backend.model.CartItem;
import com.thelilyofthenile.backend.model.Order;
import com.thelilyofthenile.backend.model.OrderItem;
import com.thelilyofthenile.backend.service.CartService;
import com.thelilyofthenile.backend.repository.OrderRepository;
import com.thelilyofthenile.backend.repository.CartItemRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.time.LocalDateTime;



@RestController
@RequestMapping("/api/cart")
@PreAuthorize("hasAuthority('ROLE_USER')")
public class CartController {

    @Autowired private CartService cartService;

    @GetMapping
    public List<CartItem> getCart(@AuthenticationPrincipal UserDetails userDetails) {
        return cartService.getCart(userDetails.getUsername());
    }

    @PostMapping("/add")
    public CartItem addToCart(@AuthenticationPrincipal UserDetails userDetails,
                              @RequestBody Map<String, Object> body) {
        Long productId = Long.valueOf(body.get("productId").toString());
        int quantity = Integer.parseInt(body.get("quantity").toString());
        return cartService.addItem(userDetails.getUsername(), productId, quantity);
    }

    @DeleteMapping("/remove/{productId}")
    public void removeFromCart(@AuthenticationPrincipal UserDetails userDetails,
                               @PathVariable Long productId) {
        cartService.removeItem(userDetails.getUsername(), productId);
    }

    @DeleteMapping("/clear")
    public void clearCart(@AuthenticationPrincipal UserDetails userDetails) {
        cartService.clearCart(userDetails.getUsername());
    }
}
