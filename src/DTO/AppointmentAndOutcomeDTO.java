package DTO;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) representing an appointment and its outcome.
 * This class contains the details of an appointment, including the appointment ID,
 * patient ID, doctor ID, status, date and time, and the associated appointment outcome.
 */
public class AppointmentAndOutcomeDTO {
    private int appointmentId;
    private String patientId;
    private String doctorId;
    private String status;
    private LocalDateTime dateTime;
    private AppointmentOutcomeDTO appointmentOutcomeDTO;

    /**
     * Retrieves the appointment ID.
     *
     * @return the appointment ID
     */
    public int getAppointmentId() {
        return appointmentId;
    }

    /**
     * Retrieves the patient ID associated with the appointment.
     *
     * @return the patient ID
     */
    public String getPatientId() {
        return patientId;
    }

    /**
     * Retrieves the doctor ID associated with the appointment.
     *
     * @return the doctor ID
     */
    public String getDoctorId() {
        return doctorId;
    }

    /**
     * Retrieves the status of the appointment.
     *
     * @return the status of the appointment
     */
    public String getStatus() {
        return status;
    }

    /**
     * Retrieves the date and time of the appointment.
     *
     * @return the date and time of the appointment
     */
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    /**
     * Retrieves the appointment outcome details.
     *
     * @return the appointment outcome details as an AppointmentOutcomeDTO
     */
    public AppointmentOutcomeDTO getAppointmentOutcomeDTO() {
        return appointmentOutcomeDTO;
    }

    /**
     * Sets the appointment ID.
     *
     * @param appointmentId the appointment ID to set
     */
    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    /**
     * Sets the patient ID associated with the appointment.
     *
     * @param patientId the patient ID to set
     */
    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    /**
     * Sets the doctor ID associated with the appointment.
     *
     * @param doctorId the doctor ID to set
     */
    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    /**
     * Sets the status of the appointment.
     *
     * @param status the status to set for the appointment
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Sets the date and time of the appointment.
     *
     * @param dateTime the date and time to set for the appointment
     */
    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    /**
     * Sets the appointment outcome details.
     *
     * @param appointmentOutcomeDTO the appointment outcome details to set
     */
    public void setAppointmentOutcomeDTO(AppointmentOutcomeDTO appointmentOutcomeDTO) {
        this.appointmentOutcomeDTO = appointmentOutcomeDTO;
    }
}
