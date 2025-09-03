package com.example.time.box.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String hashedPassword;

    @Column(nullable = false)
    @CreationTimestamp
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    private OffsetDateTime updatedAt;

    @Column
    private Long timeStudied = 0L;

    @Column
    private Integer daysStreak = 0;

    @Column
    private Integer todaySessions = 0;

    @Column
    private Integer longestSession = 0;

    @Column
    private String mostProductiveSubject;

    @Column
    private Integer weeklyGoal;

    @Column
    private Integer dailyGoal = 0;

    @Column
    private Integer dailyStudyTime = 0;

    @Column
    private Integer avgSession = 0;

    @Column(nullable = false)
    private Boolean emailVerified = false;

    @Column
    private String verificationCode;

    @Column
    private OffsetDateTime verificationCodeExpiry;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SessionEntity> sessions = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubjectEntity> subjectEntities = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AchievementEntity> achievements = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HabitEntity> habits = new ArrayList<>();
}
