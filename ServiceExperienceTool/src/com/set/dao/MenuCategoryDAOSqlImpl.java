package com.set.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.set.entities.MenuCategory;

public class MenuCategoryDAOSqlImpl implements MenuCategoryDAO{
	
	private static final String SQL_LIST_CATEGORIES_BY_MENU = "SELECT MC.menu_category_id AS id, MC.name AS name FROM menu_item AS MI JOIN item_in_menu AS IIM ON MI.menu_item_id = IIM.menu_item_id JOIN menu_category AS MC ON MC.menu_category_id = MI.menu_category_id WHERE IIM.menu_id = ? GROUP BY MC.menu_category_id";
	
	private DAOFactory daoFactory;
	
	public MenuCategoryDAOSqlImpl(DAOFactory daoFactory){
		this.daoFactory = daoFactory;
	}
	
	@Override
	public int insertCategory(MenuCategory category) {
		// TODO Auto-generated method stub
		return -1;
	}

	@Override
	public boolean updateCategory(int id, MenuCategory category) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteCategory(int id) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public List<MenuCategory> getCategories(int menuId) {
		List<MenuCategory> categories = null;
		Connection connection = null;
		try {
			connection = daoFactory.getConnection();

			PreparedStatement statement = connection.prepareStatement(SQL_LIST_CATEGORIES_BY_MENU);
			statement.setInt(1, menuId);
			ResultSet results = statement.executeQuery();

			categories = new ArrayList<MenuCategory>();
			while (results.next()) {
				int id = results.getInt("id");
				String name = results.getString("name");	
				categories.add(new MenuCategory(id,name));
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
		return categories;
	}

}
