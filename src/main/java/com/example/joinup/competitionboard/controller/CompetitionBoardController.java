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


}