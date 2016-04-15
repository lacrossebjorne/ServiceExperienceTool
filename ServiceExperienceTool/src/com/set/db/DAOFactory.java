package com.set.db;

public class DAOFactory {
	
	public static NewsFetcher getNewsFetcher() {
		
		NewsDAO newsDAO = new NewsDAO();
		
		return newsDAO;
	}
}
