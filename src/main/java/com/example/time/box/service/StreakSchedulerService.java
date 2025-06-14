package com.example.time.box.service;

import com.example.time.box.entity.UserEntity;
import com.example.time.box.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class StreakSchedulerService {
    private final UserRepository userRepository;
    private final UserService userService;

    @Scheduled(cron = "0 0 0 * * *")
    public void updateAllUsersStreak() {
        log.info("Starting daily streak update for all users");
        List<UserEntity> users = userRepository.findAll();

        users.forEach(user -> {
            try {
                Integer streak = userService.daysStreak(user.getId());
                log.info("Updated streak for user {}: {}", user.getId(), streak);
            } catch (Exception e) {
                log.error("Error updating streak for user {}: {}", user.getId(), e.getMessage());
            }
        });

        log.info("Completed daily streak update");
    }
}