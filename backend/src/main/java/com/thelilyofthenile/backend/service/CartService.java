package com.thelilyofthenile.backend.service;

import com.thelilyofthenile.backend.model.CartItem;
import com.thelilyofthenile.backend.model.Customer;
import com.thelilyofthenile.backend.model.Product;
import com.thelilyofthenile.backend.repository.CartItemRepository;
import com.thelilyofthenile.backend.repository.CustomerRepository;
import com.thelilyofthenile.backend.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    private final CartItemRepository cartItemRepo;
    private final CustomerRepository customerRepo;
    private final ProductRepository productRepo;

    public CartService(CartItemRepository cartItemRepo,
                       CustomerRepository customerRepo,
                       ProductRepository productRepo) {
        this.cartItemRepo = cartItemRepo;
        this.customerRepo = customerRepo;
        this.productRepo = productRepo;
    }

    public List<CartItem> getCart(String email) {
        Customer customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found: " + email));
        return cartItemRepo.findByCustomer(customer);
    }

    public CartItem addItem(String email, Long productId, int quantity) {
        Customer customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found: " + email));
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productId));

        CartItem item = cartItemRepo.findByCustomerAndProduct(customer, product)
                .orElse(new CartItem());
        item.setCustomer(customer);
        item.setProduct(product);
        item.setQuantity(item.getId() == null ? quantity : item.getQuantity() + quantity);

        return cartItemRepo.save(item);
    }

    public void removeItem(String email, Long productId) {
        Customer customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found: " + email));
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productId));
        cartItemRepo.findByCustomerAndProduct(customer, product)
                .ifPresent(cartItemRepo::delete);
    }

    public void clearCart(String email) {
        Customer customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found: " + email));
        cartItemRepo.deleteAll(cartItemRepo.findByCustomer(customer));
    }
}
