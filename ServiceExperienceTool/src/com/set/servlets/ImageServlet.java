package com.set.servlets;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * takes an image-request and looks for it on the server. if the file exists and
 * are of mime-type: image/*, then it's sent to the OutputStream of HttpResponse
 * If something fails then a 404-error is sent as response.
 * 
 * @author Emil
 *
 */
public class ImageServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private String imagePath;

	@Override
	public void init() {
		try {
			//looks up the path-name from context.xml
			imagePath = InitialContext.doLookup("java:comp/env/imagesPath");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// gets path after, for example /news/coolkitten.jpg
		String imageName = request.getPathInfo();
		if (imageName == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		File imageFile = new File(imagePath, imageName);

		// check if file exists or not
		if (!imageFile.exists()) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		// check if the file is actually an image
		String contentType = getServletContext().getMimeType(imageFile.getName());
		if (contentType == null || !contentType.startsWith("image")) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		// prepare the response
		response.reset();
		response.setContentType(contentType);
		response.setHeader("Content-Length", String.valueOf(imageFile.length()));
		// write the file that exists from the server machine to the
		// outputstream of the response
		Files.copy(imageFile.toPath(), response.getOutputStream());
	}
}
