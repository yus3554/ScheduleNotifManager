package schedule.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import javax.naming.InitialContext;
import javax.sql.DataSource;


public class TargetTable {

	public TargetTable() {

	}

	// まだインプットしていないターゲットがいたらそれをまとめてリストに入れて返す
	public ArrayList<HashMap<String, String>> getTargetListNotInput(){
		ArrayList<HashMap<String, String>> list = new ArrayList<>();
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sm_db", "schedule_manager", "");

			stmt = conn.createStatement();

			// isInputが0の時で検索
			String sql = "select * from targets where isInput = 0;";

			ResultSet rs = stmt.executeQuery(sql);
			HashMap<String, String> hm;
			while (rs.next()) {
				hm = new HashMap<>();
				hm.put("id", rs.getString("id"));
				hm.put("senderEmail", rs.getString("senderEmail"));
				hm.put("targetEmail", rs.getString("targetEmail"));
				hm.put("randomURL", rs.getString("randomURL"));
				hm.put("sendTimes", rs.getString("sendTimes"));
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

	// まだインプットしていないターゲットがいたらそれをまとめてリストに入れて返す
		public void changeSendTimes(String randomURL, int times){
			Connection conn = null;
			Statement stmt = null;
			try {
				Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
				conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sm_db", "schedule_manager", "");

				stmt = conn.createStatement();

				// isInputが0の時で検索
				String sql = "update targets set sendTimes = \"" + times + "\" where randomURL = \"" + randomURL + "\";";

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

	// randomURLからアドレスを取り出す
	public HashMap<String, String> getTarget(String randomURL) {
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sm_db", "schedule_manager", "");

			stmt = conn.createStatement();

			// senderEmailで検索
			String sql = "select * from targets where randomURL = \"" +
							randomURL + "\";";

			ResultSet rs = stmt.executeQuery(sql);
			HashMap<String, String> hm = new HashMap<>();
			while (rs.next()) {
				hm.put("id", rs.getString("id"));
				hm.put("senderEmail", rs.getString("senderEmail"));
				hm.put("targetEmail", rs.getString("targetEmail"));
				hm.put("isInput", rs.getString("isInput"));
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

	// targetEmailからrandomURLを取り出す
	public String getRandomURL(String id, String senderEmail, String targetEmail) {

		String randomURL = "";
		DataSource dataSource = null;
		Connection conn = null;
		Statement stmt = null;
		try {
			InitialContext context = new InitialContext();
			// lookupのjdbc/以下がテーブル名 context.xmlやweb.xmlと合わせる
			dataSource = (DataSource) context.lookup("java:comp/env/jdbc/targets");
			conn = dataSource.getConnection();

			stmt = conn.createStatement();

			// senderEmailで検索
			String sql = "select * from targets where targetEmail = \"" +
							targetEmail + "\" and id = \"" +
							id + "\" and senderEmail = \"" +
							senderEmail + "\";";

			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				randomURL = rs.getString("randomURL");
			}
			return randomURL;

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
		return randomURL;
	}

	public ArrayList<HashMap<String, String>> getTargetList(String id, String senderEmail){
		ArrayList<HashMap<String, String>> list = new ArrayList<>();
		DataSource dataSource = null;
		Connection conn = null;
		Statement stmt = null;
		try {
			InitialContext context = new InitialContext();
			// lookupのjdbc/以下がテーブル名 context.xmlやweb.xmlと合わせる
			dataSource = (DataSource) context.lookup("java:comp/env/jdbc/targets");
			conn = dataSource.getConnection();

			stmt = conn.createStatement();

			// senderEmailで検索
			String sql = "select * from targets where id = \"" +
							id + "\" and senderEmail = \"" +
							senderEmail + "\";";

			ResultSet rs = stmt.executeQuery(sql);
			HashMap<String, String> hm;
			while (rs.next()) {
				hm = new HashMap<>();
				hm.put("targetEmail", rs.getString("targetEmail"));
				hm.put("randomURL", rs.getString("randomURL"));
				hm.put("isInput", rs.getString("isInput"));
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

	// idとsenderEmailを入れて削除する
	public void delete(String id, String senderEmail) {
		DataSource dataSource = null;
		Connection conn = null;
		Statement stmt = null;
		try {
			InitialContext context = new InitialContext();
			// lookupのjdbc/以下がテーブル名 context.xmlやweb.xmlと合わせる
			dataSource = (DataSource) context.lookup("java:comp/env/jdbc/targets");
			conn = dataSource.getConnection();

			stmt = conn.createStatement();

			// senderEmailで検索
			String sql = "delete from targets where id = \"" +
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

	public void isInputUpdate(String randomURL) {
		DataSource dataSource = null;
		Connection conn = null;
		Statement stmt = null;
		try {
			InitialContext context = new InitialContext();
			// lookupのjdbc/以下がテーブル名 context.xmlやweb.xmlと合わせる
			dataSource = (DataSource) context.lookup("java:comp/env/jdbc/targets");
			conn = dataSource.getConnection();

			stmt = conn.createStatement();

			String sql = "update targets set isInput = \"" + "1" +
						"\" where randomURL = \"" + randomURL + "\";";
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
