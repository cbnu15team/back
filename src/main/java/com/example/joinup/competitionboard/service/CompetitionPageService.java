package com.example.joinup.competitionboard.service;

import com.example.joinup.competitionboard.dto.CompetitionPageResponse;
import com.example.joinup.competitionboard.entity.CompetitionPage;
import com.example.joinup.competitionboard.repository.CompetitionPageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // 추가된 import

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompetitionPageService {

    private final CompetitionPageRepository competitionPageRepository;

    public CompetitionPageService(CompetitionPageRepository competitionPageRepository) {
        this.competitionPageRepository = competitionPageRepository;
    }

    public List<CompetitionPageResponse> getAllPages() {
        return competitionPageRepository.findAll()
                .stream()
                .map(CompetitionPageResponse::new)
                .collect(Collectors.toList());
    }

    public CompetitionPageResponse getPageById(Long id) {
        CompetitionPage page = competitionPageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("페이지를 찾을 수 없습니다. ID: " + id));
        page.incrementViews();
        competitionPageRepository.save(page);
        return new CompetitionPageResponse(page);
    }

    public CompetitionPage createPage(CompetitionPage page) {
        if (page.getBoardType() == null || page.getBoardType().isEmpty()) {
            throw new RuntimeException("게시판 종류는 필수입니다.");
        }
        if (page.getUser() == null || page.getUser().getUserId() == null) {
            throw new RuntimeException("유저 정보는 필수입니다.");
        }
        return competitionPageRepository.save(page);
    }

    @Transactional
    public void deletePage(Long id) {
        competitionPageRepository.deleteById(id);
    }
}
