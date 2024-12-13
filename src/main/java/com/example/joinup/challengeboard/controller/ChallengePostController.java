package com.example.joinup.challengeboard.controller;

import com.example.joinup.challengeboard.entity.ChallengePost;
import com.example.joinup.challengeboard.service.ChallengePostService;
import com.example.joinup.user.entity.User;
import com.example.joinup.user.service.UserService;
import com.example.joinup.security.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/challenges")
public class ChallengePostController {
    private final ChallengePostService challengePostService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Value("${file.upload-dir}") // 파일 저장 경로 외부 설정
    private String uploadDir;

    public ChallengePostController(ChallengePostService challengePostService, UserService userService, JwtUtil jwtUtil) {
        this.challengePostService = challengePostService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    // 공통 에러 응답 메서드
    private ResponseEntity<?> createErrorResponse(int status, String message) {
        return ResponseEntity.status(status).body(Map.of("error", message));
    }

    // JWT 토큰 검증 메서드
    private String validateToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("인증 토큰이 필요합니다.");
        }
        return jwtUtil.extractUsername(authHeader.substring(7));
    }

    // 파일 저장 유틸 메서드
    private String saveFile(MultipartFile file) throws IOException {
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, fileName);
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, file.getBytes());
        return filePath.toString();
    }

    // 게시물 생성 (멀티파트 지원)
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<?> createPost(
            @RequestParam("photo") MultipartFile photo,
            @RequestParam("content") String content,
            @RequestHeader("Authorization") String authHeader) {
        try {
            String userId = validateToken(authHeader);
            User user = userService.findById(userId);
            if (user == null) {
                return createErrorResponse(404, "사용자를 찾을 수 없습니다.");
            }

            // 파일 확장자 검증
            String fileExtension = Objects.requireNonNull(photo.getOriginalFilename())
                    .substring(photo.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();
            if (!fileExtension.matches("jpg|jpeg|png|gif")) {
                return createErrorResponse(400, "지원되지 않는 파일 형식입니다. (jpg, jpeg, png, gif만 허용)");
            }

            // 파일 크기 검증
            if (photo.getSize() > 5 * 1024 * 1024) { // 5MB 초과
                return createErrorResponse(400, "파일 크기는 5MB를 초과할 수 없습니다.");
            }

            // 파일 저장
            String savedFilePath = saveFile(photo);

            // 게시물 생성
            ChallengePost post = new ChallengePost();
            post.setPhotoUrl(savedFilePath); // 저장된 파일 경로 설정
            post.setContent(content);
            post.setUser(user);

            ChallengePost createdPost = challengePostService.createPost(post);
            return ResponseEntity.ok(createdPost);
        } catch (IllegalArgumentException e) {
            return createErrorResponse(401, e.getMessage());
        } catch (IOException e) {
            return createErrorResponse(500, "파일 저장 중 오류가 발생했습니다.");
        }
    }

    // 게시물 수정
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(
            @PathVariable Long id,
            @RequestBody ChallengePost post,
            @RequestHeader("Authorization") String authHeader) {
        try {
            String userId = validateToken(authHeader);
            ChallengePost updatedPost = challengePostService.updatePost(id, post, userId);
            return ResponseEntity.ok(updatedPost);
        } catch (IllegalArgumentException e) {
            return createErrorResponse(403, e.getMessage());
        }
    }

    // 게시물 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {
        try {
            String userId = validateToken(authHeader);
            challengePostService.deletePost(id, userId);
            return ResponseEntity.ok(Map.of("message", "게시물이 성공적으로 삭제되었습니다."));
        } catch (IllegalArgumentException e) {
            return createErrorResponse(403, e.getMessage());
        }
    }

    // 게시물 조회
    @GetMapping("/{id}")
    public ResponseEntity<?> getPost(@PathVariable Long id) {
        try {
            ChallengePost post = challengePostService.getPost(id);
            return ResponseEntity.ok(post);
        } catch (IllegalArgumentException e) {
            return createErrorResponse(404, e.getMessage());
        }
    }
}
