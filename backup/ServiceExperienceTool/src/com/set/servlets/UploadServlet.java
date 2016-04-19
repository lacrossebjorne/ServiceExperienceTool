package com.set.servlets;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

public class UploadServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private static final String UPLOAD_DIRECTORY = "images/news";
	
	private static final int MEMORY_TRESHHOLD = 1024 * 1024 * 3; //3mb
	private static final int MAX_FILE_SIZE = 1024 * 1024 * 40; 
	private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 50;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if (!ServletFileUpload.isMultipartContent(request)) {
			PrintWriter writer = response.getWriter();
			writer.println("Error uploading files!");
			//writer.flush();
			return;
		}
		
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(MEMORY_TRESHHOLD);
		factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
		ServletFileUpload servletFileUpload = new ServletFileUpload(factory);
		
		
		
	}
}
