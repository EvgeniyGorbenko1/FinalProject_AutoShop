package com.tms.finalproject_autoshop.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

//@EnableAspectJAutoProxy(proxyTargetClass = true)
@Aspect
@Component
@Slf4j
public class TimingAspect {

    @Value("${aop.timing.enabled:true}")
    private boolean isTimingEnabled;

    @Around("@annotation(Timed)")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        if (!isTimingEnabled) {
            return joinPoint.proceed();
        }
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        log.info("Method [{}] executed in {} ms", joinPoint.getSignature(), endTime - startTime);
        return result;
    }
}

