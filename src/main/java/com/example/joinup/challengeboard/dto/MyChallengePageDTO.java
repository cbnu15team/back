package com.example.joinup.challengeboard.dto;

import com.example.joinup.challengeboard.entity.ChallengePage;
import java.time.LocalDateTime;

public class MyChallengePageDTO {
    private String title;          // 챌린지 페이지 제목
    private LocalDateTime createdAt; // 작성 시간

    public MyChallengePageDTO(ChallengePage page) {
        this.title = page.getTitle();
        this.createdAt = page.getCreatedAt();
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}