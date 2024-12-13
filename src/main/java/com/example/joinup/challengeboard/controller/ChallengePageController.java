package com.example.joinup.challengeboard.controller;

import com.example.joinup.challengeboard.dto.ChallengePageResponse;
import com.example.joinup.challengeboard.entity.ChallengePage;
import com.example.joinup.challengeboard.service.ChallengePageService;
import com.example.joinup.security.JwtUtil;
import com.example.joinup.user.entity.User;
import com.example.joinup.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/challenge-pages")
public class ChallengePageController {

    private final ChallengePageService challengePageService;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    public ChallengePageController(ChallengePageService challengePageService, JwtUtil jwtUtil, UserService userService) {
        this.challengePageService = challengePageService;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> createChallengePage(
            @RequestBody ChallengePage challengePage,
            @RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body("인증 토큰이 필요합니다.");
            }

            String token = authHeader.substring(7);
            String userId = jwtUtil.extractUsername(token);

            User user = userService.findById(userId);
            challengePage.setUser(user);

            ChallengePageResponse response = challengePageService.createPage(challengePage);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("게시글 작성 중 오류 발생: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<ChallengePageResponse>> getAllPages() {
        List<ChallengePageResponse> pages = challengePageService.getAllPages();
        return ResponseEntity.ok(pages);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPageById(@PathVariable Long id) {
        try {
            ChallengePageResponse page = challengePageService.getPageById(id);
            return ResponseEntity.ok(page);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("페이지 조회 실패: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteChallengePage(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body("인증 토큰이 필요합니다.");
            }

            String token = authHeader.substring(7);
            String userId = jwtUtil.extractUsername(token);

            challengePageService.deletePageIfOwner(id, userId);
            return ResponseEntity.ok("게시글이 삭제되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("게시글 삭제 중 오류 발생: " + e.getMessage());
        }
    }
}
