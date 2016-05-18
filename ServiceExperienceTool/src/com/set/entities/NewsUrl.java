package com.set.entities;

import java.util.HashMap;

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

	@Override
	public String toString() {
		return "NewsUrl [newsUrlId=" + newsUrlId + ", newsId=" + newsId + ", title=" + title + ", path=" + path + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!NewsUrl.class.isAssignableFrom(obj.getClass())) {
			return false;
		}
		
		final NewsUrl other = (NewsUrl) obj;
		if (!isValueEqual(this.newsId, other.newsId)) {
			return false;
		}
		if (!isValueEqual(this.newsUrlId, other.newsUrlId)) {
			return false;
		}
		if (!isValueEqual(this.path, other.path)) {
			return false;
		}
		if (!isValueEqual(this.title, other.title)) {
			return false;
		}

		return true;
	}
	
	private boolean isValueEqual(Object obj1, Object obj2) {
		if ((obj1 == null) ? (obj2 != null) : !obj1.equals(obj2)) {
			return false;
		}
		return true;
	}
}
