package boundary.Applications;

import model.Medication.Medication;
import model.Medication.ReplenishRequest;
import model.Users.User;
import repository.Users.PatientDB;
import repository.Users.StaffDB;
import repository.Appointment.AppointmentDB;
import repository.Medication.MedicationDB;
import model.Users.Admin;
import model.Users.Doctor;
import model.Users.Patient;
import model.Users.Pharmacist;
import controller.Users.StaffController;
import controller.Users.DoctorController;
import controller.Users.PatientController;
import controller.Users.AdminController;
import controller.Users.PharmacistController;
import controller.Appointment.AppointmentController;
import controller.Medication.MedicationController;
import controller.Medication.ReplenishRequestController;
import DTO.AppointmentAndOutcomeDTO;
import utils.Utility.Input;
import utils.Utility.Printer;
import utils.Utility.Util;
import utils.Utility.Config;
import utils.Utility.IdGenerator;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * The {@code AdminApp} class provides functionality for an administrator to
 * manage users,
 * update their personal information, and manage medications.
 * <p>
 * This application is designed to facilitate the following actions for an
 * administrator:
 * </p>
 * <ul>
 * <li>Managing users (add, delete)</li>
 * <li>Updating personal information of the administrator</li>
 * <li>Managing medications (add new, update stock and prices)</li>
 * </ul>
 * <p>
 * The class leverages several utility methods for input validation and for
 * displaying prompts to the user.
 * It works with various controllers (e.g., {@link StaffController},
 * {@link MedicationController}, etc.)
 * for interacting with the database and performing the necessary CRUD
 * operations.
 * </p>
 * 
 * <p>
 * Key functionalities include:
 * </p>
 * <ul>
 * <li>Display a main menu for user management and other admin options</li>
 * <li>Add new users of various roles (pharmacists, doctors, admins)</li>
 * <li>Delete existing users after verifying their identity</li>
 * <li>Allow the admin to update their personal information (name, contact,
 * password, etc.)</li>
 * <li>Add new medications to the system with details like stock levels and
 * price</li>
 * </ul>
 * 
 * <p>
 * The application follows an interactive flow where the admin provides input
 * through the console,
 * with guidance and validation provided for each action.
 * </p>
 * 
 * @see StaffController
 * @see MedicationController
 * @see Admin
 * @see Pharmacist
 * @see Doctor
 * @see Util
 */
public class AdminApp {
    // Private static instance of AdminApp (Singleton)
    private static AdminApp instance;
    private Scanner scanner = new Scanner(System.in);
    private String lang = Config.lang;

    // Controllers instantiation
    private StaffController<User> staffController = new StaffController<User>();
    private AdminController adminController = new AdminController();
    private DoctorController doctorController = new DoctorController();
    private PharmacistController pharmacistController = new PharmacistController();
    private PatientController patientController = new PatientController();
    private MedicationController medicationController = new MedicationController();
    private AppointmentController appointmentController = new AppointmentController();
    private ReplenishRequestController replenishRequestController = new ReplenishRequestController();

    // Private constructor to prevent external instantiation
    /**
     * Private constructor for the {@code AdminApp} class.
     * <p>
     * This constructor is private to prevent external instantiation of the class,
     * ensuring that the class cannot be instantiated from outside its own
     * implementation.
     * Use this constructor to perform any necessary initialization or setup for the
     * class.
     * </p>
     */
    private AdminApp() {
        // Initialization or any required setup can be done here
    }

    // Public static method to get the single instance of AdminApp
    /**
     * Returns the single instance of the {@code AdminApp} class.
     * <p>
     * This method implements the Singleton design pattern using lazy
     * initialization.
     * The instance is created only when it is accessed for the first time.
     * </p>
     *
     * @return the single instance of the {@code AdminApp} class
     */
    public static AdminApp getInstance() {
        if (instance == null) {
            // Create the instance only when it's needed (Lazy initialization)
            instance = new AdminApp();
        }
        return instance;
    }

    // Method to initialize and start the admin application
    /**
     * Initializes the {@code AdminApp} with the specified administrator.
     * <p>
     * This method starts the application using the provided {@code Admin} instance.
     * It serves as the entry point to set up and launch the application with the
     * required
     * administrator privileges or context.
     * </p>
     *
     * @param admin the {@code Admin} instance used to start the application
     */
    public void initializeApp(Admin admin) {
        startAdminApp(admin);
    }

    // Instance method to start the admin application
    /**
     * Starts the {@code AdminApp} and provides the administrative interface for
     * managing
     * the hospital's resources and operations.
     * <p>
     * This method displays menus and handles user inputs for various administrative
     * tasks,
     * including:
     * </p>
     * <ul>
     * <li>Viewing and managing hospital staff</li>
     * <li>Viewing and managing appointments</li>
     * <li>Viewing and managing medication inventory</li>
     * <li>Handling replenishment requests</li>
     * <li>Updating personal information</li>
     * <li>Logging out</li>
     * </ul>
     * <p>
     * The method also supports language selection and dynamically adjusts its
     * behavior based
     * on user inputs.
     * </p>
     *
     * @param admin the {@code Admin} instance representing the current logged-in
     *              administrator
     */
    private void startAdminApp(Admin admin) {
        Scanner sc = new Scanner(System.in);
        String choice;
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
            // System.out.println(Config.lang);
            // Printer.print("Changing language", lang);
        }
        do {
            menuHeader = "Administrator Menu";
            menuOptions = Arrays.asList(
                    "View Hospital Staff",
                    "Manage Hospital Staff",
                    "View Appointments Details",
                    "View Medication Inventory",
                    "Manage Medication Inventory",
                    "Approve Replenishment Requests",
                    "Update Personal Info",
                    "Logout");
            Printer.printMenu(menuHeader, menuOptions);
            choice = sc.nextLine().trim(); // Read the input as a string

            switch (choice) {
                case "1":
                    // View hospital staff
                    String viewStaffChoice;
                    Util.clearConsole();
                    do {
                        menuHeader = "View Hospital Staff Menu";
                        menuOptions = Arrays.asList(
                                "View all Administrators",
                                "View all Doctors",
                                "View all Pharmacists",
                                "View all Staffs",
                                "Back to Main Menu");
                        Printer.printMenu(menuHeader, menuOptions);

                        viewStaffChoice = sc.nextLine().trim(); // Read the input as a string
                        // sc.nextLine();

                        switch (viewStaffChoice) {
                            case "1":
                                // Print all administrators
                                List<Admin> admins = adminController.viewAll();
                                Printer.printUsers(admins);
                                break;
                            case "2":
                                // Print all doctors
                                List<Doctor> doctors = doctorController.viewAll();
                                Printer.printUsers(doctors);
                                break;
                            case "3":
                                // Print all pharmacists
                                List<Pharmacist> pharmacists = pharmacistController.viewAll();
                                Printer.printUsers(pharmacists);
                                break;
                            case "4":
                                // Print all users
                                List<Admin> allAdmins = adminController.viewAll();
                                Printer.printUsers(allAdmins);
                                List<Doctor> allDoctors = doctorController.viewAll();
                                Printer.printUsers(allDoctors);
                                List<Pharmacist> allPharmacists = pharmacistController.viewAll();
                                Printer.printUsers(allPharmacists);
                                break;
                            case "5":
                                Printer.print("Returning to main menu...", lang);
                                break;
                            default:
                                Printer.print("Invalid choice. Please try again.", lang);
                                break;
                        }
                    } while (!viewStaffChoice.equals("5"));
                    break;

                case "2":
                    // Manage hospital staff
                    if (manageUsers()) {
                        Printer.print("Returning to Administrator Menu...", lang);
                    }
                    break;

                case "3":
                    // View appointment details
                    String viewApptChoice;
                    menuLoop: // Label for the do-while loop
                    do {
                        menuHeader = "View Appointment Menu";
                        menuOptions = Arrays.asList(
                                "View by Patient",
                                "View by Doctor",
                                "Back to Main Menu");
                        Printer.printMenu(menuHeader, menuOptions);

                        viewApptChoice = sc.nextLine().trim(); // Read the input as a string

                        switch (viewApptChoice) {
                            case "1":
                                // View appointment details by patient ID
                                Printer.print("Enter Patient ID or enter 'q' to quit: ", lang);
                                String patientId = sc.nextLine(); // Read the patient ID input

                                // Check if the user pressed 'q' to quit
                                if (patientId.equalsIgnoreCase("q")) {
                                    Printer.print("Cancelled. No further action taken.", lang);
                                    break; // Exit the current case in the switch
                                }

                                // Get the patient by ID
                                Patient patient = patientController.getUserById(patientId);
                                if (patient != null) {
                                    List<AppointmentAndOutcomeDTO> pAppmt = appointmentController
                                            .viewAppointmentAndOutcomesByPatientId(patientId);

                                   // AppointmentDB.printAllAppointments();
                                    if (pAppmt == null || pAppmt.isEmpty()) {
                                        Printer.print("\nNo appointment found.", lang);
                                    } else {
                                        Printer.printAppointmentAndOutcome(pAppmt);
                                    }
                                } else {
                                    Printer.print("Patient not found.", lang);
                                }

                                break; // Break the current case in the switch
                            case "2":
                                // View appointment details by doctor ID
                                Printer.print("Enter Doctor(Staff) ID: ", lang);
                                String doctorId = sc.nextLine();
                                Doctor doctor = (Doctor) doctorController.getUserById(doctorId);
                                if (doctor != null) {
                                    List<AppointmentAndOutcomeDTO> dAppmt = appointmentController
                                            .viewAppointmentAndOutcomesByDoctorId(doctorId);
                                    if (dAppmt == null || dAppmt.isEmpty()) {
                                        Printer.print("\nNo appointment found.", lang);
                                    } else {
                                        Printer.printAppointmentAndOutcome(dAppmt);
                                    }
                                } else {
                                    Printer.print("Doctor not found.", lang);
                                }

                                break; // Break the current case in the switch
                            case "3":
                                Printer.print("Returning to main menu...", lang);
                                break menuLoop; // Exit the do-while loop entirely
                            default:
                                Printer.print("Invalid choice. Please try again.", lang);
                                break; // Break the current case in the switch
                        }
                    } while (true); // Keep looping until "3" is selected
                    break;

                case "4":
                    // View medication inventory
                    Printer.print("Viewing Medication Inventory...", lang);
                    HashMap<String, Medication> medications = medicationController.viewMedications();
                    Printer.printMedications(medications);
                    break;

                case "5":
                    // Manage medication inventory
                    String inventoryChoice;
                    Util.clearConsole();
                    menuLoop: // Label for the do-while loop
                    do {
                        menuHeader = "Manage Medication Inventory Menu";
                        menuOptions = Arrays.asList(
                                "Add New Medication",
                                "Remove Medication by Name",
                                "Add Medication Stock",
                                "Remove Medication Stock",
                                "Update Low Stock Level Alert",
                                "Back to Main Menu");
                        Printer.printMenu(menuHeader, menuOptions);

                        inventoryChoice = sc.nextLine().trim(); // Read the input as a string
                        // s sc.nextLine();

                        switch (inventoryChoice) {
                            case "1":
                                // Add new medication
                                addNewMedication();
                                break;
                            case "2":
                                // Remove medication by name
                                Printer.print("Search for a Medication to Remove: ", lang);
                                String medNameToRemove = sc.nextLine();

                                // Try to find the medication by its exact name
                                Medication medicationToRemove;
                                medicationToRemove = medicationController
                                        .viewMedicationById(medNameToRemove);

                                if (medicationToRemove != null) {
                                    // If the medication is found, delete it
                                    medicationController.deleteMedication(medNameToRemove);
                                    Printer.print("Medication removed: " + medNameToRemove, lang);
                                } else {
                                    // If the medication is not found, display similar medications
                                    Printer.print("Medication not found. Displaying similar medications...", lang);

                                    MedicationDB.displayAllSimilarMedications(medNameToRemove);

                                    // Query for user input
                                    Printer.print("Enter the medication name to delete or enter 'q' to quit:", lang);
                                    String userChoice = sc.nextLine();
                                    medicationToRemove = medicationController
                                            .viewMedicationById(userChoice);
                                    if (!userChoice.equalsIgnoreCase("q") && medicationToRemove != null) {
                                        // If the user entered a valid medication name, delete it
                                        medicationController.deleteMedication(userChoice);
                                        Printer.print("Medication removed: " + userChoice, lang);
                                    } else if (userChoice.equalsIgnoreCase("q")) {
                                        // If the user presses 'q', quit without deleting
                                        Printer.print("Cancelled. No medication was removed.", lang);
                                    } else {
                                        // Handle invalid input
                                        Printer.print("Invalid choice. No medication removed.", lang);
                                    }
                                }
                                break;
                            case "3":
                                // Increase stock of a current medication
                                Printer.print("Enter Medication Name to Increase Stock: ", lang);
                                String medIdToAdd = sc.nextLine();

                                Medication medStockToAdd;
                                medStockToAdd = medicationController.viewMedicationById(medIdToAdd);

                                if (medStockToAdd != null) {
                                    // If the medication is found, prompt for the stock count
                                    int stockCount = 0;
                                    while (true) {
                                        Printer.print("Enter stock count to increase: ", lang);
                                        String stockCountInput = sc.nextLine();

                                        if (stockCountInput.equalsIgnoreCase("q")) {
                                            Printer.print("Cancelled. No medication stock was increased.", lang);
                                            break; // Exit the loop and cancel operation
                                        }

                                        try {
                                            stockCount = Integer.parseInt(stockCountInput); // Try to parse the input to
                                                                                            // an integer

                                            // Validate that the stock count is a positive number
                                            if (stockCount <= 0) {
                                                Printer.print("Please enter a positive number for stock count.", lang);
                                            } else {
                                                // Increase the stock by the specified count
                                                medicationController.increaseMedicationStock(medIdToAdd, stockCount);
                                                Printer.print("Medication Stock Increased: " + medIdToAdd + " by "
                                                        + stockCount + " units.", lang);
                                                break; // Exit the loop after successful increase
                                            }
                                        } catch (NumberFormatException e) {
                                            // Handle non-numeric input
                                            Printer.print("Invalid input. Please enter a valid number or 'q' to quit.",
                                                    lang);
                                        }
                                    }
                                } else {
                                    // If the medication is not found, display similar medications
                                    Printer.print("Medication not found. Displaying similar medications...", lang);
                                    MedicationDB.displayAllSimilarMedications(medIdToAdd);

                                    Printer.print("Enter the medication name to increase stock or enter 'q' to quit:",
                                            lang);
                                    String userChoice = sc.nextLine();
                                    medStockToAdd = medicationController.viewMedicationById(userChoice);

                                    if (!userChoice.equalsIgnoreCase("q") && medStockToAdd != null) {
                                        // If the user entered a valid medication name, prompt for the stock count
                                        int stockCount = 0;
                                        while (true) {
                                            Printer.print("Enter stock count to increase: ", lang);
                                            String stockCountInput = sc.nextLine();

                                            if (stockCountInput.equalsIgnoreCase("q")) {
                                                Printer.print("Cancelled. No medication stock was increased.", lang);
                                                break; // Exit the loop and cancel operation
                                            }

                                            try {
                                                stockCount = Integer.parseInt(stockCountInput); // Try to parse the
                                                                                                // input to an integer

                                                // Validate that the stock count is a positive number
                                                if (stockCount <= 0) {
                                                    Printer.print("Please enter a positive number for stock count.",
                                                            lang);
                                                } else {
                                                    // Increase the stock by the specified count
                                                    medicationController.increaseMedicationStock(userChoice,
                                                            stockCount);
                                                    Printer.print("Medication Stock Increased: " + userChoice + " by "
                                                            + stockCount + " units.", lang);
                                                    break; // Exit the loop after successful increase
                                                }
                                            } catch (NumberFormatException e) {
                                                // Handle non-numeric input
                                                Printer.print(
                                                        "Invalid input. Please enter a valid number or 'q' to quit.",
                                                        lang);
                                            }
                                        }
                                    } else if (userChoice.equalsIgnoreCase("q")) {
                                        // If the user presses 'q', quit without making any changes
                                        Printer.print("Cancelled. No medication stock was increased.", lang);
                                    } else {
                                        // Handle invalid input
                                        Printer.print("Invalid choice. No medication stock was increased.", lang);
                                    }
                                }

                                break;

                            case "4":
                                // Decrease stock of a current medication
                                Printer.print("Enter Medication Name to Decrease Stock: ", lang);
                                String medIdToRemove = sc.nextLine();

                                Medication medStockToRemove = medicationController.viewMedicationById(medIdToRemove);

                                // Check if medication exists in the database
                                if (medStockToRemove != null) {
                                    // If the medication is found, ask for the stock count
                                    int stockCount = 0;
                                    while (true) {
                                        Printer.print("Enter the number of units to decrease the stock: ", lang);
                                        String stockCountInput = sc.nextLine();

                                        if (stockCountInput.equalsIgnoreCase("q")) {
                                            Printer.print("Cancelled. No medication stock was decreased.", lang);
                                            break; // Exit the loop and cancel operation
                                        }

                                        try {
                                            stockCount = Integer.parseInt(stockCountInput); // Try to parse the input to
                                                                                            // an integer

                                            // Validate the stock count
                                            if (stockCount <= 0) {
                                                Printer.print("Please enter a positive number for stock count.", lang);
                                            } else if (stockCount > medStockToRemove.getCurrentStock()) {
                                                // If the stock count is greater than the current stock, prompt the user
                                                // to re-enter
                                                Printer.print(
                                                        "Cannot decrease more than the available stock. Available stock: "
                                                                + medStockToRemove.getCurrentStock(),
                                                        lang);
                                            } else {
                                                // Decrease the medication stock by the entered stock count
                                                medicationController.decreaseMedicationStock(medIdToRemove, stockCount);
                                                Printer.print("Medication Stock Decreased: " + medIdToRemove + " by "
                                                        + stockCount + " units", lang);
                                                break; // Exit the loop after successful decrease
                                            }
                                        } catch (NumberFormatException e) {
                                            // Handle non-numeric input
                                            Printer.print("Invalid input. Please enter a valid number or 'q' to quit.",
                                                    lang);
                                        }
                                    }
                                } else {
                                    // If medication is not found, display similar medications
                                    Printer.print("Medication not found. Displaying similar medications...", lang);

                                    // Get and display similar medications
                                    MedicationDB.displayAllSimilarMedications(medIdToRemove);

                                    Printer.print("Enter the medication name to decrease stock or enter 'q' to quit:",
                                            lang);
                                    String userChoice = sc.nextLine();
                                    medStockToRemove = medicationController.viewMedicationById(userChoice);

                                    if (!userChoice.equalsIgnoreCase("q") && medStockToRemove != null) {
                                        // If the user entered a valid medication name, prompt for stock count
                                        int stockCount = 0;
                                        while (true) {
                                            Printer.print("Enter the number of units to decrease the stock: ", lang);
                                            String stockCountInput = sc.nextLine();

                                            if (stockCountInput.equalsIgnoreCase("q")) {
                                                Printer.print("Cancelled. No medication stock was decreased.", lang);
                                                break; // Exit the loop and cancel operation
                                            }

                                            try {
                                                stockCount = Integer.parseInt(stockCountInput); // Try to parse the
                                                                                                // input to an integer

                                                // Validate the stock count
                                                if (stockCount <= 0) {
                                                    Printer.print("Please enter a positive number for stock count.",
                                                            lang);
                                                } else if (stockCount > medStockToRemove.getCurrentStock()) {
                                                    // If the stock count is greater than the current stock, prompt the
                                                    // user to re-enter
                                                    Printer.print(
                                                            "Cannot decrease more than the available stock. Available stock: "
                                                                    + medStockToRemove.getCurrentStock(),
                                                            lang);
                                                } else {
                                                    // Decrease the medication stock by the entered stock count
                                                    medicationController.decreaseMedicationStock(userChoice,
                                                            stockCount);
                                                    Printer.print("Medication Stock Decreased: " + userChoice + " by "
                                                            + stockCount + " units", lang);
                                                    break; // Exit the loop after successful decrease
                                                }
                                            } catch (NumberFormatException e) {
                                                // Handle non-numeric input
                                                Printer.print(
                                                        "Invalid input. Please enter a valid number or 'q' to quit.",
                                                        lang);
                                            }
                                        }
                                    } else if (userChoice.equalsIgnoreCase("q")) {
                                        // If the user presses 'q', quit without decreasing stock
                                        Printer.print("Cancelled. No medication stock was decreased.", lang);
                                    } else {
                                        // Handle invalid input
                                        Printer.print("Invalid choice. No medication stock was decreased.", lang);
                                    }
                                }

                                break;
                            case "5":// Update the Low Stock Level of a current medication
                                Printer.print("Enter Medication Name to Update Low Stock Level: ", lang);
                                String medIdToUpdate = sc.nextLine();

                                Medication medToUpdate;
                                medToUpdate = medicationController.viewMedicationById(medIdToUpdate);

                                if (medToUpdate != null) {
                                    // If the medication is found, prompt for the new low stock level
                                    Printer.print("Enter the new Low Stock Level for " + medIdToUpdate + ": ", lang);
                                    int newLowStockLevel = sc.nextInt();
                                    sc.nextLine(); // Consume the newline character left by nextInt()

                                    // Update the low stock level of the medication
                                    medToUpdate.setLowStockLevelAlert(newLowStockLevel);
                                    medicationController.updateLowStockLevelAlert(medIdToUpdate, newLowStockLevel);
                                    Printer.print("Medication Low Stock Level Updated: " + medIdToUpdate, lang);
                                } else {
                                    // If the medication is not found, display similar medications
                                    Printer.print("Medication not found. Displaying similar medications...", lang);

                                    // Get and display similar medications
                                    MedicationDB.displayAllSimilarMedications(medIdToUpdate);

                                    // Query for user input
                                    Printer.print(
                                            "Enter the medication name to update low stock level or enter 'q' to quit:",
                                            lang);
                                    String userChoice = sc.nextLine();
                                    medToUpdate = medicationController.viewMedicationById(userChoice);

                                    if (!userChoice.equalsIgnoreCase("q") && medToUpdate != null) {
                                        // If the user entered a valid medication name, prompt for the new low stock
                                        // level
                                        Printer.print("Enter the new Low Stock Level for " + userChoice + ": ", lang);
                                        int newLowStockLevel = sc.nextInt();
                                        // Update the low stock level of the medication
                                        medicationController.updateLowStockLevelAlert(userChoice, newLowStockLevel);

                                        // Printer.print("Medication Low Stock Level Updated: " + userChoice, lang);
                                    } else if (userChoice.equalsIgnoreCase("q")) {
                                        // If the user presses 'q', quit without updating the low stock level
                                        Printer.print("Cancelled. No low stock level was updated.", lang);
                                    } else {
                                        // Handle invalid input
                                        Printer.print("Invalid choice. No low stock level was updated.", lang);
                                    }
                                }

                                break;
                            case "6":
                                Printer.print("Returning to main menu...", lang);
                                break menuLoop;
                            default:
                                Printer.print("Invalid choice. Please try again.", lang);
                                break;
                        }
                    } while (inventoryChoice != "5");
                    break;

                case "6":
                    // Handle replenishment request
                    if (handleReplenishmentRequest()) {
                        Printer.print("Returning to Administrator Menu...", lang);
                    }
                    break;

                case "7": // (5) Update Personal Info
                    updatePersonalInfo(admin);
                    break;
                case "8": // (6) Logout
                    Printer.print("\nLogging out...", lang);
                    return;
                default:
                    Printer.print("Invalid choice, please choose again", lang);
                    break;
            }

        } while (choice != "7");
        sc.close();
    }

    /**
     * Provides the user management interface within the {@code AdminApp}.
     * <p>
     * This method displays a menu for managing users and handles user input for
     * various
     * actions, including:
     * </p>
     * <ul>
     * <li>Adding a new user</li>
     * <li>Deleting an existing user</li>
     * <li>Returning to the main menu</li>
     * </ul>
     * <p>
     * The method runs in a loop until the administrator selects the option to
     * return
     * to the main menu.
     * </p>
     *
     * @return {@code true} when the administrator chooses to return to the main
     *         menu
     */
    public boolean manageUsers() {
        while (true) { // Main menu loop
            // Display main menu
            String menuHeader = "User Management Menu";
            List<String> menuOptions = Arrays.asList(
                    "Add a user",
                    "Delete a user",
                    "Back to Main Menu");
            Printer.printMenu(menuHeader, menuOptions);

            String mainMenuChoice = scanner.nextLine();

            switch (mainMenuChoice) {
                case "1":
                    addUser();
                    break;
                case "2":
                    deleteUser();
                    break;
                case "3":
                    Printer.print("Returning to Administrator Menu...", lang);
                    return true; // Indicates exit to the main menu
                default:
                    Printer.print("Invalid option. Please try again.", lang);
            }
        }
    }

    /**
     * Allows the deletion of a user (staff member) from the system.
     * <p>
     * This method prompts the user to enter the name of the person they wish to
     * delete. If the user enters a valid name, the system displays users with
     * similar names.
     * The user can then select the specific user by ID for deletion. If the user
     * opts to cancel the deletion process or go back to the main menu, the
     * operation is aborted, and no user is deleted.
     * The method performs the following steps:
     * </p>
     * <ul>
     * <li>Prompt the user to enter a name or 'q' to go back to the main menu.</li>
     * <li>Displays a list of users whose names are similar to the entered
     * name.</li>
     * <li>Asks the user to confirm the ID of the user to be deleted or 'q' to
     * search again.</li>
     * <li>If a valid user ID is provided, the user is removed from the system.</li>
     * <li>Allows the user to return to the search process or main menu based on the
     * input.</li>
     * </ul>
     * <p>
     * If a valid user is found and deleted, a success message is printed.
     * </p>
     * 
     */
    public void deleteUser() {
        while (true) { // Loop for deleting user
            Printer.print("Enter the name of the person to delete (or 'q' to go back to main menu): ", lang);
            String nameToDelete = scanner.nextLine();

            if (nameToDelete.equals("q")) {
                break; // Go back to main menu
            }

            // Assuming StaffDB.displaySimilarUsers() displays users similar to nameToDelete
            StaffDB.displaySimilarUsers(nameToDelete);
            Printer.print("Enter the ID of the user you want to delete (or 'q' to search again): ", lang);
            String userIdToDelete = scanner.nextLine();

            if (userIdToDelete.equals("q")) {
                continue; // Go back to searching for a person to delete
            }

            if (staffController.getUserById(userIdToDelete) != null) {
                staffController.removeStaff(userIdToDelete);
                // change this to bool and return true/ false
                Printer.print("Successfully removed user", lang);
            }
        }
    }

    /**
     * Facilitates adding a new user (Pharmacist, Doctor, or Admin) to the system in
     * the {@code AdminApp}.
     * <p>
     * This method guides the administrator through the process of creating a user
     * by:
     * </p>
     * <ul>
     * <li>Displaying a menu to select the user role</li>
     * <li>Prompting for required user details such as name, gender, username, date
     * of birth, email, and contact number</li>
     * <li>Handling role-specific details (e.g., license number for Pharmacists,
     * patient IDs for Doctors)</li>
     * <li>Auto-generating a unique staff ID for the new user</li>
     * <li>Setting a default password ("password123") with hashing</li>
     * <li>Adding the user to the appropriate controller for storage and
     * management</li>
     * </ul>
     * <p>
     * This method also validates user input through helper methods and provides
     * feedback for invalid or incomplete input.
     * </p>
     * <p>
     * Supported roles:
     * </p>
     * <ul>
     * <li>Pharmacist: Requires a license number</li>
     * <li>Doctor: Requires a list of associated patient IDs</li>
     * <li>Admin: General staff information</li>
     * </ul>
     */
    public void addUser() {
        String menuHeader = "Add User Menu";
        List<String> menuOptions = Arrays.asList(
                "Pharmacist",
                "Doctor",
                "Admin");
        Printer.printMenu(menuHeader, menuOptions);

        String roleChoice = scanner.nextLine();
        while (!roleChoice.equals("1") && !roleChoice.equals("2") && !roleChoice.equals("3")) {
            Printer.print("Invalid selection. Please choose a valid role:\n", lang);
            Printer.printMenu(menuHeader, menuOptions);

            roleChoice = scanner.nextLine().trim(); // Re-prompt the user for input
        }
        // Print instructions for name input
        Printer.print("\nEnter name (Enter in this format 'FIRSTNAME LASTNAME' ): ", lang); // Example instruction
        String name = Input.inputName(lang, false); // User enters their name

        // Print instructions for gender input
        Printer.print("\nEnter gender (M/F): ", lang); // Example instruction
        String gender = Input.inputGender(lang, false); // User enters gender

        // Print instructions for username input
        Printer.print("\nEnter username (Enter 'a' to auto-generate or enter a valid username ): ",
                lang);
        // instruction
        String username = Input.inputUsername(lang, name, "staff", false); // User enters username

        // Print instructions for password input (Note: "defaultPassword123" is preset,
        // but you can change it)
        Printer.print("\nPassword has been set to the default. You can change it later: ", lang); // Example instruction
        String password = Util.hashPassword("password123"); // Pre-set default password

        // Print instructions for date of birth input
        Printer.print("\nEnter date of birth (YYYY-MM-DD): ", lang); // Example instruction
        LocalDate dateOfBirth = Input.inputDOB(lang, false); // User enters date of birth

        // Print instructions for contact info input
        Printer.print("\nEnter Email (Enter 'a' to auto-generate email or enter a valid email ) : ", lang); // Example
        // instruction
        String emailAddress = Input.inputEmailAddress(lang, false, name, "staff"); // User enters contact info

        Printer.print("\nEnter Phone Number: ", lang);
        String contactNumber = Input.inputContactNumber(lang, false, "staff"); // User enters contact info

        String staffId;

        switch (roleChoice) {
            case "1": // Pharmacist
                Printer.print("\nEnter your Lisence Number : ", lang); // Example instruction

                String licenseNumber = Input.inputLicenseNumber(lang, false);
                staffId = IdGenerator.generateId("PHM");

                Pharmacist pharmacist = new Pharmacist(staffId, name, gender, username, password,
                        dateOfBirth, contactNumber, emailAddress, licenseNumber);
                pharmacistController.add(pharmacist, false);
                Printer.print("Pharmacist '" + name + "' has been added.", lang);
                break;

            case "2": // Doctor
                staffId = IdGenerator.generateId("DOC");
                // System.out.println(staffId);

                // Prompt for patient IDs
                Printer.print("\nEnter patient ids (separated by commas): ", lang);
                String patientIdsInput = scanner.nextLine();

                List<String> patientIds = new ArrayList<>();

                // Keep asking for valid IDs until either all are valid or user skips
                while (true) {
                    if (!patientIdsInput.trim().isEmpty()) {
                        // Split by commas and create a list of patient IDs
                        patientIds = Arrays.asList(patientIdsInput.split("\\s*,\\s*"));

                        boolean allValid = true; // Flag to track if all patient IDs are valid

                        // Validate each patient ID
                        for (String patientId : patientIds) {
                            if (PatientDB.getUserById(patientId) == null) {
                                // If the patient ID doesn't exist, flag it and break the loop
                                Printer.print("Patient " + patientId
                                        + " does not exist. Please enter valid patient IDs or press Enter to skip.",
                                        lang);
                                allValid = false;
                                break; // Exit the loop to re-enter patient IDs
                            }
                        }

                        // If all patient IDs are valid, break the loop and continue
                        if (allValid) {
                            break;
                        }
                    } else {
                        // If no IDs were entered, skip the update
                        Printer.print("No patient IDs entered. Skipping update.", lang);
                        patientIds = new ArrayList<>();
                        ; // Ensure patientIds is an empty list
                        break;
                    }

                    // Prompt for re-entering the patient IDs if any are invalid
                    Printer.print("\nPlease enter valid patient IDs (separated by commas): ", lang);
                    patientIdsInput = scanner.nextLine();
                }

                // Print the patientIds to verify the result
                String res = "\nPatient IDs: " + patientIds;
                Printer.print(res, lang);

                // Proceed with creating the Doctor object with valid patient IDs
                Doctor doctor = new Doctor(staffId, name, gender, username, password,
                        dateOfBirth, contactNumber, emailAddress, patientIds);
                doctorController.add(doctor, false);

                Printer.print("Doctor '" + name + "' has been added.", lang);

                break;
            case "3": // Admin
                staffId = IdGenerator.generateId("ADM");

                Admin admin = new Admin(staffId, name, gender, username, password,
                        dateOfBirth, contactNumber, emailAddress);
                adminController.add(admin, false);
                Printer.print("Admin '" + name + "' has been added.", lang);
                break;
            default:
                Printer.print("Invalid role selected.", lang);
                break;
        }
    }

    /**
     * Handles the processing of replenishment requests.
     *
     * <p>
     * This method retrieves all active replenishment requests and allows the user
     * to:
     * </p>
     * <ul>
     * <li>Approve a request</li>
     * <li>Deny a request</li>
     * <li>Skip a request without action</li>
     * <li>Quit the process at any time</li>
     * </ul>
     * The status of each request is updated based on the user's input, and the
     * updated status is displayed.
     * If no active requests are available, a message is displayed, and the method
     * returns immediately.
     *
     * @return {@code true} when all requests are processed or if the user exits the
     *         process.
     */
    public boolean handleReplenishmentRequest() {
        Printer.print(
                "These are all the replenishment requests, for each request, select the options availabe or press enter to skip: \n",
                lang);

        HashMap<Integer, ReplenishRequest> requests = replenishRequestController.viewAllActiveRequests();

        // display all replenish requests
        if (requests.isEmpty()) {
            Printer.print("No replenish requests. ", lang);
            return true;
        } else {
            // Printer.printReplenishRequest(requests);

            while (true) {
                // Iterate over the entries in the HashMap
                for (Map.Entry<Integer, ReplenishRequest> entry : requests.entrySet()) {

                    Printer.print("\n------ Processing next request .. \n", lang);
                    Integer requestId = entry.getKey();
                    ReplenishRequest request = entry.getValue();

                    // Create a temporary Hashtable for each entry
                    HashMap<Integer, ReplenishRequest> tempRequests = new HashMap<>();
                    tempRequests.put(requestId, request); // Add the current entry to the Hashtable

                    Printer.printTable(tempRequests, "ReplenishmentRequest");

                    String menuHeader = "Request Approval Menu";
                    List<String> menuOptions = Arrays.asList(
                            "Approve",
                            "Deny");
                    Printer.printMenu(menuHeader, menuOptions);
                    Printer.print("\nPress Enter to Skip (No Action) or 'q' to Quit the Update Process", lang);

                    String userChoice;

                    // Keep prompting the user until a valid input is provided
                    do {
                        // Get the user's choice for this request
                        userChoice = scanner.nextLine().trim().toLowerCase();

                        // Check if the input is valid
                        if (userChoice.equals("1") || userChoice.equals("2") || userChoice.equals("")
                                || userChoice.equals("q")) {
                            break; // Exit the loop if the input is valid
                        } else {
                            Printer.print(
                                    "Invalid option. Please enter a valid option: 1, 2, or press Enter to skip or 'q' to quit.",
                                    lang);
                        }
                    } while (true);

                    if (userChoice.equals("q")) {
                        Printer.print("Exiting update process...", lang);
                        return true; // Exit the entire process
                    } else if (userChoice.equals("1")) {
                        // Approve the request
                        replenishRequestController.updateReplenishmentRequest(requestId, "APPROVED");
                        Printer.print("Request " + requestId + " has been approved.", lang);
                    } else if (userChoice.equals("2")) {
                        // Deny the request
                        replenishRequestController.updateReplenishmentRequest(requestId, "DENIED");
                        Printer.print("Request " + requestId + " has been denied.", lang);
                    } else if (!userChoice.equals("")) {
                        Printer.print("Invalid option. Skipping request " + requestId + ".", lang);
                    } else {
                        Printer.print("Skipping request " + requestId + " (No Action).", lang);
                    }
                    Printer.print("This is the new updated status for this request: ", lang);
                    Printer.printTable(tempRequests, "ReplenishmentRequest");
                }
                Printer.print("All requests processed. Returning to main menu...", lang);
                return true; // Exit after processing all requests
            }
        }
    }

    /**
     * Adds a new medication to the system.
     * <p>
     * This method guides the user through the process of adding a new medication by
     * prompting for the following details:
     * </p>
     * <ul>
     * <li>Medication Name</li>
     * <li>Initial Stock</li>
     * <li>Low Stock Level Alert</li>
     * <li>Price of the medication</li>
     * </ul>
     * <p>
     * After collecting the necessary information, a new {@code Medication} object
     * is created and added to the system through the {@code medicationController}.
     * </p>
     * <p>
     * The method performs the following steps:
     * </p>
     * <ul>
     * <li>Prompts the user for input and validates the data using helper
     * methods</li>
     * <li>Creates a new {@code Medication} object using the input values</li>
     * <li>Adds the new medication to the medication controller</li>
     * </ul>
     * <p>
     * The medication is added with the specified initial stock, low stock alert
     * threshold, and price.
     * </p>
     */
    public void addNewMedication() {
        Printer.print("Enter Medication Name: ", lang);
        String medName = Input.inputMedicationName(lang);

        Printer.print("Enter Initial Stock: ", lang);
        int initialStock = Input.inputInitialStock(lang);

        Printer.print("Enter Low Stock Level Alert: ", lang);
        int lowStockAlert = Input.inputLowStockAlert(lang);

        double price = Input.inputPrice(lang);

        Medication newMedication = new Medication(medName, initialStock, lowStockAlert, price);
        medicationController.addMedication(newMedication, true);
        Printer.print("Medication successfully added!", lang);
    }

    /**
     * Allows the administrator to update their personal information.
     * <p>
     * This method prompts the user (admin) for various pieces of information, such
     * as name, username, password,
     * gender, contact number, and email address, and updates the fields if the user
     * provides new values.
     * The user can choose to skip any field by pressing Enter without entering new
     * data.
     * </p>
     * <p>
     * The method performs the following steps:
     * </p>
     * <ul>
     * <li>Prompts the administrator for new values for their name, username,
     * password, gender, contact number, and email address.</li>
     * <li>If the user enters a new value for any field, it updates the
     * corresponding property of the admin object.</li>
     * <li>If any updates are made, the updated admin information is sent to the
     * {@code adminController} for processing.</li>
     * <li>If no updates are made (all fields are skipped), a message is displayed
     * indicating that no changes were made.</li>
     * </ul>
     * <p>
     * The method ensures that all updates are optional, and no information is
     * changed unless the user explicitly enters new data.
     * </p>
     * 
     * @param admin The {@link Admin} object whose personal information is to be
     *              updated.
     */
    public void updatePersonalInfo(Admin admin) {
        boolean isUpdated = false; // Flag to track updates

        // Update Name
        Printer.print("Enter new name (or press Enter to skip): ", lang);
        String name = Input.inputName(lang, true);
        if (!name.isEmpty()) {
            admin.setName(name);
            isUpdated = true;
        }

        // Update Username
        Printer.print(
                "Enter new username (Enter 'a' to auto-generate or enter a valid username or Press Enter to skip): ",
                lang);
        String username = Input.inputUsername(lang, name, "staff", true);
        if (username != null) {
            admin.setUsername(username);
            isUpdated = true;
        }

        // Update Password
        Printer.print("Enter new password (or press Enter to skip): ", lang);
        String password = Input.inputPassword(lang, true);
        if (!password.isEmpty()) {
            admin.setPassword(Util.hashPassword(password));
            isUpdated = true;
        }

        // Update Gender
        Printer.print("Enter new gender (press Enter to skip): ", lang);
        String gender = Input.inputGender(lang, true);
        if (!gender.isEmpty()) {
            admin.setGender(gender);
            isUpdated = true;
        }

        // Update Contact Number
        Printer.print("Enter new contact number (or press Enter to skip): ", lang);
        String contactNumber = Input.inputContactNumber(lang, true, "staff");
        if (!contactNumber.isEmpty()) {
            admin.setContactNumber(contactNumber);
            isUpdated = true;
        }

        // Update Email Address
        Printer.print("Enter new email address (or press Enter to skip): ", lang);
        String emailAddress = Input.inputEmailAddress(lang, true, name, "staff");
        if (!emailAddress.isEmpty()) {
            admin.setEmailAddress(emailAddress);
            isUpdated = true;
        }

        // Check if any updates were made
        if (isUpdated) {
            String response = adminController.updatePersonalInfo(admin.getId(), admin, null);
            Printer.print(response, lang);
        } else {
            Printer.print("No information was updated.", lang); // Print if no updates were made
        }
    }

}
