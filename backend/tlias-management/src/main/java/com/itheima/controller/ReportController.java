package com.itheima.controller;

import com.itheima.pojo.ClazzOption;
import com.itheima.pojo.DegreeOption;
import com.itheima.pojo.JobOption;
import com.itheima.pojo.Result;
import com.itheima.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RequestMapping("/report")
@RestController
public class ReportController {

    @Autowired
    private ReportService reportService;

    /**
     * 统计各个职位的员工人数
     */
    @GetMapping("/empJobData")
    public Result getEmpJobData() {
        log.info("统计各个职位的员工人数");
        JobOption jobOption = reportService.getEmpJobData();
        return Result.success(jobOption);
    }

    /**
     * 统计员工性别信息
     */
    @GetMapping("/empGenderData")
    public Result getEmpGenderData() {
        log.info("统计员工性别信息");
        List<Map> genderList = reportService.getEmpGenderData();
        return Result.success(genderList);
    }

    /**
     * 统计每一个班级的人数
     */
    @GetMapping("/studentCountData")
    public Result getStudentCountData() {
        log.info("统计每一个班级的人数图片");
        ClazzOption clazzOption = reportService.getClazzNameData();
        return Result.success(clazzOption);
    }

    /**
     * 统计学员的学历信息
     */
    @GetMapping("/studentDegreeData")
    public Result getStudentDegreeData() {
        log.info("统计学员的学历信息图片");
        DegreeOption[] degreeOption = reportService.getStudentDegreeData();
        return Result.success(degreeOption);
    }

}
