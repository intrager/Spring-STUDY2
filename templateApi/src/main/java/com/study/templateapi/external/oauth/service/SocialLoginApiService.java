package com.study.templateapi.external.oauth.service;

import com.study.templateapi.external.oauth.model.OAuthAttributes;

public interface SocialLoginApiService {
    OAuthAttributes getUserInfo(String accessToken);
}
