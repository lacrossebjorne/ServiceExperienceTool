package com.set.entities;

import java.util.Date;


public class ResetPassword implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Long resetPasswordId;
	private User user;
	private String securitycode;
	private Date expirationTime;

	public ResetPassword() {
	}

	public ResetPassword(User user, String securitycode, Date experationTime) {
		this.user = user;
		this.securitycode = securitycode;
		this.expirationTime = experationTime;
	}

	public Long getResetPasswordId() {
		return this.resetPasswordId;
	}

	public void setResetPasswordId(Long resetPasswordId) {
		this.resetPasswordId = resetPasswordId;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getSecuritycode() {
		return this.securitycode;
	}

	public void setSecuritycode(String securitycode) {
		this.securitycode = securitycode;
	}

	public Date getExpirationTime() {
		return this.expirationTime;
	}

	public void setExpirationTime(Date experationTime) {
		this.expirationTime = experationTime;
	}
	
	public String toString() {
		return "ResetID: " + resetPasswordId + " Securitycode: " + securitycode + " Expiration time: " + expirationTime;
		
	}
}
