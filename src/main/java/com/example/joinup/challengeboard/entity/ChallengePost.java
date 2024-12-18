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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "challenge_page_id", nullable = true)
    private ChallengePage challengePage;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String photoUrl;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private int views;

    @Column(nullable = false, columnDefinition = "VARCHAR(255) DEFAULT 'defaultType'")
    private String challengeType;

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.views == 0) {
            this.views = 0;
        }
        if (this.challengeType == null || this.challengeType.isEmpty()) {
            this.challengeType = "defaultType";
        }
    }

    public String getChallengeType() {
        // NPE 방지
        return (this.challengePage != null && this.challengePage.getTitle() != null)
                ? this.challengePage.getTitle()
                : "defaultType";
    }

    public void incrementViews() {
        this.views = Math.max(0, this.views + 1); // 조회수는 음수로 가지 않도록 보장
    }
}
