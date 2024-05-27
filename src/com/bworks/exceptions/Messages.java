package com.bworks.exceptions;

public interface Messages {

	public static final String DATABASE_CONNECTION_ERROR = "Error connecting to database";
	public static final String APPOINTMENTS_ERROR = "Error connecting to Appointments view";
	public static final String DOCTORS_ERROR = "Error connecting to Doctors view";
	public static final String PICKLISTS_ERROR = "Error connecting to Picklists view";
	public static final String EXPORT_ERROR = "Error exporting data";
	public static final String SAVE_ERROR = "Error saving data";
	public static final String ARCHIVE_ERROR = "Error archiving data";
	public static final String SERVICE_ERROR = "Error loading post submission services";
	
	public static interface ErrorCode {

		public static final int DATABASE_CONNECTION_ERROR = 100;
		public static final int APPOINTMENTS_ERROR = 101;
		public static final int DOCTORS_ERROR = 102;
		public static final int PICKLISTS_ERROR = 102;
		public static final int EXPORT_ERROR = 200;
		public static final int SAVE_ERROR = 201;
		public static final int ARCHIVE_ERROR = 202;
		public static final int SERVICE_ERROR = 203;
	}
}
