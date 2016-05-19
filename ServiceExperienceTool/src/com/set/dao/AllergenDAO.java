package com.set.dao;

import java.sql.Connection;
import java.util.List;

import com.set.entities.Allergen;

public interface AllergenDAO {
	
	public List<Allergen> getAllergenListByItem(int itemId, Connection connection);
	public List<Allergen> getAllergenListByMenu(int menuId);

}
