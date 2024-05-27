package com.bworks.data.file;

import java.io.IOException;
import java.util.List;

/**
 * Text File
 * @author Nikhil N K
 *
 */
public class TextFile implements File {

	private String encoding;

	@Override
	public boolean write(String filePath, Object object) throws Exception {
		com.bworks.data.file.FileManager fileManager = new FileManager();
		fileManager.setEncoding(encoding);;
		return fileManager.write(filePath, object.toString());
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T read(String filePath, Class<T> type) throws IOException {
		
		FileManager fileManager = new FileManager();
		List<String> content = null;
		
		if(type == List.class) {
			fileManager.setEncoding(encoding);
			content = fileManager.readAsLines(filePath);
		}
		
		return (T) content;
	}

	@Override
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

}
