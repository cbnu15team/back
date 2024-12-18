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
        this.title = (post.getTitle() != null) ? post.getTitle() : "제목 없음";

        // challengePage가 null인 경우 안전하게 처리
        this.challengeType = (post.getChallengePage() != null && post.getChallengePage().getTitle() != null)
                ? post.getChallengePage().getTitle()
                : "미지정";

        this.photoUrl = (post.getPhotoUrl() != null) ? post.getPhotoUrl() : "이미지 없음";
        this.content = (post.getContent() != null) ? post.getContent() : "내용이 없습니다.";

        this.userName = (post.getUser() != null && post.getUser().getName() != null)
                ? post.getUser().getName()
                : "Unknown User";

        this.createdAt = (post.getCreatedAt() != null)
                ? post.getCreatedAt()
                : LocalDateTime.now();

        this.views = Math.max(post.getViews(), 0); // 조회수가 음수인 경우 0으로 설정
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
