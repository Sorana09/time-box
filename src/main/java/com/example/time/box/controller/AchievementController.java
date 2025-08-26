package com.example.time.box.controller;

import com.example.time.box.domain.AchievementDto;
import com.example.time.box.domain.mapper.Mapper;
import com.example.time.box.entity.AchievementEntity;
import com.example.time.box.entity.request.AchievementCreateRequest;
import com.example.time.box.service.AchievementService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/achievements")
@RestController
@AllArgsConstructor
public class AchievementController {
    private final AchievementService achievementService;

    @GetMapping
    public ResponseEntity<List<AchievementDto>> getAllAchievements() {
        return ResponseEntity.ok().body(achievementService.findAll().stream()
                .map(Mapper::mapper)
                .toList());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<AchievementDto>> getAllAchievementsByUserId(@PathVariable(name = "userId") Long userId) {
        return ResponseEntity.ok().body(achievementService.findAllByUserId(userId).stream()
                .map(Mapper::mapper)
                .toList());
    }

    @GetMapping("/{userId}/completed")
    public ResponseEntity<List<AchievementDto>> getCompletedAchievementsByUserId(@PathVariable(name = "userId") Long userId) {
        return ResponseEntity.ok().body(achievementService.findCompletedByUserId(userId).stream()
                .map(Mapper::mapper)
                .toList());
    }

    @GetMapping("/{userId}/incomplete")
    public ResponseEntity<List<AchievementDto>> getIncompleteAchievementsByUserId(@PathVariable(name = "userId") Long userId) {
        return ResponseEntity.ok().body(achievementService.findIncompleteByUserId(userId).stream()
                .map(Mapper::mapper)
                .toList());
    }

    @PostMapping
    public ResponseEntity<AchievementDto> createAchievement(@RequestBody AchievementCreateRequest request) {
        AchievementEntity achievement = achievementService.createAchievement(request);
        return ResponseEntity.ok().body(Mapper.mapper(achievement));
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<AchievementDto> completeAchievement(@PathVariable(name = "id") Long id) {
        AchievementEntity achievement = achievementService.completeAchievement(id);
        return ResponseEntity.ok().body(Mapper.mapper(achievement));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAchievement(@PathVariable Long id) {
        achievementService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
