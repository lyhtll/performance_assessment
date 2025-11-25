package com.example.demo.domain.vocabulary.repository;

import com.example.demo.domain.vocabulary.domain.Vocabulary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VocabularyRepository extends JpaRepository<Vocabulary, Long> {

    List<Vocabulary> findByUserId(Long userId);

    @Query("SELECT v FROM Vocabulary v WHERE v.id = :id AND v.user.id = :userId")
    Optional<Vocabulary> findByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);
}

