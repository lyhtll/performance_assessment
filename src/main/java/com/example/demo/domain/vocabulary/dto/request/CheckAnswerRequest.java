package com.example.demo.domain.vocabulary.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CheckAnswerRequest(
        @NotBlank(message = "답변은 필수입니다.")
        String answer
) {
}

