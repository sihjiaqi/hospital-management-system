package model.Appointment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import model.ICSVExportable;

/**
 * Represents an appointment between a doctor and a patient.
 * The appointment contains information about the doctor, patient, the scheduled date and time,
 * and the status of the appointment.
 */
public class Appointment implements ICSVExportable {
        /**
         * Enum representing the possible statuses of an appointment.
         */
        public enum AppointmentStatus {
            /**
         * Appointment has been confirmed and is scheduled to take place.
         */
        CONFIRMED,

        /**
         * Appointment has been canceled, and will no longer take place.
         */
        CANCELED,

        /**
         * Appointment is pending and has not yet been confirmed or declined.
         */
        PENDING,

        /**
         * Appointment was declined, and will not take place.
         */
        DECLINED,

        /**
         * Appointment has been completed, and the visit or service has been finished.
         */
        COMPLETED
    }

    private static int appointmentIdCounter = 0;
    private final int appointmentId;
    private String doctorId;
    private String patientId;
    private LocalDateTime dateTime;
    private AppointmentStatus status;

    /**
     * Constructs a new Appointment instance.
     *
     * @param doctorId The ID of the doctor for the appointment.
     * @param patientId The ID of the patient for the appointment.
     * @param dateTime The date and time of the appointment.
     * @param status The status of the appointment.
     */
    public Appointment(String doctorId, String patientId, LocalDateTime dateTime, AppointmentStatus status) {
        this.appointmentId = ++appointmentIdCounter;
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.dateTime = dateTime;
        this.status = status;
    }

    /**
     * Gets the unique appointment ID.
     *
     * @return The appointment ID.
     */
    public int getAppointmentId() {
        return appointmentId;
    }

    /**
     * Gets the ID of the doctor for the appointment.
     *
     * @return The doctor's ID.
     */
    public String getDoctorId() {
        return doctorId;
    }

    /**
     * Gets the ID of the patient for the appointment.
     *
     * @return The patient's ID.
     */
    public String getPatientId() {
        return patientId;
    }

    /**
     * Gets the scheduled date and time of the appointment.
     *
     * @return The date and time of the appointment.
     */
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    /**
     * Gets the status of the appointment.
     *
     * @return The status of the appointment.
     */
    public AppointmentStatus getStatus() {
        return status;
    }

    /**
     * Sets the doctor ID for the appointment.
     *
     * @param doctorId The new doctor ID.
     */
    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    /**
     * Sets the patient ID for the appointment.
     *
     * @param patientId The new patient ID.
     */
    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    /**
     * Sets the scheduled date and time of the appointment.
     *
     * @param dateTime The new scheduled date and time.
     */
    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    /**
     * Sets the status of the appointment.
     *
     * @param status The new status of the appointment.
     */
    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    /**
     * Converts the appointment object to a CSV record.
     * The record includes the appointment ID, doctor ID, patient ID,
     * formatted date and time, and status of the appointment.
     *
     * @return A string array representing the appointment's CSV record.
     */
    @Override
    public String[] toCSVRecord() {
        // Define a custom date format for LocalDateTime (e.g., "yyyy-MM-dd HH:mm:ss")
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = dateTime != null ? dateTime.format(formatter) : "";

        return new String[] {
                String.valueOf(getAppointmentId()), // Convert appointmentId to String
                getDoctorId(),
                getPatientId(),
                formattedDateTime, // Format LocalDateTime as String
                getStatus().toString() // Convert the enum status to a String
        };
    }

    /**
     * Returns a string representation of the appointment object.
     *
     * @return A string representing the appointment's details.
     */
    @Override
    public String toString() {
        return String.format("Appointment ID: %d\nDoctor ID: %s\nPatient ID: %s\nDate and Time: %s\nStatus: %s",
                appointmentId, doctorId, patientId, dateTime.toString(), status);
    }
}