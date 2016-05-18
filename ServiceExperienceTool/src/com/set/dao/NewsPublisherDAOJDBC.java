package com.set.dao;

import static com.set.dao.DAOUtil.prepareStatement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.set.entities.Image;
import com.set.entities.News;
import com.set.entities.NewsUrl;
import com.set.entities.Tag;

public class NewsPublisherDAOJDBC implements NewsPublisherDAO {
	private final String SQL_INSERT_INTO_NEWS = "INSERT INTO news(header, content, created_at) VALUES(?, ?, NOW())";
	private final String SQL_INSERT_INTO_IMAGE = "INSERT INTO image (image_uri) VALUE (?)";
	private final String SQL_INSERT_INTO_NEWS_IMAGE = "INSERT INTO news_image(news_id, image_id) VALUES(?, ?)";
	private final String SQL_INSERT_INTO_NEWS_URL = "INSERT INTO news_url(news_id, title, path) VALUES (?, ?, ?)";
	private final String SQL_DELETE_FROM_NEWS_URL = "DELETE FROM news_url WHERE news_url_id=?";
	private final String SQL_IMAGE_NEWS_DELETE = "DELETE FROM news_image WHERE news_id = ?";
	private final String SQL_NEWS_URL_SELECT = "SELECT news_url_id, news_id, title, path FROM news_url WHERE news_id=?";
	private final String SQL_UPDATE_NEWS = "UPDATE news SET header=?, content=?, updated_at=NOW() WHERE news_id=?";
	private final String SQL_DISABLE_NEWS = "UPDATE news SET enabled=false WHERE news_id=?";
	private final String SQL_SELECT_TAGS = "SELECT t.tag_id, t.text FROM news_tag AS nt INNER JOIN tag AS t ON nt.tag_id = t.tag_id WHERE news_id = ?";
	private final String SQL_DELETE_FROM_NEWS_TAG = "DELETE FROM news_tag WHERE news_id=? AND tag_id=?";
	private final String SQL_SELECT_TAG = "SELECT * FROM tag WHERE text=?";
	private final String SQL_INSERT_TAG = "INSERT INTO tag(text) VALUES(?)";
	private final String SQL_INSERT_NEWS_TAG = "INSERT INTO news_tag(news_id, tag_id) VALUES(?, ?)";
	
	private DAOFactory daoFactory;

	public NewsPublisherDAOJDBC(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@SuppressWarnings("resource")
	@Override
	public boolean publishNews(News news) {
		boolean published = false;

		List<String> imgUris = news.getImgUriList();
		List<NewsUrl> urlsFromNewsEntry = news.getUrlList() == null ? new ArrayList<NewsUrl>() : news.getUrlList();
		List<NewsUrl> urlsFromDB = new ArrayList<NewsUrl>();
		List<NewsUrl> urlsToAdd = new ArrayList<NewsUrl>();
		List<NewsUrl> urlsToRemove = new ArrayList<NewsUrl>();
		
		List<Tag> tagsFromNewsEntry = news.getTagData() == null ? new ArrayList<Tag>() : news.getTagData();
		List<Tag> tagsFromDB = new ArrayList<Tag>();
		List<Tag> tagsToAdd = new ArrayList<Tag>();
		List<Tag> tagsToRemove = new ArrayList<Tag>();
		
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		Long newsID = null;
		int[] batchResult = null;
		try {
			connection = daoFactory.getConnection();
			connection.setAutoCommit(false);

			if (news.getNewsId() == null) {
				// INSERT INTO NEWS
				Object[] newsObj = { news.getHeader(), news.getContent() };
				// Insert news article in news-table and retreive news PK
				statement = prepareStatement(connection, SQL_INSERT_INTO_NEWS, true, newsObj);
				if (statement.executeUpdate() == 0) {
					throw new SQLException("Couldn't insert rows into table news");
				}
				result = statement.getGeneratedKeys();
				if (result.next())
					newsID = result.getLong(1);
				news.setNewsId(newsID);
				statement.clearParameters();

				urlsToAdd = news.getUrlList();
				tagsToAdd = news.getTagData();
			} else {
				// UPDATE NEWS
				newsID = news.getNewsId();
				Object[] newsObj = { news.getHeader(), news.getContent(), news.getNewsId() };
				statement = prepareStatement(connection, SQL_UPDATE_NEWS, false, newsObj);
				if (statement.executeUpdate() == 0) {
					throw new SQLException("Couldn't update rows in table news");
				}
				
				//SELECT URLS FROM DB
				statement = prepareStatement(connection, SQL_NEWS_URL_SELECT, false, news.getNewsId());
				result = statement.executeQuery();
				
				while (result.next()) {
					Long newsUrlId = result.getLong("news_url_id");
					Long newsId = result.getLong("news_id");
					String title = result.getString("title");
					String path = result.getString("path");

					NewsUrl url = new NewsUrl(newsUrlId, newsId, title, path);
					urlsFromDB.add(url);
				}

				urlsToRemove.addAll(urlsFromDB);
				urlsToRemove.removeAll(urlsFromNewsEntry);

				urlsToAdd.addAll(urlsFromNewsEntry);
				urlsToAdd.removeAll(urlsFromDB);
				
				//SELECT TAGS FROM DB
				statement = prepareStatement(connection, SQL_SELECT_TAGS, false, news.getNewsId());
				result = statement.executeQuery();
				
				while (result.next()) {
					Long tagId = result.getLong("tag_id");
					String text = result.getString("text");
					
					Tag tag = new Tag(tagId, text);
					tagsFromDB.add(tag);
				}
				
				tagsToRemove.addAll(tagsFromDB);
				tagsToRemove.removeAll(tagsFromNewsEntry);
				
				tagsToAdd.addAll(tagsFromNewsEntry);
				tagsToAdd.removeAll(tagsFromDB);
			}

			// REMOVE/ADD IMAGEURIS (if any)
			if (imgUris != null && imgUris.size() > 0 && newsID != null) {
				removeNewsImages(newsID, statement, connection);
			}
			if (imgUris != null && imgUris.size() > 0) {
				addImageUris(imgUris, newsID, connection, statement, result, batchResult);
			}
			
			// REMOVE/ADD URLS (if any)
			if (urlsToRemove != null && urlsToRemove.size() > 0) {
				removeUrls(urlsToRemove, connection, statement, batchResult);
			}
			if (urlsToAdd != null && urlsToAdd.size() > 0) {
				addUrls(urlsToAdd, newsID, connection, statement, batchResult);
			}
			
			// REMOVE/ADD TAGS (if any)
			if (tagsToRemove != null && tagsToRemove.size() > 0) {
				removeTagsFromNews(tagsToRemove, newsID, connection, statement, batchResult);
			}
			if (tagsToAdd != null && tagsToAdd.size() > 0) {
				addTagsToNews(tagsToAdd, newsID, connection, statement, result);
			}

			connection.commit();
			published = true;
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try {
				connection.setAutoCommit(true);
				
				if (result != null)
					result.close();
				if (statement != null)
					statement.close();
				if (connection != null)
					connection.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return published;
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
	
	private void removeNewsImages(Long newsID, PreparedStatement statement, Connection connection) throws SQLException {
		statement = prepareStatement(connection, SQL_IMAGE_NEWS_DELETE, false, newsID);
		statement.executeUpdate();
	}

	public void addImageUris(List<String> imgUris, Long newsID, Connection connection, PreparedStatement statement,
			ResultSet result, int[] batchResult) throws SQLException {
		if (imgUris != null && imgUris.size() > 0) {
			Set<Long> imageKeySet = new HashSet<Long>();
			for (String uri : imgUris) {
				statement = prepareStatement(connection, SQL_INSERT_INTO_IMAGE, true, (Object) uri);
				if (statement.executeUpdate() == 0)
					throw new SQLException("Couldn't insert image into table image");
				result = statement.getGeneratedKeys();
				int i = 0;
				while (result.next()) {
					Image img = new Image();
					img.setImageId(result.getLong(1));
					img.setImageUri(imgUris.get(i));
					imageKeySet.add(img.getImageId());
					i++;
				}
				statement.clearParameters();
			}

			// Insert news-PK and image-PK into news_image-table.
			statement = connection.prepareStatement(SQL_INSERT_INTO_NEWS_IMAGE);
			for (Long imagePrimaryKey : imageKeySet) {
				statement.setLong(1, newsID);
				statement.setLong(2, imagePrimaryKey);
				statement.addBatch();
			}
			batchResult = statement.executeBatch();
			if (!isBatchSuccessful(batchResult)) {
				throw new SQLException("Couldn't insert image into table news_image");
			}
		}
	}

	private void addUrls(List<NewsUrl> urlList, Long newsID, Connection connection, PreparedStatement statement, 
			int[] batchResult) throws SQLException {
		if (urlList != null && urlList.size() > 0) {
			statement = connection.prepareStatement(SQL_INSERT_INTO_NEWS_URL);
			for (NewsUrl newsUrl : urlList) {
				statement.setLong(1, newsID);
				statement.setString(2, newsUrl.getTitle());
				statement.setString(3, newsUrl.getPath());
				statement.addBatch();
			}
			batchResult = statement.executeBatch();
			if (!isBatchSuccessful(batchResult)) {
				throw new SQLException("Couldn't insert into table newsUrl");
			}
		}
	}

	private void removeUrls(List<NewsUrl> urlList, Connection connection, PreparedStatement statement,
			int[] batchResult) throws SQLException {
		if (urlList != null && urlList.size() > 0) {
			statement = connection.prepareStatement(SQL_DELETE_FROM_NEWS_URL);
			for (NewsUrl newsUrl : urlList) {
				statement.setLong(1, newsUrl.getNewsUrlId());
				statement.addBatch();
			}
			batchResult = statement.executeBatch();
			if (!isBatchSuccessful(batchResult)) {
				throw new SQLException("Couldn't insert into table newsUrl");
			}
		}
	}
	
	private void addTagsToNews(List<Tag> tagsToAdd, Long newsID, Connection connection, PreparedStatement statement, ResultSet result) throws SQLException {
		if (tagsToAdd != null && tagsToAdd.size() > 0) {
			
			for (Tag tag : tagsToAdd) {
				statement = prepareStatement(connection, SQL_SELECT_TAG, false, tag.getText());
				result = statement.executeQuery();
				if (result.next()) {
					tag.setTagId(result.getLong("tag_id"));
				} else {
					statement = prepareStatement(connection, SQL_INSERT_TAG, true, tag.getText());
					if (statement.executeUpdate() == 0) {
						throw new SQLException("Couldn't insert rows into table tag");
					}
					result = statement.getGeneratedKeys();
					
					if (result.next()) {
						tag.setTagId(result.getLong(1));
					}
				}
				
				statement = prepareStatement(connection, SQL_INSERT_NEWS_TAG, false, new Object[] { newsID, tag.getTagId()});
				
				if (statement.executeUpdate() == 0) {
					throw new SQLException("Couldn't insert rows into table news_tag");
				}
			}
		}
	}

	private void removeTagsFromNews(List<Tag> tagsToRemove, Long newsID, Connection connection, PreparedStatement statement,
			int[] batchResult) throws SQLException {
		if (tagsToRemove != null && tagsToRemove.size() > 0) {
			
			statement = connection.prepareStatement(SQL_DELETE_FROM_NEWS_TAG);
			for (Tag tag : tagsToRemove) {
				statement.setLong(1, newsID);
				statement.setLong(2, tag.getTagId());
				statement.addBatch();
			}
			batchResult = statement.executeBatch();
			if (!isBatchSuccessful(batchResult)) {
				throw new SQLException("Couldn't insert into table newsUrl");
			}
		}
	}

	public boolean isBatchSuccessful(int[] updateCounts) {
		for (int i = 0; i < updateCounts.length; i++) {
			if (updateCounts[i] <= 0) {
				return false;
			}
		}
		return true;
	}
}
