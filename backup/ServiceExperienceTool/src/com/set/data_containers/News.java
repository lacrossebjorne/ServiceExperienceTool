package com.set.data_containers;

import java.util.List;

public class News {
	private Integer id;
	private String header;
	private String content;
	private String createdAt;
	private List<String> imgUriList;

	public News(Integer id, String header, String content, String createdAt, List<String> imgUriList) {
		this.id = id;
		this.header = header;
		this.content = content;
		this.createdAt = createdAt;
		this.imgUriList = imgUriList;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public List<String> getImgUriList() {
		return imgUriList;
	}

	public void setImgUriList(List<String> imgUriList) {
		this.imgUriList = imgUriList;
	}
}