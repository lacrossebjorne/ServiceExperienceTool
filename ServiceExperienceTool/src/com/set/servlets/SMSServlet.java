package com.set.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.set.dao.DAOFactory;
import com.set.dao.UserDAO;
import com.set.entities.News;
import com.set.entities.User;
import com.set.entities.SmsWrapper;
import com.set.entities.Tag;
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
		Gson gson = new Gson();
		SmsWrapper smsWrapper = gson.fromJson(request.getParameter("smsWrapper"), SmsWrapper.class);
		String[] numbers = smsWrapper.getRecipients();
		String message = smsWrapper.getMessage();
		
		// TODO
		// Check for available credits before sending SMS
		// int requiredCredits = numbers.length();
		
		try {
			//sending dummy-sms to avoid credit loss
			for (String number : numbers)
			SendSMS.sendDummyMessage(URL, number, message);
		} catch (SMSException | SMSTeknikSenderException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
	}
}