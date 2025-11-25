package com.example.demo.global.security.jwt.error;

import com.example.demo.global.error.CustomError;
import org.springframework.http.HttpStatus;

public enum JwtError implements CustomError {
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED.value(), "Expired JWT token"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED.value(), "Invalid JWT token"),
    UNSUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED.value(), "Unsupported JWT token"),
    MALFORMED_TOKEN(HttpStatus.UNAUTHORIZED.value(), "Malformed JWT token"),
    INVALID_TOKEN_TYPE(HttpStatus.UNAUTHORIZED.value(), "Invalid token type"),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED.value(), "Invalid refresh token"),
    TOKEN_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to delete token"),
    BLACKLISTED_TOKEN(HttpStatus.UNAUTHORIZED.value(), "Blacklisted token"),
    TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED.value(), "Token not found");

    private final int status;
    private final String message;

    JwtError(int status, String message) {
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

