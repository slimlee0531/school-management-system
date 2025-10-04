package com.itheima.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class MyAspect2 {

//    @Before("execution(void com.itheima.service.impl.DeptServiceImpl.delete(java.lang.Integer))")
//    @Before("execution(* com.itheima.service.impl.DeptServiceImpl.delete(java.lang.Integer))")
    @Before("execution(* com.itheima.*.*.DeptServiceImpl.delete(java.lang.Integer))")
    public void before(){
        log.info("before");
    }

}
