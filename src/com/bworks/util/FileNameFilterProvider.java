package com.bworks.util;

import java.io.File;
import java.io.FilenameFilter;

/**
 * FileNameFilterProvider
 * @author Nikhil N K
 */
public class FileNameFilterProvider {

	/**
	 * Provide filter as per request
	 * @param matcher
	 * @param filterMethod
	 * @return
	 */
	public static FilenameFilter getFilter(final String matcher, final FilterMethod filterMethod) {

		FilenameFilter filenameFilter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {

				boolean isAccepted = false;

				switch (filterMethod) {
				case STARTS_WITH:
					isAccepted = name.toLowerCase().startsWith(matcher);
					break;
				case CONTAINS:
					isAccepted = name.toLowerCase().contains(matcher);
					break;
				case ENDS_WITH:
					isAccepted = name.toLowerCase().endsWith(matcher);
					break;
				case REGEX:
					try {
						isAccepted = name.toLowerCase().matches(matcher);
					} catch (Exception e) {
						isAccepted = false;
					}
					break;
				default: 
					isAccepted = false;
				}

				return isAccepted;
			}
		};
		return filenameFilter;
	}
}
