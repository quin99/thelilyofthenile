package com.thelilyofthenile.backend.service;

import com.thelilyofthenile.backend.dto.CustomerLoginDTO;
import com.thelilyofthenile.backend.dto.CustomerRegisterDTO;
import com.thelilyofthenile.backend.dto.CustomerResponseDTO;
import com.thelilyofthenile.backend.model.Customer;
import com.thelilyofthenile.backend.repository.CustomerRepository;
import com.thelilyofthenile.backend.security.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerRepository repo;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final JwtUtil jwtUtil;

    public CustomerService(CustomerRepository repo, JwtUtil jwtUtil) {
        this.repo = repo;
        this.jwtUtil = jwtUtil;
    }

    public CustomerResponseDTO register(CustomerRegisterDTO dto) {
        Customer customer = Customer.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(encoder.encode(dto.getPassword()))
                .role("CUSTOMER")
                .build();
        Customer saved = repo.save(customer);
        return toResponseDTO(saved);
    }

    public String login(CustomerLoginDTO dto) {
        return repo.findByEmail(dto.getEmail())
                .filter(c -> encoder.matches(dto.getPassword(), c.getPassword()))
                .map(c -> jwtUtil.generateToken(c.getEmail(), c.getRole()))
                .orElse(null);
    }

    private CustomerResponseDTO toResponseDTO(Customer customer) {
        CustomerResponseDTO response = new CustomerResponseDTO();
        response.setId(customer.getId());
        response.setUsername(customer.getUsername());
        response.setEmail(customer.getEmail());
        return response;
    }
}
