package com.example.demo.domain.vocabulary.dto.response;

public record CheckAnswerResponse(
        boolean correct,
        String correctAnswer
) {
}

