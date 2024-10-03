package com.systex.hw3edit.util;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        Object user = request.getSession().getAttribute("loggedIn");

        // 不阻擋資料庫
        if (request.getRequestURI().contains("/h2-console")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 不阻擋 login 和 register 頁面
        if (user == null && !request.getRequestURI().contains("/login") && !request.getRequestURI().contains("/register")) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        filterChain.doFilter(request, response);
    }

}
