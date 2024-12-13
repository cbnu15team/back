package com.example.joinup.challengeboard.dto;

import com.example.joinup.challengeboard.entity.ChallengePage;

import java.time.LocalDateTime;

public class ChallengePageResponse {

    private Long pageId;
    private String title;
    private String boardType;
    private String userId;
    private LocalDateTime createdAt;
    private int views;
    private String content;

    public ChallengePageResponse(ChallengePage page) {
        this.pageId = page.getPageId();
        this.title = page.getTitle();
        this.boardType = page.getBoardType();
        this.userId = page.getUser().getId();
        this.createdAt = page.getCreatedAt();
        this.views = page.getViews();
        this.content = page.getContent();
    }

    public ChallengePage toEntity() {
        ChallengePage challengePage = new ChallengePage();
        challengePage.setBoardType(this.boardType);
        challengePage.setTitle(this.title);
        challengePage.setContent(this.content);
        // ID와 User는 엔티티에서 직접 관리하는 것이 일반적
        return challengePage;
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
