package com.example.joinup.competitionboard.service;

import com.example.joinup.competitionboard.dto.CompetitionBoardResponse;
import com.example.joinup.competitionboard.entity.CompetitionPage;
import com.example.joinup.competitionboard.repository.CompetitionPageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompetitionBoardService {

    private final CompetitionPageRepository competitionPageRepository;

    public CompetitionBoardService(CompetitionPageRepository competitionPageRepository) {
        this.competitionPageRepository = competitionPageRepository;
    }

    // 특정 게시판의 페이지 목록 조회
    public List<CompetitionBoardResponse> getPagesByBoardType(String boardType) {
        List<CompetitionPage> pages = competitionPageRepository.findAll()
                .stream()
                //.filter(page -> page.getBoardType().equalsIgnoreCase(boardType))
                .collect(Collectors.toList());

        return pages.stream()
                .map(CompetitionBoardResponse::new)
                .collect(Collectors.toList());
    }

    // 전체 페이지 목록 조회
    public List<CompetitionBoardResponse> getAllPages() {
        return competitionPageRepository.findAll()
                .stream()
                .map(CompetitionBoardResponse::new)
                .collect(Collectors.toList());
    }
}
