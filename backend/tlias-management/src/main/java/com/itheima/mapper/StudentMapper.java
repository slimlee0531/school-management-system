package com.itheima.mapper;

import com.itheima.pojo.PageResult;
import com.itheima.pojo.Student;
import com.itheima.pojo.StudentQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface StudentMapper {

    List<Student> getPage(StudentQueryParam studentQueryParam);

    void addStudent(Student student);

    Student getStudentById(Integer id);

    void updateStudent(Student student);

    void deleteStudents(Integer[] ids);

    void updateViolation(Integer id, Integer score);
}
