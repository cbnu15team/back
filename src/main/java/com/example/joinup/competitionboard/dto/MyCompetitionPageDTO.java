package com.example.joinup.competitionboard.dto;

import com.example.joinup.competitionboard.entity.CompetitionPage;
import java.time.LocalDateTime;

public class MyCompetitionPageDTO {
    private String title;          // 대회 페이지 제목
    private LocalDateTime createdAt; // 작성 시간

    public MyCompetitionPageDTO(CompetitionPage page) {
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