package com.set.dao;

/**
 * Used as a wrapper class for error messages when sending SMS
 * @author Emil
 *
 */

public class RecipientError {
	private String recipient;
	private String error;
	
	/**
	 * @param recipient recipient's phone number, e.g. +46768313154
	 * @param error the error message
	 */
	public RecipientError(String recipient, String error) {
		this.recipient = recipient;
		this.error = error;
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
}