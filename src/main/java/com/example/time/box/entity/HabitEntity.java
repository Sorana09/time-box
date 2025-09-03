package com.example.time.box.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
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
    @NotBlank(message = "Habit name is required")
    private String name;

    @Column
    private String description;

    @Column
    private HabitFrequency frequency;

    @Column
    @Min(value = 1, message = "Target count must be at least 1")
    private int targetCount;

    @Column
    private LocalDate startDate;

    @Column
    private boolean active;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @OneToMany(mappedBy = "habit", cascade = CascadeType.ALL)
    @JsonIgnore
    @JsonManagedReference("habit-completions")
    private List<HabitCompletion> completions = new ArrayList<>();

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
    @JsonBackReference("user-habits")
    private UserEntity user;

}
