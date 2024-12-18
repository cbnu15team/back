package com.example.joinup.challengeboard.repository;

import com.example.joinup.challengeboard.entity.ChallengePost;
import com.example.joinup.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChallengePostRepository extends JpaRepository<ChallengePost, Long> {

    // 특정 사용자에 의한 게시글 조회
    List<ChallengePost> findByUser(User user);

    // ChallengePage와 함께 게시글 조회 (페이징 지원)
    @Query("SELECT p FROM ChallengePost p LEFT JOIN FETCH p.challengePage")
    Page<ChallengePost> findAllWithChallengePage(Pageable pageable);

}
