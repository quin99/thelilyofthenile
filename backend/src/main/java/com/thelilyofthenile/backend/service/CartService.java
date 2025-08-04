package com.thelilyofthenile.backend.service;

import com.thelilyofthenile.backend.model.CartItem;
import com.thelilyofthenile.backend.model.User;
import com.thelilyofthenile.backend.model.Product;
import com.thelilyofthenile.backend.repository.CartItemRepository;
import com.thelilyofthenile.backend.repository.ProductRepository;
import com.thelilyofthenile.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.time.LocalDateTime;


@Service
public class CartService {
    @Autowired private CartItemRepository cartItemRepo;
    @Autowired private UserRepository userRepo;
    @Autowired private ProductRepository productRepo;

    public List<CartItem> getCart(String email) {
        User user = userRepo.findByEmail(email).orElseThrow();
        return cartItemRepo.findByUser(user);
    }

    public CartItem addItem(String email, Long productId, int quantity) {
        User user = userRepo.findByEmail(email).orElseThrow();
        Product product = productRepo.findById(productId).orElseThrow();

        CartItem item = cartItemRepo.findByUserAndProduct(user, product)
                .orElse(new CartItem());
        item.setUser(user);
        item.setProduct(product);
        item.setQuantity(item.getId() == null ? quantity : item.getQuantity() + quantity);

        return cartItemRepo.save(item);
    }

    public void removeItem(String email, Long productId) {
        User user = userRepo.findByEmail(email).orElseThrow();
        Product product = productRepo.findById(productId).orElseThrow();
        cartItemRepo.findByUserAndProduct(user, product)
                .ifPresent(cartItemRepo::delete);
    }

    public void clearCart(String email) {
        User user = userRepo.findByEmail(email).orElseThrow();
        cartItemRepo.deleteAll(cartItemRepo.findByUser(user));
    }
}
