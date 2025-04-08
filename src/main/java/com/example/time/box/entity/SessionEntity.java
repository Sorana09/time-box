package com.example.time.box.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "sessions")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SessionEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String sessionKey;

    @Column(nullable = false)
    private OffsetDateTime expiredAt;

    @Column(nullable = false)
    private Long userId;
}