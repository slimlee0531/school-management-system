package com.itheima.controller;

import com.itheima.anno.LogOperation;
import com.itheima.pojo.*;
import com.itheima.service.ClazzService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/clazzs")
@RestController
public class ClazzController {

    @Autowired
    private ClazzService clazzService;

    /**
     * 班级分页查询
     */
    @GetMapping
    public Result getPageClazzs(ClazzQueryParam param){
        log.info("param:{}",param);
        PageResult<Clazz> pageResult = clazzService.getPage(param);
        return Result.success(pageResult);
    }

    /**
     * 添加班级
     */
    @LogOperation
    @PostMapping
    public Result addClazz(@RequestBody Clazz clazz){
        log.info("添加班级 {}",clazz);
        clazzService.save(clazz);
        return Result.success();
    }

    /**
     * 根据ID查询班级/数据回显
     */
    @GetMapping("{id}")
    public Result findById(@PathVariable Integer id){
        log.info("查询班级 id:{}", id);
        Clazz clazz = clazzService.findById(id);
        return  Result.success(clazz);
    }

    /**
     * 修改班级
     */
    @LogOperation
    @PutMapping
    public Result updateClazz(@RequestBody Clazz clazz){
        log.info("修改班级 class:{}",clazz);
        clazzService.update(clazz);
        return Result.success();
    }

    /**
     * 删除班级
     */
    @LogOperation
    @DeleteMapping("{id}")
    public Result deleteById(@PathVariable Integer id){
        log.info("删除班级 id:{}", id);
        clazzService.deleteById(id);
        return Result.success();
    }

    /**
     * 查询所有班级
     */
    @GetMapping("/list")
    public Result getAllClazzs(){
        log.info("查询所有班级");
        List<Clazz> clazzList = clazzService.findAllClazzs();
        return Result.success(clazzList);
    }

}
