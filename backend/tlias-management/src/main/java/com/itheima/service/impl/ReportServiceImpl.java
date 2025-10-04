package com.itheima.service.impl;

import com.itheima.mapper.ClazzMapper;
import com.itheima.mapper.EmpMapper;
import com.itheima.pojo.ClazzOption;
import com.itheima.pojo.DegreeOption;
import com.itheima.pojo.JobOption;
import com.itheima.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private EmpMapper empMapper;
    @Autowired
    private ClazzMapper clazzMapper;

    @Override
    public JobOption getEmpJobData() {
        List<Map<String, Object>> list = empMapper.countEmpJobData();
        List<Object> jobList = list.stream().map(dataMap -> dataMap.get("pos")).toList();
        List<Object> dataList = list.stream().map(dataMap -> dataMap.get("total")).toList();
        return new JobOption(jobList, dataList);
    }

    @Override
    public List<Map> getEmpGenderData() {
        return empMapper.countEmpGenderData();
    }

    @Override
    public ClazzOption getClazzNameData() {
        List<Map<String, Object>> list = clazzMapper.countClazzNameData();
        // 提取班级名称
        List<Object> clazzList = list.stream().map(dataMap -> dataMap.get("clazzName")).toList();
        // 提取数量列表
        List<Object> dataList = list.stream().map(dataMap -> dataMap.get("total")).toList();

        return new ClazzOption(clazzList, dataList);
    }

    @Override
    public DegreeOption[] getStudentDegreeData() {
        List<Map<String, Object>> dataList = clazzMapper.countStudentDegreeData();

        // 处理空数组
        if (dataList.isEmpty() ||  dataList.size() == 0) {
            return new  DegreeOption[0];
        }

        // 初始化数组，长度与数据量一致
        DegreeOption[] degreeOptions = new DegreeOption[dataList.size()];

        for  (int i = 0; i < dataList.size(); i++) {
            Map<String, Object> map = dataList.get(i);

            String degreeName = (String) map.get("name");
            Long degreeNum = (Long) map.get("value");
            Integer value = degreeNum.intValue();

            degreeOptions[i] = new DegreeOption(degreeName, value);
        }


        return degreeOptions;
    }
}
