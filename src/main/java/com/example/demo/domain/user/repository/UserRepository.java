package com.example.demo.domain.user.repository;

import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.error.UserError;
import com.example.demo.global.error.CustomException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByName(String name);
    User findByName(String name);

    default User findByNameOrThrow(String username) {
        User user = findByName(username);
        if (user == null) {
            throw new CustomException(UserError.USER_NOT_FOUND);
        }
        return user;
    }
}

