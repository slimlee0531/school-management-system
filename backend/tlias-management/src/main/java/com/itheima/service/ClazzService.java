package com.itheima.service;

import com.itheima.pojo.Clazz;
import com.itheima.pojo.ClazzQueryParam;
import com.itheima.pojo.Emp;
import com.itheima.pojo.PageResult;

import java.awt.event.PaintEvent;
import java.util.List;

public interface ClazzService {

    PageResult getPage(ClazzQueryParam clazzQueryParam);

    void save(Clazz clazz);

    Clazz findById(Integer id);

    void update(Clazz clazz);

    void deleteById(Integer id);

    List<Clazz> findAllClazzs();
}
