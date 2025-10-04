package com.itheima.service;

import com.itheima.pojo.ClazzOption;
import com.itheima.pojo.DegreeOption;
import com.itheima.pojo.JobOption;

import java.util.List;
import java.util.Map;

public interface ReportService {
    /**
     * 统计各个职位的员工人数
     */
    JobOption getEmpJobData();

    /**
     * 统计员工性别信息
     */
    List<Map> getEmpGenderData();

    /**
     * 统计每个班级的人数
     */
    ClazzOption getClazzNameData();

    /**
     * 统计学员的学历信息
     * @return
     */
    DegreeOption[] getStudentDegreeData();
}
