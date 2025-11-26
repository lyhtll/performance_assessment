package com.example.demo.domain.vocabulary.service;

import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.domain.UserRole;
import com.example.demo.domain.vocabulary.domain.Vocabulary;
import com.example.demo.domain.vocabulary.dto.request.CreateVocabularyRequest;
import com.example.demo.domain.vocabulary.dto.request.UpdateVocabularyRequest;
import com.example.demo.domain.vocabulary.dto.response.VocabularyResponse;
import com.example.demo.domain.vocabulary.error.VocabularyError;
import com.example.demo.domain.vocabulary.repository.VocabularyRepository;
import com.example.demo.global.error.CustomException;
import com.example.demo.global.security.util.SecurityUtil;
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
@DisplayName("VocabularyService 테스트")
class VocabularyServiceTest {

    @Mock
    private VocabularyRepository vocabularyRepository;

    @Mock
    private SecurityUtil securityUtil;

    @InjectMocks
    private VocabularyService vocabularyService;

    private User testUser;
    private Vocabulary testVocabulary;

    @BeforeEach
    void setUp() {
        testUser = new User(1L, "testUser", "password123", UserRole.USER);
        testVocabulary = new Vocabulary("테스트 단어장", "테스트 설명", testUser);
        
        // Vocabulary ID 설정
        try {
            var field = Vocabulary.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(testVocabulary, 1L);
        } catch (Exception e) {
            // Ignore for test
        }
    }

    @Test
    @DisplayName("단어장 생성 성공")
    void createVocabulary_Success() {
        // given
        CreateVocabularyRequest request = new CreateVocabularyRequest("새 단어장", "새 설명");

        given(securityUtil.getCurrentUser()).willReturn(testUser);
        given(vocabularyRepository.save(any(Vocabulary.class))).willAnswer(invocation -> {
            Vocabulary saved = invocation.getArgument(0);
            return saved;
        });

        // when
        VocabularyResponse response = vocabularyService.createVocabulary(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.title()).isEqualTo("새 단어장");
        assertThat(response.description()).isEqualTo("새 설명");
        verify(vocabularyRepository, times(1)).save(any(Vocabulary.class));
    }

    @Test
    @DisplayName("내 단어장 목록 조회 성공")
    void getMyVocabularies_Success() {
        // given
        Vocabulary vocabulary1 = new Vocabulary("단어장1", "설명1", testUser);
        Vocabulary vocabulary2 = new Vocabulary("단어장2", "설명2", testUser);

        given(securityUtil.getCurrentUser()).willReturn(testUser);
        given(vocabularyRepository.findByUserId(testUser.getId()))
                .willReturn(Arrays.asList(vocabulary1, vocabulary2));

        // when
        List<VocabularyResponse> responses = vocabularyService.getMyVocabularies();

        // then
        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).title()).isEqualTo("단어장1");
        assertThat(responses.get(1).title()).isEqualTo("단어장2");
        verify(vocabularyRepository, times(1)).findByUserId(testUser.getId());
    }

    @Test
    @DisplayName("단어장 조회 성공")
    void getVocabulary_Success() {
        // given
        Long vocabularyId = 1L;

        given(securityUtil.getCurrentUser()).willReturn(testUser);
        given(vocabularyRepository.findById(vocabularyId))
                .willReturn(Optional.of(testVocabulary));

        // when
        VocabularyResponse response = vocabularyService.getVocabulary(vocabularyId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.title()).isEqualTo("테스트 단어장");
        verify(vocabularyRepository, times(1)).findById(vocabularyId);
    }

    @Test
    @DisplayName("단어장 조회 실패 - 존재하지 않는 단어장")
    void getVocabulary_NotFound() {
        // given
        Long vocabularyId = 999L;

        given(securityUtil.getCurrentUser()).willReturn(testUser);
        given(vocabularyRepository.findById(vocabularyId))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> vocabularyService.getVocabulary(vocabularyId))
                .isInstanceOf(CustomException.class)
                .extracting(e -> ((CustomException) e).getError())
                .isEqualTo(VocabularyError.VOCABULARY_NOT_FOUND);
    }

    @Test
    @DisplayName("단어장 수정 성공")
    void updateVocabulary_Success() {
        // given
        Long vocabularyId = 1L;
        UpdateVocabularyRequest request = new UpdateVocabularyRequest("수정된 제목", "수정된 설명");

        given(securityUtil.getCurrentUser()).willReturn(testUser);
        given(vocabularyRepository.findById(vocabularyId))
                .willReturn(Optional.of(testVocabulary));

        // when
        VocabularyResponse response = vocabularyService.updateVocabulary(vocabularyId, request);

        // then
        assertThat(response.title()).isEqualTo("수정된 제목");
        assertThat(response.description()).isEqualTo("수정된 설명");
        verify(vocabularyRepository, times(1)).findById(vocabularyId);
    }

    @Test
    @DisplayName("단어장 삭제 성공")
    void deleteVocabulary_Success() {
        // given
        Long vocabularyId = 1L;

        given(securityUtil.getCurrentUser()).willReturn(testUser);
        given(vocabularyRepository.findById(vocabularyId))
                .willReturn(Optional.of(testVocabulary));

        // when
        vocabularyService.deleteVocabulary(vocabularyId);

        // then
        verify(vocabularyRepository, times(1)).delete(testVocabulary);
    }
}

