package com.example.joinup.competitionboard.service;

import com.example.joinup.competitionboard.dto.CompetitionPageResponse;
import com.example.joinup.competitionboard.entity.CompetitionPage;
import com.example.joinup.competitionboard.repository.CompetitionPageRepository;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompetitionPageService {

    private final CompetitionPageRepository competitionPageRepository;
    private final EntityManager entityManager; // EntityManager 추가

    public CompetitionPageService(CompetitionPageRepository competitionPageRepository, EntityManager entityManager) {
        this.competitionPageRepository = competitionPageRepository;
        this.entityManager = entityManager; // 주입된 EntityManager 초기화
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
    public void deletePageIfOwner(Long id, String userId) {
        CompetitionPage page = competitionPageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("삭제하려는 페이지가 존재하지 않습니다. ID: " + id));

        // 작성자가 요청한 사용자와 동일한지 확인
        if (!page.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("작성자만 게시글을 삭제할 수 있습니다.");
        }

        // 삭제 처리
        competitionPageRepository.delete(page);

        // DB로 강제 반영 및 캐시 초기화
        entityManager.flush(); // 변경 사항 강제 반영
        entityManager.clear(); // 캐시 초기화
    }

}
