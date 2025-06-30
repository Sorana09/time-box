package com.example.time.box.repository;

import com.example.time.box.entity.AchievementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AchievementRepository extends JpaRepository<AchievementEntity, Long> {
    void deleteById(Long id);

    List<AchievementEntity> findAll();

    List<AchievementEntity> findByUserId(Long userId);

    List<AchievementEntity> findAllByUserId(Long userId);

    List<AchievementEntity> findByUserIdAndCompleted(Long userId, Boolean completed);
}