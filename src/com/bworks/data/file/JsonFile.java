package com.bworks.data.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.channels.FileLock;

import com.google.gson.Gson;

public class JsonFile implements com.bworks.data.file.File {

	private String encoding;

	@Override
	public boolean write(String filePath, Object object) throws Exception {
		
		Gson gson = new Gson();
		String jSonString = "";
		OutputStreamWriter streamWriter = null;
		FileOutputStream outputStream = null;
		FileLock lock = null;
		boolean status = false;
		String folderLocation = "";
		
		try {
			if(filePath != null && !filePath.isEmpty()) {
				folderLocation = filePath.substring(0, 
						filePath.lastIndexOf(System.getProperty("file.separator")));
				new File(folderLocation).mkdirs();			
				jSonString = gson.toJson(object);
				outputStream = new FileOutputStream(new File(filePath));
				if(this.encoding != null) {
					streamWriter = new OutputStreamWriter(outputStream, this.encoding);
				} else {
					streamWriter = new OutputStreamWriter(outputStream);
				}
				lock = outputStream.getChannel().tryLock();		
				if(lock != null) {
					streamWriter.write(jSonString);
				}
				status = true;
			}
		} catch (Exception e) {
			throw e;
		}
		finally {
			try {
				if(lock != null && streamWriter != null) {
					lock.release();
					outputStream.flush();
					streamWriter.flush();
					streamWriter.close();
				}
				
				if(outputStream != null) {
				    outputStream.close();
				}
			} catch (IOException e) {
				throw e;
			}
		}
		
		   object = null;
		   filePath = null;
	       gson = null;
	       jSonString = null;
	       streamWriter = null;
	       outputStream = null;
	       lock = null;
	       folderLocation = null;
		
		return status;
	}
	
	@Override
	public <T> T read(String filePath, Class<T> type) throws Exception {

		InputStreamReader streamReader = null;
		char[] fileContent = null;
		File file = new File(filePath);
		Gson gson = new Gson();
		T object = null;
		String jsonString = null;
		
		try {
			if(file.exists()) {
				streamReader = new InputStreamReader(new FileInputStream(file), "UTF-8");
				fileContent = new char[(int) file.length()];
				streamReader.read(fileContent);
				jsonString = new String(fileContent).trim();
				object = gson.fromJson(jsonString, type);
			}
		} catch (Exception e) {
			throw e;
		}
		finally {
			try {
				if(streamReader != null) {
					streamReader.close();
				}
			} catch (Exception e) {
				throw e;
			}
		}
		
		return object;
	}

	@Override
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

}
