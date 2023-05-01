package kr.board.mapper; 

import org.apache.ibatis.annotations.Mapper;

import kr.board.entity.Member;

@Mapper // - MyBatis API
public interface MemberMapper {
	public int registerMember(Member member);
	public Member validateLoginInfo(Member mvo);
	public int updateMem(Member member);
	public Member selectMemberInfo(String memID);
	public String selectMemImage(String memID);
	public void updateMemImage(Member mvo);
}
