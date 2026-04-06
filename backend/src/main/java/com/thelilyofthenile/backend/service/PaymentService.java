package com.thelilyofthenile.backend.service;

import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import com.stripe.param.PaymentIntentCreateParams;
import com.thelilyofthenile.backend.dto.PaymentIntentResponseDTO;
import com.thelilyofthenile.backend.model.Order;
import com.thelilyofthenile.backend.repository.OrderRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final OrderRepository orderRepo;

    @Value("${stripe.secret-key:}")
    private String stripeSecretKey;

    @Value("${stripe.webhook-secret:}")
    private String webhookSecret;

    public PaymentService(OrderRepository orderRepo) {
        this.orderRepo = orderRepo;
    }

    @PostConstruct
    public void init() {
        if (stripeSecretKey != null && !stripeSecretKey.isBlank()) {
            Stripe.apiKey = stripeSecretKey;
        }
    }

    public PaymentIntentResponseDTO createPaymentIntent(Long orderId, String email) throws StripeException {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getCustomer().getEmail().equals(email)) {
            throw new RuntimeException("Order does not belong to this customer");
        }

        long amountInSmallestUnit = Math.round(order.getTotalAmount() * 100);

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amountInSmallestUnit)
                .setCurrency("gbp")
                .putMetadata("orderId", orderId.toString())
                .build();

        PaymentIntent intent = PaymentIntent.create(params);
        order.setPaymentIntentId(intent.getId());
        orderRepo.save(order);

        PaymentIntentResponseDTO dto = new PaymentIntentResponseDTO();
        dto.setClientSecret(intent.getClientSecret());
        dto.setPaymentIntentId(intent.getId());
        return dto;
    }

    public void handleWebhook(String payload, String sigHeader) throws StripeException {
        if (webhookSecret == null || webhookSecret.isBlank()) {
            return;
        }

        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
        } catch (SignatureVerificationException e) {
            throw new RuntimeException("Invalid Stripe webhook signature", e);
        }

        if ("payment_intent.succeeded".equals(event.getType())) {
            PaymentIntent intent = (PaymentIntent) event.getDataObjectDeserializer()
                    .getObject()
                    .orElseThrow(() -> new RuntimeException("Could not deserialize PaymentIntent"));

            String orderIdStr = intent.getMetadata().get("orderId");
            if (orderIdStr != null) {
                orderRepo.findById(Long.parseLong(orderIdStr)).ifPresent(order -> {
                    order.setStatus("PAID");
                    orderRepo.save(order);
                });
            }
        }
    }
}
