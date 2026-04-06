package com.thelilyofthenile.backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentIntentRequestDTO {
    @NotNull
    private Long orderId;
}
