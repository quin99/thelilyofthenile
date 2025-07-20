package com.thelilyofthenile.backend.service;

import com.thelilyofthenile.backend.model.User;
import com.thelilyofthenile.backend.dto.UserLoginDTO;
import com.thelilyofthenile.backend.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository repo;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public User register(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return repo.save(user);
    }

    public boolean login(UserLoginDTO dto) {
        return repo.findByEmail(dto.getEmail())
                .map(user -> encoder.matches(dto.getPassword(), user.getPassword()))
                .orElse(false);
    }
}
