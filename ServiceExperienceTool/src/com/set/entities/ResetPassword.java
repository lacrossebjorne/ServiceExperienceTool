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
	
	 // Object overrides ---------------------------------------------------------------------------

    /**
     * The role ID is unique for each Role. So this should compare Role by ID only.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        return (other instanceof Role) && (resetPasswordId != null)
             ? resetPasswordId.equals(((ResetPassword) other).resetPasswordId)
             : (other == this);
    }

    /**
     * The role ID is unique for each Role. So Role with same ID should return same hashcode.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (resetPasswordId != null) 
             ? (this.getClass().hashCode() + resetPasswordId.hashCode()) 
             : super.hashCode();
    }
	
	public String toString() {
		return "ResetID: " + resetPasswordId + " Securitycode: " + securitycode + " Expiration time: " + expirationTime;
		
	}
}
