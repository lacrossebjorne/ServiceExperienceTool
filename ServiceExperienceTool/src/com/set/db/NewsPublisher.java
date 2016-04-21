package com.set.db;

public interface NewsPublisher {
	
	public int publishNews(String subject, String content, String... imgUris);
}
