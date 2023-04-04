package kr.board.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
