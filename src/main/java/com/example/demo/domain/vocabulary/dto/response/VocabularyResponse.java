package com.example.demo.domain.vocabulary.dto.response;

import com.example.demo.domain.vocabulary.domain.Vocabulary;

import java.time.LocalDateTime;

public record VocabularyResponse(
        Long id,
        String title,
        String description,
        int wordCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static VocabularyResponse from(Vocabulary vocabulary) {
        return new VocabularyResponse(
                vocabulary.getId(),
                vocabulary.getTitle(),
                vocabulary.getDescription(),
                vocabulary.getWords().size(),
                vocabulary.getCreatedAt(),
                vocabulary.getUpdatedAt()
        );
    }
}

