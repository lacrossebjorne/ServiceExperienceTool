package com.set.sms;

@SuppressWarnings("serial")
public class SMSTeknikSenderException extends Exception {

	public static final int ACCESS_DENIED = 0;
	public static final int NO_XML_INPUT = 1;
	public static final int INVALID_PHONENUMBER = 2;
	public static final int OUT_OF_CREDITS = 3;
	public static final int UNKNOWN_STATUS = 4;

	private int code;

  	public SMSTeknikSenderException(int code) {
  		super("Error code: " + code);
  		this.code = code;
  	}

  	public int getErrorCode() {
  		return code;
  	}
}

