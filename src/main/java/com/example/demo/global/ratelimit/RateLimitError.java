package com.example.demo.global.ratelimit;

import com.example.demo.global.error.CustomError;
import org.springframework.http.HttpStatus;

public enum RateLimitError implements CustomError {
    RATE_LIMIT_EXCEEDED("Rate limit exceeded. Please try again later.", HttpStatus.TOO_MANY_REQUESTS.value());

    private final String message;
    private final int status;

    RateLimitError(String message, int status) {
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

