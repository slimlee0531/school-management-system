package com.itheima.filter;

import org.apache.commons.lang3.StringUtils;
import com.itheima.utils.CurrentHolder;
import com.itheima.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 令牌校验过滤器
 * 用于验证JWT令牌并设置当前用户信息
 */
@Slf4j
@WebFilter(urlPatterns = "/*")
@Component
public class TokenFilter implements Filter {
    
    @Autowired
    private JwtUtils jwtUtils;
    
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            // 强转类型
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            HttpServletResponse response = (HttpServletResponse) servletResponse;

            // 1. 获取请求URL
            String url = request.getRequestURL().toString();

            // 2. 判断URL中是否包含login，如果是修改密码请求则不跳过验证
            if (url.contains("login") && !url.contains("changePassword")) {
                log.debug("登录请求，直接放行: {}", url);
                filterChain.doFilter(request, response);
                return;
            }

            // 3. 获取请求头中的令牌
            String jwt = request.getHeader("token");

            // 4. 判断令牌是否存在，如果不存在，返回错误结果（未登录）
            if (StringUtils.isEmpty(jwt)) {
                log.info("jwt令牌为空，返回错误结果");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return;
            }

            // 5. 非空解析
            try {
                Claims claims = jwtUtils.parseJwt(jwt);

                // 存入用户ID
                Integer empId = Integer.valueOf(claims.get("id").toString());
                CurrentHolder.setCurrentId(empId);

                // 存入用户姓名
                String operateEmpName = claims.get("name").toString();
                CurrentHolder.setCurrentEmpName(operateEmpName);

                log.debug("token解析成功，用户ID: {}, 用户名: {}", empId, operateEmpName);
            } catch (Exception e) {
                log.info("解析令牌失败，返回错误结果: {}", e.getMessage());
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return;
            }

            // 6. 放行
            log.debug("令牌合法，放行: {}", url);
            filterChain.doFilter(request, response);
        } finally {
            // 在finally块中清空当前线程绑定的id，确保无论请求处理成功与否，ThreadLocal都会被正确清理
            CurrentHolder.remove();
        }
    }
}
