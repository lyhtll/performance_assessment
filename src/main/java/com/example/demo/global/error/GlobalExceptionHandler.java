package com.example.demo.global.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
        return ErrorResponse.from(ex.getError());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getDefaultMessage())
                .orElse("입력값이 올바르지 않습니다");

        return ResponseEntity
                .status(400)
                .body(new ErrorResponse(400, errorMessage));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAll(Exception ex) {
        String message = ex.getMessage() != null ? ex.getMessage() : "Unknown error";
        return ResponseEntity
                .status(500)
                .body(new ErrorResponse(500, message));
    }
}

