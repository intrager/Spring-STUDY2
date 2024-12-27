package org.zerohan.exzero.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerohan.exzero.domain.BoardVO;
import org.zerohan.exzero.domain.Criteria;
import org.zerohan.exzero.mappers.BoardMapper;

import java.util.List;

@Service
public class BoardService {
    private static final Logger log = LogManager.getLogger(BoardService.class);

    private final BoardMapper boardMapper;

    public BoardService(BoardMapper boardMapper) {
        this.boardMapper = boardMapper;
    }

    @Transactional
    public Long register(BoardVO boardVO) {
        log.info("board register : {}", boardVO);
        int count = boardMapper.insert(boardVO);
        log.info("registered bno : {}", boardVO.getBno());
        return boardVO.getBno();
    }

    public List<BoardVO> getList() {
        return boardMapper.getBoardsList();
    }

    public List<BoardVO> getListAndPage(Criteria criteria) {
        return boardMapper.getBoardsAndPage(criteria);
    }

    public BoardVO getOneBoard(Long bno) {
        log.info("get one board : {}", bno);
        return boardMapper.select(bno);
    }

    @Transactional
    public boolean modify(BoardVO boardVO) {
        log.info("modify one : {}", boardVO);
        return boardMapper.update(boardVO) == 1;
    }

    @Transactional
    public boolean remove(Long bno) {
        log.info("soft delete one : {}", bno);
        return true;
    }
}
