package com.study.templateapi.api.member.dto;

import com.study.templateapi.domain.member.constant.Role;
import com.study.templateapi.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberInfoResponseDto {

    private Long memberId;
    private String email;
    private String memberName;
    private String profile;
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
