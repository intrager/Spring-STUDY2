package org.zerohan.exzero.mappers;

import org.springframework.stereotype.Repository;
import org.zerohan.exzero.domain.BoardVO;

import java.util.List;

public interface BoardMapper {
    List<BoardVO> getBoardsList();

    // insert는 dml이므로 몇 개의 행이 바뀌었다/안 바뀌었다의 개수를 반환하므로 int 반환타입임
    int insert(BoardVO boardVO);

    BoardVO select(Long bno);

    int update(BoardVO boardVO);
}
