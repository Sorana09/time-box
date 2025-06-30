package com.example.time.box.repository;

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
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private UserEntity user1;
    private UserEntity user2;

    @BeforeEach
    void setUp() {
        // Create test users
        user1 = UserEntity.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .hashedPassword("hashedPassword1")
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .timeStudied(120L)
                .daysStreak(5)
                .todaySessions(2)
                .longestSession(60)
                .mostProductiveSubject("Math")
                .weeklyGoal(10)
                .dailyStudyTime(120)
                .avgSession(45)
                .emailVerified(true)
                .build();

        user2 = UserEntity.builder()
                .firstName("Jane")
                .lastName("Smith")
                .email("jane.smith@example.com")
                .hashedPassword("hashedPassword2")
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .timeStudied(180L)
                .daysStreak(3)
                .todaySessions(1)
                .longestSession(90)
                .mostProductiveSubject("Science")
                .weeklyGoal(8)
                .dailyStudyTime(90)
                .avgSession(60)
                .emailVerified(true)
                .build();

        // Persist users
        user1 = entityManager.persist(user1);
        user2 = entityManager.persist(user2);

        // Flush to ensure data is in the database
        entityManager.flush();
    }

    @Test
    void findAll() {
        // When
        List<UserEntity> users = userRepository.findAll();

        // Then
        assertEquals(2, users.size());
        assertTrue(users.stream().anyMatch(u -> u.getId().equals(user1.getId())));
        assertTrue(users.stream().anyMatch(u -> u.getId().equals(user2.getId())));
    }

    @Test
    void findByEmail() {
        // When
        Optional<UserEntity> foundUser = userRepository.findByEmail("john.doe@example.com");

        // Then
        assertTrue(foundUser.isPresent());
        assertEquals(user1.getId(), foundUser.get().getId());
        assertEquals("John", foundUser.get().getFirstName());
        assertEquals("Doe", foundUser.get().getLastName());
    }

    @Test
    void findByEmailNotFound() {
        // When
        Optional<UserEntity> foundUser = userRepository.findByEmail("nonexistent@example.com");

        // Then
        assertFalse(foundUser.isPresent());
    }

    @Test
    void findById() {
        // When
        Optional<UserEntity> foundUser = userRepository.findById(user1.getId());

        // Then
        assertTrue(foundUser.isPresent());
        assertEquals(user1.getId(), foundUser.get().getId());
        assertEquals("John", foundUser.get().getFirstName());
        assertEquals("Doe", foundUser.get().getLastName());
    }

    @Test
    void findByIdNotFound() {
        // When
        Optional<UserEntity> foundUser = userRepository.findById(999L);

        // Then
        assertFalse(foundUser.isPresent());
    }

    @Test
    void save() {
        // Given
        UserEntity newUser = UserEntity.builder()
                .firstName("New")
                .lastName("User")
                .email("new.user@example.com")
                .hashedPassword("hashedPassword3")
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .emailVerified(false)
                .build();

        // When
        UserEntity savedUser = userRepository.save(newUser);
        entityManager.flush();

        // Then
        assertNotNull(savedUser.getId());
        assertEquals("New", savedUser.getFirstName());
        assertEquals("User", savedUser.getLastName());
        assertEquals("new.user@example.com", savedUser.getEmail());

        // Verify it's in the database
        Optional<UserEntity> foundUser = userRepository.findById(savedUser.getId());
        assertTrue(foundUser.isPresent());
        assertEquals(savedUser.getId(), foundUser.get().getId());
    }

    @Test
    void deleteById() {
        // When
        userRepository.deleteById(user1.getId());
        entityManager.flush();

        // Then
        Optional<UserEntity> deletedUser = userRepository.findById(user1.getId());
        assertFalse(deletedUser.isPresent());

        // Verify other user still exists
        Optional<UserEntity> otherUser = userRepository.findById(user2.getId());
        assertTrue(otherUser.isPresent());
    }
}