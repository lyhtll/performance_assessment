package com.example.demo.domain.user.error;

import com.example.demo.global.error.CustomError;
import org.springframework.http.HttpStatus;

public enum UserError implements CustomError {
    USERNAME_DUPLICATION(HttpStatus.CONFLICT.value(), "Username is already taken."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "User not found."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED.value(), "Invalid password."),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED.value(), "Invalid username or password.");

    private final int status;
    private final String message;

    UserError(int status, String message) {
        this.status = status;
        this.message = message;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

