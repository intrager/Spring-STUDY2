package com.study.templateapi.api.member.dto;

import com.study.templateapi.domain.member.constant.Role;
import com.study.templateapi.domain.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberInfoResponseDto {

    @Schema(description = "회원 아이디", example = "1", required = true)
    private Long memberId;

    @Schema(description = "이메일", example = "test@induk.ac.kr", required = true)
    private String email;

    @Schema(description = "회원 이름", example = "한정수", required = true)
    private String memberName;

    @Schema(description = "프로필 이미지 경로", example = "https://spring.io/images/spring-logo-9146a4d3298760c2e7e49595184e1975.svg", required = false)
    private String profile;

    @Schema(description = "회원 권한", example = "USER", required = true)
    private Role role;

    public static MemberInfoResponseDto of(Member member) {
        return MemberInfoResponseDto.builder()
                .memberId(member.getMemberId())
                .memberName(member.getMemberName())
                .email(member.getEmail())
                .profile(member.getProfile())
                .role(member.getRole())
                .build();
    }
}
