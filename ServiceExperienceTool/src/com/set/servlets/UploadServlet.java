package com.set.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.set.uploaders.FileUploader;
import com.set.uploaders.FileUploaderFactory;

/**
 * This servlet can upload files sent through a http-post request
 * 
 * @author Emil
 *
 */
@MultipartConfig
public class UploadServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter writer = response.getWriter();

		Hashtable<InputStream, String> inputstreamFilenames = new Hashtable<InputStream, String>();
		List<String> imageUris = null;
		Collection<Part> allParts = request.getParts();
		if (allParts != null) {
			Set<InputStream> streams = new HashSet<InputStream>();
			for (Part part : allParts) {
				if (part.getName().equals("file")) {
					streams.add(part.getInputStream());
					inputstreamFilenames.put(part.getInputStream(), part.getSubmittedFileName());
				}
			}

			if (inputstreamFilenames.size() > 0) {

				FileUploader imageUploader = FileUploaderFactory.getGeneralFileUploader();
				boolean filesAreUploaded = imageUploader.uploadFiles(inputstreamFilenames);
				if (filesAreUploaded) {
					imageUris = imageUploader.getRecentlyUploadedFileNames();
					writer.println("These file(s) was successfully uploaded:");
					for (String uri : imageUris) {
						writer.println(uri);
					}
				}
			}
		}
	}
}
