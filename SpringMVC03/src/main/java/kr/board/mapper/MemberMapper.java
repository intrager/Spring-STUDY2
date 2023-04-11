package kr.board.mapper;

import org.apache.ibatis.annotations.Mapper;

import kr.board.entity.Member;

@Mapper // - MyBatis API
public interface MemberMapper {
	public Member checkMemberRegister(String memID);
	public int registerMember(Member member);
}
