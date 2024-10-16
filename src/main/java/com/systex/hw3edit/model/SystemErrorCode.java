package com.systex.hw3edit.model;

import lombok.Getter;

@Getter
public enum SystemErrorCode {
    LOGIN_FAIL("登入失敗，請重試TT"),
    REGISTER_FAIL("註冊失敗，請重試TT"),
    INTERNAL_ERROR("An internal error occurred");

    private final String errorMessage;

    SystemErrorCode(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
