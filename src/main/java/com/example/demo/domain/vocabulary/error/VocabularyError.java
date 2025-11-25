package com.example.demo.domain.vocabulary.error;

import com.example.demo.global.error.CustomError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VocabularyError implements CustomError {
    VOCABULARY_NOT_FOUND(404, "단어장을 찾을 수 없습니다."),
    VOCABULARY_ACCESS_DENIED(403, "단어장에 접근할 권한이 없습니다."),
    WORD_NOT_FOUND(404, "단어를 찾을 수 없습니다."),
    EMPTY_VOCABULARY(400, "단어장에 단어가 없습니다.");

    private final int status;
    private final String message;
}

