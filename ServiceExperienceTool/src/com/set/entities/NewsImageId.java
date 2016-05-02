package com.set.entities;

public class NewsImageId {
	
	private int newsId;
	private int imageId;

	public NewsImageId() {
	}

	public NewsImageId(int newsId, int imageId) {
		this.newsId = newsId;
		this.imageId = imageId;
	}

	public int getNewsId() {
		return this.newsId;
	}

	public void setNewsId(int newsId) {
		this.newsId = newsId;
	}

	public int getImageId() {
		return this.imageId;
	}

	public void setImageId(int imageId) {
		this.imageId = imageId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof NewsImageId))
			return false;
		NewsImageId castOther = (NewsImageId) other;

		return (this.getNewsId() == castOther.getNewsId()) && (this.getImageId() == castOther.getImageId());
	}
        
        
	public int hashCode() {
		int result = 17;
		result = 37 * result + this.getNewsId();
		result = 37 * result + this.getImageId();
		return result;
	}

}
