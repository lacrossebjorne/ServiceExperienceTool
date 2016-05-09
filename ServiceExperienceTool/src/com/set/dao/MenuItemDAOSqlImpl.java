package com.set.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.set.entities.Allergen;
import com.set.entities.MenuItem;

public class MenuItemDAOSqlImpl implements MenuItemDAO{

	private static final String SQL_LIST_ITEM_BY_MENU = "SELECT MI.menu_item_id AS id, name, description, details,menu_category_id AS category FROM menu_item AS MI JOIN item_in_menu AS IIM ON MI.menu_item_id = IIM.menu_item_id WHERE IIM.menu_id = ?";
	
	private DAOFactory daoFactory;
	
	public MenuItemDAOSqlImpl(DAOFactory daoFactory){
		this.daoFactory = daoFactory;
	}
	
	public List<MenuItem> getItems(int menuId) {		
		List<MenuItem> items = null;
		Connection connection = null;
		
		try {
			connection = daoFactory.getConnection();

			PreparedStatement statement = connection.prepareStatement(SQL_LIST_ITEM_BY_MENU);
			statement.setInt(1, menuId);
			ResultSet results = statement.executeQuery();

			items = new ArrayList<MenuItem>();
			while (results.next()) {
				int id = results.getInt("id");
				String name = results.getString("name");
				String description = results.getString("description");
				String details = results.getString("details");
				int category = results.getInt("category");
				List<Allergen> allergens = getAllergenList(id, connection);
				
				MenuItem menuItem = new MenuItem(id, name, description, details, category, allergens);
				items.add(menuItem);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return items;
	}

	@Override
	public int insertItem(int menuId, MenuItem item) {
		// TODO Auto-generated method stub
		return -1;
	}

	@Override
	public boolean updateItem(int id, MenuItem item) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteItem(int id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<MenuItem> getItems(int menuId, int categoryId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private List<Allergen> getAllergenList(int itemId, Connection connection) throws SQLException{
		List<Allergen> allergenList = new ArrayList<>();
		
		//TODO Return actual data instead of empty list
			
		return allergenList;
	}

	@Override
	public MenuItem getItem(int menuId, int id) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
