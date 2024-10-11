package com.systex.hw3edit.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.systex.hw3edit.model.Users;
import com.systex.hw3edit.service.UsersService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.BufferedReader;
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
            handleLogin(request, response, session);
        }

        // 判斷是否為 AJAX 登入請求
        if (isAjaxLoginRequest(request)) {
            try {
                handleAjaxLogin(request, response, session);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return; // 直接返回，不再調用後續的 filterChain
        }

        // 處理註冊請求
        if (isRegisterRequest(request)) {
            handleRegister(request, response, session);
        }

        // 繼續處理其他請求
        filterChain.doFilter(request, response);
        return;
    }

    // 判斷是否為登入請求
    private boolean isLoginRequest(HttpServletRequest request) {
        return request.getRequestURI().contains("/login") && request.getMethod().equalsIgnoreCase("POST");
    }
    // 判斷是否為 AJAX 登入請求
    private boolean isAjaxLoginRequest(HttpServletRequest request) {
        return request.getRequestURI().contains("/ajaxLogin") && request.getMethod().equalsIgnoreCase("POST");
    }

    // 判斷是否為註冊請求
    private boolean isRegisterRequest(HttpServletRequest request) {
        return request.getRequestURI().contains("/register") && request.getMethod().equalsIgnoreCase("POST");
    }

    // 判斷是否為不需要登入的請求（如 login、register）
    private boolean isAllowedWithoutLogin(String requestURI) {
        return requestURI.contains("/login") || requestURI.contains("/ajaxLogin") || requestURI.contains("/register");
    }

    /**
     * 處理登入驗證
     */
    private void handleLogin(HttpServletRequest request, HttpServletResponse response,HttpSession session)
            throws ServletException, IOException {

        Users user;

        // 獲取用戶名和密碼
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // 嘗試查詢用戶
        try {
            user = usersService.verifyAccount(email, password);
            // 登入成功，將用戶存入 session
            if (session == null) {
                session = request.getSession(); // 如果 session 不存在，創建一個新的
            }
            session.setAttribute("loggedIn", user);

        } catch (Exception e) {
            // 找不到用戶，返回錯誤信息並轉發回登入頁
            request.setAttribute("error", "電子信箱或密碼錯誤!");

        }

    }

    /**
     * 處理 AJAX 登入驗證
     */
    private void handleAjaxLogin(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        // 讀取 JSON 資料
        StringBuilder jsonBuilder = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
        }

        // 解析 JSON 為用戶名和密碼
        ObjectMapper objectMapper = new ObjectMapper();
        Users data = objectMapper.readValue(jsonBuilder.toString(), Users.class);
        String email = data.getEmail();
        String password = data.getPassword();

        // 驗證用戶
        Users verifyUser = usersService.verifyAccount(email, password);

        // 返回 JSON 響應
        response.setContentType("application/json;charset=UTF-8");
        if (verifyUser == null) {
            response.getWriter().write("{\"status\":\"error\", \"message\":\"電子信箱或密碼錯誤!\"}");
            return;
        } else {
            // 將用戶保存到 session
            session.setAttribute("loggedIn", verifyUser);
            response.getWriter().write("{\"status\":\"success\"}");
            return;
        }
    }



    private void handleRegister(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws ServletException, IOException {

        Users newUser;

        // 獲取用戶名和密碼
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String username = request.getParameter("confirmPassword");

        // 檢查email是否已存在
        try {
            usersService.checkIfUserExists(email);
        } catch (Exception e) {
            // 用戶名已存在，返回錯誤信息
            request.setAttribute("error", "電子信箱已存在");
            return;
        }

        // 創建新用戶
        newUser = new Users();
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.setUsername(username);

        try {
            usersService.save(newUser);
        } catch (Exception e) {
            // 保存失敗，返回錯誤信息
            request.setAttribute("error", "註冊失敗，請重試");
        }
    }
}
