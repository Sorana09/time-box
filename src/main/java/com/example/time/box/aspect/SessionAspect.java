package com.example.time.box.aspect;

import com.example.time.box.metrics.SessionMetric;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class SessionAspect {
    private final SessionMetric sessionMetric;

    @AfterReturning("execution(* com.example.time.box.controller.SessionController.createSession(..))")
    public void afterSessionCreated() {
        sessionMetric.countSessionCreation();
    }

    @AfterReturning("execution(* com.example.time.box.controller.SessionController.deleteSession(..))")
    public void afterSessionDeleted() {
        sessionMetric.countSessionDeletion();
    }

    @AfterReturning("execution(* com.example.time.box.service.SessionService.find(..)) && args(userId, onlyExpired)")
    public void afterSessionsQueried(JoinPoint joinPoint, Long userId, boolean onlyExpired) {
        if (onlyExpired) {
            sessionMetric.countSessionExpiration();
        }
        sessionMetric.registerSessionForUser(userId);
    }

    @AfterReturning("execution(* com.example.time.box.controller.SubjectSessionController.createSubjectSession(..))")
    public void afterSubjectSessionCreated() {
        sessionMetric.countSessionCreation();
    }

    @AfterReturning("execution(* com.example.time.box.controller.SubjectSessionController.deleteSubjectSession(..))")
    public void afterSubjectSessionDeleted() {
        sessionMetric.countSessionDeletion();
    }
}