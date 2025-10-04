package com.itheima.mapper;

import com.itheima.pojo.Emp;
import com.itheima.pojo.EmpQueryParam;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 员工信息
 */
@Mapper
public interface EmpMapper {

//    ----------------------------------原始分页查询实现-------------------------------
    /**
     * 查询记录数
     */
    //@Select("select count(*) from emp e left join dept d on e.dept_id = d.id")
    //public Long getTotal();

    /**
     * 分页查询
     */
    //@Select("select e.*, d.name deptName from emp e left join dept d on e.dept_id = d.id " +
    //        "order by e.update_time desc limit #{empNum}, #{pageSize}")
    //public List<Emp> getPage(Integer empNum, Integer pageSize);

    //@Select("select * from emp e left join dept d on e.dept_id = d.id order by e.update_time desc")
    //public List<Emp> getEmps(String name, Integer gender, LocalDate begin, LocalDate end);

    List<Emp> findAllEmps();

    List<Emp> getEmps(EmpQueryParam empQueryParam);

    void updateById(Emp emp);

    void deleteByIds(List<Integer> ids);

    Emp getById(Integer id);

    /**
     * 统计各个职位的员工人数
     *
     * @return
     */
    @MapKey("pos")
    List<Map<String, Object>> countEmpJobData();

    /**
     * 统计员工性别信息
     */
    @MapKey("name")
    List<Map> countEmpGenderData();

    @Select("select * from emp where username = #{username} and password = #{password} ")
    Emp getUsernameAndPassword(Emp emp);

//    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Emp emp);
}
