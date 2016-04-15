package com.set.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.set.data_containers.News;
import com.set.db.DAOFactory;
import com.set.db.NewsFetcher;

/**
 * Servlet implementation class NewsServlet
 */
public class NewsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public void init() throws ServletException {
		super.init();
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			super.service(request, response);
		} catch (Exception e) {
			response.getWriter().println("Database error!");
			e.printStackTrace();
		}

	}
	
	/**
	 * Parameters to put in request:<br>
	 * action: getNews; resultsPerPage: number, selectedPage: number.<br>
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");

		String action = (String) request.getParameter("action");
		System.out.println("Incoming request: " + request.getMethod() + ", parameter-name action=" + action);
		if (action == null) {
			response.getWriter().println("No action-parameter was set!");
			return;
		} else {
			switch (action) {
			case "getNews":
				getNews(request, response);
				break;
			default:
				response.getWriter().println("An invalid value was set to the action-parameter!");
				break;
			}
		}
	}
	
	public void getNews(HttpServletRequest request, HttpServletResponse response) {
		
		List<News> allNews = null;

		PrintWriter out = null;
		try {
			out = response.getWriter();
			
			NewsFetcher newsFetcher = DAOFactory.getNewsFetcher();
			allNews = newsFetcher.getNews(request);
			
			if (allNews != null) {
				for (News news : allNews) {
					out.println("<h1>" + news.getHeader() + "</h1>");
					out.println("- " + news.getCreatedAt());
					out.println("<br />");
					out.println(news.getContent());
					out.println("<br />");
					if (news.getImgUriList() != null) {
						for (String uri : news.getImgUriList()) {
							out.format("<img src='images/news/%s' width='150' height='150' />", uri);
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}