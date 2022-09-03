package com.study.templateapi.api.token.controller;

import com.study.templateapi.api.token.dto.AccessTokenResponseDto;
import com.study.templateapi.api.token.service.TokenService;
import com.study.templateapi.global.util.AuthorizationHeaderUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TokenController {

    private final TokenService tokenService;

    @PostMapping("/access-token/issue")
    public ResponseEntity<AccessTokenResponseDto> createAccessToken(HttpServletRequest httpServletRequest) {
        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        AuthorizationHeaderUtils.validateAuthorization(authorizationHeader);

        String refreshToken = authorizationHeader.split(" ")[1];

        AccessTokenResponseDto accessTokenResponseDto = tokenService.createAccessTokenByRefreshToken(refreshToken);
        return ResponseEntity.ok(accessTokenResponseDto);
    } // authorizationHeader가 필수값으로 들어있는지 체크해야 함
}
