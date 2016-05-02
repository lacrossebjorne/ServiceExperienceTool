package com.set.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.set.entities.News;

public class NewsReaderDAOJDBC implements NewsReaderDAO {
	
	private final String SQL_NEWS_SELECT = "SELECT news_id, header, content, created_at FROM news ORDER BY created_at DESC, news_id DESC LIMIT ? OFFSET ?";
	private final String SQL_IMAGE_SELECT = "SELECT * FROM image AS i INNER JOIN news_image AS ni ON i.image_id = ni.image_id WHERE ni.news_id=?";
	private DAOFactory daoFactory;
	private String imagePath = "";
	
	public NewsReaderDAOJDBC(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	@SuppressWarnings("resource")
	@Override
	public List<News> getNews(Integer selectedPage, Integer resultsPerPage, Integer offset) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet newsResult = null;
		ResultSet imgResult = null;
		List<News> newsAndImages = null;
		try {
			connection = daoFactory.getConnection();
			statement = connection.prepareStatement(SQL_NEWS_SELECT);
			statement.setInt(1,  resultsPerPage);
			statement.setInt(2,offset);
			newsResult = statement.executeQuery();	
			
			newsAndImages = new LinkedList<>();
			while (newsResult.next()) {
				News newsEntry = new News();
				newsEntry.setNewsId(newsResult.getLong("news_id"));
				newsEntry.setHeader(newsResult.getString("header"));
				newsEntry.setContent(newsResult.getString("content"));
				newsEntry.setCreatedAt(newsResult.getDate("created_at"));
				//newsEntry.setEnabled(newsResult.getBoolean("enabled"));
				
				statement.clearParameters();
				statement = connection.prepareStatement(SQL_IMAGE_SELECT);
				statement.setLong(1, newsEntry.getNewsId());
				imgResult = statement.executeQuery();
				while (imgResult.next()) {
					if (newsEntry.getImgUriList() == null) {
						newsEntry.setImgUriList(new LinkedList<>());
					}
					String uri = imgResult.getString("image_uri");
					System.out.println(uri);
					newsEntry.getImgUriList().add(imagePath + uri);
				}
				newsAndImages.add(newsEntry);
			}
		}catch (SQLException e){
			e.printStackTrace();
		} finally {
			try {
				if (imgResult != null )
					imgResult.close();
				newsResult.close();
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return newsAndImages;
	}

	@Override
	public void setImagePath(String path) {
		imagePath = path;
	}

}
