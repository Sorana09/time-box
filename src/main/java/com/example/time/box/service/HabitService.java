package com.example.time.box.service;

import com.example.time.box.entity.HabitCompletion;
import com.example.time.box.entity.HabitEntity;
import com.example.time.box.entity.UserEntity;
import com.example.time.box.exception.EntityNotFoundException;
import com.example.time.box.repository.HabitCompletionRepository;
import com.example.time.box.repository.HabitRepository;
import com.example.time.box.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@AllArgsConstructor
public class HabitService {
    private static final Logger log = LoggerFactory.getLogger(HabitService.class);
    private final HabitRepository habitRepository;
    private final UserRepository userRepository;
    private final HabitCompletionRepository habitCompletionRepository;

    public List<HabitEntity> findByUserId(Long userId) {
        return habitRepository.findHabitEntityByUserId(userId);
    }

    public HabitEntity createHabit(HabitEntity habit) {
        log.info("Creating habit: {}", habit);

        if (habit.getUserId() == null) {
            log.error("User ID is null in habit: {}", habit);
            throw new IllegalArgumentException("User ID cannot be null");
        }

        log.info("Looking for user with ID: {}", habit.getUserId());
        UserEntity user = userRepository.findById(habit.getUserId())
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", habit.getUserId());
                    return new EntityNotFoundException();
                });

        HabitEntity habitEntity = new HabitEntity();
        habitEntity.setName(habit.getName());
        habitEntity.setDescription(habit.getDescription());
        habitEntity.setFrequency(habit.getFrequency());
        habitEntity.setTargetCount(habit.getTargetCount());
        habitEntity.setStartDate(habit.getStartDate());
        habitEntity.setActive(habit.isActive());
        habitEntity.setUserId(habit.getUserId());

        log.info("Saving habit entity: {}", habitEntity);
        HabitEntity savedHabit = habitRepository.save(habitEntity);
        log.info("Saved habit with ID: {}", savedHabit.getId());

        return savedHabit;
    }

    public HabitEntity updateHabit(Long id, HabitEntity habitDetails){
        log.info("Updating habit with ID {}: {}", id, habitDetails);

        HabitEntity habitEntity = habitRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        habitEntity.setName(habitDetails.getName());
        habitEntity.setDescription(habitDetails.getDescription());
        habitEntity.setFrequency(habitDetails.getFrequency());
        habitEntity.setTargetCount(habitDetails.getTargetCount());
        habitEntity.setActive(habitDetails.isActive());

        log.info("Saving updated habit entity: {}", habitEntity);
        HabitEntity updatedHabit = habitRepository.save(habitEntity);
        log.info("Updated habit with ID: {}", updatedHabit.getId());

        return updatedHabit;
    }

    public void deleteHabit(Long id) {
        HabitEntity habit = habitRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        habitCompletionRepository.deleteAll(habit.getCompletions());
        habitRepository.delete(habit);
    }

    public HabitCompletion markCompletion(Long habitId, LocalDate date, boolean completed) {
        log.info("Marking completion for habit {} on date {} with completed={}", habitId, date, completed);

        HabitEntity habit = habitRepository.findById(habitId)
                .orElseThrow(EntityNotFoundException::new);

        Optional<HabitCompletion> existingCompletion =  habitCompletionRepository.findByHabitIdAndCompletionDate(habitId, date);
        HabitCompletion result;

        if(existingCompletion.isPresent()) {
            HabitCompletion completion = existingCompletion.get();
            completion.setCompleted(completed);
            result = habitCompletionRepository.save(completion);
        } else {
            HabitCompletion newCompletion = new HabitCompletion();
            newCompletion.setHabit(habit);
            newCompletion.setCompletionDate(date);
            newCompletion.setCompleted(completed);
            result = habitCompletionRepository.save(newCompletion);
        }

        log.info("Marked completion result: {}", result);
        return result;
    }

    public List<HabitCompletion> getCompletions(Long habitId, LocalDate startDate, LocalDate endDate) {
        return habitCompletionRepository.findByHabitIdAndCompletionDateBetween(habitId, startDate, endDate);
    }

    public Map<LocalDate, Boolean> getCompletionMap(Long habitId, LocalDate startDate, LocalDate endDate) {
        List<HabitCompletion> completions = getCompletions(habitId, startDate, endDate);

        Map<LocalDate, Boolean> completionMap = new HashMap<>();
        for (HabitCompletion completion : completions) {
            completionMap.put(completion.getCompletionDate(), completion.isCompleted());
        }

        return completionMap;
    }
}
