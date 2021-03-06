package com.set.servlets.helpers;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.set.dao.DAOFactory;
import com.set.entities.Role;
import com.set.entities.User;

public class UserDeserializer implements JsonDeserializer<User> {

	@Override
	public User deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		
		//Get the submitted roles and create a map with the rolename and status
		JsonArray jsonRolesArray = json.getAsJsonObject().get("roles").getAsJsonArray();
		Type mapType = new TypeToken<Map<String, Boolean>>() {}.getType();
		Map<String, Boolean> rolesMap = new HashMap<>();
		for (JsonElement jsonElement : jsonRolesArray) {
			if(!jsonElement.isJsonNull())
				rolesMap.putAll(new Gson().fromJson(jsonElement, mapType));
		}
		
		//Retreive all roles and compare them to the map. Add the roles with status true to the user
		List<Role> roles = DAOFactory.getInstance("setdb.jndi").getRoleDAO().listRoles();

		List<Role> userRoles = new ArrayList<>();
		for (Role role : roles) {
			if (rolesMap.containsKey(role.getName()) && rolesMap.get(role.getName()) != false) {
				userRoles.add(role);
			}
		}
		
		User user = new Gson().fromJson(json, User.class);
		user.setRoles(userRoles);
		
		return user;
	}
}
