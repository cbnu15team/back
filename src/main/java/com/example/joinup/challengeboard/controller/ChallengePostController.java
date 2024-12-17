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
import java.util.UUID;

@RestController
@RequestMapping("/api/challenges")
public class ChallengePostController {

    private final ChallengePostService challengePostService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public ChallengePostController(ChallengePostService challengePostService, UserService userService, JwtUtil jwtUtil) {
        this.challengePostService = challengePostService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    // 게시글 작성
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<?> createPost(
            @RequestParam("title") String title,
            @RequestParam("photo") MultipartFile photo,
            @RequestParam("content") String content,
            @RequestParam("challengePageId") Long challengePageId,
            @RequestHeader("Authorization") String authHeader) {

        try {
            User user = validateUser(authHeader);
            String savedFilePath = saveFile(photo);

            ChallengePost post = new ChallengePost();
            post.setTitle(title);
            post.setPhotoUrl(savedFilePath);
            post.setContent(content);
            post.setUser(user);

            ChallengePost createdPost = challengePostService.createPost(challengePageId, post);
            return ResponseEntity.ok(new ChallengePostResponse(createdPost));

        } catch (Exception e) {
            return ResponseEntity.status(500).body("게시글 작성 중 오류 발생: " + e.getMessage());
        }
    }

    // 게시글 수정
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(
            @PathVariable Long id,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "photo", required = false) MultipartFile photo,
            @RequestParam(value = "content", required = false) String content,
            @RequestHeader("Authorization") String authHeader) {

        try {
            User user = validateUser(authHeader);
            ChallengePost updatedPost = challengePostService.getPostById(id);

            if (!updatedPost.getUser().getId().equals(user.getId())) {
                return ResponseEntity.status(403).body("이 게시글을 수정할 권한이 없습니다.");
            }

            if (title != null) updatedPost.setTitle(title);
            if (content != null) updatedPost.setContent(content);
            if (photo != null && !photo.isEmpty()) {
                String savedFilePath = saveFile(photo);
                updatedPost.setPhotoUrl(savedFilePath);
            }

            challengePostService.updatePost(id, updatedPost, user.getId());
            return ResponseEntity.ok(new ChallengePostResponse(updatedPost));

        } catch (Exception e) {
            return ResponseEntity.status(500).body("게시글 수정 중 오류 발생: " + e.getMessage());
        }
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {

        try {
            User user = validateUser(authHeader);
            challengePostService.deletePost(id, user.getId());
            return ResponseEntity.ok("게시글이 성공적으로 삭제되었습니다.");

        } catch (Exception e) {
            return ResponseEntity.status(500).body("게시글 삭제 중 오류 발생: " + e.getMessage());
        }
    }

    // 공통 메서드: 인증 및 사용자 검증
    private User validateUser(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("인증이 필요합니다.");
        }

        String token = authHeader.substring(7);
        String userId = jwtUtil.validateAndExtractUsername(token);

        if (userId == null) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }

        return userService.findById(userId);
    }

    // 파일 저장 메서드
    private String saveFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어 있습니다.");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null && !originalFilename.matches(".*\\.(jpg|jpeg|png|gif)$")) {
            throw new IllegalArgumentException("유효하지 않은 파일 형식입니다. jpg, jpeg, png, gif만 허용됩니다.");
        }

        String fileName = UUID.randomUUID() + "_" + originalFilename;
        Path directoryPath = Paths.get(uploadDir);

        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }

        Path filePath = directoryPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);
        return "/uploads/" + fileName;
    }

    // DTO 클래스: ChallengePostResponse
    private static class ChallengePostResponse {
        private Long id;
        private String title;
        private String content;
        private String photoUrl;
        private String challengeType;
        private String userName;

        public ChallengePostResponse(ChallengePost post) {
            this.id = post.getId();
            this.title = post.getTitle();
            this.content = post.getContent();
            this.photoUrl = post.getPhotoUrl();
            this.challengeType = post.getChallengeType();
            this.userName = post.getUser().getName();
        }

        // Getters
        public Long getId() { return id; }
        public String getTitle() { return title; }
        public String getContent() { return content; }
        public String getPhotoUrl() { return photoUrl; }
        public String getChallengeType() { return challengeType; }
        public String getUserName() { return userName; }
    }
}
