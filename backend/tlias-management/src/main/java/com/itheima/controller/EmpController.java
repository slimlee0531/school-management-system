package com.itheima.controller;

import com.itheima.anno.LogOperation;
import com.itheima.mapper.EmpMapper;
import com.itheima.pojo.Emp;
import com.itheima.pojo.EmpQueryParam;
import com.itheima.pojo.PageResult;
import com.itheima.pojo.Result;
import com.itheima.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequestMapping("/emps")
@RestController
public class EmpController {

    @Autowired
    private EmpService empService;
    @Autowired
    private EmpMapper empMapper;

    @GetMapping
    public Result getPageEmps(EmpQueryParam empQueryParam) {
        log.info("empQueryParam={}", empQueryParam);
        PageResult<Emp> pageResult = empService.getPage(empQueryParam);
        return Result.success(pageResult);
    }

    /**
     * 新增员工
     */
    @LogOperation
    @PostMapping
    public Result save(@RequestBody Emp emp) {
        log.info("新增员工emp={}", emp);
        empService.save(emp);
        return Result.success();
    }

    /**
     * 批量删除员工
     */
    @DeleteMapping
    public Result delete(@RequestParam List<Integer> ids) {
        log.info("批量删除员工：ids={}", ids);
        empService.deleteByIds(ids);
        return Result.success();
    }

    /**
     * 修改员工
     */
    @LogOperation
    @PutMapping
    public Result update(@RequestBody Emp emp) {
        log.info("修改员工emp={}", emp);
        empService.update(emp);
        return Result.success();
    }

    /**
     * 下拉查询全部班主任信息
     */
    @GetMapping("/list")
    public Result list(){
        log.info("查询全部员工(Master)信息");
        List<Emp> empList = empService.findAllEmps();
        return Result.success(empList);
    }

    /**
     * 查询回显
     */
    @GetMapping("/{id}")
    public Result getInfo(@PathVariable Integer id) {
        log.info("根据ID查询员工的详细信息 id={}", id);
        Emp emp = empService.getInfo(id);
        return Result.success(emp);
    }

    /**
     * 分页查询
     */
    /*
    @GetMapping
    public Result getPageEmps(@RequestParam(defaultValue = "1") Integer page,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              String name, Integer gender,
                              @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                              @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("分页查询：页码-{}, 页面数量-{}, {}, {}, {}, {}", page, pageSize, name, gender, begin, end );
        PageResult<Emp> pageResult = empService.getPage(page, pageSize, name, gender, begin, end);
        return Result.success(pageResult);
    }
    */
}

