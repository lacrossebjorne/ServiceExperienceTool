package com.set.entities;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class News {
	
	private Long newsId;
	private String header;
	private String content;
	private Date createdAt;
	private Date updatedAt;
	private boolean enabled;
	private Set<NewsImage> newsImages = new HashSet<NewsImage>(0);
	private List<String> imgUri = new LinkedList<>();
	private List<NewsUrl> newsUrls = new LinkedList<>();

	public News() {
	}

	public News(String header, String content, boolean enabled) {
		this.header = header;
		this.content = content;
		this.enabled = enabled;
	}

	
	
	public News(String header, String content, Date createdAt, Date updatedAt, boolean enabled,
			Set<NewsImage> newsImages, List<NewsUrl> newsUrls) {
		this.header = header;
		this.content = content;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.enabled = enabled;
		this.newsImages = newsImages;
		this.newsUrls = newsUrls;
	}
	
	public Long getNewsId() {
		return this.newsId;
	}

	public void setNewsId(Long newsId) {
		this.newsId = newsId;
	}

	public String getHeader() {
		return this.header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public boolean isEnabled() {
		return this.enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public Set<NewsImage> getNewsImages() {
		return this.newsImages;
	}

	public void setNewsImages(Set<NewsImage> newsImages) {
		this.newsImages = newsImages;
	}
	
	public List<String> getImgUriList() {
		return imgUri;
	}
	public void setImgUriList(List<String> imgUri) {
		this.imgUri = imgUri;
	}
	
	public List<NewsUrl> getNewsUrls() {
		return this.newsUrls;
	}
	
	public void setNewsUrls(List<NewsUrl> newsUrls) {
		this.newsUrls = newsUrls;
	}
}
