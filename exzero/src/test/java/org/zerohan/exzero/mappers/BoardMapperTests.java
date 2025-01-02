package org.zerohan.exzero.mappers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.zerohan.exzero.domain.BoardVO;
import org.zerohan.exzero.domain.Criteria;

import java.util.List;

/*
BoardMapper가 설정한 게 정상적으로 동작하는 걸 확인하기 위해 만든 테스트코드
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
public class BoardMapperTests {
    private static final Logger log = LogManager.getLogger(BoardMapperTests.class);

    @Autowired(required = false)
    BoardMapper boardMapper;

    @Test
    void testBoardMapperConnection() {
        log.info(boardMapper);
    }

    @Test
    void testGetList() {
        boardMapper.getBoardsList().forEach(log::info);
    }

    @Test
    void testInsert() {
        BoardVO vo = new BoardVO();
        vo.setTitle("testTitle");
        vo.setContent("testContent");
        vo.setWriter("testWriter");

        log.info("inserted COUNT : {}", boardMapper.insert(vo));
        log.info("BNO : {}", vo.getBno());
    }

    @Test
    void testSelect() {
        Long bno = 8L;
        log.info(boardMapper.select(bno));
    }

    @Test
    void testUpdate() {
        BoardVO vo = new BoardVO();
        vo.setBno(8L);
        vo.setTitle("testTitle2");
        vo.setContent("testContent2");
        vo.setWriter("testWriter2");

        log.info("updated COUNT : {}", boardMapper.update(vo));
    }

    @Test
    public void testPage() {
        Criteria criteria = new Criteria();
        criteria.setPageNum(2);
        criteria.setTypes(new String[]{"T"});
        criteria.setKeyword("1");
        List<BoardVO> list = boardMapper.getBoardsAndPage(criteria);
        list.forEach(log::info);
    }
}
