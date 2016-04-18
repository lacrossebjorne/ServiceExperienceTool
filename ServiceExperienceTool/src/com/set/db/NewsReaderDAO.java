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

	@Override
	public List<News> getNews(Integer selectedPage, Integer resultsPerPage, Integer offset) {

		List<News> allNews = null;
		
		try {
			connection = getConnection();

			String newsQry = "SELECT news_id, header, content, created_at FROM news ORDER BY created_at DESC, news_id DESC LIMIT ? OFFSET ?";
			PreparedStatement newsPs = connection.prepareStatement(newsQry);
			newsPs.setInt(1, resultsPerPage);
			newsPs.setInt(2, offset);
			ResultSet newsRs = newsPs.executeQuery();

			String imgQry = "SELECT * FROM image AS i INNER JOIN news_image AS ni ON i.image_id = ni.image_id WHERE ni.news_id=?";
			PreparedStatement imgPs = connection.prepareStatement(imgQry);

			allNews = new LinkedList<News>();
			while (newsRs.next()) {
				Integer id = newsRs.getInt("news_id");
				String header = newsRs.getString("header");
				String content = newsRs.getString("content");
				Date createdAt = newsRs.getDate("created_at");

				News newsEntry = new News(id, header, content, createdAt.toString(), null);
				allNews.add(newsEntry);

				imgPs.setInt(1, id);
				ResultSet imgRs = imgPs.executeQuery();

				while (imgRs.next()) {
					if (newsEntry.getImgUriList() == null)
						newsEntry.setImgUriList(new LinkedList<String>());
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
