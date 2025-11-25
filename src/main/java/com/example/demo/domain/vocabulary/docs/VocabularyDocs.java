package com.example.demo.domain.vocabulary.docs;

import com.example.demo.domain.vocabulary.dto.request.CreateVocabularyRequest;
import com.example.demo.domain.vocabulary.dto.request.UpdateVocabularyRequest;
import com.example.demo.domain.vocabulary.dto.response.VocabularyResponse;
import com.example.demo.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Vocabulary", description = "단어장 관리 API")
public interface VocabularyDocs {

    @Operation(summary = "단어장 생성", description = "새로운 단어장을 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "단어장 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    ResponseEntity<BaseResponse.Success<VocabularyResponse>> createVocabulary(
            @Valid @RequestBody CreateVocabularyRequest request);

    @Operation(summary = "내 단어장 목록 조회", description = "로그인한 사용자의 모든 단어장을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    ResponseEntity<BaseResponse.Success<List<VocabularyResponse>>> getMyVocabularies();

    @Operation(summary = "단어장 상세 조회", description = "특정 단어장의 상세 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "단어장을 찾을 수 없음"),
            @ApiResponse(responseCode = "403", description = "접근 권한 없음")
    })
    ResponseEntity<BaseResponse.Success<VocabularyResponse>> getVocabulary(
            @PathVariable Long vocabularyId);

    @Operation(summary = "단어장 수정", description = "단어장 정보를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "404", description = "단어장을 찾을 수 없음"),
            @ApiResponse(responseCode = "403", description = "접근 권한 없음")
    })
    ResponseEntity<BaseResponse.Success<VocabularyResponse>> updateVocabulary(
            @PathVariable Long vocabularyId,
            @Valid @RequestBody UpdateVocabularyRequest request);

    @Operation(summary = "단어장 삭제", description = "단어장을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "삭제 성공"),
            @ApiResponse(responseCode = "404", description = "단어장을 찾을 수 없음"),
            @ApiResponse(responseCode = "403", description = "접근 권한 없음")
    })
    ResponseEntity<BaseResponse.Empty> deleteVocabulary(
            @PathVariable Long vocabularyId);
}

