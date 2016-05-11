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
import com.set.dao.RoleDAO;
import com.set.dao.UserDAO;
import com.set.entities.Role;
import com.set.entities.User;
import com.set.helpers.UserDeserializer;


@WebServlet("/userAdminServlet")
public class UserAdminServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if (request.getParameter("getUserList") != null) {
			UserDAO userDAO = getDAOFactory().getUserDAO();
			List<User> userList = userDAO.listUsers();			
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
			RoleDAO roleDAO = getDAOFactory().getRoleDAO();
			List<Role> roleList = roleDAO.listRoles();			
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

		if (request.getParameter("newUser") != null) {
			User user = gson.fromJson(request.getParameter("newUser"), User.class);
			System.out.println(user);
			UserDAO userDAO = getDAOFactory().getUserDAO();
			boolean isValid = userDAO.createUser(user);
			Map<String, Object> map = new HashMap<>();
			map.put("isValid", isValid);
			writeJson(response, map);
		}
		
		if (request.getParameter("newRole") != null) {
			System.out.println(request.getParameter("newRole"));
			Role role = gson.fromJson(request.getParameter("newRole"), Role.class);
			RoleDAO roleDAO = getDAOFactory().getRoleDAO();
			Long roleId = roleDAO.createRole(role);
			boolean isValid = false;
			Map<String, Object> map = new HashMap<>();
			if (roleId != null) {
				isValid = true;
				map.put("roleId", roleId);
			}
			map.put("isValid", isValid);
			writeJson(response, map);
		}
	}
	
	private void writeJson(HttpServletResponse response, Map<String, Object> map) throws IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		try {
			response.getWriter().write(new Gson().toJson(map));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private DAOFactory getDAOFactory() {
		return DAOFactory.getInstance("setdb.jndi");
	}
}
