package com.example.javamailservice.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Aspect
public class LoggingAspect {

    @AfterReturning(pointcut = "execution(public * com.example.javamailservice.service.impl.UserServiceImpl.*(..))",
            returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, ResponseEntity<?> result) throws Throwable {
        if (result.getStatusCode().is4xxClientError()) {
            log.error("Method {} failed with status {}", joinPoint.getSignature().getName(), result.getStatusCode());
        } else if (result.getStatusCode().is2xxSuccessful()) {
            ProceedingJoinPoint proceedingJoinPoint = (ProceedingJoinPoint) joinPoint;
            long start = System.currentTimeMillis();
            proceedingJoinPoint.proceed();
            long executionTime = System.currentTimeMillis() - start;
            log.info("Method {} executed successfully in {} ms", joinPoint.getSignature().toShortString(), executionTime);
        }

    }
}
