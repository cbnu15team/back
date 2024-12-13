package com.example.joinup.challengeboard.service;

import com.example.joinup.challengeboard.dto.ChallengePageResponse;
import com.example.joinup.challengeboard.entity.ChallengePage;
import com.example.joinup.challengeboard.repository.ChallengePageRepository;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChallengePageService {

    private final ChallengePageRepository challengePageRepository;
    private final EntityManager entityManager;

    public ChallengePageService(ChallengePageRepository challengePageRepository, EntityManager entityManager) {
        this.challengePageRepository = challengePageRepository;
        this.entityManager = entityManager;
    }

    // 모든 페이지 조회
    public List<ChallengePageResponse> getAllPages() {
        return challengePageRepository.findAll()
                .stream()
                .map(ChallengePageResponse::new)
                .collect(Collectors.toList());
    }

    // 특정 페이지 조회 및 조회수 증가
    @Transactional
    public ChallengePageResponse getPageById(Long id) {
        ChallengePage page = challengePageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("페이지를 찾을 수 없습니다. ID: " + id));
        Hibernate.initialize(page.getUser()); // Lazy 속성 초기화
        page.incrementViews(); // 조회수 증가
        challengePageRepository.save(page);
        return new ChallengePageResponse(page);
    }

    // 페이지 작성
    public ChallengePageResponse createPage(ChallengePage page) {
        if (page.getBoardType() == null || page.getBoardType().isEmpty()) {
            throw new RuntimeException("게시판 종류는 필수입니다.");
        }
        if (page.getUser() == null || page.getUser().getUserId() == null) {
            throw new RuntimeException("유저 정보는 필수입니다.");
        }
        ChallengePage savedPage = challengePageRepository.save(page);
        return new ChallengePageResponse(savedPage);
    }

    // 페이지 삭제
    @Transactional
    public void deletePageIfOwner(Long id, String userId) {
        ChallengePage page = challengePageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("삭제하려는 페이지가 존재하지 않습니다. ID: " + id));

        // 작성자 확인
        if (!page.getUser().getId().equals(userId)) {
            throw new RuntimeException("작성자만 게시글을 삭제할 수 있습니다.");
        }

        challengePageRepository.delete(page);

        // DB 변경 사항 강제 반영 및 캐시 초기화
        entityManager.flush();
        entityManager.clear();
    }

    // 페이지 업데이트
    @Transactional
    public ChallengePageResponse updatePage(Long id, ChallengePage updatedPage, String userId) {
        ChallengePage existingPage = challengePageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("수정하려는 페이지가 존재하지 않습니다. ID: " + id));

        // 작성자 확인
        if (!existingPage.getUser().getId().equals(userId)) {
            throw new RuntimeException("작성자만 게시글을 수정할 수 있습니다.");
        }

        // 필드 업데이트
        existingPage.setTitle(updatedPage.getTitle());
        existingPage.setContent(updatedPage.getContent());
        existingPage.setBoardType(updatedPage.getBoardType());

        // 저장 후 응답 반환
        ChallengePage savedPage = challengePageRepository.save(existingPage);
        return new ChallengePageResponse(savedPage);
    }
}
