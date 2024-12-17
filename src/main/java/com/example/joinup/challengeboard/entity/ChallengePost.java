package com.example.joinup.challengeboard.entity;

import com.example.joinup.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "challenge_posts")
@Getter
@Setter
public class ChallengePost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String title; // 제목

    @Column(nullable = false)
    private String photoUrl;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false, columnDefinition = "VARCHAR(255) DEFAULT 'defaultType'")
    private String challengeType; // 챌린지 종류

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private int views;

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.views == 0) {
            this.views = 0;
        }
        if (this.challengeType == null || this.challengeType.isEmpty()) {
            this.challengeType = "defaultType"; // 기본값 설정
        }
    }


    public void incrementViews() {
        this.views++;
    }
}
