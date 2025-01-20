package repository.Users;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import model.Users.Admin;
import model.Users.Doctor;
import model.Users.Pharmacist;
import model.Users.User;
import utils.Utility.CSVWriter;
import utils.Utility.Printer;
import utils.Utility.SimilarityCalculator;

/**
 * The StaffDB class manages a database of staff members (e.g., Admins, Doctors, Pharmacists).
 * It provides methods for adding, updating, retrieving, and removing staff members, as well as persisting data to a CSV file.
 * <p>
 * This class extends {@code UserDB<User>}.
 * </p>
 */
public class StaffDB extends UserDB<User> {
    /**
     * A static HashMap to store the staff members.
     * 
     * This map holds the staff members, using the staff ID (a string) as the key and the corresponding 
     * `User` object (which could represent staff such as pharmacists, doctors, and admins) as the value. 
     * It provides efficient management and retrieval of staff information.
     */
    private static HashMap<String, User> staffDB = new HashMap<>();

    /**
     * A static variable to count the number of staff members.
     * 
     * This variable tracks the total number of staff members currently stored in the `staffDB` map.
     */
    public static int staffCount = 0;

    /**
     * Path to the CSV file that stores the staff members' data.
     * 
     * This file contains the details of the staff members, including their ID, name, gender, username, 
     * password, date of birth, contact number, email address, license number, and associated patient IDs.
     */
    public static String path = "Hospital/src/resources/CSV/Staff_List.csv";

    /**
     * The headers for the CSV file representing the staff list.
     */
    public static String[] headers = {
            "Staff ID",
            "Name",
            "Gender",
            "Username",
            "Password",
            "DateOfBirth",
            "ContactNumber",
            "Email Address",
            "License Number",
            "PatientIds"
    };

    /**
     * Retrieves the hashmap of all staff members.
     *
     * @return a hashmap of staff members keyed by their staff ID.
     */
    public static HashMap<String, User> getStaff() {
        return staffDB;
    }

    // Add a new staff member to the database
    /**
     * Adds a new staff member to the database.
     *
     * @param user the staff member to add.
     * @param init a flag indicating if the addition is during initialization (does not write to CSV).
     * @return a message indicating success or failure.
     */
    public static String addUser(User user, boolean init) {
        try {
            String userId = user.getId(); // Assuming User has getId method
            // Check if the staff member already exists
            if (!staffDB.containsKey(userId)) {
                staffDB.put(userId, user);
                staffCount++;

                // Write to CSV if not during initialization
                if (!init) {
                    CSVWriter.writeCSV(path, staffDB, headers); // Assuming Util.writeCSV handles staffDB
                }
                return "Staff member added successfully with ID: " + userId;
            } else {
                return "Staff member with ID " + userId + " already exists.";
            }
        } catch (Exception e) {
            return "Error adding staff member: " + e.getMessage();
        }
    }

    // Generic method to get users by role
    /**
     * Retrieves a list of users by their role (e.g., Admin, Doctor, Pharmacist).
     *
     * @param roleClass the class of the role to filter by.
     * @param <T>       the type of the role.
     * @return a list of users of the specified role.
     */
    public static <T extends User> List<T> getUsersByRole(Class<T> roleClass) {
        List<T> usersByRole = new ArrayList<>();
        for (User user : staffDB.values()) {
            if (roleClass.isInstance(user)) {
                usersByRole.add(roleClass.cast(user));
            }
        }
        return usersByRole;
    }

    // Get all staff (Admins, Doctors, Pharmacists)
    /**
     * Retrieves a list of all staff members.
     *
     * @return a list of all staff members.
     */
    public static List<User> getAllStaffs() {
        List<User> allUsers = new ArrayList<>();
        allUsers.addAll(getUsersByRole(Admin.class));
        allUsers.addAll(getUsersByRole(Doctor.class));
        allUsers.addAll(getUsersByRole(Pharmacist.class));
        return allUsers;
    }

    // Safe method to get a user by their ID
    /**
     * Retrieves a staff member by their ID.
     *
     * @param userId the ID of the staff member to retrieve.
     * @return the staff member with the specified ID, or {@code null} if not found.
     */
    public static User getUserById(String userId) {
        for (User user : staffDB.values()) {
            if (user.getId().equals(userId)) {
                return user;
            }
        }
        return null;
    }

    // Remove a staff member by ID
    /**
     * Removes a staff member from the database by their ID.
     *
     * @param staffId the ID of the staff member to remove.
     * @return a message indicating success or failure.
     */
    public static String removeUser(String staffId) {
        try {
            // Check if the staff member exists
            if (staffDB.containsKey(staffId)) {
                staffDB.remove(staffId);
                staffCount--;

                // Update the CSV
                CSVWriter.writeCSV(path, staffDB, headers);
                return "Staff member with ID " + staffId + " removed successfully.";
            } else {
                return "Staff member with ID " + staffId + " does not exist.";
            }
        } catch (Exception e) {
            return "Error removing staff member: " + e.getMessage();
        }
    }

    /**
     * Displays users with similar names to the specified name.
     *
     * @param name the name to search for similar users.
     */
    public static void displaySimilarUsers(String name) {
        HashMap<String, User> similarUsers = SimilarityCalculator.findSimilarObjects(name, staffDB, "getName", 0.65);

        if (similarUsers.isEmpty()) {
            System.out.println("These are the similar users found.");
            // Printer.printTable(staffDB, "GeneralStaff");
        } else {
            Printer.printTable(similarUsers, "GeneralStaff");
        }
    }

    /**
     * Retrieves a staff member by their contact number.
     *
     * @param contactNum the username of the staff member to retrieve.
     * @return the staff member with the specified username, or {@code null} if not found.
     */
    public static User getUserByContactNum(String contactNum) {
        for (User user : staffDB.values()) {
            if (user.getContactNumber().equals(contactNum)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Retrieves a staff member by their username.
     *
     * @param username the contact number of the staff member to retrieve.
     * @return the staff member with the specified contact number, or {@code null} if not found.
     */
    public static User getUserByUsername(String username) {
        for (User user : staffDB.values()) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Retrieves a staff member by their email address.
     *
     * @param email the email address of the staff member to retrieve.
     * @return the staff member with the specified email, or {@code null} if not found.
     */
    public static User getUserByEmail(String email) {
        for (User user : staffDB.values()) {
            if (user.getEmailAddress().equals(email)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Retrieves a pharmacist by their license number.
     *
     * @param licenseNum the license number of the pharmacist to retrieve.
     * @return the pharmacist with the specified license number, or {@code null} if not found.
     */
    public static User getUserByLicenseNum(String licenseNum) {
        for (User user : staffDB.values()) {
            // Check if user is an instance of Pharmacist
            if (user instanceof Pharmacist) {
                // Cast user to Pharmacist and compare license number
                Pharmacist pharmacist = (Pharmacist) user;
                if (pharmacist.getLicenseNum().equals(licenseNum)) {
                    return pharmacist; // Return the pharmacist if license numbers match
                }
            }
        }
        return null; // Not found
    }

    /**
     * Retrieves a staff member by their name.
     *
     * @param name the name of the staff member to retrieve.
     * @return the staff member with the specified name, or {@code null} if not found.
     */
    public static User getByName(String name) {
        for (User user : staffDB.values()) {
            if (user.getName().equals(name)) { // Assuming User has a getUsername() method
                return user;
            }
        }
        return null; // Not found
    }

    // Update user information in the database
    /**
     * Updates the information of a staff member in the database.
     *
     * @param oldId       the current ID of the staff member.
     * @param newId       the new ID for the staff member (optional, can be null or empty).
     * @param updatedUser the updated staff member object.
     * @return a message indicating success or failure.
     */
    public static String updateUserInfo(String oldId, String newId, User updatedUser) {
        try {
            // Check if the staff member exists in the database
            if (staffDB.containsKey(oldId)) {
                User existingUser = staffDB.get(oldId);

                // Update fields from the updatedUser object
                existingUser.setName(updatedUser.getName());
                existingUser.setGender(updatedUser.getGender());
                existingUser.setAge(updatedUser.getAge());
                existingUser.setUsername(updatedUser.getUsername());
                existingUser.setPassword(updatedUser.getPassword());
                existingUser.setContactNumber(updatedUser.getContactNumber());
                existingUser.setEmailAddress(updatedUser.getEmailAddress());
                existingUser.setDateOfBirth(updatedUser.getDateOfBirth());

                // Handle ID change
                if (newId != null && !newId.isEmpty() && !newId.equals(oldId)) {
                    // Check if the new ID already exists
                    if (staffDB.containsKey(newId)) {
                        return "The new user ID (" + newId + ") already exists. Please choose another one.";
                    }

                    // Remove the old entry and add the user under the new ID
                    staffDB.remove(oldId);
                    existingUser.setID(newId);
                    staffDB.put(newId, existingUser);
                } else {
                    // Keep the same ID and update the record
                    staffDB.put(oldId, existingUser);
                }

                // Update the CSV
                CSVWriter.writeCSV(path, staffDB, headers);
                return "Staff information updated successfully.";
            } else {
                return "Staff member with ID " + oldId + " does not exist.";
            }
        } catch (Exception e) {
            return "Error updating staff member: " + e.getMessage();
        }
    }
}
