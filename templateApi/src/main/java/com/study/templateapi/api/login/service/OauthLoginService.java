package com.study.templateapi.api.login.service;

import com.study.templateapi.api.login.dto.OauthLoginDto;
import com.study.templateapi.domain.member.constant.MemberType;
import com.study.templateapi.external.oauth.model.OAuthAttributes;
import com.study.templateapi.external.oauth.service.SocialLoginApiService;
import com.study.templateapi.external.oauth.service.SocialLoginApiServiceFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class OauthLoginService {
    public OauthLoginDto.Response oauthLogin(String accessToken, MemberType memberType) {
        SocialLoginApiService socialLoginApiService = SocialLoginApiServiceFactory.getSocialLoginApiService(memberType);
        OAuthAttributes userInfo = socialLoginApiService.getUserInfo(accessToken);
        log.info("userInfo : {}", userInfo);

        return OauthLoginDto.Response.builder().build();
    }
}
