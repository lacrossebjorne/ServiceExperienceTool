package com.set.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class DriverManagerDAOFactory extends DAOFactory {

	private String url, username, password;

	public DriverManagerDAOFactory(String url, String username, String password) {
		this.url = url;
		this.username = username;
		this.password = password;
	}

	@Override
	Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url, username, password);
	}
}
