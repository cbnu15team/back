package com.example.joinup.challengeboard.controller;

import com.example.joinup.challengeboard.dto.ChallengeBoardResponse;
import com.example.joinup.challengeboard.entity.ChallengePage;
import com.example.joinup.challengeboard.service.ChallengeBoardService;
import com.example.joinup.challengeboard.service.ChallengePageService;
import com.example.joinup.user.entity.User;
import com.example.joinup.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/challenge-boards")
public class ChallengeBoardController {

    private final ChallengeBoardService challengeBoardService;
    private final ChallengePageService challengePageService;
    private final UserService userService;

    public ChallengeBoardController(
            ChallengeBoardService challengeBoardService,
            ChallengePageService challengePageService,
            UserService userService
    ) {
        this.challengeBoardService = challengeBoardService;
        this.challengePageService = challengePageService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<ChallengeBoardResponse>> getAllPages() {
        List<ChallengeBoardResponse> pages = challengeBoardService.getAllPages();
        return ResponseEntity.ok(pages);
    }

    @GetMapping("/{boardType}")
    public ResponseEntity<List<ChallengeBoardResponse>> getPagesByBoardType(@PathVariable String boardType) {
        List<ChallengeBoardResponse> pages = challengeBoardService.getPagesByBoardType(boardType);
        return ResponseEntity.ok(pages);
    }

    @PostMapping
    public ResponseEntity<?> createBoard(@RequestBody ChallengePage page) {
        try {
            // User의 userId 확인
            if (page.getUser() == null || page.getUser().getUserId() == null) {
                throw new RuntimeException("유저 userId는 필수입니다.");
            }

            // userId로 User 조회
            User user = userService.findByUserId(page.getUser().getUserId())
                    .orElseThrow(() -> new RuntimeException("해당 userId의 유저를 찾을 수 없습니다."));

            // ChallengePage에 User 설정
            page.setUser(user);

            // ChallengePage 저장
            ChallengePage createdPage = challengePageService.createPage(page);

            return ResponseEntity.ok(new ChallengeBoardResponse(createdPage));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("게시글 생성 실패: " + e.getMessage());
        }
    }
}