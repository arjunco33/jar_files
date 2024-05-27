package com.bworks.data.core;

import java.util.List;
import java.util.Map;

import com.bworks.data.jpa.IAppointmentDetails;
import com.bworks.data.jpa.IDoctorDetails;

/**
 * <b>IDataSource</b>
 * @author <b>NIKHIL N K</b>
 */
public interface IDataSource {
	
	/**
	 * Get Patient Details
	 * @param mrn
	 * @return {@link AppointmentDetails}
	 * @throws Exception 
	 */
	public IAppointmentDetails getPatientDetails(String mrn) throws Exception;

	/**
	 * Get list of patients under all clinics
	 * @return {@link Map} of clinic and Patients' details
	 * @throws Exception 
	 */
	public Map<String, List<IAppointmentDetails>> getPatientsList() throws Exception;

	/**
	 * Get list of patients under a clinic
	 * @param clinic
	 * @return {@link List} of Patients' details
	 * @throws Exception 
	 */
	public List<IAppointmentDetails> getPatientsList(String clinic) throws Exception;
	
	/**
	 * Get {@link DoctorDetails}
	 * @param doctorId
	 * @return {@link DoctorDetail}
	 * @throws Exception 
	 */
	public IDoctorDetails getDoctorsDetails(String doctorId) throws Exception;
	
	/**
	 * Get Doctors' list
	 * @return {@link Map} of Doctors' ID and names
	 * @throws Exception 
	 */
	public Map<String ,String> getDoctorsList() throws Exception;

	/**
	 * Get {@link IAppointmentDetails} for an encounter number
	 * @return {@link List} of {@link IAppointmentDetails}
	 * @throws Exception
	 */
	public IAppointmentDetails getAppointmentDetails(String encounterNumber) throws Exception;

	/**
	 * Get picklists for the forms
	 * @return Picklists
	 * @throws Exception 
	 */
	public Map<String, Object> getPickList(String...args) throws Exception;
}
