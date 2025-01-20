package model.Appointment;

import java.util.List;
import java.util.stream.Collectors;

import model.ICSVExportable;
import repository.Medication.MedicationDB;

/**
 * Represents the outcome of a medical appointment, including prescription details, billing, and medication fees.
 * Implements {@link ICSVExportable} for exporting the outcome data in CSV format.
 */
public class AppointmentOutcome implements ICSVExportable {
    
    /**
     * Enum representing the status of the prescription.
     */
    public enum PrescriptionStatus {
        /**
         * Prescription is pending approval or fulfillment.
         */
        PENDING,

        /**
         * Prescription has been dispensed to the patient.
         */
        DISPENSED,

        /**
         * No prescription has been issued.
         */
        NONE
    }

    /**
     * Enum representing the billing status of the appointment.
     */
    public enum BillingStatus {
        /**
         * The appointment has been paid for by the patient.
         */
        PAID,

        /**
         * The appointment has not yet been paid for.
         */
        UNPAID
    }

    private int appointmentId;
    private String serviceType;
    private List<String> medicationId;
    private String consultationNotes;
    private PrescriptionStatus status;
    
    // Billing-related attributes
    private double consultationFee;
    private List<Double> medicationFee;
    private double totalAmount;
    private BillingStatus billingStatus;

    /**
     * Constructor to create an AppointmentOutcome.
     * 
     * @param appointmentId The unique ID of the appointment
     * @param serviceType The type of medical service provided
     * @param medicationId The list of medication IDs prescribed during the appointment
     * @param consultationNotes The notes from the consultation
     * @param status The prescription status (PENDING, DISPENSED, NONE)
     */
    public AppointmentOutcome(int appointmentId,
            String serviceType,
            List<String> medicationId,
            String consultationNotes,
            PrescriptionStatus status) {
        this.appointmentId = appointmentId;
        this.serviceType = serviceType;
        this.medicationId = medicationId;
        this.consultationNotes = consultationNotes;
        this.status = status;
        this.consultationFee = 10.0;
        this.medicationFee = MedicationDB.findMedicationPricesByIds(medicationId);

        // Calculate total amount
        this.totalAmount = this.consultationFee + medicationFee.stream().mapToDouble(Double::doubleValue).sum();
        this.billingStatus = BillingStatus.UNPAID;
    }

    // Getter methods

    /**
     * Gets the appointment ID.
     * 
     * @return The appointment ID
     */
    public int getAppointmentId() {
        return appointmentId;
    }

    /**
     * Gets the type of service provided during the appointment.
     * 
     * @return The service type
     */
    public String getServiceType() {
        return serviceType;
    }

    /**
     * Gets the list of medication IDs prescribed during the appointment.
     * 
     * @return The list of medication IDs
     */
    public List<String> getMedicationId() {
        return medicationId;
    }

    /**
     * Gets the consultation notes.
     * 
     * @return The consultation notes
     */
    public String getConsultationNotes() {
        return consultationNotes;
    }

    /**
     * Gets the status of the prescription (PENDING, DISPENSED, NONE).
     * 
     * @return The prescription status
     */
    public PrescriptionStatus getStatus() {
        return status;
    }

    /**
     * Gets the consultation fee.
     * 
     * @return The consultation fee
     */
    public double getConsultationFee() {
        return consultationFee;
    }

    /**
     * Gets the list of medication fees.
     * 
     * @return The list of medication fees
     */
    public List<Double> getMedicationFee() {
        return medicationFee;
    }

    /**
     * Gets the total amount due for the appointment, including consultation and medication fees.
     * 
     * @return The total amount
     */
    public double getTotalAmount() {
        return totalAmount;
    }

    /**
     * Gets the billing status of the appointment (PAID, UNPAID).
     * 
     * @return The billing status
     */
    public BillingStatus getBillingStatus() {
        return billingStatus;
    }

    // Setter methods

    /**
     * Sets the appointment ID.
     * 
     * @param appointmentId The new appointment ID
     */
    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    /**
     * Sets the service type.
     * 
     * @param serviceType The new service type
     */
    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    /**
     * Sets the list of medication IDs.
     * 
     * @param medicationId The new list of medication IDs
     */
    public void setMedicationId(List<String> medicationId) {
        this.medicationId = medicationId;
    }

    /**
     * Sets the consultation notes.
     * 
     * @param consultationNotes The new consultation notes
     */
    public void setConsultationNotes(String consultationNotes) {
        this.consultationNotes = consultationNotes;
    }

    /**
     * Sets the prescription status.
     * 
     * @param status The new prescription status
     */
    public void setStatus(PrescriptionStatus status) {
        this.status = status;
    }

    /**
     * Marks the appointment as paid by changing the billing status.
     */
    public void setBillingStatus(BillingStatus status) {
        this.billingStatus = status;
    }

    /**
     * Converts the appointment outcome data into a CSV record.
     * 
     * @return A string array representing the appointment outcome as a CSV record
     */
    @Override
    public String[] toCSVRecord() {
        // Convert lists to comma-separated strings
        String medications = medicationId != null ? String.join(",", medicationId) : "";
        String medicationFees = medicationFee != null ? medicationFee.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(",")) : "";

        // Format the CSV record
        return new String[] {
                String.valueOf(getAppointmentId()), // Convert appointmentId to String
                getServiceType(),
                medications,
                getConsultationNotes(),
                getStatus().toString(), // Convert the PrescriptionStatus enum to String
                String.format("%.2f", getConsultationFee()), // Format consultationFee to 2 decimal places
                medicationFees,
                String.format("%.2f", getTotalAmount()), // Format totalAmount to 2 decimal places
                getBillingStatus().toString() // Convert BillingStatus enum to String
        };
    }

    /**
     * Returns a string representation of the appointment outcome.
     * 
     * @return A string representing the appointment outcome
     */
    @Override
    public String toString() {
        return "AppointmentOutcome{" +
                "appointmentId=" + appointmentId +
                ", serviceType='" + serviceType + '\'' +
                ", medicationId=" + medicationId +
                ", consultationNotes=" + consultationNotes +
                ", status=" + status +
                ", consultationFee=" + consultationFee +
                ", medicationFee=" + medicationFee +
                ", totalAmount=" + totalAmount +
                ", billingStatus=" + billingStatus +
                '}';
    }
}
