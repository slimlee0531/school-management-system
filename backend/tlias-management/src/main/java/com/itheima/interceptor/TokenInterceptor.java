package com.itheima.interceptor;

import com.itheima.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 获取请求URL
        String url = request.getRequestURL().toString();
        log.debug("请求路径: {}", url);

        // 2. 判断URL中是否包含login，如果包含，说明是登录请求，直接放行
        if (url.contains("login")) {
            log.debug("登录请求，直接放行");
            return true;
        }

        // 3. 获取请求头中的令牌
        String jwt = request.getHeader("token");

        // 4. 判断令牌是否存在，如果不存在，返回错误结果（未登录）
        if (StringUtils.isEmpty(jwt)) {
            log.info("jwt令牌为空，返回错误结果");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }

        // 5. 解析token，如果解析失败，返回错误结果
        try {
            // 解析JWT令牌
            Claims claims = jwtUtils.parseJwt(jwt);
            log.info("token解析成功");
        } catch (Exception e) {
            log.info("解析令牌失败，返回错误结果: {}", e.getMessage());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }

        // 6. 放行
        log.debug("令牌合法，放行");
        return true;
    }
}
