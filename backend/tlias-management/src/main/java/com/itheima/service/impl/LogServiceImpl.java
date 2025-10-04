package com.itheima.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.mapper.LogMapper;
import com.itheima.pojo.LogQueryParam;
import com.itheima.pojo.OperateLog;
import com.itheima.pojo.PageResult;
import com.itheima.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private LogMapper logMapper;

    @Override
    public PageResult<OperateLog> getPageLogs(LogQueryParam logQueryParam) {
        // 1. 分页参数
        PageHelper.startPage(logQueryParam.getPage(), logQueryParam.getPageSize());

        // 2. 执行查询
        List<OperateLog> logList = logMapper.getLogList(logQueryParam);

        // 3. 解析查询结果，封装
        Page<OperateLog> page = (Page<OperateLog>) logList;
        return new PageResult<>(page.getTotal(), page.getResult());

    }
}
