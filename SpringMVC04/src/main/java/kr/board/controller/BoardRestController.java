package kr.board.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.board.entity.MyBoard;
import kr.board.mapper.BoardMapper;

@RequestMapping("/board")
@RestController
public class BoardRestController {

	@Autowired
	BoardMapper boardMapper;

//	@RequestMapping("/boardList.do")
	@GetMapping("/all")
	public List<MyBoard> boardList() {
		return boardMapper.getList();
	}

//	@RequestMapping("/insertBoard.do")
	@PostMapping("/new")
	public void insertBoard(MyBoard vo) {
		boardMapper.insertBoard(vo);
	}

	@PatchMapping("/update")
	public void updateBoard(@RequestBody MyBoard vo) {
		boardMapper.updateBoard(vo);
	}

	@DeleteMapping("/{idx}")
	public void deleteBoard(@PathVariable("idx") int idx) {
		boardMapper.deleteBoard(idx);
	}

	@GetMapping("/{idx}")
	public MyBoard boardContent(@PathVariable("idx") int idx) {
		return boardMapper.boardContent(idx); // vo -> JSON
	}

	@PutMapping("/count/{idx}")
	public MyBoard countView(@PathVariable("idx") int idx) {
		boardMapper.countingViews(idx);
		return boardMapper.boardContent(idx);
	}
}
