package org.zerohan.exzero;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * root-context.xml의 내용을 테스트
 *
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml") // 내가 지금 spring을 테스트하는데, 어떤 설정을 이용할건지 지정하는 부분
public class LoadTests {

    @Autowired
    private ApplicationContext ctx;

    @Test
    public void testLoad() {
        System.out.println("test... ");
        System.out.println(ctx);
    }
}
