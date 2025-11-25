package com.example.demo.domain.vocabulary.docs;

import com.example.demo.domain.vocabulary.dto.request.CreateWordRequest;
import com.example.demo.domain.vocabulary.dto.request.UpdateWordRequest;
import com.example.demo.domain.vocabulary.dto.response.WordResponse;
import com.example.demo.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Word", description = "단어 관리 API")
public interface WordDocs {

    @Operation(summary = "단어 추가", description = "단어장에 새로운 단어를 추가합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "단어 추가 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "404", description = "단어장을 찾을 수 없음")
    })
    ResponseEntity<BaseResponse.Success<WordResponse>> createWord(
            @PathVariable Long vocabularyId,
            @Valid @RequestBody CreateWordRequest request);

    @Operation(summary = "단어 목록 조회", description = "단어장의 모든 단어를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "단어장을 찾을 수 없음")
    })
    ResponseEntity<BaseResponse.Success<List<WordResponse>>> getWords(
            @PathVariable Long vocabularyId);

    @Operation(summary = "단어 수정", description = "단어 정보를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "404", description = "단어를 찾을 수 없음")
    })
    ResponseEntity<BaseResponse.Success<WordResponse>> updateWord(
            @PathVariable Long vocabularyId,
            @PathVariable Long wordId,
            @Valid @RequestBody UpdateWordRequest request);

    @Operation(summary = "단어 삭제", description = "단어를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "삭제 성공"),
            @ApiResponse(responseCode = "404", description = "단어를 찾을 수 없음")
    })
    ResponseEntity<BaseResponse.Empty> deleteWord(
            @PathVariable Long vocabularyId,
            @PathVariable Long wordId);
}

