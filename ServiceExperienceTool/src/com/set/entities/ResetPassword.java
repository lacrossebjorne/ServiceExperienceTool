package com.set.entities;


public class ResetPassword implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Long resetPasswordId;
	private Long userId;
	private String securitycode;
	private String expirationTime;

	public ResetPassword() {
	}

	public ResetPassword(Long userId, String securitycode, String experationTime) {
		this.userId = userId;
		this.securitycode = securitycode;
		this.expirationTime = experationTime;
	}

	public Long getResetPasswordId() {
		return this.resetPasswordId;
	}

	public void setResetPasswordId(Long resetPasswordId) {
		this.resetPasswordId = resetPasswordId;
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getSecuritycode() {
		return this.securitycode;
	}

	public void setSecuritycode(String securitycode) {
		this.securitycode = securitycode;
	}

	public String getExpirationTime() {
		return this.expirationTime;
	}

	public void setExpirationTime(String experationTime) {
		this.expirationTime = experationTime;
	}
	
	public String toString() {
		return "ResetID: " + resetPasswordId + " Securitycode: " + securitycode + " Expiration time: " + expirationTime;
		
	}
}
