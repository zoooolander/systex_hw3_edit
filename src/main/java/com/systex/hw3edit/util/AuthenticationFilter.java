package com.systex.hw3edit.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.systex.hw3edit.command.IdentityCommand;
import com.systex.hw3edit.error.SystemException;
import com.systex.hw3edit.error.UsersException;
import com.systex.hw3edit.model.SystemErrorCode;
import com.systex.hw3edit.model.UserErrorCode;
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
import java.io.UnsupportedEncodingException;
import java.util.Map;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UsersService usersService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IdentityCommand identityCommand;

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

        // 不阻擋資料庫路徑
        if (isH2ConsolePath(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 若 session 已設定 login，則不阻擋
        HttpSession session = request.getSession();
        if (!isLoggedIn(session) && !isAllowedWithoutLogin(request.getRequestURI())) {
            response.sendRedirect(request.getContextPath() + LOGIN_PATH);
            return;
        }

        // 判斷是哪種登入或註冊方式
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

    /**
     * 設定編碼方式
     */
    private void setupEncoding(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        request.setCharacterEncoding(UTF_8);
        response.setCharacterEncoding(UTF_8);
    }

    /**
     * 判斷是否為資料庫路徑
     */
    private boolean isH2ConsolePath(HttpServletRequest request) {
        return request.getRequestURI().contains(H2_CONSOLE_PATH);
    }

    /**
     * 判斷是否已經登入
     */
    private boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("loggedIn") != null;
    }

    /**
     * 判斷是否為不須阻擋的路徑
     */
    private boolean isAllowedWithoutLogin(String requestURI) {
        return requestURI.contains(LOGIN_PATH) || requestURI.contains(AJAX_LOGIN_PATH) ||
                requestURI.contains(REGISTER_PATH) || requestURI.contains(AJAX_REGISTER_PATH);
    }

    /**
     * 判斷是否為登入請求
     */
    private boolean isLoginRequest(HttpServletRequest request) {
        return request.getRequestURI().contains(LOGIN_PATH) && request.getMethod().equalsIgnoreCase("POST");
    }

    /**
     * 判斷是否為 Ajax 登入請求
     */
    private boolean isAjaxLoginRequest(HttpServletRequest request) {
        return request.getRequestURI().contains(AJAX_LOGIN_PATH) && request.getMethod().equalsIgnoreCase("POST");
    }

    /**
     * 判斷是否為註冊請求
     */
    private boolean isRegisterRequest(HttpServletRequest request) {
        return request.getRequestURI().contains(REGISTER_PATH) && request.getMethod().equalsIgnoreCase("POST");
    }

    /**
     * 判斷是否為 Ajax 註冊請求
     */
    private boolean isAjaxRegisterRequest(HttpServletRequest request) {
        return request.getRequestURI().contains(AJAX_REGISTER_PATH) && request.getMethod().equalsIgnoreCase("POST");
    }

    // todo template pattern?
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
            //writeJsonResponse(response, createSuccessResponse(session, loginUser));
            writeJsonResponse(response, createSuccessCommand(session, loginUser));
        } catch (UsersException e) {
            // 捕獲登入失敗例外，將錯誤訊息發送到前端
            //writeJsonResponse(response, createErrorResponse(e.getErrorCode().getErrorMessage()));
            writeJsonResponse(response, createErrorCommand(e.getErrorCode().getErrorMessage()));
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
           // writeJsonResponse(response, createSuccessResponse(session, newUser));
            writeJsonResponse(response, createSuccessCommand(session, newUser));
        } catch (UsersException e) {
            //writeJsonResponse(response, createErrorResponse(e.getErrorCode().getErrorMessage()));
            writeJsonResponse(response, createErrorCommand(e.getErrorCode().getErrorMessage()));
            //response.getWriter();
        }
    }

    /**
     * 新增用戶
     */
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

    /**
     * 讀取 json file
     */
    private <T> T readJsonData(HttpServletRequest request, Class<T> valueType) throws IOException {
        return objectMapper.readValue(request.getReader(), valueType);
    }

    /**
     * 回寫 json response
     */
    private void writeJsonResponse(HttpServletResponse response, String jsonString) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(jsonString);
    }

    private String createSuccessResponse(HttpSession session, Users user) throws JsonProcessingException {
        setUserSession(session, user);
        identityCommand.setStatus("success");
        return objectMapper.writeValueAsString(Map.of("status", "success"));
    }

    private String createErrorResponse(String message) throws JsonProcessingException {
        identityCommand.setStatus("error");
        identityCommand.setMessage(message);
        return objectMapper.writeValueAsString(Map.of("status", "error", "message", message));
    }

    private IdentityCommand createSuccessCommand(HttpSession session, Users user) {
        setUserSession(session, user);
        return new IdentityCommand("success", null, user);
    }

    private IdentityCommand createErrorCommand(String message) {
        return new IdentityCommand("error", message, null);
    }

    private void writeJsonResponse(HttpServletResponse response, IdentityCommand identityCommand) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        objectMapper.writeValue(response.getWriter(), identityCommand);
    }


    /**
     * 例外處理
     */
    private void handleException(HttpServletResponse response, Exception e) throws IOException {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        //writeJsonResponse(response, createErrorResponse(SystemErrorCode.INTERNAL_ERROR.getErrorMessage()));
        writeJsonResponse(response, createErrorCommand(SystemErrorCode.INTERNAL_ERROR.getErrorMessage()));
        e.printStackTrace();
    }
}