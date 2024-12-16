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

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<?> createPost(
            @RequestParam("title") String title,
            @RequestParam("photo") MultipartFile photo,
            @RequestParam("content") String content,
            @RequestHeader("Authorization") String authHeader) {

        // authHeader 값을 확인하기 위한 로그
        System.out.println("Authorization Header: " + authHeader);

        try {
            // Authorization 헤더에서 토큰 추출 및 검증
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body("인증이 필요합니다. 유효한 토큰을 제공해주세요.");
            }
            String token = authHeader.substring(7); // Bearer 이후 토큰만 추출
            String userId = jwtUtil.validateAndExtractUsername(token);

            if (userId == null) {
                return ResponseEntity.status(401).body("유효하지 않은 토큰입니다.");
            }

            // 사용자 검증
            User user = userService.findById(userId);

            // 파일 저장
            String savedFilePath = saveFile(photo);

            // ChallengePost 객체 생성 및 저장
            ChallengePost post = new ChallengePost();
            post.setTitle(title);
            post.setPhotoUrl(savedFilePath);
            post.setContent(content);
            post.setUser(user);

            ChallengePost createdPost = challengePostService.createPost(post);
            return ResponseEntity.ok(createdPost);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("파일 저장 중 오류가 발생했습니다: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("오류 발생: " + e.getMessage());
        }
    }



    // 파일 저장 메서드
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
}
