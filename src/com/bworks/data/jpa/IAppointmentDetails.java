package com.bworks.data.jpa;

public interface IAppointmentDetails {
    
    String getEncounterNumber();

    void setEncounterNumber(String encounterNumber);

    String getDateOfBirth();

    void setDateOfBirth(String dateOfBirth);
    
    String getApptTime();
    
    void setApptTime(String apptTime);
    
    String getApptStatus();
    
    void setApptStatus(String apptStatus);

    String getFirstName();

    void setFirstName(String firstName);

    String getLastName();

    void setLastName(String lastName);

    String getPatientMrn();

    void setPatientMrn(String patientMrn);

    String getClinic();

    void setClinic(String clinic);

    void formatDates();
}
