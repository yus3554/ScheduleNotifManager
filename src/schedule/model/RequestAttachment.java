package schedule.model;

import java.io.InputStream;

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
