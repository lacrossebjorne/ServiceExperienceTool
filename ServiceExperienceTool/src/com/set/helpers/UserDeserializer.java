package com.set.helpers;

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
import com.set.dao.RoleDAO;
import com.set.entities.Role;
import com.set.entities.User;

public class UserDeserializer implements JsonDeserializer<User> {
	{

	}

	@Override
	public User deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {

		JsonArray jsonRolesArray = json.getAsJsonObject().get("roles").getAsJsonArray();
		Type mapType = new TypeToken<Map<String, Boolean>>() {}.getType();
		Map<String, Boolean> rolesMap = new HashMap<>();
		for (JsonElement jsonElement : jsonRolesArray) {
			rolesMap.putAll(new Gson().fromJson(jsonElement, mapType));
		}
		
		DAOFactory daoFactory = DAOFactory.getInstance("setdb.jndi");
		RoleDAO roleDAO = daoFactory.getRoleDAO();
		List<Role> roles = roleDAO.listRoles();
		
		List<Role> userRoles = new ArrayList<>();
		for (Role role : roles) {
			if (rolesMap.containsKey((role.getName()))) {
				userRoles.add(role);
			}
		}
		
		User user = new Gson().fromJson(json, User.class);
		user.setRoles(userRoles);
		
		return user;
	}
}
