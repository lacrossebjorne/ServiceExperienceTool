package com.set.dao;

import java.util.List;

import com.set.entities.Event;

public interface EventDAO {

	public int insertEvent(Event event);
	public boolean updateEvent(int id, Event event);
	public boolean deleteEvent(int id);
	public List<Event> getEventList();
	
}
