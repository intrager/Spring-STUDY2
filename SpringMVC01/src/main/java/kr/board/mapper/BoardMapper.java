package kr.board.mapper;

import java.util.List;

import kr.board.entity.Board;

@Mapper // - MyBatis API
public interface BoardMapper {
	public List<Board> getList(); // 전체 리스트
}
