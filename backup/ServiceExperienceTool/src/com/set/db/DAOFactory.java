package com.set.db;

public class DAOFactory {
	
	public static NewsPublisherDAO getNewsPublisher() {
		
		return new NewsPublisherDAO();
	}
	
	public static NewsReader getNewsFetcher() {
		
		return new NewsReaderDAO();
	}
}
