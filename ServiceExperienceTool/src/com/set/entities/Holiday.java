package com.set.entities;

import java.util.Date;

public class Holiday implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	private Long holidayId;
	private Calendar calendar;
	private String name;
	private String description;
	private Date date;
	private Date createdAt;
	private Date updatedAt;
	private Boolean enabled;

	public Holiday() {
	}

	public Holiday(Calendar calendar, String name, String description, Date date, Date createdAt, Date updatedAt,
			Boolean enabled) {
		this.calendar = calendar;
		this.name = name;
		this.description = description;
		this.date = date;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.enabled = enabled;
	}
	
	public Long getHolidayId() {
		return this.holidayId;
	}

	public void setHolidayId(Long holidayId) {
		this.holidayId = holidayId;
	}
	
	public Calendar getCalendar() {
		return this.calendar;
	}

	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
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

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
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

}
