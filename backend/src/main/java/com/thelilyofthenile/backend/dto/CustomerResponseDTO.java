package com.thelilyofthenile.backend.dto;

import lombok.Data;

@Data
public class CustomerResponseDTO {

    private Long id;
    private String username;
    private String email;
}
