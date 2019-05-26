package schedule.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import javax.naming.InitialContext;
import javax.sql.DataSource;

public class ScheduleTable {

	public ScheduleTable() {
	}

	public ArrayList<HashMap<String, String>> getAllSchedule() {
		ArrayList<HashMap<String, String>> list = new ArrayList<>();
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sm_db", "schedule_manager", "");

			stmt = conn.createStatement();

			// senderEmailで検索
			String sql = "select * from schedules;";

			// HashMapに入れてそれをArrayListに格納
			// HashMapはwhileでループごとに毎回初期化する必要がある
			ResultSet rs = stmt.executeQuery(sql);
			HashMap<String, String> hm;
			while (rs.next()) {
				hm = new HashMap<>();
				hm.put("id", rs.getString("id"));
				hm.put("eventName", rs.getString("eventName"));
				hm.put("eventContent", rs.getString("eventContent"));
				hm.put("eventStartDate", rs.getString("eventStartDate"));
				hm.put("eventEndDate", rs.getString("eventEndDate"));
				hm.put("eventDeadline", rs.getString("eventDeadline"));
				list.add(hm);
			}

			return list;

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

	// senderEmailを入れて全てのスケジュールを返す
	public ArrayList<HashMap<String, String>> getScheduleList(String senderEmail) {
		ArrayList<HashMap<String, String>> list = new ArrayList<>();
		DataSource dataSource = null;
		Connection conn = null;
		Statement stmt = null;
		try {
			InitialContext context = new InitialContext();
			// lookupのjdbc/以下がテーブル名 context.xmlやweb.xmlと合わせる
			dataSource = (DataSource) context.lookup("java:comp/env/jdbc/schedules");
			conn = dataSource.getConnection();

			stmt = conn.createStatement();

			// senderEmailで検索
			String sql = "select * from schedules where senderEmail = \"" + senderEmail +"\";";

			// HashMapに入れてそれをArrayListに格納
			// HashMapはwhileでループごとに毎回初期化する必要がある
			ResultSet rs = stmt.executeQuery(sql);
			HashMap<String, String> hm;
			while (rs.next()) {
				hm = new HashMap<>();
				hm.put("id", rs.getString("id"));
				hm.put("eventName", rs.getString("eventName"));
				hm.put("eventContent", rs.getString("eventContent"));
				hm.put("eventStartDate", rs.getString("eventStartDate"));
				hm.put("eventEndDate", rs.getString("eventEndDate"));
				hm.put("eventDeadline", rs.getString("eventDeadline"));
				list.add(hm);
			}

			return list;

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

	// idとsenderEmailを入れて一つのスケジュールを返す
	public HashMap<String, String> getSchedule(String id, String senderEmail) {
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sm_db", "schedule_manager", "");

			stmt = conn.createStatement();

			// senderEmailで検索
			String sql = "select * from schedules where id = \"" +
					id +
					"\" and senderEmail = \"" +
					senderEmail +"\";";

			ResultSet rs = stmt.executeQuery(sql);

			// HashMapに格納
			HashMap<String, String> hm = new HashMap<>();
			while (rs.next()) {
				hm.put("id", rs.getString("id"));
				hm.put("eventName", rs.getString("eventName"));
				hm.put("eventContent", rs.getString("eventContent"));
				hm.put("eventDeadline", rs.getString("eventDeadline"));
				hm.put("decideDate", rs.getString("decideDate"));
				hm.put("note", rs.getString("note"));
				hm.put("isDecideFirst", rs.getString("isDecideFirst"));
			}

			return hm;

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

	// idとsenderEmailを入れて削除する
	public void delete(String id, String senderEmail) {
		DataSource dataSource = null;
		Connection conn = null;
		Statement stmt = null;
		try {
			InitialContext context = new InitialContext();
			// lookupのjdbc/以下がテーブル名 context.xmlやweb.xmlと合わせる
			dataSource = (DataSource) context.lookup("java:comp/env/jdbc/schedules");
			conn = dataSource.getConnection();

			stmt = conn.createStatement();

			// senderEmailで検索
			String sql = "delete from schedules where id = \"" +
					id +
					"\" and senderEmail = \"" +
					senderEmail + "\";";

			stmt.executeUpdate(sql);

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
	}

	// idとsenderEmailで検索をかけて、日時決定のデータを上書きする
	public void updateDecideDate(String id, String senderEmail) {
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sm_db", "schedule_manager", "");

			stmt = conn.createStatement();

			// senderEmailで検索
			String sql = "update schedules set isDecideFirst = 1"
					+ " where id = ? and senderEmail = ?;";
			PreparedStatement patmt = conn.prepareStatement(sql);

			patmt.setString(1, id);
			patmt.setString(2, senderEmail);

			patmt.executeUpdate();

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
	}
}
