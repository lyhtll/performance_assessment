package com.example.demo.domain.vocabulary.controller;

import com.example.demo.domain.vocabulary.dto.request.CreateWordRequest;
import com.example.demo.domain.vocabulary.dto.request.UpdateWordRequest;
import com.example.demo.domain.vocabulary.dto.response.WordResponse;
import com.example.demo.domain.vocabulary.service.WordService;
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
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("WordController 테스트")
class WordControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private WordService wordService;

    @InjectMocks
    private WordController wordController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(wordController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("단어 추가 API 테스트")
    void createWord() throws Exception {
        // given
        Long vocabularyId = 1L;
        CreateWordRequest request = new CreateWordRequest("apple", "사과");
        WordResponse response = new WordResponse(1L, "apple", "사과");

        given(wordService.createWord(eq(vocabularyId), any(CreateWordRequest.class)))
                .willReturn(response);

        // when & then
        mockMvc.perform(post("/vocabularies/{vocabularyId}/words", vocabularyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.message").value("단어가 추가되었습니다."))
                .andExpect(jsonPath("$.data.term").value("apple"))
                .andExpect(jsonPath("$.data.definition").value("사과"));
    }

    @Test
    @DisplayName("단어 추가 실패 - term 누락")
    void createWord_InvalidRequest_MissingTerm() throws Exception {
        // given
        Long vocabularyId = 1L;
        CreateWordRequest request = new CreateWordRequest("", "사과");

        // when & then
        mockMvc.perform(post("/vocabularies/{vocabularyId}/words", vocabularyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("단어 추가 실패 - definition 누락")
    void createWord_InvalidRequest_MissingDefinition() throws Exception {
        // given
        Long vocabularyId = 1L;
        CreateWordRequest request = new CreateWordRequest("apple", "");

        // when & then
        mockMvc.perform(post("/vocabularies/{vocabularyId}/words", vocabularyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("단어 목록 조회 API 테스트")
    void getWords() throws Exception {
        // given
        Long vocabularyId = 1L;
        List<WordResponse> responses = Arrays.asList(
                new WordResponse(1L, "apple", "사과"),
                new WordResponse(2L, "banana", "바나나"),
                new WordResponse(3L, "cherry", "체리")
        );

        given(wordService.getWords(vocabularyId)).willReturn(responses);

        // when & then
        mockMvc.perform(get("/vocabularies/{vocabularyId}/words", vocabularyId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(3))
                .andExpect(jsonPath("$.data[0].term").value("apple"))
                .andExpect(jsonPath("$.data[1].term").value("banana"))
                .andExpect(jsonPath("$.data[2].term").value("cherry"));
    }

    @Test
    @DisplayName("단어 수정 API 테스트")
    void updateWord() throws Exception {
        // given
        Long vocabularyId = 1L;
        Long wordId = 1L;
        UpdateWordRequest request = new UpdateWordRequest("apple", "사과 (수정됨)");
        WordResponse response = new WordResponse(wordId, "apple", "사과 (수정됨)");

        given(wordService.updateWord(eq(vocabularyId), eq(wordId), any(UpdateWordRequest.class)))
                .willReturn(response);

        // when & then
        mockMvc.perform(put("/vocabularies/{vocabularyId}/words/{wordId}", vocabularyId, wordId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("단어가 수정되었습니다."))
                .andExpect(jsonPath("$.data.term").value("apple"))
                .andExpect(jsonPath("$.data.definition").value("사과 (수정됨)"));
    }

    @Test
    @DisplayName("단어 삭제 API 테스트")
    void deleteWord() throws Exception {
        // given
        Long vocabularyId = 1L;
        Long wordId = 1L;
        doNothing().when(wordService).deleteWord(vocabularyId, wordId);

        // when & then
        mockMvc.perform(delete("/vocabularies/{vocabularyId}/words/{wordId}", vocabularyId, wordId))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.status").value(204))
                .andExpect(jsonPath("$.message").value("단어가 삭제되었습니다."));
    }

    @Test
    @DisplayName("여러 단어 생성 및 조회 시나리오 테스트")
    void createMultipleWordsAndRetrieve() throws Exception {
        // given
        Long vocabularyId = 1L;

        // 단어 3개 추가
        CreateWordRequest request1 = new CreateWordRequest("hello", "안녕");
        CreateWordRequest request2 = new CreateWordRequest("world", "세계");
        CreateWordRequest request3 = new CreateWordRequest("test", "테스트");

        given(wordService.createWord(eq(vocabularyId), any(CreateWordRequest.class)))
                .willReturn(new WordResponse(1L, "hello", "안녕"))
                .willReturn(new WordResponse(2L, "world", "세계"))
                .willReturn(new WordResponse(3L, "test", "테스트"));

        // when & then - 첫 번째 단어 추가
        mockMvc.perform(post("/vocabularies/{vocabularyId}/words", vocabularyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.term").value("hello"));

        // 두 번째 단어 추가
        mockMvc.perform(post("/vocabularies/{vocabularyId}/words", vocabularyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request2)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.term").value("world"));

        // 세 번째 단어 추가
        mockMvc.perform(post("/vocabularies/{vocabularyId}/words", vocabularyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request3)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.term").value("test"));
    }
}

