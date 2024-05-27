package com.bworks.log;

import java.io.File;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

import com.bworks.util.AppConstants;
import com.bworks.util.ConfigurationManager;

/**
 * Logger framework
 * @author Nikhil N K
 *
 */
public class LoggerProvider {
	
	static Logger log = null;
	
	/**
	 * Static initializer
	 */
	static {
		
		RollingFileAppender appender = null;
		String logFolder = null;
		ConfigurationManager configManager = null;
		String logLevelString = "";
		Level logLevel = null;
		String patternLayout = null;
		String logFileSizeString = null;
		long logFileSize = 0;
		
		try {
			log = Logger.getLogger("AudioHub");
			appender = new RollingFileAppender();
			try {
				configManager = ConfigurationManager.createInstance();
				logFolder = configManager.tryGetProperty("LogDirectory", true);
				patternLayout = configManager.tryGetProperty("LogPattern");
				logFileSizeString = configManager.tryGetProperty("LogFileSize");
				logLevelString = configManager.tryGetProperty("LogLevel");
			} catch(Exception e) {
				e.printStackTrace();
			}
			logLevel = (logLevel != null && Level.toLevel(logLevelString.toUpperCase()) != null)?
				Level.toLevel(logLevelString):Level.DEBUG;
			logFolder = logFolder != null ? logFolder : "";
			patternLayout = (patternLayout != null && !patternLayout.isEmpty()) ? 
					patternLayout : AppConstants.Log.DEFUALT_PATTERN;
			logFileSize = logFileSizeString != null && logFileSizeString.matches("^\\d+$") ? 
					Long.parseLong(logFileSizeString) : AppConstants.Log.DEFAULT_FILE_SIZE;
			appender.setName("FileLogger");
			try {
				new File(logFolder).mkdirs();
			} catch(Exception e) {
				logFolder = "";
			}
			appender.setFile(logFolder + "audiohub.log");
			appender.setLayout(new PatternLayout(patternLayout));
			appender.setThreshold(logLevel);
			appender.setAppend(true);
			appender.setMaxBackupIndex(AppConstants.Log.DEFAULT_BACKUP_COUNT);
			appender.setMaximumFileSize(logFileSize);
			appender.activateOptions();
			log.addAppender(appender);
		} catch(Exception e) {
			e.printStackTrace();
		}
	};
	
	/**
	 * getLogger
	 * @return
	 */
	public synchronized static Logger getLogger() {
		return log;
	}
}
