package com.example.joinup.competitionboard.service;

import com.example.joinup.competitionboard.dto.CompetitionPageResponse;
import com.example.joinup.competitionboard.entity.CompetitionPage;
import com.example.joinup.competitionboard.repository.CompetitionPageRepository;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompetitionPageService {

    private final CompetitionPageRepository competitionPageRepository;

    public CompetitionPageService(CompetitionPageRepository competitionPageRepository) {
        this.competitionPageRepository = competitionPageRepository;
    }

    /**
     * 모든 CompetitionPage를 조회
     */
    public List<CompetitionPageResponse> getAllPages() {
        return competitionPageRepository.findAll()
                .stream()
                .map(CompetitionPageResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 특정 CompetitionPage를 ID로 조회
     * Lazy 속성 초기화를 위해 @Transactional 적용
     */
    @Transactional
    public CompetitionPageResponse getPageById(Long id) {
        CompetitionPage page = competitionPageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("페이지를 찾을 수 없습니다. ID: " + id));
        Hibernate.initialize(page.getUser()); // Lazy-Loaded 속성 초기화
        page.incrementViews(); // 조회수 증가
        competitionPageRepository.save(page); // 변경사항 저장
        return new CompetitionPageResponse(page);
    }

    /**
     * 새로운 CompetitionPage 생성
     */
    public CompetitionPageResponse createPage(CompetitionPage page) {
        if (page.getBoardType() == null || page.getBoardType().isEmpty()) {
            throw new IllegalArgumentException("게시판 종류는 필수입니다.");
        }
        if (page.getUser() == null || page.getUser().getUserId() == null) {
            throw new IllegalArgumentException("유저 정보는 필수입니다.");
        }
        CompetitionPage savedPage = competitionPageRepository.save(page);
        return new CompetitionPageResponse(savedPage);
    }

    /**
     * CompetitionPage 삭제
     */
    @Transactional
    public void deletePage(Long id) {
        if (!competitionPageRepository.existsById(id)) {
            throw new IllegalArgumentException("삭제하려는 페이지가 존재하지 않습니다. ID: " + id);
        }
        competitionPageRepository.deleteById(id);
    }
}
