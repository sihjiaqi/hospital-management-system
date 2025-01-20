package repository.Appointment;

import java.util.List;
import java.security.Provider.Service;
import java.util.ArrayList;
import java.util.HashMap;

import model.Appointment.AppointmentOutcome;
import model.Appointment.AppointmentOutcome.BillingStatus;
import model.Appointment.AppointmentOutcome.PrescriptionStatus;
import model.Medication.Medication;
import utils.Utility.CSVWriter;

/**
 * The AppointmentOutcomeDB class provides methods for managing appointment outcomes.
 * These outcomes are stored in a HashMap and can be persisted to a CSV file.
 * The class includes methods for adding, removing, updating, and retrieving appointment outcomes.
 */
public class AppointmentOutcomeDB {
    /**
     * A HashMap to store appointment outcomes.
     * <p>
     * This map uses the AppointmentId (Integer) as the key and the corresponding {@link AppointmentOutcome} object as the value.
     * The map provides a way to efficiently retrieve and manage appointment outcomes by their ID.
     * </p>
     */
    public static HashMap<Integer, AppointmentOutcome> appointmentOutcomes = new HashMap<>();

    /**
     * The file path to the CSV file containing the list of appointment outcomes.
     * <p>
     * This path is used to locate the external CSV file where appointment outcomes are stored and managed.
     * </p>
     */
    public static String path = "Hospital/src/resources/CSV/AppointmentOutcome_List.csv";

    /**
     * The headers for the appointment outcomes CSV file.
     * <p>
     * These headers are used to define the structure of the CSV file and to correctly map the columns to the respective fields in the {@link AppointmentOutcome} objects.
     * </p>
     */
    public static String[] headers = {
        "AppointmentId",
        "Service Type", 
        "MedicationId",
        "Consultation Notes",
        "Status",
        "Medication Price",
        "Service Fee",
        "Payment Status"
    };

    // Add an appointment outcome to the HashMap
    /**
     * Adds an appointment outcome to the database.
     *
     * @param appointmentOutcome the AppointmentOutcome object to be added.
     * @param init specifies whether the method is being called during initialization.
     * @return true if the appointment outcome is successfully added, false otherwise.
     */
    public static Boolean addAppointmentOutcome(AppointmentOutcome appointmentOutcome, Boolean init) {
        try {
            appointmentOutcomes.put(appointmentOutcome.getAppointmentId(), appointmentOutcome);
            if (!init) {
                CSVWriter.writeCSV(path, appointmentOutcomes, headers);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Remove an appointment outcome by appointmentId
    /**
     * Removes an appointment outcome from the database by appointment ID.
     *
     * @param appointmentId the ID of the appointment outcome to be removed.
     * @return a message indicating success or failure of the operation.
     */
    public static String removeAppointmentOutcome(int appointmentId) {
        try {
            if (appointmentOutcomes.containsKey(appointmentId)) {
                appointmentOutcomes.remove(appointmentId);
                return "Appointment outcome removed successfully for ID: " + appointmentId;
            } else {
                return "Error: Appointment outcome not found for ID: " + appointmentId;
            }
        } catch (Exception e) {
            return "Error removing appointment outcome: " + e.getMessage();
        }
    }

    // Update prescription status (by pharmacist)
    /**
     * Updates the prescription status of an appointment outcome.
     *
     * @param appointmentId the ID of the appointment whose status needs to be updated.
     * @param status the new prescription status to set.
     * @param init specifies whether the method is being called during initialization.
     * @return a message indicating success or failure of the operation.
     */
    public static String updatePrescriptionStatus(int appointmentId, PrescriptionStatus status, Boolean init) {
        try {
            if (appointmentOutcomes.containsKey(appointmentId)) {
                AppointmentOutcome outcome = appointmentOutcomes.get(appointmentId);
                outcome.setStatus(status);
                appointmentOutcomes.put(appointmentId, outcome);
                if (!init) {
                    CSVWriter.writeCSV(path, appointmentOutcomes, headers);
                }
                return "Prescription status updated successfully for appointment ID: " + appointmentId;
            } else {
                return "Error: No appointment outcome found with ID: " + appointmentId;
            }
        } catch (Exception e) {
            return "Error updating prescription status: " + e.getMessage();
        }
    }

    // Get an appointment outcome by appointmentId
    /**
     * Removes an appointment outcome from the database by appointment ID.
     *
     * @param appointmentId the ID of the appointment outcome to be removed.
     * @return a message indicating success or failure of the operation.
     */
    public static AppointmentOutcome getAppointmentOutcome(int appointmentId) {
        return appointmentOutcomes.get(appointmentId);
    }

    // Get appoinment outcomes by status
    /**
     * Retrieves a list of {@code AppointmentOutcome} objects filtered by a specified {@code PrescriptionStatus}.
     *
     * <p>This method iterates through the collection of appointment outcomes and returns 
     * only those outcomes whose status matches the specified {@code status}.
     *
     * @param status the {@code PrescriptionStatus} to filter the appointment outcomes by
     * @return a {@code List} of {@code AppointmentOutcome} objects that match the specified status
     */
    public static List<AppointmentOutcome> getAppointmentOutcomesByStatus(PrescriptionStatus status) {
        List<AppointmentOutcome> filteredOutcomes = new ArrayList<>();

        for (AppointmentOutcome outcome : appointmentOutcomes.values()) {
            if (outcome.getStatus() == status) { // Compare the status
                filteredOutcomes.add(outcome);
            }
        }
        return filteredOutcomes;
    }

    // Get all appointment outcomes as a list
    /**
     * Retrieves all appointment outcomes in the database.
     *
     * @return a HashMap containing all appointment outcomes, with appointment ID as the key.
     */
    public static HashMap<Integer, AppointmentOutcome> getAllAppointmentOutcomes() {
        return appointmentOutcomes;
    }

    /**
     * Updates the billing status of a specified appointment.
     *
     * @param appointmentId the unique ID of the appointment whose billing status is to be updated
     * @param status the new billing status to be set
     * @param init a flag indicating whether this update is an initialization process (if {@code true}, CSV writing is skipped)
     * @return a message indicating the success or failure of the operation
     *         <ul>
     *           <li>If successful, returns: "Billing status updated successfully for appointment ID: [appointmentId]"</li>
     *           <li>If no matching appointment is found, returns: "Error: No appointment outcome found with ID: [appointmentId]"</li>
     *           <li>If an exception occurs, returns: "Error updating billing status: [exception message]"</li>
     *         </ul>
     * @throws Exception if an unexpected error occurs during the update process
     */
    public static String updateBillingStatus(int appointmentId, BillingStatus status, Boolean init) {
        try {
            if (appointmentOutcomes.containsKey(appointmentId)) {
                AppointmentOutcome outcome = appointmentOutcomes.get(appointmentId);
                outcome.setBillingStatus(status);
                appointmentOutcomes.put(appointmentId, outcome);
                if (!init) {
                    CSVWriter.writeCSV(path, appointmentOutcomes, headers);
                }
                return "Billing status updated successfully for appointment ID: " + appointmentId;
            } else {
                return "Error: No appointment outcome found with ID: " + appointmentId;
            }
        } catch (Exception e) {
            return "Error updating billing status: " + e.getMessage();
        }
    }
}