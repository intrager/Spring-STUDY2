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
		if(checkForMember != null || memID.equals("")) {
			return 0;
		} 
		return 1;
	}
	
	@RequestMapping("/registMember.do")
	public String registMember(Member member, String memPassword1, String memPassword2,
			RedirectAttributes rttr, HttpSession session) {
		if(checkMemberEffectiveness(member, memPassword1, memPassword2)) {
			// 유효성 오류 메시지를 가지고 가야한다 -> 객체 바인딩은 Model에다가 
			rttr.addFlashAttribute("msgType", "누락 메시지");
			rttr.addFlashAttribute("msg", "모든 내용을 입력하세요.");
			return "redirect:/joinMember.do"; // ${msgType}, ${msg}
		}
		if(!memPassword1.equals(memPassword2)) {
			rttr.addFlashAttribute("msgType", "실패 메시지");
			rttr.addFlashAttribute("msg", "비밀번호가 서로 다릅니다");
			return "redirect:/joinMember.do";
		}
		member.setMemProfile(" "); // TODO 사진 이미지 첨부 기능 추가
		if(memberMapper.registerMember(member) > 0) {
			rttr.addFlashAttribute("msgType", "성공 메시지");
			rttr.addFlashAttribute("msg", "회원가입에 성공했습니다.");
			session.setAttribute("memVO", member);
			return "redirect:/";
		} else {
			rttr.addFlashAttribute("msgType", "실패 메시지");
			rttr.addFlashAttribute("msg", "회원 가입에 실패했습니다.");
			return "redirect:/joinMember.do";
		}
	}

	@RequestMapping("/logout.do")
	public String logoutSession(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
	
	@RequestMapping("/loginForm.do")
	public String loginForm() {
		return "member/memLoginForm";
	}
	
	private boolean checkMemberEffectiveness(Member member, String memPassword1, String memPassword2) {
		return StringUtils.isEmpty(memPassword1) ||
			   StringUtils.isEmpty(memPassword2) ||
		   StringUtils.isEmpty(member.getMemID()) || 
		   StringUtils.isEmpty(member.getMemName()) ||
		   StringUtils.isEmpty(member.getMemAge()) || member.getMemAge() == 0 ||
		   StringUtils.isEmpty(member.getMemGender()) ||
		   StringUtils.isEmpty(member.getMemEmail());
	}
}
