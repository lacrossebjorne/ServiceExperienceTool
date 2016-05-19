package com.set.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.set.entities.Allergen;

public class AllergenDAOSqlImpl implements AllergenDAO{
	
	private static final String SQL_LIST_ALLERGEN_BY_ITEM = "SELECT A.allergen_id AS id, name FROM allergen AS A JOIN allergen_in_item AS AII ON A.allergen_id = AII.allergen_id WHERE AII.menu_item_id = ?";
	private static final String SQL_LIST_ALLERGEN_BY_MENU = "SELECT DISTINCT A.allergen_id AS id, A.name FROM allergen AS A JOIN allergen_in_item AS AII ON A.allergen_id = AII.allergen_id JOIN menu_item AS MI ON AII.menu_item_id = MI.menu_item_id JOIN item_in_menu AS IIM ON MI.menu_item_id = IIM.menu_item_id WHERE IIM.menu_id = ?;";
	
	private DAOFactory daoFactory;
	
	public AllergenDAOSqlImpl(DAOFactory daoFactory){
		this.daoFactory = daoFactory;
	}

	@Override
	public List<Allergen> getAllergenListByItem(int itemId, Connection connection) {
		List<Allergen> allergenList = new ArrayList<>();
		PreparedStatement statement = null;
		ResultSet results = null;
		try{
			statement = connection.prepareStatement(SQL_LIST_ALLERGEN_BY_ITEM);
			statement.setInt(1, itemId);
			results = statement.executeQuery();
			while (results.next()) {
				int id = results.getInt("id");
				String name = results.getString("name");
				allergenList.add(new Allergen(id, name));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { if(results != null) results.close(); } catch (SQLException e) {};
			try { if(statement != null) statement.close(); } catch (SQLException e) {};
		}
		return allergenList;
	}

	@Override
	public List<Allergen> getAllergenListByMenu(int menuId) {
		List<Allergen> allergenList = new ArrayList<>();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet results = null;
		
		try {
			connection = daoFactory.getConnection();
			statement = connection.prepareStatement(SQL_LIST_ALLERGEN_BY_MENU);
			statement.setInt(1, menuId);
			results = statement.executeQuery();
			while (results.next()) {
				int id = results.getInt("id");
				String name = results.getString("name");
				allergenList.add(new Allergen(id, name));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { if(results != null) results.close(); } catch (SQLException e) {};
			try { if(statement != null) statement.close(); } catch (SQLException e) {};
			try { if(connection != null) connection.close(); } catch (SQLException e) {};
		}	
		return allergenList;
	}

}
