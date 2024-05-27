package com.bworks.data.file;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.List;

import com.bworks.log.LoggerProvider;

/**
 * Watch for directory changes
 * @author Nikhil N K
 */
public class DirectoryWatcher {
	
	private Path directory = null;
	List<DirectoryChangeListener> listeners = new ArrayList<DirectoryChangeListener>();
	
	/**
	 * Set path
	 * @param path
	 */
	public void setPath(String path) {
		directory = Paths.get(path);
	}
	
	/**
	 * Start watching
	 * @param kinds
	 */
	public void startWatch(Kind<?>... kinds) {
		try {
	           WatchService watcher = directory.getFileSystem().newWatchService();
	           directory.register(watcher, StandardWatchEventKinds.ENTRY_CREATE, 
	           StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);

	           WatchKey watckKey = watcher.take();

	           List<WatchEvent<?>> events = watckKey.pollEvents();
	           for (WatchEvent<?> event : events) {
	                if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
	                	LoggerProvider.getLogger().debug("Created: " + event.context().toString());
	                }
	                if (event.kind() == StandardWatchEventKinds.ENTRY_DELETE) {
	                	LoggerProvider.getLogger().debug("Delete: " + event.context().toString());
	                }
	                if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
	                	LoggerProvider.getLogger().debug("Modify: " + event.context().toString());
	                }
	                
	                for (DirectoryChangeListener listener : listeners) {
	                    listener.onFileChanged(new DirectoryChangeEventArgs());
	                }
	            }
	           
	        } catch (Exception e) {
	        	LoggerProvider.getLogger().error("Error: " + e.toString());
	        }
	}

	/**
	 * Add event listeners
	 * @param listener
	 */
	public void addListener(DirectoryChangeListener listener) {
		listeners.add(listener);
	}
}
