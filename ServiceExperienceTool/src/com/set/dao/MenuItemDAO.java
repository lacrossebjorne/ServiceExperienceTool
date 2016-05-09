package com.set.dao;

import java.util.List;

import com.set.entities.MenuItem;

public interface MenuItemDAO {

	public int insertItem(int menuId, MenuItem item);
	public boolean updateItem(int id, MenuItem item);
	public boolean deleteItem(int id);
	public MenuItem	getItem(int menuId, int id);
	public List<MenuItem> getItems(int menuId);
	public List<MenuItem> getItems(int menuId, int categoryId);
	
}
