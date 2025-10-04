package com.itheima.controller;

import com.itheima.pojo.*;
import com.itheima.service.EmpLogService;
import com.itheima.service.EmpService;
import com.itheima.service.PwService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@RequestMapping("/login")
@RestController
public class LoginController {

    @Autowired
    private EmpService empService;
    
    @Autowired
    private EmpLogService empLogService;

    @Autowired
    private PwService pwService;

    @PostMapping
    public Result login(@RequestBody Emp emp) {
        log.info("员工登录 emp:{}", emp);
        LoginInfo loginInfo = empService.login(emp);
        if (loginInfo != null){
            // 记录登录成功日志
            EmpLog empLog = new EmpLog(null, LocalDateTime.now(), "员工登录成功: " + loginInfo.getName() + "(" + loginInfo.getUsername() + ")");
            empLogService.insertLog(empLog);
            log.info("员工登录成功: {}", loginInfo.getName());
            return Result.success(loginInfo);
        }
        // 记录登录失败日志
        EmpLog empLog = new EmpLog(null, LocalDateTime.now(), "员工登录失败: " + emp.getUsername());
        empLogService.insertLog(empLog);
        log.info("员工登录失败: {}", emp.getUsername());
        return Result.error("用户名或密码错误");
    }

    @PostMapping("/changePassword")
    public Result changePassword(@RequestBody Pw pw) {
        // 记录完整的Pw对象
        log.info("修改密码请求Pw对象: {}", pw);
        
        // 记录详细的密码字段值
        log.info("旧密码: {}", pw.getOldPassword());
        log.info("新密码: {}", pw.getNewPassword());
//        log.info("确认密码: {}", pw.getConfirmPassword());
        
        try {
            // 调用服务层方法修改密码
            pwService.changePw(pw);
            log.info("修改密码成功");
            // 返回成功结果，code为1表示成功
            return Result.success();
        } catch (RuntimeException e) {
            log.error("修改密码失败: {}", e.getMessage());
            // 返回失败结果，包含错误信息
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("修改密码发生异常: {}", e.getMessage());
            // 返回系统错误
            return Result.error("修改密码失败，请联系管理员");
        }
    }


}
