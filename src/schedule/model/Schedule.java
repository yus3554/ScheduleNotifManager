package schedule.model;

public class Schedule {
	private String id;
	private String eventName;
	private String eventContent;
	private String eventDeadline;
	private String senderEmail;
	private String decideDate;
	private String note;
	private boolean isDecideFirst;

	public Schedule(String id, String eventName, String eventContent,
		 String eventDeadline, String senderEmail, String decideDate, String note, boolean isDecideFirst) {
		this.id = id;
		this.eventName = eventName;
		this.eventContent = eventContent;
		this.eventDeadline = eventDeadline;
		this.senderEmail = senderEmail;
		this.decideDate = decideDate;
		this.note = note;
		this.isDecideFirst = isDecideFirst;
	}

	public String getId() {
		return id;
	}

	public String getEventName() {
		return eventName;
	}

	public String getEventContent() {
		return eventContent;
	}

	public String getEventDeadline() {
		return eventDeadline;
	}

	public String getSenderEmail() {
		return senderEmail;
	}

	public String getDecideDate() {
		return decideDate;
	}

	public String getNote() {
		return note;
	}

	public boolean getisDecideFirst() {
		return isDecideFirst;
	}
}
