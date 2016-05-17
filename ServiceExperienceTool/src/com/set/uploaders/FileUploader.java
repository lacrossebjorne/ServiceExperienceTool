package com.set.uploaders;

import java.io.InputStream;
import java.util.Hashtable;
import java.util.List;

public interface FileUploader {
	
	public boolean uploadFiles(Hashtable<InputStream, String> filesToUpload);
	public boolean uploadFile(InputStream stream, String filename);
	public List<String> getRecentlyUploadedFileNames();
}
