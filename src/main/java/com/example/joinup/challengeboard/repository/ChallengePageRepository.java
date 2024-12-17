package com.example.joinup.challengeboard.repository;

import com.example.joinup.challengeboard.entity.ChallengePage;
import com.example.joinup.user.entity.User; // User import 추가
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChallengePageRepository extends JpaRepository<ChallengePage, Long> {

    @Query("SELECT cp FROM ChallengePage cp JOIN FETCH cp.user")
    List<ChallengePage> findAllWithUser();

    // 사용자에 의해 작성된 ChallengePage만 조회하는 메서드 추가
    List<ChallengePage> findByUser(User user);
    Optional<ChallengePage> findByTitle(String title);
}
