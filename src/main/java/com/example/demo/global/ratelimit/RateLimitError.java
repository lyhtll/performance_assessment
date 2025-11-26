package com.example.demo.global.ratelimit;

import com.example.demo.global.error.CustomError;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum RateLimitError implements CustomError {
    RATE_LIMIT_EXCEEDED("Rate limit exceeded. Please try again later.", HttpStatus.TOO_MANY_REQUESTS.value());

    private final String message;
    private final int status;

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public int getStatus() {
        return status;
    }
}

