package com.study.templateapi.global.interceptor;

import com.study.templateapi.global.error.ErrorCode;
import com.study.templateapi.global.error.exception.AuthenticationException;
import com.study.templateapi.global.jwt.constant.TokenType;
import com.study.templateapi.global.jwt.service.TokenManager;
import com.study.templateapi.global.util.AuthorizationHeaderUtils;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final TokenManager tokenManager;

    /**
     * 컨트롤러 로직을 수행하기 전에 먼저 수행됨
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. Authorization Header 검증
        String authorizationHeader = request.getHeader("Authorization");
        // 1-a. Authorization Header가 빈 값인지와 앞에 Bearer 가 붙어서 오는지 검증
        AuthorizationHeaderUtils.validateAuthorization(authorizationHeader);

        // 2. 토큰 검증
        String token = authorizationHeader.split(" ")[1];
        tokenManager.validateToken(token);

        // 3. 토큰 타입
        Claims tokenClaims = tokenManager.getTokenClaims(token);
        String tokenType = tokenClaims.getSubject();
        if(!TokenType.isAccessToken(tokenType)) {
            throw new AuthenticationException(ErrorCode.NOT_ACCESS_TOKEN_TYPE);
        }

        return true;
    }
}
