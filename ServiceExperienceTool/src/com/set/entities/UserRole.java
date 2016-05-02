package com.set.entities;

public class UserRole implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Integer userRoleId;
	private Role role;
	private User user;

	public UserRole() {
	}

	public UserRole(Role role, User user) {
		this.role = role;
		this.user = user;
	}

	public Integer getUserRoleId() {
		return this.userRoleId;
	}

	public void setUserRoleId(Integer userRoleId) {
		this.userRoleId = userRoleId;
	}

	public Role getRole() {
		return this.role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
