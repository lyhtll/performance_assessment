package com.example.demo.domain.vocabulary.controller;

import com.example.demo.domain.vocabulary.docs.VocabularyDocs;
import com.example.demo.domain.vocabulary.dto.request.CreateVocabularyRequest;
import com.example.demo.domain.vocabulary.dto.request.UpdateVocabularyRequest;
import com.example.demo.domain.vocabulary.dto.response.VocabularyResponse;
import com.example.demo.domain.vocabulary.service.VocabularyService;
import com.example.demo.global.common.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vocabularies")
@RequiredArgsConstructor
public class VocabularyController implements VocabularyDocs {

    private final VocabularyService vocabularyService;

    @PostMapping
    @Override
    public ResponseEntity<BaseResponse.Success<VocabularyResponse>> createVocabulary(
            @Valid @RequestBody CreateVocabularyRequest request) {
        VocabularyResponse response = vocabularyService.createVocabulary(request);
        return BaseResponse.of(response, HttpStatus.CREATED.value(), "단어장이 생성되었습니다.");
    }

    @GetMapping
    @Override
    public ResponseEntity<BaseResponse.Success<List<VocabularyResponse>>> getMyVocabularies() {
        List<VocabularyResponse> response = vocabularyService.getMyVocabularies();
        return BaseResponse.of(response, HttpStatus.OK.value(), "단어장 목록 조회 성공");
    }

    @GetMapping("/{vocabularyId}")
    @Override
    public ResponseEntity<BaseResponse.Success<VocabularyResponse>> getVocabulary(
            @PathVariable Long vocabularyId) {
        VocabularyResponse response = vocabularyService.getVocabulary(vocabularyId);
        return BaseResponse.of(response, HttpStatus.OK.value(), "단어장 조회 성공");
    }

    @PutMapping("/{vocabularyId}")
    @Override
    public ResponseEntity<BaseResponse.Success<VocabularyResponse>> updateVocabulary(
            @PathVariable Long vocabularyId,
            @Valid @RequestBody UpdateVocabularyRequest request) {
        VocabularyResponse response = vocabularyService.updateVocabulary(vocabularyId, request);
        return BaseResponse.of(response, HttpStatus.OK.value(), "단어장이 수정되었습니다.");
    }

    @DeleteMapping("/{vocabularyId}")
    @Override
    public ResponseEntity<BaseResponse.Empty> deleteVocabulary(
            @PathVariable Long vocabularyId) {
        vocabularyService.deleteVocabulary(vocabularyId);
        return BaseResponse.success(HttpStatus.NO_CONTENT.value(), "단어장이 삭제되었습니다.");
    }
}

