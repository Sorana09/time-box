package com.example.time.box.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table(name = "study_rooms")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RoomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String invitationToken;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private String createdBy;

    @Column
    private OffsetDateTime createdAt;

    @Column
    private OffsetDateTime startedAt;

    @Column
    private Boolean isActive;

    @ElementCollection
    private List<String> participants;

    @Column
    private Boolean chatEnabled;

    @Column
    private String timerStatus;
}