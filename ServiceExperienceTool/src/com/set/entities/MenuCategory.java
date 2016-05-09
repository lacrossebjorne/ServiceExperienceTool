package com.set.entities;

public class MenuCategory {

	private int id;
	private String name;
	
	public MenuCategory(int id, String name){
		this.id = id;
		this.name = name;
	}
	
	public int getId(){
		return this.id;
	}
	public String getName(){
		return this.name;
	}
	
}
