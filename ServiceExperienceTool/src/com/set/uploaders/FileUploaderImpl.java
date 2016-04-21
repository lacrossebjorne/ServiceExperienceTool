package com.set.uploaders;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import javax.naming.InitialContext;
import javax.naming.NamingException;

public class FileUploaderImpl implements FileUploader {

	private String[] allowedMimeTypes;
	private File uploadPath;
	private Set<String> recentlyUploadedFilenames;

	public FileUploaderImpl() throws NamingException {
		this(InitialContext.doLookup("java:comp/env/generalUploadPath"), "");
	}

	public FileUploaderImpl(String absolutePathName, String... allowedMimeTypes) {
		this.allowedMimeTypes = allowedMimeTypes;
		this.uploadPath = new File(absolutePathName);
		this.recentlyUploadedFilenames = new HashSet<String>();
	}

	@Override
	public boolean uploadFiles(Hashtable<InputStream, String> filesToUpload) {

		if (!uploadPath.exists()) {
			uploadPath.mkdirs();
		}

		Set<InputStream> streams = filesToUpload.keySet();

		for (InputStream stream : streams) {
			String fileName = filesToUpload.get(stream);

			if (!uploadFile(stream, fileName))
				return false;
		}

		return true;
	}

	private boolean isFiletypeAllowed(File file) throws IOException {

		String contentType = Files.probeContentType(file.toPath());
		System.out.println("mimeType of file: " + contentType);
		for (String allowedMimeType : allowedMimeTypes) {
			if (contentType != null && contentType.startsWith(allowedMimeType)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean uploadFile(InputStream fileIn, String submittedFileName) {

		// some browsers like IE/Edge send the full path
		// so the regex will split the text where the character / or \ occurs
		String[] fileNameSplit = submittedFileName.split("[/\\\\]");
		String fileName = fileNameSplit[fileNameSplit.length - 1];

		File fullPath = new File(uploadPath, fileName);

		// if there are an image with same file-name, then rename it
		// if the file has no extention, return from this method

		if (fullPath.exists()) {
			int addedNumber = 1;
			String newName = null;
			do {
				newName = "";
				String[] splits = fileName.split("\\.");
				if (splits.length >= 2) {
					int extensionIndex = splits.length - 1;

					for (int i = 0; i < extensionIndex; i++)
						newName += splits[i];
					newName += String.format("(%d).%s", addedNumber++, splits[extensionIndex]);
				} else {
					return false;
				}
				fullPath = new File(uploadPath, newName);
			} while (fullPath.exists());
			fileName = newName;
		}

		OutputStream fileOut = null;

		try {
			if (!isFiletypeAllowed(fullPath)) {
				return false;
			}

			fileOut = new FileOutputStream(fullPath);

			int read = 0;
			final byte[] bytes = new byte[1024];

			// read content and write to disk
			while ((read = fileIn.read(bytes)) != -1) {
				fileOut.write(bytes, 0, read);
			}
			recentlyUploadedFilenames.add(fileName);

		} catch (IOException e) {
			e.printStackTrace();
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
		return true;
	}

	public String[] getRecentlyUploadedFileNames() {
		Set<String> returnSet = new HashSet<String>();
		returnSet.addAll(recentlyUploadedFilenames);
		this.recentlyUploadedFilenames.clear();
		return (String[]) returnSet.toArray(new String[returnSet.size()]);
	}
}
