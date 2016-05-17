package com.set.uploaders;

import java.io.InputStream;
import java.util.Hashtable;
import java.util.List;

public class ImageStreamUploader {
	
	private boolean isError = false;
	private int errorCode = -1;
	List<String> recentlyUploadedFileNames = null;
	
	public boolean uploadFiles(Hashtable<InputStream, String> inputstreamFilenames) {
		this.isError = false;
		this.errorCode = -1;
		FileUploader imageUploader = FileUploaderFactory.getNewsImageUploader();
		boolean filesAreUploaded = imageUploader.uploadFiles(inputstreamFilenames);
		if (filesAreUploaded) {
			recentlyUploadedFileNames = imageUploader.getRecentlyUploadedFileNames();
			return true;
		} else {
			this.isError = true;
			this.errorCode = 415;
			return false;
		}
	}
	
	public List<String> getRecentlyUploadedFileNames() {
		return this.recentlyUploadedFileNames;
	}
	
	public boolean isError() {
		return this.isError;
	}
	
	public int getErrorCode() {
		return this.errorCode;
	}
}
