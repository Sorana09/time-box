package com.example.time.box.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import static jakarta.persistence.GenerationType.*;

@Entity
@Table(name = "habit_completions")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HabitCompletion {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private LocalDate completionDate;
    private boolean completed;

    @ManyToOne
    @JoinColumn(name = "habit_id")
    private HabitEntity habit;
}
