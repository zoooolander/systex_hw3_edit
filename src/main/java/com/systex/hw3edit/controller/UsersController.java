package com.systex.hw3edit.controller;

import com.systex.hw3edit.model.Users;
import com.systex.hw3edit.repository.UsersRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UsersController {

    @Autowired
    private UsersRepository usersRepository;

    /**
     * @Description 顯示登入頁面
     */
    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("users", new Users());
        return "login";
    }

    /**
     * @Description 登入處理
     * @param email
     * @param password
     * @param session
     * @param model
     * @return
     */
    @PostMapping("/login")
    public ModelAndView handleLogin(@RequestParam String email, @RequestParam String password,
                                    HttpSession session, Model model) {

        Users user = null;

        try {
            // 嘗試查找用戶
            user = usersRepository.findByEmailAndPassword(email, password);
        } catch (Exception e) {
            model.addAttribute("error");
            return new ModelAndView("login");
        }

        // 檢查用戶是否存在並驗證密碼
        if (user != null) {
            session.setAttribute("loggedIn", user);
            return new ModelAndView("lottery/main"); // 重定向至 main.jsp
        } else {
            model.addAttribute("error", "invalid email or password");
        }

        return new ModelAndView("login"); // 返回 login 視圖
    }

    /**
     * @Description 顯示註冊頁面
     */
    @GetMapping("/register")
    public ModelAndView showRegisterPage() {
        return new ModelAndView("register.jsp", "user", new Users());
    }

    /**
     * @Description 註冊處理
     * @param user
     * @param model
     * @return
     */
    @PostMapping("/register")
    public ModelAndView handleRegister(@ModelAttribute Users user, Model model) {
        // 檢查此 email 是否已存在
        if (usersRepository.findByEmail(user.getEmail()) != null) {
            model.addAttribute("error", "This email already exists");
            return new ModelAndView("register");
        }
        try {
            usersRepository.save(user);
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed");
            return new ModelAndView("register");
        }
        return new ModelAndView("redirect:/login");
    }

    @PostMapping("/logout")
    public ModelAndView handleLogout(HttpSession session) {
        session.removeAttribute("loggedIn");
        return new ModelAndView("redirect:/login");
    }

}
