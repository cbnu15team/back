package com.example.joinup.competitionboard.dto;

import com.example.joinup.competitionboard.entity.CompetitionPage;

import java.time.LocalDateTime;

public class CompetitionPageResponse {

    private Long pageId;
    private String title;
    private String boardType;
    private String userId;
    private LocalDateTime createdAt;
    private int views;
    private String content;

    public CompetitionPageResponse(CompetitionPage page) {
        this.pageId = page.getPageId();
        this.title = page.getTitle();
        this.boardType = page.getBoardType();
        this.userId = page.getUser().getId(); // 작성자의 로그인 ID
        this.createdAt = page.getCreatedAt();
        this.views = page.getViews();
        this.content = page.getContent();
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

    public String getContent() {
        return content;
    }
}