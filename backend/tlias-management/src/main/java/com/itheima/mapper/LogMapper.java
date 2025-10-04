package com.itheima.mapper;

import com.itheima.pojo.LogQueryParam;
import com.itheima.pojo.OperateLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LogMapper {

    // 分页查询
    List<OperateLog> getLogList(LogQueryParam logQueryParam);
}
