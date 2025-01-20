package repository.Users;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import model.Users.Patient;
import model.Users.User;
import utils.Utility.CSVWriter;
import utils.Utility.Printer;

/**
 * The PatientDB class manages a database of Patient objects.
 * It provides functionality to add, update, retrieve, and remove patients, 
 * as well as persist data to a CSV file.
 * <p>
 * This class extends {@code UserDB<Patient>}.
 * </p>
 */
public class PatientDB extends UserDB<Patient> {
    /**
     * A collection of patients in the hospital system.
     * 
     * This HashMap stores patient objects, with the patient's unique ID as the key. It provides access to 
     * patient records and allows for management of patient data.
     */
    private static HashMap<String, Patient> patients = new HashMap<>();

    /**
     * The file path where the patient data is stored in CSV format.
     * 
     * This path points to the location of the `Patient_List.csv` file, which contains the list of patients and 
     * their respective details, such as ID, name, contact information, and medical records.
     */
    public static String path = "Hospital/src/resources/CSV/Patient_List.csv";

    /**
     * The total number of patients in the system.
     * 
     * This variable keeps track of the total count of patients currently stored in the system. It is updated 
     * whenever a new patient is added or removed from the system.
     */
    public static int patientCount = 0;

    /**
     * The header row used in the CSV file for patient data.
     * 
     * This array defines the column headers for the CSV file representing patient information, such as 
     * Patient ID, Name, Gender, Username, Password, Date of Birth, Contact Number, Contact Information, and Blood Type.
     */
    public static String[] headers = {
            "Patient ID",
            "Name",
            "Gender",
            "Username",
            "Password",
            "Date of Birth",
            "Contact Number",
            "Contact Information",
            "Blood Type"
    };

    /**
     * Returns the hashmap of patients.
     *
     * @return a hashmap of patients keyed by patient ID.
     */
    public static HashMap<String, Patient> getPatients() {
        return patients;
    }

    /**
     * Retrieves a user by their email address.
     *
     * @param email the email address of the user to retrieve.
     * @return the user with the specified email, or {@code null} if no such user exists.
     */
    public static User getUserByEmail(String email) {
        for (User user : patients.values()) {
            if (user.getEmailAddress() == null) {
                if (user.getEmailAddress().equals(email)) { // Assuming User has a getUsername() method
                    return user;
                }
            } else {
                return null;
            }
        }
        return null;
    }

    // Add a new patient to the database
    /**
     * Adds a new patient to the database.
     *
     * @param patient the patient to add.
     * @param init    a flag indicating whether this is an initialization operation (does not write to CSV).
     * @return a message indicating success or failure.
     */
    public static String addUser(Patient patient, boolean init) {
        try {
            String patientId = patient.getId(); // Assuming Patient has getId method

            // Check if the patient ID already exists
            if (!patients.containsKey(patientId)) {
                patients.put(patientId, patient);
                patientCount++;

                // Write to CSV if not initializing
                if (!init) {
                    CSVWriter.writeCSV(path, patients, headers);
                }
                return "Patient added successfully with ID: " + patientId;
            } else {
                return "Patient with ID " + patientId + " already exists.";
            }
        } catch (Exception e) {
            return "Error adding patient: " + e.getMessage();
        }
    }

    // Update user information in the database
    /**
     * Updates the information of an existing patient.
     *
     * @param oldId       the current ID of the patient.
     * @param newId       the new ID for the patient (optional, can be null or empty).
     * @param updatedUser the updated patient object.
     * @return a message indicating success or failure.
     */
    public static String updateUserInfo(String oldId, String newId, Patient updatedUser) {
        try {
            if (patients.containsKey(oldId)) {
                Patient existingUser = patients.get(oldId);
    
                existingUser.setName(updatedUser.getName());
                existingUser.setGender(updatedUser.getGender());
                existingUser.setAge(updatedUser.getAge());
                existingUser.setUsername(updatedUser.getUsername());
                existingUser.setPassword(updatedUser.getPassword());
                existingUser.setContactNumber(updatedUser.getContactNumber());
                existingUser.setEmailAddress(updatedUser.getEmailAddress());
                existingUser.setDateOfBirth(updatedUser.getDateOfBirth());
    
                // ID change handling
                if (newId != null && !newId.isEmpty() && !newId.equals(oldId)) {
                    if (patients.containsKey(newId)) {
                        return "The new ID (" + newId + ") already exists. Please choose another one.";
                    }
    
                    patients.remove(oldId);
                    existingUser.setID(newId);
                    patients.put(newId, existingUser);
                } else {
                    patients.put(oldId, existingUser);
                }
    
                CSVWriter.writeCSV(path, patients, headers);
                return "User information updated successfully for ID: " + oldId;
            } else {
                return "User with ID " + oldId + " does not exist.";
            }
        } catch (Exception e) {
            return "Error updating user information: " + e.getMessage();
        }
    }
    
    /**
     * Prints all patients in the database to the console.
     */
    public static void printAllUsers() {
        Printer.printTable(patients, "PatientDB");
    }

    /**
     * Retrieves a patient by their ID.
     *
     * @param patientId the ID of the patient to retrieve.
     * @return the patient with the specified ID, or {@code null} if no such patient exists.
     */
    public static Patient getUserById(String patientId) {
        return patients.get(patientId);
    }

    /**
     * Retrieves a patient by their username.
     *
     * @param username the username of the patient to retrieve.
     * @return the patient with the specified username, or {@code null} if no such patient exists.
     */
    public static Patient getUserByUsername(String username) {
        for (Patient patient : patients.values()) {
            // Check if the patient's username is not null before comparing
            if (patient.getUsername() != null && patient.getUsername().equals(username)) {
                return patient; // Return the patient if the usernames match
            }
        }
        return null; // Return null if no patient found with the given username
    }
    
    /**
     * Retrieves a patient by their contact number.
     *
     * @param contactNum the contact number of the patient to retrieve.
     * @return the patient with the specified username, or {@code null} if no such patient exists.
     */
    public static Patient getUserByContactNum(String contactNum) {
        for (Patient patient : patients.values()) {
            // Check if the patient's contact number is not null before comparing
            if (patient.getContactNumber() != null && patient.getContactNumber().equals(contactNum)) {
                return patient; // Return the patient if the contact numbers match
            }
        }
        return null; // Return null if no patient found with the given contact number
    }
    

    // Remove a patient from the database
    /**
     * Removes a patient from the database by their ID.
     *
     * @param patientId the ID of the patient to remove.
     * @return a message indicating success or failure.
     */
    public static String removeUser(String patientId) {
        try {
            // Check if the patient exists
            if (patients.containsKey(patientId)) {
                patients.remove(patientId);
                patientCount--;
                CSVWriter.writeCSV(path, patients, headers);
                return "Patient with ID " + patientId + " removed successfully.";
            } else {
                return "Patient with ID " + patientId + " does not exist.";
            }
        } catch (Exception e) {
            return "Error removing patient: " + e.getMessage();
        }
    }

    /**
     * Retrieves a list of all patients in the database.
     *
     * @return a list of all patients.
     */
    public static List<User> getAllUsers() {
        return new ArrayList<>(patients.values());
    }


}
