package com.set.entities;

public class Role implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Long roleId;
	private String name;
	private String description;
	private Boolean enabled;

	public Role() {
	}

	public Role(Long roleId) {
		this.roleId = roleId;
	}

	public Role(Long roleId, String name, String description, Boolean enabled) {
		this.roleId = roleId;
		this.name = name;
		this.description = description;
		this.enabled = enabled;
	}

	public Long getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean isEnabled() {
		return this.enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	
	public String toString() {
		return "ID: " + roleId + " Rolename: " + name + " Description: " + description + " Enabled: " + enabled;
	}
}
