package com.example.time.box.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@Table(name = "achievements")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class AchievementEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column(name = "subject_id")
    private Long subjectId;

    @Column
    private Boolean completed;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "deadline")
    private OffsetDateTime deadline;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
    @JsonBackReference
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "subject_id", insertable = false, updatable = false)
    @JsonBackReference
    private SubjectEntity subject;
}
