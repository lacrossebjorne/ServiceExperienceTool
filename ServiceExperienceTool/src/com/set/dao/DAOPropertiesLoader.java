package com.set.dao;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DAOPropertiesLoader {

	private static final String PROPERTIES_FILE = "dao.properties";
	private static final Properties PROPERTIES = new Properties();
	private String specificKey;

	static {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream propertiesFileStream = classLoader.getResourceAsStream(PROPERTIES_FILE);
		try {
			if (propertiesFileStream == null)
				throw new IOException("No properties file in classpath.");

			PROPERTIES.load(propertiesFileStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public DAOPropertiesLoader(String specificKey) {
		this.specificKey = specificKey;
	}

	public String getProperties(String key, boolean mandatory) {
		String fullKey = specificKey + "." + key;
		String property = PROPERTIES.getProperty(fullKey);
		if (property == null || property.trim().length() == 0) {
			if (mandatory)
				throw new IllegalArgumentException("Mandatory key is missing in " + PROPERTIES_FILE);
			else
				property = null;
		}
		return property;
	}
}
