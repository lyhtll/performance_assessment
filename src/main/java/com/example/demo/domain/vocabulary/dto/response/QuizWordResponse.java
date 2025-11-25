package com.example.demo.domain.vocabulary.dto.response;

public record QuizWordResponse(
        Long wordId,
        String term  // 정답은 숨김
) {
}

