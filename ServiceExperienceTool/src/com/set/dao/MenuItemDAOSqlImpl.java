package com.set.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.set.entities.Allergen;
import com.set.entities.MenuItem;

public class MenuItemDAOSqlImpl implements MenuItemDAO{

	private static final String SQL_LIST_ITEM_BY_MENU = "SELECT MI.menu_item_id AS id, name, description, details,menu_category_id AS category FROM menu_item AS MI JOIN item_in_menu AS IIM ON MI.menu_item_id = IIM.menu_item_id WHERE IIM.menu_id = ?";
	private static final String SQL_INSERT_ITEM = "INSERT INTO menu_item (name, description, details, menu_category_id) VALUES (?,?,?,?)";
	private static final String SQL_INSERT_ITEM_ALLERGENS = "INSERT INTO allergen_in_item (menu_item_id, allergen_id) VALUES (?,?)";
	private static final String SQL_INSERT_ITEM_IN_MENU = "INSERT INTO item_in_menu (menu_id, menu_item_id) VALUES (?,?)";

	
	private DAOFactory daoFactory;
	
	public MenuItemDAOSqlImpl(DAOFactory daoFactory){
		this.daoFactory = daoFactory;
	}
	
	public List<MenuItem> getItems(int menuId) {		
		List<MenuItem> items = null;
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet results = null;
		
		try {
			connection = daoFactory.getConnection();

			statement = connection.prepareStatement(SQL_LIST_ITEM_BY_MENU);
			statement.setInt(1, menuId);
			results = statement.executeQuery();

			items = new ArrayList<MenuItem>();
			while (results.next()) {
				int id = results.getInt("id");
				String name = results.getString("name");
				String description = results.getString("description");
				String details = results.getString("details");
				int category = results.getInt("category");
				List<Allergen> allergens = new AllergenDAOSqlImpl(daoFactory)
						.getAllergenListByItem(id, connection);
				
				MenuItem menuItem = new MenuItem(id, name, description, details, category, allergens);
				items.add(menuItem);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { if(results != null) results.close(); } catch (SQLException e) {};
			try { if(statement != null) statement.close(); } catch (SQLException e) {};
			try { if(connection != null) connection.close(); } catch (SQLException e) {};
		}
		
		return items;
	}

	@Override
	public int insertItem(int menuId, MenuItem item) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet results = null;
		int insertedId = -1;
		try {
			connection = daoFactory.getConnection();
			connection.setAutoCommit(false);

			statement = connection.prepareStatement(SQL_INSERT_ITEM, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, item.getName());
			statement.setString(2, item.getDescription());
			statement.setString(3, item.getDetails());
			statement.setInt(4, item.getCategory());
			statement.executeUpdate();
			
			results = statement.getGeneratedKeys();
			if (results.next()) {
				insertedId = results.getInt(1);
				results.close();
				statement.close();
				
				statement = connection.prepareStatement(SQL_INSERT_ITEM_ALLERGENS);
				for(Allergen a: item.getAllergens()){
					statement.setInt(1, insertedId);
					statement.setInt(2, a.getId());
					statement.addBatch();
				}
				statement.executeBatch();
				statement.close();
				
				statement = connection.prepareStatement(SQL_INSERT_ITEM_IN_MENU);
				statement.setInt(1, menuId);
				statement.setInt(2, insertedId);
				
			}
			connection.commit();
			
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try { if(results != null) results.close(); } catch (SQLException e) {};
			try { if(statement != null) statement.close(); } catch (SQLException e) {};
			try { if(connection != null) connection.setAutoCommit(true); connection.close(); } catch (SQLException e) {};
		}	
		return insertedId;
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

	@Override
	public MenuItem getItem(int menuId, int id) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
