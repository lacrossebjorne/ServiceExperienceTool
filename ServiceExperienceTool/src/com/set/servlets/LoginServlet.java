package com.set.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.set.dao.DAOFactory;
import com.set.entities.User;


/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/loginServlet")
public class LoginServlet extends HttpServlet {
	
	 private static final long serialVersionUID = 1L;
	

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("login.html").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//Gson gson = new Gson();
		
		
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		System.out.println(username + " " + password);

		if (!username.isEmpty() && !password.isEmpty()) {
			User user = DAOFactory.getInstance("setdb.jndi").getUserDAO().find(username, password);
			if (user != null && user.isEnabled()) {
				request.getSession().setAttribute("user", user);
				response.sendRedirect(request.getContextPath() + "/user_admin.html");
				return;
			} else {
				RequestDispatcher rd = getServletContext().getRequestDispatcher("/login.html");
				response.getWriter().println("<span><font color=red>Your login credentials are incorrect, please try again...</span>");
				rd.include(request, response);
			}
		}
		request.getRequestDispatcher("/login.html").forward(request, response);
	}
}