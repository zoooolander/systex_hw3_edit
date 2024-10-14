package com.systex.hw3edit.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.systex.hw3edit.model.Users;
import com.systex.hw3edit.service.UsersService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
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

        // 設置編碼
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

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

        // todo
        if (isLoginRequest(request)) {
            handleLogin(request, response, session);
        } else if (isAjaxLoginRequest(request)) {
            try {
                handleAjaxLogin(request, response, session);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return; // AJAX請求直接返回，不繼續處理其他請求
        } else if (isRegisterRequest(request)) {
            handleRegister(request, response, session);
        } else if (isAjaxRegisterRequest(request)) {
            try {
                handleAjaxRegister(request, response, session);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return; // AJAX註冊請求直接返回，不繼續處理其他請求
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
    // 判斷是否為 AJAX 登入請求
    private boolean isAjaxRegisterRequest(HttpServletRequest request) {
        return request.getRequestURI().contains("/ajaxRegister") && request.getMethod().equalsIgnoreCase("POST");
    }

    // 判斷是否為不需要登入的請求（如 login、register）
    private boolean isAllowedWithoutLogin(String requestURI) {
        return requestURI.contains("/login") || requestURI.contains("/ajaxLogin") || requestURI.contains("/register")|| requestURI.contains("/ajaxRegister");
    }

    /**
     * 處理登入驗證
     */
    private void handleLogin(HttpServletRequest request, HttpServletResponse response,HttpSession session)
            throws ServletException, IOException {

        // 獲取用戶名和密碼
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        Users loginUser = null;
        try {
            loginUser = usersService.verifyAccount(email, password);

        } catch (Exception e) {
            request.setAttribute("error", "請先登入");
            return;
        }
        if (loginUser != null) {
            session.setAttribute("loggedIn", loginUser);
            session.setAttribute("username", loginUser.getUsername());
        } else {
            request.setAttribute("error", "電子信箱或密碼錯誤");
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

        // 返回 JSON response
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

    /**
     * 處理註冊邏輯
     */
    private void handleRegister(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws ServletException, IOException {

        Users newUser;

        // 獲取用戶名和密碼
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String username = request.getParameter("username");

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
    /**
     * 處理 AJAX 註冊邏輯
     */
    private void handleAjaxRegister(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        Users newUser;
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
        String username = data.getUsername();

        // 驗證用戶
        response.setContentType("application/json;charset=UTF-8");
        try {
            usersService.checkIfUserExists(email);
        } catch (Exception e) {
            // 用戶名已存在，返回錯誤信息
            response.getWriter().write("{\"status\":\"error\", \"message\":\"此電子信箱已存在\"}");

            return;
        }
        // 創建新用戶
        newUser = new Users();
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.setUsername(username);


        try {
            usersService.save(newUser);
            session.setAttribute("loggedIn", newUser);
            response.getWriter().write("{\"status\":\"success\"}");
        } catch (Exception e) {
            // 保存失敗，返回錯誤信息
            request.setAttribute("error", "註冊失敗，請重試");
            response.getWriter().write("{\"status\":\"error\", \"message\":\"Registration fail\"}");

        }
    }
}
