package com.example.demo.global.common;

import org.springframework.http.ResponseEntity;

public abstract class BaseResponse {
    private final int status;
    private final String message;

    protected BaseResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public static class Success<T> extends BaseResponse {
        private final T data;

        public Success(int status, String message, T data) {
            super(status, message);
            this.data = data;
        }

        public T getData() {
            return data;
        }
    }

    public static class Empty extends BaseResponse {
        public Empty(int status, String message) {
            super(status, message);
        }
    }

    public static <T> ResponseEntity<Success<T>> of(T data, int status, String message) {
        return ResponseEntity
                .status(status)
                .body(new Success<>(status, message, data));
    }

    public static <T> ResponseEntity<Success<T>> of(T data) {
        return of(data, 200, "Success");
    }

    public static ResponseEntity<Empty> success(int status, String message) {
        return ResponseEntity
                .status(status)
                .body(new Empty(status, message));
    }

    public static ResponseEntity<Empty> success() {
        return success(200, "Success");
    }
}

