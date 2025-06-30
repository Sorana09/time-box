package com.example.time.box.aspect;

import com.example.time.box.metrics.ViewSubjectMetric;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class SubjectAspect {

    private final ViewSubjectMetric viewSubjectMetric;

    @AfterReturning("execution(* com.example.time.box.controller.SubjectController.getNumberOfSessions(Long)) || " +
            "execution(* com.example.time.box.controller.SubjectController.getTotalTimeAllotedForAnSubject(Long))")
    public void afterSubjectViewed(JoinPoint joinPoint) {
        Long subjectId = (Long) joinPoint.getArgs()[0];
        viewSubjectMetric.createViewForSubject(subjectId);
    }

    @AfterReturning("execution(* com.example.time.box.controller.SubjectController.createSubject(..))")
    public void afterSubjectCreated() {
        viewSubjectMetric.countSubjectCreation();
    }
}
