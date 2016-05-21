package com.set.dao;

import java.util.List;

import com.set.entities.News;
import com.set.entities.Tag;

public interface NewsReaderDAO {
	public List<News> getNews(Integer selectedPage, Integer resultsPerPage, Integer offset, boolean isDisabledEntriesIncluded, boolean isImportantSelected, List<Tag> tags);
	public void setImagePath(String path);
}
