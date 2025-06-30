package com.example.time.box.controller;

import com.example.time.box.domain.SubjectSessionDto;
import com.example.time.box.entity.SubjectSession;
import com.example.time.box.entity.request.SubjectSessionRequest;
import com.example.time.box.security.TestSecurityConfig;
import com.example.time.box.service.SubjectSessionService;
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

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SubjectSessionController.class)
@Import({SubjectSessionControllerTest.TestConfig.class, TestSecurityConfig.class})
class SubjectSessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SubjectSessionService subjectSessionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Configuration
    static class TestConfig {
        @Bean
        public SubjectSessionService subjectSessionService() {
            return mock(SubjectSessionService.class);
        }

        @Bean
        public SubjectSessionController subjectSessionController(SubjectSessionService subjectSessionService) {
            return new SubjectSessionController(subjectSessionService);
        }
    }

    private SubjectSession subjectSession;
    private List<SubjectSession> subjectSessions;
    private SubjectSessionRequest subjectSessionRequest;

    @BeforeEach
    void setUp() {
        subjectSession = SubjectSession.builder()
                .id(1L)
                .subjectId(1L)
                .startTime(OffsetDateTime.now())
                .endTime(OffsetDateTime.now().plusHours(1))
                .running(true)
                .timeAllotted(60L)
                .build();

        SubjectSession subjectSession2 = SubjectSession.builder()
                .id(2L)
                .subjectId(2L)
                .startTime(OffsetDateTime.now())
                .endTime(OffsetDateTime.now().plusHours(2))
                .running(false)
                .timeAllotted(120L)
                .build();

        subjectSessions = Arrays.asList(subjectSession, subjectSession2);

        subjectSessionRequest = SubjectSessionRequest.builder()
                .subjectId(1L)
                .build();
    }

    @Test
    void getAllSubjectSessions() throws Exception {
        when(subjectSessionService.getAllSubjectSessions()).thenReturn(subjectSessions);

        mockMvc.perform(get("/subject-session"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].subjectId").value(1))
                .andExpect(jsonPath("$[0].running").value(true))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].subjectId").value(2))
                .andExpect(jsonPath("$[1].running").value(false));

        verify(subjectSessionService, times(1)).getAllSubjectSessions();
    }

    @Test
    void createSubjectSession() throws Exception {
        when(subjectSessionService.saveSubjectSession(any(SubjectSessionRequest.class))).thenReturn(subjectSession);

        mockMvc.perform(post("/subject-session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subjectSessionRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.subjectId").value(1))
                .andExpect(jsonPath("$.running").value(true));

        verify(subjectSessionService, times(1)).saveSubjectSession(any(SubjectSessionRequest.class));
    }

    @Test
    void pauseSession() throws Exception {
        doNothing().when(subjectSessionService).pauseSession(anyLong());

        mockMvc.perform(put("/subject-session/1/pause"))
                .andExpect(status().isOk());

        verify(subjectSessionService, times(1)).pauseSession(1L);
    }

    @Test
    void restartSession() throws Exception {
        doNothing().when(subjectSessionService).restartSession(anyLong());

        mockMvc.perform(put("/subject-session/1/restart"))
                .andExpect(status().isOk());

        verify(subjectSessionService, times(1)).restartSession(1L);
    }

    @Test
    void setStartTime() throws Exception {
        doNothing().when(subjectSessionService).setStartTime(anyLong());

        mockMvc.perform(put("/subject-session/1/start"))
                .andExpect(status().isOk());

        verify(subjectSessionService, times(1)).setStartTime(1L);
    }

    @Test
    void setEndTime() throws Exception {
        doNothing().when(subjectSessionService).setEndTime(anyLong());

        mockMvc.perform(put("/subject-session/1/end"))
                .andExpect(status().isOk());

        verify(subjectSessionService, times(1)).setEndTime(1L);
    }

    @Test
    void getDurationTime() throws Exception {
        when(subjectSessionService.durationForAnSessions(anyLong())).thenReturn(3600L);

        mockMvc.perform(get("/subject-session/duration/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("3600"));

        verify(subjectSessionService, times(1)).durationForAnSessions(1L);
    }

    @Test
    void deleteSubjectSession() throws Exception {
        doNothing().when(subjectSessionService).deleteSubjectSession(anyLong());

        mockMvc.perform(delete("/subject-session/1"))
                .andExpect(status().isOk());

        verify(subjectSessionService, times(1)).deleteSubjectSession(1L);
    }
}
