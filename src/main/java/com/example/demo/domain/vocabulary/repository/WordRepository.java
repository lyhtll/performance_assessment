package com.example.demo.domain.vocabulary.repository;

import com.example.demo.domain.vocabulary.domain.Word;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WordRepository extends JpaRepository<Word, Long> {

    List<Word> findByVocabularyId(Long vocabularyId);
}

