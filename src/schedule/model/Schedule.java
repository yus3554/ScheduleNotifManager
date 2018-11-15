package schedule.model;

public class Schedule {
	private String id;
	private String eventName;
	private String eventContent;
	private String eventStartDate;
	private String eventEndDate;
	private String eventDeadline;
	private String senderEmail;
	private String decideDate;
	private String note;
	private String fileName;

	public Schedule(String id, String eventName, String eventContent, String eventStartDate,
			String eventEndDate, String eventDeadline, String senderEmail, String decideDate, String note, String fileName) {
		this.id = id;
		this.eventName = eventName;
		this.eventContent = eventContent;
		this.eventStartDate = eventStartDate;
		this.eventEndDate = eventEndDate;
		this.eventDeadline = eventDeadline;
		this.senderEmail = senderEmail;
		this.decideDate = decideDate;
		this.note = note;
		this.fileName = fileName;
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

	public String getEventStartDate() {
		return eventStartDate;
	}

	public String getEventEndDate() {
		return eventEndDate;
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

	public String getFileName() {
		return fileName;
	}

}
