package com.example.joinup.challengeboard.dto;

import com.example.joinup.challengeboard.entity.ChallengePost;
import java.time.LocalDateTime;

public class MyChallengePostDTO {
    private String title;          // 게시글 제목
    private LocalDateTime createdAt; // 작성 시간

    public MyChallengePostDTO(ChallengePost post) {
        this.title = post.getTitle();
        this.createdAt = post.getCreatedAt();
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}