package com.set.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.set.dao.helpers.IsoDateConverter;
import com.set.entities.News;
import com.set.entities.NewsUrl;
import com.set.entities.Tag;

public class NewsReaderDAOJDBC implements NewsReaderDAO {

	private final String SQL_NEWS_SELECT_ALL = "SELECT news_id, header, content, created_at, updated_at, important_until FROM news ORDER BY created_at DESC, news_id DESC LIMIT ? OFFSET ?";
	private final String SQL_NEWS_SELECT_IMPORTANT = "SELECT news_id, header, content, created_at, updated_at, important_until FROM news WHERE enabled=true AND important_until > NOW() ORDER BY created_at DESC, news_id DESC";
	private final String SQL_NEWS_SELECT_ENABLED = "SELECT news_id, header, content, created_at, updated_at, important_until FROM news WHERE enabled=true AND important_until IS NULL OR important_until <= NOW() ORDER BY created_at DESC, news_id DESC LIMIT ? OFFSET ?";
	private final String SQL_IN_PLACEHOLDER_NEWS_SELECT_ENABLED_WITH_TAGS = "SELECT DISTINCT N.news_id, header, content, created_at, updated_at, important_until FROM news AS N JOIN news_tag AS NT ON N.news_id = NT.news_id JOIN tag AS T ON NT.tag_id = T.tag_id WHERE N.enabled = true AND important_until IS NULL AND T.text IN (#) ORDER BY created_at DESC, news_id DESC LIMIT ? OFFSET ?";
	private final String SQL_IMAGE_SELECT = "SELECT * FROM image AS i INNER JOIN news_image AS ni ON i.image_id = ni.image_id WHERE ni.news_id=?";
	private final String SQL_NEWS_URL_SELECT = "SELECT news_url_id, news_id, title, path FROM news_url WHERE news_id=?";
	private final String SQL_TAGS_SELECT = "SELECT t.tag_id, t.text FROM news_tag AS nt INNER JOIN tag AS t ON nt.tag_id = t.tag_id WHERE news_id = ?";
	private final String SQL_ACTIVE_TAGS_SELECT = "SELECT DISTINCT nt.tag_id, t.text FROM news_tag AS nt INNER JOIN tag AS t ON nt.tag_id = t.tag_id";
	private DAOFactory daoFactory;
	private String imagePath = "";

	public NewsReaderDAOJDBC(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public List<Tag> getActiveTags() {

		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		List<Tag> activeTags = new ArrayList<Tag>();
		try {
			connection = daoFactory.getConnection();
			statement = connection.createStatement();
			result = statement.executeQuery(SQL_ACTIVE_TAGS_SELECT);
			
			while (result.next()) {
				activeTags.add(new Tag(result.getLong("tag_id"), result.getString("text")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (result != null) {
					result.close();
				}
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

		return activeTags;
	}

	@SuppressWarnings("resource")
	@Override
	public List<News> getNews(Integer selectedPage, Integer resultsPerPage, Integer offset,
			boolean isDisabledEntriesIncluded, boolean isImportantSelected, List<Tag> tags) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet newsResult = null;
		ResultSet imgResult = null;
		ResultSet urlResult = null;
		ResultSet tagResult = null;
		List<News> newsAndImages = null;
		IsoDateConverter isoDateConverter = new IsoDateConverter();

		try {
			connection = daoFactory.getConnection();

			if (isImportantSelected) {
				// OFFSET AND LIMIT IGNORED
				statement = connection.prepareStatement(SQL_NEWS_SELECT_IMPORTANT);
			} else if (tags == null || tags.size() == 0) {
				// IF NO TAGS AT ALL
				statement = connection
						.prepareStatement(isDisabledEntriesIncluded ? SQL_NEWS_SELECT_ALL : SQL_NEWS_SELECT_ENABLED);
				statement.setInt(1, resultsPerPage);
				statement.setInt(2, offset);
			} else {
				// IF TAGS ARE REQUESTED, CREATE A SQL-STRING DYNAMICALLY
				String sqlNewsSelectAllEnabledWithTags = createSQLFromInPlaceholder(
						SQL_IN_PLACEHOLDER_NEWS_SELECT_ENABLED_WITH_TAGS, tags.size());
				statement = connection.prepareStatement(sqlNewsSelectAllEnabledWithTags);

				int statementIndex = 1;
				for (Tag tag : tags) {
					statement.setString(statementIndex++, tag.getText());
				}
				statement.setInt(statementIndex++, resultsPerPage);
				statement.setInt(statementIndex++, offset);
			}

			System.out.println("offset: " + offset);

			newsResult = statement.executeQuery();

			System.out.println(isImportantSelected ? "Fetching important news..." : "Fetching news...");
			newsAndImages = new ArrayList<>();
			while (newsResult.next()) {
				News newsEntry = new News();
				newsEntry.setNewsId(newsResult.getLong("news_id"));
				newsEntry.setHeader(newsResult.getString("header"));
				newsEntry.setContent(newsResult.getString("content"));

				Timestamp timestampCreated = newsResult.getTimestamp("created_at");
				if (timestampCreated != null) {
					String createdAtUTC = isoDateConverter.parseToUTCString(timestampCreated.getTime());
					System.out.print("newsID: " + newsEntry.getNewsId() + ", newsHeader: " + newsEntry.getHeader());
					System.out.print(", createdAt: " + createdAtUTC);
					newsEntry.setCreatedAt(createdAtUTC);
				}

				Timestamp timestampUpdated = newsResult.getTimestamp("updated_at");

				if (timestampUpdated != null) {
					String updatedAtUTC = isoDateConverter.parseToUTCString(timestampUpdated.getTime());
					System.out.print(", updatedAt: " + updatedAtUTC);
					newsEntry.setUpdatedAt(updatedAtUTC);
				}

				if (isImportantSelected) {
					Timestamp timestampImportant = newsResult.getTimestamp("important_until");
					if (timestampImportant != null) {
						String importantUntilUTC = isoDateConverter.parseToUTCString(timestampImportant.getTime());
						System.out.println(", importantUntil: " + importantUntilUTC);
						newsEntry.setImportantUntil(importantUntilUTC);
					}
					newsEntry.setIsImportant(true);
				} else {
					newsEntry.setIsImportant(false);
				}

				// select images from table image
				statement.clearParameters();
				statement = connection.prepareStatement(SQL_IMAGE_SELECT);
				statement.setLong(1, newsEntry.getNewsId());
				imgResult = statement.executeQuery();
				while (imgResult.next()) {
					if (newsEntry.getImgUriList() == null) {
						newsEntry.setImgUriList(new ArrayList<>());
					}
					String uri = imgResult.getString("image_uri");
					newsEntry.getImgUriList().add(imagePath + uri);
				}
				newsAndImages.add(newsEntry);

				// select newsUrls from table news_url
				statement.clearParameters();
				statement = connection.prepareStatement(SQL_NEWS_URL_SELECT);
				statement.setLong(1, newsEntry.getNewsId());
				urlResult = statement.executeQuery();
				while (urlResult.next()) {
					if (newsEntry.getUrlList() == null) {
						newsEntry.setUrlList(new ArrayList<>());
					}
					Long newsUrlId = urlResult.getLong("news_url_id");
					String title = urlResult.getString("title");
					String path = urlResult.getString("path");

					newsEntry.getUrlList().add(new NewsUrl(newsUrlId, newsEntry.getNewsId(), title, path));
				}

				// select tags by newsId
				statement = connection.prepareStatement(SQL_TAGS_SELECT);
				statement.setLong(1, newsEntry.getNewsId());
				tagResult = statement.executeQuery();

				while (tagResult.next()) {
					if (newsEntry.getTagData() == null) {
						newsEntry.setTagData(new ArrayList<Tag>());
					}
					Long tagId = tagResult.getLong("tag_id");
					String text = tagResult.getString("text");
					Tag tag = new Tag(tagId, text);

					newsEntry.getTagData().add(tag);
				}
				System.out.print("\n");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (imgResult != null)
					imgResult.close();
				if (urlResult != null) {
					urlResult.close();
				}
				if (tagResult != null) {
					tagResult.close();
				}
				newsResult.close();
				statement.close();
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return newsAndImages;
	}

	private String createSQLFromInPlaceholder(String sqlInPlaceholder, int size) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < size; i++) {
			if (i > 0) {
				sb.append(",");
			}
			sb.append("?");
		}
		return sqlInPlaceholder.replace("#", sb.toString());
	}

	@Override
	public void setImagePath(String path) {
		imagePath = path;
	}

}