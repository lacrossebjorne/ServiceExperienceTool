package com.set.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.naming.NamingException;

public class NewsPublisherDAO extends AbstractDAO implements NewsPublisher {

	/**
	 * Main is just for testing purposes, for accessing/using an instance of this class without using a servlet.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		Properties props = new Properties();
		FileInputStream fis = null;

		NewsPublisherDAO publisher = new NewsPublisherDAO();

		try {
			fis = new FileInputStream("db.properties");
			props.load(fis);
			String url = props.getProperty("MYSQL_DB_URL");
			String username = props.getProperty("MYSQL_DB_USERNAME");
			String password = props.getProperty("MYSQL_DB_PASSWORD");
			Class.forName("com.mysql.jdbc.Driver");
			publisher.connection = DriverManager.getConnection(url, username, password);
		} catch (IOException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
		String subject = "Time has passed!";
		String content = "Time for dinner!";
		publisher.publishNews(content, subject);
	}

	/**
	 * This method is not finished, please do not add modifications to it
	 * @param subject
	 * @param content
	 * @return
	 */
	public int publishNews(String subject, String content) {

		int primaryKey = -1;

		try {
			connection = getConnection();

			String publishQry = "INSERT INTO news(header, content, created_at) VALUES(?, ?, NOW())";
			PreparedStatement publishPs = connection.prepareStatement(publishQry, Statement.RETURN_GENERATED_KEYS);
			publishPs.setString(1, subject);
			publishPs.setString(2, content);
			publishPs.executeUpdate();

			ResultSet rsGeneratedKeys = publishPs.getGeneratedKeys();

			if (rsGeneratedKeys.next()) {
				primaryKey = rsGeneratedKeys.getInt(1);
			}
		} catch (SQLException | NamingException e) {
			e.printStackTrace();
		} finally {
			try {
				disconnect(connection);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return primaryKey;
	}
}
