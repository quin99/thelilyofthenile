package com.thelilyofthenile.backend.controller;

import com.thelilyofthenile.backend.dto.CustomerLoginDTO;
import com.thelilyofthenile.backend.dto.CustomerRegisterDTO;
import com.thelilyofthenile.backend.dto.CustomerResponseDTO;
import com.thelilyofthenile.backend.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customers")
@CrossOrigin(origins = "http://localhost:4200")
public class CustomerController {

    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<CustomerResponseDTO> register(@Valid @RequestBody CustomerRegisterDTO dto) {
        return ResponseEntity.ok(service.register(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody CustomerLoginDTO dto) {
        String token = service.login(dto);
        if (token != null) {
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }
}
