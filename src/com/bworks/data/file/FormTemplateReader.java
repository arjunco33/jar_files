package com.bworks.data.file;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.bworks.exceptions.AudioHubException;
import com.bworks.log.LoggerProvider;
import com.bworks.util.AppConstants;
import com.bworks.util.ConfigurationManager;
import com.bworks.util.FileNameFilterProvider;
import com.bworks.util.FilterMethod;

/**
 * Template Reader
 * @author Nikhil N K
 *
 */
public class FormTemplateReader {

	/**
	 * Read templates
	 * @param basePath
	 * @param fileName
	 * @return
	 * @throws Exception 
	 */
	public static synchronized String read(String basePath, String fileName) {
		
		FileManager fileManager = null;
		String formData = "";

		try {
			if(new File(basePath + fileName).exists()) {
				fileManager = new FileManager();
				fileManager.setEncoding("UTF-8");
				formData = fileManager.readAsText(basePath + fileName).
						replace('"', '\'').replaceAll("\\r?\\n", " ");
			}
		} catch (Exception e) {
			LoggerProvider.getLogger().error(FormTemplateReader.class.getSimpleName(), e);
		}
		
		return formData;
	}
	
	/**
	 * Read all templates
	 * @param basePath
	 * @param fileList
	 * @return
	 * @throws AudioHubException 
	 * @throws Exception 
	 */
	public static synchronized Map<String, String> readAll(String basePath, String userFolder)
			throws AudioHubException {
		
		Map<String, String> formTemplates = null;
		String formsDirectory = null;
		
		try {
			formsDirectory = ConfigurationManager.createInstance().tryGetProperty("FormsFolder", true);
			basePath = formsDirectory == null ? basePath += AppConstants.FORM_TEMPLATE_DIRECTORY :
				formsDirectory;
			if(userFolder != null && !userFolder.isEmpty()) {
				basePath += (userFolder + System.getProperty("file.separator"));
			}
			LoggerProvider.getLogger().debug("Reading form templates from " + basePath);
			formTemplates = new HashMap<String, String>();
			if(new File(basePath).exists()) {
				String[] formXmlSources = new File(basePath).list(FileNameFilterProvider.getFilter(
						AppConstants.Extentions.XML, FilterMethod.ENDS_WITH));
				for (String filename : formXmlSources) {
					formTemplates.put(filename.replaceFirst("[.][^.]+$", ""), read(basePath, filename));
				}
				LoggerProvider.getLogger().debug("Reading form templates completed");
			}
		} catch (Exception e) {
			LoggerProvider.getLogger().error(FormTemplateReader.class.getSimpleName(), e);
			throw new AudioHubException(e);
		}
		
		return formTemplates;
	}

	/**
	 * Read all templates
	 * @param basePath
	 * @param userFolder
	 * @return
	 * @throws AudioHubException 
	 */
	public static synchronized Map<String, String> readAll(String basePath) 
			throws AudioHubException {
		return readAll(basePath, null);
	}
}
