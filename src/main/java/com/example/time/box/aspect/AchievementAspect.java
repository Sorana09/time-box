package com.example.time.box.aspect;

import com.example.time.box.metrics.ViewAchievementMetric;
import lombok.AllArgsConstructor;
import org.aspectj.lang.JoinPoint;
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

    @AfterReturning("execution(* com.example.time.box.controller.AchievementController.deleteAchievement(..))")
    public void afterAchievementDeleted() {
        viewAchievementMetric.countAchievementDeleted();
    }

    @AfterReturning("execution(* com.example.time.box.controller.AchievementController.getAllAchievements(..)) || " +
                    "execution(* com.example.time.box.controller.AchievementController.getAllAchievementsByUserId(..)) || " +
                    "execution(* com.example.time.box.controller.AchievementController.getCompletedAchievementsByUserId(..)) || " +
                    "execution(* com.example.time.box.controller.AchievementController.getIncompleteAchievementsByUserId(..))")
    public void afterAchievementViewed() {
        viewAchievementMetric.countAchievementViewed();
    }

    @AfterReturning(
        pointcut = "execution(* com.example.time.box.controller.AchievementController.getAllAchievementsByUserId(Long)) || " +
                  "execution(* com.example.time.box.controller.AchievementController.getCompletedAchievementsByUserId(Long)) || " +
                  "execution(* com.example.time.box.controller.AchievementController.getIncompleteAchievementsByUserId(Long))",
        returning = "result")
    public void afterUserAchievementsRetrieved(JoinPoint joinPoint, Object result) {
        Long userId = (Long) joinPoint.getArgs()[0];
        viewAchievementMetric.registerAchievementForUser(userId, 0L);
    }
}
