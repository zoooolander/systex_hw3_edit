package com.systex.hw3edit.error;

import com.systex.hw3edit.model.UserErrorCode;
import lombok.Getter;

@Getter
public class UsersException extends Exception{
    private final UserErrorCode errorCode;

    public UsersException(UserErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}
