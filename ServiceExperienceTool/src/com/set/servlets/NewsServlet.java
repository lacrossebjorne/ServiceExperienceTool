package com.set.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.set.data_containers.News;
import com.set.db.DAOFactory;
import com.set.db.NewsPublisher;
import com.set.db.NewsReader;

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
	 * action: publishNews; newsHeader: text, newsContent: text
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
			case "publishNews":
				publishNews(request, response);
				break;
			case "fileUpload":
				testFileUpload(request, response);
				break;
			default:
				response.getWriter().println("An invalid value was set to the action-parameter!");
				break;
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * this method is not finished, please do not add modifications to it
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void testFileUpload(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		UploadServlet us = new UploadServlet();

		us.doPost(request, response);

	}

	/**
	 * This method is not finished, please do not add modifications to it
	 * 
	 * @param request
	 * @param response
	 */
	private void publishNews(HttpServletRequest request, HttpServletResponse response) {

		NewsPublisher newsPublisher = DAOFactory.getNewsPublisher();

		String subject = request.getParameter("newsHeader");
		String content = request.getParameter("newsContent");

		int primaryKey = newsPublisher.publishNews(subject, content);
		if (primaryKey > -1) {
			try {
				// after publishing news, get help from additional servlet to
				// post pictures and update references in db
				response.getWriter().println("<h3>NEWS ARE PUBLISHED, NOW IMPLEMENT AJAX!</h3>");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void getNews(HttpServletRequest request, HttpServletResponse response) {

		Integer selectedPage = 1;
		Integer resultsPerPage = 5;

		String type = request.getParameter("type");
		try {
			String selectedPageParameter = request.getParameter("selectedPage");
			String resultsPerPageParameter = request.getParameter("resultsPerPage");
			if (selectedPageParameter != null)
				selectedPage = Integer.parseInt(selectedPageParameter);
			if (resultsPerPageParameter != null)
				resultsPerPage = Integer.parseInt(resultsPerPageParameter);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		// LIMIT = entriesPerPage
		// OFFSET = (selectedPage - 1) * entriesPerPage = (3-1)*5 = 2 *
		// an OFFSET of 10 means start from row 11 of ResultSet)
		// LIMIT means: show up to x entriesPerPage
		Integer offset = (selectedPage - 1) * resultsPerPage;

		List<News> allNews = null;

		PrintWriter out = null;
		try {
			out = response.getWriter();
			NewsReader newsFetcher = DAOFactory.getNewsFetcher();
			allNews = newsFetcher.getNews(selectedPage, resultsPerPage, offset);

			if (allNews != null) {

				if (type == null || type.equals("html")) {
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
				} else if (type.equals("json")) {
					HashMap<String, List<News>> jsonMap = new HashMap<String, List<News>>();
					jsonMap.put("news", allNews);
					response.setContentType("application/json");
					response.getWriter().write(new Gson().toJson(jsonMap));
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}