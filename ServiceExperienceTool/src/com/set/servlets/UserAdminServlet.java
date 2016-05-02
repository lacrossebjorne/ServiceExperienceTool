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
import com.set.dao.DAOFactory;
import com.set.dao.UserDAO;
import com.set.entities.User;

/**
 * Servlet implementation class UserAdminServlet
 */
@WebServlet("/UserAdminServlet")
public class UserAdminServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if (request.getParameter("getUserList") != null) {
			DAOFactory daoFactory = DAOFactory.getInstance("setdb.jndi");
			UserDAO userDAO = daoFactory.getUserDAO();
			List<User> userList = userDAO.listUsers();
			Map<String, Object> map = new HashMap<>();
			map.put("userList", userList);
			writeJson(response, map);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}
	
	private void writeJson(HttpServletResponse response, Map<String, Object> map) {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		try {
			response.getWriter().write(new Gson().toJson(map));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
