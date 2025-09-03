package com.example.time.box.repository;

import com.example.time.box.entity.HabitCompletion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface HabitCompletionRepository extends JpaRepository<HabitCompletion,Long> {
    List<HabitCompletion> findByHabitIdAndCompletionDateBetween(Long habitId, LocalDate startDate, LocalDate endDate);
    Optional<HabitCompletion> findByHabitIdAndCompletionDate(Long habitId, LocalDate date);
    List<HabitCompletion> findByHabitId(Long habitId);
}
