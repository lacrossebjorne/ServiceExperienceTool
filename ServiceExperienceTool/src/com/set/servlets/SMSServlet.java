package com.set.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.set.dao.RecipientError;
import com.set.entities.SmsWrapper;
import com.set.sms.SMSSender;

public class SMSServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private enum SendSMSStatus {
		SUCCESSFUL, COMPLETED_WITH_ERRORS, UNSUCCESSFUL, NOT_ENOUGH_CREDITS
	};

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
			case "checkCredits":
				checkCredits(response);
				break;
			case "sendSMS":
				sendSMS(request, response);
				break;
			default:
				response.sendError(HttpServletResponse.SC_BAD_REQUEST);
				return;
			}
		}
	}
	
	/**
	 * Checks the amount of SMS-credits left
	 * @param response response a response with data describing the result in JSON
	 * @throws IOException
	 */
	private void checkCredits(HttpServletResponse response) throws IOException {
		Integer creditsLeft = SMSSender.checkCredits();
		Map<String, Object> map = new HashMap<String, Object>();
		boolean isValid = true;
		if (creditsLeft != null) {
				map.put("creditsLeft", creditsLeft);

		} else {
			isValid = false;
		}
		
		map.put("isValid", isValid);
		writeJson(response, map);
	}
	
	/**
	 * Processes the request to send an SMS
	 * @param request a request containing message and recipients wrapped in a SmsWrapper-object
	 * @param response a response with data describing the result in JSON
	 * @throws IOException
	 * @throws ServletException
	 */
	private void sendSMS(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		Gson gson = new Gson();
		SmsWrapper smsWrapper = gson.fromJson(request.getParameter("smsWrapper"), SmsWrapper.class);
		String[] numbers = smsWrapper.getRecipients();
		String message = smsWrapper.getMessage();
		Map<String, Object> map = new HashMap<String, Object>();
		List<RecipientError> errors = new ArrayList<RecipientError>();
		int successfulSendAttempts = 0;
		int totalSendAttempts = 0;
		
		try {
			// Check for available credits before sending SMS
			Integer creditsLeft = SMSSender.checkCredits();
			
			if (creditsLeft != null && creditsLeft >= numbers.length) {
				//loop through recipients and send SMS to every recipient
				for (String number : numbers) {
					String status = SMSSender.send(number, message);
					Integer id = null;
					
					try {
						String smsid = SMSSender.extractSMSIDFromStatus(status);
						id = Integer.parseInt(smsid);
					} catch (NumberFormatException | NullPointerException e) {
						//if smsid cannot be parsed, it means that it's null or not a digit
						//then the status-message should be used to create an instance of RecipientError
						//then store it in a list
						errors.add(SMSSender.createRecipientError(status, number));
					}
					
					if (id != null) {
						if (id > 0) {
							successfulSendAttempts++;
						}
					}
					
					totalSendAttempts++;
				}
				
				if (totalSendAttempts == successfulSendAttempts) {
					map.put("statusCode", SendSMSStatus.SUCCESSFUL);
				} else {
					if (successfulSendAttempts > 0) {
						map.put("statusCode", SendSMSStatus.COMPLETED_WITH_ERRORS);
					} else {
						map.put("statusCode", SendSMSStatus.UNSUCCESSFUL);
					}
					
					map.put("errors", errors);
				}
			} else if (creditsLeft != null && creditsLeft < numbers.length){
				System.out.println("Not enough credits");
				map.put("statusCode", SendSMSStatus.NOT_ENOUGH_CREDITS);
			} else {
				map.put("statusCode", SendSMSStatus.UNSUCCESSFUL);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		} 
		
		map.put("isValid", true);
		map.put("totalSendAttempts", totalSendAttempts);
		map.put("successfulSendAttempts", successfulSendAttempts);
		writeJson(response, map);
	}
	
	/**
	 * Writes a specific Map-instance to the HttpServletResponse as JSON
	 * @param response a response with data describing the result in JSON
	 * @param map the Map<String, Object> to write to JSON
	 * @throws IOException
	 */
	public void writeJson(HttpServletResponse response, Map<String, Object> map) throws IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String jsonOutput = gson.toJson(map);
		System.out.println(jsonOutput);
		response.getWriter().write(jsonOutput);
	}
}