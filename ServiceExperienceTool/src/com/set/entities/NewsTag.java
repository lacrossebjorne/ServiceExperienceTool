package com.set.entities;

public class NewsTag {
	
	Long newsTagId;
	Long newsId;
	Long tagId;
	
	public NewsTag(Long newsTagId, Long newsId, Long tagId) {
		this.newsTagId = newsTagId;
		this.newsId = newsId;
		this.tagId = tagId;
	}
	public Long getNewsTagId() {
		return newsTagId;
	}
	public void setNewsTagId(Long newsTagId) {
		this.newsTagId = newsTagId;
	}
	public Long getNewsId() {
		return newsId;
	}
	public void setNewsId(Long newsId) {
		this.newsId = newsId;
	}
	public Long getTagId() {
		return tagId;
	}
	public void setTagId(Long tagId) {
		this.tagId = tagId;
	}
}
