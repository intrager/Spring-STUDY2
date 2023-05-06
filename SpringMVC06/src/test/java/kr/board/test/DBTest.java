package kr.board.test;

import java.sql.Connection;
import java.sql.DriverManager;

import org.junit.jupiter.api.Test;


public class DBTest {
	
	@Test
	public void testConnection() {
		try(Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "cometrue")) {
			System.out.println(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
