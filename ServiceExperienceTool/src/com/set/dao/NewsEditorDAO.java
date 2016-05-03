package com.set.dao;

import com.set.entities.News;

public interface NewsEditorDAO {

	public boolean updateNews(News newsEntry);

	public boolean disableNewsEntry(Long id);
}
