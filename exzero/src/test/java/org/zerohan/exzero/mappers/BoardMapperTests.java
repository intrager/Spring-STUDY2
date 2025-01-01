package org.zerohan.exzero.mappers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
    public void testBoardMapperConnection() {
        log.info(boardMapper);
    }

    @Test
    public void testGetList() {
        boardMapper.getBoardsList().forEach(log::info);
    }
}
