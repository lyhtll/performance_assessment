package com.example.demo.domain.vocabulary.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateVocabularyRequest(
        @NotBlank(message = "단어장 제목은 필수입니다.")
        String title,

        String description
) {
}

