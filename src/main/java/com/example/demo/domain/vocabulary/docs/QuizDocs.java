package com.example.demo.domain.vocabulary.docs;

import com.example.demo.domain.vocabulary.dto.request.CheckAnswerRequest;
import com.example.demo.domain.vocabulary.dto.response.CheckAnswerResponse;
import com.example.demo.domain.vocabulary.dto.response.QuizWordResponse;
import com.example.demo.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Quiz", description = "단어 학습 퀴즈 API")
public interface QuizDocs {

    @Operation(summary = "랜덤 퀴즈 시작", description = "단어장의 단어들을 랜덤 순서로 가져옵니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "퀴즈 시작 성공"),
            @ApiResponse(responseCode = "400", description = "단어장에 단어가 없음"),
            @ApiResponse(responseCode = "404", description = "단어장을 찾을 수 없음")
    })
    ResponseEntity<BaseResponse.Success<List<QuizWordResponse>>> getRandomQuiz(
            @PathVariable Long vocabularyId);

    @Operation(summary = "답변 확인", description = "사용자의 답변이 정답인지 확인합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "답변 확인 완료"),
            @ApiResponse(responseCode = "404", description = "단어를 찾을 수 없음")
    })
    ResponseEntity<BaseResponse.Success<CheckAnswerResponse>> checkAnswer(
            @PathVariable Long vocabularyId,
            @PathVariable Long wordId,
            @Valid @RequestBody CheckAnswerRequest request);
}

