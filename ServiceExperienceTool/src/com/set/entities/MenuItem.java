package com.set.entities;

import java.util.List;

public class MenuItem {
	private int id;
	private String name;
	private String description;
	private String details;
	private int category;
	private List<Allergen> allergens;
	
	public MenuItem(int id, String name, String description, String details, 
			int category, List<Allergen> allergens){
		this.id = id;
		this.name = name;
		this.description = description;
		this.details = details;
		this.category = category;
		this.allergens = allergens;
	}
	
	public int getId(){
		return this.id;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getDescription(){
		return this.description;
	}
	
	public String getDetails(){
		return this.details;
	}
	
	public int getCategory(){
		return this.category;
	}
	
	public List<Allergen> getAllergens(){
		return this.allergens;
	}
}

