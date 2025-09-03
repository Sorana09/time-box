package com.example.time.box.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @NotNull(message = "Habit frequency is required")
    private HabitFrequency frequency;
    
    @Column
    @Min(value = 1, message = "Target count must be at least 1")
    private int targetCount;
    
    @Column
    @NotNull(message = "Start date is required")
    private LocalDate startDate;
    
    @Column
    private boolean active;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    @NotNull(message = "User is required")
    private UserEntity user;

    @OneToMany(mappedBy = "habit", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<HabitCompletion> completions = new ArrayList<>();

}
