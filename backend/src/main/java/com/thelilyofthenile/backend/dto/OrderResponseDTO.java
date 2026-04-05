package com.thelilyofthenile.backend.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponseDTO {

    private Long id;
    private String status;
    private Double totalAmount;
    private String paymentIntentId;
    private LocalDateTime createdAt;
    private List<OrderItemDTO> items;
}
