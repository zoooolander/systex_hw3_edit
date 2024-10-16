package com.systex.hw3edit.service;

import com.systex.hw3edit.error.UsersException;
import com.systex.hw3edit.model.UserErrorCode;
import com.systex.hw3edit.model.Users;
import com.systex.hw3edit.repository.UsersRepository;
import jdk.jshell.spi.ExecutionControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsersService {

    @Autowired
    private UsersRepository usersRepository;

    /**
     * 登入處理，驗證使用者是否已具有系統用戶身分
     */
    public Users verifyAccount(String email, String password) throws UsersException {
        Users loginUser = usersRepository.findByEmailAndPassword(email, password);

        // 檢查帳號密碼是否正確
        if (loginUser == null) {
            throw new UsersException(UserErrorCode.USER_NOT_FOUND);
        }

        return loginUser;
    }

    /**
     * 註冊處理，檢查此用戶是否存在
     */
    public void checkIfUserExists(String email) throws UsersException {
        Users existedUser = usersRepository.findByEmail(email);

        if (existedUser != null) {
            throw new UsersException(UserErrorCode.USER_ALREADY_EXISTS);
        }
    }

    public void save(Users user) {
        usersRepository.save(user);
    }

}
