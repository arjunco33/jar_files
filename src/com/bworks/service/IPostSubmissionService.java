package com.bworks.service;

import com.bworks.service.util.ServiceParams;

public interface IPostSubmissionService extends Runnable {

	/**
	 * Load {@link IPostSubmissionService} instance
	 * @param args
	 */

	public void load(String... args);

	/**
	 * Start {@link IPostSubmissionService}
	 */
	public void start(ServiceParams... params);

	/**
	 * Abort {@link IPostSubmissionService}
	 */
	public void abort();
	
	/**
	 * Set service parameters
	 * @param params
	 */
	public void setServiceParams(ServiceParams params);
}
