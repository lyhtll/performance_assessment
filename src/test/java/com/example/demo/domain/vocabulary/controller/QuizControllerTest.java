package com.example.demo.domain.vocabulary.controller;

import com.example.demo.domain.vocabulary.dto.request.CheckAnswerRequest;
import com.example.demo.domain.vocabulary.dto.response.CheckAnswerResponse;
import com.example.demo.domain.vocabulary.dto.response.QuizWordResponse;
import com.example.demo.domain.vocabulary.service.QuizServiceInterface;
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

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("QuizController 테스트")
class QuizControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private QuizServiceInterface quizService;

    @InjectMocks
    private QuizController quizController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(quizController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("퀴즈 단어 목록 조회 성공")
    void getQuizWords_Success() throws Exception {
        // Given
        Long vocabularyId = 1L;
        List<QuizWordResponse> response = Arrays.asList(
                new QuizWordResponse(1L, "apple"),
                new QuizWordResponse(2L, "banana")
        );
        given(quizService.getQuizWords(vocabularyId)).willReturn(response);

        // When & Then
        mockMvc.perform(get("/vocabularies/{vocabularyId}/quiz", vocabularyId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("퀴즈를 시작합니다."))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].wordId").value(1))
                .andExpect(jsonPath("$.data[0].term").value("apple"))
                .andExpect(jsonPath("$.data[1].wordId").value(2))
                .andExpect(jsonPath("$.data[1].term").value("banana"))
                .andDo(print());
    }

    @Test
    @DisplayName("정답 체크 성공 - 정답")
    void checkAnswer_Success_Correct() throws Exception {
        // Given
        Long vocabularyId = 1L;
        Long wordId = 1L;
        CheckAnswerRequest request = new CheckAnswerRequest("사과");
        CheckAnswerResponse response = new CheckAnswerResponse(true, "사과");
        given(quizService.checkAnswer(eq(vocabularyId), eq(wordId), any(CheckAnswerRequest.class)))
                .willReturn(response);

        // When & Then
        mockMvc.perform(post("/vocabularies/{vocabularyId}/quiz/{wordId}/check", vocabularyId, wordId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("정답입니다!"))
                .andExpect(jsonPath("$.data.correct").value(true))
                .andExpect(jsonPath("$.data.correctAnswer").value("사과"))
                .andDo(print());
    }

    @Test
    @DisplayName("정답 체크 성공 - 오답")
    void checkAnswer_Success_Incorrect() throws Exception {
        // Given
        Long vocabularyId = 1L;
        Long wordId = 1L;
        CheckAnswerRequest request = new CheckAnswerRequest("바나나");
        CheckAnswerResponse response = new CheckAnswerResponse(false, "사과");
        given(quizService.checkAnswer(eq(vocabularyId), eq(wordId), any(CheckAnswerRequest.class)))
                .willReturn(response);

        // When & Then
        mockMvc.perform(post("/vocabularies/{vocabularyId}/quiz/{wordId}/check", vocabularyId, wordId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("오답입니다."))
                .andExpect(jsonPath("$.data.correct").value(false))
                .andExpect(jsonPath("$.data.correctAnswer").value("사과"))
                .andDo(print());
    }

    @Test
    @DisplayName("정답 체크 실패 - 유효하지 않은 입력")
    void checkAnswer_Fail_InvalidInput() throws Exception {
        // Given
        Long vocabularyId = 1L;
        Long wordId = 1L;
        CheckAnswerRequest request = new CheckAnswerRequest("");

        // When & Then
        mockMvc.perform(post("/vocabularies/{vocabularyId}/quiz/{wordId}/check", vocabularyId, wordId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}