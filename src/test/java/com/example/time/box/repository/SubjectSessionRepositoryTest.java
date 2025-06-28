package com.example.time.box.repository;

import com.example.time.box.entity.SubjectEntity;
import com.example.time.box.entity.SubjectSession;
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
class SubjectSessionRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SubjectSessionRepository subjectSessionRepository;

    private UserEntity user;
    private SubjectEntity subject;
    private SubjectSession session1;
    private SubjectSession session2;

    @BeforeEach
    void setUp() {
        // Create test user
        user = UserEntity.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .hashedPassword("hashedPassword")
                .emailVerified(true)
                .build();
        
        // Persist user
        user = entityManager.persist(user);
        
        // Create test subject
        subject = SubjectEntity.builder()
                .name("Math")
                .description("Mathematics")
                .timeAllotted(120L)
                .numberOfSessions(2)
                .userId(user.getId())
                .build();
        
        // Persist subject
        subject = entityManager.persist(subject);
        
        // Create test sessions
        session1 = SubjectSession.builder()
                .subjectId(subject.getId())
                .startTime(OffsetDateTime.now().minusHours(2))
                .endTime(OffsetDateTime.now().minusHours(1))
                .running(false)
                .timeAllotted(3600L) // 1 hour in seconds
                .build();
        
        session2 = SubjectSession.builder()
                .subjectId(subject.getId())
                .startTime(OffsetDateTime.now().minusMinutes(30))
                .endTime(null)
                .running(true)
                .timeAllotted(0L)
                .build();
        
        // Persist sessions
        session1 = entityManager.persist(session1);
        session2 = entityManager.persist(session2);
        
        // Flush to ensure data is in the database
        entityManager.flush();
    }

    @Test
    void findById() {
        // When
        Optional<SubjectSession> foundSession = subjectSessionRepository.findById(session1.getId());
        
        // Then
        assertTrue(foundSession.isPresent());
        assertEquals(session1.getId(), foundSession.get().getId());
        assertEquals(subject.getId(), foundSession.get().getSubjectId());
        assertEquals(3600L, foundSession.get().getTimeAllotted());
    }

    @Test
    void findAll() {
        // When
        List<SubjectSession> sessions = subjectSessionRepository.findAll();
        
        // Then
        assertEquals(2, sessions.size());
        assertTrue(sessions.stream().anyMatch(s -> s.getId().equals(session1.getId())));
        assertTrue(sessions.stream().anyMatch(s -> s.getId().equals(session2.getId())));
    }

    @Test
    void save() {
        // Given
        SubjectSession newSession = SubjectSession.builder()
                .subjectId(subject.getId())
                .startTime(OffsetDateTime.now())
                .endTime(OffsetDateTime.now().plusHours(1))
                .running(false)
                .timeAllotted(3600L)
                .build();
        
        // When
        SubjectSession savedSession = subjectSessionRepository.save(newSession);
        entityManager.flush();
        
        // Then
        assertNotNull(savedSession.getId());
        assertEquals(subject.getId(), savedSession.getSubjectId());
        assertEquals(3600L, savedSession.getTimeAllotted());
        
        // Verify it's in the database
        Optional<SubjectSession> foundSession = subjectSessionRepository.findById(savedSession.getId());
        assertTrue(foundSession.isPresent());
        assertEquals(savedSession.getId(), foundSession.get().getId());
    }

    @Test
    void deleteById() {
        // When
        subjectSessionRepository.deleteById(session1.getId());
        entityManager.flush();
        
        // Then
        Optional<SubjectSession> deletedSession = subjectSessionRepository.findById(session1.getId());
        assertFalse(deletedSession.isPresent());
        
        // Verify other session still exists
        Optional<SubjectSession> otherSession = subjectSessionRepository.findById(session2.getId());
        assertTrue(otherSession.isPresent());
    }
}