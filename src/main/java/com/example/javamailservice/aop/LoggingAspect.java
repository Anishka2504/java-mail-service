package com.example.javamailservice.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Aspect
public class LoggingAspect {

    @AfterReturning(pointcut = "com.example.javamailservice.aop.pointcut.CommonPointcutStorage.methodWithAnnotationAfterReturning()",
            returning = "retVal")
    public Object logExecutionAfterReturning(JoinPoint joinPoint, ResponseEntity retVal) throws Throwable {
        switch (retVal.getStatusCode().value()) {
            case 200 -> {
                long start = System.currentTimeMillis();
                ProceedingJoinPoint proceedingJoinPoint = (ProceedingJoinPoint) joinPoint;
                Object proceed = proceedingJoinPoint.proceed();
                long executionTime = System.currentTimeMillis() - start;
                log.info("Method {} executed in {} ms", joinPoint.getSignature().toShortString(), executionTime);
                return proceed;
            }
            case 400 -> {
                log.error("Method {} failed with status {}", joinPoint.getSignature().toShortString(), retVal.getStatusCode());
                return null;
            }
        }
        return null;
    }

    @Before("com.example.javamailservice.aop.pointcut.CommonPointcutStorage.logMethodStart()")
    public void logWhenCallAnyMethod(JoinPoint joinPoint) {
        log.info("Method {} called", joinPoint.getSignature().toShortString());
    }
}
