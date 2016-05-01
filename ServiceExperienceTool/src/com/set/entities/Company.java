package com.set.entities;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Company {
	
	private Long companyId;
	private String name;
	private String description;
	private Date createdAt;
	private Date updatedAt;
	private boolean enabled;
	private Set<Client> clients = new HashSet<Client>(0);

	public Company() {
	}

	public Company(String name, Date createdAt, boolean enabled) {
		this.name = name;
		this.createdAt = createdAt;
		this.enabled = enabled;
	}

	public Company(String name, String description, Date createdAt, Date updatedAt, boolean enabled,
			Set<Client> clients) {
		this.name = name;
		this.description = description;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.enabled = enabled;
		this.clients = clients;
	}

	public Long getCompanyId() {
		return this.companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
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

	public Set<Client> getClients() {
		return this.clients;
	}

	public void setClients(Set<Client> clients) {
		this.clients = clients;
	}

}
