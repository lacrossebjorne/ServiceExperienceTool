package com.set.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.set.entities.MenuCategory;

public class MenuCategoryDAOSqlImpl implements MenuCategoryDAO{
	
//	private static final String SQL_LIST_CATEGORIES_BY_MENU = "SELECT MC.menu_category_id AS id, MC.name AS name, MC.parent_category AS parent FROM menu_item AS MI JOIN item_in_menu AS IIM ON MI.menu_item_id = IIM.menu_item_id JOIN menu_category AS MC ON MC.menu_category_id = MI.menu_category_id WHERE IIM.menu_id = ? GROUP BY MC.menu_category_id";
	private static final String SQL_LIST_CATEGORIES_BY_MENU ="SELECT MC.menu_category_id AS id, MC.name AS name, MC.parent_category AS parent FROM menu_item AS MI JOIN item_in_menu AS IIM ON MI.menu_item_id = IIM.menu_item_id JOIN menu_category AS MC ON MC.menu_category_id = MI.menu_category_id WHERE IIM.menu_id = ? GROUP BY MC.menu_category_id UNION SELECT mc1.menu_category_id, mc1.name, mc1.parent_category FROM menu_category AS mc1 JOIN menu_category AS mc2 ON mc1.menu_category_id = mc2.parent_category";

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
		PreparedStatement statement = null;
		ResultSet results = null;
		try {
			connection = daoFactory.getConnection();

			statement = connection.prepareStatement(SQL_LIST_CATEGORIES_BY_MENU);
			statement.setInt(1, menuId);
			results = statement.executeQuery();

			categories = new ArrayList<MenuCategory>();
			while (results.next()) {
				Integer id = results.getInt("id");
				String name = results.getString("name");
				Integer parent = results.getInt("parent");
				categories.add(new MenuCategory(id,name, parent));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { if(results != null) results.close(); } catch (SQLException e) {};
			try { if(statement != null) statement.close(); } catch (SQLException e) {};
			try { if(connection != null) connection.close(); } catch (SQLException e) {};
		}	
		return categories;
	}

}
