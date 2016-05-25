package com.set.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.set.dao.DAOFactory;
import com.set.entities.Role;
import com.set.entities.User;
import com.set.servlets.helpers.UserDeserializer;


@WebServlet("/userAdminServlet")
public class UserAdminServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if (request.getParameter("getUserList") != null) {
			List<User> userList = getDAOFactory().getUserDAO().listUsers();
			Map<String, Object> map = new HashMap<>();
			boolean isValid = false;
			if (!userList.isEmpty()) {
				isValid = true;
				map.put("userList", userList);
			}
			map.put("isValid", isValid);
			writeJson(response, map);
		}
		
		if (request.getParameter("getRolesList") != null) {
			List<Role> roleList = getDAOFactory().getRoleDAO().listRoles();
			Map<String, Object> map = new HashMap<>();
			boolean isValid = false;
			if (!roleList.isEmpty()) {
				isValid = true;
				map.put("roleList", roleList);
			}
			map.put("isValid", isValid);
			writeJson(response, map);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		final GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(User.class, new UserDeserializer());
		final Gson gson = builder.create();

		if (request.getParameter("insertUser") != null) {
			User user = getDAOFactory().getUserDAO().createUser(gson.fromJson(request.getParameter("insertUser"), User.class));
			Map<String, Object> map = new HashMap<>();
			boolean isValid = false;
			if(user != null)
				map.put("user", user);
			map.put("isValid", isValid);
			writeJson(response, map);
		}
		
		if (request.getParameter("insertRole") != null) {
			Role role = getDAOFactory().getRoleDAO().createRole(gson.fromJson(request.getParameter("insertRole"), Role.class));
			boolean isValid = false;
			Map<String, Object> map = new HashMap<>();
			if (role != null) {
				isValid = true;
				map.put("role", role);
			}
			map.put("isValid", isValid);
			writeJson(response, map);
		}
	}
	
	private void writeJson(HttpServletResponse response, Map<String, Object> map) throws IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		try {
			System.out.println(new Gson().toJson(map));
			response.getWriter().write(new Gson().toJson(map));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private DAOFactory getDAOFactory() {
		return DAOFactory.getInstance("setdb.jndi");
	}
}
