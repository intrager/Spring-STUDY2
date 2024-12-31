package org.zerohan.exzero.mappers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
public class TimeTests {
    private static final Logger log = LogManager.getLogger(TimeTests.class);

    @Autowired(required = false) // TimeMapper에 Bean이 없기 때문에 required = false, 실행할 때 체크해줘. 이 객체가 존재하는지 따지지마. 없을 수도 있지 뭐.
    TimeMapper timeMapper;

    @Test
    public void test() {
        log.info(timeMapper.getClass().getName());
        log.info("----------------------------");
        log.info(timeMapper.getTime());
        log.info("----------------------------");
    }

    @Test
    public void testMapper() {
        log.info("----------------------------");
        log.info(timeMapper.getTimeUsingMapper());
        log.info("----------------------------");
    }
}
