package com.example.demo.domain.vocabulary.controller;

import com.example.demo.domain.vocabulary.docs.QuizDocs;
import com.example.demo.domain.vocabulary.dto.request.CheckAnswerRequest;
import com.example.demo.domain.vocabulary.dto.response.CheckAnswerResponse;
import com.example.demo.domain.vocabulary.dto.response.QuizWordResponse;
import com.example.demo.domain.vocabulary.service.QuizServiceInterface;
import com.example.demo.global.common.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vocabularies/{vocabularyId}/quiz")
@RequiredArgsConstructor
public class QuizController implements QuizDocs {

    private final QuizServiceInterface quizService;

    @GetMapping
    @Override
    public ResponseEntity<BaseResponse.Success<List<QuizWordResponse>>> getRandomQuiz(
            @PathVariable Long vocabularyId) {
        List<QuizWordResponse> response = quizService.getQuizWords(vocabularyId);
        return BaseResponse.of(response, HttpStatus.OK.value(), "퀴즈를 시작합니다.");
    }

    @PostMapping("/{wordId}/check")
    @Override
    public ResponseEntity<BaseResponse.Success<CheckAnswerResponse>> checkAnswer(
            @PathVariable Long vocabularyId,
            @PathVariable Long wordId,
            @Valid @RequestBody CheckAnswerRequest request) {
        CheckAnswerResponse response = quizService.checkAnswer(vocabularyId, wordId, request);
        return BaseResponse.of(
                response,
                HttpStatus.OK.value(),
                response.correct() ? "정답입니다!" : "오답입니다."
        );
    }
}