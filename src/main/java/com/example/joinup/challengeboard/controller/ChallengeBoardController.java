package com.example.joinup.challengeboard.controller;

import com.example.joinup.challengeboard.dto.ChallengeBoardResponse;
import com.example.joinup.challengeboard.service.ChallengeBoardService;
import com.example.joinup.challengeboard.service.ChallengePageService;
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
}
