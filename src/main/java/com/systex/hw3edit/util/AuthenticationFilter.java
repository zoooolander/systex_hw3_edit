package com.systex.hw3edit.util;

import com.systex.hw3edit.model.Users;
import com.systex.hw3edit.service.UsersService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UsersService usersService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Object user = session.getAttribute("loggedIn");

        // 不阻擋 H2 資料庫控制台
        if (request.getRequestURI().contains("/h2-console")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 如果未登入，且不是登入或註冊頁面，則重定向至登入頁面
        if (user == null && !isAllowedWithoutLogin(request.getRequestURI())) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // 處理登入請求
        if (isLoginRequest(request)) {
            handleLogin(request, session);
        }

        // 處理註冊請求
        if (isRegisterRequest(request)) {
            handleRegister(request, session);
        }

        // 繼續處理其他請求
        filterChain.doFilter(request, response);
    }

    // 判斷是否為登入請求
    private boolean isLoginRequest(HttpServletRequest request) {
        return request.getRequestURI().contains("/login") && request.getMethod().equalsIgnoreCase("POST");
    }

    // 判斷是否為註冊請求
    private boolean isRegisterRequest(HttpServletRequest request) {
        return request.getRequestURI().contains("/register") && request.getMethod().equalsIgnoreCase("POST");
    }

    // 判斷是否為不需要登入的請求（如 login、register）
    private boolean isAllowedWithoutLogin(String requestURI) {
        return requestURI.contains("/login") || requestURI.contains("/ajax_login") || requestURI.contains("/register");
    }

    // 處理登入邏輯
    private void handleLogin(HttpServletRequest request, HttpSession session) {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            Users loginUser = usersService.login(email, password);
            session.setAttribute("loggedIn", loginUser);
            session.setAttribute("username", loginUser.getUsername());
        } catch (Exception e) {
            request.setAttribute("error", "Invalid email or password");
        }
    }

    // 處理註冊邏輯
    private void handleRegister(HttpServletRequest request, HttpSession session) {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String userName = request.getParameter("username");

        try {
            Users user = usersService.register(email, password, userName);
        } catch (Exception e) {
            request.setAttribute("error", "Registration failed");
        }
    }
}
