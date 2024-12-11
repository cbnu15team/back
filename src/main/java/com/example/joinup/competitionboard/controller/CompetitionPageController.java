package com.example.joinup.competitionboard.controller;

import com.example.joinup.competitionboard.dto.CompetitionPageResponse;
import com.example.joinup.competitionboard.entity.CompetitionPage;
import com.example.joinup.user.entity.User;
import com.example.joinup.competitionboard.service.CompetitionPageService;
import com.example.joinup.user.service.UserService;
import com.example.joinup.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/competition-pages")
public class CompetitionPageController {

    private final CompetitionPageService competitionPageService;
    private final JwtUtil jwtUtil; // JWT 유틸리티 추가
    private final UserService userService; // UserService 추가

    public CompetitionPageController(CompetitionPageService competitionPageService, JwtUtil jwtUtil, UserService userService) {
        this.competitionPageService = competitionPageService;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    // 새로운 CaompetitionPage 작성
    @PostMapping
    public ResponseEntity<?> createCompetitionPage(
            @RequestBody CompetitionPage competitionPage,
            @RequestHeader("Authorization") String authHeader) {
        try {
            // JWT에서 사용자 ID 추출
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body("인증 토큰이 필요합니다.");
            }

            String token = authHeader.substring(7); // "Bearer " 제거
            String userId = jwtUtil.extractUsername(token);

            // 사용자 조회 및 설정
            User user = userService.findById(userId);
            competitionPage.setUser(user); // CompetitionPage에 사용자 설정

            // CompetitionPage 생성
            CompetitionPage savedPage = competitionPageService.createPage(competitionPage);
            return ResponseEntity.ok(savedPage);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("게시글 작성 중 오류 발생: " + e.getMessage());
        }
    }

    // CompetitionPage 조회
    @GetMapping("/{id}")
    public ResponseEntity<?> getCompetitionPage(@PathVariable Long id) {
        try {
            CompetitionPageResponse pageResponse = competitionPageService.getPageById(id); // 반환 타입 수정
            return ResponseEntity.ok(pageResponse);
        } catch (Exception e) {
            return ResponseEntity.status(404).body("게시글을 찾을 수 없습니다: " + e.getMessage());
        }
    }

    // CompetitionPage 전체 조회
    @GetMapping
    public ResponseEntity<?> getAllCompetitionPages() {
        try {
            return ResponseEntity.ok(competitionPageService.getAllPages());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("게시글 조회 중 오류 발생: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCompetitionPage(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {
        try {
            // JWT에서 사용자 ID 추출
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body("인증 토큰이 필요합니다.");
            }

            String token = authHeader.substring(7); // "Bearer " 제거
            String userId = jwtUtil.extractUsername(token); // 토큰에서 사용자 ID 추출

            // 게시글 조회
            CompetitionPage competitionPage = competitionPageService.getPageById(id).toEntity();

            // 작성자와 토큰 소유자가 같은지 확인
            if (!competitionPage.getUser().getId().equals(userId)) {
                return ResponseEntity.status(403).body("작성자만 게시글을 삭제할 수 있습니다.");
            }

            // 게시글 삭제
            competitionPageService.deletePage(id);
            return ResponseEntity.ok("게시글이 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(404).body("게시글 삭제 실패: " + e.getMessage());
        }
    }
}
