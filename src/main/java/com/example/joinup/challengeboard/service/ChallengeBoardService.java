package com.example.joinup.challengeboard.service;

import com.example.joinup.challengeboard.dto.ChallengeBoardResponse;
import com.example.joinup.challengeboard.entity.ChallengePage;
import com.example.joinup.challengeboard.repository.ChallengePageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChallengeBoardService {

    private final ChallengePageRepository challengePageRepository;

    public ChallengeBoardService(ChallengePageRepository challengePageRepository) {
        this.challengePageRepository = challengePageRepository;
    }

    // 특정 게시판의 페이지 목록 조회
    public List<ChallengeBoardResponse> getPagesByBoardType(String boardType) {
        List<ChallengePage> pages = challengePageRepository.findAll()
                .stream()
                //.filter(page -> page.getBoardType().equalsIgnoreCase(boardType))
                .collect(Collectors.toList());

        return pages.stream()
                .map(ChallengeBoardResponse::new)
                .collect(Collectors.toList());
    }

    // 전체 페이지 목록 조회
    public List<ChallengeBoardResponse> getAllPages() {
        return challengePageRepository.findAll()
                .stream()
                .map(ChallengeBoardResponse::new)
                .collect(Collectors.toList());
    }
}