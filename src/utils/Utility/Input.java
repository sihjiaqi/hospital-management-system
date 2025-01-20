package utils.Utility;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

import repository.Medication.MedicationDB;

/**
 * This class contains utility methods for prompting and validating various types of user input, including names, email addresses,
 * phone numbers, dates, stock levels, and more.
 * <p>Each method prompts the user for input based on specific criteria and ensures that the input meets the required format or validation rules.</p>
 * <p>If the input is invalid or empty (depending on the context), the user will be repeatedly prompted until a valid response is provided or the input is skipped (if allowed).</p>
 */
public class Input {
    private static Scanner scanner = new Scanner(System.in);

    // License number input with skip feature based on 'isUpdate' flag
    /**
     * Prompts the user to input a license number and validates it.
     * 
     * <p>If the action is an update (determined by the {@code isUpdate} flag) and the input is empty, 
     * the method will return an empty string to skip the update. Otherwise, it ensures the input is a 
     * valid license number before returning it. If the input is invalid or empty (except for updates), 
     * the user will be prompted again until valid input is received or the action is skipped.</p>
     * 
     * @param lang The language to display error messages in.
     * @param isUpdate A boolean flag indicating whether the action is an update. If true, an empty input is allowed.
     * @return The valid license number entered by the user, or an empty string if the action is skipped (during update).
     */
    public static String inputLicenseNumber(String lang, boolean isUpdate) {
        String lnum;
        do {
            lnum = scanner.nextLine().trim(); // Trim any whitespace

            // Skip input if isUpdate is true and input is empty
            if (lnum.isEmpty() && isUpdate) {
                return lnum; // Return empty string to skip if action is Update
            } else if (lnum.isEmpty()) {
                Printer.print("License number cannot be empty.", lang); // Enforce input for non-Update actions
            }

            if (!Validation.validateLicenseNumber(lnum)) {
                Printer.print("Invalid license number", lang);
            }

        } while (!lnum.isEmpty() && !Validation.validateLicenseNumber(lnum)); // Validate input if not skipped

        return lnum; // Return the valid license number or empty string if skipped
    }

    /**
     * Prompts the user to input a password and validates it based on specified requirements.
     * 
     * <p>The password must meet the following criteria:</p>
     * <ul>
     *   <li>Length between 8 and 16 characters</li>
     *   <li>At least one special character </li>
     *   <li>At least one lowercase letter</li>
     *   <li>At least one uppercase letter</li>
     * </ul>
     * 
     * <p>If the action is an update (determined by the {@code isUpdate} flag) and the input is empty, 
     * the method will return an empty string to skip the update. Otherwise, it ensures the input meets the
     * password requirements before returning it. If the input is invalid or empty (except for updates), 
     * the user will be prompted again until valid input is received or the action is skipped.</p>
     * 
     * @param lang The language to display error messages in.
     * @param isUpdate A boolean flag indicating whether the action is an update. If true, an empty input is allowed.
     * @return The valid password entered by the user, or an empty string if the action is skipped (during update).
     */
    public static String inputPassword(String lang, boolean isUpdate) {
        String password;
        String validationMessage = null;

        // Display password requirements
        Printer.print("The password must meet the following requirements:", lang);
        Printer.print("1. Length should be between 8 and 16 characters.", lang);
        Printer.print("2. It must contain at least one special character (e.g., !@#$%^&*).", lang);
        Printer.print("3. It must contain at least one lowercase letter.", lang);
        Printer.print("4. It must contain at least one uppercase letter.", lang);
        Printer.print("5. It must contain at least one numeric value.", lang);
        Printer.print("Enter your new password: ", lang);

        do {
            password = scanner.nextLine().trim(); // Trim any whitespace

            // If update is true and input is empty, allow it to skip (return empty string)
            if (password.isEmpty() && isUpdate) {
                return password; // Return empty string to skip if it's an update
            }

            // If password is empty and not an update, prompt for valid input
            if (password.isEmpty()) {
                Printer.print("Password cannot be empty.", lang);
                continue; // Continue the loop to ask again
            }

            // Validate the password using the validatePassword function
            validationMessage = Validation.validatePassword(password);

            // If validation fails, print the error message and prompt again
            if (validationMessage != null) {
                Printer.print(validationMessage, lang);
            }

        } while (validationMessage != null); // Continue looping until a valid password is entered

        return password; // Return valid password
    }

    /**
     * Prompts the user to input a contact number and validates the input.
     * <p>
     * This method loops until the user provides a valid contact number or an empty 
     * contact number (if the action is an update). It ensures the input is not empty 
     * for non-update actions and validates the contact number format.
     * 
     * @param lang the language code for error messages
     * @param isUpdate a flag indicating whether this is an update action (true to allow empty input)
     * @return a string representing the valid contact number, or an empty string if skipped during update
     */
    public static String inputContactNumber(String lang, boolean isUpdate,String utype) {
        String contactInfo;
        String errorMessage;

        do {
            contactInfo = scanner.nextLine().trim(); // Trim any whitespace

            // Skip input if isUpdate is true and input is empty
            if (contactInfo.isEmpty() && isUpdate) {
                return contactInfo; // Return empty string to skip if action is Update
            } else if (contactInfo.isEmpty()) {
                Printer.print("Contact info cannot be empty.", lang); // Enforce input for non-Update actions
            }

            // Validate the contact info
            errorMessage = Validation.validateContactInfo(contactInfo,utype);

            // If there's an error message, print it
            if (errorMessage != null) {
                Printer.print(errorMessage, lang); // Print the error message returned by validator
            }

        } while (contactInfo.isEmpty() || errorMessage != null); // Continue looping until valid input

        return contactInfo; // Return the valid contact info or empty string if skipped
    }

    /**
     * Prompts the user to input an email address and validates the input.
     * <p>
     * This method loops until the user provides a valid email address or skips 
     * the update by leaving the input empty (if it’s an update action). It allows 
     * auto-generation of an email address and ensures the input email meets format 
     * requirements.
     * 
     * @param lang the language code for error messages
     * @param isUpdate a flag indicating whether this is an update action (true to allow empty input)
     * @param name the name of the user (used for generating an email if 'a' is entered)
     * @param userType the type of the user (used in email validation)
     * @return a string representing the valid email address, or an auto-generated email address
     *         if 'a' is entered, or an empty string if skipped during update
     */
    public static String inputEmailAddress(String lang, boolean isUpdate, String name, String userType) {
        String emailAddress;
        do {
            // Display prompt based on whether this is an update
            emailAddress = scanner.nextLine().trim(); // Trim any whitespace

            // If user presses 'a' for auto-generated email, generate an email
            if (emailAddress.equalsIgnoreCase("a")) {
                emailAddress = Util.generateEmail(name); // Call a method to generate a default email
                System.out.println("No email provided. Using system-generated email: " + emailAddress);
                return emailAddress;
            }

            // If user presses Enter and it's an Update operation, allow empty input to skip
            // the change
            if (emailAddress.isEmpty() && isUpdate) {
                return emailAddress; // Skip updating the email if empty
            }

            // If the email is empty and it's not an update, prompt again
            if (emailAddress.isEmpty()) {
                Printer.print("Email address cannot be empty.", lang);
                continue; // Prompt again if the input is empty
            }

            // Validate the email format (this could include checking length and format)
            if (!Validation.validateEmail(emailAddress, userType, false)) {
                Printer.print("Invalid input. Only <= 30 characters allowed and valid email format.", lang);
                continue; // Loop again if the email is invalid
            }

            // If the email is valid and non-empty, exit the loop
            break;

        } while (true);

        return emailAddress;
    }

    // Date of Birth input with skip feature based on 'isUpdate' flag
    /**
     * Prompts the user to input a date of birth (DOB) and validates the input.
     * 
     * <p>The date of birth must be in the format <b>YYYY-MM-DD</b>. If the input is empty and the action 
     * is an update (as determined by the {@code isUpdate} flag), the method will allow the user to skip 
     * the update. Otherwise, the input will be re-prompted until a valid date is entered. If the user enters 
     * an invalid date format or an empty string (and it's not an update), they will be prompted again until
     * valid input is received or the update is skipped.</p>
     * 
     * @param lang The language to display error messages in.
     * @param isUpdate A boolean flag indicating whether the action is an update. If true, an empty input is allowed to skip the update.
     * @return The valid {@code LocalDate} representing the date of birth entered by the user, or null if the action is skipped during an update.
     */
    public static LocalDate inputDOB(String lang, boolean isUpdate) {
        String dateString;
        LocalDate newDOB = null;
        LocalDate today = LocalDate.now();
        LocalDate maxAllowedDate = today.minusYears(150); // Date 150 years ago
    
        while (true) {
            dateString = scanner.nextLine().trim(); // Trim any whitespace
    
            // Skip input if isUpdate is true and input is empty
            if (dateString.isEmpty() && isUpdate) {
                break;
            } else if (dateString.isEmpty()) {
                Printer.print("Date of birth cannot be empty.", lang); // Enforce input for non-Update actions
                continue;
            } else if (!Validation.validateDate(dateString)) {
                Printer.print("Invalid input. Date must be in the format YYYY-MM-DD.", lang);
                continue;
            }
    
            try {
                newDOB = LocalDate.parse(dateString);
    
                // Check if the date of birth is more than 150 years ago
                if (newDOB.isBefore(maxAllowedDate)) {
                    Printer.print("The date of birth cannot be more than 150 years ago.", lang);
                    continue; // Ask for input again if it's too old
                }
    
                // Check if the date of birth is in the future
                if (newDOB.isAfter(today)) {
                    Printer.print("The date of birth cannot be in the future.", lang);
                    continue; // Ask for input again if the date is in the future
                }
    
                break; // Exit loop if everything is valid
            } catch (DateTimeParseException e) {
                Printer.print("Invalid date format. Please use yyyy-MM-dd.", lang);
            }
        }
    
        return newDOB; // Return the valid date of birth or null if skipped
    }
    

    // Username input with skip feature based on 'isUpdate' flag
    /**
     * Prompts the user to input a valid username. This method handles multiple cases of input:
     * <ul>
     * <li>If 'a' is pressed, a system-generated username will be used.</li>
     * <li>If the input is empty, it will exit without changes if the update flag is true, or prompt again if not updating.</li>
     * <li>If the input is not empty, the username is validated. If invalid, the user will be asked to try again.</li>
     * </ul>
     *
     * @param lang The language in which the prompts and error messages will be printed.
     * @param name The name of the user, used for generating a system username when 'a' is pressed.
     * @param utype The type of user (e.g., staff or patient), used for validating the username.
     * @param isUpdate A flag indicating whether the action is for updating an existing username or creating a new one.
     * 
     * @return The valid username entered by the user, or {@code null} if the input is cancelled or left empty during an update.
     */
    public static String inputUsername(String lang, String name, String utype, boolean isUpdate) {
        String username = "";
        do {
            username = scanner.nextLine().trim(); // Get username input and trim spaces

            // Case 1: If 'a' is pressed, generate a system username
            if (username.equalsIgnoreCase("a")) {
                username = Util.generateValidUsername(name, utype);
                Printer.print("Using system-generated username: " + username, lang);
                break; // Skip further input if 'a' is pressed to auto-generate a username
            }

            // Case 2: If Enter is pressed (empty input), exit the username input
            if (username.isEmpty()) {
                if (isUpdate) {
                    Printer.print("Exiting username input without changes.", lang);
                    return null; // Exit username input without updating
                } else {
                    Printer.print("Username cannot be empty.", lang);
                    continue; // Continue to ask for input if not an update
                }
            }

            // Case 3: Validate the username using the validateUsername function
            String errorMsg = Validation.validateUsername(username, utype); // Get error message or null
            if (errorMsg != null) {
                // If validation failed, print the error message
                Printer.print(errorMsg, lang);
            } else {
                // If the username is valid, break out of the loop
                break;
            }

        } while (true);

        return username; // Return the valid username or null if exited without username
    }

    // Name input with skip feature based on 'isUpdate' flag
    /**
     * Prompts the user to input a name and validates the input.
     * 
     * <p>The name must meet the following conditions:</p>
     * <ul>
     *   <li>The input must only contain letters and spaces.</li>
     *   <li>The length of the name must be less than or equal to 20 characters.</li>
     * </ul>
     * 
     * <p>If the action is an update (determined by the {@code isUpdate} flag) and the input is empty, 
     * the method will allow the user to skip updating the name. If the name is empty and it is not an update,
     * the user will be prompted again to enter a valid name.</p>
     * 
     * @param lang The language code to display error or prompt messages in.
     * @param isUpdate A boolean flag indicating whether the action is an update. If true, an empty input is allowed to skip updating the name.
     * @return The valid name entered by the user, or an empty string if the action is skipped during an update.
     */
    public static String inputName(String lang, boolean isUpdate) {
        String name;
        String validationMessage;

        do {
            name = scanner.nextLine().trim(); // Read input and trim whitespace

            // Skip input if isUpdate is true and input is empty
            if (name.isEmpty() && isUpdate) {
                return name; // Return empty string to indicate user skipped input if action is Update
            }
            // If the name is empty for non-update action, print the error and ask again
            else if (name.isEmpty()) {
                Printer.print("Name cannot be empty.", lang);
            }

            // Validate the name
            validationMessage = Validation.validateName(name);

            // If validation fails, print the error message and prompt again
            if (validationMessage != null) {
                Printer.print(validationMessage, lang); // Display the validation error message
            }

        } while (validationMessage != null); // Continue looping until a valid name is entered

        // Capitalize the name before returning
        name = Util.capitalizeName(name);

        return name; // Return valid name
    }

    /**
     * Prompts the user to input a blood type and validates the input.
     * 
     * <p>The blood type must be one of the following valid types: </p>
     * <ul>
     *   <li>"A", "B", "AB", "O"</li>
     * </ul>
     * <p>with an optional "+" or "-" suffix (e.g., "A+", "B-", "AB-", "O+").</p>
     * <p>If the action is an update (determined by the {@code isUpdate} flag) and the input is empty, 
     * the method will allow the user to skip updating the blood type. If the input is empty and it is not an update,
     * the user will be prompted again to enter a valid blood type.</p>
     * 
     * @param lang The language code to display error or prompt messages in.
     * @param isUpdate A boolean flag indicating whether the action is an update. If true, an empty input is allowed to skip updating the blood type.
     * @return The valid blood type entered by the user, or an empty string if the action is skipped during an update.
     */
    public static String inputBloodType(String lang, boolean isUpdate) {
        String bloodType;
        String[] validBloodTypes = { "A", "B", "AB", "O" }; // Valid blood types

        // This flag will hold the validation result for blood type
        boolean isValid;

        // Loop until valid input or skipping allowed
        do {
            bloodType = scanner.nextLine().trim(); // Read input and trim whitespace

            // Skip input if isUpdate is true and input is empty
            if (bloodType.isEmpty() && isUpdate) {
                return bloodType; // Return empty string to indicate user skipped input if action is Update
            } else if (bloodType.isEmpty()) {
                Printer.print("Blood type cannot be empty.", lang); // Enforce input for non-Update actions
            }

            // Check if the entered blood type matches any of the valid types with optional
            // "+" or "-" suffix
            isValid = false; // Reset isValid flag on each iteration
            for (String validType : validBloodTypes) {
                // Check if input matches any of the valid blood types with + or - suffix
                if (bloodType.equalsIgnoreCase(validType) ||
                        bloodType.equalsIgnoreCase(validType + "+") ||
                        bloodType.equalsIgnoreCase(validType + "-")) {
                    isValid = true; // Set isValid to true if match found
                    break; // Exit loop once a valid type is found
                }
            }

            if (!isValid) {
                Printer.print("Invalid input. Blood type must be one of A, B, AB, O with optional '+' or '-' suffix.",
                        lang);
            }

        } while (bloodType.isEmpty() || !isValid); // Validate blood type if not skipped

        return bloodType; // Return valid blood type or empty string if skipped
    }

    // Gender input with skip feature based on 'isUpdate' flag
    /**
     * Prompts the user to input their gender and validates the input.
     * 
     * <p>The gender must be represented by either "M" for Male or "F" for Female. The input is case-insensitive,
     * and any other input will be considered invalid.</p>
     * <p>If the action is an update (determined by the {@code isUpdate} flag) and the input is empty, 
     * the method will allow the user to skip updating the gender. If the input is empty and it is not an update,
     * the user will be prompted again to enter a valid gender.</p>
     * 
     * @param lang The language code to display error or prompt messages in.
     * @param isUpdate A boolean flag indicating whether the action is an update. If true, an empty input is allowed to skip updating the gender.
     * @return The valid gender entered by the user, or an empty string if the action is skipped during an update.
     */
    public static String inputGender(String lang, boolean isUpdate) {
        String gender;
        do {
            gender = scanner.nextLine().toUpperCase().trim(); // Read input, convert to uppercase, and trim whitespace

            // Skip input if isUpdate is true and input is empty
            if (gender.isEmpty() && isUpdate) {
                return gender; // Return empty string to skip gender input if action is Update
            } else if (gender.isEmpty()) {
                Printer.print("Gender cannot be empty.", lang); // Enforce input for non-Update actions
            }

            if (gender.equals("M")) {
                gender = "Male";
                break;
            } else if (gender.equals("F")) {
                gender = "Female";
                break;
            } else {
                Printer.print("Invalid input. Only M/F allowed. Please enter 'M' for Male or 'F' for Female.", lang);
            }

        } while (true); // Loop until valid input or skip

        return gender; // Return valid gender or empty string if skipped
    }

    // Method for medication name input with validation
    /**
     * Prompts the user to input a medication name and validates the input.
     * 
     * <p>The medication name must meet the following criteria:</p>
     * <ul>
     *   <li>The name must only contain letters and spaces.</li>
     *   <li>The length of the name must be less than or equal to 50 characters.</li>
     * </ul>
     * 
     * <p>If the input is empty or invalid, the user will be prompted to re-enter the medication name until a valid input is provided.</p>
     * 
     * @param lang The language code to display error or prompt messages in.
     * @return The valid medication name entered by the user.
     */
    public static String inputMedicationName(String lang) {
        String medName;

        while (true) {
            medName = scanner.nextLine().trim();

            // Check if the input is empty
            if (medName.isEmpty()) {
                Printer.print("Medication name cannot be empty", lang);
                continue;

                // Check if the input is valid
            } else if (!Validation.validateMedicationName(medName)) {
                Printer.print("Invalid input. Only letters and spaces and <= 50 characters allowed.", lang);
                continue; // Skip to the next iteration if validation fails
            }else if(MedicationDB.findMedicationByName(medName)!= null){
                Printer.print("Medication already in database. Enter another medication name: ", lang);
                continue;

            }
            break;
        }

        return medName;
    }

    // Method for inputting initial stock with validation
    /**
     * Prompts the user to input the initial stock quantity and validates the input.
     * 
     * <p>The input must be a valid non-negative integer. If the input is empty, non-numeric, or a negative number,
     * the user will be prompted to enter the value again.</p>
     * 
     * @param lang The language code to display error or prompt messages in.
     * @return The valid initial stock quantity entered by the user. It will be a non-negative integer.
     */
    public static int inputInitialStock(String lang) {
        int initialStock;
        while (true) {
            String input = scanner.nextLine().trim();

            // Check if input is empty
            if (input.isEmpty()) {
                Printer.print("Initial stock cannot be empty.", lang);
                continue; // Prompt user again
            }

            try {
                initialStock = Integer.parseInt(input);

                if (initialStock < 0) {
                    Printer.print("Stock cannot be negative. Please enter a valid number.", lang);
                } else {
                    break; // Exit loop if valid input
                }
            } catch (NumberFormatException e) {
                Printer.print("Invalid input. Please enter a valid number for initial stock.", lang);
            }
        }
        return initialStock;
    }

    // Method for inputting low stock alert with validation
    /**
     * Prompts the user to input the low stock alert threshold and validates the input.
     * 
     * <p>The input must be a valid non-negative integer. If the input is empty, non-numeric, or a negative number,
     * the user will be prompted to enter the value again.</p>
     * 
     * @param lang The language code to display error or prompt messages in.
     * @return The valid low stock alert threshold entered by the user. It will be a non-negative integer.
     */
    public static int inputLowStockAlert(String lang) {
        int lowStockAlert;
        int maxStockAlert = 100000000; // Define a reasonable upper limit for low stock alert (adjust as necessary)

        while (true) {
            String input = scanner.nextLine().trim();

            // Check if input is empty
            if (input.isEmpty()) {
                Printer.print("Low stock alert threshold cannot be empty.", lang);
                continue; // Prompt user again
            }

            try {
                lowStockAlert = Integer.parseInt(input);

                // Check if the value is negative
                if (lowStockAlert < 0) {
                    Printer.print("Alert threshold cannot be negative. Please enter a valid number.", lang);
                }
                // Check if the value exceeds the reasonable upper limit (e.g., 10,000 units)
                else if (lowStockAlert > maxStockAlert) {
                    Printer.print("Alert threshold cannot exceed " + maxStockAlert + ". Please enter a valid number.",
                            lang);
                } else {
                    break; // Exit loop if valid input
                }
            } catch (NumberFormatException e) {
                Printer.print("Invalid input. Please enter a valid number for low stock alert.", lang);
            }
        }

        return lowStockAlert; // Return the valid low stock alert value
    }

    // Method for inputting price with validation
    /**
     * Prompts the user to input a price and validates the input.
     * 
     * <p>The input must be a valid non-negative decimal number. If the input is empty, non-numeric, or a negative value,
     * the user will be prompted to enter the value again.</p>
     * 
     * @param lang The language code to display error or prompt messages in.
     * @return The valid price entered by the user. It will be a non-negative decimal number.
     */
    public static double inputPrice(String lang) {
        double price = -1; // Initialize to an invalid value (negative) to enter the loop
        String inputPrice;

        do {
            Printer.print("Enter price:", lang);
            inputPrice = scanner.nextLine().trim(); // Get user input and trim any whitespace

            try {
                // Try parsing the price
                price = Double.parseDouble(inputPrice);

                // Check if the price is negative
                if (price <= 0) {
                    Printer.print("Price cannot be negative. Please enter a valid number (greater than 0).", lang);
                }
                // Check if the price is extremely large or small (out of reasonable range)
                else if (price > 1_000_000_000 || price < Double.MIN_VALUE) {
                    Printer.print("Price is too large or too small. Please enter a reasonable price.", lang);
                } else {
                    // Valid input, exit loop
                    break;
                }
            } catch (NumberFormatException e) {
                Printer.print("Invalid input. Please enter a valid number for price.", lang);
            }
        } while (price < 0 || price > 1_000_000_000 || price < Double.MIN_VALUE); // Continue loop for invalid price
                                                                                  // ranges

        return price; // Return the valid price
    }




}
