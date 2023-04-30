package kr.board.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import kr.board.entity.Member;
import kr.board.mapper.MemberMapper;

@Controller
public class MemberController {
	
	@Autowired
	MemberMapper memberMapper;
	
	@RequestMapping("/joinMember.do")
	public String joinMember() {
		return "member/join";	// join.jsp
	} 
	
	@RequestMapping("/checkMemberRegister.do")
	public @ResponseBody int checkMemberRegister(@RequestParam("memID") String memID) {
		Member checkForMember = memberMapper.checkMemberRegister(memID);
		if(checkForMember != null || memID.equals("")) return 0;
		return 1;
	}
	
	@RequestMapping("/registMember.do")
	public String registMember(Member member, String memPassword1, String memPassword2,
			RedirectAttributes rttr, HttpSession session) {
		if(checkMemberValidation(member, memPassword1, memPassword2)) {
			// 유효성 오류 메시지를 가지고 가야한다 -> 객체 바인딩은 Model에다가 
			flashAttributeMessage(rttr, "누락 메시지", "모든 내용을 입력하세요.");
			return "redirect:/joinMember.do"; // ${msgType}, ${msg}
		}
		if(!memPassword1.equals(memPassword2)) {
			flashAttributeMessage(rttr, "실패 메시지", "비밀번호가 서로 다릅니다.");
			return "redirect:/joinMember.do";
		}
		member.setMemProfile(" "); // TODO 사진 이미지 첨부 기능 추가
		if(memberMapper.registerMember(member) > 0) {
			flashAttributeMessage(rttr, "성공 메시지", "회원가입에 성공했습니다.");
			session.setAttribute("memVO", member);
			return "redirect:/";
		} else {
			flashAttributeMessage(rttr, "실패 메시지", "회원 가입에 실패했습니다.");
			return "redirect:/joinMember.do";
		}
	}

	@RequestMapping("/logout.do")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
	
	@RequestMapping("/loginForm.do")
	public String loginForm() {
		return "member/memLoginForm";
	}
	
	@RequestMapping("/login.do")
	public String login(Member member, RedirectAttributes rttr, HttpSession session) {
		if(validateLoginInfo(member)) {
			flashAttributeMessage(rttr, "실패 메시지", "모든 내용을 입력해주세요.");
			return "redirect:/loginForm.do";
		}
		Member mvo = memberMapper.validateLoginInfo(member);
		if(mvo != null) {
			flashAttributeMessage(rttr, "성공 메시지", "로그인에 성공했습니다.");
			session.setAttribute("mvo", mvo);
			return "redirect:/";
		} else {
			flashAttributeMessage(rttr, "실패 메시지", "로그인을 다시 시도하십시오.");
			return "redirect:/loginForm.do";
		}
	}
	
	@RequestMapping("/updateMemForm.do")
	public String goUpdateMemForm() {
		return "member/memUpdateForm";
	}

	@RequestMapping("/updateMem.do")
	public String updateMem(Member member, RedirectAttributes rttr,
			String memPassword1, String memPassword2, HttpSession session) {
		if(checkMemberValidation(member, memPassword1, memPassword2)) {
			flashAttributeMessage(rttr, "실패 메시지", "모든 내용을 입력하세요.");
			return "redirect:/updateMemForm.do";
		}
		if(!memPassword1.equals(memPassword2)) {
			flashAttributeMessage(rttr, "실패 메시지", "비밀번호가 서로 다릅니다.");
			return "redirect:/updateMemForm.do";
		}
		
		if(memberMapper.updateMem(member) > 0) {
			flashAttributeMessage(rttr, "성공 메시지", "회원정보 수정에 성공했습니다.");
			session.setAttribute("mvo", member);
			return "redirect:/";
		} else {
			flashAttributeMessage(rttr, "실패 메시지", "회원정보 수정에 실패했습니다.");
			return "redirect:/updateMemForm.do";
		}
	}
	
	@RequestMapping("/updateImageForm.do")
	public String updateImageForm() {
		return "member/memImageForm";
	}
	
	private void flashAttributeMessage(RedirectAttributes rttr, String msgType, String msg) {
		rttr.addFlashAttribute("msgType", msgType);
		rttr.addFlashAttribute("msg", msg);
	}

	private boolean validateLoginInfo(Member member) {
		return StringUtils.isEmpty(member.getMemID()) ||
			StringUtils.isEmpty(member.getMemPassword());
	}
	
	private boolean checkMemberValidation(Member member, String memPassword1, String memPassword2) {
		return StringUtils.isEmpty(memPassword1) ||
			   StringUtils.isEmpty(memPassword2) ||
		   StringUtils.isEmpty(member.getMemID()) || 
		   StringUtils.isEmpty(member.getMemName()) ||
		   StringUtils.isEmpty(member.getMemAge()) || member.getMemAge() == 0 ||
		   StringUtils.isEmpty(member.getMemGender()) ||
		   StringUtils.isEmpty(member.getMemEmail());
	}
}
