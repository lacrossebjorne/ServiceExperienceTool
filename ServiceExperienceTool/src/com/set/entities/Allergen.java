package com.set.entities;

public class Allergen {

	private int id;
	private String name;
	
	public Allergen(int id, String name){
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
