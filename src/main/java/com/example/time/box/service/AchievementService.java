package com.example.time.box.service;

import com.example.time.box.entity.AchievementEntity;
import com.example.time.box.entity.request.AchievementCreateRequest;
import com.example.time.box.exception.EntityNotFoundException;
import com.example.time.box.repository.AchievementRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AchievementService {
    private final AchievementRepository achievementRepository;

    public List<AchievementEntity> findAll() {
        return achievementRepository.findAll();
    }

    public List<AchievementEntity> findAllByUserId(Long userId) {
        return achievementRepository.findByUserId(userId);
    }

    public List<AchievementEntity> findCompletedByUserId(Long userId) {
        return achievementRepository.findByUserIdAndCompleted(userId, true);
    }

    public List<AchievementEntity> findIncompleteByUserId(Long userId) {
        return achievementRepository.findByUserIdAndCompleted(userId, false);
    }

    public AchievementEntity findById(Long id) {
        return achievementRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public AchievementEntity save(AchievementEntity achievement) {
        return achievementRepository.save(achievement);
    }

    public AchievementEntity createAchievement(String name, String description, Long subjectId, Long userId) {
        AchievementEntity achievement = new AchievementEntity();
        achievement.setName(name);
        achievement.setDescription(description);
        achievement.setSubjectId(subjectId);
        achievement.setUserId(userId);
        achievement.setCompleted(false);

        return achievementRepository.save(achievement);
    }

    public AchievementEntity createAchievement(AchievementCreateRequest request) {
        AchievementEntity achievement = new AchievementEntity();
        achievement.setName(request.getName());
        achievement.setDescription(request.getDescription());
        achievement.setSubjectId(request.getSubjectId());
        achievement.setUserId(request.getUserId());
        achievement.setCompleted(false);

        return achievementRepository.save(achievement);
    }

    public AchievementEntity completeAchievement(Long id) {
        AchievementEntity achievement = findById(id);
        achievement.setCompleted(true);
        return achievementRepository.save(achievement);
    }

    public void deleteById(Long id) {
        achievementRepository.deleteById(id);
    }
}
