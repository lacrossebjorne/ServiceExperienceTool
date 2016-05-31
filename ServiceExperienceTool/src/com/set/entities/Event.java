package com.set.entities;

public class Event {
	private int id;
	private String title;
	private String description;
	private String startAt;
	private String endAt;
	private boolean isFullDay;
	
	public Event(int i, String t, String d, String s, String e, boolean is){
		this.id = i;
		this.title = t;
		this.description = d;
		this.startAt = s;
		this.endAt = e;
		this.isFullDay = is;
	}
	public int getId(){
		return this.id;
	}
	public String getTitle(){
		return this.title;
	}
	public String getDescription(){
		return this.description;
	}
	public String getStartAt(){
		return this.startAt;
	}
	public String getEndAt(){
		return this.endAt;
	}
	public boolean getIsFullDay(){
		return this.isFullDay;
	}
}
