package com.example.demo.domain.user.repository;

import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.error.UserError;
import com.example.demo.global.error.CustomException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByName(String name);

    Optional<User> findByName(String name);

    default User findByNameOrThrow(String username) {
        return findByName(username)
                .orElseThrow(() -> new CustomException(UserError.USER_NOT_FOUND));
    }
}
