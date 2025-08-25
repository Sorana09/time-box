package com.example.time.box.controller;

import com.example.time.box.domain.SubjectSessionDto;
import com.example.time.box.domain.UserDto;
import com.example.time.box.domain.mapper.Mapper;
import com.example.time.box.entity.request.UserSignUpRequest;
import com.example.time.box.service.SubjectSessionService;
import com.example.time.box.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.time.box.domain.mapper.Mapper.mapper;

@RequestMapping("/users")
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final SubjectSessionService subjectSessionService;

    @GetMapping("/sessions/{userId}")
    public ResponseEntity<List<SubjectSessionDto>> getAllSessionForAnUser(@PathVariable(name = "userId") Long userId) {
        return ResponseEntity.ok().body(subjectSessionService
                .getAllSubjectSessionsForAnUser(userId)
                .stream()
                .map(Mapper::mapper)
                .toList());
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers().stream().map(it -> mapper(it)).toList();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        UserDto user = mapper(userService.getUserById(id));
        return ResponseEntity.ok(user);
    }

    @GetMapping("/time-studied/{id}")
    public ResponseEntity<Long> getTimeStudiedForAnUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.timeStudied(id));
    }

    @PostMapping
    public ResponseEntity<UserDto> signUp(@RequestBody UserSignUpRequest userSignUpRequest) {
        UserDto userDto = mapper(userService.signUp(userSignUpRequest));
        return ResponseEntity.ok().body(userDto);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
    }

    @GetMapping("/days-streak/{id}")
    public Integer getDaysStreak(@PathVariable Long id) {
        return userService.daysStreak(id);
    }

    @PutMapping("/{id}/weekly-goal")
    public ResponseEntity<Integer> setWeeklyGoal(@PathVariable Long id, @RequestBody Integer weeklyGoal) {
        return ResponseEntity.ok().body(userService.setWeeklyGoal(id, weeklyGoal));
    }

    @PutMapping("/{id}/daily-goal")
    public ResponseEntity<Integer> setDailyGoal(@PathVariable Long id, @RequestBody Integer dailyGoal) {
        return ResponseEntity.ok().body(userService.setDailyGoal(id, dailyGoal));
    }

    @PutMapping("/{id}/daily-study-time")
    public ResponseEntity<Void> setDailyStudyTime(@PathVariable Long id, @RequestBody Integer dailyStudyTime) {
        userService.setDailyStudyTime(id, dailyStudyTime);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/avg-session")
    public ResponseEntity<Integer> getAvgSession(@PathVariable Long id) {
        Integer avgSession = userService.calculateAvgSession(id);
        return ResponseEntity.ok(avgSession);
    }

    @GetMapping("/{id}/today-sessions")
    public ResponseEntity<Integer> getTodaySessions(@PathVariable Long id) {
        Integer todaySessions = userService.calculateTodaySessions(id);
        return ResponseEntity.ok(todaySessions);
    }

    @GetMapping("/{id}/most-productive-subject")
    public ResponseEntity<String> getMostProductiveSubject(@PathVariable Long id) {
        String mostProductiveSubject = userService.calculateMostProductiveSubject(id);
        return ResponseEntity.ok(mostProductiveSubject);
    }

    @GetMapping("/{id}/longest-session")
    public ResponseEntity<Integer> getLongestSession(@PathVariable Long id) {
        Integer longestSession = userService.calculateLongestSession(id);
        return ResponseEntity.ok(longestSession);
    }

    @GetMapping("/{id}/daily-study-time")
    public ResponseEntity<Integer> getDailyStudyTime(@PathVariable Long id) {
        Integer dailyStudyTime = userService.getDailyStudyTime(id);
        return ResponseEntity.ok(dailyStudyTime);
    }


}
