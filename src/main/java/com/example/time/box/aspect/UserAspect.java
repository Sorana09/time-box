package com.example.time.box.aspect;

import com.example.time.box.metrics.ViewUserMetric;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class UserAspect {

    private final ViewUserMetric viewUserMetric;

    @AfterReturning("execution(* com.example.time.box.controller.UserController.getUserById(Long)) || " +
                    "execution(* com.example.time.box.controller.UserController.getTimeStudiedForAnUser(Long)) || " +
                    "execution(* com.example.time.box.controller.UserController.getDaysStreak(Long)) || " +
                    "execution(* com.example.time.box.controller.UserController.getAvgSession(Long)) || " +
                    "execution(* com.example.time.box.controller.UserController.getTodaySessions(Long)) || " +
                    "execution(* com.example.time.box.controller.UserController.getMostProductiveSubject(Long)) || " +
                    "execution(* com.example.time.box.controller.UserController.getLongestSession(Long)) || " +
                    "execution(* com.example.time.box.controller.UserController.getDailyStudyTime(Long))")
    public void afterUserViewed(JoinPoint joinPoint) {
        Long userId = (Long) joinPoint.getArgs()[0];
        viewUserMetric.registerViewForUser(userId);
    }

    @AfterReturning("execution(* com.example.time.box.controller.UserController.signUp(..))")
    public void afterUserRegistered() {
        viewUserMetric.countUserRegistration();
    }
}
