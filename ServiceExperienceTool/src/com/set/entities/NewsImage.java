package com.set.entities;


public class NewsImage implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private NewsImageId id;
	private Image image;
	private News news;

	public NewsImage() {
	}

	public NewsImage(NewsImageId id, Image image, News news) {
		this.id = id;
		this.image = image;
		this.news = news;
	}

	public NewsImageId getId() {
		return this.id;
	}

	public void setId(NewsImageId id) {
		this.id = id;
	}

	public Image getImage() {
		return this.image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public News getNews() {
		return this.news;
	}

	public void setNews(News news) {
		this.news = news;
	}

}
