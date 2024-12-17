package com.example.joinup.challengeboard.service;

import com.example.joinup.challengeboard.entity.ChallengePage;
import com.example.joinup.challengeboard.entity.ChallengePost;
import com.example.joinup.challengeboard.repository.ChallengePageRepository;
import com.example.joinup.challengeboard.repository.ChallengePostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChallengePostService {
    private final ChallengePostRepository challengePostRepository;
    private final ChallengePageRepository challengePageRepository;

    public ChallengePostService(ChallengePostRepository challengePostRepository,
                                ChallengePageRepository challengePageRepository) {
        this.challengePostRepository = challengePostRepository;
        this.challengePageRepository = challengePageRepository;
    }

    // 게시글 생성
    public ChallengePost createPost(Long challengePageId, ChallengePost post) {
        // ChallengePage 조회
        ChallengePage challengePage = challengePageRepository.findById(challengePageId)
                .orElseThrow(() -> new IllegalArgumentException("해당 챌린지가 존재하지 않습니다."));

        // ChallengePage 설정 및 challengeType 자동 설정
        post.setChallengePage(challengePage);
        post.setChallengeType(challengePage.getTitle()); // ChallengePage의 title을 challengeType으로 설정
        return challengePostRepository.save(post);
    }

    @Transactional
    public ChallengePost updatePost(Long id, ChallengePost updatedPost, String userId) {
        ChallengePost post = challengePostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        // 작성자 확인
        if (!post.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
        }

        // 제목, 내용, 사진만 수정
        post.setTitle(updatedPost.getTitle());
        post.setContent(updatedPost.getContent());
        post.setPhotoUrl(updatedPost.getPhotoUrl());

        return post; // @Transactional이 적용되므로 save 호출 불필요
    }

    @Transactional
    public ChallengePost getPostById(Long id) {
        return challengePostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
    }


    @Transactional
    public void deletePost(Long id, String userId) {
        ChallengePost post = challengePostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        // 작성자 확인
        if (!post.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("이 게시글을 삭제할 권한이 없습니다.");
        }

        challengePostRepository.delete(post);
    }
}
