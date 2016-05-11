package com.set.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import com.set.entities.Image;
import com.set.entities.News;
import com.set.entities.NewsUrl;

import static com.set.dao.DAOUtil.*;

public class NewsPublisherDAOJDBC implements NewsPublisherDAO {
	private final String SQL_INSERT_INTO_NEWS = "INSERT INTO news(header, content, created_at) VALUES(?, ?, NOW())";
	private final String SQL_INSERT_INTO_IMAGE = "INSERT INTO image (image_uri) VALUE (?)";
	private final String SQL_INSERT_INTO_NEWS_IMAGE = "INSERT INTO news_image(news_id, image_id) VALUES(?, ?)";
	private final String SQL_INSERT_INTO_NEWS_URL = "INSERT INTO news_url(news_id, title, path) VALUES (?, ?, ?)";
	private DAOFactory daoFactory;

	public NewsPublisherDAOJDBC(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@SuppressWarnings("resource")
	@Override
	public boolean publishNews(News news, String[] imgUris, NewsUrl[] newsUrls) {
		boolean published = false;
		Object[] newsObj = { news.getHeader(), news.getContent() };
		Connection connection = null;
		PreparedStatement publishPS = null;
		ResultSet rsNewsKeys = null;
		ResultSet rsImageKeys = null;
		Long newsID = null;
		int[] batchResult = null;
		try {
			connection = daoFactory.getConnection();
			connection.setAutoCommit(false);
			
			//Insert news article in news-table and retreive news PK
			publishPS = prepareStatement(connection, SQL_INSERT_INTO_NEWS, true, newsObj);
			if (publishPS.executeUpdate() == 0) {
				throw new SQLException("Couldn't insert rows into table news");
			}
			rsNewsKeys = publishPS.getGeneratedKeys();
			if (rsNewsKeys.next())
				newsID = rsNewsKeys.getLong(1);
			news.setNewsId(newsID);
			publishPS.clearParameters();
			
			//Insert image urls in Image-table and retrieve image PK
			if (imgUris != null && imgUris.length > 0) {
				Set<Long> imageKeySet = new HashSet<Long>();
				for (String uri : imgUris) {
					publishPS = prepareStatement(connection, SQL_INSERT_INTO_IMAGE, true, (Object) uri);
					if (publishPS.executeUpdate() == 0)
						throw new SQLException("Couldn't insert image into table image");
					rsImageKeys = publishPS.getGeneratedKeys();
					int i = 0;
					while (rsImageKeys.next()) {
						Image img = new Image();
						img.setImageId(rsImageKeys.getLong(1));
						img.setImageUri(imgUris[i]);
						imageKeySet.add(img.getImageId());
						i++;
					}
					publishPS.clearParameters();
				}
				
				//Insert news-PK and image-PK into news_image-table. 
				publishPS = connection.prepareStatement(SQL_INSERT_INTO_NEWS_IMAGE);
				for (Long imagePrimaryKey : imageKeySet) {
					publishPS.setLong(1, newsID);
					publishPS.setLong(2, imagePrimaryKey);
					publishPS.addBatch();
				}
				batchResult = publishPS.executeBatch();
				if (!isBatchSuccessful(batchResult)) {
					throw new SQLException("Couldn't insert image into table news_image");
				}
			}
			
			//Insert urls into newsUrl-table
			if (newsUrls != null && newsUrls.length > 0) {
				publishPS = connection.prepareStatement(SQL_INSERT_INTO_NEWS_URL);
				for (NewsUrl newsUrl : newsUrls) {
					publishPS.setLong(1, newsID);
					publishPS.setString(2, newsUrl.getTitle());
					publishPS.setString(3, newsUrl.getPath());
					publishPS.addBatch();
				}
				batchResult = publishPS.executeBatch();
				if (!isBatchSuccessful(batchResult)) {
					throw new SQLException("Couldn't insert into table newsUrl");
				}
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
				if (rsImageKeys != null)
					rsImageKeys.close();
				rsNewsKeys.close();
				publishPS.close();
				connection.setAutoCommit(true);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return published;
	}
	
	public boolean isBatchSuccessful(int[] updateCounts){
		for (int i = 0; i < updateCounts.length; i++) {
			if (updateCounts[i] <= 0) {
				return false;
			}
		}
		return true;
	}
}