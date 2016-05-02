package com.set.entities;

import java.util.HashSet;
import java.util.Set;

public class Image {

	private Long imageId;
	private String imageUri;
	private Set<NewsImage> newsImages = new HashSet<NewsImage>(0);

	public Image() {
	}

	public Image(String imageUri) {
		this.imageUri = imageUri;
	}

	public Image(String imageUri, Set<NewsImage> newsImages) {
		this.imageUri = imageUri;
		this.newsImages = newsImages;
	}

	public Long getImageId() {
		return this.imageId;
	}

	public void setImageId(Long imageId) {
		this.imageId = imageId;
	}

	public String getImageUri() {
		return this.imageUri;
	}

	public void setImageUri(String imageUri) {
		this.imageUri = imageUri;
	}

	public Set<NewsImage> getNewsImages() {
		return this.newsImages;
	}

	public void setNewsImages(Set<NewsImage> newsImages) {
		this.newsImages = newsImages;
	}

}
