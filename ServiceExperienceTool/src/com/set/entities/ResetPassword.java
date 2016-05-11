package com.set.entities;
// Generated 2016-apr-24 20:41:36 by Hibernate Tools 5.1.0.Alpha1

import java.util.Date;


public class ResetPassword implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
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
