package com.itheima.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;

import java.io.IOException;

//@WebFilter(urlPatterns = "/*")
public class DemoFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("Init初始化");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("拦截到了请求...");
    }

    @Override
    public void destroy() {
        System.out.println("destroy...");
    }
}
