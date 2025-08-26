package com.example.time.box.controller;

import com.example.time.box.entity.SessionEntity;
import com.example.time.box.entity.request.LoginRequest;
import com.example.time.box.security.TestSecurityConfig;
import com.example.time.box.service.SessionService;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SessionController.class)
@Import({SessionControllerTest.TestConfig.class, TestSecurityConfig.class})
class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private ObjectMapper objectMapper;
    private SessionEntity sessionEntity;
    private List<SessionEntity> sessionEntities;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        sessionEntity = SessionEntity.builder()
                .id(1)
                .sessionKey("test-session-key")
                .userId(1L)
                .expiredAt(OffsetDateTime.now().plusHours(1))
                .build();

        SessionEntity sessionEntity2 = SessionEntity.builder()
                .id(2)
                .sessionKey("test-session-key-2")
                .userId(2L)
                .expiredAt(OffsetDateTime.now().plusHours(1))
                .build();

        sessionEntities = Arrays.asList(sessionEntity, sessionEntity2);

        loginRequest = LoginRequest.builder()
                .email("testuser@example.com")
                .password("password")
                .build();
    }

    @Test
    void getSession() throws Exception {
        when(sessionService.getSessionBySessionKey(anyString())).thenReturn(Optional.of(sessionEntity));

        mockMvc.perform(get("/sessions/test-session-key"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.sessionKey").value("test-session-key"));

        verify(sessionService, times(1)).getSessionBySessionKey("test-session-key");
    }

    @Test
    void getSessionNotFound() throws Exception {
        when(sessionService.getSessionBySessionKey(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(get("/sessions/non-existent-key"))
                .andExpect(status().isNotFound());

        verify(sessionService, times(1)).getSessionBySessionKey("non-existent-key");
    }

    @Test
    void createSession() throws Exception {
        // Reset any previous interactions with the mock
        reset(sessionService);

        when(sessionService.createSession(any(LoginRequest.class))).thenReturn(Optional.of(sessionEntity));

        mockMvc.perform(post("/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.sessionKey").value("test-session-key"));

        // Verify that createSession was called exactly once
        verify(sessionService, times(1)).createSession(any(LoginRequest.class));
    }

    @Test
    void createSessionBadRequest() throws Exception {
        when(sessionService.createSession(argThat(request ->
                request.getEmail().equals("testuser@example.com") &&
                        request.getPassword().equals("password")
        ))).thenReturn(Optional.empty());

        mockMvc.perform(post("/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest());

        verify(sessionService, times(1)).createSession(argThat(request ->
                request.getEmail().equals("testuser@example.com") &&
                        request.getPassword().equals("password")
        ));
    }

    @Test
    void deleteSession() throws Exception {
        doNothing().when(sessionService).deleteSessionBySessionKey(anyString());

        mockMvc.perform(delete("/sessions")
                        .param("key", "test-session-key"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(sessionService, times(1)).deleteSessionBySessionKey("test-session-key");
    }

    @Test
    void getActiveSessions() throws Exception {
        when(sessionService.getActiveSessions()).thenReturn(sessionEntities);

        mockMvc.perform(get("/sessions/active"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].sessionKey").value("test-session-key"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].sessionKey").value("test-session-key-2"));

        verify(sessionService, times(1)).getActiveSessions();
    }

    @Configuration
    static class TestConfig {
        @Bean
        public SessionService sessionService() {
            return mock(SessionService.class);
        }

        @Bean
        public SessionController sessionController(SessionService sessionService) {
            return new SessionController(sessionService);
        }
    }
}
