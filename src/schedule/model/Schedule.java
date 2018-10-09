package schedule.model;

public class Schedule {
	private String id;
	private String eventName;
	private String eventContent;
	private String eventStartDate;
	private String eventEndDate;
	private String eventDeadlineDate;
	private String senderEmail;
	private String decideDate;
	private String note;

	public Schedule(String id, String eventName, String eventContent, String eventStartDate,
			String eventEndDate, String eventDeadlineDate, String senderEmail, String decideDate, String note) {
		this.id = id;
		this.eventName = eventName;
		this.eventContent = eventContent;
		this.eventStartDate = eventStartDate;
		this.eventEndDate = eventEndDate;
		this.eventDeadlineDate = eventDeadlineDate;
		this.senderEmail = senderEmail;
		this.decideDate = decideDate;
		this.note = note;
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

	public String getEventDeadlineDate() {
		return eventDeadlineDate;
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

}
