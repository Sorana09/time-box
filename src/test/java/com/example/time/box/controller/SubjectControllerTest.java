package com.example.time.box.controller;

import com.example.time.box.entity.SubjectEntity;
import com.example.time.box.entity.request.SubjectCreateRequest;
import com.example.time.box.security.TestSecurityConfig;
import com.example.time.box.service.SubjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SubjectController.class)
@Import({SubjectControllerTest.TestConfig.class, TestSecurityConfig.class})
class SubjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private ObjectMapper objectMapper;
    private SubjectEntity subjectEntity;
    private List<SubjectEntity> subjectEntities;
    private SubjectCreateRequest createRequest;

    @BeforeEach
    void setUp() {
        subjectEntity = SubjectEntity.builder()
                .id(1L)
                .name("Test Subject")
                .description("Test Description")
                .timeAllotted(120L)
                .numberOfSessions(5)
                .userId(1L)
                .build();

        SubjectEntity subjectEntity2 = SubjectEntity.builder()
                .id(2L)
                .name("Test Subject 2")
                .description("Test Description 2")
                .timeAllotted(180L)
                .numberOfSessions(3)
                .userId(1L)
                .build();

        subjectEntities = Arrays.asList(subjectEntity, subjectEntity2);

        createRequest = SubjectCreateRequest.builder()
                .userId(1L)
                .name("New Subject")
                .description("New Description")
                .build();
    }

    @Test
    void getAllSubjects() throws Exception {
        when(subjectService.findAll()).thenReturn(subjectEntities);

        mockMvc.perform(get("/subjects"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Test Subject"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Test Subject 2"));

        verify(subjectService, times(1)).findAll();
    }

    @Test
    void getAllSubjectByUserId() throws Exception {
        when(subjectService.findAllByUserId(anyLong())).thenReturn(subjectEntities);

        mockMvc.perform(get("/subjects/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Test Subject"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Test Subject 2"));

        verify(subjectService, times(1)).findAllByUserId(1L);
    }

    @Test
    void changeDescription() throws Exception {
        when(subjectService.changeDescription(anyLong(), anyString())).thenReturn("Updated Description");

        mockMvc.perform(put("/subjects/1/description")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("Updated Description"))
                .andExpect(status().isOk())
                .andExpect(content().string("Updated Description"));

        verify(subjectService, times(1)).changeDescription(1L, "Updated Description");
    }

    @Test
    void getNumberOfSessions() throws Exception {
        when(subjectService.getNumberOfSessionsForAnSubject(anyLong())).thenReturn(5);

        mockMvc.perform(get("/subjects/number-of-sessions/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));

        verify(subjectService, times(1)).getNumberOfSessionsForAnSubject(1L);
    }

    @Test
    void getTotalTimeAllotedForAnSubject() throws Exception {
        when(subjectService.timeAllotedForAnSubject(anyLong())).thenReturn(120L);

        mockMvc.perform(get("/subjects/total-time-allotted/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("120"));

        verify(subjectService, times(1)).timeAllotedForAnSubject(1L);
    }

    @Test
    void createSubject() throws Exception {
        when(subjectService.save(any(SubjectCreateRequest.class))).thenReturn(subjectEntity);

        mockMvc.perform(post("/subjects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Subject"));

        verify(subjectService, times(1)).save(any(SubjectCreateRequest.class));
    }

    @Test
    void deleteSubject() throws Exception {
        doNothing().when(subjectService).deleteById(anyLong());

        mockMvc.perform(delete("/subjects/1"))
                .andExpect(status().isOk());

        verify(subjectService, times(1)).deleteById(1L);
    }

    @Configuration
    static class TestConfig {
        @Bean
        public SubjectService subjectService() {
            return mock(SubjectService.class);
        }

        @Bean
        public SubjectController subjectController(SubjectService subjectService) {
            return new SubjectController(subjectService);
        }
    }
}
