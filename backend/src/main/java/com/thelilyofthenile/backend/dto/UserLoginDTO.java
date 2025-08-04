package com.thelilyofthenile.backend.dto;

import lombok.Data;

@Data
public class UserLoginDTO {
    private String email;
    private String password;
}
