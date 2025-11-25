package com.example.demo.domain.vocabulary.service;

import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.domain.UserRole;
import com.example.demo.domain.vocabulary.domain.Vocabulary;
import com.example.demo.domain.vocabulary.domain.Word;
import com.example.demo.domain.vocabulary.dto.request.CreateWordRequest;
import com.example.demo.domain.vocabulary.dto.request.UpdateWordRequest;
import com.example.demo.domain.vocabulary.dto.response.WordResponse;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("WordService 테스트")
class WordServiceTest {

    @Mock
    private WordRepository wordRepository;

    @Mock
    private VocabularyService vocabularyService;

    @InjectMocks
    private WordService wordService;

    private User testUser;
    private Vocabulary testVocabulary;
    private Word testWord;

    @BeforeEach
    void setUp() {
        testUser = new User(1L, "testUser", "password123", UserRole.USER);
        testVocabulary = new Vocabulary("테스트 단어장", "설명", testUser);
        // Reflection을 사용하여 ID 설정
        try {
            var field = Vocabulary.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(testVocabulary, 1L);
        } catch (Exception e) {
            // ignore
        }

        testWord = new Word("apple", "사과", testVocabulary);
        try {
            var field = Word.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(testWord, 1L);
        } catch (Exception e) {
            // ignore
        }
    }

    @Test
    @DisplayName("단어 추가 성공")
    void createWord_Success() {
        // given
        Long vocabularyId = 1L;
        CreateWordRequest request = new CreateWordRequest("book", "책");

        given(vocabularyService.findVocabularyWithPermission(vocabularyId))
                .willReturn(testVocabulary);
        given(wordRepository.save(any(Word.class))).willAnswer(invocation -> invocation.getArgument(0));

        // when
        WordResponse response = wordService.createWord(vocabularyId, request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.term()).isEqualTo("book");
        assertThat(response.definition()).isEqualTo("책");
        verify(wordRepository, times(1)).save(any(Word.class));
    }

    @Test
    @DisplayName("단어 목록 조회 성공")
    void getWords_Success() {
        // given
        Long vocabularyId = 1L;
        Word word1 = new Word("apple", "사과", testVocabulary);
        Word word2 = new Word("banana", "바나나", testVocabulary);

        given(vocabularyService.findVocabularyWithPermission(vocabularyId))
                .willReturn(testVocabulary);
        given(wordRepository.findByVocabularyId(vocabularyId))
                .willReturn(Arrays.asList(word1, word2));

        // when
        List<WordResponse> responses = wordService.getWords(vocabularyId);

        // then
        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).term()).isEqualTo("apple");
        assertThat(responses.get(1).term()).isEqualTo("banana");
        verify(wordRepository, times(1)).findByVocabularyId(vocabularyId);
    }

    @Test
    @DisplayName("단어 수정 성공")
    void updateWord_Success() {
        // given
        Long vocabularyId = 1L;
        Long wordId = 1L;
        UpdateWordRequest request = new UpdateWordRequest("apple", "사과 (수정됨)");

        given(vocabularyService.findVocabularyWithPermission(vocabularyId))
                .willReturn(testVocabulary);
        given(wordRepository.findById(wordId)).willReturn(Optional.of(testWord));

        // when
        WordResponse response = wordService.updateWord(vocabularyId, wordId, request);

        // then
        assertThat(response.term()).isEqualTo("apple");
        assertThat(response.definition()).isEqualTo("사과 (수정됨)");
    }

    @Test
    @DisplayName("단어 수정 실패 - 존재하지 않는 단어")
    void updateWord_NotFound() {
        // given
        Long vocabularyId = 1L;
        Long wordId = 999L;
        UpdateWordRequest request = new UpdateWordRequest("apple", "사과");

        given(vocabularyService.findVocabularyWithPermission(vocabularyId))
                .willReturn(testVocabulary);
        given(wordRepository.findById(wordId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> wordService.updateWord(vocabularyId, wordId, request))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("error", VocabularyError.WORD_NOT_FOUND);
    }

    @Test
    @DisplayName("단어 삭제 성공")
    void deleteWord_Success() {
        // given
        Long vocabularyId = 1L;
        Long wordId = 1L;

        given(vocabularyService.findVocabularyWithPermission(vocabularyId))
                .willReturn(testVocabulary);
        given(wordRepository.findById(wordId)).willReturn(Optional.of(testWord));

        // when
        wordService.deleteWord(vocabularyId, wordId);

        // then
        verify(wordRepository, times(1)).delete(testWord);
    }
}

