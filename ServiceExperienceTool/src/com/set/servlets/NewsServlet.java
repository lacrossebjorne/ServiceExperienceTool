package com.set.servlets;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.set.dao.DAOFactory;
import com.set.dao.NewsEditorDAO;
import com.set.dao.NewsPublisherDAO;
import com.set.dao.NewsReaderDAO;
import com.set.entities.News;
import com.set.entities.NewsUrl;
//import com.set.data_containers.News;
//import com.set.db.DAOFactory;
//import com.set.db.NewsPublisher;
//import com.set.db.NewsReader;
import com.set.uploaders.FileUploader;
import com.set.uploaders.FileUploaderFactory;

/**
 * Servlet implementation class NewsServlet
 */

@MultipartConfig
public class NewsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Map<Integer, String> errorMap = new HashMap<Integer, String>();

	@Override
	public void init() throws ServletException {
		errorMap.put(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error");
		errorMap.put(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, "Unsupported Media Type");
		errorMap.put(HttpServletResponse.SC_BAD_REQUEST, "Bad Request");
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

		//////////////////////////// Printing some Console-info
		//////////////////////////// ////////////////////////////
		Date currentTime = new Date(System.currentTimeMillis());
		SimpleDateFormat df = new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a");
		System.out.format("################### Time: %s\n", df.format(currentTime));
		System.out.format("Incoming request: %s, action-parameter: %s\n", request.getMethod(), action);
		System.out.format("Server Path: %s\n", getServerRequestPath(request));
		System.out.format("Protocol: %s\n", request.getProtocol());
		////////////////////////////////////////////////////////////////////////////////////

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
				uploadFile(request, response);
				break;
			case "updateNews":
				updateNews(request, response);
				break;
			case "disableNews":
				disableNews(request, response);
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

	private void uploadFile(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/uploadServlet");
		dispatcher.forward(request, response);
	}

	private void publishNews(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		System.out.format("subject: %s, content: %s\n", request.getParameter("newsHeader"),
				request.getParameter("newsContent"));
		String jsonUrlList = request.getParameter("urlList");

		Gson gson = new Gson();
		NewsUrl[] urlList = gson.fromJson(jsonUrlList, NewsUrl[].class);
		System.out.println("urlList-length: " + urlList.length);
		for (int i = 0; i < urlList.length; i++) {
			System.out.format("newsUrl.getTitle(): %s, newsUrl.getPath(): %s\n", urlList[i].getTitle(), urlList[i].getPath());
		}

		Hashtable<InputStream, String> inputstreamFilenames = new Hashtable<InputStream, String>();
		String[] imageUris = null;
		Collection<Part> allParts = request.getParts();
		if (allParts != null) {
			Set<InputStream> streams = new HashSet<InputStream>();
			for (Part part : allParts) {
				if (part.getName().equals("file")) {
					InputStream inputStream = part.getInputStream();
					String fileName = part.getSubmittedFileName();
					if (fileName == null) {
						sendError(response, HttpServletResponse.SC_BAD_REQUEST);
						return;
					}
					streams.add(part.getInputStream());
					inputstreamFilenames.put(inputStream, fileName);
				}
			}
			if (inputstreamFilenames.size() > 0) {

				FileUploader imageUploader = FileUploaderFactory.getNewsImageUploader();
				boolean filesAreUploaded = imageUploader.uploadFiles(inputstreamFilenames);
				if (filesAreUploaded) {
					imageUris = imageUploader.getRecentlyUploadedFileNames();
				} else {
					sendError(response, HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
					return;
				}
			}
		}

		/*
		 * NewsPublisher newsPublisher = DAOFactory.getNewsPublisher();
		 * 
		 * String subject = request.getParameter("newsHeader"); String content =
		 * request.getParameter("newsContent");
		 * 
		 * int primaryKey = newsPublisher.publishNews(subject, content,
		 * imageUris);
		 */

		String subject = request.getParameter("newsHeader");
		String content = request.getParameter("newsContent");
		boolean enabled = false;
		if (request.getParameter("status") != null)
			enabled = true;
		News news = new News(subject, content, enabled);
		DAOFactory daoFactory = DAOFactory.getInstance("setdb.jndi");
		NewsPublisherDAO newsPublisher = daoFactory.getNewsPublisherDAO();
		boolean isPublished = newsPublisher.publishNews(news, imageUris, urlList);
		System.out.println("isPublished: " + isPublished);
		// if (primaryKey > -1) {
		if (isPublished) {
			try {
				response.getWriter().println("News are published!");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}
	}

	public void getNews(HttpServletRequest request, HttpServletResponse response) {

		Integer selectedPage = 1;
		Integer resultsPerPage = 5;
		Boolean isDisabledEntriesIncluded = false;

		String type = request.getParameter("type");
		System.out.format("Type: %s\n", type);

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

		String showDisabled = request.getParameter("showDisabled");
		System.out.println("showDisabled: " + showDisabled);
		if (showDisabled != null && showDisabled.equals("true")) {
			isDisabledEntriesIncluded = true;
		}

		System.out.println("selectedPage " + selectedPage);
		System.out.println("resultsPerPage " + resultsPerPage);
		// LIMIT = entriesPerPage
		// OFFSET = (selectedPage - 1) * entriesPerPage = (3-1)*5 = 2 *
		// an OFFSET of 10 means start from row 11 of ResultSet)
		// LIMIT means: show up to x entriesPerPage
		Integer offset = (selectedPage - 1) * resultsPerPage;

		List<News> allNews = null;

		PrintWriter out = null;
		try {
			out = response.getWriter();
			// NewsReader newsFetcher = DAOFactory.getNewsFetcher();
			DAOFactory daoFactory = DAOFactory.getInstance("setdb.jndi");
			NewsReaderDAO newsFetcher = daoFactory.getNewsReaderDAO();

			// temporary fix - temporaryImagesNewsPath is a constant used only
			// during development phase
			try {
				newsFetcher.setImagePath(InitialContext.doLookup("java:comp/env/temporaryImagesNewsPath"));
			} catch (NamingException e) {
				System.out.println(
						"Namingexpeption occured in NewsServlet::getNews(): Now trying to build path from request.");
				newsFetcher.setImagePath(getServerRequestPath(request) + "/images/news/");
			}

			allNews = newsFetcher.getNews(selectedPage, resultsPerPage, offset, isDisabledEntriesIncluded);

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
								out.format("<img src=\"%s\" width=\"150\" height=\"150\" />", uri);
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

	private void disableNews(HttpServletRequest request, HttpServletResponse response) {
		
		Long id = null;
		try {
			String idParameter = request.getParameter("id");

			if (idParameter != null) {
				id = Long.parseLong(idParameter);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		if (id == null) {
			sendError(response, HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		System.out.println("id: " + id);

		DAOFactory daoFactory = DAOFactory.getInstance("setdb.jndi");
		NewsEditorDAO newsEditorDAO = daoFactory.getNewsEditorDAO();
		boolean isDisabled = newsEditorDAO.disableNewsEntry(id);

		if (isDisabled) {
			try {
				response.getWriter().println("News entry is disabled!");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}

	}

	private void updateNews(HttpServletRequest request, HttpServletResponse response) {

		Long id = null;
		String header = request.getParameter("header");
		String content = request.getParameter("content");

		try {
			String idParameter = request.getParameter("id");

			if (idParameter != null) {
				id = Long.parseLong(idParameter);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		if (id == null || header == null || content == null) {
			sendError(response, HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		System.out.println("id: " + id);
		System.out.println("header: " + header);
		System.out.println("content: " + content);

		DAOFactory daoFactory = DAOFactory.getInstance("setdb.jndi");
		NewsEditorDAO newsEditorDAO = daoFactory.getNewsEditorDAO();

		News newsEntry = new News(header, content, null, null, true, null, null);
		newsEntry.setNewsId(id);

		boolean isPublished = newsEditorDAO.updateNews(newsEntry);

		if (isPublished) {
			try {
				response.getWriter().println("News are published!");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}

	}

	private String getServerRequestPath(HttpServletRequest request) {
		String protocol = "";
		// todo add support for more protocols
		if (request.getProtocol().equals("HTTP/1.1")) {
			protocol = "http://";
		}
		String server = request.getServerName();
		String port = Integer.toString(request.getServerPort());
		String contextPath = request.getContextPath();

		String fullPath = protocol + server + ":" + port + contextPath;
		System.out.println(fullPath);
		return fullPath;
	}

	public void sendError(HttpServletResponse response, Integer errorCode) {
		String errorName = this.errorMap.get(errorCode);
		try {
			System.out.format("Sending Response %d: %s", errorCode, errorName);
			response.sendError(errorCode);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}