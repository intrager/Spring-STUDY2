package kr.board.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import kr.board.entity.AuthVO;
import kr.board.entity.Member;
import kr.board.entity.MemberUser;
import kr.board.mapper.MemberMapper;
import kr.board.security.MemberUserDetailsService;

@Controller
public class MemberController {
	
	@Autowired
	MemberMapper memberMapper;
	
	@Autowired
	MemberUserDetailsService memberUserDetailsService;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@RequestMapping("/joinMember.do")
	public String joinMember() {
		return "member/join";	// join.jsp
	} 
	
	@RequestMapping("/checkMemberRegister.do")
	public @ResponseBody int checkMemberRegister(@RequestParam("memID") String memID) {
		Member checkForMember = memberMapper.selectMemberInfo(memID);
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
		member.setMemProfile("");
		String encryptedPassword = passwordEncoder.encode(member.getMemPassword());
		member.setMemPassword(encryptedPassword);

		if(memberMapper.registerMember(member) > 0) {
			List<AuthVO> list = member.getAuthList();
			insertNewAuth(member, list);
			flashAttributeMessage(rttr, "성공 메시지", "회원가입에 성공했습니다.");

			return "redirect:/loginForm.do";
		} else {
			flashAttributeMessage(rttr, "실패 메시지", "회원 가입에 실패했습니다.");
			return "redirect:/joinMember.do";
		}
	}
	
	@RequestMapping("/loginForm.do")
	public String loginForm() {
		return "member/memLoginForm";
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
		String encryptedPassword = passwordEncoder.encode(member.getMemPassword());
		member.setMemPassword(encryptedPassword);
		if(memberMapper.updateMem(member) > 0) {
			memberMapper.deleteAuth(member.getMemID());
			List<AuthVO> list = member.getAuthList();
			
			insertNewAuth(member, list);
			flashAttributeMessage(rttr, "성공 메시지", "회원정보 수정에 성공했습니다.");
			// 회원 수정이 성공하면 -> 로그인처리하기
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			MemberUser userAccount = (MemberUser) authentication.getPrincipal();
			SecurityContextHolder.getContext().setAuthentication(createNewAuthentication(authentication, userAccount.getMember().getMemID()));;
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
	
	@SuppressWarnings("deprecation")
	@RequestMapping("/updateMemImage.do")
	public String updateMemImage(HttpServletRequest request, HttpSession session, RedirectAttributes rttr) throws IOException {
		MultipartRequest multipart = null;
		int fileMaxSize = 10 * 1024 * 1024; // 10MB
		String savePath = request.getRealPath("resources/upload");
		try {
			multipart = new MultipartRequest(request, savePath, fileMaxSize, "UTF-8", new DefaultFileRenamePolicy());
		} catch (IOException e) {
			e.printStackTrace();
			flashAttributeMessage(rttr, "실패 메시지", "파일의 크기는 10MB를 넘을 수 없습니다.");
			return "redirect:/updateImageForm.do";
		}
		String memID = multipart.getParameter("memID");
		String newProfile = "";
		File file = multipart.getFile("memProfile");
		if(file != null) {
			// 이미지 파일 여부 체크 -> 이미지 파일이 아니면 삭제
			String ext = file.getName()
							 .substring(file.getName().lastIndexOf(".") + 1)
							 .toUpperCase();
			if(ext.matches("PNG|GIF|JPG")) {
				String oldProfile = memberMapper.selectMemImage(memID);
				File oldFile = new File(savePath + "/" + oldProfile);
				if(oldFile.exists()) deleteFile(oldFile);
				newProfile = file.getName();
			} else { // 이미지 파일이 아니면
				if(file.exists()) deleteFile(file);
				flashAttributeMessage(rttr, "실패 메시지", "이미지 파일만 업로드할 수 있습니다.");
				return "redirect:/updateImageForm.do";
			}
		}
		// 새로운 이미지를 테이블에 업데이트
		Member mvo = new Member();
		mvo.setMemID(memID);
		mvo.setMemProfile(newProfile);
		memberMapper.updateMemImage(mvo);
		mvo = memberMapper.selectMemberInfo(memID);
		
		// 스프링 보안(새로운 인증 세션을 생성 -> 객체바인딩)
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		MemberUser userAccount = (MemberUser) authentication.getPrincipal();
		SecurityContextHolder.getContext().setAuthentication(createNewAuthentication(authentication, userAccount.getMember().getMemID()));
		
		flashAttributeMessage(rttr, "성공 메시지", "프로필 이미지가 정상적으로 변경되었습니다.");
		return "redirect:/";
	}

	@GetMapping("/access-denied")
	public String showAccessDenied() {
		return "access-denied";
	}
	
	// 스프링 보안(새로운 세션 생성 메서드)
	// UsernamePasswordAuthenticationToken -> 회원정보 + 권한정보
	protected Authentication createNewAuthentication(Authentication currentAuth, String username) {
		UserDetails newPrincipal = memberUserDetailsService.loadUserByUsername(username);
		UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(newPrincipal, currentAuth.getCredentials(), newPrincipal.getAuthorities());
		newAuth.setDetails(currentAuth.getDetails());
		return newAuth;
	}
	
	private boolean deleteFile(File filename) {
		return filename.delete();
	}
	
	private void flashAttributeMessage(RedirectAttributes rttr, String msgType, String msg) {
		rttr.addFlashAttribute("msgType", msgType);
		rttr.addFlashAttribute("msg", msg);
	}
	
	private boolean checkMemberValidation(Member member, String memPassword1, String memPassword2) {
		return StringUtils.isEmpty(memPassword1) ||
			   StringUtils.isEmpty(memPassword2) ||
		   StringUtils.isEmpty(member.getMemID()) || 
		   StringUtils.isEmpty(member.getMemName()) ||
		   StringUtils.isEmpty(member.getMemAge()) || member.getMemAge() == 0 ||
		   StringUtils.isEmpty(member.getMemGender()) ||
		   StringUtils.isEmpty(member.getMemEmail()) || 
		   member.getAuthList().size() == 0;
	}

	private void insertNewAuth(Member member, List<AuthVO> list) {
		for(AuthVO vo : list) {
			if(vo.getAuth() != null) {
				AuthVO saveVO = new AuthVO();
				saveVO.setMemID(member.getMemID());
				saveVO.setAuth(vo.getAuth());
				memberMapper.insertAuth(saveVO);
			}
		}
	}
}
