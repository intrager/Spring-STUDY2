package kr.board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import kr.board.entity.MyBoard;

@Mapper // - MyBatis API
public interface BoardMapper {
	public List<MyBoard> getList(); // 전체 리스트
	public void insertBoard(MyBoard vo);
	public MyBoard boardContent(int idx);
	public void deleteBoard(int idx);
	public void updateBoard(MyBoard vo);
	
	@Update("UPDATE MYBOARD SET COUNT = COUNT + 1 WHERE IDX=#{idx}")
	public void countingViews(int idx);
}
