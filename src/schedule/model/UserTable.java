package schedule.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class UserTable {

	public UserTable() {

	}

	public String getName(String email){
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sm_db", "schedule_manager", "");

			stmt = conn.createStatement();

			// senderEmailで検索
			String sql = "select * from users where email = \"" +
					email + "\";";

			// HashMapに入れてそれをArrayListに格納
			// HashMapはwhileでループごとに毎回初期化する必要がある
			ResultSet rs = stmt.executeQuery(sql);
			String name = "";
			while (rs.next()) {
				name = rs.getString("name");
			}

			return name;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(stmt != null) {
					stmt.close();
				}
				if(conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
