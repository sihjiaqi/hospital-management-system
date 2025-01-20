package model.Users;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import model.ICSVExportable;
import model.Appointment.AppointmentOutcome;
import model.Appointment.AppointmentOutcome.PrescriptionStatus;
import model.Medication.Medication;
import model.Medication.ReplenishRequest;
import model.Medication.ReplenishRequest.ReplenishRequestStatus;
import repository.Appointment.AppointmentOutcomeDB;
import repository.Medication.MedicationDB;
import repository.Medication.ReplenishRequestDB;

/**
 * Represents a Pharmacist user, extending the {@link User} class.
 * The Pharmacist class includes specific responsibilities and functionality related to prescription management, 
 * medication inventory, and replenishment requests. It implements the {@link ICSVExportable} interface to allow 
 * exporting pharmacist data to CSV format.
 */
public class Pharmacist extends User implements ICSVExportable {
    
    private String licenseNumber;

    /**
     * Constructs a Pharmacist object with the specified details.
     * 
     * @param staffId       The unique staff ID of the pharmacist.
     * @param name          The full name of the pharmacist.
     * @param gender        The gender of the pharmacist.
     * @param username      The username for the pharmacist's account.
     * @param password      The password for the pharmacist's account.
     * @param dateOfBirth   The date of birth of the pharmacist.
     * @param contactNumber The contact number of the pharmacist.
     * @param emailAddress  The email address of the pharmacist.
     * @param licenseNumber The license number of the pharmacist.
     */
    public Pharmacist(String staffId,
                      String name,
                      String gender,
                      String username,
                      String password,
                      LocalDate dateOfBirth,
                      String contactNumber,
                      String emailAddress,
                      String licenseNumber) {
        super(staffId, name, gender, username, password, dateOfBirth, contactNumber, emailAddress);
        this.licenseNumber = licenseNumber;
    }

    /**
     * Converts the Pharmacist object to a CSV-compatible record.
     * This method implements the {@link ICSVExportable#toCSVRecord()} interface
     * and returns a string array that represents the pharmacist's details.
     * 
     * @return A string array representing the Pharmacist's data in CSV format.
     */
    @Override
    public String[] toCSVRecord() {
        return new String[] {
            getId(), // Staff ID
            getName(), // Name
            getGender(), // Gender
            getUsername(), // Username
            getPassword(), // Password
            getDateOfBirth() != null ? getDateOfBirth().toString() : "", // Date of Birth
            getContactNumber(), // Contact Number
            getEmailAddress(), // Email Address
            licenseNumber, // License Number (specific to Pharmacist)
            "" // Patient IDs (empty for Pharmacist)
        };
    }

    // ----------------------------------------------------------------
    // Prescription Management
    // ----------------------------------------------------------------
    /**
     * Views the outcome of an appointment using the appointment ID.
     * 
     * @param appointmentId The ID of the appointment.
     * @return The outcome of the appointment.
     */
    public AppointmentOutcome viewAppointmentOutcome(int appointmentId) {
        return AppointmentOutcomeDB.getAppointmentOutcome(appointmentId);
    }

    /**
     * Views appointment outcomes filtered by prescription status.
     * 
     * @param status The prescription status to filter by.
     * @return A list of appointment outcomes matching the specified status.
     */
    public List<AppointmentOutcome> viewAppointmentOutcomeByStatus(String status) {
        PrescriptionStatus prescriptionStatus = PrescriptionStatus.valueOf(status.toUpperCase());
        return AppointmentOutcomeDB.getAppointmentOutcomesByStatus(prescriptionStatus);
    }

    /**
     * Updates the prescription status of an appointment.
     * 
     * @param appointmentId The ID of the appointment to update.
     * @param status        The new status of the prescription.
     */
    public void updatePrescriptionStatus(int appointmentId, String status) {
        PrescriptionStatus prescriptionStatus = PrescriptionStatus.valueOf(status.toUpperCase());
        AppointmentOutcomeDB.updatePrescriptionStatus(appointmentId, prescriptionStatus, false);
    }

    // ----------------------------------------------------------------
    // Medication Management
    // ----------------------------------------------------------------
    /**
     * Views the entire medication inventory, including tracking stock levels.
     * 
     * @return A map of medication IDs to Medication objects representing the entire inventory.
     */
    public HashMap<String, Medication> viewAllMedicationInventory() {
        return MedicationDB.getAllMedications();
    }

    /**
     * Views medication details based on the medication ID.
     * 
     * @param medicineId The ID of the medication to view.
     */
    public void viewMedicationInventoryByName(String medicineId) {
        MedicationDB.findMedicationByName(medicineId);
    }

    /**
     * Submits a replenishment request when stock levels of a medication are low.
     * 
     * @param staffId     The staff ID of the pharmacist submitting the request.
     * @param medicationId The ID of the medication needing replenishment.
     * @param status       The status of the replenishment request.
     * @param amount       The amount of medication needed for replenishment.
     */
    public void submitReplenishmentRequest(String staffId, String medicationId, String status, int amount) {
        ReplenishRequestStatus replenishRequestStatus = ReplenishRequestStatus.valueOf(status);
        ReplenishRequest replenishRequest = new ReplenishRequest(staffId, medicationId, replenishRequestStatus, amount,
                LocalDate.now());
        ReplenishRequestDB.addReplenishRequest(replenishRequest, false);
    }

    /**
     * Returns a string representation of the Pharmacist object.
     * The string includes the pharmacist's license number and all details from the super class {@link User}.
     * 
     * @return A string representation of the Pharmacist object.
     */
    @Override
    public String toString() {
        return "\nPharmacist{" +
               "licenseNumber='" + licenseNumber + '\'' +
                ", " + super.toString() + // Call the parent's toString
                '}';
    }

    /**
     * Sets the prescription status for an appointment (logic to be implemented).
     * 
     * @param appointmentId The ID of the appointment for which to set the prescription status.
     */
    public void setPrescriptionStatus(int appointmentId) {
        // Insert logic for setting prescription status here.
        System.out.println("Prescription status for " + appointmentId + " has been changed from ");
    }

    /**
     * Requests replenishment for a medication when stock levels are low (logic to be implemented).
     * 
     * @param medicatId The ID of the medication for which to request replenishment.
     */
    public void requestReplenishment(int medicatId) {
        // Insert logic to send request for replenishment to Administrator here
        System.out.println("Request sent");
    }

    /**
     * Gets the license number of the pharmacist.
     * 
     * @return The license number of the pharmacist.
     */
    public String getLicenseNum() {
        return licenseNumber;
    }
}
