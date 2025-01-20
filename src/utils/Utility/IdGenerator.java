package utils.Utility;

import repository.Users.PatientDB;
import repository.Users.StaffDB;

/**
 * A utility class for generating and finding available IDs for patients and
 * staff.
 * <p>
 * This class provides methods to find available IDs based on a given prefix,
 * and to generate new IDs for either patients or staff members.
 */
public class IdGenerator {
    /**
     * Finds an available ID by iterating through possible IDs with the given
     * prefix.
     * <p>
     * This method generates IDs with a given prefix (e.g., "DOC", "p") followed by
     * a
     * 4-digit number, checks if the ID already exists in the corresponding database
     * (patient or staff), and returns the first available ID.
     * 
     * @param prefix    the prefix for the ID (e.g., "DOC" for doctor, "p" for
     *                  patient)
     * @param isPatient a boolean flag indicating whether to check for a patient
     *                  (true)
     *                  or a staff member (false)
     * @return a string representing the available ID, or {@code null} if no
     *         available ID is found
     */
    public static String findAvailId(String prefix, boolean isPatient) {
        for (int i = 1; i < 10000; i++) {
            String formattedId = String.format("%04d", i); // Formats the number as 4 digits
            String newId0 = prefix + '0' + formattedId; // Check with prefix + '0'
            String newId1 = prefix + '1' + formattedId; // Check with prefix + '1'

            // Check if the new ID already exists in the appropriate database
            if (isPatient) {
                if (PatientDB.getUserById(newId0) == null && PatientDB.getUserById(newId1) == null) { // Use PatientDB
                                                                                                      // for patients
                    return newId0; // Return the first available ID with prefix + '0'
                }

            } else {
                if (StaffDB.getUserById(newId0) == null && StaffDB.getUserById(newId1) == null) { // Use StaffDB for
                                                                                                  // staff
                    return newId0; // Return the first available ID with prefix + '0'
                }
            }
        }

        return null; // Return null if no available ID is found
    }

    /**
     * Generates a new ID based on the given prefix.
     * <p>
     * This method calls {@link #findAvailId(String, boolean)} to generate and
     * return
     * an available ID for either a patient or staff member, depending on the
     * prefix.
     * 
     * @param prefix the prefix for the ID (e.g., "DOC" for doctor, "p" for patient)
     * @return a string representing the generated ID, or {@code null} if the prefix
     *         is unrecognized
     */
    public static String generateId(String prefix) {
        String generatedId;
        switch (prefix) {
            case "DOC":
            case "PHM":
            case "ADM":
                generatedId = findAvailId(prefix, false); // Add 0 after prefix and find ID
                return generatedId;

            case "p":
                generatedId = findAvailId(prefix, true); // Add 0 after prefix and find ID
                return generatedId;

            default:
                return null; // Return null for unrecognized prefixes
        }
    }
}
