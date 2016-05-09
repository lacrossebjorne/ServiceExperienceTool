package com.set.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.set.entities.Menu;

public class MenuDAOSqlImpl implements MenuDAO{

	private static final String SQL_LIST_MENUS = "SELECT menu_id AS id, name FROM menu";
	
	private DAOFactory daoFactory;
	
	public MenuDAOSqlImpl(DAOFactory daoFactory){
		this.daoFactory = daoFactory;
	}
	
	@Override
	public int insertMenu(Menu menu) {
		// TODO Auto-generated method stub
		return 0;
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
		
		try {
			connection = daoFactory.getConnection();

			PreparedStatement statement = connection.prepareStatement(SQL_LIST_MENUS);
			ResultSet results = statement.executeQuery();

			while (results.next()) {
				int id = results.getInt("id");
				String name = results.getString("name");	
				menuList.add(new Menu(id,name));
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
		return menuList;
	}
	
}
