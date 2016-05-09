package com.set.entities;


public class Menu {

	private int id;
	private String name;
	
	public Menu(int id, String name){
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
