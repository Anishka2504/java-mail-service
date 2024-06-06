package com.example.javamailservice.aop.pointcut;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class CommonPointcutStorage {

    @Pointcut("@annotation(com.example.javamailservice.annotation.LogExecution)")
    public void methodWithAnnotationAfterReturning() {}

    @Pointcut("within(com.example.javamailservice.service.impl.UserServiceImpl)")
    public void logMethodStart(){}
}
