import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import boundary.Applications.AdminApp;
import boundary.Applications.DoctorApp;
import boundary.Applications.PatientApp;
import boundary.Applications.PharmacistApp;
import model.Users.User;
import repository.Appointment.AppointmentDB;
import repository.MedicalRecord.MedicalRecordDB;
import repository.Medication.ReplenishRequestDB;
import repository.Users.PatientDB;
import repository.Users.StaffDB;
import utils.Database;
import utils.Utility.Input;
import utils.Utility.Printer;
import utils.Utility.Util;
import utils.Utility.Config;
import utils.Utility.IdGenerator;
import model.Users.Admin;
import model.Users.Doctor;
import model.Users.Patient;
import model.Users.Pharmacist;

/**
 * Main entry point of the Hospital Management System. This class handles user
 * interaction
 * for login, registration, and navigating to different applications based on
 * user roles (e.g., Patient, Admin, Doctor, Pharmacist).
 */
public class Main {
    private static boolean isRunning = true;
    private static String lang = Config.getLang();
    private static Scanner scanner = new Scanner(System.in);

    // bg process
    /**
     * The main method that initializes the application, handles user input and
     * interacts with
     * various system components such as database initialization, user login, and
     * application navigation.
     *
     * @param args Command-line arguments (not used in this program)
     */
    public static void main(String[] args) {
        int await = 0;
        do {
            Database db = new Database();
            await = db.init();

        } while (await == 0);

        while (isRunning) {
            printLoginPage();
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    // RegisterApp.start(); // Placeholder for register functionality
                    register();
                    break;
                case "2":
                    Patient patient = login(lang);
                    if (patient != null) {

                        String patientId = patient.getId();
                        // System.out.println(patientId);
                        checkFirstLogin(patientId, patient);
                        PatientApp patientApp = PatientApp.getInstance();
                        patientApp.initializeApp(patient);
                    }
                    break;
                case "3":
                    return; // Exit the program
                case "4":
                    User user = staffLogin(lang); // Handle staff login
                    if (user != null) {
                        String staffId = user.getId();
                        checkFirstLogin(staffId, user);

                        if (user instanceof Admin) {
                            Admin admin = (Admin) user;
                            AdminApp adminApp = AdminApp.getInstance();
                            adminApp.initializeApp(admin); // Initialize Admin application
                        } else if (user instanceof Doctor) {
                            Doctor doctor = (Doctor) user;
                            DoctorApp doctorApp = DoctorApp.getInstance();
                            doctorApp.initializeApp(doctor); // Initialize Doctor application
                        } else if (user instanceof Pharmacist) {
                            Pharmacist pharmacist = (Pharmacist) user;
                            PharmacistApp pharmacistApp = PharmacistApp.getInstance();
                            pharmacistApp.initializeApp(pharmacist); // Initialize Pharmacist application
                        }
                    } else {
                        Printer.print("Login failed or user not found.", lang);
                    }
                    break;
                default:
                    Printer.print("Invalid option, please try again", lang);
                    break;
            }
        }
    }

    /**
     * Handles the staff login process. Prompts for username and password, verifies
     * credentials,
     * and returns the corresponding staff object if login is successful.
     *
     * @param lang The language of the application
     * @return The logged-in staff member or null if login fails
     */
    public static User staffLogin(String lang) {
        User staff = null; // Initialize staff variable outside the loop
        while (true) {
            Printer.print("Enter your username or enter 'q' to quit: ", lang);
            String loginCred = scanner.nextLine().trim(); // Trim whitespace from input

            // Check if the user pressed 'q' to quit
            if (loginCred.equalsIgnoreCase("q")) {
                Printer.print("Cancelled. No further action taken.", lang);
                break; // Exit the loop if the user chooses to quit
            } else {
                // Retrieve the user by username
                staff = StaffDB.getUserByUsername(loginCred);

                if (staff != null) {
                    // Enter the password prompt loop
                    while (true) {
                        System.out.print("Enter your password or press 'r' to reset your password: ");
                        String enteredPassword = scanner.nextLine().trim(); // Trim any whitespace

                        // Check if the user pressed 'q' to quit
                        if (enteredPassword.equalsIgnoreCase("q")) {
                            Printer.print("Cancelled. No further action taken.", lang);
                            return null; // Return null if the user chooses to quit during password entry
                        }

                        // Check if the user wants to reset the password
                        if (enteredPassword.equalsIgnoreCase("r")) {
                            // Verify identity by asking for contact number
                            Printer.print("Enter your contact number for verification: ", lang);
                            String contactNumber = scanner.nextLine().trim();

                            // Check if the contact number matches the stored one
                            if (staff.getContactNumber().equals(contactNumber)) {
                                // Reset password to the default value
                                String newPassword = "password123";
                                staff.setPassword(Util.hashPassword(newPassword));

                                // Get the current prefix from the ID
                                String currentId = staff.getId();
                                String prefix = currentId.substring(0, 3); // Extract the prefix (DOC, ADM, PHM)
                                String updatedId = prefix + "0" + currentId.substring(4); // Change the first numeric
                                                                                          // character to '0'

                                // Update the user ID
                                staff.setID(updatedId);

                                // Update user in the database
                                StaffDB.updateUserInfo(staff.getId(), updatedId, staff);

                                Printer.print("Password reset successful! Your new password is: password123", lang);
                                Printer.print("Your staff ID has been updated to: " + updatedId, lang);
                                return staff; // Return the staff object after successful reset
                            } else {
                                Printer.print("Contact number verification failed. Cannot reset password.", lang);
                            }
                        }

                        String hashedEnteredPassword = Util.hashPassword(enteredPassword);

                        if (staff.login(hashedEnteredPassword)) {
                            Printer.print("Login successful!", lang);
                            return staff; // Return the staff object if login is successful
                        } else {
                            Printer.print("Incorrect password! Please try again or press 'r' to reset your password.",
                                    lang);
                            // The loop will continue, allowing the user to re-enter the password or reset
                            // it
                        }
                    }
                } else {
                    Printer.print("User does not exist!", lang);
                    // Continue asking for the username if the user does not exist
                }
            }
        }

        return null; // Return null if the user chooses to quit or fails to login
    }

    /**
     * Handles the login process for a patient.
     * <p>
     * This method allows a patient to log in using their username and password. It
     * provides the following functionality:
     * </p>
     * <ul>
     * <li>Prompts the user to enter their username or quit by pressing 'q'.</li>
     * <li>If the username is found, prompts the user to enter their password or
     * reset it.</li>
     * <li>If the password is incorrect, the user is given the option to try again
     * or reset the password.</li>
     * <li>If the user opts to reset their password, they must verify their contact
     * number, after which the password will be reset to a default value, and their
     * user ID will be updated.</li>
     * <li>If the login is successful, the corresponding patient object is
     * returned.</li>
     * </ul>
     * <p>
     * If the user decides to quit at any point, the method returns null and no
     * further action is taken.
     * </p>
     * 
     * @param lang The language code to specify the language in which prompts are
     *             displayed (e.g., "en" for English).
     * @return The patient object if login is successful, or null if the user quits
     *         or fails to log in.
     */
    public static Patient login(String lang) {
        Patient patient = null; // Initialize patient variable outside the loop
        while (true) {
            Printer.print("Enter your username or enter 'q' to quit:", lang);
            String loginCred = scanner.nextLine().trim(); // Trim whitespace from input

            // Check if the user pressed 'q' to quit
            if (loginCred.equalsIgnoreCase("q")) {
                Printer.print("Cancelled. No further action taken.", lang);
                break; // Exit the loop if the user chooses to quit
            } else {
                // Retrieve the user by username
                patient = PatientDB.getUserByUsername(loginCred);

                if (patient != null) {
                    // Enter the password prompt loop
                    while (true) {
                        System.out.print("Enter your password or press 'r' to reset your password: ");
                        String enteredPassword = scanner.nextLine().trim(); // Trim any whitespace

                        // Check if the user pressed 'q' to quit
                        if (enteredPassword.equalsIgnoreCase("q")) {
                            Printer.print("Cancelled. No further action taken.", lang);
                            return null; // Return null if the user chooses to quit during password entry
                        }

                        // Check if the user wants to reset the password
                        if (enteredPassword.equalsIgnoreCase("r")) {
                            // Verify identity by asking for contact number
                            Printer.print("Enter your contact number for verification: ", lang);
                            String contactNumber = scanner.nextLine().trim();

                            // Check if the contact number matches the stored one
                            if (patient.getContactNumber().equals(contactNumber)) {
                                // Reset password to the default value
                                String newPassword = "password123";
                                patient.setPassword(Util.hashPassword(newPassword));

                                // Get the current prefix from the ID
                                String currentId = patient.getId();
                                String prefix = currentId.substring(0, 1); // Extract the prefix ('p')
                                String updatedId = prefix + "0" + currentId.substring(2); // Change the first numeric
                                                                                          // character to '0'

                                // Update the user ID
                                patient.setID(updatedId);

                                // Update user in the database
                                PatientDB.updateUserInfo(patient.getId(), updatedId, patient);

                                Printer.print("Password reset successful! Your new password is: password123", lang);
                                // Printer.print("Your patient ID has been updated to: " + updatedId, lang);
                                return patient; // Return the patient object after successful reset
                            } else {
                                Printer.print("Contact number verification failed. Cannot reset password.", lang);
                            }
                        }

                        String hashedEnteredPassword = Util.hashPassword(enteredPassword);

                        if (patient.login(hashedEnteredPassword)) {
                            Printer.print("Login successful!", lang);
                            return patient; // Return the patient object if login is successful
                        } else {
                            Printer.print("Incorrect password! Please try again or press 'r' to reset your password.",
                                    lang);
                            // The loop will continue, allowing the user to re-enter the password or reset
                            // it
                        }
                    }
                } else {
                    Printer.print("User does not exist!", lang);
                    // Continue asking for the username if the user does not exist
                }
            }
        }

        return null; // Return null if the user chooses to quit or fails to login
    }

    /**
     * Checks if the user is logging in for the first time and if so, prompts them
     * to reset their password.
     * The user ID is updated based on the first login rules.
     *
     * @param id   The user ID (either patient or staff)
     * @param user The user object (Patient or Staff)
     */
    public static void checkFirstLogin(String id, User user) {
        // Check if the ID is valid (at least 4 characters)
        if (id.length() >= 4) {
            // Staff ID logic: it must start with "DOC", "ADM", or "PHM"
            if (id.startsWith("DOC") || id.startsWith("ADM") || id.startsWith("PHM")) {
                // Check if the ID is long enough to have a 4th character
                if (id.length() >= 4) {
                    char fourthChar = id.charAt(3); // Get the 4th character (index 3)

                    if (fourthChar == '0') {
                        // If the 4th character is '0', prompt for password reset (first login)
                        Printer.print("It appears that this is your first login attempt. Please reset your password.",
                                lang);
                        String password = Input.inputPassword(lang, false);
                        user.setPassword(Util.hashPassword(password));
                        Printer.print("Successful reset of password! Welcome!", lang);

                        // After resetting password, update the ID by setting the 4th character to '1'
                        String uid = id.substring(0, 3) + '1' + id.substring(4); // Change the 4th character to '1'
                        user.setID(uid);

                        StaffDB.updateUserInfo(id, uid, user);


                        //
                        String prefix = id.length() >= 3 ? id.substring(0, 3) : "";

                        switch (prefix) {
                            case "DOC":
                            // Handle case for DOC prefix
                            AppointmentDB.updateDoctorIdInAppointments(id, uid);
                            break;
                        case "ADM":
                            // Handle case for ADM prefix
                            break;
                        case "PHM":
                            // Handle case for PHM prefix
                            ReplenishRequestDB.updatePharmacistIdInReplenishRequest(id, uid);
                            break;
                        default:
                              
                                break;
                        }

                        Printer.print("Your Staff ID has been updated to: " + uid, lang);
                    } else if (fourthChar == '1') {
                        // If the 4th character is '1', just continue (already logged in before)
                        // Printer.print("Welcome back! You have already logged in before.", lang);
                    } else {
                        // If the 4th character is neither '0' nor '1', it's an invalid format
                        Printer.print("Invalid Staff ID format. The 4th character should be '0' or '1'.", lang);
                    }
                } else {
                    // If the ID is too short to have a 4th character, it's invalid
                    Printer.print("Invalid Staff ID format. The ID should have at least 4 characters.", lang);
                }
            }

            // Patient ID logic: it must start with "p" and have a valid 5-character format
            else if (id.charAt(0) == 'p') {
                char secondChar = id.charAt(1); // Get the second character (first digit after 'p')

                if (Character.isDigit(secondChar)) {
                    // Patient ID logic (using the first numeric character after 'p')
                    if (secondChar == '0') {
                        // Assuming the user ID is passed as a string like "1611023" (without the "p")
                        Printer.print("It appears that this is your first login attempt. Please reset your password.",
                                lang);
                        Printer.print("Please enter a new password! ", lang);

                        // Prompt for a new password and hash it
                        String password = Input.inputPassword(lang, false);
                        user.setPassword(Util.hashPassword(password));

                        // After resetting the password, update the ID
                        // First, ensure that the ID starts with 'p'
                        // Ensure that the ID starts with 'p'
                        String uid = user.getId(); // Get the current user ID

                        if (!uid.startsWith("p")) {
                            uid = "p" + uid; // Add the 'p' prefix if it's missing
                        }

                        // Now, update the first zero to '1' after the first login
                        if (uid.length() >= 2 && uid.charAt(1) == '0') {
                            uid = "p1" + uid.substring(2); // Replaces the first zero with '1'
                        } else {

                        }

                        PatientDB.updateUserInfo(id, uid, (Patient) user);
                        MedicalRecordDB.updatePatientIdInMedicalRecord(id, uid);
                        AppointmentDB.updatePatientIdInAppointment(id, uid);
                    } else if (secondChar == '1') {
                        // If the first numeric character is '1', just continue
                        // Printer.print("Welcome back! You have already logged in before.", lang);
                    } else {
                        Printer.print("Invalid Patient ID format. The second character should be '0' or '1'.", lang);
                    }
                } else {
                    Printer.print("Invalid Patient ID format. The second character should be a digit.", lang);
                }
            } else {
                Printer.print(
                        "Invalid ID format. Staff IDs must start with DOC, ADM, or PHM; Patient IDs must start with 'p'.",
                        lang);
            }
        } else {
            Printer.print("Invalid ID format. It should have at least 4 characters.", lang);
        }
    }

    /**
     * Prints the login page menu options for the user to choose from.
     */
    public static void printLoginPage() {
        List<String> menuOptions = Arrays.asList(
                "Register",
                "Login",
                "Exit",
                "Staff Login");
        String menuHeader = "HZMZS";
        Printer.printBlockHeader(menuHeader);
        Printer.printMenu(menuOptions);
    }

    /**
     * Registers a new patient by collecting necessary information such as name,
     * username, password, and additional details.
     * Once registered, the patient's details are saved in the database.
     */
    public static void register() {
        // Ensure user enters a valid name
        String name;

        System.out.print("Enter your name in this format 'firstname lastname' (Required): ");
        name = Input.inputName(lang, false);

        // Ensure user enters a valid username
        String username;

        Printer.print(
                "Enter a username (Enter 'a' to auto-generate or enter a valid username): ",
                lang);
        username = Input.inputUsername(lang, name, "patient", false);

        Printer.print("Enter your contact number : ", lang);
        String contactNumber = Input.inputContactNumber(lang, false, "patient");

        Printer.print("Enter your date of birth ", lang);
        LocalDate dobInput = Input.inputDOB(lang, false);

        // Ensure user enters a valid password
        String password;
        Printer.print("Your password is set to the default password, change it after first login ", lang);
        password = Util.hashPassword("password123");

        // Step 2: Collect optional information (can be skipped by pressing Enter)
        Printer.print("Enter your gender (Optional) [Press Enter to skip]: ", lang);
        String gender = Input.inputGender(lang, true);

        Printer.print("Enter your email address (Optional) [Press Enter to skip]: ", lang);
        String emailAddress = Input.inputEmailAddress(lang, true, "", "patient");

        Printer.print("Enter your blood type (Optional) [Press Enter to skip]: ", lang);
        String bloodType = Input.inputBloodType(lang, true);

        // Step 3: Create the Patient object with required fields
        String id = IdGenerator.generateId("p");
        Patient newPatient = new Patient(id,
                name,
                gender,
                username,
                password,
                dobInput,
                contactNumber,
                emailAddress,
                bloodType);

        PatientDB.addUser(newPatient, false);

        // Step 4: Confirm registration and show the user details
        Printer.print("\nRegistration successful!", lang);
        Printer.print("Account created for username: " + newPatient.getUsername(), lang);

    }
}
