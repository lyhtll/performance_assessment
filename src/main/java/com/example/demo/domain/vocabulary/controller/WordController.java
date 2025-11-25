package com.example.demo.domain.vocabulary.controller;

import com.example.demo.domain.vocabulary.docs.WordDocs;
import com.example.demo.domain.vocabulary.dto.request.CreateWordRequest;
import com.example.demo.domain.vocabulary.dto.request.UpdateWordRequest;
import com.example.demo.domain.vocabulary.dto.response.WordResponse;
import com.example.demo.domain.vocabulary.service.WordService;
import com.example.demo.global.common.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vocabularies/{vocabularyId}/words")
@RequiredArgsConstructor
public class WordController implements WordDocs {

    private final WordService wordService;

    @PostMapping
    @Override
    public ResponseEntity<BaseResponse.Success<WordResponse>> createWord(
            @PathVariable Long vocabularyId,
            @Valid @RequestBody CreateWordRequest request) {
        WordResponse response = wordService.createWord(vocabularyId, request);
        return BaseResponse.of(response, HttpStatus.CREATED.value(), "단어가 추가되었습니다.");
    }

    @GetMapping
    @Override
    public ResponseEntity<BaseResponse.Success<List<WordResponse>>> getWords(
            @PathVariable Long vocabularyId) {
        List<WordResponse> response = wordService.getWords(vocabularyId);
        return BaseResponse.of(response, HttpStatus.OK.value(), "단어 목록 조회 성공");
    }

    @PutMapping("/{wordId}")
    @Override
    public ResponseEntity<BaseResponse.Success<WordResponse>> updateWord(
            @PathVariable Long vocabularyId,
            @PathVariable Long wordId,
            @Valid @RequestBody UpdateWordRequest request) {
        WordResponse response = wordService.updateWord(vocabularyId, wordId, request);
        return BaseResponse.of(response, HttpStatus.OK.value(), "단어가 수정되었습니다.");
    }

    @DeleteMapping("/{wordId}")
    @Override
    public ResponseEntity<BaseResponse.Empty> deleteWord(
            @PathVariable Long vocabularyId,
            @PathVariable Long wordId) {
        wordService.deleteWord(vocabularyId, wordId);
        return BaseResponse.success(HttpStatus.OK.value(), "단어가 삭제되었습니다.");
    }
}

