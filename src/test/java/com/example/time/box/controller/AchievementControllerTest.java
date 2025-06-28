package com.example.time.box.controller;

import com.example.time.box.domain.AchievementDto;
import com.example.time.box.domain.mapper.Mapper;
import com.example.time.box.entity.AchievementEntity;
import com.example.time.box.entity.request.AchievementCreateRequest;
import com.example.time.box.security.TestSecurityConfig;
import com.example.time.box.service.AchievementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AchievementController.class)
@Import({AchievementControllerTest.TestConfig.class, TestSecurityConfig.class})
class AchievementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AchievementService achievementService;

    @Autowired
    private ObjectMapper objectMapper;

    @Configuration
    static class TestConfig {
        @Bean
        public AchievementService achievementService() {
            return mock(AchievementService.class);
        }

        @Bean
        public AchievementController achievementController(AchievementService achievementService) {
            return new AchievementController(achievementService);
        }
    }

    private AchievementEntity achievementEntity;
    private List<AchievementEntity> achievementEntities;
    private AchievementCreateRequest createRequest;

    @BeforeEach
    void setUp() {
        achievementEntity = new AchievementEntity();
        achievementEntity.setId(1L);
        achievementEntity.setName("Test Achievement");
        achievementEntity.setDescription("Test Description");
        achievementEntity.setCompleted(false);
        achievementEntity.setUserId(1L);

        AchievementEntity achievementEntity2 = new AchievementEntity();
        achievementEntity2.setId(2L);
        achievementEntity2.setName("Test Achievement 2");
        achievementEntity2.setDescription("Test Description 2");
        achievementEntity2.setCompleted(true);
        achievementEntity2.setUserId(1L);

        achievementEntities = Arrays.asList(achievementEntity, achievementEntity2);

        createRequest = new AchievementCreateRequest();
        createRequest.setName("New Achievement");
        createRequest.setDescription("New Description");
        createRequest.setUserId(1L);
    }

    @Test
    void getAllAchievements() throws Exception {
        when(achievementService.findAll()).thenReturn(achievementEntities);

        mockMvc.perform(get("/achievements"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Test Achievement"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Test Achievement 2"));

        verify(achievementService, times(1)).findAll();
    }

    @Test
    void getAllAchievementsByUserId() throws Exception {
        when(achievementService.findAllByUserId(anyLong())).thenReturn(achievementEntities);

        mockMvc.perform(get("/achievements/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Test Achievement"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Test Achievement 2"));

        verify(achievementService, times(1)).findAllByUserId(1L);
    }

    @Test
    void getCompletedAchievementsByUserId() throws Exception {
        List<AchievementEntity> completedAchievements = List.of(achievementEntities.get(1));
        when(achievementService.findCompletedByUserId(anyLong())).thenReturn(completedAchievements);

        mockMvc.perform(get("/achievements/1/completed"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(jsonPath("$[0].name").value("Test Achievement 2"));

        verify(achievementService, times(1)).findCompletedByUserId(1L);
    }

    @Test
    void getIncompleteAchievementsByUserId() throws Exception {
        List<AchievementEntity> incompleteAchievements = List.of(achievementEntities.get(0));
        when(achievementService.findIncompleteByUserId(anyLong())).thenReturn(incompleteAchievements);

        mockMvc.perform(get("/achievements/1/incomplete"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Test Achievement"));

        verify(achievementService, times(1)).findIncompleteByUserId(1L);
    }

    @Test
    void createAchievement() throws Exception {
        when(achievementService.createAchievement(any(AchievementCreateRequest.class))).thenReturn(achievementEntity);

        mockMvc.perform(post("/achievements")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Achievement"));

        verify(achievementService, times(1)).createAchievement(any(AchievementCreateRequest.class));
    }

    @Test
    void completeAchievement() throws Exception {
        AchievementEntity completedAchievement = new AchievementEntity();
        completedAchievement.setId(1L);
        completedAchievement.setName("Test Achievement");
        completedAchievement.setDescription("Test Description");
        completedAchievement.setCompleted(true);
        completedAchievement.setUserId(1L);

        when(achievementService.completeAchievement(anyLong())).thenReturn(completedAchievement);

        mockMvc.perform(put("/achievements/1/complete"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Achievement"))
                .andExpect(jsonPath("$.completed").value(true));

        verify(achievementService, times(1)).completeAchievement(1L);
    }

    @Test
    void deleteAchievement() throws Exception {
        doNothing().when(achievementService).deleteById(anyLong());

        mockMvc.perform(delete("/achievements/1"))
                .andExpect(status().isOk());

        verify(achievementService, times(1)).deleteById(1L);
    }
}
