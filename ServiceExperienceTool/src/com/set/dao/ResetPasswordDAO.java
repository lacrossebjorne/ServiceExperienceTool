package com.set.dao;

import com.set.entities.ResetPassword;

/**
 * 
 * @author Björn Dalberg
 * @created 19 apr. 2016
 */

public interface ResetPasswordDAO {
	
	public ResetPassword find(Long id);
	
	public ResetPassword find(String username);
	
	public void insert(ResetPassword resetPassword);
	
}
