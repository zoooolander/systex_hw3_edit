package com.systex.hw3edit.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.systex.hw3edit.error.UsersException;
import com.systex.hw3edit.model.UserErrorCode;
import com.systex.hw3edit.model.Users;
import com.systex.hw3edit.service.UsersService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jdk.jshell.spi.ExecutionControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

//@Component
//public class AuthenticationFilter extends OncePerRequestFilter {
//
//    @Autowired
//    private UsersService usersService;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//
//        // 設置編碼
//        request.setCharacterEncoding("UTF-8");
//        response.setCharacterEncoding("UTF-8");
//
//        HttpSession session = request.getSession();
//        Object user = session.getAttribute("loggedIn");
//
//        // 不阻擋 H2 資料庫控制台
//        if (request.getRequestURI().contains("/h2-console")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        // 如果未登入，且不是登入或註冊頁面，則重定向至登入頁面
//        if (user == null && !isAllowedWithoutLogin(request.getRequestURI())) {
//            response.sendRedirect(request.getContextPath() + "/login");
//            return;
//        }
//
//        // todo
//        if (isLoginRequest(request)) {
//            handleLogin(request, response, session);
//        } else if (isAjaxLoginRequest(request)) {
//            try {
//                handleAjaxLogin(request, response, session);
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//            return; // AJAX請求直接返回，不繼續處理其他請求
//        } else if (isRegisterRequest(request)) {
//            handleRegister(request, response, session);
//        } else if (isAjaxRegisterRequest(request)) {
//            try {
//                handleAjaxRegister(request, response, session);
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//            return; // AJAX註冊請求直接返回，不繼續處理其他請求
//        }
//
//        // 繼續處理其他請求
//        filterChain.doFilter(request, response);
//        return;
//    }
//
//
//    // 判斷是否為登入請求
//    private boolean isLoginRequest(HttpServletRequest request) {
//        return request.getRequestURI().contains("/login") && request.getMethod().equalsIgnoreCase("POST");
//    }
//    // 判斷是否為 AJAX 登入請求
//    private boolean isAjaxLoginRequest(HttpServletRequest request) {
//        return request.getRequestURI().contains("/ajaxLogin") && request.getMethod().equalsIgnoreCase("POST");
//    }
//
//    // 判斷是否為註冊請求
//    private boolean isRegisterRequest(HttpServletRequest request) {
//        return request.getRequestURI().contains("/register") && request.getMethod().equalsIgnoreCase("POST");
//    }
//    // 判斷是否為 AJAX 登入請求
//    private boolean isAjaxRegisterRequest(HttpServletRequest request) {
//        return request.getRequestURI().contains("/ajaxRegister") && request.getMethod().equalsIgnoreCase("POST");
//    }
//
//    // 判斷是否為不需要登入的請求（如 login、register）
//    private boolean isAllowedWithoutLogin(String requestURI) {
//        return requestURI.contains("/login") || requestURI.contains("/ajaxLogin") || requestURI.contains("/register")|| requestURI.contains("/ajaxRegister");
//    }
//
//    /**
//     * 處理登入驗證
//     */
//    private void handleLogin(HttpServletRequest request, HttpServletResponse response,HttpSession session)
//            throws ServletException, IOException {
//
//        // 獲取用戶名和密碼
//        String email = request.getParameter("email");
//        String password = request.getParameter("password");
//        Users loginUser = null;
//        try {
//            loginUser = usersService.verifyAccount(email, password);
//
//        } catch (Exception e) {
//            request.setAttribute("error", e.getMessage());
//            return;
//        }
//        if (loginUser != null) {
//            session.setAttribute("loggedIn", loginUser);
//            session.setAttribute("username", loginUser.getUsername());
//        } else {
//            request.setAttribute("error", "電子信箱或密碼錯誤");
//        }
//    }
//
//    /**
//     * 處理 AJAX 登入驗證
//     */
//    private void handleAjaxLogin(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
//        // 讀取 JSON 資料
//        StringBuilder jsonBuilder = new StringBuilder();
//        try (BufferedReader reader = request.getReader()) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                jsonBuilder.append(line);
//            }
//        }
//
//        // 解析 JSON 為用戶名和密碼
//        ObjectMapper objectMapper = new ObjectMapper();
//        Users data = objectMapper.readValue(jsonBuilder.toString(), Users.class);
//        String email = data.getEmail();
//        String password = data.getPassword();
//
//
//        try {
//            // 驗證用戶
//            Users verifyUser = usersService.verifyAccount(email, password);
//
//            // 登入成功，保存到 session 並回傳 JSON response
//            session.setAttribute("loggedIn", verifyUser);
//            response.setContentType("application/json;charset=UTF-8");
//            response.getWriter().write("{\"status\":\"success\"}");
//
//        } catch (UsersException e) {
//            // 捕獲 UserException，回傳錯誤訊息
//            response.setContentType("application/json;charset=UTF-8");
//            response.getWriter().write("{\"status\":\"error\", \"message\":\"" + e.getMessage() + "\"}");
//        }
//
//    }
//
//    /**
//     * 處理註冊邏輯
//     */
//    private void handleRegister(HttpServletRequest request, HttpServletResponse response, HttpSession session)
//            throws ServletException, IOException {
//
//        Users newUser;
//
//        // 獲取用戶名和密碼
//        String email = request.getParameter("email");
//        String password = request.getParameter("password");
//        String username = request.getParameter("username");
//
//        // 檢查email是否已存在
//        try {
//            usersService.checkIfUserExists(email);
//        } catch (Exception e) {
//            // 用戶名已存在，返回錯誤信息
//            request.setAttribute("error", e.getMessage());
//            return;
//        }
//
//        // 創建新用戶
//        newUser = new Users();
//        newUser.setEmail(email);
//        newUser.setPassword(password);
//        newUser.setUsername(username);
//
//        try {
//            usersService.save(newUser);
//        } catch (Exception e) {
//            // 保存失敗，返回錯誤信息
//            request.setAttribute("error", e.getMessage());
//        }
//    }
//    /**
//     * 處理 AJAX 註冊邏輯
//     */
//    private void handleAjaxRegister(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
//        Users newUser;
//        // 讀取 JSON 資料
//        StringBuilder jsonBuilder = new StringBuilder();
//        try (BufferedReader reader = request.getReader()) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                jsonBuilder.append(line);
//            }
//        }
//
//        // 解析 JSON 為用戶名和密碼
//        ObjectMapper objectMapper = new ObjectMapper();
//        Users data = objectMapper.readValue(jsonBuilder.toString(), Users.class);
//        String email = data.getEmail();
//        String password = data.getPassword();
//        String username = data.getUsername();
//
//        // 驗證用戶
//        response.setContentType("application/json;charset=UTF-8");
//        try {
//            usersService.checkIfUserExists(email);
//        } catch (Exception e) {
//            // 用戶名已存在，返回錯誤信息
//            response.getWriter().write("{\"status\":\"error\", \"message\":\"" + e.getMessage() + "\"}");
//
//            return;
//        }
//        // 創建新用戶
//        newUser = new Users();
//        newUser.setEmail(email);
//        newUser.setPassword(password);
//        newUser.setUsername(username);
//
//
//        try {
//            usersService.save(newUser);
//            session.setAttribute("loggedIn", newUser);
//            response.getWriter().write("{\"status\":\"success\"}");
//        } catch (Exception e) {
//            // 保存失敗，返回錯誤信息
//            //request.setAttribute("error", "註冊失敗，請重試");
//            response.getWriter().write("{\"status\":\"error\", \"message\":\"" + e.getMessage() + "\"}");
//
//        }
//    }
//}
@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UsersService usersService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String UTF_8 = "UTF-8";
    private static final String LOGIN_PATH = "/login";
    private static final String REGISTER_PATH = "/register";
    private static final String AJAX_LOGIN_PATH = "/ajaxLogin";
    private static final String AJAX_REGISTER_PATH = "/ajaxRegister";
    private static final String H2_CONSOLE_PATH = "/h2-console";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        setupEncoding(request, response);

        if (isH2ConsolePath(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        HttpSession session = request.getSession();
        if (!isLoggedIn(session) && !isAllowedWithoutLogin(request.getRequestURI())) {
            response.sendRedirect(request.getContextPath() + LOGIN_PATH);
            return;
        }

        try {
            if (isLoginRequest(request)) {
                handleLogin(request, response, session);
            } else if (isAjaxLoginRequest(request)) {
                handleAjaxLogin(request, response, session);
                return;
            } else if (isRegisterRequest(request)) {
                handleRegister(request, response, session);
            } else if (isAjaxRegisterRequest(request)) {
                handleAjaxRegister(request, response, session);
                return;
            }
        } catch (Exception e) {
            handleException(response, e);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void setupEncoding(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        request.setCharacterEncoding(UTF_8);
        response.setCharacterEncoding(UTF_8);
    }

    private boolean isH2ConsolePath(HttpServletRequest request) {
        return request.getRequestURI().contains(H2_CONSOLE_PATH);
    }

    private boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("loggedIn") != null;
    }

    private boolean isAllowedWithoutLogin(String requestURI) {
        return requestURI.contains(LOGIN_PATH) || requestURI.contains(AJAX_LOGIN_PATH) ||
                requestURI.contains(REGISTER_PATH) || requestURI.contains(AJAX_REGISTER_PATH);
    }

    private boolean isLoginRequest(HttpServletRequest request) {
        return request.getRequestURI().contains(LOGIN_PATH) && request.getMethod().equalsIgnoreCase("POST");
    }

    private boolean isAjaxLoginRequest(HttpServletRequest request) {
        return request.getRequestURI().contains(AJAX_LOGIN_PATH) && request.getMethod().equalsIgnoreCase("POST");
    }

    private boolean isRegisterRequest(HttpServletRequest request) {
        return request.getRequestURI().contains(REGISTER_PATH) && request.getMethod().equalsIgnoreCase("POST");
    }

    private boolean isAjaxRegisterRequest(HttpServletRequest request) {
        return request.getRequestURI().contains(AJAX_REGISTER_PATH) && request.getMethod().equalsIgnoreCase("POST");
    }

    /**
     * 處理登入驗證
     */
    private void handleLogin(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        try {
            // 嘗試取得登入用戶
            Users loginUser = usersService.verifyAccount(email, password);
            // 若找到用戶，設置 session
            setUserSession(session, loginUser);
        } catch (UsersException e) {
            // 捕獲登入失敗例外，將錯誤訊息設置到 request 中
            request.setAttribute("error", e.getErrorCode().getErrorMessage());
        }
    }

    /**
     * 處理 Ajax 登入驗證
     */
    private void handleAjaxLogin(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        Users data = readJsonData(request, Users.class);
        try {
            // 嘗試取得登入用戶，並自動處理驗證失敗的情況
            Users loginUser = usersService.verifyAccount(data.getEmail(), data.getPassword());
            // 若找到用戶，則回應成功
            writeJsonResponse(response, createSuccessResponse(session, loginUser));
        } catch (UsersException e) {
            // 捕獲登入失敗例外，將錯誤訊息發送到前端
            writeJsonResponse(response, createErrorResponse(e.getErrorCode().getErrorMessage()));
        }
    }

    /**
     * 處理註冊邏輯
     */
    private void handleRegister(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String username = request.getParameter("username");

        try {
            usersService.checkIfUserExists(email);
            Users newUser = createUser(email, password, username);
            usersService.save(newUser);
        } catch (UsersException e) {
            request.setAttribute("error", e.getErrorCode().getErrorMessage());
        }
    }

    /**
     * 處理 Ajax 註冊邏輯
     */
    private void handleAjaxRegister(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        Users data = readJsonData(request, Users.class);

        try {
            usersService.checkIfUserExists(data.getEmail());
            Users newUser = createUser(data.getEmail(), data.getPassword(), data.getUsername());
            usersService.save(newUser);
            writeJsonResponse(response, createSuccessResponse(session, newUser));
        } catch (UsersException e) {
            writeJsonResponse(response, createErrorResponse(e.getErrorCode().getErrorMessage()));
            response.getWriter();
        }
    }

    private Users createUser(String email, String password, String username) {
        Users newUser = new Users();
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.setUsername(username);
        return newUser;
    }

    /**
     * 儲存 user login 的狀態
     */
    private void setUserSession(HttpSession session, Users user) {
        session.setAttribute("loggedIn", user);
        session.setAttribute("username", user.getUsername());
    }

    private <T> T readJsonData(HttpServletRequest request, Class<T> valueType) throws IOException {
        return objectMapper.readValue(request.getReader(), valueType);
    }

    private void writeJsonResponse(HttpServletResponse response, String jsonString) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(jsonString);
    }

    private String createSuccessResponse(HttpSession session, Users user) throws JsonProcessingException {
        setUserSession(session, user);
        return objectMapper.writeValueAsString(Map.of("status", "success"));
    }

    private String createErrorResponse(String message) throws JsonProcessingException {
        return objectMapper.writeValueAsString(Map.of("status", "error", "message", message));
    }

    private void handleException(HttpServletResponse response, Exception e) throws IOException {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        writeJsonResponse(response, createErrorResponse("An internal error occurred"));
        e.printStackTrace();
    }
}