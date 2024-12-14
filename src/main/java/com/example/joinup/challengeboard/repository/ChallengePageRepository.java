package com.example.joinup.challengeboard.repository;

import com.example.joinup.challengeboard.entity.ChallengePage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChallengePageRepository extends JpaRepository<ChallengePage, Long> {

    @Query("SELECT cp FROM ChallengePage cp JOIN FETCH cp.user")
    List<ChallengePage> findAllWithUser();
}
