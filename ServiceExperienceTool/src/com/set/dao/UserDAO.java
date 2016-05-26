package com.set.dao;

import java.util.List;

import com.set.entities.User;

/**
 * 
 * @author Björn Dalberg
 * @created 19 apr. 2016
 */

public interface UserDAO {
	
	public User find(Long id);
	
	public User find(String username, String password);
	
	public User find(String username);
	
	public List<User> listUsers();
	
	public User createUser(User user);
	
	public void updateUser(User user);
	
	public boolean deleteUser(User user);
	
	public boolean existUserName(String username);
	
	public void changePassword(User user);
	
	public void enableUser(User user);
	
	public void disableUser(User user);

}
