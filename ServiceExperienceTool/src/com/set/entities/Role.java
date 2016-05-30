package com.set.entities;

import java.util.ArrayList;
import java.util.List;

public class Role implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Long roleId;
	private String name;
	private String description;
	private Boolean enabled = false;
	private List<User> userList = new ArrayList<>();

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
	
	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}
	
	public Role merge(Role other) {
		this.userList.addAll(other.userList);
		return this;
	}

	 // Object overrides ---------------------------------------------------------------------------

    /**
     * The role ID is unique for each Role. So this should compare Role by ID only.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        return (other instanceof Role) && (roleId != null)
             ? roleId.equals(((Role) other).roleId)
             : (other == this);
    }

    /**
     * The role ID is unique for each Role. So Role with same ID should return same hashcode.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (roleId != null) 
             ? (this.getClass().hashCode() + roleId.hashCode()) 
             : super.hashCode();
    }
    
	public String toString() {
		return "ID: " + roleId + " Rolename: " + name + " Description: " + description + " Enabled: " + enabled + " Users: " + userList;
	}
}
