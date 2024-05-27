package com.bworks.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.bworks.exceptions.AudioHubException;

/**
 * 
 * @author NIKHIL N K
 * 
 */
public class ConfigurationManager {

	private static ConfigurationManager configurationManager = null;
	private static Properties properties = null;
	private static String rootPath = new File(AppConstants.FILE_SEPARATOR).getAbsolutePath();

	/**
	 * Private constructor
	 * @throws Exception 
	 */
	private ConfigurationManager() throws Exception {
		initialize();
	}

	/**
	 * Private constructor
	 * @throws Exception 
	 */
	private ConfigurationManager(String path) throws Exception {
		initialize(path);
	}

	/**
	 * Load configuration file and initialization
	 * @throws Exception 
	 */
	@SuppressWarnings("resource")
	private void initialize(String... path) throws Exception {
		
		properties = new Properties();
		File configFile = null;
		
		try {
			if(path.length > 0) {
				rootPath = path[0]; 
				if(!rootPath.endsWith(System.getProperty("file.separator"))) {
					rootPath += System.getProperty("file.separator");
				}
				configFile = new File(rootPath + AppConstants.CONFIG_FILE_NAME);
				if(!configFile.exists() || !configFile.canRead()) {
					configFile = new File(AppConstants.CONFIG_FILE_NAME);
				}
			} else {
				configFile = new File(AppConstants.CONFIG_FILE_NAME);
			}
			if(configFile.exists() && configFile.canRead()) {
				properties.loadFromXML(new FileInputStream(configFile));
			} else {
				throw new IOException("Configuration file not found or cannot read");
			}
		} catch (IOException e) {
			throw new AudioHubException(e.getMessage(), e);
		} catch (Exception e) {
			throw new AudioHubException("Error reading configuration file " + 
					configFile.getAbsolutePath(), e);
		}
	}

	/**
	 * Create a new instance of ConfigurationManager
	 * @return {@link ConfigurationManager}
	 * @throws Exception 
	 */
	public static synchronized ConfigurationManager createInstance() throws Exception {

		if (configurationManager == null) {
			configurationManager = new ConfigurationManager();
		}

		return configurationManager;
	}

	/**
	 * Create a new instance of ConfigurationManager
	 * @return {@link ConfigurationManager}
	 * @throws Exception 
	 */
	public static synchronized ConfigurationManager createInstance(String path) throws Exception {

		if (configurationManager == null) {
			if(path != null && !path.isEmpty()) {
				configurationManager = new ConfigurationManager(path);
			} else {
				configurationManager = new ConfigurationManager();
			}
		}

		return configurationManager;
	}

	/**
	 * Get value for a given key in configuration file
	 * 
	 * @param key
	 * @return Value
	 * @throws Exception 
	 */
	public synchronized String getProperty(String key) throws Exception {

		String value = "";
		
		if(properties == null) {
			throw new Exception("Configuration file is not loaded");
		}
		if(properties.containsKey(key)) {
			value = properties.getProperty(key).trim();
		} else {
			throw new Exception("Given key is not found in configuration file");
		}

		return value;
	}

	/**
	 * Get value for a given key in configuration file
	 * 
	 * @param key
	 * @return Value
	 */
	public synchronized String tryGetProperty(String key) {

		return tryGetProperty(key, false);
	}

	/**
	 * Get value for a given key in configuration file
	 * 
	 * @param key
	 * @return Value
	 * @throws Exception 
	 */
	public synchronized String tryGetProperty(String key, boolean isPath) {

		String value = null;
		
		if(properties != null && properties.containsKey(key)) {
			value = properties.getProperty(key).trim();
			if(isPath && !value.endsWith(System.getProperty("file.separator"))) {
				value += System.getProperty("file.separator");
			}
		}

		return value;
	}

	/**
	 * Get the root path of the application
	 * By default it will be the execution path
	 * @return
	 */
	public static String getRootPath() {
		return rootPath;
	}
}
