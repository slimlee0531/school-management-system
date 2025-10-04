package com.itheima.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.mapper.EmpExprMapper;
import com.itheima.mapper.EmpLogMapper;
import com.itheima.mapper.EmpMapper;
import com.itheima.pojo.*;
import com.itheima.service.EmpLogService;
import com.itheima.service.EmpService;
import com.itheima.utils.JwtUtils;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmpServiceImpl implements EmpService {

    @Autowired
    private EmpMapper empMapper;
    @Autowired
    private EmpExprMapper empExprMapper;
    @Autowired
    private EmpLogService empLogService;
    @Autowired
    private JwtUtils jwtUtils;

    /**
     * PageHelper分页查询
     * 这里的SQL查询不可以加上;
     */
    @Override
    public PageResult<Emp> getPage(EmpQueryParam empQueryParam) {
        // 1. 设置分页参数
        PageHelper.startPage(empQueryParam.getPage(), empQueryParam.getPageSize()); // PageHelper仅对后面第一个查询有效果

        // 2. 执行查询
        List<Emp> empList = empMapper.getEmps(empQueryParam);

        // 3. 解析查询结果，并封装
        Page<Emp> p =  (Page<Emp>) empList;
        return new PageResult<Emp>(p.getTotal(), p.getResult());
    }

    @Override
    public List<Emp> findAllEmps() {
        return empMapper.findAllEmps();
    }

    @Transactional(rollbackFor = Exception.class)  // 事务  出现的所有异常都会回滚
    @Override
    public void save(Emp emp) {
        try {
            // 1. 保存员工基本信息
            emp.setCreateTime(LocalDateTime.now());
            emp.setUpdateTime(LocalDateTime.now());

            // 2. 保存员工的基本信息
            empMapper.insert(emp);

            // 3. 保存员工工作经历信息-批量
            Integer empId = emp.getId();
            List<EmpExpr> exprList = emp.getExprList();
            if (!CollectionUtils.isEmpty(exprList)) {
                // 遍历集合，为empId赋值
                exprList.forEach(e -> e.setEmpId(empId));
                empExprMapper.insertBatch(exprList);
            }
        } finally {
            // 3. 记录操作日志
            EmpLog empLog = new EmpLog(null, LocalDateTime.now(), "新增员工:" + emp);
            empLogService.insertLog(empLog);

        }

    }

    @Transactional
    @Override
    public void update(Emp emp) {
        // 1. 修改时间
        emp.setUpdateTime(LocalDateTime.now());
        empMapper.updateById(emp);

        // 2. 根据员工ID删除员工的工作经历信息（旧的）
        empExprMapper.deleteByEmpIds(Arrays.asList(emp.getId()));

        // 3. 新增员工的工作经历数据
        Integer empId = emp.getId();
        List<EmpExpr> exprList = emp.getExprList();
        if (!CollectionUtils.isEmpty(exprList)) {
            exprList.forEach(e -> {
                e.setEmpId(empId);
            });
            empExprMapper.insertBatch(exprList);
        }

    }

    @Override
    public Emp getInfo(Integer id) {
        return empMapper.getById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteByIds(List<Integer> ids) {
        // 1. 根据ID批量删除员工
        empMapper.deleteByIds(ids);

        // 2. 根据员工的ID批量删除员工的工作经历信息
        empExprMapper.deleteByEmpIds(ids);
    }

    /*
    @Override
    public PageResult<Emp> getPage(Integer pageNum, Integer pageSize) {
        // 1. 调用mapper接口，查询总记录数
        Long total = empMapper.getTotal();

        // 2. 调用mapper接口，查询结果列表
        Integer empNum = (pageNum - 1) * pageSize;
        List<Emp> lists = empMapper.getPage(empNum, pageSize);

        // 3. 封装结果 PageResult
        return new PageResult<Emp>(total, lists);

    }
     */

    @Override
    public LoginInfo login(Emp emp) {

        Emp empLogin = empMapper.getUsernameAndPassword(emp);

        if (empLogin != null){
            // 1. 生成JWT令牌
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("id",  empLogin.getId());
            dataMap.put("username", empLogin.getUsername());
            dataMap.put("name",empLogin.getName()); // 新加逻辑（丰富令牌）

            String jwt = jwtUtils.generateJwt(dataMap);
            LoginInfo loginInfo = new LoginInfo(empLogin.getId(), empLogin.getUsername(), empLogin.getName(), jwt);

            return loginInfo;
        }
        return null;
    }

}
