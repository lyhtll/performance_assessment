package com.example.demo.domain.vocabulary.domain;

import com.example.demo.domain.user.domain.User;
import com.example.demo.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "vocabulary")
public class Vocabulary extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 500)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "vocabulary", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Word> words = new ArrayList<>();

    public Vocabulary(String title, String description, User user) {
        this.title = title;
        this.description = description;
        this.user = user;
    }

    public void update(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public void addWord(Word word) {
        this.words.add(word);
    }
}

