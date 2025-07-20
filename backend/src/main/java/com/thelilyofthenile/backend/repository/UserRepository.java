package com.thelilyofthenile.backend.repository;


import com.thelilyofthenile.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    
}
