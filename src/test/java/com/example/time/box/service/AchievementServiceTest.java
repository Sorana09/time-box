package com.example.time.box.service;

import com.example.time.box.entity.AchievementEntity;
import com.example.time.box.entity.request.AchievementCreateRequest;
import com.example.time.box.exception.EntityNotFoundException;
import com.example.time.box.repository.AchievementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AchievementServiceTest {

    @Mock
    private AchievementRepository achievementRepository;

    @InjectMocks
    private AchievementService achievementService;

    private AchievementEntity achievement1;
    private AchievementEntity achievement2;
    private List<AchievementEntity> achievements;
    private AchievementCreateRequest createRequest;

    @BeforeEach
    void setUp() {
        achievement1 = new AchievementEntity();
        achievement1.setId(1L);
        achievement1.setName("Test Achievement 1");
        achievement1.setDescription("Test Description 1");
        achievement1.setCriteria("Test Criteria 1");
        achievement1.setUserId(1L);
        achievement1.setCompleted(false);

        achievement2 = new AchievementEntity();
        achievement2.setId(2L);
        achievement2.setName("Test Achievement 2");
        achievement2.setDescription("Test Description 2");
        achievement2.setCriteria("Test Criteria 2");
        achievement2.setUserId(1L);
        achievement2.setCompleted(true);

        achievements = Arrays.asList(achievement1, achievement2);

        createRequest = new AchievementCreateRequest();
        createRequest.setName("New Achievement");
        createRequest.setDescription("New Description");
        createRequest.setCriteria("New Criteria");
        createRequest.setUserId(1L);
    }

    @Test
    void findAll() {
        when(achievementRepository.findAll()).thenReturn(achievements);

        List<AchievementEntity> result = achievementService.findAll();

        assertEquals(2, result.size());
        assertEquals("Test Achievement 1", result.get(0).getName());
        assertEquals("Test Achievement 2", result.get(1).getName());
        verify(achievementRepository, times(1)).findAll();
    }

    @Test
    void findAllByUserId() {
        when(achievementRepository.findByUserId(anyLong())).thenReturn(achievements);

        List<AchievementEntity> result = achievementService.findAllByUserId(1L);

        assertEquals(2, result.size());
        assertEquals("Test Achievement 1", result.get(0).getName());
        assertEquals("Test Achievement 2", result.get(1).getName());
        verify(achievementRepository, times(1)).findByUserId(1L);
    }

    @Test
    void findCompletedByUserId() {
        List<AchievementEntity> completedAchievements = List.of(achievement2);
        when(achievementRepository.findByUserIdAndCompleted(anyLong(), eq(true))).thenReturn(completedAchievements);

        List<AchievementEntity> result = achievementService.findCompletedByUserId(1L);

        assertEquals(1, result.size());
        assertEquals("Test Achievement 2", result.get(0).getName());
        assertTrue(result.get(0).getCompleted());
        verify(achievementRepository, times(1)).findByUserIdAndCompleted(1L, true);
    }

    @Test
    void findIncompleteByUserId() {
        List<AchievementEntity> incompleteAchievements = List.of(achievement1);
        when(achievementRepository.findByUserIdAndCompleted(anyLong(), eq(false))).thenReturn(incompleteAchievements);

        List<AchievementEntity> result = achievementService.findIncompleteByUserId(1L);

        assertEquals(1, result.size());
        assertEquals("Test Achievement 1", result.get(0).getName());
        assertFalse(result.get(0).getCompleted());
        verify(achievementRepository, times(1)).findByUserIdAndCompleted(1L, false);
    }

    @Test
    void findById() {
        when(achievementRepository.findById(anyLong())).thenReturn(Optional.of(achievement1));

        AchievementEntity result = achievementService.findById(1L);

        assertEquals(1L, result.getId());
        assertEquals("Test Achievement 1", result.getName());
        verify(achievementRepository, times(1)).findById(1L);
    }

    @Test
    void findByIdNotFound() {
        when(achievementRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> achievementService.findById(999L));
        verify(achievementRepository, times(1)).findById(999L);
    }

    @Test
    void save() {
        when(achievementRepository.save(any(AchievementEntity.class))).thenReturn(achievement1);

        AchievementEntity result = achievementService.save(achievement1);

        assertEquals(1L, result.getId());
        assertEquals("Test Achievement 1", result.getName());
        verify(achievementRepository, times(1)).save(achievement1);
    }

    @Test
    void createAchievementWithParams() {
        AchievementEntity newAchievement = new AchievementEntity();
        newAchievement.setId(3L);
        newAchievement.setName("New Achievement");
        newAchievement.setDescription("New Description");
        newAchievement.setCriteria("New Criteria");
        newAchievement.setUserId(1L);
        newAchievement.setCompleted(false);

        when(achievementRepository.save(any(AchievementEntity.class))).thenReturn(newAchievement);

        AchievementEntity result = achievementService.createAchievement(
                "New Achievement", "New Description", "New Criteria", 1L);

        assertEquals(3L, result.getId());
        assertEquals("New Achievement", result.getName());
        assertEquals("New Description", result.getDescription());
        assertEquals("New Criteria", result.getCriteria());
        assertEquals(1L, result.getUserId());
        assertFalse(result.getCompleted());
        verify(achievementRepository, times(1)).save(any(AchievementEntity.class));
    }

    @Test
    void createAchievementWithRequest() {
        AchievementEntity newAchievement = new AchievementEntity();
        newAchievement.setId(3L);
        newAchievement.setName("New Achievement");
        newAchievement.setDescription("New Description");
        newAchievement.setCriteria("New Criteria");
        newAchievement.setUserId(1L);
        newAchievement.setCompleted(false);

        when(achievementRepository.save(any(AchievementEntity.class))).thenReturn(newAchievement);

        AchievementEntity result = achievementService.createAchievement(createRequest);

        assertEquals(3L, result.getId());
        assertEquals("New Achievement", result.getName());
        assertEquals("New Description", result.getDescription());
        assertEquals("New Criteria", result.getCriteria());
        assertEquals(1L, result.getUserId());
        assertFalse(result.getCompleted());
        verify(achievementRepository, times(1)).save(any(AchievementEntity.class));
    }

    @Test
    void completeAchievement() {
        AchievementEntity completedAchievement = new AchievementEntity();
        completedAchievement.setId(1L);
        completedAchievement.setName("Test Achievement 1");
        completedAchievement.setDescription("Test Description 1");
        completedAchievement.setCriteria("Test Criteria 1");
        completedAchievement.setUserId(1L);
        completedAchievement.setCompleted(true);

        when(achievementRepository.findById(anyLong())).thenReturn(Optional.of(achievement1));
        when(achievementRepository.save(any(AchievementEntity.class))).thenReturn(completedAchievement);

        AchievementEntity result = achievementService.completeAchievement(1L);

        assertEquals(1L, result.getId());
        assertEquals("Test Achievement 1", result.getName());
        assertTrue(result.getCompleted());
        verify(achievementRepository, times(1)).findById(1L);
        verify(achievementRepository, times(1)).save(any(AchievementEntity.class));
    }

    @Test
    void deleteById() {
        doNothing().when(achievementRepository).deleteById(anyLong());

        achievementService.deleteById(1L);

        verify(achievementRepository, times(1)).deleteById(1L);
    }
}