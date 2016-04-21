package com.set.uploaders;

import java.io.File;

import javax.naming.InitialContext;
import javax.naming.NamingException;

public class FileUploaderFactory {
	
	public static FileUploader getGeneralFileUploader() {
		FileUploaderImpl generalFileUploader = null;
		try {
			generalFileUploader = new FileUploaderImpl();
		} catch (NamingException e) {
			e.printStackTrace();
		}
		
		return generalFileUploader;
	}
	
	public static FileUploader getNewsImageUploader() {
		FileUploaderImpl newsImageUploader = null;
		try {
			String uploadPath = InitialContext.doLookup("java:comp/env/imagesUploadPath") + File.separator + "news";
			String allowedMimeTypes = "image/";
			newsImageUploader = new FileUploaderImpl(uploadPath, allowedMimeTypes);
		} catch (NamingException e) {
			e.printStackTrace();
		}
		
		return newsImageUploader;
	}
}
