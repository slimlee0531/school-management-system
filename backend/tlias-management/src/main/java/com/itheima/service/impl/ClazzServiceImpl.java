package com.itheima.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.exception.ClassHasStudentsException;
import com.itheima.exception.GlobalExceptionHandler;
import com.itheima.mapper.ClazzMapper;
//import com.itheima.mapper.ClazzQueryMapper;
import com.itheima.pojo.Clazz;
import com.itheima.pojo.ClazzQueryParam;
import com.itheima.pojo.Emp;
import com.itheima.pojo.PageResult;
import com.itheima.service.ClazzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClazzServiceImpl implements ClazzService {


    @Autowired
    private ClazzMapper clazzMapper;

    /**
     * PageHelper分页查询
     */
    @Override
    public PageResult getPage(ClazzQueryParam clazzQueryParam) {
        // 1. 分页参数
        PageHelper.startPage(clazzQueryParam.getPage(), clazzQueryParam.getPageSize());

        // 2. 执行查询
        List<Clazz> clazzList = clazzMapper.getClazzs(clazzQueryParam);

        for  (Clazz clazz : clazzList) {
            if (LocalDate.now().isAfter(clazz.getEndDate())) {
                clazz.setStatus("已结课");
            } else if (LocalDate.now().isBefore(clazz.getBeginDate())) {
                clazz.setStatus("未开班");
            } else {
                clazz.setStatus("在读中");
            }

            /*if (clazz.getMasterId() == 3) {
                clazz.setMasterName("李政");
            }*/
        }

        // 3. 解析查询结果，封装
        Page<Clazz> page = (Page<Clazz>) clazzList;
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void save(Clazz clazz) {
        clazz.setCreateTime(LocalDateTime.now());
        clazz.setUpdateTime(LocalDateTime.now());

        clazzMapper.insert(clazz);
    }

    @Override
    public Clazz findById(Integer id) {
        return clazzMapper.findById(id);
    }

    @Override
    public void update(Clazz clazz) {
        clazz.setUpdateTime(LocalDateTime.now());

        clazzMapper.update(clazz);
    }

    @Override
    public void deleteById(Integer id) {
        int studentCount = clazzMapper.countStudentsById(id);
        if (studentCount > 0) {
            throw new ClassHasStudentsException("该班级下有学生，不能直接删除！");
        }
        clazzMapper.deleteById(id);
    }

    @Override
    public List<Clazz> findAllClazzs() {
        return clazzMapper.findAllClazzs();
    }

//    @Override
//    public PageResult<Clazz> list() {
//        PageHelper.startPage();
//
//        return clazzMapper.list();
//    }
}
