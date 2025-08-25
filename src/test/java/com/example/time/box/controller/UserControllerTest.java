package com.example.time.box.controller;

import com.example.time.box.domain.UserDto;
import com.example.time.box.entity.UserEntity;
import com.example.time.box.entity.request.UserSignUpRequest;
import com.example.time.box.security.TestSecurityConfig;
import com.example.time.box.service.UserService;
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

@WebMvcTest(UserController.class)
@Import({UserControllerTest.TestConfig.class, TestSecurityConfig.class})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Configuration
    static class TestConfig {
        @Bean
        public UserService userService() {
            return mock(UserService.class);
        }

        @Bean
        public com.example.time.box.service.SubjectSessionService subjectSessionService() {
            return mock(com.example.time.box.service.SubjectSessionService.class);
        }

        @Bean
        public UserController userController(UserService userService, com.example.time.box.service.SubjectSessionService subjectSessionService) {
            return new UserController(userService, subjectSessionService);
        }
    }

    private UserEntity userEntity;
    private List<UserEntity> userEntities;
    private UserSignUpRequest signUpRequest;

    @BeforeEach
    void setUp() {
        userEntity = UserEntity.builder()
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
                .dailyGoal(5)
                .dailyStudyTime(120)
                .avgSession(45)
                .emailVerified(true)
                .build();

        UserEntity userEntity2 = UserEntity.builder()
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

        userEntities = Arrays.asList(userEntity, userEntity2);

        signUpRequest = UserSignUpRequest.builder()
                .firstName("New")
                .lastName("User")
                .email("new.user@example.com")
                .password("password")
                .build();
    }

    @Test
    void getAllUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(userEntities);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].firstName").value("Jane"))
                .andExpect(jsonPath("$[1].lastName").value("Smith"));

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void getUserById() throws Exception {
        when(userService.getUserById(anyLong())).thenReturn(userEntity);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));

        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    void getTimeStudiedForAnUser() throws Exception {
        when(userService.timeStudied(anyLong())).thenReturn(120L);

        mockMvc.perform(get("/users/time-studied/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("120"));

        verify(userService, times(1)).timeStudied(1L);
    }

    @Test
    void signUp() throws Exception {
        when(userService.signUp(any(UserSignUpRequest.class))).thenReturn(userEntity);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));

        verify(userService, times(1)).signUp(any(UserSignUpRequest.class));
    }

    @Test
    void deleteUser() throws Exception {
        doNothing().when(userService).deleteUserById(anyLong());

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk());

        verify(userService, times(1)).deleteUserById(1L);
    }

    @Test
    void getDaysStreak() throws Exception {
        when(userService.daysStreak(anyLong())).thenReturn(5);

        mockMvc.perform(get("/users/days-streak/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));

        verify(userService, times(1)).daysStreak(1L);
    }

    @Test
    void setWeeklyGoal() throws Exception {
        when(userService.setWeeklyGoal(anyLong(), anyInt())).thenReturn(10);

        mockMvc.perform(put("/users/1/weekly-goal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("10"))
                .andExpect(status().isOk())
                .andExpect(content().string("10"));

        verify(userService, times(1)).setWeeklyGoal(1L, 10);
    }

    @Test
    void setDailyGoal() throws Exception {
        when(userService.setDailyGoal(anyLong(), anyInt())).thenReturn(5);

        mockMvc.perform(put("/users/1/daily-goal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("5"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));

        verify(userService, times(1)).setDailyGoal(1L, 5);
    }

    @Test
    void setDailyStudyTime() throws Exception {
        doNothing().when(userService).setDailyStudyTime(anyLong(), anyInt());

        mockMvc.perform(put("/users/1/daily-study-time")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("120"))
                .andExpect(status().isOk());

        verify(userService, times(1)).setDailyStudyTime(1L, 120);
    }

    @Test
    void getAvgSession() throws Exception {
        when(userService.calculateAvgSession(anyLong())).thenReturn(45);

        mockMvc.perform(get("/users/1/avg-session"))
                .andExpect(status().isOk())
                .andExpect(content().string("45"));

        verify(userService, times(1)).calculateAvgSession(1L);
    }

    @Test
    void getTodaySessions() throws Exception {
        when(userService.calculateTodaySessions(anyLong())).thenReturn(2);

        mockMvc.perform(get("/users/1/today-sessions"))
                .andExpect(status().isOk())
                .andExpect(content().string("2"));

        verify(userService, times(1)).calculateTodaySessions(1L);
    }

    @Test
    void getMostProductiveSubject() throws Exception {
        when(userService.calculateMostProductiveSubject(anyLong())).thenReturn("Math");

        mockMvc.perform(get("/users/1/most-productive-subject"))
                .andExpect(status().isOk())
                .andExpect(content().string("Math"));

        verify(userService, times(1)).calculateMostProductiveSubject(1L);
    }

    @Test
    void getLongestSession() throws Exception {
        when(userService.calculateLongestSession(anyLong())).thenReturn(60);

        mockMvc.perform(get("/users/1/longest-session"))
                .andExpect(status().isOk())
                .andExpect(content().string("60"));

        verify(userService, times(1)).calculateLongestSession(1L);
    }

    @Test
    void getDailyStudyTime() throws Exception {
        when(userService.getDailyStudyTime(anyLong())).thenReturn(120);

        mockMvc.perform(get("/users/1/daily-study-time"))
                .andExpect(status().isOk())
                .andExpect(content().string("120"));

        verify(userService, times(1)).getDailyStudyTime(1L);
    }
}
