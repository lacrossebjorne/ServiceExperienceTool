package com.set.entities;

public class SmsWrapper {
	String[] recipients;
	String message;

	public SmsWrapper(String[] recipients, String message) {
		this.recipients = recipients;
		this.message = message;
	}

	public String[] getRecipients() {
		return recipients;
	}

	public void setRecipients(String[] recipients) {
		this.recipients = recipients;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
