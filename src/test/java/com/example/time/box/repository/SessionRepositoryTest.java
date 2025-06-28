package com.example.time.box.repository;

import com.example.time.box.entity.SessionEntity;
import com.example.time.box.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class SessionRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SessionRepository sessionRepository;

    private UserEntity user1;
    private UserEntity user2;
    private SessionEntity session1;
    private SessionEntity session2;
    private SessionEntity session3;

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
        
        // Create test sessions
        session1 = SessionEntity.builder()
                .sessionKey("session-key-1")
                .expiredAt(OffsetDateTime.now().plusHours(1))
                .userId(user1.getId())
                .build();
        
        session2 = SessionEntity.builder()
                .sessionKey("session-key-2")
                .expiredAt(OffsetDateTime.now().plusHours(2))
                .userId(user1.getId())
                .build();
        
        session3 = SessionEntity.builder()
                .sessionKey("session-key-3")
                .expiredAt(OffsetDateTime.now().plusHours(3))
                .userId(user2.getId())
                .build();
        
        // Persist sessions
        entityManager.persist(session1);
        entityManager.persist(session2);
        entityManager.persist(session3);
        
        // Flush to ensure data is in the database
        entityManager.flush();
    }

    @Test
    void findByUserId() {
        // When
        List<SessionEntity> sessions = sessionRepository.findByUserId(user1.getId());
        
        // Then
        assertEquals(2, sessions.size());
        assertTrue(sessions.stream().anyMatch(s -> s.getSessionKey().equals("session-key-1")));
        assertTrue(sessions.stream().anyMatch(s -> s.getSessionKey().equals("session-key-2")));
    }

    @Test
    void findBySessionKey() {
        // When
        Optional<SessionEntity> session = sessionRepository.findBySessionKey("session-key-1");
        
        // Then
        assertTrue(session.isPresent());
        assertEquals("session-key-1", session.get().getSessionKey());
        assertEquals(user1.getId(), session.get().getUserId());
    }

    @Test
    void findBySessionKeyNotFound() {
        // When
        Optional<SessionEntity> session = sessionRepository.findBySessionKey("non-existent-key");
        
        // Then
        assertFalse(session.isPresent());
    }

    @Test
    void deleteById() {
        // When
        sessionRepository.deleteById(session1.getId().longValue());
        entityManager.flush();
        
        // Then
        List<SessionEntity> sessions = sessionRepository.findAll();
        assertEquals(2, sessions.size());
        assertFalse(sessions.stream().anyMatch(s -> s.getId().equals(session1.getId())));
    }

    @Test
    void deleteBySessionKey() {
        // When
        sessionRepository.deleteBySessionKey("session-key-1");
        entityManager.flush();
        
        // Then
        List<SessionEntity> sessions = sessionRepository.findAll();
        assertEquals(2, sessions.size());
        assertFalse(sessions.stream().anyMatch(s -> s.getSessionKey().equals("session-key-1")));
    }
}