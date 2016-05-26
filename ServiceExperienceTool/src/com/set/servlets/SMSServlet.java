package com.set.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.GsonBuilder;
import com.set.dao.DAOFactory;
import com.set.dao.UserDAO;
import com.set.entities.News;
import com.set.entities.User;
import com.set.sms.SMSException;
import com.set.sms.SMSTeknikSenderException;
import com.set.sms.SendSMS;

public class SMSServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String URL = "https://www.smsteknik.se/webservices/SendSMSws1/httppostws1.aspx";

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String action = request.getParameter("action");

		if (action == null) {
			response.setContentType("text/html");
			response.getWriter().println("No action-parameter was set!");
			return;
		} else {
			switch (action) {
			case "sendSMS":
				sendSMS(request, response);
				break;
			case "listUsers":
				listUsers(request, response);
				break;
			default:
				response.sendError(HttpServletResponse.SC_BAD_REQUEST);
				return;
			}
		}
	}

	private void sendSMS(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String number1 = request.getParameter("numba");
		String message = request.getParameter("msg");

		try {
			SendSMS.sendDummyMessage(URL, number1, message);
		} catch (SMSException | SMSTeknikSenderException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
	}
	
	private void listUsers(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException  {
		
		DAOFactory daoFactory = DAOFactory.getInstance("setdb.jndi");
		
		UserDAO userDAO = daoFactory.getUserDAO();
		
		List<User> users = userDAO.listUsers();
		HashMap<String, List<User>> userMap = new HashMap<>();
//		userMap.put("userMap", users);
		
		response.setContentType("application/json");
		response.getWriter().write(new GsonBuilder().setPrettyPrinting().create().toJson(users));
		
	}
}