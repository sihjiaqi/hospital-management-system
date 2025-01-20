package utils.Utility;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

import model.Users.Patient;
import model.Users.User;
import repository.Users.PatientDB;
import repository.Users.StaffDB;

/**
 * The {@code Validation} class provides various static utility methods for validating
 * common data inputs such as license numbers, names, emails, usernames, contact information, 
 * passwords, dates, times, and more. These validations are typically used to ensure data integrity 
 * before storing or processing user inputs.
 */
public class Validation {
    private static final String LICENSE_NUMBER_PATTERN = "^L\\d{5}$";

    // Method to validate the license number
    /**
     * Validates the format and availability of a license number.
     * 
     * @param licenseNumber The license number to be validated.
     * @return {@code true} if the license number matches the pattern and does not already exist in the system, 
     *         otherwise {@code false}.
     */
    public static boolean validateLicenseNumber(String licenseNumber) {
        // Check if the license number matches the pattern
        return Pattern.matches(LICENSE_NUMBER_PATTERN, licenseNumber)
                && StaffDB.getUserByLicenseNum(licenseNumber) == null;
    }

    // Validate that a name only contains letters and spaces, and no more than 20
    // characters
    /**
     * Validates the format and content of a name.
     * <p>
     * This method checks the following criteria:
     * </p>
     * <ul>
     *   <li>The name is not null or empty.</li>
     *   <li>The length of the name does not exceed 50 characters.</li>
     *   <li>The name contains exactly two parts (first name and last name).</li>
     *   <li>Both the first name and last name are at least 2 characters long.</li>
     *   <li>Both the first name and last name only contain alphabetic characters.</li>
     * </ul>
     * <p>
     * If the name doesn't meet these requirements, an appropriate error message is returned.
     * </p>
     * 
     * @param name The name to be validated.
     * @return A string containing an error message if the name is invalid, or null if the name is valid.
     */
    public static String validateName(String name) {
        // Check if the name is not null and not empty
        if (name == null || name.trim().isEmpty()) {
            return "Error: Name cannot be null or empty.";
        }

        // Trim leading/trailing spaces
        name = name.trim();

        // Ensure the name does not exceed 50 characters
        if (name.length() > 50) {
            return "Error: Name cannot be longer than 50 characters.";
        }

        // Split the name into first and last names based on spaces
        String[] parts = name.split("\\s+");

        // Ensure there are exactly two parts (first name and last name)
        if (parts.length != 2) {
            return "Error: Name should contain exactly a first name and a last name.";
        }

        String firstName = parts[0];
        String lastName = parts[1];

        // Check if the length of both first and last names is at least 2 characters
        if (firstName.length() < 2 || lastName.length() < 2) {
            return "Error: Both first name and last name must be at least 2 characters long.";
        }

        // Check that both first and last names only contain alphabetic characters
        if (!firstName.matches("[a-zA-Z]+") || !lastName.matches("[a-zA-Z]+")) {
            return "Error: Both first name and last name must contain only alphabetic characters.";
        }

        // If all checks pass, the name is valid
        return null; // Return null if the name is valid
    }

    // Validate that a username is no more than 20 characters
    /**
     * Validates the format and uniqueness of a username.
     * <p>
     * This method checks the following criteria:
     * </p>
     * <ul>
     *   <li>The username is between 5 to 20 characters in length.</li>
     *   <li>The username does not already exist in the respective user database based on the user type (staff or patient).</li>
     * </ul>
     * <p>
     * If the username doesn't meet these requirements, an appropriate error message is returned.
     * </p>
     * 
     * @param username The username to be validated.
     * @param userType The type of user ("staff" or "patient") to check in the appropriate database.
     * @return A string containing an error message if the username is invalid, or null if the username is valid.
     */
    public static String validateUsername(String username, String userType) {
        // Check if username is null or exceeds max length
        if (username == null || username.length() > 20 || username.length()<5) {
            return "Invalid input. Username must be between 5 to 20 characters"; // Return error message if invalid
        }

        // Check in the appropriate database based on the user type
        if ("staff".equalsIgnoreCase(userType)) {
            // Check in StaffDB (pharmacists, doctors, and admins)
            if (StaffDB.getUserByUsername(username) != null) {
                return "Invalid input. Username Taken."; // Return error if username already exists
            }
        } else if ("patient".equalsIgnoreCase(userType)) {
            // Check in PatientDB
            for (User user : PatientDB.getAllUsers()) {
                Patient patient = (Patient) user;
                if (patient.getUsername().equals(username)) {
                    return "Invalid input. Username Taken."; // Return error if username already exists
                }
            }
        }

        // If the username is valid and not already in the respective database, return
        // null (valid username)
        return null;
    }

    /**
     * Validates an email address, ensuring it matches a basic email format and is unique based on the user type.
     * 
     * @param email The email to be validated.
     * @param userType The type of user (either "staff" or "patient").
     * @param auto A flag indicating whether auto-validation is required (e.g., for registration).
     * @return {@code true} if the email is valid and unique, otherwise {@code false}.
     */
    public static boolean validateEmail(String email, String userType, boolean auto) {
        // Check if email is null or does not match the basic email format (basic regex)
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            System.out.println("Invalid input. Please enter a valid email address.");
            return false; // Invalid email if null or doesn't match the regex pattern
        }

        // Check in the appropriate database based on the user type
        if ("staff".equalsIgnoreCase(userType)) {
            // Check in StaffDB (pharmacists, doctors, and admins)
            if (StaffDB.getUserByEmail(email) != null) {
                if (!auto) {
                    System.out.println("Invalid input. Email is already taken by another staff member.");
                }
                return false; // Email already exists in the StaffDB
            }
        } else if ("patient".equalsIgnoreCase(userType)) {
            // Check in PatientDB
            if (PatientDB.getUserByEmail(email) != null) {
                if (!auto) {
                    System.out.println("Invalid input. Email is already taken by another patient.");
                }
                return false; // Email already exists in PatientDB
            }
        }

        // If the email is valid and not already in the respective database (or auto is
        // true)
        return true;
    }

    // Validate that a password is at least 3 characters long
    /**
     * Validates the strength and format of a password.
     * <p>
     * This method checks if the provided password meets the following criteria:
     * </p>
     * <ul>
     *   <li>It is not empty or null.</li>
     *   <li>The length is between 8 and 16 characters (inclusive).</li>
     *   <li>It contains at least one special character.</li>
     *   <li>It contains at least one lowercase letter.</li>
     *   <li>It contains at least one uppercase letter.</li>
     *   <li>It contains at least one numeric value.</li>
     * </ul>
     * <p>
     * If the password doesn't meet these requirements, an appropriate error message is returned.
     * </p>
     * 
     * @param password The password to be validated.
     * @return A string containing an error message if the password is invalid, or null if the password is valid.
     */
    public static String validatePassword(String password) {
        // Check if the password is not null or empty
        if (password == null || password.trim().isEmpty()) {
            return "Password cannot be empty.";
        }

        // Check the length of the password
        if (password.length() < 8 || password.length() > 16) {
            return "Password length should be between 8 and 16 characters.";
        }

        // Check if the password contains at least one special character
        if (!password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
            return "Password should contain at least one special character (e.g., !@#$%^&*).";
        }

        // Check if the password contains at least one lowercase letter
        if (!password.matches(".*[a-z].*")) {
            return "Password should contain at least one lowercase letter.";
        }

        // Check if the password contains at least one uppercase letter
        if (!password.matches(".*[A-Z].*")) {
            return "Password should contain at least one uppercase letter.";
        }

        // Check if the password contains at least one numeric value
        if (!password.matches(".*[0-9].*")) {
            return "Password should contain at least one numeric value.";
        }

        // If all conditions are met, the password is valid
        return null;
    }

    // Validate that contact information is no more than 30 characters
    /**
     * Validates the format and uniqueness of a contact number.
     * <p>
     * This method checks if the provided contact information meets the following criteria:
     * </p>
     * <ul>
     *   <li>It is not empty or null.</li>
     *   <li>The length is exactly 8 digits.</li>
     *   <li>It only contains digits.</li>
     *   <li>It starts with 6, 8, or 9 (valid prefixes for Singapore phone numbers).</li>
     *   <li>The contact number does not already exist in the patient database.</li>
     * </ul>
     * <p>
     * If the contact number doesn't meet these requirements, an appropriate error message is returned.
     * </p>
     * 
     * @param contactInfo The contact information to be validated (must be a string of digits).
     * @return A string containing an error message if the contact information is invalid, or null if the contact information is valid.
     */
    public static String validateContactInfo(String contactInfo, String userType) {
        // Check if the contactInfo is not null, is not empty, and is exactly 8 digits
        if (contactInfo == null || contactInfo.isEmpty()) {
            return "Contact info cannot be empty.";
        }
    
        // Check if the length is 8 digits
        if (contactInfo.length() != 8) {
            return "Contact info must be exactly 8 digits.";
        }
    
        // Check if the contactInfo only contains digits
        if (!contactInfo.matches("\\d{8}")) {
            return "Contact info must contain only digits.";
        }
    
        // Check if it starts with 6, 8, or 9 (valid prefixes for Singapore phone numbers)
        char firstDigit = contactInfo.charAt(0);
        if (!(firstDigit == '6' || firstDigit == '8' || firstDigit == '9')) {
            return "Contact info must start with 6, 8, or 9.";
        }
    
        // If userType is 'staff', check if the contact number exists in StaffDB
        if ("staff".equalsIgnoreCase(userType)) {
            User existingStaff = StaffDB.getUserByContactNum(contactInfo);
            if (existingStaff != null) {
                return "Contact number already exists for a staff member.";
            }
        }
        // If userType is 'patient', check if the contact number exists in PatientDB
        else if ("patient".equalsIgnoreCase(userType)) {
            Patient existingPatient = PatientDB.getUserByContactNum(contactInfo);
            if (existingPatient != null) {
                return "Contact number already exists for a patient.";
            }
        }
    
        // If all validations pass, return null (no error)
        return null;
    }
    

    

    // Validate that gender is either Male or Female
    /**
     * Validates the gender input, ensuring it is either "Male" or "Female".
     * 
     * @param gender The gender to be validated.
     * @return {@code true} if the gender is valid, otherwise {@code false}.
     */
    public static boolean validateGender(String gender) {
        return gender != null && (gender.equalsIgnoreCase("Male") || gender.equalsIgnoreCase("Female"));
    }

    // Validate that age is numeric and greater than 0
    /**
     * Validates the age input, ensuring it is numeric and greater than 0.
     * 
     * @param age The age to be validated.
     * @return {@code true} if the age is valid, otherwise {@code false}.
     */
    public static boolean validateAge(String age) {
        try {
            int ageInt = Integer.parseInt(age);
            return ageInt > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Validate blood type: A+/A-/B+/B-/O+/O-/AB+/AB-
    /**
     * Validates the blood type, ensuring it matches one of the valid types (e.g., A+/A-/B+/B-/O+/O-/AB+/AB-).
     * 
     * @param bloodType The blood type to be validated.
     * @return {@code true} if the blood type is valid, otherwise {@code false}.
     */
    public static boolean validateBloodType(String bloodType) {
        return bloodType != null && bloodType.matches("^(A|B|AB|O)[+-]$");
    }

    // Validate date (yyyy-MM-dd format)
    /**
     * Validates a date, ensuring it follows the "yyyy-MM-dd" format.
     * 
     * @param date The date to be validated.
     * @return {@code true} if the date is valid, otherwise {@code false}.
     */
    public static boolean validateDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            LocalDate.parse(date, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    // Validate time (HH:mm format)
    /**
     * Validates the time, ensuring it follows the "HH:mm" format.
     * 
     * @param time The time to be validated.
     * @return {@code true} if the time is valid, otherwise {@code false}.
     */
    public static boolean validateTime(String time) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            formatter.parse(time);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    // Validate Medication Name
    /**
     * Validates a medication name, ensuring it contains only letters and spaces, 
     * and is no longer than 50 characters.
     * 
     * @param name The medication name to be validated.
     * @return {@code true} if the name is valid, otherwise {@code false}.
     */
    public static boolean validateMedicationName(String name) {
        return name != null && name.matches("^[a-zA-Z\\s]{1,50}$");
    }

}
