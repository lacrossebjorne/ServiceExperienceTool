package com.set.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

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
import com.set.dao.DAOFactory;
import com.set.dao.NewsPublisherDAO;
import com.set.dao.NewsReaderDAO;
import com.set.entities.News;
import com.set.entities.NewsUrl;
import com.set.entities.Tag;
import com.set.servlets.helpers.FilePartProcessor;
import com.set.uploaders.ImageStreamUploader;

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

		/////////////// Printing some Console-info //////////////
		Date currentTime = new Date(System.currentTimeMillis());
		SimpleDateFormat df = new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a");
		System.out.format("################### Time: %s\n", df.format(currentTime));
		System.out.format("Incoming request: %s, action-parameter: %s\n", request.getMethod(), action);
		System.out.format("Server Path: %s\n", getServerRequestPath(request));
		System.out.format("Protocol: %s\n", request.getProtocol());
		System.out.println("Character Encoding: " + request.getCharacterEncoding());
		/////////////////////////////////////////////////////////

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
			case "disableNews":
				disableNews(request, response);
				break;
			case "fileUpload":
				uploadFile(request, response);
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

	public void getNews(HttpServletRequest request, HttpServletResponse response) {

		Integer selectedPage = 1;
		Integer resultsPerPage = 5;
		boolean isDisabledEntriesIncluded = false;
		boolean isImportantSelected = false;

		String type = request.getParameter("type");
		System.out.format("Type: %s\n", type);
		String jsonTags = request.getParameter("tags");
		System.out.format("jsonTags: %s\n", jsonTags);

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
		
		String selectImportant = request.getParameter("selectImportant");
		System.out.println("selectImportant: " + selectImportant);
		if (selectImportant != null && selectImportant.equals("true")) {
			isImportantSelected = true;
		}
		
		List<Tag> tags = parseTagListFromJson(jsonTags);
		
		if (tags != null) {
			System.out.println("Printing tag-info");
			for (Tag tag : tags) {
				System.out.format("tagId: %d, text: %s\n", tag.getTagId(), tag.getText());
			}
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
			DAOFactory daoFactory = DAOFactory.getInstance("setdb.jndi");
			NewsReaderDAO newsFetcher = daoFactory.getNewsReaderDAO();

			/*
			 * Temporary fix - temporaryImagesNewsPath is a constant used only
			 * during development phase
			 */
			try {
				newsFetcher.setImagePath(InitialContext.doLookup("java:comp/env/temporaryImagesNewsPath"));
			} catch (NamingException e) {
				System.out.println(
						"Namingexpeption occured in NewsServlet::getNews(): Now trying to build path from request.");
				newsFetcher.setImagePath(getServerRequestPath(request) + "/images/news/");
			}

			allNews = newsFetcher.getNews(selectedPage, resultsPerPage, offset, isDisabledEntriesIncluded, isImportantSelected, tags);

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
					response.getWriter().write(new GsonBuilder().setPrettyPrinting().create().toJson(jsonMap));
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void publishNews(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		System.out.format("subject: %s, content: %s\n", request.getParameter("newsHeader"),
				request.getParameter("newsContent"));

		String newsHeader = request.getParameter("newsHeader");
		String newsContent = request.getParameter("newsContent");
		Long newsId = null;
		Long importantUntilMillis = null;
		Date importantUntil = null;

		try {
			String idParameter = request.getParameter("newsId");
			String importantUntilParameter = request.getParameter("importantUntil");

			if (idParameter != null) {
				newsId = Long.parseLong(idParameter);
			}
			if (importantUntilParameter != null) {
				importantUntilMillis = Long.parseLong(importantUntilParameter);
			}
			
		} catch (NumberFormatException e) {
			sendError(response, HttpServletResponse.SC_BAD_REQUEST);
		}

		if (newsHeader == null || newsContent == null) {
			sendError(response, HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		
		if (importantUntilMillis != null) {
			importantUntil = new Date(importantUntilMillis);
		}
		

		String jsonUrlList = request.getParameter("urlList");
		System.out.println("urlList: " + jsonUrlList);
		String jsonTags = request.getParameter("tagData");
		System.out.println("tagData: " + jsonTags);

		List<NewsUrl> urlList = parseNewsUrlListFromJson(jsonUrlList);
		List<Tag> tagData = parseTagListFromJson(jsonTags);

		List<String> imageUris = null;

		Hashtable<InputStream, String> inputstreamFilenames = new Hashtable<InputStream, String>();
		Collection<Part> allParts = request.getParts();

		FilePartProcessor filePartProcessor = new FilePartProcessor();
		if (filePartProcessor.processParts(allParts)) {
			inputstreamFilenames = filePartProcessor.getInputstreamTable();
			ImageStreamUploader imageStreamUploader = new ImageStreamUploader();
			if (imageStreamUploader.uploadFiles(inputstreamFilenames)) {
				imageUris = imageStreamUploader.getRecentlyUploadedFileNames();
			} else if (imageStreamUploader.isError()) {
				int errorCode = imageStreamUploader.getErrorCode();
				sendError(response, errorCode);
				return;
			}
		} else if (filePartProcessor.isError()) {
			int errorCode = filePartProcessor.getErrorCode();
			sendError(response, errorCode);
			return;
		}
		
		News newsEntry = new News(newsId, newsHeader, newsContent, null, null, importantUntil, true, null, imageUris, urlList, null, tagData);
		DAOFactory daoFactory = DAOFactory.getInstance("setdb.jndi");

		boolean isPublished = false;

		NewsPublisherDAO newsPublisher = daoFactory.getNewsPublisherDAO();
		isPublished = newsPublisher.publishNews(newsEntry);
		System.out.println("isPublished: " + isPublished);

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

	private void disableNews(HttpServletRequest request, HttpServletResponse response) {

		Long id = null;
		try {
			String idParameter = request.getParameter("newsId");

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
		NewsPublisherDAO newsPublisherDAO = daoFactory.getNewsPublisherDAO();
		boolean isDisabled = newsPublisherDAO.disableNewsEntry(id);

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
	
	private List<Tag> parseTagListFromJson(String jsonTags) {
		if (jsonTags == null) {
			return null;
		}
		
		Gson gson = new Gson();
		Tag[] tagArray = gson.fromJson(jsonTags, Tag[].class);
		if (tagArray != null && tagArray.length > 0) {
			return Arrays.asList(tagArray);
		} else {
			return null;
		}
	}

	private List<NewsUrl> parseNewsUrlListFromJson(String jsonUrlList) {
		if (jsonUrlList == null) {
			return null;
		}

		Gson gson = new Gson();
		NewsUrl[] urlArray = gson.fromJson(jsonUrlList, NewsUrl[].class);
		System.out.println("urlList-length: " + urlArray.length);
		for (int i = 0; i < urlArray.length; i++) {
			System.out.format("newsUrl.getTitle(): %s, newsUrl.getPath(): %s\n", urlArray[i].getTitle(),
					urlArray[i].getPath());
		}

		if (urlArray != null && urlArray.length > 0) {
			return Arrays.asList(urlArray);
		} else {
			return null;
		}
	}

	private void uploadFile(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/uploadServlet");
		dispatcher.forward(request, response);
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
			System.out.format("Sending Response %d: %s\n", errorCode, errorName);
			response.sendError(errorCode);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}