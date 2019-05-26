package schedule.model;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.naming.InitialContext;
import javax.sql.DataSource;

public class RequestAttachmentTable {

	public RequestAttachmentTable() {
	}

	// attachmentをrequestAttachmentsテーブルにインサート
	public void insert(String id, String senderEmail, String fileName, InputStream stream) {
		Connection conn = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sm_db", "schedule_manager", "");

			String sql = "insert into requestAttachments values (?, ?, ?, ?);";
			PreparedStatement patmt = conn.prepareStatement(sql);

			patmt.setString(1, id);
			patmt.setString(2, senderEmail);
			patmt.setString(3, fileName);
			patmt.setBlob(4, stream);

			patmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// idとsenderEmailを入れて対応するfilesを返す
	public ArrayList<RequestAttachment> getFiles(String id, String senderEmail) {
		ArrayList<RequestAttachment> files = new ArrayList<>();
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sm_db", "schedule_manager", "");

			stmt = conn.createStatement();

			// senderEmailで検索
			String sql = "select * from requestAttachments where id = \"" +
					id + "\" and senderEmail = \"" + senderEmail + "\";";

			ResultSet rs = stmt.executeQuery(sql);
			RequestAttachment am;
			while (rs.next()) {
				am = new RequestAttachment(rs.getString("fileName"), (rs.getBlob("file")).getBinaryStream());
				files.add(am);
			}

			return files;

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
			dataSource = (DataSource) context.lookup("java:comp/env/jdbc/requestAttachments");
			conn = dataSource.getConnection();

			stmt = conn.createStatement();

			// senderEmailで検索
			String sql = "delete from requestAttachments where id = \"" + id + "\" and "
					+ "senderEmail = \"" + senderEmail +"\";";

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
