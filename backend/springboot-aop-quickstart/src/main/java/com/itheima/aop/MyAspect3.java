package com.itheima.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Component
@Aspect
public class MyAspect3 {
    // 针对list方法、delete方法进行前置通知和后置通知

    // 前置通知
    @Before("@annotation(com.itheima.anno.LogOperation)")
    public void before(JoinPoint joinPoint) {
        log.info("MyAspect3 before");

        // 1. 获取目标对象
        Object target = joinPoint.getTarget();
        log.info("获取目标对象:{}", target);

        // 2. 获取目标类
        String className = joinPoint.getTarget().getClass().getName();
        log.info("获取目标类：{}", className);

        // 3. 获取目标方法
        String methodName = joinPoint.getSignature().getName();
        log.info("获取目标方法：{}", methodName);

        // 4. 获取目标方法参数
        Object[] args = joinPoint.getArgs();
        log.info("获取目标方法参数：{}", Arrays.toString(args));

    }

    // 后置通知
    @After("@annotation(com.itheima.anno.LogOperation)")
    public void after() {
        log.info("MyAspect3 after");
    }

}
