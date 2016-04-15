package com.set.db;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public abstract class AbstractDAO {

	private Context initCtx = null;
	private Context envCtx = null;
	private DataSource ds = null;
	private Connection connection;

	public Connection getConnection() throws NamingException, SQLException {

		if (connection == null || connection.isClosed()) {
			connect();
		}

		if (connection == null) {
			throw new NullPointerException("No valid connection!");
		}

		return connection;
	}

	private void connect() throws NamingException, SQLException {
		if (initCtx == null)
			initCtx = new InitialContext();
		if (envCtx == null)
			envCtx = (Context) initCtx.lookup("java:comp/env");
		if (ds == null) {
			ds = (DataSource) envCtx.lookup("jdbc/setdb");
		}
		if (connection == null)
			connection = ds.getConnection();
	}

	public void disconnect(Connection connection) throws SQLException {
		if (connection != null) {
			connection.close();
		}
	}
}
