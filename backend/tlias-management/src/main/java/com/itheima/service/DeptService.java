package com.itheima.service;

import com.itheima.pojo.Dept;

import java.util.List;

public interface DeptService {

    /**
     * 查询所有部门
     */
    List<Dept> findAll();

    /**
     * 根据ID删除部门
     */
    void deleteById(Integer id);


    void add(Dept dept);

    Dept getById(Integer id);

    void update(Dept dept);
}
