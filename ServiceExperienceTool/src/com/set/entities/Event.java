package com.set.entities;

public class Event {
	private int id;
	private String title;
	private String description;
	private String startAt;
	private String endAt;
	private boolean isFullDay;
	
	public Event(int id, String title, String desc, String start, String end, boolean isFullD){
		this.setId(id);
		this.setTitle(title);
		this.setDescription(desc);
		this.setStartAt(start);
		this.setEndAt(end);
		this.setFullDay(isFullD);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStartAt() {
		return startAt;
	}

	public void setStartAt(String startAt) {
		this.startAt = startAt;
	}

	public String getEndAt() {
		return endAt;
	}

	public void setEndAt(String endAt) {
		this.endAt = endAt;
	}

	public boolean isFullDay() {
		return isFullDay;
	}

	public void setFullDay(boolean isFullDay) {
		this.isFullDay = isFullDay;
	}
	
}
