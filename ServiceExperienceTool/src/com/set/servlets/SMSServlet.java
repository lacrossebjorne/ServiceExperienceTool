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
		System.out.println("action: " + action);
		
		if (action == null) {
			response.setContentType("text/html");
			response.getWriter().println("No action-parameter was set!");
			return;
		} else {
			switch (action) {
			case "sendSMS":
				sendSMS(request, response);
				break;
			default:
				response.sendError(HttpServletResponse.SC_BAD_REQUEST);
				return;
			}
		}
	}

	private void sendSMS(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String number1 = request.getParameter("recipient");
		String message = request.getParameter("message");

		try {
			//sending dummy-sms to avoid credit loss
			SendSMS.sendDummyMessage(URL, number1, message);
		} catch (SMSException | SMSTeknikSenderException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
	}
}