package model.Users;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.time.LocalDate;
import java.time.LocalTime;

import model.ICSVExportable;
import utils.Utility.CSVWriter;

/**
 * Represents a Doctor user, extending the {@link User} class.
 * The Doctor class contains details specific to a doctor, including a list of patient IDs 
 * and the monthly availability of the doctor, and implements the {@link ICSVExportable} 
 * interface for exporting doctor information to CSV format.
 */
public class Doctor extends User implements ICSVExportable {
    
    private List<String> patientIds = new ArrayList<String>();
    private Map<LocalDate, List<LocalTime>> monthlyAvailability = new HashMap<>();

    /**
     * Constructs a Doctor object with the specified details.
     * 
     * @param staffId       The unique staff ID of the doctor.
     * @param name          The full name of the doctor.
     * @param gender        The gender of the doctor.
     * @param username      The username for the doctor's account.
     * @param password      The password for the doctor's account.
     * @param dateOfBirth   The date of birth of the doctor.
     * @param contactNumber The contact number of the doctor.
     * @param emailAddress  The email address of the doctor.
     * @param patientIds    A list of patient IDs associated with the doctor.
     */
    public Doctor(String staffId,
            String name,
            String gender,
            String username,
            String password,
            LocalDate dateOfBirth,
            String contactNumber,
            String emailAddress,
            List<String> patientIds) {
        super(staffId, name, gender, username, password, dateOfBirth, contactNumber, emailAddress);
        this.patientIds = patientIds;
    }

    /**
     * Converts the Doctor object to a CSV-compatible record.
     * This method implements the {@link ICSVExportable#toCSVRecord()} interface
     * and returns a string array that represents the doctor's details.
     * 
     * @return A string array representing the Doctor's data in CSV format.
     */
    @Override
    public String[] toCSVRecord() {
        // Convert patientIds list to a comma-separated string
        String patientIdsString = patientIds != null ? String.join(";", patientIds) : "";

        return new String[] {
                getId(), // Staff ID
                getName(), // Name
                getGender(), // Gender
                getUsername(), // Username
                getPassword(), // Password
                getDateOfBirth() != null ? getDateOfBirth().toString() : "", // Date of Birth
                getContactNumber(), // Contact Number
                getEmailAddress(), // Email Address
                "", // License Number (empty for Doctor)
                patientIdsString // Patient IDs (list converted to string)
        };
    }

    /**
     * Gets the list of patient IDs associated with the doctor.
     * 
     * @return A list of patient IDs.
     */
    public List<String> getPatientIds() {
        return patientIds;
    }

    /**
     * Gets the monthly availability of the doctor, mapping dates to available times.
     * 
     * @return A map where the key is a {@link LocalDate} and the value is a list of available {@link LocalTime} slots.
     */
    public Map<LocalDate, List<LocalTime>> getMonthlyAvailability() {
        return monthlyAvailability;
    }

    /**
     * Sets the doctor's monthly availability and optionally saves it to a CSV file.
     * 
     * @param monthlyAvailability A map of the doctor's availability for the month, mapping dates to available times.
     * @param init If true, it initializes the availability; if false, it writes the availability to a CSV file.
     * @return A message indicating the result of setting the availability.
     */
    public String setMonthlyAvailability(Map<LocalDate, List<LocalTime>> monthlyAvailability, Boolean init) {
        String path = "Hospital/src/resources/CSV/Availability.csv";
    
        try {
            this.monthlyAvailability = monthlyAvailability;
    
            // Save to CSV if not initializing
            if (!init) {
                CSVWriter.writeAvailabilityToCSV(path, this.getId(), monthlyAvailability);
            }
    
            return "Monthly availability set successfully.";
        } catch (Exception e) {
            return "Error setting monthly availability: " + e.getMessage();
        }
    }    
}
