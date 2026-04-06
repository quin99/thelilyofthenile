package com.thelilyofthenile.backend.controller;

import com.stripe.exception.StripeException;
import com.thelilyofthenile.backend.dto.PaymentIntentRequestDTO;
import com.thelilyofthenile.backend.dto.PaymentIntentResponseDTO;
import com.thelilyofthenile.backend.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/create-intent")
    public ResponseEntity<PaymentIntentResponseDTO> createPaymentIntent(
            @Valid @RequestBody PaymentIntentRequestDTO request,
            @AuthenticationPrincipal String email) throws StripeException {
        return ResponseEntity.ok(
                paymentService.createPaymentIntent(request.getOrderId(), email)
        );
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> handleWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) throws StripeException {
        paymentService.handleWebhook(payload, sigHeader);
        return ResponseEntity.ok().build();
    }
}
