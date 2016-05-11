package com.set.dao;

import com.set.entities.News;
import com.set.entities.NewsUrl;

public interface NewsPublisherDAO {

	public boolean publishNews(News news, String[] imgUris, NewsUrl[] newsUrls);
	
}