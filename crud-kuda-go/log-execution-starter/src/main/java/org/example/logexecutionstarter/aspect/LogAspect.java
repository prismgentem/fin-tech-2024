package org.example.logexecutionstarter.aspect;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LogAspect {
    @SneakyThrows
    @Around("@annotation(org.example.logexecutionstarter.annotation.LogExecutionTime) || @within(org.example.logexecutionstarter.annotation.LogExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) {
        long start = System.nanoTime();
        Object proceed;
        try {
            proceed = joinPoint.proceed();
        } catch (Throwable throwable) {
            log.error("Exception in method: {}.{}: {}",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(),
                    throwable.getStackTrace());
            throw throwable;
        }

        long executionTime = System.nanoTime() - start;

        log.info("Executed method: {}.{} in {}ms",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                executionTime / 1_000_000);

        return proceed;
    }
}