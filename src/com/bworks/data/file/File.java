package com.bworks.data.file;

import java.io.IOException;

/**
 * File
 * @author Nikhil N K
 *
 */
public interface File {

	/**
	 * Write
	 * @param filePath
	 * @param object
	 * @return
	 * @throws IOException
	 * @throws Exception 
	 */
	public boolean write(String filePath, Object object) throws IOException, Exception;
	
	/**
	 * Read
	 * @param filePath
	 * @param type
	 * @return
	 * @throws IOException 
	 * @throws Exception 
	 */
	public <T> T read(String filePath, Class<T> type) throws IOException, Exception;

	/**
	 * Set character encoding
	 * @param encoding
	 */
	public void setEncoding(String encoding);
}
