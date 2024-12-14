package com.example.joinup.challengeboard.service;

import com.example.joinup.challengeboard.dto.ChallengeBoardResponse;
import com.example.joinup.challengeboard.entity.ChallengePage;
import com.example.joinup.challengeboard.repository.ChallengePageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChallengeBoardService {

    private final ChallengePageRepository challengePageRepository;

    public ChallengeBoardService(ChallengePageRepository challengePageRepository) {
        this.challengePageRepository = challengePageRepository;
    }

    /**
     * 특정 게시판의 페이지 목록 조회
     *
     * @param boardType 게시판 유형
     * @return ChallengeBoardResponse 리스트
     */
    @Transactional(readOnly = true)
    public List<ChallengeBoardResponse> getPagesByBoardType(String boardType) {
        // Fetch Join을 사용하여 관련 엔터티를 미리 로드
        List<ChallengePage> pages = challengePageRepository.findAllWithUser();

        return pages.stream()
                .filter(page -> boardType.equalsIgnoreCase(page.getBoardType())) // 필터링
                .map(ChallengeBoardResponse::new) // DTO 변환
                .collect(Collectors.toList());
    }

    /**
     * 전체 페이지 목록 조회
     *
     * @return ChallengeBoardResponse 리스트
     */
    @Transactional(readOnly = true)
    public List<ChallengeBoardResponse> getAllPages() {
        // Fetch Join을 사용하여 관련 엔터티를 미리 로드
        List<ChallengePage> pages = challengePageRepository.findAllWithUser();

        return pages.stream()
                .map(ChallengeBoardResponse::new) // DTO 변환
                .collect(Collectors.toList());
    }
}
