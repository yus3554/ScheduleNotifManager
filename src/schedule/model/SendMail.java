package schedule.model;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;

import org.apache.commons.text.StringEscapeUtils;


public class SendMail {

	public SendMail() {
	}

	public void send(Schedule schedule, String randomURL, String to, int type) throws Exception {

		final String from = "";
		final String username = "";

		final String password = "";

		final String charset = "UTF-8";

		final String encoding = "base64";

		String host = "";
		String port = "";
		//String starttls = "false";

		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", port);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.socketFactory.port", port);
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		//props.put("mail.smtp.starttls.enable", starttls);

		props.put("mail.smtp.connectiontimeout", "10000");
		props.put("mail.smtp.timeout", "10000");

		//props.put("mail.debug", "true");

		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		// 回答ページのリンクに使うポート番号などを含むアドレス
		String address = "";

		String osName = System.getProperty("os.name").toLowerCase();
		////////////////////////////////////////////////////////////////
		// 自分のパソコンだと、ipアドレスは変更されるので、こっち
		if(osName.startsWith("mac")) {
			String ipAddr = "";
			System.setProperty("java.net.preferIPv4Stack" , "true");
			try {
				InetAddress addr = InetAddress.getLocalHost();
				ipAddr = addr.getHostAddress();
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			}
			address = "https://" + ipAddr + ":8443";
			/////////////////////////////////////////////////////////////////
			/////////////////////////////////////////////////////////////////
			// 研究室のサーバーでは、ipアドレスは固定なのでこっち
		} else if(osName.startsWith("linux")) {
			address = "http://192.168.132.118:8081";
		}
		/////////////////////////////////////////////////////////////////

		// 差出人メールアドレス
		String senderEmail = schedule.getSenderEmail();
		// 差出人表記
		String mailSendName = "";
		// 件名
		String subject = "";
		// 本文
		String content = "";
		// 要求者の名前取得
		String senderName = new UserTable().getName(schedule.getSenderEmail());
		// 日時決定時の備考
		String note = "";
		// 添付ファイル
		ArrayList<RequestAttachment> files = new RequestAttachmentTable().getFiles(schedule.getId(), schedule.getSenderEmail());

		// 対象者への催促のメール
		if ( type == 0 || type == 1 ) {
			mailSendName = senderName;
			// 件名
			// 最初のメールかそうでないかで件名を変える
			if(type == 1) {
				subject += "[再送] ";
			}
			subject += schedule.getEventName() + "(要求者："+ senderName +")";

			// 本文
			String resendContent = "";
			if(type == 1) {
				resendContent += "※このメールは、まだ入力されていない方に送信しています。<br><br> ";
			}

			content = "<html><body><br>" + resendContent + senderName + "(" + schedule.getSenderEmail() + ")" + "さんから日程調整が届いています。<br><br>"
					+ "回答は以下のURLから、"
					+ schedule.getEventDeadline()
					+ "までにお願いします。<br>"
					+ address + "/ScheduleManager/AnswerPage/" + randomURL + "<br><br>"
					+ "<hr align=\"left\" width=\"55%\"><br>"
					+ schedule.getEventContent()
					+ "<br><br><hr align=\"left\" width=\"55%\"><br>"
					+ "<br><br></body></html>";
		}
		// 対象者への決定のメール
		else if( type == 2 ) {
			mailSendName = senderName;
			String decideDate = schedule.getDecideDate();
			decideDate = decideDate.replace(",", "<br>");
			note = schedule.getNote();

			if( schedule.getisDecideFirst() ) {
				subject = "[日時決定]";
			} else {
				subject = "[日時決定変更]";
			}
			// 件名
			subject += schedule.getEventName() + "(要求者："+ senderName +")";

			// オリジナルメッセージ
			String originalMsg = "> " + senderName + "(" + schedule.getSenderEmail() + ")" + "さんから日程調整が届いています。<br><br>"
					+ "回答は以下のURLから、"
					+ schedule.getEventDeadline()
					+ "までにお願いします。<br>"
					+ address + "/ScheduleManager/AnswerPage/" + randomURL + "<br><br>"
					+ "<hr align=\"left\" width=\"55%\"><br>"
					+ schedule.getEventContent()
					+ "<br><br><hr align=\"left\" width=\"55%\">";

			// 本文
			if( schedule.getisDecideFirst() ) {
				content = "<html><body><br>" + schedule.getEventName() + "の日時が決定しました。" ;
			} else {
				content = "<html><body><br>" + schedule.getEventName() + "の日時の決定が変更されました。" ;
			}
			content += "(要求者："+ senderName +")<br><br>"
					+ "イベント名 : " + schedule.getEventName() + "<br>"
					+ "<hr align=\"left\" width=\"55%\"><br>"
					+ "[決定日時]<br>"
					+ decideDate + "<br><br>[備考]<br>"
					+ note
					+ "<br><br><hr align=\"left\" width=\"55%\"><br>"
					+ "<p>------------Original Message------------</p>"
					+ "<blockquote>" + originalMsg + "</blockquote>"
					+ "</body></html>";
		}
		// 全対象者が回答したら
		else if( type == 3 ) {
			senderEmail = from;
			// 件名
			subject = "日程調整が全員に回答されました";

			// 本文
			content = "<html><body><br>「" + schedule.getEventName() + "」が全員に回答されました。" + "<br><br>"
					+ "以下のURLからログインをして、日時を決定してください。<br>"
					+ address + "/ScheduleManager/AnswerPage/" + randomURL + "<br>"
					+ "<br><br><hr align=\"left\" width=\"55%\"><br>"
					+ "<table><tr><th>イベント名：</th><td>" + schedule.getEventName() + "</td></tr>"
					+ "<tr><th>イベント内容：</th><td>" + schedule.getEventContent() + "</td></tr>"
					+ "</table></body></html>";
		}
		// 締め切り来た時の通知
		else if ( type == 4 ) {
			mailSendName = senderName;
			// 件名
			subject += "[締切日時超過] " + schedule.getEventName() + "(要求者："+ senderName +")";

			// 本文
			String resendContent = "※このメールは、まだ入力されていない方に送信しています。<br>"
					+ "<font size=\"+2\" color=\"red\">締め切り時間を過ぎています。早急に回答を行ってください。</font><br><br>";

			content = "<html><body><br>" + resendContent + senderName + "(" + schedule.getSenderEmail() + ")" + "さんから日程調整が届いています。<br><br>"
					+ "回答は以下のURLからお願いします。<br>"
					+ address + "/ScheduleManager/AnswerPage/" + randomURL + "<br><br>"
					+ "<hr align=\"left\" width=\"55%\"><br>"
					+ schedule.getEventContent()
					+ "<br><br><hr align=\"left\" width=\"55%\"><br>"
					+ "<br><br></body></html>";
		}


		try {
			MimeMessage message = new MimeMessage(session);
			message.setHeader("Content-Transfer-Encoding", encoding);

			message.setSubject(subject, charset);

			// Set From:
			message.setFrom(new InternetAddress(senderEmail, mailSendName + "日程調整システム"));
			// Set ReplyTo:
			message.setReplyTo(new Address[]{new InternetAddress(from)});
			// Set To:
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));

			Multipart multiPart = new MimeMultipart();
			MimeBodyPart textBodyPart = new MimeBodyPart();
			textBodyPart.setText(content, charset);
			textBodyPart.setHeader("Content-Type", "text/html" + ";charset=\"" + charset + "\"");
			multiPart.addBodyPart(textBodyPart);

			if(type == 0) {
				// 添付ファイルがあるならば
				if (!files.isEmpty()) {
					for (int i = 0; i < files.size(); i++) {
						MimeBodyPart fileBodyPart = new MimeBodyPart();
						fileBodyPart.setDataHandler(new DataHandler(new ByteArrayDataSource(files.get(i).getFile(), "application/octet-stream")));
						fileBodyPart.setFileName(MimeUtility.encodeText(files.get(i).getFileName(), charset, "B"));
						fileBodyPart.setDescription("attachment");
						multiPart.addBodyPart(fileBodyPart);
					}
				}
			}

			message.setContent(multiPart);

			System.out.println("schedule id : " + schedule.getId());
			System.out.println(schedule.getSenderEmail() + "(" + senderName + ") to " + to);
			Transport.send(message);
			System.out.println("-------メール送信終了-------");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}

	}

}
