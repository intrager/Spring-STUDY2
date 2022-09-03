package com.study.templateapi.api.member.service;

import com.study.templateapi.api.member.dto.MemberInfoResponseDto;
import com.study.templateapi.domain.member.entity.Member;
import com.study.templateapi.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberInfoService {

    private final MemberService memberService;

    @Transactional(readOnly = true)
    public MemberInfoResponseDto getMemberInfo(Long memberId) {
        Member member = memberService.findMemberByMemberId(memberId);
        return MemberInfoResponseDto.of(member);
    }
}
