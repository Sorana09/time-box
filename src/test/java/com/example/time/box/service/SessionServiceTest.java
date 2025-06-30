package com.example.time.box.service;

import com.example.time.box.entity.SessionEntity;
import com.example.time.box.entity.UserEntity;
import com.example.time.box.entity.request.LoginRequest;
import com.example.time.box.exception.EntityNotFoundException;
import com.example.time.box.exception.IncorrectPasswordException;
import com.example.time.box.exception.PasswordIsNullException;
import com.example.time.box.exception.TooManySessionsException;
import com.example.time.box.repository.SessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private SessionService sessionService;

    private SessionEntity activeSession;
    private SessionEntity expiredSession;
    private List<SessionEntity> sessions;
    private UserEntity user;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        activeSession = new SessionEntity();
        activeSession.setId(1);
        activeSession.setSessionKey("active-session-key");
        activeSession.setUserId(1L);
        activeSession.setExpiredAt(OffsetDateTime.now(ZoneOffset.UTC).plusDays(1));

        expiredSession = new SessionEntity();
        expiredSession.setId(2);
        expiredSession.setSessionKey("expired-session-key");
        expiredSession.setUserId(1L);
        expiredSession.setExpiredAt(OffsetDateTime.now(ZoneOffset.UTC).minusDays(1));

        sessions = Arrays.asList(activeSession, expiredSession);

        user = new UserEntity();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setHashedPassword("hashedPassword");

        loginRequest = LoginRequest.builder()
                .email("test@example.com")
                .password("password")
                .build();
    }

    @Test
    void findAllSessions() {
        when(sessionRepository.findByUserId(anyLong())).thenReturn(sessions);

        List<SessionEntity> result = sessionService.find(1L, false);

        assertEquals(2, result.size());
        assertEquals("active-session-key", result.get(0).getSessionKey());
        assertEquals("expired-session-key", result.get(1).getSessionKey());
        verify(sessionRepository, times(1)).findByUserId(1L);
    }

    @Test
    void findActiveSessions() {
        when(sessionRepository.findByUserId(anyLong())).thenReturn(sessions);

        List<SessionEntity> result = sessionService.find(1L, true);

        assertEquals(1, result.size());
        assertEquals("active-session-key", result.get(0).getSessionKey());
        verify(sessionRepository, times(1)).findByUserId(1L);
    }

    @Test
    void isExpired() {
        assertFalse(SessionService.isExpired(activeSession));
        assertTrue(SessionService.isExpired(expiredSession));
    }

    @Test
    void getActiveSessions() {
        when(sessionRepository.findAll()).thenReturn(sessions);

        List<SessionEntity> result = sessionService.getActiveSessions();

        assertEquals(1, result.size());
        assertEquals("active-session-key", result.get(0).getSessionKey());
        verify(sessionRepository, times(1)).findAll();
    }

    @Test
    void createSession() {
        when(userService.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(userService.verifyPassword(anyLong(), anyString())).thenReturn(true);
        when(sessionRepository.findByUserId(anyLong())).thenReturn(List.of(activeSession));
        when(sessionRepository.save(any(SessionEntity.class))).thenAnswer(invocation -> {
            SessionEntity session = invocation.getArgument(0);
            session.setId(3);
            return session;
        });

        Optional<SessionEntity> result = sessionService.createSession(loginRequest);

        assertTrue(result.isPresent());
        assertEquals(3, result.get().getId());
        assertEquals(1L, result.get().getUserId());
        assertNotNull(result.get().getSessionKey());
        assertNotNull(result.get().getExpiredAt());
        verify(userService, times(1)).findByEmail("test@example.com");
        verify(userService, times(1)).verifyPassword(1L, "password");
        verify(sessionRepository, times(1)).findByUserId(1L);
        verify(sessionRepository, times(1)).save(any(SessionEntity.class));
    }

    @Test
    void createSessionUserNotFound() {
        when(userService.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> sessionService.createSession(loginRequest));
        verify(userService, times(1)).findByEmail("test@example.com");
        verify(userService, never()).verifyPassword(anyLong(), anyString());
        verify(sessionRepository, never()).save(any(SessionEntity.class));
    }

    @Test
    void createSessionNullPassword() {
        loginRequest.setPassword(null);
        when(userService.findByEmail(anyString())).thenReturn(Optional.of(user));

        assertThrows(PasswordIsNullException.class, () -> sessionService.createSession(loginRequest));
        verify(userService, times(1)).findByEmail("test@example.com");
        verify(userService, never()).verifyPassword(anyLong(), anyString());
        verify(sessionRepository, never()).save(any(SessionEntity.class));
    }

    @Test
    void createSessionIncorrectPassword() {
        when(userService.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(userService.verifyPassword(anyLong(), anyString())).thenReturn(false);

        assertThrows(IncorrectPasswordException.class, () -> sessionService.createSession(loginRequest));
        verify(userService, times(1)).findByEmail("test@example.com");
        verify(userService, times(1)).verifyPassword(1L, "password");
        verify(sessionRepository, never()).save(any(SessionEntity.class));
    }

    @Test
    void createSessionTooManySessions() {
        when(userService.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(userService.verifyPassword(anyLong(), anyString())).thenReturn(true);

        SessionEntity session1 = new SessionEntity();
        session1.setId(1);
        session1.setSessionKey("session-key-1");
        session1.setUserId(1L);
        session1.setExpiredAt(OffsetDateTime.now(ZoneOffset.UTC).plusDays(1));

        SessionEntity session2 = new SessionEntity();
        session2.setId(2);
        session2.setSessionKey("session-key-2");
        session2.setUserId(1L);
        session2.setExpiredAt(OffsetDateTime.now(ZoneOffset.UTC).plusDays(1));

        SessionEntity session3 = new SessionEntity();
        session3.setId(3);
        session3.setSessionKey("session-key-3");
        session3.setUserId(1L);
        session3.setExpiredAt(OffsetDateTime.now(ZoneOffset.UTC).plusDays(1));

        when(sessionRepository.findByUserId(anyLong())).thenReturn(Arrays.asList(session1, session2, session3));

        assertThrows(TooManySessionsException.class, () -> sessionService.createSession(loginRequest));
        verify(userService, times(1)).findByEmail("test@example.com");
        verify(userService, times(1)).verifyPassword(1L, "password");
        verify(sessionRepository, times(1)).findByUserId(1L);
        verify(sessionRepository, never()).save(any(SessionEntity.class));
    }

    @Test
    void getSessionBySessionKey() {
        when(sessionRepository.findBySessionKey(anyString())).thenReturn(Optional.of(activeSession));

        Optional<SessionEntity> result = sessionService.getSessionBySessionKey("active-session-key");

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
        assertEquals("active-session-key", result.get().getSessionKey());
        verify(sessionRepository, times(1)).findBySessionKey("active-session-key");
    }

    @Test
    void getSessionBySessionKeyNotFound() {
        when(sessionRepository.findBySessionKey(anyString())).thenReturn(Optional.empty());

        Optional<SessionEntity> result = sessionService.getSessionBySessionKey("non-existent-key");

        assertFalse(result.isPresent());
        verify(sessionRepository, times(1)).findBySessionKey("non-existent-key");
    }

    @Test
    void deleteSessionById() {
        doNothing().when(sessionRepository).deleteById(anyLong());

        sessionService.deleteSessionById(1L);

        verify(sessionRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteSessionBySessionKey() {
        doNothing().when(sessionRepository).deleteBySessionKey(anyString());

        sessionService.deleteSessionBySessionKey("active-session-key");

        verify(sessionRepository, times(1)).deleteBySessionKey("active-session-key");
    }
}
