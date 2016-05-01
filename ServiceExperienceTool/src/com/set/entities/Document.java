package com.set.entities;

import java.util.Date;

public class Document {

	private Long documentId;
	private String name;
	private String description;
	private Date validFrom;
	private Date validUntil;
	private String documentContents;
	private Date createdAt;
	private Date updatedAt;
	private boolean enabled;

	public Document() {
	}

	public Document(String name, Date createdAt, boolean enabled) {
		this.name = name;
		this.createdAt = createdAt;
		this.enabled = enabled;
	}

	public Document(String name, String description, Date validFrom, Date validUntil, String documentContents,
			Date createdAt, Date updatedAt, boolean enabled) {
		this.name = name;
		this.description = description;
		this.validFrom = validFrom;
		this.validUntil = validUntil;
		this.documentContents = documentContents;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.enabled = enabled;
	}


	public Long getDocumentId() {
		return this.documentId;
	}

	public void setDocumentId(Long documentId) {
		this.documentId = documentId;
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

	public Date getValidFrom() {
		return this.validFrom;
	}

	public void setValidFrom(Date validFrom) {
		this.validFrom = validFrom;
	}

	public Date getValidUntil() {
		return this.validUntil;
	}

	public void setValidUntil(Date validUntil) {
		this.validUntil = validUntil;
	}

	public String getDocumentContents() {
		return this.documentContents;
	}

	public void setDocumentContents(String documentContents) {
		this.documentContents = documentContents;
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
