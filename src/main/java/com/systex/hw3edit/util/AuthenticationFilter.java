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
            handleRegister(request, response, session);
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
        return requestURI.contains("/login") || requestURI.contains("/ajaxLogin") || requestURI.contains("/register");
    }

    /**
     * 處理登入驗證
     */
//    private void handleLogin(HttpServletRequest request, HttpSession session) {
//        String email = request.getParameter("email");
//        String password = request.getParameter("password");
//        Users loginUser = null;
//        try {
//            loginUser = usersService.login(email, password);
//
//        } catch (Exception e) {
//            request.setAttribute("error", "請先登入");
//            return;
//        }
//        if (loginUser != null) {
//            session.setAttribute("loggedIn", loginUser);
//            session.setAttribute("username", loginUser.getUsername());
//        } else {
//            request.setAttribute("error", "電子信箱或密碼錯誤");
//        }
//    }
    private void handleLogin(HttpServletRequest request, HttpSession session)
            throws ServletException, IOException {

        Users user;

        // 獲取用戶名和密碼
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // 嘗試查詢用戶
        try {
            user = usersService.login(email, password);
            // 登錄成功，將用戶存入 session
            if (session == null) {
                session = request.getSession(); // 如果 session 不存在，創建一個新的
            }
            session.setAttribute("loggedIn", user);
            return;
        } catch (Exception e) {
            // 找不到用戶，返回錯誤信息並轉發回登入頁
            request.setAttribute("error", "電子信箱或密碼錯誤!");
            return;
        }

//        // 驗證密碼
//        if (user != null) {
//            // 登錄成功，將用戶存入 session
//            if (session == null) {
//                session = request.getSession(); // 如果 session 不存在，創建一個新的
//            }
//            session.setAttribute("loggedIn", user);
//            return;
//        } else {
//            request.setAttribute("error", "用戶名或密碼錯誤");
//            return;
//        }
    }

    /**
     * 處理註冊驗證
     */
//    private void handleRegister(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
//        String email = request.getParameter("email");
//        String password = request.getParameter("password");
//        String userName = request.getParameter("username");
//
//        try {
//            usersService.register(email, password, userName);
//            // 註冊成功後可重定向至登入頁面
//            response.sendRedirect(request.getContextPath() + "/login");
//        } catch (Exception e) {
//            request.setAttribute("error", e.getMessage()); // 傳遞錯誤訊息
//            // 可以在這裡將錯誤訊息傳遞至註冊頁面，或者返回錯誤處理邏輯
//        }
//    }
    private void handleRegister(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws ServletException, IOException {

        Users newUser;

        // 獲取用戶名和密碼
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String username = request.getParameter("confirmPassword");

//        // 檢查密碼是否一致
//        if (!password.equals(confirmPassword)) {
//            // 密碼不一致，返回錯誤信息
//            request.setAttribute("error", "密碼不一致");
//            return;
//        }

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
