package com.example.time.box.aspect;

import com.example.time.box.metrics.ViewAchievementMetric;
import lombok.AllArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@AllArgsConstructor
public class AchievementAspect {
    private final ViewAchievementMetric viewAchievementMetric;

    @AfterReturning("execution(* com.example.time.box.controller.AchievementController.createAchievement(..))")
    public void afterAchievementCreated() {
        viewAchievementMetric.countAchievementCreation();
    }

    @AfterReturning("execution(* com.example.time.box.controller.AchievementController.completeAchievement(..))")
    public void afterAchievementCompleted() {
        viewAchievementMetric.countAchievementCompleted();
    }
}
