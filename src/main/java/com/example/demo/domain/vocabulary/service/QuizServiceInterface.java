package com.example.demo.domain.vocabulary.service;

import com.example.demo.domain.vocabulary.dto.request.CheckAnswerRequest;
import com.example.demo.domain.vocabulary.dto.response.CheckAnswerResponse;
import com.example.demo.domain.vocabulary.dto.response.QuizWordResponse;

import java.util.List;

public interface QuizServiceInterface {
    
    List<QuizWordResponse> getQuizWords(Long vocabularyId);
    
    CheckAnswerResponse checkAnswer(Long vocabularyId, Long wordId, CheckAnswerRequest request);
}
