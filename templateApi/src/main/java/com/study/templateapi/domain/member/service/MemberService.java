package com.study.templateapi.domain.member.service;

import com.study.templateapi.domain.member.entity.Member;
import com.study.templateapi.domain.member.repository.MemberRepository;
import com.study.templateapi.global.error.ErrorCode;
import com.study.templateapi.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Member registerMember(Member member) {
        validateDuplicateMember(member);    // 한 번 더 체크하는 방어 로직
        return memberRepository.save(member);
    }

    private void validateDuplicateMember(Member member) {
        Optional<Member> optionalMember = memberRepository.findByEmail(member.getEmail());
        if(optionalMember.isPresent()) {
            throw new BusinessException(ErrorCode.ALREADY_REGISTERED_MEMBER);
        }
    }

    public Optional<Member> findMemberByEmail(String email) {
        return memberRepository.findByEmail(email);
    }
}
