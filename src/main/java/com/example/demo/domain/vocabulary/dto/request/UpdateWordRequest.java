package com.example.demo.domain.vocabulary.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateWordRequest(
        @NotBlank(message = "단어(질문)는 필수입니다.")
        String term,

        @NotBlank(message = "뜻(정답)은 필수입니다.")
        String definition
) {
}

