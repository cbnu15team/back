package com.example.joinup.challengeboard.service;

import com.example.joinup.challengeboard.dto.ChallengePageResponse;
import com.example.joinup.challengeboard.entity.ChallengePage;
import com.example.joinup.challengeboard.repository.ChallengePageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChallengePageService {

    private final ChallengePageRepository challengePageRepository;

    // Constructor injection for repository
    public ChallengePageService(ChallengePageRepository challengePageRepository) {
        this.challengePageRepository = challengePageRepository;
    }

    // 모든 페이지 조회
    public List<ChallengePageResponse> getAllPages() {
        return challengePageRepository.findAll()
                .stream()
                .map(ChallengePageResponse::new)
                .collect(Collectors.toList());
    }

    // 특정 페이지 조회 및 조회수 증가
    public ChallengePageResponse getPageById(Long id) {
        ChallengePage page = challengePageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("페이지를 찾을 수 없습니다. ID: " + id));
        page.incrementViews(); // 조회수 증가
        challengePageRepository.save(page);
        return new ChallengePageResponse(page);
    }

    // 페이지 작성
    public ChallengePage createPage(ChallengePage page) {
        // boardType과 user 검증
        if (page.getBoardType() == null || page.getBoardType().isEmpty()) {
            throw new RuntimeException("게시판 종류는 필수입니다.");
        }
        if (page.getUser() == null || page.getUser().getUserId() == null) {
            throw new RuntimeException("유저 정보는 필수입니다.");
        }

        return challengePageRepository.save(page);
    }
}