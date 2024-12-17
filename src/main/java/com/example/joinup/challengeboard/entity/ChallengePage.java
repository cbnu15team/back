package com.example.joinup.challengeboard.entity;

import com.example.joinup.user.entity.User; // User 엔티티와 연관관계 설정을 위해 import
import jakarta.persistence.*;

import java.time.LocalDateTime;

// 이 클래스는 Challenge Page(도전 과제 페이지) 정보를 나타내는 엔티티입니다.
// 해당 클래스는 데이터베이스 테이블 'challenge_pages'와 매핑됩니다.
@Entity
@Table(name = "challenge_pages")
public class ChallengePage {

    // 'pageId'는 Challenge Page의 고유 ID로, 기본 키 역할을 합니다.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 데이터베이스에서 자동으로 ID 값을 생성
    private Long pageId;

    // 'boardType'은 도전 과제 게시판의 종류를 나타냅니다 (예: 공모전, 대회 등).
    @Column(nullable = false)
    private String boardType;

    // 'title'은 게시물의 제목을 저장합니다.
    @Column(nullable = false)
    private String title;

    // 'content'는 게시물의 내용을 저장합니다.
    // 데이터 크기가 클 수 있어 columnDefinition을 "TEXT"로 설정
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    // 'user'는 게시물을 작성한 사용자를 나타냅니다.
    // User 엔티티와 다대일(Many-to-One) 연관관계를 가지며, user_id를 외래 키로 사용
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false) // user_id로 User 테이블과 매핑
    private User user;

    // 'createdAt'은 게시물 생성 시각을 나타냅니다.
    // 값이 변경되지 않도록 updatable=false로 설정
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // 'views'는 게시물의 조회 수를 저장합니다.
    @Column(nullable = false)
    private int views;

    // 데이터 삽입 전에 생성 시간과 조회 수 기본값을 설정하는 메서드
    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) { // createdAt이 설정되지 않았다면 현재 시간으로 초기화
            this.createdAt = LocalDateTime.now();
        }
        if (this.views == 0) { // 조회 수 기본값을 0으로 설정
            this.views = 0;
        }
    }

    // 게시물의 조회수를 증가시키는 메서드
    public void incrementViews() {
        this.views++;
    }

    // Getters와 Setters
    // 엔티티의 필드에 접근하고 값을 설정하기 위한 메서드

    public Long getPageId() { // 고유 ID 조회
        return pageId;
    }

    public String getBoardType() { // 게시판 종류 조회
        return boardType;
    }

    public void setBoardType(String boardType) { // 게시판 종류 설정
        this.boardType = boardType;
    }

    public String getTitle() { // 제목 조회
        return title;
    }

    public void setTitle(String title) { // 제목 설정
        this.title = title;
    }

    public String getContent() { // 내용 조회
        return content;
    }

    public void setContent(String content) { // 내용 설정
        this.content = content;
    }

    public User getUser() { // 작성자 조회
        return user;
    }

    public void setUser(User user) { // 작성자 설정
        this.user = user;
    }

    public LocalDateTime getCreatedAt() { // 생성 시간 조회
        return createdAt;
    }

    public int getViews() { // 조회 수 조회
        return views;
    }

    public void setViews(int views) { // 조회 수 설정
        this.views = views;
    }
}