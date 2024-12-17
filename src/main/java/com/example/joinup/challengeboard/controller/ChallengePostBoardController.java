package com.example.joinup.challengeboard.controller;

import com.example.joinup.challengeboard.dto.ChallengePostResponse;
import com.example.joinup.challengeboard.service.ChallengePostBoardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/challenge-posts")
public class ChallengePostBoardController {

    private final ChallengePostBoardService challengePostBoardService;

    public ChallengePostBoardController(ChallengePostBoardService challengePostBoardService) {
        this.challengePostBoardService = challengePostBoardService;
    }

    // 게시글 목록 조회 (페이지네이션 포함)
    @GetMapping
    public ResponseEntity<Page<ChallengePostResponse>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ChallengePostResponse> posts = challengePostBoardService.getAllPosts(pageable);
        return ResponseEntity.ok(posts);
    }

    // 단일 게시글 조회 (조회수 증가 포함)
    @GetMapping("/{id}")
    public ResponseEntity<ChallengePostResponse> getPostById(@PathVariable Long id) {
        // 조회수 증가 및 게시글 조회
        ChallengePostResponse post = challengePostBoardService.getPostByIdAndIncrementViews(id);
        return ResponseEntity.ok(post);
    }
}
