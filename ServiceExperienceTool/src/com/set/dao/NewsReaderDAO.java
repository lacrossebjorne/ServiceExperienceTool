package com.set.dao;

import java.util.List;

import com.set.entities.News;

public interface NewsReaderDAO {
	public List<News> getNews(Integer selectedPage, Integer resultsPerPage, Integer offset, boolean isDisabledEntriesIncluded);
	public void setImagePath(String path);
}