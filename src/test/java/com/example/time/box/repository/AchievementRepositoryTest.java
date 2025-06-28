package com.example.time.box.repository;

import com.example.time.box.entity.AchievementEntity;
import com.example.time.box.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AchievementRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AchievementRepository achievementRepository;

    private UserEntity user1;
    private UserEntity user2;
    private AchievementEntity achievement1;
    private AchievementEntity achievement2;
    private AchievementEntity achievement3;

    @BeforeEach
    void setUp() {
        // Create test users
        user1 = UserEntity.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .hashedPassword("hashedPassword")
                .emailVerified(true)
                .build();
        
        user2 = UserEntity.builder()
                .firstName("Jane")
                .lastName("Smith")
                .email("jane.smith@example.com")
                .hashedPassword("hashedPassword")
                .emailVerified(true)
                .build();
        
        // Persist users
        user1 = entityManager.persist(user1);
        user2 = entityManager.persist(user2);
        
        // Create test achievements
        achievement1 = AchievementEntity.builder()
                .name("Achievement 1")
                .description("Description 1")
                .criteria("Criteria 1")
                .completed(true)
                .userId(user1.getId())
                .build();
        
        achievement2 = AchievementEntity.builder()
                .name("Achievement 2")
                .description("Description 2")
                .criteria("Criteria 2")
                .completed(false)
                .userId(user1.getId())
                .build();
        
        achievement3 = AchievementEntity.builder()
                .name("Achievement 3")
                .description("Description 3")
                .criteria("Criteria 3")
                .completed(true)
                .userId(user2.getId())
                .build();
        
        // Persist achievements
        entityManager.persist(achievement1);
        entityManager.persist(achievement2);
        entityManager.persist(achievement3);
        
        // Flush to ensure data is in the database
        entityManager.flush();
    }

    @Test
    void findByUserId() {
        // When
        List<AchievementEntity> achievements = achievementRepository.findByUserId(user1.getId());
        
        // Then
        assertEquals(2, achievements.size());
        assertTrue(achievements.stream().anyMatch(a -> a.getName().equals("Achievement 1")));
        assertTrue(achievements.stream().anyMatch(a -> a.getName().equals("Achievement 2")));
    }

    @Test
    void findAllByUserId() {
        // When
        List<AchievementEntity> achievements = achievementRepository.findAllByUserId(user1.getId());
        
        // Then
        assertEquals(2, achievements.size());
        assertTrue(achievements.stream().anyMatch(a -> a.getName().equals("Achievement 1")));
        assertTrue(achievements.stream().anyMatch(a -> a.getName().equals("Achievement 2")));
    }

    @Test
    void findByUserIdAndCompleted() {
        // When - Find completed achievements for user1
        List<AchievementEntity> completedAchievements = achievementRepository.findByUserIdAndCompleted(user1.getId(), true);
        
        // Then
        assertEquals(1, completedAchievements.size());
        assertEquals("Achievement 1", completedAchievements.get(0).getName());
        
        // When - Find incomplete achievements for user1
        List<AchievementEntity> incompleteAchievements = achievementRepository.findByUserIdAndCompleted(user1.getId(), false);
        
        // Then
        assertEquals(1, incompleteAchievements.size());
        assertEquals("Achievement 2", incompleteAchievements.get(0).getName());
    }

    @Test
    void deleteById() {
        // When
        achievementRepository.deleteById(achievement1.getId());
        entityManager.flush();
        
        // Then
        List<AchievementEntity> achievements = achievementRepository.findAll();
        assertEquals(2, achievements.size());
        assertFalse(achievements.stream().anyMatch(a -> a.getId().equals(achievement1.getId())));
    }
}