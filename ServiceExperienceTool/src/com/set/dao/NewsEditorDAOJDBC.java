package com.set.dao;

import static com.set.dao.DAOUtil.prepareStatement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.set.entities.News;

public class NewsEditorDAOJDBC implements NewsEditorDAO {

	private final String SQL_UPDATE_NEWS = "UPDATE news SET header=?, content=?, updated_at=NOW() WHERE news_id=?";
	private final String SQL_DISABLE_NEWS = "UPDATE news SET enabled=false WHERE news_id=?";

	private DAOFactory daoFactory;

	public NewsEditorDAOJDBC(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public boolean updateNews(News newsEntry) {

		boolean isUpdated = false;
		Object[] newsObj = { newsEntry.getHeader(), newsEntry.getContent(), newsEntry.getNewsId() };
		Connection connection = null;
		PreparedStatement statement = null;

		try {
			connection = daoFactory.getConnection();
			connection.setAutoCommit(false);

			statement = prepareStatement(connection, SQL_UPDATE_NEWS, false, newsObj);

			if (statement.executeUpdate() == 0) {
				throw new SQLException("Couldn't update row in news with id: " + newsEntry.getNewsId());
			} 
			connection.commit();
			isUpdated = true;
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return isUpdated;
	}

	@Override
	public boolean disableNewsEntry(Long id) {
		
		boolean isDisabled = false;
		Object[] newsObj = { id };
		Connection connection = null;
		PreparedStatement statement = null;

		try {
			connection = daoFactory.getConnection();

			statement = prepareStatement(connection, SQL_DISABLE_NEWS, false, newsObj);

			if (statement.executeUpdate() == 0) {
				throw new SQLException("Couldn't disable row in news with id: " + id);
			} 
			isDisabled = true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return isDisabled;
	}

}
