package model.Users;

import java.time.LocalDate;

import model.ICSVExportable;

/**
 * Represents an Admin user, extending the {@link User} class.
 * The Admin class is used to manage staff-specific information and implements 
 * the {@link ICSVExportable} interface to allow exporting the admin's data to CSV format.
 */
public class Admin extends User implements ICSVExportable {

    /**
     * Constructs an Admin object with the specified details.
     * 
     * @param staffId       The unique staff ID of the admin.
     * @param name          The full name of the admin.
     * @param gender        The gender of the admin.
     * @param username      The username for the admin.
     * @param password      The password for the admin's account.
     * @param dateOfBirth   The date of birth of the admin.
     * @param contactNumber The contact number of the admin.
     * @param emailAddress  The email address of the admin.
     */
    public Admin(String staffId,
            String name,
            String gender,
            String username,
            String password,
            LocalDate dateOfBirth,
            String contactNumber,
            String emailAddress) {
        super(staffId, name, gender, username, password, dateOfBirth, contactNumber, emailAddress);
    }

    /**
     * Converts the Admin object to a CSV-compatible record.
     * This method implements the {@link ICSVExportable#toCSVRecord()} interface
     * and returns a string array that represents the admin's details.
     * 
     * @return A string array representing the Admin's data in CSV format.
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
            "", // License Number (empty for Admin)
            "" // Patient IDs (empty for Admin)
        };
    }
    
    
}