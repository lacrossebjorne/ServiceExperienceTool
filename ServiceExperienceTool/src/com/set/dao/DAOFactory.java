package com.set.dao;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


public abstract class DAOFactory {

	private static final String PROPERTY_URL = "url";
	private static final String PROPERTY_USERNAME = "username";
	private static final String PROPERTY_PASSWORD = "password";
	private static final String PROPERTY_DRIVER = "driver";

	public static DAOFactory getInstance(String name) {
		if (name.equals(null))
			System.err.println("Database name is null");

		DAOPropertiesLoader properties = new DAOPropertiesLoader(name);
		String url = properties.getProperties(PROPERTY_URL, true);
		String username = properties.getProperties(PROPERTY_USERNAME, false);
		String password = properties.getProperties(PROPERTY_PASSWORD, false);
		String driver = properties.getProperties(PROPERTY_DRIVER, false);
		DAOFactory instance;

		if (driver != null) {
			try {
				Class.forName(driver);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			instance = new DriverManagerDAOFactory(url, username, password);
		}
		// Else assume URL as DataSource URL and lookup it in the JNDI.
		else {
			DataSource dataSource = null;
			try {
				dataSource = (DataSource) new InitialContext().lookup(url);
			} catch (NamingException e) {
				e.printStackTrace();
			}
			if (username != null) {
				instance = new DataSourceWithLoginDAOFactory(dataSource, username, password);
			} else {
				instance = new DataSourceDAOFactory(dataSource);
			}
		}
		return instance;
	}

	abstract Connection getConnection() throws SQLException;

	public UserDAO getUserDAO() {
		return new UserDAOJDBC(this);
	}

	public ResetPasswordDAO getResetPasswordDAO() {
		return new ResetPasswordDAOJDBC(this);
	}
	
	public NewsPublisherDAO getNewsPublisherDAO() {
		return new NewsPublisherDAOJDBC(this);
	}
	
	public NewsReaderDAO getNewsReaderDAO() {
		return new NewsReaderDAOJDBC(this);
	}
	
	public MenuItemDAO getMenuItemDAO() {
		return new MenuItemDAOSqlImpl(this);
	}
	
	public MenuCategoryDAO getMenuCategoryDAO() {
		return new MenuCategoryDAOSqlImpl(this);
	}
	
	public MenuDAO getMenuDAO(){
		return new MenuDAOSqlImpl(this);
	}
	
	public AllergenDAO getAllergenDAO(){
		return new AllergenDAOSqlImpl(this);
	}
	
	public RoleDAO getRoleDAO() {
		return new RoleDAOJDBC(this);
	}
	public EventsDAO getEventDAO(){
		return new EventsDAOJDBC(this);
	}
}
