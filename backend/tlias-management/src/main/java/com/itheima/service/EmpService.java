package com.itheima.service;

import com.itheima.pojo.Emp;
import com.itheima.pojo.EmpQueryParam;
import com.itheima.pojo.LoginInfo;
import com.itheima.pojo.PageResult;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface EmpService {

    public PageResult<Emp> getPage(EmpQueryParam empQueryParam);

    void save(Emp emp);

    void deleteByIds(List<Integer> ids);

    List<Emp> findAllEmps();

    void update(Emp emp);

    /**
     * 根据ID查询员工的详细信息
     */
    Emp getInfo(Integer id);

    LoginInfo login(Emp emp);
}
