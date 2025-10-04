package com.itheima.service.impl;

import com.itheima.mapper.EmpMapper;
import com.itheima.pojo.Emp;
import com.itheima.pojo.EmpLog;
import com.itheima.pojo.Pw;
import com.itheima.service.EmpLogService;
import com.itheima.service.PwService;
import com.itheima.utils.CurrentHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
public class PwServiceImpl implements PwService {

    @Autowired
    private EmpMapper empMapper;

    @Autowired
    private EmpLogService empLogService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void changePw(Pw pw) {
        // 1. 获取当前登录用户ID
        Integer empId = CurrentHolder.getCurrentId();
        String empName = CurrentHolder.getCurrentEmpName();

        // 2. 检查用户是否已登录
        if (empId == null) {
            throw new RuntimeException("用户未登录，请先登录");
        }

        // 3. 查询当前用户信息
        Emp emp = empMapper.getById(empId);

        // 4. 检查用户是否存在
        if (emp == null) {
            throw new RuntimeException("用户不存在");
        }

        // 5. 验证旧密码是否正确
        if (pw.getOldPassword() == null || !emp.getPassword().equals(pw.getOldPassword())) {
            log.error("旧密码错误: 输入的旧密码[{}] vs 存储的密码[{}]", pw.getOldPassword(), emp.getPassword());
            throw new RuntimeException("旧密码错误");
        }

        // 6. 检查新密码是否为空
        if (pw.getNewPassword() == null || pw.getNewPassword().trim().isEmpty()) {
            log.error("新密码不能为空");
            throw new RuntimeException("新密码不能为空");
        }

//        // 7. 检查确认密码是否为空
//        if (pw.getConfirmPassword() == null || pw.getConfirmPassword().trim().isEmpty()) {
//            log.error("确认密码不能为空");
//            throw new RuntimeException("确认密码不能为空");
//        }

        // 8. 获取并去除密码前后空格
        String newPassword = pw.getNewPassword().trim();
//        String confirmPassword = pw.getConfirmPassword().trim();

        // 9. 记录密码值用于调试
        log.info("新密码值(处理前): {}", pw.getNewPassword());
//        log.info("确认密码值(处理前): {}", pw.getConfirmPassword());
        log.info("新密码值(处理后): {}", newPassword);
//        log.info("确认密码值(处理后): {}", confirmPassword);

//        // 10. 验证新密码和确认密码是否一致
//        if (!newPassword.equals(confirmPassword)) {
//            log.error("密码不一致: 新密码[{}] vs 确认密码[{}]", newPassword, confirmPassword);
//            throw new RuntimeException("两次输入的新密码不一致");
//        }

        // 11. 更新密码
        Emp updateEmp = new Emp();
        updateEmp.setId(empId);
        updateEmp.setPassword(newPassword);
        updateEmp.setUpdateTime(LocalDateTime.now());
        empMapper.updateById(updateEmp);

        // 12. 记录操作日志
        EmpLog empLog = new EmpLog(null, LocalDateTime.now(), "修改密码: " + empName + "(" + emp.getUsername() + ")");
        empLogService.insertLog(empLog);
    }
}
