package com.example.demo.domain.vocabulary.service;

import com.example.demo.domain.vocabulary.domain.Word;
import com.example.demo.domain.vocabulary.dto.request.CheckAnswerRequest;
import com.example.demo.domain.vocabulary.dto.response.CheckAnswerResponse;
import com.example.demo.domain.vocabulary.dto.response.QuizWordResponse;
import com.example.demo.domain.vocabulary.error.VocabularyError;
import com.example.demo.domain.vocabulary.repository.WordRepository;
import com.example.demo.global.error.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuizService {

    private final WordRepository wordRepository;
    private final VocabularyService vocabularyService;

    /**
     * 단어장에서 랜덤으로 섞인 단어 목록을 가져옵니다.
     */
    public List<QuizWordResponse> getRandomQuiz(Long vocabularyId) {
        vocabularyService.findVocabularyWithPermission(vocabularyId);

        List<Word> words = wordRepository.findByVocabularyId(vocabularyId);

        if (words.isEmpty()) {
            throw new CustomException(VocabularyError.EMPTY_VOCABULARY);
        }

        // 랜덤 섞기
        Collections.shuffle(words);

        return words.stream()
                .map(word -> new QuizWordResponse(word.getId(), word.getTerm()))
                .collect(Collectors.toList());
    }

    /**
     * 특정 단어에 대한 답변을 체크합니다.
     */
    public CheckAnswerResponse checkAnswer(Long vocabularyId, Long wordId, CheckAnswerRequest request) {
        vocabularyService.findVocabularyWithPermission(vocabularyId);

        Word word = wordRepository.findById(wordId)
                .orElseThrow(() -> new CustomException(VocabularyError.WORD_NOT_FOUND));

        if (!word.getVocabulary().getId().equals(vocabularyId)) {
            throw new CustomException(VocabularyError.WORD_NOT_FOUND);
        }

        boolean correct = word.checkAnswer(request.answer());

        return new CheckAnswerResponse(correct, word.getDefinition());
    }
}

