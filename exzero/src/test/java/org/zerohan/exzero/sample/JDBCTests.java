package org.zerohan.exzero.sample;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCTests {

    @Test
    public void testConnection() throws ClassNotFoundException, SQLException {
        // DB Driver class
        Class.forName("oracle.jdbc.driver.OracleDriver");

        // URL
        // username / password
        Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE"
                , "inflearn"
                , "cometrue");

        System.out.println("connection = " + connection);
        connection.close();
    }
}
