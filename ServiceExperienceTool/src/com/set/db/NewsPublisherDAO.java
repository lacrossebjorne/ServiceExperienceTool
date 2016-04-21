package com.set.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import javax.naming.NamingException;

public class NewsPublisherDAO extends AbstractDAO implements NewsPublisher {

	/**
	 * Main is just for testing purposes, for accessing/using an instance of
	 * this class without using a servlet.
	 * 
	 * @param args
	 */

	private final String SQL_INSERT_INTO_NEWS = "INSERT INTO news(header, content, created_at) VALUES(?, ?, NOW())";
	private final String SQL_INSERT_INTO_NEWS_IMAGE = "INSERT INTO news_image(news_id, image_id) VALUES(?, ?)";
	private final String SQL_INSERT_INTO_IMAGE = "INSERT INTO image(image_uri) values (?)";

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
		String[] imageUris = new String[] { "testimage.png", "testimage.jpg" };
		publisher.publishNews(content, subject, imageUris);
	}

	@Override
	public int publishNews(String subject, String content, String... imageUris) {

		int newsPrimaryKey = -1;

		try {
			connection = getConnection();
			connection.setAutoCommit(false);

			PreparedStatement publishPs = connection.prepareStatement(SQL_INSERT_INTO_NEWS,
					Statement.RETURN_GENERATED_KEYS);
			publishPs.setString(1, subject);
			publishPs.setString(2, content);

			if (publishPs.executeUpdate() == 0) {
				throw new SQLException("Couldn't insert rows into table news");
			}

			ResultSet rsGeneratedKeys = publishPs.getGeneratedKeys();

			if (rsGeneratedKeys.next()) {
				newsPrimaryKey = rsGeneratedKeys.getInt(1);

				if (imageUris != null && imageUris.length > 0) {
					Set<Integer> imageKeySet = new HashSet<Integer>();

					PreparedStatement insertImagePs = connection.prepareStatement(SQL_INSERT_INTO_IMAGE,
							Statement.RETURN_GENERATED_KEYS);

					for (String uri : imageUris) {
						insertImagePs.setString(1, uri);
						if (insertImagePs.executeUpdate() == 0) {
							throw new SQLException("Couldn't insert rows into table image");
						}

						ResultSet rsGeneratedImageKeys = insertImagePs.getGeneratedKeys();
						if (rsGeneratedImageKeys.next()) {
							imageKeySet.add(rsGeneratedImageKeys.getInt(1));
						}

					}
					PreparedStatement insertNewsImagePs = connection.prepareStatement(SQL_INSERT_INTO_NEWS_IMAGE);
					for (Integer imagePrimaryKey : imageKeySet) {
						insertNewsImagePs.setInt(1, newsPrimaryKey);
						insertNewsImagePs.setInt(2, imagePrimaryKey);
						Integer rowCount = insertNewsImagePs.executeUpdate();
						if (rowCount == 0) {
							throw new SQLException("Couldn't insert rows into table news_image");
						}
					}
				}

				connection.commit();
			}
		} catch (SQLException | NamingException e) {
			rollbackTransaction();
			e.printStackTrace();
		} finally {
			try {
				disconnect(connection);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return newsPrimaryKey;
	}

	private void rollbackTransaction() {
		try {
			connection.rollback();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
}
