package com.thelilyofthenile.backend.controller;

import com.thelilyofthenile.backend.dto.UserLoginDTO;
import com.thelilyofthenile.backend.model.User;
import com.thelilyofthenile.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController{
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        return ResponseEntity.ok(service.register(user));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginDTO dto) {
        boolean success = service.login(dto);
        if(success) {
            return ResponseEntity.ok("Login successful");
        }
        else{
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }
}