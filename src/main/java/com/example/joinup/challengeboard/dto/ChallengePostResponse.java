package com.example.joinup.challengeboard.dto;

import com.example.joinup.challengeboard.entity.ChallengePost;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL) // null 값인 필드를 제외
public class ChallengePostResponse {

    @JsonProperty("id")
    private Long id; // 게시글 ID

    @JsonProperty("title")
    private String title; // 제목

    @JsonProperty("challengeType")
    private String challengeType; // 챌린지 종류

    @JsonProperty("photoUrl")
    private String photoUrl; // 이미지 URL (절대 경로)

    @JsonProperty("content")
    private String content; // 게시글 내용

    @JsonProperty("userName")
    private String userName; // 작성자 이름

    @JsonProperty("createdAt")
    private LocalDateTime createdAt; // 작성 시간

    @JsonProperty("views")
    private int views; // 조회수

    // 기본 생성자 (Jackson 직렬화를 위해 필수)
    public ChallengePostResponse() {
    }

    // 생성자: baseUrl을 사용해 절대 경로 처리
    public ChallengePostResponse(ChallengePost post, String baseUrl) {
        this.id = post.getId();
        this.title = post.getTitle() != null ? post.getTitle() : "제목 없음";

        // challengePage가 null인 경우 기본값 처리
        this.challengeType = (post.getChallengePage() != null && post.getChallengePage().getTitle() != null)
                ? post.getChallengePage().getTitle()
                : "미지정";

        // 절대 경로로 변환된 photoUrl 처리
        this.photoUrl = (post.getPhotoUrl() != null)
                ? baseUrl + post.getPhotoUrl()
                : "이미지 없음";

        this.content = post.getContent() != null ? post.getContent() : "내용이 없습니다.";

        // 사용자 정보가 없는 경우 기본값 설정
        this.userName = (post.getUser() != null && post.getUser().getName() != null)
                ? post.getUser().getName()
                : "Unknown User";

        this.createdAt = post.getCreatedAt() != null ? post.getCreatedAt() : LocalDateTime.now();

        // 조회수는 음수가 되지 않도록 보장
        this.views = Math.max(post.getViews(), 0);
    }

    // 단일 인자 생성자 (baseUrl 없이 호출 시 처리)
    public ChallengePostResponse(ChallengePost post) {
        this(post, ""); // baseUrl을 빈 문자열로 처리
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
