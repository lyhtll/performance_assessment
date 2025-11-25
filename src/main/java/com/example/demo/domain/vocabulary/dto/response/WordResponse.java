package com.example.demo.domain.vocabulary.dto.response;

import com.example.demo.domain.vocabulary.domain.Word;

public record WordResponse(
        Long id,
        String term,
        String definition
) {
    public static WordResponse from(Word word) {
        return new WordResponse(
                word.getId(),
                word.getTerm(),
                word.getDefinition()
        );
    }
}

