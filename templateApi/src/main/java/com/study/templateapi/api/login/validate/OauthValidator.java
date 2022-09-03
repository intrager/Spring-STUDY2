package com.study.templateapi.api.login.validate;

import com.study.templateapi.domain.member.constant.MemberType;
import com.study.templateapi.global.error.ErrorCode;
import com.study.templateapi.global.error.exception.AuthenticationException;
import com.study.templateapi.global.error.exception.BusinessException;
import com.study.templateapi.global.jwt.constant.GrantType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class OauthValidator {

    public void validMemberType(String memberType) {
        if(!MemberType.isMemberType(memberType)) {
            throw new BusinessException(ErrorCode.INVALID_MEMBER_TYPE);
        }
    }
}
