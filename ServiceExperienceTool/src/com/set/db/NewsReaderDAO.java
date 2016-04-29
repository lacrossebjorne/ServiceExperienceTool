package com.set.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.naming.NamingException;

import com.set.data_containers.News;

public class NewsReaderDAO extends AbstractDAO implements NewsReader {

	private final String SQL_NEWS_SELECT = "SELECT news_id, header, content, created_at FROM news ORDER BY created_at DESC, news_id DESC LIMIT ? OFFSET ?";
	private final String SQL_IMAGE_SELECT = "SELECT * FROM image AS i INNER JOIN news_image AS ni ON i.image_id = ni.image_id WHERE ni.news_id=?";

	@Override
	public List<News> getNews(Integer selectedPage, Integer resultsPerPage, Integer offset) {

		List<News> allNews = null;

		try {
			connection = getConnection();

			PreparedStatement newsPs = connection.prepareStatement(SQL_NEWS_SELECT);
			newsPs.setInt(1, resultsPerPage);
			newsPs.setInt(2, offset);
			ResultSet newsRs = newsPs.executeQuery();

			PreparedStatement imgPs = connection.prepareStatement(SQL_IMAGE_SELECT);

			allNews = new LinkedList<News>();
			while (newsRs.next()) {
				Integer id = newsRs.getInt("news_id");
				String header = newsRs.getString("header");
				String content = newsRs.getString("content");
				Date createdAt = newsRs.getDate("created_at");

				News newsEntry = new News(id, header, content, createdAt.toString(), new LinkedList<String>());
				allNews.add(newsEntry);

				imgPs.setInt(1, id);
				ResultSet imgRs = imgPs.executeQuery();

				while (imgRs.next()) {
					String uri = imgRs.getString("image_uri");
					newsEntry.getImgUriList().add(uri);
				}
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

		return allNews;
	}
}
