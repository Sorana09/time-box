package com.example.time.box.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @JsonBackReference
    private HabitEntity habit;
    
    @Override
    public String toString() {
        return "HabitCompletion{" +
                "id=" + id +
                ", completionDate=" + completionDate +
                ", completed=" + completed +
                ", habitId=" + (habit != null ? habit.getId() : "null") +
                '}';
    }
}
