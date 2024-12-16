package com.example.joinup.challengeboard.repository;

import com.example.joinup.challengeboard.entity.ChallengePost;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.joinup.user.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChallengePostRepository extends JpaRepository<ChallengePost, Long> {
    List<ChallengePost> findByUser(User user);
}
