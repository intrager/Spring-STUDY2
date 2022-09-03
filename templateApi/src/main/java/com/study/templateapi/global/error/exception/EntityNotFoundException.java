package com.study.templateapi.global.error.exception;

import com.study.templateapi.global.error.ErrorCode;

public class EntityNotFoundException extends BusinessException {
    public EntityNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
