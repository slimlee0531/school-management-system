package com.itheima.exception;

import com.itheima.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    // 处理异常
    @ExceptionHandler
    public Result handleException(Exception e) {
        log.error("程序出错了", e);
        e.printStackTrace(); // 打印堆栈中的异常信息
        // 捕获到异常之后，响应一个标准的Result
        return Result.error(e.getMessage());
    }

    @ExceptionHandler
    public Result handDuplicateKeyException(DuplicateKeyException e) {
        log.error("关键字重复错误", e);
        String message = e.getMessage();
        int i = message.indexOf("Duplicate Key");
        String errMsg = message.substring(i);
        String[] arr =  errMsg.split(",");
        return Result.error(arr[2] + "已存在");
    }

    @ExceptionHandler
    public Result handleClassHasStudentsException(ClassHasStudentsException e) {
        log.error("删除班级错误, {}", e.getMessage());
        return Result.error(e.getMessage());
    }

    @ExceptionHandler
    public Result handleDeptHasEmpsException(DeptHasEmpsException e) {
        log.error("日志：当前部门下有员工，无法删除！{}",  e.getMessage());
        return Result.error(e.getMessage());
    }
}


