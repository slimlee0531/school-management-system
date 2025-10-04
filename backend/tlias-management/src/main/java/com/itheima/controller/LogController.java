package com.itheima.controller;

import com.itheima.pojo.*;
import com.itheima.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("log")
@RestController
public class LogController {

    @Autowired
    private LogService logService;

    @GetMapping("/page")
    public Result getPageLogs(LogQueryParam logQueryParam) {
        log.info("日志信息分页查询 logQueryParam:{}", logQueryParam);
        PageResult<OperateLog> pageResult = logService.getPageLogs(logQueryParam);
        return Result.success(pageResult);
    }

}
