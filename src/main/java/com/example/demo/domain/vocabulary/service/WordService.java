package com.example.demo.domain.vocabulary.service;

import com.example.demo.domain.vocabulary.domain.Word;
import com.example.demo.domain.vocabulary.dto.request.CreateWordRequest;
import com.example.demo.domain.vocabulary.dto.request.UpdateWordRequest;
import com.example.demo.domain.vocabulary.dto.response.WordResponse;

import java.util.List;

public interface WordService {
    WordResponse createWord(Long vocabularyId, CreateWordRequest request);
    List<WordResponse> getWords(Long vocabularyId);
    WordResponse updateWord(Long vocabularyId, Long wordId, UpdateWordRequest request);
    void deleteWord(Long vocabularyId, Long wordId);
    Word findWordWithPermission(Long vocabularyId, Long wordId);
}