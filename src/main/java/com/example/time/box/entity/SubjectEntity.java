package com.example.time.box.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.OffsetDateTime;

import static jakarta.persistence.GenerationType.*;

@Entity
@Table(name = "subjects")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class SubjectEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private OffsetDateTime startTime;

    @Column
    private OffsetDateTime endTime;

    @Column
    private Boolean running;

    @Column
    private String description;

    @Column
    private Integer numberOfSessions = 0;

    @Column
    private Long timeAllotted;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
    private UserEntity user;

}
