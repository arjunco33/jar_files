package com.bworks.util;


/**
 * Application Constants
 * @author NIKHIL N K
 *
 */
public class AppConstants {

	public static final String FILE_SEPARATOR = System.getProperty("file.separator");
	public static final String CONFIG_FILE_NAME = "audiohub.config";
	public static final String PERSISTENCE_UNIT = "AudioHubJPA";
	public static final String PERSISTENCE_UNIT_EXPORT = "AudioHubExportJPA";
	public static final String DATASOURCE = "DataSource";
	public static final String DATASOURCE_DB = "DATABASE";
	public static final String DATASOURCE_FILE = "FILE";
	public static final String RESOURCES_DIRECTORY = "resources" + AppConstants.FILE_SEPARATOR;
	public static final String FORM_TEMPLATE_DIRECTORY = RESOURCES_DIRECTORY + 
			"forms" + System.getProperty("file.separator");
	public static final String PREVIEW_DIRECTORY = RESOURCES_DIRECTORY + 
			"preview" + System.getProperty("file.separator");
	public static final String PENDING_RECORDS = "PendingRecords" + AppConstants.FILE_SEPARATOR;
	public static final String ARCHIEVED_RECORDS = "ArchivedRecords";
	public static final String SAVE_DIRECTORY = "SaveFolder";
	public static final String CSS_DIRECTORY = "CSSFolder";
	public static final String FORMS_DIRECTORY = "FormsFolder";
	public static final String FILE_NAME_FORMAT = "FileNameFormat";
	public static final String DEFAULT_FILE_NAME_FORMAT = "mrn_encounterNumber_yyyyMMddHHmmss";
	public static final String PREVIEW_FILE = "formpreview.xml";
	public static final String IMG_DIRECTORY = "ImageFolder";
	public static final String PDF_RESOURCE_DIRECTORY = "pdf";
	public static final long DEFAULT_CLEANUP_TIME = 15;
	public static final String SPLIT_CHAR = ";";
	public static final String FONT_DIRECTORY = "FontsFolder";
	public static final String TIMESTAMP_FORMAT = "MM-dd-yyyy hh:mm a";
	public static final String EXPORTY_PROPERTY_FILEPATH = "ExportEntryMapFilePath";
	public static final String ClientJSFiles = "ClientFiles_JS";
	public static final String ClientCSSFiles = "ClientFiles_CSS";
	public static final String ClientHMLFiles = "ClientFiles_HTML";
	public static final String ClientIMGFiles = "ClientFiles_IMG";
	public static final String CRG_FILEPATH = "CRG_FILEPATH";
	public static final String ZIP_FILEPATH = "ZIP_FILEPATH";
	public static final String ARCHIVE_FILE_NAME = "archive.zip";
	public static final String RESOURCE_ROOT_PATH = "RESOURCE_PATH";
	public static final String SENDING_SYSTEM = "AUDIOHUB";	
	public static final String TEMPLATE_ID_ABR = "1";
	public static final String TEMPLATE_ID_HEARING_TEST = "2";
	public static final String TEMPLATE_ID_TYMP = "3";
	public static final String PDF_TEMPLATE_ABR = "PdfTemplateAbr";
	public static final String PDF_TEMPLATE_HEARING_TEST = "PdfTemplateHearingTest";
	public static final String PDF_TEMPLATE_TYMP = "PdfTemplateTymp";
	public static final String PDF_TEMPLATE_ABR_DEF_PAGE_COUNT = "PdfTemplateAbrDefaultPageCount";
	public static final String PDF_TEMPLATE_HEARING_TEST_DEF_PAGE_COUNT = "PdfTemplateHearingTestDefaultPageCount";
	public static final String PDF_TEMPLATE_TYMP_DEF_PAGE_COUNT = "PdfTemplateTympDefaultPageCount";
	
    public static final String INCLUDE_BILLING = "1";
    public static final String ONLY_BILLING    = "2";
    public static final String NO_BILLING      = "0";
    
    // XML Parser constants
    public static final String FIRST_NAME 				= "FirstName";
    public static final String LAST_NAME 				= "LastName";
    public static final String DOB 						= "DOB";
    public static final String APPOINTMENT_DETAILS 		= "AppointmentDetails";
    public static final String PATIENT_MRN 				= "PatientMrn";
    public static final String ENCOUNTER_NUMBER 		= "EncounterNumber";
    public static final String PATIENT_DETAILS			= "PatientDetails";
    public static final String APPOINTMENT_DATE			= "AppointmentDate";
    public static final String MRN 						= "MRN";
    public static final String CLINIC 					= "Clinic";
    public static final String DOCTOR_DETAILS			= "DoctorDetails";
    public static final String EMPLOYEE_ID				= "EmployeeId";
    public static final String SPECIALITY				= "Speciality";
    public static final String APPTIME 					= "ApptTime";
    public static final String APP_STATUS				= "ApptStatus";
    public static final String DATE_FORMAT_YYYY_MM_DD 	= "yyyy-MM-dd";
    
    // DB column name keys
    public static final String KEY_SESSION_ID = "sessionId";

    /*Some of the Keys and Values for the PDF Report data binding map.*/
	public static final String PATIENT_FD_DATE_OF_BIRTH_KEY = "dateOfBirth";
	public static final String PATIENT_FD_ACOUSTIC_IMMITANCE_MEASURE_KEY = "acousticImmitanceMeasure";
	public static final String PATIENT_FD_INTERPRETATION_OF_IMMITTANCE_KEY = "immittanceInterpretation";
	public static final String PATIENT_FD_ACOUSTIC_IMMITANCE_MEASURE_RIGHT_KEY = "right";
	public static final String PATIENT_FD_ACOUSTIC_IMMITANCE_MEASURE_LEFT_KEY = "left";
	
	//Value of this constant should be exactly same as member variable reliability if AudioFormData
	public static final String AUDIO_FD_RELIABILITY_KEY = "reliability";
	
	public static final String AUDIO_FD_RELIABILITY_GOOD_KEY = "isGoodReliability";
	public static final String AUDIO_FD_RELIABILITY_FAIR_KEY = "isFairReliability";
	public static final String AUDIO_FD_RELIABILITY_POOR_KEY = "isPoorReliability";
	
	/*Tympanometry Form Data keys*/
	//These values should be exactly same as the name of the member variables acousticImmitanceMeasure and acousticReflexThreshold of TympanometryFormData.
	public static final String TYMP_FD_ACOUSTIC_IMMITANCE_MEASURE_KEY = "acousticImmitanceMeasure";
	public static final String TYMP_FD_ACOUSTIC_REFLEX_THRESHOLD_KEY  = "acousticReflexThreshold";

    public static final String TYMP_FD_LEFT_IMAGE_KEY                    = "leftImage";
    public static final String TYMP_FD_RIGHT_IMAGE_KEY                   = "rightImage";
    public static final String TYMP_FD_LEFT_IMAGE_NOTES_KEY              = "leftComment";
    public static final String TYMP_FD_RIGHT_IMAGE_NOTES_KEY             = "rightComment";
    public static final String HEARING_TEST_TYMP_IMAGE_PAGE_REQUIRED_KEY = "hearingTestTympImagePageRequired";
    
    /*ABR Form Data keys*/
    public static final String ABR_FD_LEFT_IMAGE_KEY           = "abrAttachmentIMG";
    public static final String ABR_FD_RIGHT_IMAGE_KEY          = "abrAttachment178321abrAttachmentIMG";
    public static final String ABR_FD_SECOND_PAGE_REQUIRED_KEY = "abrSecondPageRequired";
    
    /*OAE Form Data Keys*/
    public static final String OAE_FD_OTO_ACOUSTIC_EMISSION_RIGHT_EAR = "otoAcousticEmissionRightEar";
    public static final String OAE_FD_OTO_ACOUSTIC_EMISSION_LEFT_EAR  = "otoAcousticEmissionLeftEar";
    public static final String OAE_INTERPRETATION_RIGHT_EAR_CONSISTENT_WITH_LBL = "Otoacoustic emissions testing in the right ear is consistent with ";
    public static final String OAE_INTERPRETATION_LEFT_EAR_CONSISTENT_WITH_LBL = "Otoacoustic emissions testing in the left ear is consistent with ";
    public static final String OAE_INTERPRETATION_BOTH_EAR_CONSISTENT_WITH_LBL_PREFIX = "Otoacoustic emissions testing was consistent with "; 
    public static final String OAE_INTERPRETATION_BOTH_EAR_CONSISTENT_WITH_LBL_SUFFIX = " in both ears";
    
    /*RECS Form Data Keys*/
    public static final String RECS_FD_ASSESSMENT = "assessment";
    
    /*Speech Form Data Keys*/
    public static final String SPEECH_FD_SPEECH_THRESHOILD = "SpeechThreshold";
	
	public static final String DATE_FORMAT_MM_dd_yyyy = "MM-dd-yyyy";
    
    // FORM DATA JSON Keys
    public static final String KEY_LEFT_CHART_PREVIEW 	= "leftChartPreview";
    public static final String KEY_RIGHT_CHART_PREVIEW 	= "rightChartPreview";
    public static final String KEY_FULL_CHART_PREVIEW 	= "fullChartPreview";
    public static final String KEY_AUDIOGRAM_OPTION		= "audiogramOption";
    
    //Audiogram options
    public static final String AUDIOGRAM_OPTION_CNT		     = "CNT";
    public static final String AUDIOGRAM_OPTION_DNT		     = "DNT";
    public static final String AUDIOGRAM_OPTION_SKIP_TO_PAGE = "SkipeToPage";
    public static final String AUDIOGRAM_OPTION_TYMP		 = "TYMP";
    public static final String AUDIOGRAM_OPTION_NONE		 = "";
    
    //PDF paragraph width category
    public static final String PDF_TEMPLATE_PARA_WIDTH_F = "F"; //Full width
    public static final String PDF_TEMPLATE_PARA_WIDTH_H = "H"; //Half width
    
    //Labels used in the PDF report tables
    public static final String RIGHT_EAR_LBL_R = "R";
    public static final String LEFT_EAR_LBL_L  = "L";

    //SII and PTA Keys
    public static final String RIGHT_EAR_SII_KEY = "SIICompute.SII_Right.Target";
    public static final String LEFT_EAR_SII_KEY = "SIICompute.SII_Left.Target";
    public static final String RIGHT_EAR_PTA_KEY = "PTACompute.PTA_Right.Target";
    public static final String LEFT_EAR_PTA_KEY = "PTACompute.PTA_Left.Target";
    
    // Log Messages
    public static final String LOG_MSG_FETCHING_DOCTOR_LIST 		= "Fetching doctor list";
    public static final String LOG_MSG_FETCHING_PATIENTS_LIST 		= "Fetching patients list";
    public static final String LOG_MSG_ERROR_READING_APPT_DETAILS 	= "Error occured while getting appointment details from file";
    public static final String LOG_MSG_ERROR_FILE					= "File exception";
    
    public static final String SSO_LOGIN_CONFIG = "ssoLoginConf";
    public static final String SSO_CONFIG		= "ssoConfig";

	/**
	 * Log Settings
	 * @author Nikhil N K
	 */
	public static class Log {

		public static final String DEFUALT_PATTERN = "%d [%-5p] [%c{1}] [%-28C{1}] %m%n";
		public static final long DEFAULT_FILE_SIZE = 2000000;
		public static final int DEFAULT_BACKUP_COUNT = 100;
	}

	/**
	 * File extensions
	 * @author Nikhil N K
	 */
	public static class Extentions {
		
		public static final String PDF = ".pdf";
		public static final String CSV = ".csv";
		public static final String XML = ".xml";
		public static final String JSON = ".json";
		public static final String TEXT = ".txt";
		public static final String CSS = ".css";
		public static final String JS = ".js";
		public static final String HTML = ".html";
		public static final String IMG = ".png";
	}
	
	/**
	 * Resource files
	 * @author Nikhil N K
	 */
	public static class Resources {
		
		public static final String ROOT = "resources" + AppConstants.FILE_SEPARATOR;
		public static final String CSS = AppConstants.Resources.ROOT +
				"css" + AppConstants.FILE_SEPARATOR;
		public static final String IMAGE = AppConstants.Resources.ROOT +
				"images" + AppConstants.FILE_SEPARATOR;
		public static final String FONT = AppConstants.Resources.ROOT +
				"fonts" + AppConstants.FILE_SEPARATOR;
	}

	/**
	 * JPA Queries
	 * @author Nikhil N K
	 *
	 */
	public static class Queries {

		public static interface Names {
			public static final String PATIENT_DETAILS = "Patient.Details";
			public static final String PATIENT_LIST = "Patients.Clinic";
			public static final String ARCHIVED_LIST = "AudiohubExport.All";
			public static final String PENDING_LIST = "AudiohubExport.Pending";
			public static final String GET_RECORD = "AudiohubExport.Record";
			public static final String GET_RECORD_ = "AudiohubExport.Get";
			public static final String LATEST_HISTORY = "Patient.History";
			public static final String LATEST_PATIENT_HISTORY = "Patient.Clinic.History";
			public static final String AUDIOGRAM_HISTORY = "Audiogram.History";
			public static final String IS_SESSION_EXIST = "Patient.Session";
			public static final String DELETE_SESSION = "Delete.Session";
			public static final String UPDATE_RECORD = "Patient.Update";
			public static final String UPDATE_RECORD_STATUS = "Patient.status";
			public static final String PATIENT_LIST_ALL = "Patients.All";
			public static final String DOCTORS_DETAILS = "Doctor.Details";
			public static final String DOCTORS_LIST = "Doctors.All";
			public static final String APPOINTMENT_DETAILS = "Appointment.Details";
			public static final String PICKLIST_FIND_ALL = "Picklist.All";
			public static final String PICKLIST_GET = "Picklist.Get";
			public static final String PICKLIST_MAP_FIND_ALL = "Picklist.Map.All";
		}

		public static final String UPDATE_RECORD = "UPDATE AudiohubExport A "
				+ "SET A.templateId = :templateId, A.visitDate = :visitDate, A.status = :status,"
				+ "A.history = :history, A.tymp = :tymp, A.audio = :audio, A.speech = :speech,"
				+ "A.oae = :oae, A.abr = :abr, A.recs = :recs, A.recommendation = :recommendation,"
				+ "A.submittedDate = :submittedDate, A.lastModified = :lastModified "
				+ "WHERE A.medicalRecNumber = :mrn AND A.sessionId = :sessionId";
		public static final String IS_SESSION_EXIST = "SELECT COUNT(A) FROM AudiohubExport A "
				+ "WHERE A.medicalRecNumber = :mrn AND A.sessionId = :sessionId";
		public static final String DELETE_SESSION = "DELETE FROM AudiohubExport A "
				+ "WHERE A.status = 0 AND A.sessionId = :sessionId";
		public static final String AUDIOGRAM_HISTORY = "SELECT A FROM AudiohubExport A "
				+ "WHERE A.medicalRecNumber = :mrn AND A.visitDate IN :dates AND A.status = 1";
		public static final String LATEST_PATIENT_HISTORY = "SELECT A FROM AudiohubExport A "
				+ "WHERE A.medicalRecNumber = :mrn AND A.clinicId = :clinicId AND A.status = 1";
		public static final String LATEST_HISTORY = "SELECT A.clinicId, A.doBirth, A.firstName, A.lastName, A.visitDate, A.audio FROM AudiohubExport A "
						+ "WHERE A.medicalRecNumber = :mrn AND A.status IN (1,2) AND A.audio IS NOT NULL AND A.templateId = 2";
		public static final String ARCHIVED_LIST = "SELECT A.medicalRecNumber, A.encounterNumber, A.lastModified, A.sessionId FROM AudiohubExport A "
				+ "WHERE A.medicalRecNumber like :mrn AND A.status = 1";
		public static final String GET_RECORD_ = "SELECT A FROM AudiohubExport A "
				+ "WHERE A.medicalRecNumber = :mrn "
				+ "AND A.sessionId = :sessionId";
		public static final String GET_RECORD = "SELECT A FROM AudiohubExport A "
				+ "WHERE A.sessionId = :sessionId";
		public static final String UPDATE_RECORD_STATUS = "UPDATE AudiohubExport A "
				+ "SET A.status = 2 "
				+ "WHERE A.medicalRecNumber = :mrn AND A.sessionId = :sessionId";
		public static final String PENDING_LIST = "SELECT A.medicalRecNumber, A.firstName, A.lastName, A.sessionId, A.templateId, A.encounterNumber, A.lastModified FROM AudiohubExport A "
				+ "WHERE A.doctorId = :doctorId AND A.clinicId = :clinicId AND A.status = 0";
		public static final String PATIENT_DETAILS = "SELECT A FROM AppointmentDetails A "
				+ "WHERE A.patientMrn = :mrn";
		public static final String PATIENT_LIST = "SELECT A FROM AppointmentDetails A "
				+ "WHERE A.clinic = :clinic";// AND A.appointmentDate = CURRENT_DATE";
		public static final String PATIENT_LIST_ALL = "SELECT A FROM AppointmentDetails A ";
				//+ "WHERE A.appointmentDate = CURRENT_DATE";
		public static final String DOCTORS_DETAILS = "SELECT D FROM DoctorDetails D "
				+ "WHERE D.employeeId = :doctorId";
		public static final String DOCTORS_LIST = "SELECT D FROM DoctorDetails D ORDER BY D.lastName";
		public static final String APPOINTMENT_DETAILS = "SELECT A FROM AppointmentDetails A "
				+ "WHERE A.encounterNumber = :encounterNumber";
		public static final String PICKLIST_FIND_ALL = "SELECT P FROM Picklist P";
		public static final String PICKLIST_GET = "SELECT M.name, P.item FROM Picklist P, Picklistmap M "
				+ "WHERE P.pickListId = M.id";
		public static final String PICKLIST_MAP_FIND_ALL = "SELECT P FROM Picklistmap P";
	}

	/**
	 * File source names
	 * @author Nikhil N K
	 *
	 */
	public interface FileSources {

		String PatientDetails = "PatientDetails";
		String DoctorDetails = "DoctorDetails";
		String AppointmentDetails = "AppointmentDetails";
		
	}

	/**
	 * Table names
	 * @author Nikhil N K
	 *
	 */
	public interface Tables {

		String DOCTOR_DETAILS = "ahub_specialist";
		String APPOINTMENT_DETAILS = "ahub_pat";
		String PICKLISTS = "ahub_picklists";
	}
}
