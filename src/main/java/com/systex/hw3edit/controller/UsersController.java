package com.systex.hw3edit.controller;

import com.systex.hw3edit.model.Users;
import com.systex.hw3edit.repository.UsersRepository;
import com.systex.hw3edit.service.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UsersController {

    /**
     * 顯示登入頁面
     */
    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("users", new Users());
        return "login";
    }

    /**
     * 處理 login 和 Ajax login 邏輯
     */
    @PostMapping(value = {"/login", "/ajaxLogin"})
    public ModelAndView handleLogin(HttpServletRequest request, HttpSession session, Model model) {
        // 取得錯誤訊息
        Object error = request.getAttribute("error");

        // 如果已登入，重定向到主頁
        if (session.getAttribute("loggedIn") != null) {
            return new ModelAndView("redirect:/lottery/main.jsp");
        }

        model.addAttribute("error", error);

        // 判斷是否為 AJAX 請求
        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            return new ModelAndView("ajaxLogin");
        }

        // 非 AJAX 請求，返回登入頁面
        return new ModelAndView("login");
    }


    /**
     * 顯示註冊頁面
     */
    @GetMapping("/register")
    public ModelAndView showRegisterPage() {
        return new ModelAndView("register", "user", new Users());
    }

    /**
     * 處理 register 和 Ajax register 邏輯
     */
    @PostMapping(value = {"/register", "/ajaxRegister"})
    public ModelAndView handleRegister(HttpServletRequest request, HttpSession session, Model model) {
        // 取得錯誤訊息
        Object error = request.getAttribute("error");

        model.addAttribute("error", error);

        // 判斷是否為 AJAX 請求
        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            return new ModelAndView("ajaxRegister");
        }

        // 如果有錯誤訊息，返回註冊頁面
        if(error != null) {
            return new ModelAndView("register");
        }
        return new ModelAndView("ajaxLogin"); // 註冊成功後重定向到登入頁面
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
