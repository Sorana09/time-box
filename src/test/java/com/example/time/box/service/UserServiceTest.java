package com.example.time.box.service;

import com.example.time.box.entity.SubjectEntity;
import com.example.time.box.entity.SubjectSession;
import com.example.time.box.entity.UserEntity;
import com.example.time.box.entity.request.UserSignUpRequest;
import com.example.time.box.exception.EntityNotFoundException;
import com.example.time.box.exception.PasswordIsNullException;
import com.example.time.box.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SubjectService subjectService;

    @Mock
    private SubjectSessionService subjectSessionService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private UserEntity user;
    private List<UserEntity> users;
    private SubjectEntity subject1;
    private SubjectEntity subject2;
    private List<SubjectEntity> subjects;
    private SubjectSession session1;
    private SubjectSession session2;
    private List<SubjectSession> sessions;
    private UserSignUpRequest signUpRequest;

    @BeforeEach
    void setUp() {
        user = UserEntity.builder()
            .id(1L)
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@example.com")
            .hashedPassword("hashedPassword")
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

        UserEntity user2 = UserEntity.builder()
            .id(2L)
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

        users = Arrays.asList(user, user2);

        subject1 = SubjectEntity.builder()
            .id(1L)
            .name("Math")
            .description("Mathematics")
            .timeAllotted(120L)
            .numberOfSessions(2)
            .userId(1L)
            .build();

        subject2 = SubjectEntity.builder()
            .id(2L)
            .name("Science")
            .description("Science studies")
            .timeAllotted(180L)
            .numberOfSessions(3)
            .userId(1L)
            .build();

        subjects = Arrays.asList(subject1, subject2);

        session1 = SubjectSession.builder()
            .id(1L)
            .subjectId(1L)
            .startTime(OffsetDateTime.now().minusDays(1))
            .endTime(OffsetDateTime.now().minusDays(1).plusHours(1))
            .running(false)
            .timeAllotted(3600L) // 1 hour in seconds
            .build();

        session2 = SubjectSession.builder()
            .id(2L)
            .subjectId(2L)
            .startTime(OffsetDateTime.now())
            .endTime(OffsetDateTime.now().plusHours(2))
            .running(false)
            .timeAllotted(7200L) // 2 hours in seconds
            .build();

        sessions = Arrays.asList(session1, session2);

        signUpRequest = UserSignUpRequest.builder()
            .firstName("New")
            .lastName("User")
            .email("new.user@example.com")
            .password("password")
            .build();
    }

    @Test
    void findByEmail() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        Optional<UserEntity> result = userService.findByEmail("john.doe@example.com");

        assertTrue(result.isPresent());
        assertEquals("John", result.get().getFirstName());
        assertEquals("Doe", result.get().getLastName());
        verify(userRepository, times(1)).findByEmail("john.doe@example.com");
    }

    @Test
    void verifyPassword() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        Boolean result = userService.verifyPassword(1L, "password");

        assertTrue(result);
        verify(userRepository, times(1)).findById(1L);
        verify(passwordEncoder, times(1)).matches("password", "hashedPassword");
    }

    @Test
    void verifyPasswordNullPassword() {
        assertThrows(PasswordIsNullException.class, () -> userService.verifyPassword(1L, null));
    }

    @Test
    void signUp() {
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> {
            UserEntity savedUser = invocation.getArgument(0);
            savedUser.setId(3L);
            return savedUser;
        });

        UserEntity result = userService.signUp(signUpRequest);

        assertEquals(3L, result.getId());
        assertEquals("New", result.getFirstName());
        assertEquals("User", result.getLastName());
        assertEquals("new.user@example.com", result.getEmail());
        assertEquals("encodedPassword", result.getHashedPassword());
        assertFalse(result.getEmailVerified());
        verify(passwordEncoder, times(1)).encode("password");
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void getAllSubjectSesionsForAnUser() {
        when(subjectService.findAllByUserId(anyLong())).thenReturn(subjects);
        when(subjectSessionService.getAllSubjectSessions()).thenReturn(sessions);

        List<SubjectSession> result = userService.getAllSubjectSesionsForAnUser(1L);

        assertEquals(2, result.size());
        verify(subjectService, times(1)).findAllByUserId(1L);
        verify(subjectSessionService, times(2)).getAllSubjectSessions();
    }

    @Test
    void save() {
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);

        userService.save(user);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void daysStreak() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(subjectService.findAllByUserId(anyLong())).thenReturn(subjects);
        when(subjectSessionService.getAllSubjectSessions()).thenReturn(sessions);
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);

        Integer result = userService.daysStreak(1L);

        assertEquals(2, result);
        verify(userRepository, times(1)).findById(1L);
        verify(subjectService, times(1)).findAllByUserId(1L);
        verify(subjectSessionService, times(2)).getAllSubjectSessions();
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void setWeeklyGoal() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);

        Integer result = userService.setWeeklyGoal(1L, 15);

        assertEquals(15, result);
        assertEquals(15, user.getWeeklyGoal());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void calculateAvgSession() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(subjectService.findAllByUserId(anyLong())).thenReturn(subjects);
        when(subjectSessionService.getAllSubjectSessions()).thenReturn(sessions);
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);

        Integer result = userService.calculateAvgSession(1L);

        assertEquals(5400, result); // (3600 + 7200) / 2 = 5400
        verify(userRepository, times(1)).findById(1L);
        verify(subjectService, times(1)).findAllByUserId(1L);
        verify(subjectSessionService, times(2)).getAllSubjectSessions();
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void calculateTodaySessions() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(subjectService.findAllByUserId(anyLong())).thenReturn(subjects);
        when(subjectSessionService.getAllSubjectSessions()).thenReturn(sessions);
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);

        Integer result = userService.calculateTodaySessions(1L);

        assertEquals(1, result); // Only session2 is today
        verify(userRepository, times(1)).findById(1L);
        verify(subjectService, times(1)).findAllByUserId(1L);
        verify(subjectSessionService, times(2)).getAllSubjectSessions();
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void calculateMostProductiveSubject() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(subjectService.findAllByUserId(anyLong())).thenReturn(subjects);
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);

        String result = userService.calculateMostProductiveSubject(1L);

        assertEquals("Science", result); // Science has more time allotted (180 vs 120)
        verify(userRepository, times(1)).findById(1L);
        verify(subjectService, times(1)).findAllByUserId(1L);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void calculateLongestSession() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(subjectService.findAllByUserId(anyLong())).thenReturn(subjects);
        when(subjectSessionService.getAllSubjectSessions()).thenReturn(sessions);
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);

        Integer result = userService.calculateLongestSession(1L);

        assertEquals(7200, result); // session2 is longer (7200 vs 3600)
        verify(userRepository, times(1)).findById(1L);
        verify(subjectService, times(1)).findAllByUserId(1L);
        verify(subjectSessionService, times(2)).getAllSubjectSessions();
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void getDailyStudyTime() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        Integer result = userService.getDailyStudyTime(1L);

        assertEquals(120, result);
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void timeStudied() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(subjectService.findAllByUserId(anyLong())).thenReturn(subjects);
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);

        Long result = userService.timeStudied(1L);

        // Initial timeStudied (120) + subject1 (120) + subject2 (180) = 420
        assertEquals(420L, result);
        verify(userRepository, times(1)).findById(1L);
        verify(subjectService, times(1)).findAllByUserId(1L);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void getUserById() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        UserEntity result = userService.getUserById(1L);

        assertEquals(1L, result.getId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getAllUsers() {
        when(userRepository.findAll()).thenReturn(users);

        List<UserEntity> result = userService.getAllUsers();

        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Jane", result.get(1).getFirstName());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void deleteUserById() {
        doNothing().when(userRepository).deleteById(anyLong());

        userService.deleteUserById(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }
}
