package utils.Utility;

import java.util.Random;
import java.util.Scanner;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * A utility class providing various helper methods for common tasks such as clearing the console,
 * generating usernames, hashing passwords, and converting data types. The methods include functionality
 * for managing console outputs, handling date conversions, generating random values, and ensuring data 
 * integrity through hashing and validation.
 * 
 * <p>Methods in this class are designed to assist with common operations such as:</p>
 * <ul>
 *   <li>Clearing the console based on the operating system.</li>
 *   <li>Validating and converting user input such as dates and integers.</li>
 *   <li>Generating unique usernames, email addresses, and random numbers.</li>
 *   <li>Hashing passwords securely using the SHA-256 algorithm.</li>
 *   <li>Formatting and capitalizing user names.</li>
 * </ul>
 * 
 * <p>Note: Some methods rely on external validation classes like {@code Validation.validateUsername} 
 * and {@code Validation.validateEmail} for checking the validity of generated usernames and email addresses.</p>
 */
public class Util {
    /**
     * Clears the console by printing 100 blank lines.
     * <p>
     * This method simulates clearing the console screen by printing 100 blank lines to the standard output. 
     * It also includes a brief pause between each line (10 milliseconds) to ensure the screen is cleared visually.
     * </p>
     */
    public static void clearConsole2() {
        for (int i = 0; i < 100; i++) {
            System.out.println(); // Print many blank lines
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Clears the console screen based on the operating system.
     * 
     * - For Windows, the method uses the `cmd` command to execute `cls` and clear the console.
     * - For Unix-based systems (Linux, macOS), it uses an ANSI escape code to clear the console.
     * 
     * The method checks the operating system type and executes the appropriate clearing method.
     * In case of errors while clearing the console on Windows, an error message is printed to the standard error stream.
     */
    public static void clearConsole() {
        // For Windows
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            try {
                // Run a command to clear the console screen
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } catch (Exception e) {
                System.err.println("Error clearing the console.");
            }
        } else {
            // For Unix-based systems (Linux, macOS)
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }
    }

    /**
     * Converts a string representing a date into a LocalDate object.
     * The expected date format is "yyyy-MM-dd". If the input string does not match the expected format,
     * the method prints an error message and returns null.
     * 
     * @param dateString The string representation of the date to be converted.
     *                   It must follow the "yyyy-MM-dd" format.
     * @return The corresponding LocalDate object if the string is valid; otherwise, returns null.
     */
    public static LocalDate convertStringToDate(String dateString) {
        // Define the format pattern to match yyyy-mm-dd (or any other format you need)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            // Parse the date string and return as LocalDate
            return LocalDate.parse(dateString, formatter);
        } catch (DateTimeParseException e) {
            // Handle the case where the string does not match the expected format
            System.out.println("Invalid date format: " + dateString);
            return null;
        }
    }

    /**
     * Prompts the user to enter an integer input. If the input is invalid, it continues prompting until a valid integer is entered.
     * 
     * @param scanner The scanner object used to read the user input.
     * @param msg The message displayed to the user, prompting for input.
     * @return The integer value entered by the user.
     */
    public static int intScanner(Scanner scanner, String msg) {
        int result = 0;
        boolean validInput = false;

        while (!validInput) {
            System.out.print(msg);
            String input = scanner.nextLine();

            try {
                result = Integer.parseInt(input);
                validInput = true; // If parsing is successful, break out of loop
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid integer.");
            }
        }
        return result;
    }

    // Method to generate a unique, valid username
    /**
     * Generates a valid username based on the user's full name and user type. The username is repeatedly generated
     * until it passes validation based on the user type.
     * 
     * The method first generates a potential username from the full name, then validates it using the
     * `Validation.validateUsername` method. If the generated username is not valid, it will continue generating and
     * validating new usernames until a valid one is found.
     * 
     * @param fullName The full name of the user, used to generate the initial username.
     * @param utype The user type, which is used in the username validation process.
     * @return A valid username that conforms to the validation rules for the given user type.
     */
    public static String generateValidUsername(String fullName, String utype) {
        String username;

        // Keep generating usernames until a valid one is found
        do {
            username = generateUsername(fullName); // Generate a new username
        } while (Validation.validateUsername(username, utype)!=null); // Repeat until a valid username is found

        return username;
    }

    // Helper method to generate a username based on the person's full name
    /**
     * Generates a username from the provided full name by cleaning and shortening the name, and appending a random number.
     * 
     * The method performs the following steps:
     * 1. Removes all spaces from the full name and converts it to lowercase.
     * 2. Takes the first 5 characters of the cleaned name (or the entire cleaned name if it is shorter than 5 characters).
     * 3. Appends a random number to ensure the username is unique.
     * 
     * @param fullName The full name of the user, used to generate the username.
     * @return A unique username based on the cleaned version of the user's full name, followed by a random number.
     */
    private static String generateUsername(String fullName) {
        // Clean the name by removing spaces and making it lowercase
        String cleanName = fullName.replaceAll("\\s+", "").toLowerCase();

        // Get the first 5 characters of the cleaned name
        String username = cleanName.length() > 5 ? cleanName.substring(0, 5) : cleanName;

        // Append a random number to make the username unique
        username += generateRandomNumber();

        return username;
    }

    // Helper method to generate a random number to add to the username
    /**
     * Generates a random number between 0 and 999 and formats it as a 3-digit string.
     * 
     * The method generates a random integer between 0 and 999 (inclusive) using the `Random` class, and then formats it
     * to ensure the number is always represented as a 3-digit string, padding with leading zeros if necessary.
     * 
     * @return A 3-digit string representing a randomly generated number between 0 and 999, inclusive (e.g., "007").
     */
    private static String generateRandomNumber() {
        Random random = new Random();
        int randomNumber = random.nextInt(1000); // Generates a number between 0 and 999
        return String.format("%03d", randomNumber); // Formats to 3 digits (e.g., 007)
    }

    // Method to hash the password using SHA-256
    // Method to hash the password using SHA-256
    /**
     * Hashes the provided password using the SHA-256 hashing algorithm.
     * 
     * This method converts the input password into a secure SHA-256 hash. The password is first converted into a byte array
     * using UTF-8 encoding, then hashed using the SHA-256 algorithm. The resulting hash is returned as a hexadecimal string
     * representation.
     * 
     * @param password The plaintext password to be hashed.
     * @return A string representing the SHA-256 hash of the input password, in hexadecimal format.
     * @throws RuntimeException If the SHA-256 algorithm is not available on the system.
     */
    public static String hashPassword(String password) {
        try {
            // Create a MessageDigest instance for SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Perform the hashing (converting the input string to bytes using UTF-8)
            byte[] hashedBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            // Convert the byte array to a hexadecimal string representation
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashedBytes) {
                hexString.append(String.format("%02x", b));
            }

            // Return the hashed password as a hexadecimal string
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error: SHA-256 algorithm not found.", e);
        }
    }

    /**
     * Generates a unique email address for the given name.
     * 
     * This method creates a basic email address by converting the input name to lowercase and appending a standard domain
     * (e.g., "@gmail.com"). It then checks if the generated email already exists in the UserDB. If the email exists, a random
     * suffix is appended to make the email unique. This process continues until a unique email address is found.
     * 
     * @param name The name used to generate the email address. It is typically a user's full name.
     * @return A unique email address based on the input name. If necessary, a random number suffix will be added.
     */
    public static String generateEmail(String name) {
        // Convert name to lowercase and create a basic email format
        String email = createBasicEmail(name);

        // Check if email already exists in the UserDB (validateEmail will return false
        // if email is unique)
        while (!Validation.validateEmail(email, "staff", true)) {
            // If the email exists, append a number to make it unique
            email = createBasicEmail(name) + generateRandomSuffix() + "@gmail.com";
        }

        // Return the generated unique email
        return email;
    }

    // Method to create a basic email based on name
    // Method to create a basic email based on name
    /**
     * Creates a basic email address based on the provided name.
     * 
     * This method splits the input name into its first and last parts (assuming the name is formatted as "First Last").
     * It then creates an email prefix by concatenating the first name and last name (if provided), separated by an underscore.
     * The result is a basic email address format, without a domain.
     * 
     * @param name The name used to generate the email address. This should typically be the user's full name in "First Last" format.
     * @return A basic email address prefix (e.g., "john_doe" for "John Doe").
     */
    private static String createBasicEmail(String name) {
        String[] nameParts = name.split(" ");
        String firstName = nameParts[0].toLowerCase();
        String lastName = nameParts.length > 1 ? nameParts[1].toLowerCase() : "";
        return firstName + (lastName.isEmpty() ? "" : "_" + lastName); // Remove the @gmail.com part here
    }

    // Method to generate a random suffix (for email uniqueness)
    /**
     * Generates a random suffix to be appended to an email address.
     * 
     * This method generates a random integer between 0 and 999 and appends it to the string
     * as a suffix, prefixed with an underscore ("_"). It can be used to create unique
     * email addresses when multiple accounts share the same basic name.
     * 
     * @return A random string suffix, formatted as "_<random_number>" where <random_number>
     *         is a number between 0 and 999 (inclusive).
     */
    private static String generateRandomSuffix() {
        return "_" + (int) (Math.random() * 1000); // A random number as suffix
    }

    /**
     * Capitalizes the first and last names in a full name string.
     * <p>
     * This method takes a name consisting of exactly two parts (first name and last name), trims any leading or
     * trailing whitespace, and capitalizes the first letter of each part. The rest of the letters in both parts are
     * converted to lowercase. If the name doesn't contain exactly two parts, an exception is thrown.
     * </p>
     * 
     * @param name The full name string, containing exactly a first name and a last name.
     * @return The name with both the first and last names capitalized.
     * @throws IllegalArgumentException if the name doesn't contain exactly two parts (first and last name).
     */
    public static String capitalizeName(String name) {
        // Step 1: Trim leading/trailing spaces
        name = name.trim();
        
        // Step 2: Split the name into first and last names (assuming two parts)
        String[] parts = name.split("\\s+");
        
        if (parts.length != 2) {
            throw new IllegalArgumentException("Name should contain exactly a first name and a last name.");
        }
    
        // Step 3: Capitalize the first letter of each part
        String firstName = parts[0].substring(0, 1).toUpperCase() + parts[0].substring(1).toLowerCase();
        String lastName = parts[1].substring(0, 1).toUpperCase() + parts[1].substring(1).toLowerCase();
    
        // Step 4: Reassemble the name
        name = firstName + " " + lastName;
    
        // Step 5: Return the modified name
        return name;
    }
}
