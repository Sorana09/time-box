package com.example.time.box.controller;

import com.example.time.box.entity.HabitCompletion;
import com.example.time.box.entity.HabitEntity;
import com.example.time.box.service.HabitService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/habits")
@AllArgsConstructor
@Slf4j
public class HabitController {

    private final HabitService habitService;



    @GetMapping("/user/{userId}")
    public ResponseEntity<List<HabitEntity>> getUserHabits(@PathVariable(name = "userId") Long userId) {
        return ResponseEntity.ok().body(habitService.findByUserId(userId));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HabitEntity> createHabit(@RequestBody HabitEntity habitEntity) {
        log.info("Received request to create habit: {}", habitEntity);
        HabitEntity createdHabit = habitService.createHabit(habitEntity);
        log.info("Successfully created habit with ID: {}", createdHabit.getId());
        return ResponseEntity.ok(createdHabit);
    }


    @PutMapping(value = "/{id}", consumes = {"application/json", "application/json;charset=UTF-8", "text/plain;charset=UTF-8", "text/plain", "*/*"})
    public ResponseEntity<HabitEntity> updateHabit(@PathVariable Long id, @RequestBody HabitEntity habit) {
        log.info("Received request to update habit with ID {}: {}", id, habit);
        HabitEntity updatedHabit = habitService.updateHabit(id, habit);
        log.info("Successfully updated habit with ID: {}", updatedHabit.getId());
        return ResponseEntity.ok().body(updatedHabit);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHabit(@PathVariable Long id) {
        habitService.deleteHabit(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/{habitId}/mark-completion", consumes = {"application/json", "application/json;charset=UTF-8", "text/plain;charset=UTF-8", "text/plain", "*/*", "application/x-www-form-urlencoded"})
    public ResponseEntity<HabitCompletion> markCompletions(
            @PathVariable(name = "habitId") Long habitId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam Boolean completed
    ) {
        return ResponseEntity.ok().body(habitService.markCompletion(habitId, date, completed));
    }

    @GetMapping("/{habitId}/completions")
    public ResponseEntity<List<HabitCompletion>> getCompletions(
            @PathVariable(name = "habitId") Long habitId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return ResponseEntity.ok().body(habitService.getCompletions(habitId, startDate, endDate));
    }

}
