package com.set.db;

import java.util.List;

import javax.servlet.ServletRequest;

import com.set.data_containers.News;

public interface NewsFetcher {
	public List<News> getNews(ServletRequest request);
}
