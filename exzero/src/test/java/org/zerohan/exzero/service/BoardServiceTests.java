package org.zerohan.exzero.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.zerohan.exzero.domain.BoardVO;
import org.zerohan.exzero.mappers.BoardMapperTests;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
public class BoardServiceTests {
    private static final Logger log = LogManager.getLogger(BoardMapperTests.class);

    @Autowired(required = false)
    BoardService boardService;

    @Test
    void testList() {
        log.info(boardService.getList());
    }

    @Test
    void testOneBoard() {

    }
}
