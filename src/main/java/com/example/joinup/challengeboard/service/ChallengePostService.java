package com.example.joinup.challengeboard.service;

import com.example.joinup.challengeboard.entity.ChallengePost;
import com.example.joinup.challengeboard.repository.ChallengePostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChallengePostService {
    private final ChallengePostRepository challengePostRepository;

    public ChallengePostService(ChallengePostRepository challengePostRepository) {
        this.challengePostRepository = challengePostRepository;
    }

    public ChallengePost createPost(ChallengePost post) {
        return challengePostRepository.save(post);
    }

    @Transactional
    public ChallengePost updatePost(Long id, ChallengePost updatedPost, String userId) {
        ChallengePost post = challengePostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        if (!post.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
        }
        post.setTitle(updatedPost.getTitle()); // 제목 업데이트
        post.setContent(updatedPost.getContent());
        post.setPhotoUrl(updatedPost.getPhotoUrl());
        return challengePostRepository.save(post);
    }
}
