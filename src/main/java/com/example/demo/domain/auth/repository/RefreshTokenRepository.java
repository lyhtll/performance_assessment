package com.example.demo.domain.auth.repository;

import com.example.demo.domain.auth.domain.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
    default RefreshToken findByUsername(String username) {
        return findById(username).orElse(null);
    }
}

