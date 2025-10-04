package com.itheima.mapper;

import com.itheima.pojo.Emp;
import com.itheima.pojo.EmpExpr;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 工作经历
 */
@Mapper
public interface EmpExprMapper {

    /**
     * 批量保存员工的工作经历信息
     */
    void insertBatch(List<EmpExpr> empExprList);

    void deleteByEmpIds(List<Integer> ids);
}
