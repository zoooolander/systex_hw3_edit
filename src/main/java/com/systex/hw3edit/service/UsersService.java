package com.systex.hw3edit.service;

import com.systex.hw3edit.error.UserAlreadyExistsException;
import com.systex.hw3edit.error.UserNotFoundException;
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

    /**
     * 登入處理
     */
    public Users login(String email, String password) throws Exception {
        Users loginUser = usersRepository.findByEmailAndPassword(email, password);

        // 檢查帳號密碼是否正確
        if (loginUser == null) {
            throw new UserNotFoundException("電子信箱或密碼錯誤");
        }

        return loginUser;
    }

    /**
     * 註冊處理
     */
    public String register(String email, String password, String username) throws Exception {
        // 檢查此 email 是否已存在
        Users existedUser = usersRepository.findByEmail(email);
        if (existedUser != null) {
            throw new Exception("此電子信箱已存在"); // 拋出異常
        }

        // 創建新用戶並保存
        Users newUser = new Users();
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.setUsername(username);
        usersRepository.save(newUser); // 保存新用戶

        return "register success"; // 返回成功訊息
    }

    public void checkIfUserExists(String email) throws UserAlreadyExistsException {
        Users member = usersRepository.findByEmail(email);

        if (member != null) {
            throw new UserAlreadyExistsException("用戶已存在");
        }
    }

    public void save(Users user) {
        usersRepository.save(user);
    }


}
