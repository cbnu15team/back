package com.example.joinup.challengeboard.dto;

import com.example.joinup.challengeboard.entity.ChallengePost;

import java.time.LocalDateTime;

public class ChallengePostResponse {

    private Long id;               // 게시글 ID
    private String title;          // 제목
    private String challengeType;  // 챌린지 종류
    private String photoUrl;       // 이미지 URL
    private String content;        // 게시글 내용
    private String userName;       // 작성자 이름
    private LocalDateTime createdAt; // 작성 시간
    private int views;             // 조회수

    // Constructor
    public ChallengePostResponse(ChallengePost post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.challengeType = post.getChallengeType();
        this.photoUrl = post.getPhotoUrl();
        this.content = post.getContent();
        this.userName = (post.getUser() != null && post.getUser().getName() != null)
                ? post.getUser().getName()
                : "Unknown User";
        this.createdAt = post.getCreatedAt() != null ? post.getCreatedAt() : LocalDateTime.now();
        this.views = post.getViews() > 0 ? post.getViews() : 0;
    }


    // Getters
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getChallengeType() {
        return challengeType;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getContent() {
        return content;
    }

    public String getUserName() {
        return userName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public int getViews() {
        return views;
    }
}
