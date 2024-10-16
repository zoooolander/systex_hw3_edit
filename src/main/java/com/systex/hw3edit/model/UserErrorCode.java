package com.systex.hw3edit.model;

import lombok.Getter;

@Getter
public enum UserErrorCode {
    USER_NOT_FOUND("電子信箱或密碼錯誤！"),
    USER_ALREADY_EXISTS("用戶電子信箱已存在！");

    private final String errorMessage;

    UserErrorCode(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
