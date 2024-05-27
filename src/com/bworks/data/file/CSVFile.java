package com.bworks.data.file;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.bworks.util.AppConstants;

public class CSVFile implements File {

	private String encoding;
	private String splitCharacter = ",";

	@SuppressWarnings("unchecked")
	@Override
	public boolean write(String filePath, Object object) throws Exception {
	 
		FileManager fileManager = new FileManager();
		String content = "";
		boolean status = false;
		
		if(object != null && object.getClass() == Arrays.class || Collection.class.isAssignableFrom(object.getClass())) {
			if(object.getClass() == Arrays.class) {
				object = Arrays.asList(object);
			}
			for (Object item : (Iterable<Object>)object) {
				if(item.getClass() == Arrays.class || Collection.class.isAssignableFrom(item.getClass())) {
					for(Object innerItem : (Iterable<Object>) item) {
						content += innerItem + this.splitCharacter;
					}
					content += "\n";
				} else {
					content += item + this.splitCharacter;
				}
			}
			status = fileManager.write(filePath, content);
		} else {
			new IllegalArgumentException("Object is not iterable");
		}
		fileManager = null;
		object = null;
		filePath = null;
		content = null;
		return status;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T read(String filePath, Class<T> type) throws IOException {
		
		FileManager fileManager = new FileManager();
		List<String> content = null;
		
		if(type == List.class) {	
			content = new ArrayList<String>();
			fileManager.setEncoding(encoding);
			for (String line : fileManager.readAsText(filePath).split((AppConstants.SPLIT_CHAR))) {
				if(!line.trim().isEmpty()) {
					content.add(line.trim());
				}
			}
		}
		
		return (T) content;
	}

	@Override
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public void setSplitCharacter(String splitCharacter) {
		this.splitCharacter = splitCharacter;
	}

}
