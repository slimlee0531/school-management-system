package com.itheima.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Component
//@Aspect
@Order(1)
public class MyAspect1 {

    // 切入点方法（公共的切入点表达式）
    @Pointcut("execution(* com.itheima.service.impl.*.*(..))")
    public void pt() {}

    // 前置通知
    @Before("pt()")
    public void before(JoinPoint joinPoint) {
        log.info("before");
    }

    // 环绕通知
    @Around("pt()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        log.info("around");
        Object result = pjp.proceed();
        log.info("around");
        return result;
    }

    // 后置通知
    @After("pt()")
    public void after(JoinPoint joinPoint) {
        log.info("after");
    }

    // 返回后通知（程序正常执行，后置通知）
    @AfterReturning("pt()")
    public void afterReturning(JoinPoint joinPoint) {
        log.info("afterReturning");
    }

    // 异常通知
    @AfterThrowing("pt()")
    public void afterThrowing(JoinPoint joinPoint) {
        log.info("afterThrowing");
    }
}
