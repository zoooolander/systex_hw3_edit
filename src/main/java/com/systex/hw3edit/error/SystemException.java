package com.systex.hw3edit.error;

import com.systex.hw3edit.model.SystemErrorCode;
import com.systex.hw3edit.model.UserErrorCode;
import lombok.Getter;

@Getter
public class SystemException extends Exception {
    private final SystemErrorCode errorCode;

    public SystemException(SystemErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }
}
