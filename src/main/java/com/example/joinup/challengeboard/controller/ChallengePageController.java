package com.example.joinup.challengeboard.controller;

import com.example.joinup.challengeboard.dto.ChallengePageResponse;
import com.example.joinup.challengeboard.service.ChallengePageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/challenge-pages")
public class ChallengePageController {

    private final ChallengePageService challengePageService;

    public ChallengePageController(ChallengePageService challengePageService) {
        this.challengePageService = challengePageService;
    }

    // 모든 페이지 조회
    @GetMapping
    public ResponseEntity<List<ChallengePageResponse>> getAllPages() {
        // 반환 타입을 ChallengePageResponse로 변경
        List<ChallengePageResponse> pages = challengePageService.getAllPages();
        return ResponseEntity.ok(pages);
    }

    // 특정 페이지 조회 (조회수 증가 포함)
    @GetMapping("/{id}")
    public ResponseEntity<?> getPageById(@PathVariable Long id) {
        try {
            ChallengePageResponse page = challengePageService.getPageById(id);
            return ResponseEntity.ok(page);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("페이지 조회 실패: " + e.getMessage());
        }
    }
}