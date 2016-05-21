package com.set.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class News {
	
	private Long newsId;
	private String header;
	private String content;
	private Date createdAt;
	private Date updatedAt;
	private Date importantUntil;
	private boolean enabled;
	private Set<NewsImage> newsImages = new HashSet<NewsImage>(0);
	private List<String> imgUri = new ArrayList<String>();
	private List<NewsUrl> urlList = new ArrayList<NewsUrl>();
	private Set<NewsTag> newsTags = new HashSet<NewsTag>();
	private List<Tag> tagData = new ArrayList<Tag>();
	private boolean isImportant; //isImportant: this field should not be saved in database and only be set at runtime

	public News() {}
	
	public News(Long newsId, String header, String content, Date createdAt, Date updatedAt, Date importantUntil, boolean enabled,
			Set<NewsImage> newsImages, List<String> imgUri, List<NewsUrl> urlList, Set<NewsTag> newsTags,
			List<Tag> tagData) {
		super();
		this.newsId = newsId;
		this.header = header;
		this.content = content;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.importantUntil = importantUntil;
		this.enabled = enabled;
		this.newsImages = newsImages;
		this.imgUri = imgUri;
		this.urlList = urlList;
		this.newsTags = newsTags;
		this.tagData = tagData;
		this.isImportant = false;
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
	
	public Date getImportantUntil() {
		return this.importantUntil;
	}
	
	public void setImportantUntil(Date importantUntil) {
		this.importantUntil = importantUntil;
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
	
	public List<NewsUrl> getUrlList() {
		return this.urlList;
	}
	
	public void setUrlList(List<NewsUrl> urlList) {
		this.urlList = urlList;
	}

	public Set<NewsTag> getNewsTags() {
		return newsTags;
	}

	public void setNewsTags(Set<NewsTag> newsTags) {
		this.newsTags = newsTags;
	}

	public List<Tag> getTagData() {
		return tagData;
	}

	public void setTagData(List<Tag> tagData) {
		this.tagData = tagData;
	}
	
	public boolean isImportant() {
		return this.isImportant;
	}
	
	public void setIsImportant(boolean isImportant) {
		this.isImportant = isImportant;
	}
}
