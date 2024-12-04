package com.example.joinup.challengeboard.dto;

import com.example.joinup.challengeboard.entity.ChallengePage;

import java.time.LocalDateTime;

public class ChallengeBoardResponse {

    private Long pageId;
    private String title;
    private String boardType;
    private String userId; // 작성자의 로그인 ID
    private LocalDateTime createdAt;
    private int views;

    // Constructor
    public ChallengeBoardResponse(ChallengePage page) {
        this.pageId = page.getPageId();
        this.title = page.getTitle();
        this.boardType = page.getBoardType();
        // userId가 null일 경우 "Unknown User"로 설정
        this.userId = (page.getUser() != null && page.getUser().getId() != null)
                ? page.getUser().getId()
                : "Unknown User";
        this.createdAt = page.getCreatedAt() != null ? page.getCreatedAt() : LocalDateTime.now(); // Null-safe 처리
        this.views = page.getViews(); // 기본값이 0으로 보장됨
    }

    // Getters
    public Long getPageId() {
        return pageId;
    }

    public String getTitle() {
        return title;
    }

    public String getBoardType() {
        return boardType;
    }

    public String getUserId() {
        return userId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public int getViews() {
        return views;
    }
}