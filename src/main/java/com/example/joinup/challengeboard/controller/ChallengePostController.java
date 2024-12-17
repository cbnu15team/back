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

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(
            @PathVariable Long id,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "photo", required = false) MultipartFile photo,
            @RequestParam(value = "content", required = false) String content,
            @RequestParam(value = "challengeType", required = false) String challengeType, // challengeType 수정 가능하게 추가
            @RequestHeader("Authorization") String authHeader) {

        try {
            // Authorization 헤더 검증
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body("인증이 필요합니다.");
            }

            String token = authHeader.substring(7);
            String userId = jwtUtil.validateAndExtractUsername(token);

            User user = userService.findById(userId);
            ChallengePost existingPost = challengePostService.getPostById(id);

            // 게시글 수정 권한 확인
            if (!existingPost.getUser().getId().equals(user.getId())) {
                return ResponseEntity.status(403).body("이 게시글을 수정할 권한이 없습니다.");
            }

            // 게시글 수정
            if (title != null) existingPost.setTitle(title);
            if (content != null) existingPost.setContent(content);

            // challengeType 수정 추가
            if (challengeType != null) {
                existingPost.setChallengeType(challengeType); // challengeType 수정 로직
            }

            // 파일 수정
            if (photo != null && !photo.isEmpty()) {
                String savedFilePath = saveFile(photo);
                existingPost.setPhotoUrl(savedFilePath);
            }

            // 서비스 호출
            ChallengePost updatedPost = challengePostService.updatePost(id, existingPost, userId);
            return ResponseEntity.ok(updatedPost);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("파일 저장 중 오류 발생: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("게시글 수정 중 오류 발생: " + e.getMessage());
        }
    }

    private String saveFile(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path directoryPath = Paths.get(uploadDir);

        // 디렉터리 생성
        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }

        Path filePath = directoryPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);
        return "/uploads/" + fileName;
    }
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<?> createPost(
            @RequestParam("title") String title,
            @RequestParam("photo") MultipartFile photo,
            @RequestParam("content") String content,
            @RequestParam("challengeType") String challengeType, // challengeType 추가

            @RequestHeader("Authorization") String authHeader) {

        try {
            // Authorization 헤더 검증
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body("인증이 필요합니다.");
            }

            String token = authHeader.substring(7);
            String userId = jwtUtil.validateAndExtractUsername(token);

            if (userId == null) {
                return ResponseEntity.status(401).body("유효하지 않은 토큰입니다.");
            }

            // 사용자 조회
            User user = userService.findById(userId);

            // 파일 저장
            String savedFilePath = saveFile(photo);

            // ChallengePost 객체 생성 및 저장
            ChallengePost post = new ChallengePost();
            post.setTitle(title);
            post.setPhotoUrl(savedFilePath);
            post.setContent(content);
            post.setChallengeType(challengeType); // challengeType 설정


            post.setUser(user);

            ChallengePost createdPost = challengePostService.createPost(post);
            return ResponseEntity.ok(createdPost);

        } catch (IOException e) {
            return ResponseEntity.status(500).body("파일 저장 중 오류 발생: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("게시글 작성 중 오류 발생: " + e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {

        try {
            // Authorization 헤더 검증
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body("인증이 필요합니다.");
            }

            String token = authHeader.substring(7);
            String userId = jwtUtil.validateAndExtractUsername(token);

            // 게시글 삭제 호출
            challengePostService.deletePost(id, userId);
            return ResponseEntity.ok("게시글이 성공적으로 삭제되었습니다.");

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(403).body("삭제 권한이 없거나 게시글이 존재하지 않습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("게시글 삭제 중 오류 발생: " + e.getMessage());
        }
    }



}
