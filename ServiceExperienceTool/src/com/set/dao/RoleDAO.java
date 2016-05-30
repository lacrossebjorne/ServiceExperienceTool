package com.set.dao;

import java.util.List;

import com.set.entities.Role;

/**
 * @author Björn Dalberg
 * @created 4 maj 2016
 */
public interface RoleDAO {
	
	public Role find(Long id);
	
	public Role find(String name);
	
	public List<Role> listRoles();
	
	public Role createRole(Role role);
	
	public boolean updateRole(Role role);
	
	public boolean deleteRole(Role role);
	
	public void enableRole(Role role);
	
	public void disableRole(Role role);
	
}
