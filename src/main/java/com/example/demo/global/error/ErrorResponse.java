package com.example.demo.global.error;

import org.springframework.http.ResponseEntity;

public record ErrorResponse(int status, String message) {

    public static ResponseEntity<ErrorResponse> from(CustomError error) {
        return ResponseEntity
                .status(error.getStatus())
                .body(new ErrorResponse(error.getStatus(), error.getMessage()));
    }
}

