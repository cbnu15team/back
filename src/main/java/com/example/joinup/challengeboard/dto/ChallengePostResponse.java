package com.example.joinup.challengeboard.dto;

import com.example.joinup.challengeboard.entity.ChallengePost;

import java.time.LocalDateTime;

public class ChallengePostResponse {

    private Long postId;          // 게시글 ID
    private String title;         // 제목
    private String challengeType; // 챌린지 종류
    private String photoUrl;      // 이미지 URL
    private String content;       // 게시글 내용
    private String userId;        // 작성자의 로그인 ID
    private LocalDateTime createdAt; // 작성 시간
    private int views;            // 조회수

    // Constructor
    public ChallengePostResponse(ChallengePost post) {
        this.postId = post.getId();
        this.title = post.getTitle();
        this.challengeType = post.getChallengeType(); // 챌린지 종류 매핑
        this.photoUrl = post.getPhotoUrl();
        this.content = post.getContent();
        this.userId = (post.getUser() != null && post.getUser().getId() != null)
                ? post.getUser().getId()
                : "Unknown User";
        this.createdAt = post.getCreatedAt() != null ? post.getCreatedAt() : LocalDateTime.now();
        this.views = post.getViews();
    }

    // Getters
    public Long getPostId() {
        return postId;
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
