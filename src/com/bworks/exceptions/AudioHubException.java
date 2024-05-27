package com.bworks.exceptions;

public class AudioHubException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Create a new instance of {@link AudioHubException}
	 */
	public AudioHubException() {
		super();
	}

	public AudioHubException(String message) {
		super(message);
	}

	public AudioHubException(String message, int errorCode) {
		super(message);
	}

    public AudioHubException(String message, int errorCode, Throwable cause) {
        super(message, cause);
    }

    public AudioHubException(String message, Throwable cause) {
        super(message, cause);
    }

    public AudioHubException(Throwable cause) {
        super(cause);
    }

    protected AudioHubException(String message, Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace) {
	super(message, cause, enableSuppression, writableStackTrace);
	}
}
