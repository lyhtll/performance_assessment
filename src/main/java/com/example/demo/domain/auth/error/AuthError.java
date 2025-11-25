package com.example.demo.domain.auth.error;

import com.example.demo.global.error.CustomError;

public enum AuthError implements CustomError {
    INVALID_APP_SECRET("Invalid app secret", 401),
    REFRESH_TOKEN_NOT_FOUND("Refresh token not found", 401),
    INVALID_CREDENTIALS("Invalid credentials", 401),
    INVALID_REFRESH_TOKEN("Invalid refresh token", 401),
    ACCESS_DENIED("Access denied", 403),
    USER_ALREADY_EXISTS("User already exists", 409),
    USER_NOT_FOUND("User not found", 404);

    private final String message;
    private final int status;

    AuthError(String message, int status) {
        this.message = message;
        this.status = status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public int getStatus() {
        return status;
    }
}

