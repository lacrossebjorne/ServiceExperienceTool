package com.set.dao;

import com.set.entities.News;

public interface NewsPublisherDAO {

	public boolean publishNews(News news);
	public boolean disableNewsEntry(Long id);
}
