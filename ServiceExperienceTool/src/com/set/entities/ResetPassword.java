package com.set.entities;


import java.util.Date;

public class ResetPassword {
	
	private Long resetPasswordId;
	private User user;
	private String securitycode;
	private Date experationTime;

	public ResetPassword() {
	}

	public ResetPassword(User user, String securitycode, Date experationTime) {
		this.user = user;
		this.securitycode = securitycode;
		this.experationTime = experationTime;
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

	public Date getExperationTime() {
		return this.experationTime;
	}

	public void setExperationTime(Date experationTime) {
		this.experationTime = experationTime;
	}

}
