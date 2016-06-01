package com.set.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.set.entities.Event;
import com.set.entities.Menu;

public class EventDAOJDBC implements EventDAO {
	
	private static final String SQL_LIST_EVENTS = "SELECT * FROM event";
	private static final String SQL_DELETE_FROM_EVENT = "DELETE FROM event WHERE id=?";
	
	
	private DAOFactory daoFactory;
	
	public EventDAOJDBC(DAOFactory daoFactory){
		this.daoFactory = daoFactory;
	}
	
	@Override
	public int insertEvent(Event event) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean updateEvent(int id, Event event) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteEvent(int id) {
		// TODO Auto-generated method stub
		
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try{
			connection = daoFactory.getConnection();
			ps = connection.prepareStatement(SQL_DELETE_FROM_EVENT);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			return true;
		}catch(SQLException e){
			e.printStackTrace();
			return false;
		}finally{
			try { if(rs != null) rs.close(); } catch (SQLException e) {e.printStackTrace();};
			try { if(ps != null) ps.close(); } catch (SQLException e) {e.printStackTrace();};
			try { if(connection != null) connection.close(); } catch (SQLException e) {e.printStackTrace();};
		}
	}

	@Override
	public List<Event> getEventList() {
		List<Event> eventList = new ArrayList<Event>();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet results = null;
		
		try {
			connection = daoFactory.getConnection();

			statement = connection.prepareStatement(SQL_LIST_EVENTS);
			results = statement.executeQuery();

			while (results.next()) {
				int id = results.getInt("id");
				String title = results.getString("title");
				String description = results.getString("description");
				String start = results.getString("startAt");
				String end = results.getString("endAt");
				boolean isfullday = results.getBoolean("isFullDay");
				eventList.add(new Event(id,title,description,start,end,isfullday));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { if(results != null) results.close(); } catch (SQLException e) {};
			try { if(statement != null) statement.close(); } catch (SQLException e) {};
			try { if(connection != null) connection.close(); } catch (SQLException e) {};
		}
		return eventList;
	}
}
	
