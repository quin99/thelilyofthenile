package com.thelilyofthenile.backend.dto;

import lombok.Data;

@Data
public class PaymentIntentResponseDTO {
    private String clientSecret;
    private String paymentIntentId;
}
