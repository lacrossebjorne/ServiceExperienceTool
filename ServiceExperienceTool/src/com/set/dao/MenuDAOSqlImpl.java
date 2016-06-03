package com.set.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.set.entities.Menu;

public class MenuDAOSqlImpl implements MenuDAO{

	private static final String SQL_LIST_MENUS = "SELECT menu_id AS id, name FROM menu";
	private static final String SQL_INSERT_MENU = "INSERT INTO menu (name) VALUES (?)";
	
	private DAOFactory daoFactory;
	
	public MenuDAOSqlImpl(DAOFactory daoFactory){
		this.daoFactory = daoFactory;
	}
	
	@Override
	public int insertMenu(Menu menu) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet results = null;
		int insertedId = -1;
		try {
			connection = daoFactory.getConnection();

			statement = connection.prepareStatement(SQL_INSERT_MENU, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, menu.getName());
			statement.executeUpdate();
			
			results = statement.getGeneratedKeys();
			if (results.next()) {
				insertedId = results.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { if(results != null) results.close(); } catch (SQLException e) {};
			try { if(statement != null) statement.close(); } catch (SQLException e) {};
			try { if(connection != null) connection.close(); } catch (SQLException e) {};
		}	
		return insertedId;
	}

	@Override
	public boolean updateMenu(int id, Menu menu) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteMenu(int id) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public List<Menu> getMenuList(){
		List<Menu> menuList = new ArrayList<Menu>();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet results = null;
		
		try {
			connection = daoFactory.getConnection();

			statement = connection.prepareStatement(SQL_LIST_MENUS);
			results = statement.executeQuery();

			while (results.next()) {
				int id = results.getInt("id");
				String name = results.getString("name");	
				menuList.add(new Menu(id,name));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { if(results != null) results.close(); } catch (SQLException e) {};
			try { if(statement != null) statement.close(); } catch (SQLException e) {};
			try { if(connection != null) connection.close(); } catch (SQLException e) {};
		}
		return menuList;
	}
	
}
