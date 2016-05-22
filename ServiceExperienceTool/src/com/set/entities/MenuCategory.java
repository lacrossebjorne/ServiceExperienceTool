package com.set.entities;

public class MenuCategory {

	private int id;
	private String name;
	private int parent;
	
	public MenuCategory(int id, String name, int parent){
		this.id = id;
		this.name = name;
		this.parent = parent;
	}
	
	public int getId(){
		return this.id;
	}
	public String getName(){
		return this.name;
	}
	public int getParent(){
		return this.parent;
	}
}
