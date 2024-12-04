package com.example.joinup.competitionboard.controller;

import com.example.joinup.competitionboard.dto.CompetitionBoardResponse;
import com.example.joinup.competitionboard.entity.CompetitionPage;
import com.example.joinup.competitionboard.service.CompetitionBoardService;
import com.example.joinup.competitionboard.service.CompetitionPageService;
import com.example.joinup.user.entity.User;
import com.example.joinup.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/competition-boards")
public class CompetitionBoardController {

    private final CompetitionBoardService competitionBoardService;
    private final CompetitionPageService competitionPageService;
    private final UserService userService;

    public CompetitionBoardController(
            CompetitionBoardService competitionBoardService,
            CompetitionPageService competitionPageService,
            UserService userService
    ) {
        this.competitionBoardService = competitionBoardService;
        this.competitionPageService = competitionPageService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<CompetitionBoardResponse>> getAllPages() {
        List<CompetitionBoardResponse> pages = competitionBoardService.getAllPages();
        return ResponseEntity.ok(pages);
    }

    @GetMapping("/{boardType}")
    public ResponseEntity<List<CompetitionBoardResponse>> getPagesByBoardType(@PathVariable String boardType) {
        List<CompetitionBoardResponse> pages = competitionBoardService.getPagesByBoardType(boardType);
        return ResponseEntity.ok(pages);
    }

    @PostMapping
    public ResponseEntity<?> createBoard(@RequestBody CompetitionPage page) {
        try {
            if (page.getUser() == null || page.getUser().getUserId() == null) {
                throw new RuntimeException("유저 userId는 필수입니다.");
            }

            User user = userService.findByUserId(page.getUser().getUserId())
                    .orElseThrow(() -> new RuntimeException("해당 userId의 유저를 찾을 수 없습니다."));

            page.setUser(user);

            CompetitionPage createdPage = competitionPageService.createPage(page);

            return ResponseEntity.ok(new CompetitionBoardResponse(createdPage));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("게시글 생성 실패: " + e.getMessage());
        }
    }
}