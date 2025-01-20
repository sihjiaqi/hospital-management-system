package boundary.Applications;

import java.util.Arrays;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import controller.Appointment.AppointmentController;
import controller.Medication.MedicationController;
import controller.Medication.ReplenishRequestController;
import controller.Users.PharmacistController;
import model.Appointment.AppointmentOutcome;
import model.Appointment.AppointmentOutcome.PrescriptionStatus;
import model.Medication.Medication;
import model.Medication.ReplenishRequest;
import model.Users.Pharmacist;
import repository.Medication.MedicationDB;
import utils.Utility.Config;
import utils.Utility.Input;
import utils.Utility.Printer;
import utils.Utility.Util;

/**
 * Represents the main application for the pharmacist interface.
 * 
 * The {@code PharmacistApp} class provides a user interface that allows a pharmacist to manage appointments, 
 * medications, prescriptions, and personal information. The app allows the pharmacist to:
 * <ul>
 *     <li>View appointment outcome records</li>
 *     <li>Update prescription statuses</li>
 *     <li>View and refresh medication inventory</li>
 *     <li>Submit replenishment requests for medications</li>
 *     <li>Update personal information (e.g., name, username, password, contact details)</li>
 *     <li>Logout of the system</li>
 * </ul>
 * The app supports multiple languages and provides the option to switch between English, Spanish, and French.
 * 
 * This class follows a singleton design pattern and ensures that only one instance of the application 
 * is created during its lifecycle.
 * 
 * <p>Usage example:</p>
 * <pre>
 *     PharmacistApp app = PharmacistApp.getInstance();
 *     app.initializeApp(pharmacist);
 * </pre>
 */
public class PharmacistApp {
    private static String lang = "en";
    private static PharmacistApp instance;
    private static Scanner scanner = new Scanner(System.in);

    // Controller instantiation
    private MedicationController medicationController = new MedicationController();
    private AppointmentController appointmentController = new AppointmentController();
    private ReplenishRequestController replenishRequestController = new ReplenishRequestController();
    private PharmacistController pharmacistController = new PharmacistController();

    /**
     * Constructor for the {@link PharmacistApp} class.
     * 
     * This constructor is used for initializing an instance of the PharmacistApp class. 
     * Any necessary setup or initialization required for the application can be performed here.
     * 
     * Note: Currently, this constructor does not take any arguments or perform any specific 
     * actions, but can be extended to include setup tasks as needed in the future.
     */
    private PharmacistApp() {
        // Initialization or any required setup can be done here
    }

    /**
     * Provides access to the singleton instance of the {@link PharmacistApp} class.
     * 
     * This method implements the Singleton design pattern, ensuring that only one instance
     * of the PharmacistApp class is created. The instance is created lazily, meaning it will 
     * only be instantiated when it is first needed.
     * 
     * @return the singleton instance of the {@link PharmacistApp} class.
     */
    public static PharmacistApp getInstance() {
        if (instance == null) {
            // Create the instance only when it's needed (Lazy initialization)
            instance = new PharmacistApp();
        }
        return instance;
    }

    /**
     * Initializes the PharmacistApp with the provided {@link Pharmacist} instance.
     * 
     * This method is responsible for initializing the PharmacistApp by starting the application
     * and passing in the provided {@link Pharmacist} object, which may represent the currently 
     * active pharmacist or user of the application. It delegates to the {@link #startPharmacistApp(Pharmacist)}
     * method to begin the application process.
     * 
     * @param pharmacist the {@link Pharmacist} object representing the pharmacist to initialize the app with.
     */
    public void initializeApp(Pharmacist pharmacist) {
        startPharmacistApp(pharmacist);
    }

    /**
     * Starts the Pharmacist application and presents the main menu to the user.
     * 
     * This method initializes the application by prompting the pharmacist to select their preferred 
     * language and displays a menu with various options related to pharmacy operations. The menu includes 
     * options such as viewing appointment outcomes, updating prescription statuses, viewing medication 
     * inventory, submitting replenishment requests, updating personal information, and logging out.
     * 
     * The pharmacist can interact with the menu and perform actions based on their choices. If an invalid 
     * input is provided at any step, appropriate error messages will be displayed.
     * 
     * @param pharmacist the {@link Pharmacist} object representing the current user of the application.
     */
    private void startPharmacistApp(Pharmacist pharmacist) {
        Scanner sc = new Scanner(System.in);
        int choice;
        int appointmentId;
        String menuHeader;
        List<String> menuOptions;

        System.out.println("Please select a language (1 - English, 2 - Spanish, 3 - French): ");

        String inpt = sc.nextLine().trim(); // Read the input as a string

        // Check if the input is empty (i.e., user pressed Enter without entering
        // anything)
        if (inpt.isEmpty()) {
            lang = "en"; // Default to English
            System.out.println("No input detected, defaulting to English.");
        } else {
            switch (inpt) {
                case "1":
                    lang = "en"; // English
                    break;
                case "2":
                    lang = "es"; // Spanish
                    break;
                case "3":
                    lang = "fr"; // French
                    break;
                default:
                    System.out.println("Invalid selection, defaulting to English.");
                    lang = "en"; // Default to English if input is invalid
                    break;
            }
            Config.setLang(lang);

        }

        do {
           // Util.clearConsole();
            menuHeader = "Pharmacist Menu";
            menuOptions = Arrays.asList(
                    "View Appointment Outcome Record",
                    "Update Prescription Status",
                    "View Medication Inventory",
                    "Submit Replenishment Request",
                    "Update Personal Info", // New option added
                    "Logout");
            Printer.printMenu(menuHeader, menuOptions);

            choice = sc.nextInt();
            switch (choice) {
                case 1:// (1) View Appointment Outcome Record
                    Util.clearConsole();
                    Printer.printHeader("----------------- Appointment Outcome Record -----------------");

                    while (true) {
                        Printer.print("Enter ID of appointment (or Enter 'q' to return to main menu): ", lang);
                        String input = sc.next();

                        // Check if user wants to return to the main menu
                        if (input.equalsIgnoreCase("q")) {
                            Util.clearConsole();
                            break; // Exit this case and go back to the main menu
                        }

                        try {
                            appointmentId = Integer.parseInt(input); // Try to parse the appointment ID
                            // Retrieve the appointment outcome
                            AppointmentOutcome appointmentOutcome = appointmentController
                                    .viewAppointmentOutcome(appointmentId);

                            if (appointmentOutcome == null) {
                                Printer.print("\nNo appointment outcome found for ID: " + appointmentId, lang);
                                
                            }else{
                                Printer.printAppointmentOutcome(appointmentOutcome);

                            }
                        } catch (NumberFormatException e) {
                            Printer.print("Invalid appointment ID format. Please try again.", lang);
                        }
                    }
                    break;
                case 2: // (2) Update Prescription Status
                    Util.clearConsole();
                    Printer.printHeader("----------------- Update Prescription Status -----------------");

                    for (HashMap.Entry<Integer, AppointmentOutcome> entry : appointmentController
                            .viewAllAppointmentOutcomes().entrySet()) {
                        AppointmentOutcome apptOutcome = entry.getValue();

                        if (apptOutcome.getStatus() == PrescriptionStatus.PENDING) {
                            // Util.clearConsole();

                            Printer.print("\nAppointment Outcome ID: " + apptOutcome.getAppointmentId(), lang);
                            Printer.print("Service Type: " + apptOutcome.getServiceType(), lang);
                            Printer.print("Status: " + apptOutcome.getStatus(), lang);

                            // Prompt user to update the prescription status
                            Printer.print(
                                    "Do you want to approve the prescription status? Enter '1' to approve, or press Enter to skip: ",
                                    lang);

                            String userInput = scanner.nextLine().trim(); // Trim to remove any extra spaces
                            // Check for approval input
                            if (userInput.equals("1")) {
                                // Update the status to DISPENSED (or another desired status)
                                apptOutcome.setStatus(PrescriptionStatus.DISPENSED);
                                Printer.print("Prescription status updated to DISPENSED for Appointment Outcome ID: "
                                        + apptOutcome.getAppointmentId(), lang);
                            } else {
                                Printer.print("Skipped updating the prescription status for Appointment Outcome ID: "
                                        + apptOutcome.getAppointmentId(), lang);
                            }
                            System.out.println("\n");
                       
                        }
                    }
                    Printer.print("No more prescription requests available! \n", lang);
                    break;
                case 3: // (3) View Medication Inventory
                    boolean keepRunning = true;

                    while (keepRunning) {
                        Util.clearConsole(); // Clear the console to make the display cleaner
                        Printer.printHeader("----------------- Medication Inventory -----------------");
                       
                        HashMap<String, Medication> medications = medicationController.viewMedications();
                        Printer.printMedications(medications);
                        Printer.print(
                                "\nEnter 'q' to return to the main menu or any other key to refresh the inventory.",
                                lang);

                        // Wait for user input
                        char input = sc.next().charAt(0); // Read the first character of user input



                        if (input == 'q') {
                            keepRunning = false; // Exit the loop and return to main menu
                        }
                    }

                    break;
                // (4) Submit Replenishment Request
                case 4:
                    submitReplenishmentRequest(pharmacist);

                    break;
                // (5) Logout
                case 5: // (5) Update Personal Info
                    updatePersonalInfo(pharmacist);
                    break;
                case 6: // (6) Logout
                    Printer.print("\nLogging out...", lang);
                    return;
                default:
                    Printer.print("Invalid choice, please choose again", lang);
                    break;
            }
        } while (choice <= 5);
        sc.close();
    }

    /**
     * Allows the pharmacist to submit a replenishment request for medications.
     * 
     * This method prompts the pharmacist to either enter a Medication ID, view all available medications, 
     * or return to the main menu. If a valid Medication ID is provided, the pharmacist is asked to specify 
     * the amount requested. The request is processed only if the medication's stock is below the low-stock 
     * threshold, and the pharmacist must authenticate their action with a password.
     * 
     * The pharmacist can continue creating replenishment requests or choose to return to the main menu after 
     * each request submission. If authentication fails, the pharmacist is informed and the request is not submitted.
     * 
     * @param pharmacist the {@link Pharmacist} object representing the pharmacist submitting the replenishment request.
     */
    public void submitReplenishmentRequest(Pharmacist pharmacist) {
        boolean continueRequest = true;
        Scanner scanner = new Scanner(System.in); // Use only one Scanner object

        while (continueRequest) {
            // Util.clearConsole(); // Clear the console for a clean display
            Printer.printHeader("----------------- Submit Replenishment Request -----------------");

            // Prompt user for input, allowing them to enter Medication ID, 'a' to view all
            // medications, or 'q' to return to the main menu
            Printer.print(
                    "Enter Medication ID, enter 'a' to view all medications, or enter 'q' to return to the main menu: ",
                    lang);
            String userInput = scanner.nextLine().trim(); // Read user input and remove any leading/trailing whitespace

            if (userInput.equalsIgnoreCase("q")) {
                // If the user presses 'q', exit the loop to return to the main menu
                Printer.print("Returning to the main menu...", lang);
                continueRequest = false;
            } else if (userInput.equalsIgnoreCase("a")) {
                // If the user presses 'a', display all medications
                Util.clearConsole();
                Printer.print("All medications: ", lang);
                HashMap<String, Medication> medications = medicationController.viewMedications();
                Printer.printMedications(medications);

                Printer.print("\nPress any key to return to the request.", lang);
                scanner.nextLine(); // Wait for user to press any key to return to the request
            } else if (!userInput.isEmpty() && MedicationDB.findMedicationByName(userInput)!=null) {
                // If the user enters a valid Medication ID, prompt for the amount requested
                Printer.print("Enter Amount Requested for Medication ID " + userInput + ": ", lang);
                String amountRequested = scanner.nextLine().trim();

                if (!amountRequested.isEmpty()) {
                    try {
                        // Convert the requested amount to an integer
                        int requestAmount = Integer.parseInt(amountRequested);

                        // Find the medication by ID (if applicable)
                        Medication target = medicationController.viewMedicationById(userInput);
                        if (target != null && target.getCurrentStock() < target.getLowStockLevelAlert()) {
                            // Authentication process
                            Printer.print("Enter password to authenticate: ", lang);
                            String password = scanner.nextLine();

                            if (pharmacist.getPassword().equals(Util.hashPassword(password))) {
                                Printer.print(
                                        "Authentication successful. Sending Replenishment Request to Administrator...",
                                        lang);

                                // Create and add the replenishment request
                                replenishRequestController.addReplenishRequest(new ReplenishRequest(pharmacist.getId(),
                                        userInput, ReplenishRequest.ReplenishRequestStatus.PENDING,
                                        requestAmount, LocalDate.now()), false);

                                // After successful submission, ask if the user wants to create another request
                                Printer.print("\nReplenishment request submitted successfully!", lang);

                                // Ask user whether they want to create another request or return to the main
                                // menu
                                Printer.print(
                                        "Enter 'q' to return to the main menu or any other key to create another request: ",
                                        lang);
                                char choice = scanner.next().charAt(0); // Read the user's choice

                                // Consume the newline character left by scanner.next() to prevent issues in the
                                // next iteration
                                scanner.nextLine(); // This consumes the remaining newline character

                                if (choice == 'q' || choice == 'Q') {
                                    continueRequest = false;
                                } else {

                                }
                            } else {
                                // If authentication fails, print a message
                                Printer.print("Authentication failed. Please check the password.", lang);
                            }
                        } else if (target == null) {
                            // If the medication is not found or stock levels are fine, notify the user
                            Printer.print("Medication not found", lang);
                        } else {
                            Printer.print("Stock level is sufficient", lang);
                        }
                    } catch (NumberFormatException e) {
                        // If the amount is not a valid number, notify the user
                        Printer.print("Invalid amount entered. Please enter a valid number.", lang);
                        scanner.nextLine(); // Consume any extra newline characters
                    }
                } else {
                    Printer.print("Amount Requested cannot be empty.", lang);
                    break;
                }
             }else if(MedicationDB.findMedicationByName(userInput)==null){
                Printer.print("Cannot find medication: ",lang);
             } 
            
            else {
                // Handle case where the input is empty or invalid
                Printer.print(
                        "Invalid input. Please enter a valid Medication ID, 'a' to view all medications, or 'q' to return to the main menu.",
                        lang);
            }

        }
        // Close the scanner once done
        // scanner.close();
    }

    /**
     * Allows the pharmacist to update their personal information.
     * 
     * This method prompts the pharmacist to update various personal details such as their name, username, password, 
     * gender, contact number, and email address. The user can either skip updating a particular field by pressing 
     * Enter or provide new information. If any details are updated, the pharmacist's information is saved and updated 
     * via the pharmacistController. If no updates are made, a message is displayed indicating that no information was changed.
     * 
     * @param pharmacist the {@link Pharmacist} object representing the pharmacist whose information is being updated.
     */
    public void updatePersonalInfo(Pharmacist pharmacist) {
        boolean isUpdated = false; // Flag to track updates
    
        // Update Name
        Printer.print("Enter new name (or press Enter to skip): ", lang);
        String name = Input.inputName(lang, true);
        if (!name.isEmpty()) {
            pharmacist.setName(name);
            isUpdated = true; 
        }
    
        // Update Username
        Printer.print("Enter new username (Enter 'a' to auto-generate or enter a valid username or Press Enter to skip): ", lang);
        String username = Input.inputUsername(lang, name, "staff", true);
        if (username != null) {
            pharmacist.setUsername(username); 
            isUpdated = true;
        }
    
        // Update Password
        Printer.print("Enter new password (or press Enter to skip): ", lang);
        String password = Input.inputPassword(lang, true);
        if (!password.isEmpty()) {
            pharmacist.setPassword(Util.hashPassword(password));
            isUpdated = true;
        }
    
        // Update Gender
        Printer.print("Enter new gender (press Enter to skip): ", lang);
        String gender = Input.inputGender(lang, true);
        if (!gender.isEmpty()) {
            pharmacist.setGender(gender);
            isUpdated = true;
        }
    
        // Update Contact Number
        Printer.print("Enter new contact number (or press Enter to skip): ", lang);
        String contactNumber = Input.inputContactNumber(lang, true, "staff");
        if (!contactNumber.isEmpty()) {
            pharmacist.setContactNumber(contactNumber);
            isUpdated = true;
        }
    
        // Update Email Address
        Printer.print("Enter new email address (or press Enter to skip): ", lang);
        String emailAddress = Input.inputEmailAddress(lang, true, name, "staff");
        if (!emailAddress.isEmpty()) {
            pharmacist.setEmailAddress(emailAddress);
            isUpdated = true;
        }
    
        // Check if any updates were made
        if (isUpdated) {
            String response = pharmacistController.updatePersonalInfo(pharmacist.getId(), pharmacist, null);
            Printer.print(response, lang);
        } else {
            Printer.print("No information was updated.", lang);
        }
    }
    
}