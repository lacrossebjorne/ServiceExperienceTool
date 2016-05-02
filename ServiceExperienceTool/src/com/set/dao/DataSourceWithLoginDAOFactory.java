package com.set.dao;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

class DataSourceWithLoginDAOFactory extends DAOFactory {
	private String username, password;
	private DataSource dataSource;

	public DataSourceWithLoginDAOFactory(DataSource dataSource, String username, String password) {
		this.dataSource = dataSource;
		this.username = username;
		this.password = password;
	}

	@Override
	Connection getConnection() throws SQLException {
		return dataSource.getConnection(username, password);
	}
}
