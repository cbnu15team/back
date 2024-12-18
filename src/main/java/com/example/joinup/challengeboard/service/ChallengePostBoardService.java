package com.example.joinup.challengeboard.service;

import com.example.joinup.challengeboard.dto.ChallengePostResponse;
import com.example.joinup.challengeboard.entity.ChallengePost;
import com.example.joinup.challengeboard.repository.ChallengePostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChallengePostBoardService {

    private final ChallengePostRepository challengePostRepository;

    public ChallengePostBoardService(ChallengePostRepository challengePostRepository) {
        this.challengePostRepository = challengePostRepository;
    }

    // 게시글 목록 조회
    public Page<ChallengePostResponse> getAllPosts(Pageable pageable) {
        Page<ChallengePost> posts = challengePostRepository.findAllWithChallengePage(pageable);
        return posts.map(ChallengePostResponse::new);
    }

    // 단일 게시글 조회 및 조회수 증가
    @Transactional
    public ChallengePostResponse getPostByIdAndIncrementViews(Long id) {
        // 게시글 조회
        ChallengePost post = challengePostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다."));

        // 조회수 증가
        post.incrementViews();

        return new ChallengePostResponse(post);
    }
}
