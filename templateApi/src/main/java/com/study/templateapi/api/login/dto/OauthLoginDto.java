package com.study.templateapi.api.login.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.study.templateapi.global.jwt.dto.JwtTokenDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Date;

public class OauthLoginDto {
    @Getter @Setter
    public static class Request {
        @Schema(description = "소셜 로그인 회원 타입", example = "KAKAO", required = true)
        private String memberType;
    }

    @Getter @Setter
    @Builder @NoArgsConstructor @AllArgsConstructor
    public static class Response {

        @Schema(description = "grantType", example = "Bearer", required = true)
        private String grantType;

        @Schema(description = "accessToken", example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxbnH-dtao4cMT4-FXtb_7GF-NlNGZymKYsr10jZHIjl6OZKtpSX_C6Nmndx0e4w", required = true)
        private String accessToken;

        @Schema(description = "access token 만료 시간", example = "2022-09-03 20:40:00", required = true)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Date accessTokenExpireTime;

        @Schema(description = "refreshToken", example = "eyJ0eXAiOiJKV1QiLCJhbGciONjYyMjA0kIjoxfQ.lTWAPx0bcoThqracqlYuoeZxbNYcJc_ab1LlILFV2jqvhIFVVO0Co_", required = true)
        private String refreshToken;

        @Schema(description = "refresh token 만료 시간", example = "2022-09-08 20:25:00", required = true)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Date refreshTokenExpireTime;

        public static Response of(JwtTokenDto jwtTokenDto) {
            return Response.builder()
                    .grantType(jwtTokenDto.getGrantType())
                    .accessToken(jwtTokenDto.getAccessToken())
                    .accessTokenExpireTime(jwtTokenDto.getAccessTokenExpireTime())
                    .refreshToken(jwtTokenDto.getRefreshToken())
                    .refreshTokenExpireTime(jwtTokenDto.getRefreshTokenExpireTime())
                    .build();
        }
    }

}
