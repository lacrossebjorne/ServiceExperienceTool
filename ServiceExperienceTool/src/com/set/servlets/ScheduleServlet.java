package com.set.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.GsonBuilder;
import com.set.dao.DAOFactory;
import com.set.dao.EventDAO;
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
		String action = (String) request.getParameter("action");
		
		if(action == null){
			response.getWriter().println("No parameter for action is set!");
			return;
		}else{
			switch(action){
			case "getAllEvents":
				getAllEvents(request, response);
				break;
				default: 
					break;
			}
		}
	}
	protected void getAllEvents(HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		//Hämtar alla event i Databasen. Printar ut det i en JSON-string
		
		DAOFactory daoFactory = DAOFactory.getInstance("setdb.jndi");
		EventDAO eventFetch = daoFactory.getEventDAO();
		List<Event> list = eventFetch.getEventList();
		response.setContentType("application/json");
		response.getWriter().write(new GsonBuilder().setPrettyPrinting().create().toJson(list));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
