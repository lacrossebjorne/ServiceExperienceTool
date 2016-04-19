package com.set.db;

import java.util.List;

import com.set.data_containers.News;

public interface NewsReader {
	public List<News> getNews(Integer selectedPage, Integer resultsPerPage, Integer offset);
}
