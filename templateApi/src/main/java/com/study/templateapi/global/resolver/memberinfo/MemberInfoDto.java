package com.study.templateapi.global.resolver.memberinfo;

import com.study.templateapi.domain.member.constant.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberInfoDto {
    private Long memberId;
    private Role role;
}
