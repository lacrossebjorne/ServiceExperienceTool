package com.set.servlets;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 * this class is not finished, please do not add modifications to it
 * @author Emil
 *
 */
@MultipartConfig
public class UploadServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
//	private static final int MEMORY_TRESHHOLD = 1024 * 1024 * 3; // 3mb
//	private static final int MAX_FILE_SIZE = 1024 * 1024 * 40;
//	private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 50;
	private final String IMAGES_NEWS_PATH = "images/news";

	private PrintWriter writer;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		List<Part> filesToUpload = new LinkedList<Part>();
		Collection<Part> parts = request.getParts();
		for (Part part : parts) {
			if (part.getName().equals("uploadFile")) {
				filesToUpload.add(part);
				System.out.println("file found: " + part.getContentType() + ";" + part.getName() + ";" + part.getSubmittedFileName());
			}
		}
		writer = response.getWriter();

		String homePath = "images/news";
		try {
			homePath = InitialContext.doLookup("java:comp/env/homePath");
		} catch (NamingException e) {
			e.printStackTrace();
		}

		//todo this fileuploader is generic, so it should not only upload images
		//make some kind of check that determines where the file should be saved
		File uploadDir = new File(homePath, IMAGES_NEWS_PATH);
		if (!uploadDir.exists()) {
			uploadDir.mkdirs();
		}

		for (Part filePart : filesToUpload) {
			uploadFile(uploadDir, filePart);
		}

	}

	private void uploadFile(File uploadDir, Part filePart) {
		String fileName = filePart.getSubmittedFileName();
		OutputStream fileOut = null;
		InputStream fileIn = null;
		File fullPath = new File(uploadDir, fileName);
		
		//if there are an image with same file-name, then rename it
		//if the file has no extention, return from this method
		int addedNumber = 1;
		while (fullPath.exists()) {
			String newName = "";
			String[] splits = fileName.split("\\.");
			if (splits.length >= 2) {
				int extensionIndex = splits.length - 1;

				for (int i = 0; i < extensionIndex; i++)
					newName += splits[i];
				newName += String.format("(%d).%s", addedNumber++, splits[extensionIndex]);
			} else {
				writer.println("<h1>Invalid filename!</h1>");
				return;
			}
			fullPath.renameTo(new File(uploadDir, newName));
		}
		
		try {
			fileOut = new FileOutputStream(fullPath);
			fileIn = filePart.getInputStream();

			int read = 0;
			final byte[] bytes = new byte[1024];
			
			//read content and write to disk
			while ((read = fileIn.read(bytes)) != -1) {
				fileOut.write(bytes, 0, read);
			}
			writer.println("File was successfully uploaded!");
			System.out.println("file uploaded: " + fullPath.getAbsolutePath());

		} catch (IOException e) {
			writer.println("<h1>Error saving the file.</h1>");
		} finally {
			try {
				if (fileOut != null)
					fileOut.close();
				if (fileIn != null)
					fileIn.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
