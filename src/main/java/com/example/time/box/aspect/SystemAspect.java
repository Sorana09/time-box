package com.example.time.box.aspect;

import com.example.time.box.metrics.SystemMetric;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class SystemAspect {
    private final SystemMetric systemMetric;

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *) || " +
            "within(@org.springframework.stereotype.Controller *)")
    public void controllerPointcut() {
    }

    @Pointcut("execution(* com.example.time.box.repository.*.*(..))")
    public void repositoryPointcut() {
    }

    @Around("controllerPointcut()")
    public Object trackApiCall(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String endpoint = getEndpoint(joinPoint);
        String httpMethod = getHttpMethod(joinPoint);

        systemMetric.countApiCall(endpoint, httpMethod);

        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();

        systemMetric.recordOperationTime("api." + className + "." + methodName, endTime - startTime);

        return result;
    }

    @Around("repositoryPointcut()")
    public Object trackDatabaseQuery(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String queryType = getQueryType(methodName);

        systemMetric.countDatabaseQuery(queryType);

        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();

        systemMetric.recordOperationTime("db." + className + "." + methodName, endTime - startTime);

        return result;
    }

    @AfterThrowing(pointcut = "execution(* com.example.time.box..*.*(..))", throwing = "ex")
    public void trackException(JoinPoint joinPoint, Exception ex) {
        String exceptionType = ex.getClass().getSimpleName();
        systemMetric.countErrorOccurrence(exceptionType);
    }

    private String getEndpoint(JoinPoint joinPoint) {
        try {
            Class<?> controllerClass = joinPoint.getTarget().getClass();
            RequestMapping classMapping = controllerClass.getAnnotation(RequestMapping.class);
            String basePath = "";
            if (classMapping != null && classMapping.value().length > 0) {
                basePath = classMapping.value()[0];
            }
            return basePath + "/" + joinPoint.getSignature().getName();
        } catch (Exception e) {
            return joinPoint.getSignature().toShortString();
        }
    }

    private String getHttpMethod(JoinPoint joinPoint) {
        try {
            String methodName = joinPoint.getSignature().getName();
            if (methodName.startsWith("get")) return "GET";
            if (methodName.startsWith("post")) return "POST";
            if (methodName.startsWith("put")) return "PUT";
            if (methodName.startsWith("delete")) return "DELETE";
            if (methodName.startsWith("patch")) return "PATCH";
            return "UNKNOWN";
        } catch (Exception e) {
            return "UNKNOWN";
        }
    }

    private String getQueryType(String methodName) {
        if (methodName.startsWith("find") || methodName.startsWith("get") || methodName.startsWith("read")) {
            return "SELECT";
        } else if (methodName.startsWith("save") || methodName.startsWith("insert") || methodName.startsWith("add")) {
            return "INSERT";
        } else if (methodName.startsWith("update") || methodName.startsWith("modify")) {
            return "UPDATE";
        } else if (methodName.startsWith("delete") || methodName.startsWith("remove")) {
            return "DELETE";
        } else if (methodName.startsWith("count")) {
            return "COUNT";
        } else {
            return "OTHER";
        }
    }
}