package com.example.demo.domain.vocabulary.domain;

import com.example.demo.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "word")
public class Word extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String term; // 영어 단어 또는 질문

    @Column(nullable = false)
    private String definition; // 한글 뜻 또는 정답

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vocabulary_id", nullable = false)
    private Vocabulary vocabulary;

    public Word(String term, String definition, Vocabulary vocabulary) {
        this.term = term;
        this.definition = definition;
        this.vocabulary = vocabulary;
    }

    public void update(String term, String definition) {
        this.term = term;
        this.definition = definition;
    }

    public boolean checkAnswer(String answer) {
        return this.definition.trim().equalsIgnoreCase(answer.trim());
    }
}

