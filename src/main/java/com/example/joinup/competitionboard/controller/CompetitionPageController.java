// CompetitionPageController.java
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

    // 새로운 CompetitionPage 작성
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
            String userId = jwtUtil.validateAndExtractUsername(token);

            // 사용자 조회 및 설정
            User user = userService.findById(userId);
            competitionPage.setUser(user); // CompetitionPage에 사용자 설정

            // CompetitionPage 생성 및 Response 반환
            CompetitionPageResponse response = competitionPageService.createPage(competitionPage);
            return ResponseEntity.ok(response);

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
            // JWT 토큰 검증 및 사용자 ID 추출
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body("인증 토큰이 필요합니다.");
            }

            String token = authHeader.substring(7); // "Bearer " 제거
            String userId = jwtUtil.validateAndExtractUsername(token); // 사용자 ID 추출

            // 삭제 권한 확인 및 삭제 수행
            competitionPageService.deletePageIfOwner(id, userId);
            return ResponseEntity.ok("게시글이 삭제되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("게시글 삭제 중 오류 발생: " + e.getMessage());
        }
    }
    @PutMapping("/{id}") //수정
    public ResponseEntity<?> updateCompetitionPage(
            @PathVariable Long id,
            @RequestBody CompetitionPage updatedPage,
            @RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body("인증 토큰이 필요합니다.");
            }

            String token = authHeader.substring(7); // "Bearer " 제거
            String userId = jwtUtil.validateAndExtractUsername(token); // 사용자 ID 추출

            // 수정 작업 수행
            CompetitionPageResponse response = competitionPageService.updatePage(id, updatedPage, userId);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("게시글 수정 중 오류 발생: " + e.getMessage());
        }
    }

}
