package com.systex.hw3edit.service;

import com.systex.hw3edit.error.UserAlreadyExistsException;
import com.systex.hw3edit.error.UserNotFoundException;
import com.systex.hw3edit.model.Users;
import com.systex.hw3edit.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsersService {

    @Autowired
    private UsersRepository usersRepository;

    /**
     * 登入處理，驗證是否有會員身分
     */
    public Users verifyAccount(String email, String password) throws Exception {
        Users loginUser = usersRepository.findByEmailAndPassword(email, password);

        // 檢查帳號密碼是否正確
        if (loginUser == null) {
            throw new UserNotFoundException("電子信箱或密碼錯誤");
        }

        return loginUser;
    }

    /**
     * 註冊處理，檢查此用戶是否存在
     */
    public void checkIfUserExists(String email) throws UserAlreadyExistsException {
        Users existedUser = usersRepository.findByEmail(email);

        if (existedUser != null) {
            throw new UserAlreadyExistsException("用戶已存在");
        }
    }

    public void save(Users user) {
        usersRepository.save(user);
    }

}
