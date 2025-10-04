package com.itheima.aop;

import com.itheima.anno.LogOperation;
import com.itheima.mapper.OperateLogMapper;
import com.itheima.pojo.OperateLog;
import com.itheima.utils.CurrentHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Aspect
@Component
public class OperationLogAspect {

    @Autowired
    private OperateLogMapper operateLogMapper;

    // 环绕通知
    @Around("@annotation(logOperation)")
    public Object around(ProceedingJoinPoint joinPoint, LogOperation logOperation) throws Throwable {
        Integer empId = getCurrentUserId();
        String empName = getCurrentEmpName();
        // 记录开始时间
        long startTime = System.currentTimeMillis();
        // 执行方法
        Object result = null;
        Throwable thrown = null;
        try {
            result = joinPoint.proceed();
        } catch (Throwable t) {
            thrown = t;
            throw t;
        } finally {
            long endTime = System.currentTimeMillis();
            long costTime = endTime - startTime;
            OperateLog operateLog = new OperateLog();
            operateLog.setOperateEmpId(empId);
            operateLog.setOperateEmpName(empName != null ? empName : "无名氏");
            operateLog.setOperateTime(LocalDateTime.now());
            operateLog.setClassName(joinPoint.getTarget().getClass().getName());
            operateLog.setMethodName(joinPoint.getSignature().getName());
            operateLog.setMethodParams(Arrays.toString(joinPoint.getArgs()));
            operateLog.setReturnValue(result != null ? result.toString() : (thrown != null ? "Exception" + thrown.getMessage() : "Void"));
            operateLog.setCostTime(costTime);
            operateLogMapper.insert(operateLog);
        }

        return result;
    }

    private String getCurrentEmpName() {
        return CurrentHolder.getCurrentEmpName();
    }

    private Integer getCurrentUserId() {
        return CurrentHolder.getCurrentId();
    }

}
