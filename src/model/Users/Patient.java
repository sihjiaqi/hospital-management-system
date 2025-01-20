package model.Users;

import java.time.LocalDate;

import model.ICSVExportable;

/**
 * Represents a Patient user, extending the {@link User} class.
 * The Patient class includes specific details related to a patient, such as their blood type,
 * and implements the {@link ICSVExportable} interface to allow exporting patient data to CSV format.
 */
public class Patient extends User implements ICSVExportable {
    /**
     * Represents the blood type of an individual.
     */
    public String bloodType;

    /**
     * Constructs a Patient object with the specified details.
     * 
     * @param patientID     The unique patient ID.
     * @param name          The full name of the patient.
     * @param gender        The gender of the patient.
     * @param username      The username for the patient's account.
     * @param password      The password for the patient's account.
     * @param dateOfBirth   The date of birth of the patient.
     * @param contactNumber The contact number of the patient.
     * @param emailAddress  The email address of the patient.
     * @param bloodType     The blood type of the patient.
     */
    public Patient(String patientID,
            String name,
            String gender,
            String username,
            String password,
            LocalDate dateOfBirth,
            String contactNumber,
            String emailAddress,
            String bloodType) {
        super(patientID, name, gender, username, password, dateOfBirth, contactNumber, emailAddress);
        this.bloodType = bloodType;
    }

    /**
     * Sets the blood type for the patient.
     * 
     * @param bloodType The blood type to be set for the patient.
     */
    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    /**
     * Gets the blood type of the patient.
     * 
     * @return The blood type of the patient.
     */
    public String getBloodType() {
        return bloodType;
    }

    /**
     * Converts the Patient object to a CSV-compatible record.
     * This method implements the {@link ICSVExportable#toCSVRecord()} interface
     * and returns a string array that represents the patient's details.
     * 
     * @return A string array representing the Patient's data in CSV format.
     */
    @Override
    public String[] toCSVRecord() {
        return new String[] {
                getId(),
                getName(),
                getGender(),
                getUsername(),
                getPassword(),
                getDateOfBirth() != null ? getDateOfBirth().toString() : "",
                getContactNumber(),
                getEmailAddress(),
                bloodType
        };
    }

    /**
     * Returns a string representation of the Patient object.
     * The string includes the patient's blood type and all details from the super class {@link User}.
     * 
     * @return A string representation of the Patient object.
     */
    @Override
    public String toString() {
        return "\nPatient{" +
                "bloodType='" + bloodType + '\'' +
                ", " + super.toString() +
                '}';
    }
}
