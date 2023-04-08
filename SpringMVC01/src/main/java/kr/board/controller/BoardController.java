package kr.board.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.board.entity.MyBoard;
import kr.board.mapper.BoardMapper;

@Controller
public class BoardController {
	
	@Autowired
	private BoardMapper mapper;
	
	// /boardList.do
	// HandlerMapping
	@RequestMapping("/boardList.do")
	public String boardList(Model model) {
		
		List<MyBoard> list = mapper.getList();
		model.addAttribute("list", list);
		return "boardList"; // /WEB-INF/views/boardList.jsp -> forward
	}
	
	@GetMapping("/boardForm.do")
	public String boardForm() {
		return "boardForm";	// /WEB-INF/views/boardForm.jsp -> forward
	}
	
	@PostMapping("/insertBoard.do")
	public String insertBoard(MyBoard vo) { // title, content, writer 파라메터수집(board)
		mapper.insertBoard(vo);	// 등록
		return "redirect:/boardList.do"; // redirect
	}
	
	@GetMapping("/boardContent.do")
	public String boardContent(@RequestParam("idx") int idx, Model model) {	// ?idx=7
		MyBoard vo = mapper.boardContent(idx);
		mapper.countingViews(idx);
		model.addAttribute("vo", vo);
		return "boardContent";
	}
	
	@GetMapping("/deleteBoard.do/{idx}")
	public String deleteBoard(@PathVariable("idx") int idx) {
		mapper.deleteBoard(idx);
		return "redirect:/boardList.do";
	}
	
	@GetMapping("/updateBoardForm.do/{idx}")
	public String updateBoardForm(@PathVariable("idx") int idx, Model model) {
		MyBoard vo = mapper.boardContent(idx);
		model.addAttribute("vo", vo);
		return "updateBoard"; // updateBoard.jsp
	}
	
	@PostMapping("/updateBoard.do")
	public String updateBoard(MyBoard vo) {	// idx, title, content
		mapper.updateBoard(vo); // 수정
		return "redirect:/boardList.do";
	}
}
