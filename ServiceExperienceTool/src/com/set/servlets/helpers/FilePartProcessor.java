package com.set.servlets.helpers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import javax.servlet.http.Part;

/**
 * This class is responsible of turning Http-parts into Inputstreams
 * @author Emil
 *
 */
public class FilePartProcessor {

	Hashtable<InputStream, String> inputstreamFilenames;
	private boolean isError;
	private int errorCode;
	
    /**
     * Tries to convert the Collection<Part> into streams stores in a HashTable<InputStream, String>
     * @param allParts
     * @return if the operation was successful
     */
	public boolean processParts(Collection<Part> allParts) {
		this.isError = false;
		this.errorCode = -1;
		this.inputstreamFilenames = new Hashtable<InputStream, String>();
		if (allParts != null) {
			Set<InputStream> streams = new HashSet<InputStream>();
			for (Part part : allParts) {
				if (part.getName().equals("file")) {
					try {
					InputStream inputStream = part.getInputStream();
					String fileName = part.getSubmittedFileName();
					if (fileName == null) {
						this.isError = true;
						this.errorCode = 415;
						return false;
					}
					streams.add(part.getInputStream());
					inputstreamFilenames.put(inputStream, fileName);
					} catch (IOException e) {
						this.isError = true;
						this.errorCode = 500;
						return false;
					}
				}
			}
			if (inputstreamFilenames.size() > 0) {
				return true;
			}
		} 
		return false;
	}
	
	public boolean isError() {
		return this.isError;
	}
	
	public int getErrorCode() {
		return this.errorCode;
	}
	
	public Hashtable<InputStream, String> getInputstreamTable() {
		return inputstreamFilenames;
	}
}
