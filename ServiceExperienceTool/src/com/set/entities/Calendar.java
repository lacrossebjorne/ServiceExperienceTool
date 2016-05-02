package com.set.entities;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Calendar {
	
	private Long calendarId;
	private String name;
	private String description;
	private Date validUntil;
	private Date createdAt;
	private Date updatedAt;
	private Boolean enabled;
	private Set<Holiday> holidays = new HashSet<Holiday>(0);

	public Calendar() {
	}

	public Calendar(String name, String description, Date validUntil, Date createdAt, Date updatedAt, Boolean enabled,
			Set<Holiday> holidays) {
		this.name = name;
		this.description = description;
		this.validUntil = validUntil;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.enabled = enabled;
		this.holidays = holidays;
	}

	public Long getCalendarId() {
		return this.calendarId;
	}

	public void setCalendarId(Long calendarId) {
		this.calendarId = calendarId;
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

	public Date getValidUntil() {
		return this.validUntil;
	}

	public void setValidUntil(Date validUntil) {
		this.validUntil = validUntil;
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

	public Boolean getEnabled() {
		return this.enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Set<Holiday> getHolidays() {
		return this.holidays;
	}

	public void setHolidays(Set<Holiday> holidays) {
		this.holidays = holidays;
	}

}
