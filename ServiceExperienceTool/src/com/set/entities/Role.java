package com.set.entities;


import java.util.HashSet;
import java.util.Set;

public class Role implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Long roleId;
	private String name;
	private String description;
	private Boolean enabled;
	private Set<UserRole> userRoles = new HashSet<UserRole>(0);
	private Set<User> users = new HashSet<User>(0);

	public Role() {
	}

	public Role(Long roleId) {
		this.roleId = roleId;
	}

	public Role(Long roleId, String name, String description, Boolean enabled, Set<UserRole> userRoles,
			Set<User> users) {
		this.roleId = roleId;
		this.name = name;
		this.description = description;
		this.enabled = enabled;
		this.userRoles = userRoles;
		this.users = users;
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

	public Set<UserRole> getUserRoles() {
		return this.userRoles;
	}

	public void setUserRoles(Set<UserRole> userRoles) {
		this.userRoles = userRoles;
	}

	public Set<User> getUsers() {
		return this.users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}
	
	public String toString() {
		return "ID: " + roleId + " Name: " + name + " Description: " + description + " Enabled: " + enabled;
	}
}
