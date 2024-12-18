package com.example.joinup.challengeboard.controller;

import com.example.joinup.challengeboard.dto.ChallengePostResponse;

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

    @Value("${file.upload-dir}") // 파일 업로드 디렉토리를 설정 파일에서 읽음
    private String uploadDir;

    @Value("${server.base-url}") // 서버 기본 URL을 설정 파일에서 읽음
    private String baseUrl;

    public ChallengePostController(ChallengePostService challengePostService, UserService userService, JwtUtil jwtUtil) {
        this.challengePostService = challengePostService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    // 게시글 작성
    @PostMapping(consumes = {"multipart/form-data"}, produces = "application/json")
    public ResponseEntity<?> createPost(
            @RequestParam("title") String title,
            @RequestParam("photo") MultipartFile photo,
            @RequestParam("content") String content,
            @RequestParam("challengePageId") Long challengePageId,
            @RequestHeader("Authorization") String authHeader) {

        try {
            User user = validateUser(authHeader); // 사용자 인증 및 검증
            String savedFilePath = saveFile(photo); // 파일 저장 후 경로 반환

            ChallengePost post = new ChallengePost();
            post.setTitle(title);
            post.setPhotoUrl(savedFilePath); // 저장된 파일의 경로 설정
            post.setContent(content);
            post.setUser(user);

            ChallengePost createdPost = challengePostService.createPost(challengePageId, post);
            return ResponseEntity.ok(new ChallengePostResponse(createdPost, baseUrl)); // 응답 DTO에 절대 경로 포함

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage()); // 잘못된 요청 처리
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
            User user = validateUser(authHeader); // 사용자 인증 및 검증
            ChallengePost updatedPost = challengePostService.getPostById(id);

            if (!updatedPost.getUser().getId().equals(user.getId())) {
                return ResponseEntity.status(403).body("이 게시글을 수정할 권한이 없습니다."); // 권한 확인
            }

            if (title != null) updatedPost.setTitle(title); // 제목 수정
            if (content != null) updatedPost.setContent(content); // 내용 수정
            if (photo != null && !photo.isEmpty()) {
                String savedFilePath = saveFile(photo); // 새 파일 저장
                updatedPost.setPhotoUrl(savedFilePath); // 파일 경로 업데이트
            }

            challengePostService.updatePost(id, updatedPost, user.getId());
            return ResponseEntity.ok(new ChallengePostResponse(updatedPost, baseUrl)); // 응답 DTO 반환

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
            User user = validateUser(authHeader); // 사용자 인증 및 검증
            challengePostService.deletePost(id, user.getId()); // 게시글 삭제
            return ResponseEntity.ok("게시글이 성공적으로 삭제되었습니다.");

        } catch (Exception e) {
            return ResponseEntity.status(500).body("게시글 삭제 중 오류 발생: " + e.getMessage());
        }
    }

    // 파일 저장 메서드
    private String saveFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어 있습니다.");
        }

        // MIME 타입 확인 (기존 확장자 확인 대신 추가된 로직)
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("이미지 파일만 업로드할 수 있습니다.");
        }

        String originalFilename = file.getOriginalFilename();
        String fileName = UUID.randomUUID() + "_" + (originalFilename != null ? originalFilename : "file");
        Path directoryPath = Paths.get(uploadDir);

        // 디렉토리가 없는 경우 생성
        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }

        Path filePath = directoryPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath); // 파일 저장
        return "/uploads/" + fileName; // 상대 경로 반환
    }

    // 공통 메서드: 사용자 인증 및 검증
    private User validateUser(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("인증이 필요합니다.");
        }

        String token = authHeader.substring(7);
        String userId = jwtUtil.validateAndExtractUsername(token); // 토큰에서 사용자 ID 추출

        if (userId == null) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }

        return userService.findById(userId); // 사용자 조회
    }

    // DTO 클래스: ChallengePostResponse

        // Getters (필요 시 추가)
    }

