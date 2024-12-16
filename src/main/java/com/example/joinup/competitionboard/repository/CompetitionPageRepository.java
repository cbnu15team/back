package com.example.joinup.competitionboard.repository;

import com.example.joinup.competitionboard.entity.CompetitionPage;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.joinup.user.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CompetitionPageRepository extends JpaRepository<CompetitionPage, Long> {
    List<CompetitionPage> findByUser(User user);
}