package com.set.dao;

import java.util.List;

import com.set.entities.Event;

public interface EventsDAO {
	
	public int insertEvent(Event e);
	public boolean updateEvent(int id, Event e);
	public boolean deleteEvent(int id);
	public List<Event> getEventList();
	
}
