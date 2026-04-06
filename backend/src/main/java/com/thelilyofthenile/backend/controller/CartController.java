package com.thelilyofthenile.backend.controller;

import com.thelilyofthenile.backend.model.CartItem;
import com.thelilyofthenile.backend.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<List<CartItem>> getCart(@AuthenticationPrincipal String email) {
        return ResponseEntity.ok(cartService.getCart(email));
    }

    @PostMapping("/add")
    public ResponseEntity<CartItem> addToCart(@AuthenticationPrincipal String email,
                                              @RequestBody Map<String, Object> body) {
        Long productId = Long.valueOf(body.get("productId").toString());
        int quantity = Integer.parseInt(body.get("quantity").toString());
        return ResponseEntity.ok(cartService.addItem(email, productId, quantity));
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<Void> removeFromCart(@AuthenticationPrincipal String email,
                                               @PathVariable Long productId) {
        cartService.removeItem(email, productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart(@AuthenticationPrincipal String email) {
        cartService.clearCart(email);
        return ResponseEntity.noContent().build();
    }
}
