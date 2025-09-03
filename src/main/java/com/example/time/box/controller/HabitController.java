package com.example.time.box.controller;

import com.example.time.box.entity.HabitCompletion;
import com.example.time.box.entity.HabitEntity;
import com.example.time.box.service.HabitService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/habits")
@AllArgsConstructor
public class HabitController {

    private final HabitService habitService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<HabitEntity>> getUserHabits(@PathVariable(name = "userId") Long userId) {
        return ResponseEntity.ok().body(habitService.findByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<HabitEntity> createHabit(@RequestBody HabitEntity habitEntity) {
        return ResponseEntity.ok().body(habitService.createHabit(habitEntity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HabitEntity> updateHabit(@PathVariable Long id, @RequestBody HabitEntity habit) {
        return ResponseEntity.ok().body(habitService.updateHabit(id, habit));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHabit(@PathVariable Long id) {
        habitService.deleteHabit(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{habitId}/mark-completion")
    public ResponseEntity<HabitCompletion> markCompletions(
            @PathVariable(name = "habitId") Long habitId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate date,
            @RequestParam Boolean completed
    ) {
        return ResponseEntity.ok().body(habitService.markCompletion(habitId, date, completed));
    }

    @GetMapping("/{habitId}/completions")
    public ResponseEntity<List<HabitCompletion>> getCompletions(
            @PathVariable(name = "habitId") Long habitId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate endDate
    ) {
        return ResponseEntity.ok().body(habitService.getCompletions(habitId, startDate, endDate));
    }

}
