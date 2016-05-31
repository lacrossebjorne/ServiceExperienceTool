package com.set.servlets;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.GsonBuilder;
import com.set.dao.DAOFactory;
import com.set.dao.EventsDAO;
import com.set.entities.Event;

/**
 * Servlet implementation class ScheduleServlet
 */
@WebServlet("/schedule")
public class ScheduleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ScheduleServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String action = request.getParameter("action");
		System.out.println("Action = " + action);
		
		if(action == null){
			response.setContentType("text/html");
			response.getWriter().println("Nooo action parameter was set!");
			return;
		}else{
			switch(action){
			case "getAllEvents":
				getAllEvents(request, response);
				break;
			default:
				response.sendError(HttpServletResponse.SC_BAD_REQUEST);
				return;
			}
		}
	}
	private void getAllEvents(HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		DAOFactory daoFactory = DAOFactory.getInstance("setdb.jndi");
		EventsDAO eventsFetcher = daoFactory.getEventDAO();
		List<Event> event = eventsFetcher.getEventList();
		
		response.setContentType("application/json");
		response.getWriter().write(new GsonBuilder().setPrettyPrinting().create().toJson(event));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}