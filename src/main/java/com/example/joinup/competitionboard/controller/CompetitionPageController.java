package com.example.joinup.competitionboard.controller;

import com.example.joinup.competitionboard.dto.CompetitionPageResponse;
import com.example.joinup.competitionboard.service.CompetitionPageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/competition-pages")
public class CompetitionPageController {

    private final CompetitionPageService competitionPageService;

    public CompetitionPageController(CompetitionPageService competitionPageService) {
        this.competitionPageService = competitionPageService;
    }

    @GetMapping
    public ResponseEntity<List<CompetitionPageResponse>> getAllPages() {
        List<CompetitionPageResponse> pages = competitionPageService.getAllPages();
        return ResponseEntity.ok(pages);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPageById(@PathVariable Long id) {
        try {
            CompetitionPageResponse page = competitionPageService.getPageById(id);
            return ResponseEntity.ok(page);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("페이지 조회 실패: " + e.getMessage());
        }
    }
}