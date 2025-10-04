package com.itheima.controller;

import com.github.pagehelper.Page;
import com.itheima.mapper.StudentMapper;
import com.itheima.pojo.PageResult;
import com.itheima.pojo.Result;
import com.itheima.pojo.Student;
import com.itheima.pojo.StudentQueryParam;
import com.itheima.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    /**
     * 学员分页查询
     */
    @GetMapping
    public Result getPageStudents(StudentQueryParam studentQueryParam) {
        log.info("学员分页查询 studentQueryParam={}", studentQueryParam);
        PageResult<Student> pageResult = studentService.getPage(studentQueryParam);
        return Result.success(pageResult);
    }

    /**
     * 添加学员
     */
    @PostMapping
    public Result addStudent(@RequestBody Student student) {
        log.info("添加学员 {}", student);
        studentService.addStudent(student);
        return Result.success();
    }

    /**
     * 根据ID查询学员
     */
    @GetMapping("{id}")
    public Result getStudentById(@PathVariable Integer id) {
        log.info("根据ID查询学生 ID:{}", id);
        Student student = studentService.getStudentById(id);
        return Result.success(student);
    }

    /**
     * 修改学员
     */
    @PutMapping
    public Result updateStudent(@RequestBody Student student) {
        log.info("修改学员 {}", student);
        studentService.updateStudent(student);
        return Result.success();
    }

    /**
     * 批量删除学员
     */
    @DeleteMapping("{ids}")
    public Result deleteStudents(@PathVariable Integer[] ids) {
        log.info("批量删除学生信息 ids={}", ids);
        studentService.deleteStudents(ids);
        return Result.success();
    }

    /**
     * 违纪处理
     */
    @PutMapping("/violation/{id}/{score}")
    public Result updateStudentViolation(@PathVariable Integer id, @PathVariable Integer score) {
        log.info("违纪信息处理 id={}, score={}", id, score);
        studentService.updateViolation(id, score);
        return Result.success();
    }

}
