package org.zerohan.exzero.sample;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.sql.Connection;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
public class DITests {

    /*
     아래 타입의 객체가 필요한데
     너 좀 자동으로 찔러줄래? 나 테스트코드에다가 집어넣어줘
     */
    @Autowired
    Restaurant restaurant;

    @Autowired
    DataSource dataSource;

    @Test
    public void testConnection() throws Exception {
        Connection conn = dataSource.getConnection();
        System.out.println("conn = " + conn);
        conn.close();
    }

    @Test
    public void testExist() {
        System.out.println(restaurant); // Restaurant(chef=Chef()) // org.zerohan.exzero.sample.Restaurant@1e0b4072
    }
}
