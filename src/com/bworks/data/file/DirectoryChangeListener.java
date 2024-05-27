package com.bworks.data.file;

/**
 * Listen for Directoy changees
 * @author Nikhil N K
 */
public interface DirectoryChangeListener {

	/**
	 * Fired on directory changed
	 * @param directoryChangeEventArgs
	 */
	public void onFileChanged(DirectoryChangeEventArgs directoryChangeEventArgs);
}
