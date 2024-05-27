package com.bworks.data.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

/**
 * Property File Reader
 * @author NIKHIL N K
 *
 */
public class PropertyReader {
	
	Properties properties;
	
	/**
	 * Create new instance of {@link PropertyReader}
	 * @param propertyFile
	 * @param {@link PropertyFileType}
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws InvalidPropertiesFormatException 
	 * @throws Exception 
	 */
	public PropertyReader(String propertyFile, PropertyFileType fileType)
			throws InvalidPropertiesFormatException, FileNotFoundException, IOException {
		this(new File(propertyFile), fileType);
	}
	
	/**
	 * Create new instance of {@link PropertyReader}
	 * @param propertyFile
	 * @param {@link PropertyFileType}
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws InvalidPropertiesFormatException 
	 * @throws Exception 
	 */
	public PropertyReader(File propertyFile, PropertyFileType fileType) 
			throws InvalidPropertiesFormatException, FileNotFoundException, IOException {
		
		properties = new Properties();

		if(propertyFile.exists() && propertyFile.canRead()) {
			switch (fileType) {
				case PLAIN:
					properties.load(new FileInputStream(propertyFile));
					break;
				default:
					properties.loadFromXML(new FileInputStream(propertyFile));
			}
		}
	}
	
	/**
	 * Get value from property file
	 * @param key
	 * @return value
	 * @throws Exception 
	 */
	public String getValue(String key) throws Exception {
		
		String value = null;
		
		try {			
			value = properties.getProperty(key);
		} catch (Exception e) {
			throw e;
		}
		
		return value;
	}
	
	/**
	 * Get value from property file
	 * @param key
	 * @return value
	 * @throws Exception 
	 */
	public String tryGetValue(String key) throws Exception {
		
		String value = null;
		
		try {			
			value = properties.getProperty(key);
		} catch (Exception e) {			
		}
		
		return value;
	}
	
	/**
	 * Property file types
	 * @author NIKHIL N K
	 *
	 */
	public enum PropertyFileType {
		PLAIN,
		XML
	}
}
