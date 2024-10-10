package com.systex.hw3edit.service;

import com.systex.hw3edit.model.Users;
import com.systex.hw3edit.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

@Service
public class UsersService {

    @Autowired
    private UsersRepository usersRepository;

    public Users login(String email, String password) throws Exception {
        Users loginUser = usersRepository.findByEmailAndPassword(email, password);

        // 檢查帳號密碼是否正確
        if (loginUser == null) {
            throw new Exception("找不到用戶");
        }

        return loginUser;
    }

    public Users register(@ModelAttribute String email, String password, String username)//, Model model)
            throws Exception {
         //檢查此 email 是否已存在
        Users existedUser = usersRepository.findByEmail(email);
        if(existedUser != null){
            throw new Exception("This email already exists");
        }

        Users registerUser = new Users();
        registerUser.setEmail(email);       // 設置 email
        registerUser.setPassword(password); // 設置 password
        registerUser.setUsername(username); // 設置 userName
        usersRepository.save(registerUser);

        return registerUser;
    }

}
