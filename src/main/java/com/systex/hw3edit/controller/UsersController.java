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
    public ModelAndView handleLogin(HttpServletRequest request,HttpSession session, Model model) {
        // 取得錯誤訊息
        Object error = request.getAttribute("error");

        if (session.getAttribute("loggedIn") != null) {
            return new ModelAndView("redirect:/lottery/main.jsp");
        }

        model.addAttribute("error", error);
        return new ModelAndView("login");
    }

    /**
     * 處理 AJAX 登入邏輯
     */
    @PostMapping(value = "/ajaxLogin", headers = "X-Requested-With=XMLHttpRequest")
    public ModelAndView handleAjaxLogin(HttpServletRequest request,HttpSession session, Model model) {
        Object error = request.getAttribute("error");
        if(session.getAttribute("loggedIn") != null) {
            return new ModelAndView("redirect:/lottery/main.jsp");
        }
        model.addAttribute("error", error);
        return new ModelAndView("ajaxLogin");
    }

    /**
     * 顯示註冊頁面
     */
    @GetMapping("/register")
    public ModelAndView showRegisterPage() {
        return new ModelAndView("register", "user", new Users());
    }

    /**
     * 處理註冊邏輯
     */
//    @PostMapping("/register")
//    public ModelAndView handleRegister(@ModelAttribute Users user, HttpServletRequest request, Model model) {
//        try {
//            if ("register success".equals(usersService.register(user.getEmail(), user.getPassword(), user.getUsername()))) {
//                return new ModelAndView("redirect:/login"); // 註冊成功後重定向至登入頁面
//            }
//        } catch (Exception e) {
//            model.addAttribute("error", e.getMessage()); // 設置正確的錯誤訊息
//        }
//
//        return new ModelAndView("register"); // 返回註冊頁面並顯示錯誤訊息
//    }

    @PostMapping("/register")
    public ModelAndView handleRegister(HttpServletRequest request, HttpSession session, Model model) {
        // 取得錯誤訊息
        Object error = request.getAttribute("error");

        model.addAttribute("error", error);

        // 如果有錯誤訊息，返回註冊頁面
        if(error != null) {
            return new ModelAndView("register");
        }

        return new ModelAndView("redirect:/login"); // 註冊成功後重定向到登入頁面
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
