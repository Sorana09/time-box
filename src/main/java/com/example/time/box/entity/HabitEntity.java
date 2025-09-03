package com.example.time.box.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "habits")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HabitEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;
    @Column
    private String description;
    @Column
    private HabitFrequency frequency;
    @Column
    private int targetCount;
    @Column
    private LocalDate startDate;
    @Column
    private boolean active;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToMany(mappedBy = "habit", cascade = CascadeType.ALL)
    private List<HabitCompletion> completions = new ArrayList<>();

}
