package com.example.time.box.repository;

import com.example.time.box.entity.SubjectEntity;
import com.example.time.box.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class SubjectRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SubjectRepository subjectRepository;

    private UserEntity user1;
    private UserEntity user2;
    private SubjectEntity subject1;
    private SubjectEntity subject2;
    private SubjectEntity subject3;

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
        
        // Create test subjects
        subject1 = SubjectEntity.builder()
                .name("Math")
                .description("Mathematics")
                .timeAllotted(120L)
                .numberOfSessions(5)
                .userId(user1.getId())
                .build();
        
        subject2 = SubjectEntity.builder()
                .name("Science")
                .description("Science studies")
                .timeAllotted(180L)
                .numberOfSessions(3)
                .userId(user1.getId())
                .build();
        
        subject3 = SubjectEntity.builder()
                .name("History")
                .description("History studies")
                .timeAllotted(90L)
                .numberOfSessions(2)
                .userId(user2.getId())
                .build();
        
        // Persist subjects
        entityManager.persist(subject1);
        entityManager.persist(subject2);
        entityManager.persist(subject3);
        
        // Flush to ensure data is in the database
        entityManager.flush();
    }

    @Test
    void findByUserId() {
        // When
        List<SubjectEntity> subjects = subjectRepository.findByUserId(user1.getId());
        
        // Then
        assertEquals(2, subjects.size());
        assertTrue(subjects.stream().anyMatch(s -> s.getName().equals("Math")));
        assertTrue(subjects.stream().anyMatch(s -> s.getName().equals("Science")));
    }

    @Test
    void findAllByUserId() {
        // When
        List<SubjectEntity> subjects = subjectRepository.findAllByUserId(user1.getId());
        
        // Then
        assertEquals(2, subjects.size());
        assertTrue(subjects.stream().anyMatch(s -> s.getName().equals("Math")));
        assertTrue(subjects.stream().anyMatch(s -> s.getName().equals("Science")));
    }

    @Test
    void deleteById() {
        // When
        subjectRepository.deleteById(subject1.getId());
        entityManager.flush();
        
        // Then
        List<SubjectEntity> subjects = subjectRepository.findAll();
        assertEquals(2, subjects.size());
        assertFalse(subjects.stream().anyMatch(s -> s.getId().equals(subject1.getId())));
    }
}