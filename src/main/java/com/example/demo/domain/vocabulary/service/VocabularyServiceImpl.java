package com.example.demo.domain.vocabulary.service;

import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.vocabulary.domain.Vocabulary;
import com.example.demo.domain.vocabulary.dto.request.CreateVocabularyRequest;
import com.example.demo.domain.vocabulary.dto.request.UpdateVocabularyRequest;
import com.example.demo.domain.vocabulary.dto.response.VocabularyResponse;
import com.example.demo.domain.vocabulary.error.VocabularyError;
import com.example.demo.domain.vocabulary.repository.VocabularyRepository;
import com.example.demo.global.error.CustomException;
import com.example.demo.global.security.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VocabularyServiceImpl implements VocabularyService {

    private final VocabularyRepository vocabularyRepository;
    private final SecurityUtil securityUtil;

    @Transactional
    public VocabularyResponse createVocabulary(CreateVocabularyRequest request) {
        User currentUser = securityUtil.getCurrentUser();

        Vocabulary vocabulary = new Vocabulary(
                request.title(),
                request.description(),
                currentUser
        );

        Vocabulary saved = vocabularyRepository.save(vocabulary);
        return VocabularyResponse.from(saved);
    }

    public List<VocabularyResponse> getMyVocabularies() {
        User currentUser = securityUtil.getCurrentUser();

        return vocabularyRepository.findByUserId(currentUser.getId())
                .stream()
                .map(VocabularyResponse::from)
                .collect(Collectors.toList());
    }

    public VocabularyResponse getVocabulary(Long vocabularyId) {
        Vocabulary vocabulary = findVocabularyWithPermission(vocabularyId);
        return VocabularyResponse.from(vocabulary);
    }

    @Transactional
    public VocabularyResponse updateVocabulary(Long vocabularyId, UpdateVocabularyRequest request) {
        Vocabulary vocabulary = findVocabularyWithPermission(vocabularyId);

        vocabulary.update(request.title(), request.description());

        return VocabularyResponse.from(vocabulary);
    }

    @Transactional
    public void deleteVocabulary(Long vocabularyId) {
        Vocabulary vocabulary = findVocabularyWithPermission(vocabularyId);
        vocabularyRepository.delete(vocabulary);
    }

    public Vocabulary findVocabularyWithPermission(Long vocabularyId) {
        User currentUser = securityUtil.getCurrentUser();

        Vocabulary vocabulary = vocabularyRepository.findById(vocabularyId)
                .orElseThrow(() -> new CustomException(VocabularyError.VOCABULARY_NOT_FOUND));

        if (!vocabulary.getUser().getId().equals(currentUser.getId())) {
            throw new CustomException(VocabularyError.VOCABULARY_ACCESS_DENIED);
        }

        return vocabulary;
    }
}