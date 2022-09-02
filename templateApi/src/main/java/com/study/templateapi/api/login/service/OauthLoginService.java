package com.study.templateapi.api.login.service;

import com.study.templateapi.api.login.dto.OauthLoginDto;
import com.study.templateapi.domain.member.constant.MemberType;
import com.study.templateapi.domain.member.constant.Role;
import com.study.templateapi.domain.member.entity.Member;
import com.study.templateapi.domain.member.service.MemberService;
import com.study.templateapi.external.oauth.model.OAuthAttributes;
import com.study.templateapi.external.oauth.service.SocialLoginApiService;
import com.study.templateapi.external.oauth.service.SocialLoginApiServiceFactory;
import com.study.templateapi.global.jwt.dto.JwtTokenDto;
import com.study.templateapi.global.jwt.service.TokenManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class OauthLoginService {

    private final MemberService memberService;
    private final TokenManager tokenManager;

    public OauthLoginDto.Response oauthLogin(String accessToken, MemberType memberType) {
        SocialLoginApiService socialLoginApiService = SocialLoginApiServiceFactory.getSocialLoginApiService(memberType);
        OAuthAttributes userInfo = socialLoginApiService.getUserInfo(accessToken);
        log.info("userInfo : {}", userInfo);

        JwtTokenDto jwtTokenDto;
        Optional<Member> optionalMember = memberService.findMemberByEmail(userInfo.getEmail());
        if(optionalMember.isEmpty()) {  // 신규 회원 가입
            Member oauthMember = userInfo.toMemberEntity(memberType, Role.ADMIN);
            oauthMember = memberService.registerMember(oauthMember);

            // 토큰 생성
            jwtTokenDto = tokenManager.createJwtTokenDto(oauthMember.getMemberId(), oauthMember.getRole());
            oauthMember.updateRefreshToken(jwtTokenDto);
        } else {    // 기존 회원일 경우
            Member oauthMember = optionalMember.get();

            // 토큰 생성
            jwtTokenDto = tokenManager.createJwtTokenDto(oauthMember.getMemberId(), oauthMember.getRole());
            oauthMember.updateRefreshToken(jwtTokenDto);
        }

        return OauthLoginDto.Response.of(jwtTokenDto);
    }
}
