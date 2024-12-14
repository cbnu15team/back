package com.example.joinup.competitionboard.service;

import com.example.joinup.competitionboard.dto.CompetitionBoardResponse;
import com.example.joinup.competitionboard.entity.CompetitionPage;
import com.example.joinup.competitionboard.repository.CompetitionPageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompetitionBoardService {

    private final CompetitionPageRepository competitionPageRepository;

    public CompetitionBoardService(CompetitionPageRepository competitionPageRepository) {
        this.competitionPageRepository = competitionPageRepository;
    }

    /**
     * 특정 게시판의 페이지 목록 조회
     *
     * @param boardType 게시판 유형
     * @return CompetitionBoardResponse 리스트
     */
    @Transactional(readOnly = true)
    public List<CompetitionBoardResponse> getPagesByBoardType(String boardType) {
        // boardType에 해당하는 페이지 필터링
        return competitionPageRepository.findAll()
                .stream()
                .filter(page -> boardType.equalsIgnoreCase(page.getBoardType())) // Null-safe 필터링
                .map(CompetitionBoardResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 전체 페이지 목록 조회
     *
     * @return CompetitionBoardResponse 리스트
     */
    @Transactional(readOnly = true)
    public List<CompetitionBoardResponse> getAllPages() {
        // 전체 페이지를 DTO로 변환
        return competitionPageRepository.findAll()
                .stream()
                .map(CompetitionBoardResponse::new)
                .collect(Collectors.toList());
    }
}
