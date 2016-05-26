package com.set.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.set.sms.SMSException;
import com.set.sms.SMSTeknikSenderException;
import com.set.sms.SendSMS;


public class SMSServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
    public SMSServlet() {
        super();
        
    }
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String url = "https://www.smsteknik.se/webservices/SendSMSws1/httppostws1.aspx";
		String number1 = request.getParameter("numba");
		String message = request.getParameter("msg");
		
		try {
			SendSMS.sendDummyMessage(url, number1, message);
		} catch (SMSException e) {
			e.printStackTrace();
		} catch (SMSTeknikSenderException e) {
			e.printStackTrace();
		}
	}	
}