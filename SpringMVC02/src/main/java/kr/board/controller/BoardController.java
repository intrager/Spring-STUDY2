package kr.board.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.board.entity.MyBoard;
import kr.board.mapper.BoardMapper;

@Controller
public class BoardController {
	
	@Autowired
	BoardMapper mapper;
	
	@RequestMapping("/")
	public String main() {
		return "main";
	}
	
	@RequestMapping("/boardList.do")
	public @ResponseBody List<MyBoard> boardList() {
		return mapper.getList();
	}
}
