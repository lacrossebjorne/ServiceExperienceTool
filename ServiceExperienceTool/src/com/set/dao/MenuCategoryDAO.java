package com.set.dao;

import java.util.List;

import com.set.entities.MenuCategory;

public interface MenuCategoryDAO {

	public int insertCategory(MenuCategory category);
	public boolean updateCategory(int id, MenuCategory category);
	public boolean deleteCategory(int id);
	public List<MenuCategory> getCategories(int menuId);
	
}
