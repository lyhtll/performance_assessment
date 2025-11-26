package com.example.demo.domain.vocabulary.service;

import com.example.demo.domain.vocabulary.domain.Vocabulary;
import com.example.demo.domain.vocabulary.domain.Word;
import com.example.demo.domain.vocabulary.dto.request.CreateWordRequest;
import com.example.demo.domain.vocabulary.dto.request.UpdateWordRequest;
import com.example.demo.domain.vocabulary.dto.response.WordResponse;
import com.example.demo.domain.vocabulary.error.VocabularyError;
import com.example.demo.domain.vocabulary.repository.WordRepository;
import com.example.demo.global.error.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WordService {

    private final WordRepository wordRepository;
    private final VocabularyService vocabularyService;

    @Transactional
    public WordResponse createWord(Long vocabularyId, CreateWordRequest request) {
        Vocabulary vocabulary = vocabularyService.findVocabularyWithPermission(vocabularyId);

        Word word = Word.builder()
                .term(request.term())
                .definition(request.definition())
                .vocabulary(vocabulary)
                .build();

        Word saved = wordRepository.save(word);
        vocabulary.addWord(saved);

        return WordResponse.from(saved);
    }

    public List<WordResponse> getWords(Long vocabularyId) {
        vocabularyService.findVocabularyWithPermission(vocabularyId);

        return wordRepository.findByVocabularyId(vocabularyId)
                .stream()
                .map(WordResponse::from)
                .toList();
    }

    @Transactional
    public WordResponse updateWord(Long vocabularyId, Long wordId, UpdateWordRequest request) {
        Word word = findWordWithPermission(vocabularyId, wordId);

        word.update(request.term(), request.definition());

        return WordResponse.from(word);
    }

    @Transactional
    public void deleteWord(Long vocabularyId, Long wordId) {
        Word word = findWordWithPermission(vocabularyId, wordId);
        wordRepository.delete(word);
    }

    public Word findWordWithPermission(Long vocabularyId, Long wordId) {
        vocabularyService.findVocabularyWithPermission(vocabularyId);

        Word word = wordRepository.findById(wordId)
                .orElseThrow(() -> new CustomException(VocabularyError.WORD_NOT_FOUND));

        if (!word.getVocabulary().getId().equals(vocabularyId)) {
            throw new CustomException(VocabularyError.WORD_NOT_FOUND);
        }

        return word;
    }
}
