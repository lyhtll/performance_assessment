package com.example.demo.domain.vocabulary.controller;

import com.example.demo.domain.vocabulary.dto.request.CreateVocabularyRequest;
import com.example.demo.domain.vocabulary.dto.request.UpdateVocabularyRequest;
import com.example.demo.domain.vocabulary.dto.response.VocabularyResponse;
import com.example.demo.domain.vocabulary.service.VocabularyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("VocabularyController 테스트")
class VocabularyControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private VocabularyService vocabularyService;

    @InjectMocks
    private VocabularyController vocabularyController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(vocabularyController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("단어장 생성 API 테스트")
    void createVocabulary() throws Exception {
        // given
        CreateVocabularyRequest request = new CreateVocabularyRequest("토익 단어장", "토익 필수 단어 모음");
        VocabularyResponse response = new VocabularyResponse(
                1L, "토익 단어장", "토익 필수 단어 모음", 0,
                LocalDateTime.now(), LocalDateTime.now()
        );

        given(vocabularyService.createVocabulary(any(CreateVocabularyRequest.class)))
                .willReturn(response);

        // when & then
        mockMvc.perform(post("/vocabularies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.message").value("단어장이 생성되었습니다."))
                .andExpect(jsonPath("$.data.title").value("토익 단어장"))
                .andExpect(jsonPath("$.data.description").value("토익 필수 단어 모음"));
    }

    @Test
    @DisplayName("단어장 생성 실패 - 제목 누락")
    void createVocabulary_InvalidRequest() throws Exception {
        // given
        CreateVocabularyRequest request = new CreateVocabularyRequest("", "설명");

        // when & then
        mockMvc.perform(post("/vocabularies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("내 단어장 목록 조회 API 테스트")
    void getMyVocabularies() throws Exception {
        // given
        List<VocabularyResponse> responses = Arrays.asList(
                new VocabularyResponse(1L, "토익", "토익 단어", 50, LocalDateTime.now(), LocalDateTime.now()),
                new VocabularyResponse(2L, "토플", "토플 단어", 30, LocalDateTime.now(), LocalDateTime.now())
        );

        given(vocabularyService.getMyVocabularies()).willReturn(responses);

        // when & then
        mockMvc.perform(get("/vocabularies"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].title").value("토익"))
                .andExpect(jsonPath("$.data[1].title").value("토플"));
    }

    @Test
    @DisplayName("단어장 상세 조회 API 테스트")
    void getVocabulary() throws Exception {
        // given
        Long vocabularyId = 1L;
        VocabularyResponse response = new VocabularyResponse(
                vocabularyId, "토익 단어장", "설명", 100,
                LocalDateTime.now(), LocalDateTime.now()
        );

        given(vocabularyService.getVocabulary(vocabularyId)).willReturn(response);

        // when & then
        mockMvc.perform(get("/vocabularies/{vocabularyId}", vocabularyId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.id").value(vocabularyId))
                .andExpect(jsonPath("$.data.title").value("토익 단어장"))
                .andExpect(jsonPath("$.data.wordCount").value(100));
    }

    @Test
    @DisplayName("단어장 수정 API 테스트")
    void updateVocabulary() throws Exception {
        // given
        Long vocabularyId = 1L;
        UpdateVocabularyRequest request = new UpdateVocabularyRequest("수정된 제목", "수정된 설명");
        VocabularyResponse response = new VocabularyResponse(
                vocabularyId, "수정된 제목", "수정된 설명", 0,
                LocalDateTime.now(), LocalDateTime.now()
        );

        given(vocabularyService.updateVocabulary(eq(vocabularyId), any(UpdateVocabularyRequest.class)))
                .willReturn(response);

        // when & then
        mockMvc.perform(put("/vocabularies/{vocabularyId}", vocabularyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.title").value("수정된 제목"))
                .andExpect(jsonPath("$.data.description").value("수정된 설명"));
    }

    @Test
    @DisplayName("단어장 삭제 API 테스트")
    void deleteVocabulary() throws Exception {
        // given
        Long vocabularyId = 1L;
        doNothing().when(vocabularyService).deleteVocabulary(vocabularyId);

        // when & then
        mockMvc.perform(delete("/vocabularies/{vocabularyId}", vocabularyId))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.status").value(204))
                .andExpect(jsonPath("$.message").value("단어장이 삭제되었습니다."));
    }
}

