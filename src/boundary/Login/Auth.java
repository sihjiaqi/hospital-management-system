package boundary.Login;

import model.Users.Patient;
import model.Users.User;
import repository.Users.PatientDB;
import repository.Users.StaffDB;

/**
 * The Auth class provides static methods for user authentication in a system.
 * It allows users to log in as either a staff member or a patient by verifying their
 * username and password against corresponding databases (StaffDB and PatientDB).
 * 
 * The class contains the following login methods:
 * <ul>
 *   <li><b>staffLogin:</b> Authenticates a staff member using their username and password.</li>
 *   <li><b>patientLogin:</b> Authenticates a patient using their username and password.</li>
 * </ul>
 * Each method checks the appropriate database for the provided username and verifies
 * the password. If successful, it returns the corresponding user object (either
 * a staff member or a patient); otherwise, it returns null.
 */
public class Auth {

    /**
     * Registers a new user with the given username and password.
     * This method is currently under development and does not perform any
     * actual registration logic. It simply prints a message indicating
     * that it is in the development phase.
     *
     * @param username The username of the user to be registered.
     * @param password The password for the user account.
     */
    public void registerUser(String username, String password) {
        System.out.println("in dev");
    }

    /**
     * Authenticates a user by their username and password, checking first in the staff database,
     * then in the patient database.
     * 
     * If the user is found in the staff database, their password is validated and the corresponding
     * staff member is returned if the password matches.
     * If the user is found in the patient database, their password is validated and the corresponding
     * patient is returned if the password matches.
     * 
     * If the username is not found in either database, or if the password does not match, 
     * the method returns null to indicate a failed login attempt.
     *
     * @param username The username of the user attempting to log in.
     * @param password The password provided by the user attempting to log in.
     * @return The corresponding User object (either a staff member or a patient) if authentication is successful, 
     *         or null if the username is not found or the password is incorrect.
     */
    public static User login(String username, String password) {
        User staffMember = StaffDB.getUserByUsername(username);
        if (staffMember != null) {
            if (staffMember.getPassword().equals(password)) {
                return staffMember; // Login successful for staff
            } else {
                return null; 
            }
        }

        // Check in PatientDB
        Patient patient = PatientDB.getUserByUsername(username);
        if (patient != null) {
            // Check the password (assuming a method `getPassword()` exists)
            if (patient.getPassword().equals(password)) {
                return patient; // Login successful for patient
            } else {
                return null; // Incorrect password for patient
            }
        }
        // If not found in either DB
        return null; // Login failed
    }

    /**
     * Authenticates a staff member by their username and password.
     * This method checks the staff database to find a user by the given username.
     * If the staff member is found and their password matches the provided password,
     * the staff member object is returned, indicating a successful login.
     * If the username is not found or the password does not match, the method returns null.
     *
     * @param username The username of the staff member attempting to log in.
     * @param password The password provided by the staff member attempting to log in.
     * @return The corresponding User object (staff member) if authentication is successful, 
     *         or null if the username is not found or the password is incorrect.
     */
    public static User staffLogin(String username, String password) {
        User staffMember = StaffDB.getUserByUsername(username);
        if (staffMember != null) {
            if (staffMember.getPassword().equals(password)) {
                return staffMember;
            }
        }

        return null;
    }

    /**
     * Authenticates a patient by their username and password.
     * This method checks the patient database to find a user by the given username.
     * If the patient is found and their password matches the provided password,
     * the patient object is returned, indicating a successful login.
     * If the username is not found or the password does not match, the method returns null.
     *
     * @param username The username of the patient attempting to log in.
     * @param password The password provided by the patient attempting to log in.
     * @return The corresponding Patient object if authentication is successful, 
     *         or null if the username is not found or the password is incorrect.
     */
    public static User patientLogin(String username, String password) {
        Patient patient = PatientDB.getUserByUsername(username);

        if (patient != null) {
            if (patient.getPassword().equals(password)) {
                return patient;
            }
        }

        return null;
    }
}
