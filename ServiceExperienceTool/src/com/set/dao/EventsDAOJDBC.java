package com.set.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.set.entities.Event;

public class EventsDAOJDBC implements EventsDAO {
	
	private static final String SQL_LIST_EVENTS = "SELECT id, title, description, startAt, endAt, isFullDay "
												+ "FROM events";
	private DAOFactory daoFactory;
	
	public EventsDAOJDBC(DAOFactory daoFactory){
		this.daoFactory= daoFactory;
	}

	@Override
	public int insertEvent(Event e) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean updateEvent(int id, Event e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteEvent(int id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Event> getEventList() {
		List<Event> eventList = new ArrayList<Event>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet result = null;
		
		try{
			conn = daoFactory.getConnection();
			ps = conn.prepareStatement(SQL_LIST_EVENTS);
			result = ps.executeQuery();
			
			while(result.next()){
				int id = result.getInt("id");
				String title = result.getString("title");
				String description = result.getString("description");
				String startAt = result.getString("startAt");
				String endAt = result.getString("endAt");
				boolean isFullDay = result.getBoolean("isFullDay");
				
				eventList.add(new Event(id, title, description, startAt, endAt, isFullDay));
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			try{ if(result != null) result.close();}catch(SQLException e){};
			try{ if(ps != null) ps.close();}catch(SQLException e){};
			try{ if(conn != null) conn.close();}catch(SQLException e){};
		}
		return eventList;
				
	}
		
}
	

