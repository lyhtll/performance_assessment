package com.example.demo.domain.vocabulary.service;

import com.example.demo.domain.vocabulary.domain.Vocabulary;
import com.example.demo.domain.vocabulary.dto.request.CreateVocabularyRequest;
import com.example.demo.domain.vocabulary.dto.request.UpdateVocabularyRequest;
import com.example.demo.domain.vocabulary.dto.response.VocabularyResponse;

import java.util.List;

public interface VocabularyServiceInterface {
    
    VocabularyResponse createVocabulary(CreateVocabularyRequest request);
    
    List<VocabularyResponse> getMyVocabularies();
    
    VocabularyResponse getVocabulary(Long vocabularyId);
    
    VocabularyResponse updateVocabulary(Long vocabularyId, UpdateVocabularyRequest request);
    
    void deleteVocabulary(Long vocabularyId);
    
    Vocabulary findVocabularyWithPermission(Long vocabularyId);
}