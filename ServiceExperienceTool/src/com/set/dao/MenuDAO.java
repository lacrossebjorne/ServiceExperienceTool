package com.set.dao;

import java.util.List;

import com.set.entities.Menu;

public interface MenuDAO {

	public int insertMenu(Menu menu);
	public boolean updateMenu(int id, Menu menu);
	public boolean deleteMenu(int id);
	public List<Menu> getMenuList();
	
}
