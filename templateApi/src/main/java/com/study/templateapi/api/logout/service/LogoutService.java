package com.study.templateapi.api.logout.service;

import com.study.templateapi.domain.member.entity.Member;
import com.study.templateapi.domain.member.service.MemberService;
import com.study.templateapi.global.error.ErrorCode;
import com.study.templateapi.global.error.exception.AuthenticationException;
import com.study.templateapi.global.jwt.constant.TokenType;
import com.study.templateapi.global.jwt.service.TokenManager;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class LogoutService {

    private final MemberService memberService;
    private final TokenManager tokenManager;

    public void logout(String accessToken) {

        // 1. 토큰 검증
        tokenManager.validateToken(accessToken);

        // 2. 토큰 타입 확인
        Claims tokenClaims = tokenManager.getTokenClaims(accessToken);
        String tokenType = tokenClaims.getSubject();
        if(!TokenType.isAccessToken(tokenType)) {
            throw new AuthenticationException(ErrorCode.NOT_ACCESS_TOKEN_TYPE);
        }

        // 3. refresh token 만료 처리
        Long memberId = Long.valueOf((Integer) tokenClaims.get("memberId"));
        Member member = memberService.findMemberByMemberId(memberId);
        member.expireRefreshToken(LocalDateTime.now());
    }
}
