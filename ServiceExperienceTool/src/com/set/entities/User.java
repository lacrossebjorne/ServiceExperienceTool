package com.set.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class User implements java.io.Serializable {

	static final long serialVersionUID = 1L;
	private Long userId;
	private Role role;
	private String firstName;
	private String lastName;
	private String email;
	private String username;
	private String password;
	private String phoneNumber;
	private Date createdAt;
	private Date updatedAt;
	private boolean enabled;
	private List<Role> roles = new ArrayList<>();
	private Set<ResetPassword> resetPasswords = new HashSet<ResetPassword>(0);

	public User() {
	}

	public User(String firstName, String lastName, String email, String username, String password, Date createdAt,
			boolean enabled) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.username = username;
		this.password = password;
		this.createdAt = createdAt;
		this.enabled = enabled;
	}

	public User(Role role, String firstName, String lastName, String email, String username, String password,
			String phoneNumber, Date createdAt, Date updatedAt, boolean enabled, Set<ResetPassword> resetPasswords) {
		this.role = role;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.username = username;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.enabled = enabled;
		this.resetPasswords = resetPasswords;
	}


	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Role getRole() {
		return this.role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserName() {
		return this.username;
	}

	public void setUserName(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Date getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public boolean isEnabled() {
		return this.enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public Set<ResetPassword> getResetPasswords() {
		return this.resetPasswords;
	}

	public void setResetPasswords(Set<ResetPassword> resetPasswords) {
		this.resetPasswords = resetPasswords;
	}

	public User merge(User other) {
		this.roles.addAll(other.roles);
		this.resetPasswords.addAll(other.resetPasswords);
		return this;
	}
	
	 // Object overrides ---------------------------------------------------------------------------

    /**
     * The user ID is unique for each User. So this should compare User by ID only.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        return (other instanceof User) && (userId != null)
             ? userId.equals(((User) other).userId)
             : (other == this);
    }

    /**
     * The user ID is unique for each User. So User with same ID should return same hashcode.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (userId != null) 
             ? (this.getClass().hashCode() + userId.hashCode()) 
             : super.hashCode();
    }
	
	@Override
	public String toString() {
		return ("ID:" + userId + " First name: " +firstName + " Last name: " + lastName + " Username: " + username + " Email: "
				+ email + " Roles: " + roles + " Created date: " + createdAt + " Enabled: " + enabled + " Resetpass requests: " + resetPasswords);
		
	}
}
