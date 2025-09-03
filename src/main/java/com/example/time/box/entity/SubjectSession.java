package com.example.time.box.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@Table(name = "subject_sessions")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SubjectSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "subject_id", nullable = false)
    private Long subjectId;

    @Column
    private OffsetDateTime startTime;

    @Column
    private OffsetDateTime endTime;

    @Column
    private Boolean running;

    @Column
    private Long timeAllotted;

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false, insertable = false, updatable = false)
    @JsonBackReference
    private SubjectEntity subject;

}
