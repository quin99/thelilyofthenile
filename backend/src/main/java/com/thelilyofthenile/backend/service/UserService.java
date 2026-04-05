package com.thelilyofthenile.backend.service;

import com.thelilyofthenile.backend.model.User;
import com.thelilyofthenile.backend.dto.UserLoginDTO;
import com.thelilyofthenile.backend.repository.UserRepository;
import com.thelilyofthenile.backend.security.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository repo;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final JwtUtil jwtUtil;


    public UserService(UserRepository repo, JwtUtil jwtUtil) {
        this.repo = repo;
        this.jwtUtil = jwtUtil;
    }

    public User register(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return repo.save(user);
    }

    public String login(UserLoginDTO dto) {
        return repo.findByEmail(dto.getEmail())
                .filter(user -> encoder.matches(dto.getPassword(), user.getPassword()))
                .map(user -> jwtUtil.generateToken(user.getEmail()))
                .orElse(null);
    }
}
