package com.itheima.mapper;

import com.itheima.pojo.Clazz;
import com.itheima.pojo.ClazzQueryParam;
import com.itheima.pojo.Emp;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface ClazzMapper {

    void insert(Clazz clazz);

    List<Clazz> getClazzs(ClazzQueryParam clazzQueryParam);

    @Select("select * from clazz where id = #{id}")
    Clazz findById(Integer id);

    @Update("update clazz set name = #{name}, room = #{room}, begin_date = #{beginDate}, end_date = #{endDate}, master_id = #{masterId}, subject = #{subject}, update_time = #{updateTime} where id = #{id}")
    void update(Clazz clazz);

    @Delete("delete from clazz where id = #{id}")
    void deleteById(Integer id);

    @Select("select count(*) from student where clazz_id = #{id}")
    int countStudentsById(Integer id);

    @Select("select id, name, room, begin_date, end_date, master_id, subject, create_time, update_time from clazz")
    List<Clazz> findAllClazzs();

    @MapKey("clazzName")
    List<Map<String, Object>> countClazzNameData();

    List< Map<String, Object>> countStudentDegreeData();
}
