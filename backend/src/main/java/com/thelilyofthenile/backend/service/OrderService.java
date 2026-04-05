package com.thelilyofthenile.backend.service;

import com.thelilyofthenile.backend.dto.OrderItemDTO;
import com.thelilyofthenile.backend.dto.OrderResponseDTO;
import com.thelilyofthenile.backend.model.CartItem;
import com.thelilyofthenile.backend.model.Customer;
import com.thelilyofthenile.backend.model.Order;
import com.thelilyofthenile.backend.model.OrderItem;
import com.thelilyofthenile.backend.repository.CartItemRepository;
import com.thelilyofthenile.backend.repository.CustomerRepository;
import com.thelilyofthenile.backend.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepo;
    private final CartItemRepository cartRepo;
    private final CustomerRepository customerRepo;

    public OrderService(OrderRepository orderRepo,
                        CartItemRepository cartRepo,
                        CustomerRepository customerRepo) {
        this.orderRepo = orderRepo;
        this.cartRepo = cartRepo;
        this.customerRepo = customerRepo;
    }

    public OrderResponseDTO placeOrder(String email) {
        Customer customer = customerRepo.findByEmail(email).orElseThrow();
        List<CartItem> cartItems = cartRepo.findByCustomer(customer);

        if (cartItems.isEmpty()) throw new RuntimeException("Cart is empty");

        Order order = new Order();
        order.setCustomer(customer);
        order.setStatus("PENDING");

        double total = 0;

        for (CartItem cart : cartItems) {
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(cart.getProduct());
            item.setQuantity(cart.getQuantity());
            item.setPriceAtPurchase(cart.getProduct().getPrice());

            total += item.getQuantity() * item.getPriceAtPurchase();
            order.getItems().add(item);
        }

        order.setTotalAmount(total);
        cartRepo.deleteAll(cartItems);

        return toResponseDTO(orderRepo.save(order));
    }

    public List<OrderResponseDTO> getOrders(String email) {
        Customer customer = customerRepo.findByEmail(email).orElseThrow();
        return orderRepo.findByCustomer(customer).stream().map(this::toResponseDTO).toList();
    }

    private OrderResponseDTO toResponseDTO(Order order) {
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setId(order.getId());
        dto.setStatus(order.getStatus());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setPaymentIntentId(order.getPaymentIntentId());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setItems(order.getItems().stream().map(this::toItemDTO).toList());
        return dto;
    }

    private OrderItemDTO toItemDTO(OrderItem item) {
        OrderItemDTO dto = new OrderItemDTO();
        dto.setProductId(item.getProduct().getId());
        dto.setProductName(item.getProduct().getName());
        dto.setQuantity(item.getQuantity());
        dto.setPriceAtPurchase(item.getPriceAtPurchase());
        return dto;
    }
}
