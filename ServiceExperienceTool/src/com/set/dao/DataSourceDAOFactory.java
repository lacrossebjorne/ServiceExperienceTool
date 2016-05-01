package com.set.dao;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

class DataSourceDAOFactory extends DAOFactory {
	private DataSource dataSource;

	public DataSourceDAOFactory(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}
}
