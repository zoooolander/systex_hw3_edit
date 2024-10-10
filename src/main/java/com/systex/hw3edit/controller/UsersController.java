package com.systex.hw3edit.controller;

import com.systex.hw3edit.model.Users;
import com.systex.hw3edit.repository.UsersRepository;
import com.systex.hw3edit.service.UsersService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UsersController {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private UsersService usersService;

    /**
     * 顯示登入頁面
     */
    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("users", new Users());
        return "login";
    }

    /**
     * 處理登入邏輯
     */
    @PostMapping("/login")
    public ModelAndView handleLogin(HttpSession session, Model model) {
        if (session.getAttribute("loggedIn") != null) {
            return new ModelAndView("redirect:/lottery/main.jsp");
        }

        model.addAttribute("error", "找不到用戶，請先註冊");
        return new ModelAndView("login");
    }


//    /**
//     * 處理 AJAX 登入邏輯
//     */
//    @PostMapping("/ajax_login")
//    public ResponseEntity<Map<String, Object>> handleAjaxLogin(@RequestBody Map<String, String> loginData,
//                                                               HttpSession session) {
//        String email = loginData.get("email");
//        String password = loginData.get("password");
//
//        Users user = usersRepository.findByEmailAndPassword(email, password);
//        Map<String, Object> response = new HashMap<>();
//
//        if (user != null) {
//            session.setAttribute("loggedIn", user);
//            session.setAttribute("username", user.getUsername());
//            response.put("success", true);
//        } else {
//            response.put("success", false);
//            response.put("message", "Invalid email or password");
//        }
//
//        return ResponseEntity.ok(response);
//    }

    /**
     * 顯示註冊頁面
     */
    @GetMapping("/register")
    public ModelAndView showRegisterPage() {
        return new ModelAndView("register.jsp", "user", new Users());
    }

    /**
     * 處理註冊邏輯
     */
    @PostMapping("/register")
    public ModelAndView handleRegister(@ModelAttribute Users user, HttpServletRequest request, Model model) {
        try {
            usersService.register(user.getEmail(), user.getPassword(), user.getUsername());
            return new ModelAndView("redirect:/login"); // 註冊成功後重定向至登入頁面
        } catch (Exception e) {
            //ModelAndView modelAndView = new ModelAndView("register");
            model.addAttribute("error", e.getMessage()); // 設置錯誤訊息
            return new ModelAndView("register"); // 返回註冊頁面並顯示錯誤訊息
        }
    }

    /**
     * 處理登出邏輯
     */
    @PostMapping("/logout")
    public ModelAndView handleLogout(HttpSession session) {
        session.removeAttribute("loggedIn");
        return new ModelAndView("redirect:/login");
    }
}
