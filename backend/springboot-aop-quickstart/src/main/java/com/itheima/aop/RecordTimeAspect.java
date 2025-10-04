package com.itheima.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
//@Aspect
@Slf4j
public class RecordTimeAspect {

    @Around("execution(* com.itheima.service.impl.DeptServiceImpl.*(..))")
    public Object recordTime(ProceedingJoinPoint jpt) throws Throwable {
        // 记录方法执行开始时间
        long start = System.currentTimeMillis();
        // 执行原始方法
        Object result = jpt.proceed();
        // 记录方法执行结束时间
        long end = System.currentTimeMillis();
        long time = end - start;

        log.info("方法{}执行耗时: {}ms",jpt.getSignature(), time);
        return result;
    }

}
