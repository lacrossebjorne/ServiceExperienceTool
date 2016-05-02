package com.set.entities;

import java.util.Date;

public class Client {
	
	private Long clientId;
	private Company company;
	private String name;
	private String description;
	private Date createdAt;
	private Date updatedAt;
	private boolean enabled;

	public Client() {
	}

	public Client(String name, Date createdAt, boolean enabled) {
		this.name = name;
		this.createdAt = createdAt;
		this.enabled = enabled;
	}

	public Client(Company company, String name, String description, Date createdAt, Date updatedAt, boolean enabled) {
		this.company = company;
		this.name = name;
		this.description = description;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.enabled = enabled;
	}


	public Long getClientId() {
		return this.clientId;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

	public Company getCompany() {
		return this.company;
	}

	public void setCompany(Company company) {
		this.company = company;
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

}
