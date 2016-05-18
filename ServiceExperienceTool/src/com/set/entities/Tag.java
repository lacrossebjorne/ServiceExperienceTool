package com.set.entities;

public class Tag {
	
	private Long tagId;
	private String text;
	
	public Tag(Long tagId, String text) {
		this.tagId = tagId;
		this.text = text;
	}
	
	public Long getTagId() {
		return tagId;
	}
	public void setTagId(Long tagId) {
		this.tagId = tagId;
	}
	public String getText() {
		return text;
	}
	public void setText(String name) {
		this.text = name;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!Tag.class.isAssignableFrom(obj.getClass())) {
			return false;
		}
		
		final Tag other = (Tag)obj;
		if (!isValueEqual(this.tagId, other.tagId)) {
			return false;
		}
		if (!isValueEqual(this.text, other.text)) {
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
