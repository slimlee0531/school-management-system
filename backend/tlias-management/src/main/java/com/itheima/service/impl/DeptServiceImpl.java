package com.itheima.service.impl;

import com.itheima.exception.DeptHasEmpsException;
import com.itheima.mapper.DeptMapper;
import com.itheima.mapper.EmpMapper;
import com.itheima.pojo.Dept;
import com.itheima.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeptServiceImpl implements DeptService {

    @Autowired
    private DeptMapper deptMapper;
/*    @Autowired
    private EmpMapper empMapper;*/

    @Override
    public List<Dept> findAll() {
        return deptMapper.findAll();
    }

    @Override
    public void deleteById(Integer id) {
        int empCount = deptMapper.countEmpByDeptId(id);
        if (empCount > 0) {
            throw new DeptHasEmpsException("该部门下有员工，无法删除！");
        }

        deptMapper.deleteById(id);
    }

    @Override
    public void add(Dept dept) {
        // 1. 补全基础属 - createTime, updateTime
        dept.setCreateTime(LocalDateTime.now());
        dept.setUpdateTime(LocalDateTime.now());

        // 2. 调用Mapper接口方法插入数据
        deptMapper.insert(dept);
    }

    @Override
    public Dept getById(Integer id) {
        return deptMapper.getById(id);
    }

    @Override
    public void update(Dept dept) {
        // 1. 补全基础属性-updateTime
        dept.setUpdateTime(LocalDateTime.now());

        // 2. 调用Mapper接口方法更新部门
        deptMapper.update(dept);
    }


}
