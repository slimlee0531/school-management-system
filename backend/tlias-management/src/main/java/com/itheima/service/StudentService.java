package com.itheima.service;

import com.itheima.pojo.PageResult;
import com.itheima.pojo.Student;
import com.itheima.pojo.StudentQueryParam;

public interface StudentService {

    PageResult<Student> getPage(StudentQueryParam studentQueryParam);

    void addStudent(Student student);

    Student getStudentById(Integer id);

    void deleteStudents(Integer[] ids);

    void updateViolation(Integer id, Integer score);

    void updateStudent(Student student);
}
