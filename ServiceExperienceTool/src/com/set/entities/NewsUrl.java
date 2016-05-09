package com.set.entities;

public class NewsUrl implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Long newsUrlId;
	private Long newsId;
	private String title;
	private String path;
	
	public NewsUrl(String title, String path) {
		this.title = title;
		this.path = path;
	}
	
	public NewsUrl(Long newsUrlId, Long newsId, String title, String path) {
		this(title, path);
		this.newsUrlId = newsUrlId;
		this.newsId = newsId;
	}

	public Long getNewsUrlId() {
		return newsUrlId;
	}

	public void setNewsUrlId(Long newsUrlId) {
		this.newsUrlId = newsUrlId;
	}

	public Long getNewsId() {
		return newsId;
	}

	public void setNewsId(Long newsId) {
		this.newsId = newsId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
