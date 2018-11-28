package schedule.model;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class RequestAttachment {

	private String fileName;
	private InputStream file;

	public RequestAttachment(String fileName, InputStream file) {
		this.fileName = fileName;
		this.file = file;
	}

	public String getFileName() {
		return fileName;
	}

	public InputStream getFile() {
		return file;
	}
}
