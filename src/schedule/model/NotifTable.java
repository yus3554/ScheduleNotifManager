package schedule.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;


public class NotifTable {

	public NotifTable() {

	}

	// 現在の時間を入れてnotifリストを返す
	public ArrayList<HashMap<String, String>> getNotifList(String nowTime) {
		ArrayList<HashMap<String, String>> notifs = new ArrayList<>();
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sm_db", "schedule_manager", "");

			stmt = conn.createStatement();

			// senderEmailで検索
			String sql = "select * from notifs where notifTime < \"" +
					nowTime + "\";";

			// HashMapに入れてそれをArrayListに格納
			// HashMapはwhileでループごとに毎回初期化する必要がある
			ResultSet rs = stmt.executeQuery(sql);
			HashMap<String, String> hm;
			while (rs.next()) {
				hm = new HashMap<>();
				hm.put("randomURL", rs.getString("randomURL"));
				hm.put("type", rs.getString("type"));
				notifs.add(hm);
			}

			return notifs;

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

	public void delete(ArrayList<String> randomURL, String notifTime) {
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sm_db", "schedule_manager", "");

			stmt = conn.createStatement();

			for(String url: randomURL) {
				String sql = "delete from notifs where randomURL = \"" + url + "\" and notifTime < \"" + notifTime + "\";";
				stmt.executeUpdate(sql);
			}

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

	public void delete(String randomURL) {
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sm_db", "schedule_manager", "");

			stmt = conn.createStatement();

			String sql = "delete from notifs where randomURL = \"" + randomURL + "\";";
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

}
