package com.example.demo.domain.vocabulary.service;

import com.example.demo.domain.vocabulary.domain.Vocabulary;
import com.example.demo.domain.vocabulary.domain.Word;
import com.example.demo.domain.vocabulary.dto.request.CheckAnswerRequest;
import com.example.demo.domain.vocabulary.dto.response.CheckAnswerResponse;
import com.example.demo.domain.vocabulary.dto.response.QuizWordResponse;
import com.example.demo.domain.vocabulary.error.VocabularyError;
import com.example.demo.domain.vocabulary.repository.WordRepository;
import com.example.demo.global.error.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("QuizService 테스트")
class QuizServiceTest {

    @Mock
    private WordRepository wordRepository;

    @Mock
    private VocabularyService vocabularyService;

    @InjectMocks
    private QuizService quizService;

    private Vocabulary testVocabulary;
    private Word testWord1;
    private Word testWord2;

    @BeforeEach
    void setUp() {
        testVocabulary = new Vocabulary("테스트 단어장", "설명", null);
        // Vocabulary ID 설정
        try {
            var field = Vocabulary.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(testVocabulary, 1L);
        } catch (Exception e) {
            // Ignore for test
        }
        
        testWord1 = Word.builder()
                .term("apple")
                .definition("사과")
                .vocabulary(testVocabulary)
                .build();
        testWord2 = Word.builder()
                .term("banana")
                .definition("바나나")
                .vocabulary(testVocabulary)
                .build();
        
        // Word IDs 설정
        try {
            var field = Word.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(testWord1, 1L);
            field.set(testWord2, 2L);
        } catch (Exception e) {
            // Ignore for test
        }
    }

    @Test
    @DisplayName("퀴즈 단어 목록 조회 성공")
    void getQuizWords_Success() {
        // Given
        Long vocabularyId = 1L;
        List<Word> words = Arrays.asList(testWord1, testWord2);
        given(wordRepository.findByVocabularyId(vocabularyId)).willReturn(words);

        // When
        List<QuizWordResponse> result = quizService.getQuizWords(vocabularyId);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).term()).isIn("apple", "banana");
        assertThat(result.get(1).term()).isIn("apple", "banana");
        verify(vocabularyService).findVocabularyWithPermission(vocabularyId);
        verify(wordRepository).findByVocabularyId(vocabularyId);
    }

    @Test
    @DisplayName("퀴즈 단어 목록 조회 실패 - 빈 단어장")
    void getQuizWords_Fail_EmptyVocabulary() {
        // Given
        Long vocabularyId = 1L;
        given(wordRepository.findByVocabularyId(vocabularyId)).willReturn(Collections.emptyList());

        // When & Then
        assertThatThrownBy(() -> quizService.getQuizWords(vocabularyId))
                .isInstanceOf(CustomException.class)
                .extracting(e -> ((CustomException) e).getError())
                .isEqualTo(VocabularyError.EMPTY_VOCABULARY);

        verify(vocabularyService).findVocabularyWithPermission(vocabularyId);
        verify(wordRepository).findByVocabularyId(vocabularyId);
    }

    @Test
    @DisplayName("정답 체크 성공 - 정답")
    void checkAnswer_Success_Correct() {
        // Given
        Long vocabularyId = 1L;
        Long wordId = 1L;
        CheckAnswerRequest request = new CheckAnswerRequest("사과");
        
        
        given(wordRepository.findById(wordId)).willReturn(Optional.of(testWord1));

        // When
        CheckAnswerResponse result = quizService.checkAnswer(vocabularyId, wordId, request);

        // Then
        assertThat(result.correct()).isTrue();
        assertThat(result.correctAnswer()).isEqualTo("사과");
        verify(vocabularyService).findVocabularyWithPermission(vocabularyId);
        verify(wordRepository).findById(wordId);
    }

    @Test
    @DisplayName("정답 체크 성공 - 오답")
    void checkAnswer_Success_Incorrect() {
        // Given
        Long vocabularyId = 1L;
        Long wordId = 1L;
        CheckAnswerRequest request = new CheckAnswerRequest("바나나");
        
        
        given(wordRepository.findById(wordId)).willReturn(Optional.of(testWord1));

        // When
        CheckAnswerResponse result = quizService.checkAnswer(vocabularyId, wordId, request);

        // Then
        assertThat(result.correct()).isFalse();
        assertThat(result.correctAnswer()).isEqualTo("사과");
        verify(vocabularyService).findVocabularyWithPermission(vocabularyId);
        verify(wordRepository).findById(wordId);
    }

    @Test
    @DisplayName("정답 체크 실패 - 존재하지 않는 단어")
    void checkAnswer_Fail_WordNotFound() {
        // Given
        Long vocabularyId = 1L;
        Long wordId = 999L;
        CheckAnswerRequest request = new CheckAnswerRequest("사과");
        given(wordRepository.findById(wordId)).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> quizService.checkAnswer(vocabularyId, wordId, request))
                .isInstanceOf(CustomException.class)
                .extracting(e -> ((CustomException) e).getError())
                .isEqualTo(VocabularyError.WORD_NOT_FOUND);

        verify(vocabularyService).findVocabularyWithPermission(vocabularyId);
        verify(wordRepository).findById(wordId);
    }
}