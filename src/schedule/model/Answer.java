package schedule.model;

import java.time.LocalDate;

public class Answer {

	private String id;
	private String[] targetEmails;
	private String senderEmail;
	private String eventStartDate;
	private String eventEndDate;

	public Answer(String id, String[] targetEmails, String senderEmail, String eventStartDate, String eventEndDate) {
		this.id = id;
		this.targetEmails = targetEmails;
		this.senderEmail = senderEmail;
		this.eventStartDate = eventStartDate;
		this.eventEndDate = eventEndDate;
	}

	public String getId() {
		return id;
	}

	public String[] getTargetEmails() {
		return targetEmails;
	}

	public String getSenderEmail() {
		return senderEmail;
	}

	public String getEventStartDate() {
		return eventStartDate;
	}

	public String getEventEndDate() {
		return eventEndDate;
	}

	// startとendの差を求める
	// end - start の日数が返ってくる
	public int getDateLength() {
		LocalDate startDate = LocalDate.parse(eventStartDate);
		LocalDate endDate = LocalDate.parse(eventEndDate);

		int length = endDate.compareTo(startDate);
		return length;
	}
}
