package com.study.templateapi.global.error.exception;

import com.study.templateapi.global.error.ErrorCode;

public class AuthenticationException extends BusinessException {
    public AuthenticationException(ErrorCode errorCode) {
        super(errorCode);
    }
}
