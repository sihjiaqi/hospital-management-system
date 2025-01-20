package boundary.Applications;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.Set;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import model.Appointment.Appointment;
import model.MedicalRecord.MedicalRecord;
import model.Users.Doctor;
import model.Users.Patient;
import repository.Appointment.AppointmentDB;
import repository.Medication.MedicationDB;
import repository.Users.PatientDB;
import utils.Utility.Config;
import utils.Utility.FileScanner;
import utils.Utility.CSVReader;
import utils.Utility.Input;
import utils.Utility.Printer;
import utils.Utility.Util;
import utils.Utility.Validation;
import DTO.PatientMedicalRecordDTO;
import controller.Users.DoctorController;
import controller.Users.PatientController;
import controller.Appointment.AppointmentController;
import controller.MedicalRecord.MedicalRecordController;

/**
 * This class represents the Doctor Application which allows doctors to manage
 * and update medical records,
 * as well as their own personal information.
 * <p>
 * The `DoctorApp` class provides functionality to interact with the system to
 * update medical records, view patient details,
 * and manage personal information of doctors. It contains methods for uploading
 * medical records from a file,
 * updating diagnoses, prescriptions, treatment plans, and personal details of
 * doctors.
 * The class also includes CLI-based interactions for these updates, with
 * prompts for users and validation of inputs.
 * </p>
 *
 * <p>
 * Key functionalities include:
 * </p>
 * <ul>
 * <li>Upload medical records from a file</li>
 * <li>Update patient diagnoses, prescriptions, and treatment plans</li>
 * <li>View and update personal information of doctors</li>
 * </ul>
 * 
 *
 * <p>
 * This class heavily relies on the use of utility methods (like printing
 * messages, handling inputs, etc.)
 * and controllers (like medical record and doctor controllers) to interact with
 * the backend system.
 * </p>
 *
 * @see Util
 * @see Printer
 * @see Input
 * @see MedicalRecordController
 * @see DoctorController
 * @see MedicalRecord
 * @see Patient
 */
public class DoctorApp {
    private static DoctorApp instance;
    private static Scanner sc = new Scanner(System.in);
    private static String lang = "en";
    private static Map<LocalDate, List<LocalTime>> monthlyAvailability;

    // Controllers instantiation
    private PatientController patientController = new PatientController();
    private AppointmentController appointmentController = new AppointmentController();
    private MedicalRecordController medicalRecordController = new MedicalRecordController();
    private DoctorController doctorController = new DoctorController();

    /**
     * Constructor for the DoctorApp class.
     * <p>
     * This constructor initializes the DoctorApp instance. Any required setup or
     * initialization
     * tasks related to the app can be handled here. Currently, no parameters are
     * required.
     * </p>
     */
    private DoctorApp() {
        // Initialization or any required setup can be done here
    }

    /**
     * Returns the single instance of the DoctorApp class.
     * <p>
     * This method implements the Singleton pattern, ensuring that only one instance
     * of
     * the DoctorApp class is created throughout the application's lifecycle. If the
     * instance
     * has not been created yet, it will be initialized lazily when this method is
     * called.
     * </p>
     *
     * @return the single instance of the DoctorApp class.
     */
    public static DoctorApp getInstance() {
        if (instance == null) {
            // Create the instance only when it's needed (Lazy initialization)
            instance = new DoctorApp();
        }
        return instance;
    }

    /**
     * Initializes the DoctorApp with the given Doctor instance.
     * <p>
     * This method starts the DoctorApp by calling the
     * {@link #startDoctorApp(Doctor)} method.
     * It expects a valid Doctor object to initialize the app with the appropriate
     * context or setup.
     * </p>
     *
     * @param doctor the Doctor instance to initialize the app with.
     */
    public void initializeApp(Doctor doctor) {
        startDoctorApp(doctor);
    }

    /**
     * Starts the DoctorApp and presents the doctor with various menu options to
     * manage their tasks.
     * <p>
     * This method handles the initialization of the app's user interface,
     * presenting the doctor
     * with a language selection menu. After selecting a language, the method enters
     * a loop where the
     * doctor can choose from a range of menu options, such as viewing or updating
     * patient medical records,
     * managing appointments, setting availability, and updating personal
     * information.
     * The method continues to loop until the doctor chooses to log out.
     * </p>
     *
     * @param doctor the Doctor instance whose information and tasks are managed in
     *               the app.
     */
    private void startDoctorApp(Doctor doctor) {
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
        }

        do {
            menuHeader = "Doctor Menu";
            menuOptions = Arrays.asList(
                    "View Patient Medical Records",
                    "Update Patient Medical Records",
                    "View Personal Schedule",
                    "Set Availability for Appointments",
                    "Accept or Decline Appointment Requests",
                    "View Upcoming Appointments",
                    "Record Appointment Outcome",
                    "Update Personal Info",
                    "Logout");
            Printer.printMenu(menuHeader, menuOptions);

            choice = sc.nextLine();
            String option;

            switch (choice) {
                case "1": // View patient medical records
                    menuHeader = "View Patient Medical Record";
                    menuOptions = Arrays.asList(
                            "Show all medical records",
                            "Search by patient",
                            "Back to Main Menu");
                    Printer.printMenu(menuHeader, menuOptions);
                    option = sc.nextLine().trim(); // Read the input as a string
                    Util.clearConsole();

                    switch (option) {
                        case "1": // Show all medical records
                            Printer.printHeader("----------------- Patient Medical Records -----------------");
                            List<PatientMedicalRecordDTO> medicalRecords = medicalRecordController.viewMedicalRecords(doctor);
                            //System.out.println(medicalRecords);
                            if (medicalRecords == null) {
                                Printer.print("\nUnable to retrieve patient medical record.", lang);
                                break;
                            }else{

                                Printer.printMedicalRecords(medicalRecords);
                                break;
                            }


                         
                        case "2": // Search medical record by patient ID
                            while (true) {
                                String patientId;
                                List<Patient> patients = patientController.viewPatientsByDoctor(doctor);

                                if (patients == null) {
                                    Printer.print("\nUnable to retrieve patient medical record.", lang);
                                    break;
                                }

                                Printer.printUsers(patients);

                                do {
                                    Printer.print("Enter the patient's ID: ", lang);
                                    patientId = sc.nextLine().trim().toLowerCase();

                                    if (patientId.isEmpty()) {
                                        Printer.print("No patient ID entered. Please try again.", lang);
                                    }
                                } while (patientId.isEmpty());
                                Printer.printHeader("----------------- Patient Medical Record -----------------");
                                PatientMedicalRecordDTO medicalRecord = medicalRecordController
                                        .viewMedicalRecordByPatientId(patientId);
                                if (medicalRecord == null) {
                                    Printer.print("\nNo medical record found.", lang);
                                    break;
                                }else{
                                    Printer.printMedicalRecords(medicalRecord);
                                    break;
                                }
                               
                            }
                            break;
                        case "3":
                            break;
                        default:
                            Printer.print("Invalid option. Please select 1, 2 or 3.", lang);
                    }
                    break;

                case "2": // Update patient medical record
                    // Display patients under the doctor's care
                    List<Patient> patients = patientController.viewPatientsByDoctor(doctor);
                    if (patients == null) {
                        Printer.print("\nNo patient found under the doctor's care.", lang);
                        break;
                    }
                    Printer.printUsers(patients);

                    menuHeader = "Update Patient Medical Record";
                    menuOptions = Arrays.asList(
                            "Update medical records via interface",
                            "Update medical records from a file",
                            "Back to Main Menu");
                    Printer.printMenu(menuHeader, menuOptions);

                    option = sc.nextLine().trim(); // Read the input as a string

                    switch (option) {
                        case "1": // Upload via CLI
                            uploadMedicalRecordsCLI();
                            break;
                        case "2": // Upload via File
                            uploadMedicalRecordsFromFile();
                            break;
                        case "3":
                            Printer.print("Exiting...", lang);
                            break;
                        default:
                            Printer.print("Invalid option. Please try again.", lang);
                    }
                    break;

                case "3": // View personal schedule in calendar format
                    Printer.printHeader("----------------- Doctor Personal Schedule -----------------");
                    // Retreive upcoming appointments
                    List<Appointment> appointments = appointmentController.viewUpcomingAppointments(doctor.getId());
                    // Retrieve availability
                    monthlyAvailability = appointmentController.viewMonthlyAvailability(doctor);
                    Printer.printSchedule(monthlyAvailability, appointments);
                    break;

                case "4": // Set availability for the month
                    Printer.printHeader(
                            "----------------- Set Monthly Availability for Appointments -----------------");

                    String startDateStr;
                    boolean exitCase4 = false; // Flag to break out of the entire case
                    do {
                        Printer.print("Enter the start date (YYYY-MM-DD) or enter 'q' to quit: ", lang);
                        startDateStr = sc.nextLine();
                        // Check if the user pressed 'q' to quit
                        if (startDateStr.equalsIgnoreCase("q")) {
                            Printer.print("Cancelled. No further action taken.", lang);
                            exitCase4 = true; // Set the flag to exit the case
                            break;
                        }

                        if (!Validation.validateDate(startDateStr)) {
                            Printer.print("Invalid input. Date must be in the format YYYY-MM-DD.", lang);
                        }
                    } while (!Validation.validateDate(startDateStr));

                    if (exitCase4)
                        break; // Exit case if 'q' was pressed

                    LocalDate startDate;
                    try {
                        startDate = LocalDate.parse(startDateStr);
                    } catch (DateTimeParseException e) {
                        Printer.print("Error parsing date. Please ensure it's in the format YYYY-MM-DD.", lang);
                        break;
                    }

                    // Get the start and end times
                    String startTimeStr;
                    do {
                        Printer.print("Enter the start time (HH:MM) or enter 'q' to quit: ", lang);
                        startTimeStr = sc.nextLine();
                        // Check if the user pressed 'q' to quit
                        if (startTimeStr.equalsIgnoreCase("q")) {
                            Printer.print("Cancelled. No further action taken.", lang);
                            exitCase4 = true;
                            break;
                        }
                        if (!Validation.validateTime(startTimeStr)) {
                            Printer.print("Invalid input. Time must be in the format HH:MM.", lang);
                        }
                    } while (!Validation.validateTime(startTimeStr));

                    if (exitCase4)
                        break;

                    LocalTime startTime;
                    try {
                        startTime = LocalTime.parse(startTimeStr);
                    } catch (DateTimeParseException e) {
                        Printer.print("Error parsing time. Please ensure it's in the format HH:MM.", lang);
                        break;
                    }

                    String endTimeStr;
                    do {
                        Printer.print("Enter the end time (HH:MM) or enter 'q' to quit: ", lang);
                        endTimeStr = sc.nextLine();
                        // Check if the user pressed 'q' to quit
                        if (endTimeStr.equalsIgnoreCase("q")) {
                            Printer.print("Cancelled. No further action taken.", lang);
                            exitCase4 = true;
                            break;
                        }
                        if (!Validation.validateTime(endTimeStr)) {
                            Printer.print("Invalid input. Time must be in the format HH:MM.", lang);
                        }
                    } while (!Validation.validateTime(endTimeStr));

                    if (exitCase4)
                        break;

                    LocalTime endTime;
                    try {
                        endTime = LocalTime.parse(endTimeStr);
                    } catch (DateTimeParseException e) {
                        Printer.print("Error parsing time. Please ensure it's in the format HH:MM.", lang);
                        break;
                    }

                    // Get the interval in minutes
                    Printer.print(
                            "Enter the interval in minutes (e.g., 30 for 30-minute intervals) or enter 'q' to quit: ",
                            lang);
                    String intervalMinutesStr = sc.nextLine();
                    // Check if the user pressed 'q' to quit
                    if (intervalMinutesStr.equalsIgnoreCase("q")) {
                        Printer.print("Cancelled. No further action taken.", lang);
                        break;
                    } else {
                        try {
                            int intervalMinutes = Integer.parseInt(intervalMinutesStr);
                            // Set availability for the entire month
                            appointmentController.setMonthlyAvailability(doctor, startDate, startTime, endTime,
                                    intervalMinutes);
                            Printer.print("Monthly availability set successfully!", lang);
                        } catch (NumberFormatException e) {
                            Printer.print("Invalid input. Please enter a valid number for the interval.", lang);
                        }
                    }
                    break;

                case "5": // Accept or decline appointment request
                    // Display all upcoming appointments
                    List<Appointment> upcomingAppointments = appointmentController
                            .viewAllUpcomingAppointments(doctor.getId());
                    if (upcomingAppointments.isEmpty()) {
                        Printer.print("\nNo upcoming appointment found.", lang);
                        break;
                    }
                    Printer.printHeader("----------------- Upcoming Appointments -----------------");
                    Printer.printAppointments(upcomingAppointments);
                    // Extract appointment IDs from the list
                    Set<Integer> appointmentIds = new HashSet<>();
                    for (Appointment appointment : upcomingAppointments) {
                        appointmentIds.add(appointment.getAppointmentId());
                    }
                    int apptChoice;
                    do {
                        menuHeader = "Appointment Requests Menu";
                        menuOptions = Arrays.asList(
                                "Accept An Appointment",
                                "Accept All Appointments",
                                "Decline An Appointment",
                                "Decline All Appointment",
                                "Back to Main Menu");
                        Printer.printMenu(menuHeader, menuOptions);

                        apptChoice = sc.nextInt();

                        switch (apptChoice) {
                            case 1:
                                Printer.print("Enter appointment ID to accept or enter 'q' to quit: ", lang);
                                String acceptIDString = sc.next();
                                // Check if the user pressed 'q' to quit
                                if (acceptIDString.equalsIgnoreCase("q")) {
                                    Printer.print("Cancelled. No further action taken.", lang);
                                    break;
                                } else {
                                    int acceptID = Integer.parseInt(acceptIDString);
                                    if (!appointmentIds.contains(acceptID)) {
                                        Printer.print("Error: Invalid Appointment ID. Please try again.", lang);
                                        break; // Exit or loop again based on your application's requirements
                                    }
                                    appointmentController.updateAppointmentStatus(acceptID, "CONFIRMED");
                                    Printer.print("Appointment " + acceptID + " confirmed.", lang);
                                }
                                break;
                            case 2:
                                Printer.print("Accepting all appointment requests...", lang);
                                appointmentController.acceptAllUpcomingAppointments(doctor.getId());
                                Printer.print("All appointments confirmed.", lang);
                                break;
                            case 3:
                                Printer.print("Enter the appointment ID to decline or enter 'q' to quit: ", lang);
                                String declineIDString = sc.next();
                                // Check if the user pressed 'q' to quit
                                if (declineIDString.equalsIgnoreCase("q")) {
                                    Printer.print("Cancelled. No further action taken.", lang);
                                    break;
                                } else {
                                    int declineID = Integer.parseInt(declineIDString);
                                    if (!appointmentIds.contains(declineID)) {
                                        Printer.print("Error: Invalid Appointment ID. Please try again.", lang);
                                        break; // Exit or loop again based on your application's requirements
                                    }
                                    appointmentController.updateAppointmentStatus(declineID, "DECLINED");
                                    Printer.print("Appointment " + declineID + " declined.", lang);
                                }
                                break;
                            case 4:
                                Printer.print("Declining all appointment requests...", lang);
                                appointmentController.declineAllUpcomingAppointments(doctor.getId());
                                Printer.print("All appointments declined.", lang);
                                break;
                            case 5:
                                Printer.print("Returning to main menu...", lang);
                                break;
                            default:
                                Printer.print("Invalid choice. Please try again.", lang);
                                break;
                        }
                    } while (apptChoice != 5);
                    break;

                case "6": // View upcoming appointments
                    Printer.printHeader("----------------- Upcoming Appointments -----------------");
                    List<Appointment> upcomingAppointments2 = appointmentController
                            .viewUpcomingAppointments(doctor.getId());
                    if (upcomingAppointments2.isEmpty()) {
                        Printer.print("\nNo upcoming appointment found.", lang);
                        break;
                    }
                    Printer.printAppointments(upcomingAppointments2);
                    break;

                case "7": // Record appointment outcome
                    Printer.printHeader("----------------- Past Appointments -----------------");
                    List<Appointment> pastAppointments = appointmentController.viewPastAppointmentsByDoctorId(doctor.getId());
                    if (pastAppointments.isEmpty()) {
                        Printer.print("\nNo past appointment found.", lang);
                        break;
                    }
                    Printer.printAppointments(pastAppointments);
                    // Extract appointment IDs from the list
                    Set<Integer> appointmentIds2 = new HashSet<>();
                    for (Appointment appointment : pastAppointments) {
                        appointmentIds2.add(appointment.getAppointmentId());
                    }

                    // Prompt for appointment ID
                    Printer.print("Enter the Appointment ID to record the outcome or enter 'q' to quit: ", lang);
                    String apptIdString = sc.nextLine();
                    // Check if the user pressed 'q' to quit
                    if (apptIdString.equalsIgnoreCase("q")) {
                        Printer.print("Cancelled. No further action taken.", lang);
                        break;
                    } else {
                        int appointmentIdToRecord = Integer.parseInt(apptIdString); // Convert input to an integer
                        if (!appointmentIds2.contains(appointmentIdToRecord)) {
                            Printer.print("Error: Invalid Appointment ID. Please try again.", lang);
                            break; // Exit or loop again based on your application's requirements
                        }
                    
                        // Retrieve the appointment using the appointment ID
                        Appointment appointmentToRecord = AppointmentDB.getAppointmentById(appointmentIdToRecord);

                        if (appointmentToRecord == null) {
                            Printer.print("No appointment found with ID " + appointmentIdToRecord, lang);
                        } else {
                            // Ask for the outcome details
                            Printer.print("Enter the service type or enter 'q' to quit: ", lang);
                            String serviceType = sc.nextLine();
                            // Check if the user pressed 'q' to quit
                            if (serviceType.equalsIgnoreCase("q")) {
                                Printer.print("Cancelled. No further action taken.", lang);
                                break;
                            }
                            // Prompt for multiple medications with validation
                            List<String> medications = new ArrayList<>();
                            Printer.print("Enter medications (enter 'done' to finish or enter 'q' to quit):", lang);
                            while (true) {
                                String medication = sc.nextLine().trim();
                                // Check if the user pressed 'q' to quit
                                if (medication.equalsIgnoreCase("q")) {
                                    Printer.print("Cancelled. No further action taken.", lang);
                                    break;
                                }
                                if (medication.equalsIgnoreCase("done")) {
                                    break;
                                } else if (!medication.isEmpty()) {
                                    if (MedicationDB.findMedicationByName(medication) != null) {
                                        medications.add(medication);
                                    } else {
                                        Printer.print("Medication '" + medication
                                                + "' does not exist. Please enter a valid medication.", lang);
                                    }
                                }
                            }
                            // Prompt for consultation notes
                            Printer.print("Enter the consultation notes or enter 'q' to quit: ", lang);
                            String consultationNotes = sc.nextLine();
                            // Check if the user pressed 'q' to quit
                            if (consultationNotes.equalsIgnoreCase("q")) {
                                Printer.print("Cancelled. No further action taken.", lang);
                                break;
                            }
                            // Record the appointment outcome
                            appointmentController.recordAppointmentOutcome(appointmentIdToRecord, serviceType,
                                    medications,
                                    consultationNotes, "PENDING");
                            Printer.print("Outcome recorded for Appointment ID " + appointmentIdToRecord, lang);
                        }
                    }

                    break;
                case "8": // (5) Update Personal Info
                    updatePersonalInfo(doctor);
                    break;
                case "9": // (6) Logout
                    Printer.print("\nLogging out...", lang);
                    return;
                default:
                    Printer.print("Invalid choice, please choose again", lang);
                    break;
            }
        } while (choice != "9");

    }

    /**
     * Allows the doctor to update a patient's medical records through the command
     * line interface (CLI).
     * <p>
     * This method prompts the doctor to enter a patient's ID and provides the
     * option to update various sections
     * of the medical record, including diagnosis, prescription, and treatment plan.
     * The doctor can also view the
     * current medical record or return to the main doctor menu. The method
     * continues to loop until the doctor chooses
     * to return to the main menu.
     * </p>
     *
     */
    public void uploadMedicalRecordsCLI() {
        // Prompt for patient ID to update records
        Printer.print("Enter the Patient ID to update records or enter 'q' to quit: ", lang);
        String patientIdToUpdate = sc.next();
        // Check if the user pressed 'q' to quit
        if (patientIdToUpdate.equalsIgnoreCase("q")) {
            Printer.print("Cancelled. No further action taken.", lang);
            return;
        }
        Patient patientToUpdate = PatientDB.getUserById(patientIdToUpdate);
        if (patientToUpdate == null) {
            Printer.print("Patient with ID " + patientIdToUpdate + " does not exist.", lang);
            return;
        }
        PatientMedicalRecordDTO medicalRecord = medicalRecordController.viewMedicalRecordByPatientId(patientIdToUpdate);
        if (medicalRecord == null) {
            Printer.print("No medical records found for Patient ID " + patientIdToUpdate, lang);
            return;
        }
        int updateChoice;
        do {
            String menuHeader = "Update Medical Record Menu";
            List<String> menuOptions = Arrays.asList(
                    "Diagnosis",
                    "Prescription",
                    "Treatment Plan",
                    "View Current Medical Record",
                    "Back to Doctor Menu");
            Printer.printMenu(menuHeader, menuOptions);

            updateChoice = sc.nextInt();
            sc.nextLine();
            Util.clearConsole();

            switch (updateChoice) {
                case 1:
                    updateDiagnosis(patientIdToUpdate);
                    break;
                case 2:
                    updatePrescription(patientIdToUpdate);
                    break;
                case 3:
                    updateTreatmentPlan(patientIdToUpdate);
                    break;
                case 4:
                    Printer.print("Current Medical Record:", lang);
                    Printer.printMedicalRecords(medicalRecord);
                    break;
                case 5:
                    Printer.print("Returning to Doctor Menu...", lang);
                    break;
                default:
                    Printer.print("Invalid choice. Please try again.", lang);
                    break;
            }

        } while (updateChoice != 5);
    }

    /**
     * Allows the doctor to update a patient's diagnosis through the command line
     * interface (CLI).
     * <p>
     * This method presents the doctor with a menu of options to either add a new
     * diagnosis, delete an existing diagnosis,
     * or view the current diagnoses associated with a patient. The doctor can also
     * choose to cancel the operation. If the
     * user chooses to add a diagnosis, they can enter multiple diagnoses separated
     * by a semicolon. The method also
     * ensures that any updates are reflected in the medical record.
     * </p>
     *
     * @param patientId The ID of the patient whose diagnosis is being updated.
     * @see MedicalRecordController#viewMedicalRecord(String)
     * @see MedicalRecordController#updateMedicalRecord(String, List, List, List)
     * @see Printer#printMenu(String, List)
     * @see Util#clearConsole()
     * @see #deleteItems(String, MedicalRecord)
     */
    private void updateDiagnosis(String patientId) {
        MedicalRecord medicalRecord = medicalRecordController.viewMedicalRecord(patientId);
        String menuHeader = "Update Diagnosis Menu";
        List<String> menuOptions = Arrays.asList(
                "Add Diagnosis",
                "Delete Diagnosis");
        Printer.printMenu(menuHeader, menuOptions);

        int option = sc.nextInt();
        sc.nextLine();
        Util.clearConsole();

        switch (option) {
            case 1:
                Printer.print("Enter the diagnosis to add or enter 'q' to quit: ", lang);
                String diagnosisToAdd = sc.nextLine();
                // Check if the user pressed 'q' to quit
                if (diagnosisToAdd.equalsIgnoreCase("q")) {
                    Printer.print("Cancelled. No further action taken.", lang);
                    break;
                }
                List<String> diagnoses = Arrays.asList(diagnosisToAdd.split(";"));
                medicalRecordController.updateMedicalRecord(patientId, diagnoses, null, null);
                Printer.print("Diagnosis added.", lang);
                break;
            case 2:
                deleteItems("diagnoses", medicalRecord);
                break;
            case 3:
                Printer.print("Current diagnoses: " + medicalRecord.getDiagnoses(), lang);
                break;
            default:
                Printer.print("Invalid option!", lang);
        }
    }

    /**
     * Allows the doctor to update a patient's prescription through the command line
     * interface (CLI).
     * <p>
     * This method provides the doctor with a menu of options to add a new
     * prescription, delete an existing prescription,
     * or view the current prescriptions associated with a patient. The doctor can
     * also cancel the operation. If the user
     * chooses to add a prescription, they can input multiple prescriptions
     * separated by a semicolon. The method updates the
     * medical record accordingly.
     * </p>
     *
     * @param patientId The ID of the patient whose prescription is being updated.
     * @see MedicalRecordController#viewMedicalRecord(String)
     * @see MedicalRecordController#updateMedicalRecord(String, List, List, List)
     * @see Printer#printMenu(String, List)
     * @see Util#clearConsole()
     * @see #deleteItems(String, MedicalRecord)
     */
    private void updatePrescription(String patientId) {
        MedicalRecord medicalRecord = medicalRecordController.viewMedicalRecord(patientId);
        String menuHeader = "Update Presription Menu";
        List<String> menuOptions = Arrays.asList(
                "Add Prescription",
                "Delete Prescription");
        Printer.printMenu(menuHeader, menuOptions);

        int option = sc.nextInt();
        sc.nextLine();
        Util.clearConsole();

        switch (option) {
            case 1:
                Printer.print("Enter the prescription to add or enter 'q' to quit: ", lang);
                String presrciptionToAdd = sc.nextLine();
                // Check if the user pressed 'q' to quit
                if (presrciptionToAdd.equalsIgnoreCase("q")) {
                    Printer.print("Cancelled. No further action taken.", lang);
                    break;
                }
                List<String> prescriptions = Arrays.asList(presrciptionToAdd.split(";"));
                medicalRecordController.updateMedicalRecord(patientId, null, prescriptions, null);

                Printer.print("Prescription added.", lang);
                break;
            case 2:
                // Deleting prescriptions
                deleteItems("prescriptions", medicalRecord);
                break;
            case 3:
                Printer.print("Current prescriptions: " + medicalRecord.getPrescriptions(), lang);
                break;
            default:
                Printer.print("Invalid option!", lang);
        }
    }

    /**
     * Allows the doctor to update a patient's treatment plan through the command
     * line interface (CLI).
     * <p>
     * This method provides the doctor with a menu of options to add a new treatment
     * plan, delete an existing treatment plan,
     * or view the current treatment plans associated with a patient. The doctor can
     * also cancel the operation. If the user
     * chooses to add a treatment plan, they can input multiple treatment plans
     * separated by a semicolon. The method updates the
     * medical record accordingly.
     * </p>
     *
     * @param patientId The ID of the patient whose treatment plan is being updated.
     * @see MedicalRecordController#viewMedicalRecord(String)
     * @see MedicalRecordController#updateMedicalRecord(String, List, List, List)
     * @see Printer#printMenu(String, List)
     * @see Util#clearConsole()
     * @see #deleteItems(String, MedicalRecord)
     */
    private void updateTreatmentPlan(String patientId) {
        MedicalRecord medicalRecord = medicalRecordController.viewMedicalRecord(patientId);
        String menuHeader = "Update Treatment Plan Menu";
        List<String> menuOptions = Arrays.asList(
                "Add Treatment Plan",
                "Delete Treatment Plan");
        Printer.printMenu(menuHeader, menuOptions);

        int option = sc.nextInt();
        sc.nextLine();
        Util.clearConsole();

        switch (option) {
            case 1:
                Printer.print("Enter the treatment plan to add or enter 'q' to quit: ", lang);
                String treatmentPlanToAdd = sc.nextLine();
                // Check if the user pressed 'q' to quit
                if (treatmentPlanToAdd.equalsIgnoreCase("q")) {
                    Printer.print("Cancelled. No further action taken.", lang);
                    break;
                }
                List<String> treatments = Arrays.asList(treatmentPlanToAdd.split(";"));
                medicalRecordController.updateMedicalRecord(patientId, null, null, treatments);
                Printer.print("Treatment plan added.", lang);
                break;
            case 2:
                // Deleting treatment plans
                deleteItems("treatment plans", medicalRecord);
                break;
            case 3:
                Printer.print("Current treatment plans: " + medicalRecord.getTreatmentPlans(), lang);
                break;
            default:
                Printer.print("Invalid option!", lang);
        }
    }

    /**
     * Uploads medical record data from CSV files and updates patient medical
     * records in the system.
     * <p>
     * This method prompts the user to upload medical record CSV files into a
     * specified folder. After the user confirms
     * the completion of the file upload by entering '1', the system reads the files
     * and updates the medical records for
     * each patient found in the files. Each CSV file is expected to contain patient
     * information, including patient ID,
     * diagnoses, prescriptions, and treatment plans, with multiple values separated
     * by semicolons.
     * </p>
     *
     * <p>
     * The method processes each CSV file in the folder by calling a utility
     * function to read the file and then
     * updates the corresponding medical records using the
     * `medicalRecordController.updateMedicalRecord` method.
     * </p>
     *
     */
    public void uploadMedicalRecordsFromFile() {
        Printer.print("Please upload your medical record files into the folder.", lang);
        Printer.print("Once you've finished, enter '1' to continue:", lang);

        // Wait for user to enter '1' to continue
        int confirmation = -1;
        while (confirmation != 1) {
            confirmation = sc.nextInt();
            if (confirmation != 1) {
                Printer.print("Invalid input. Please enter '1' when you've finished uploading files.", lang);
            }
        }
        Printer.print("Your file has been received! Updating values now...", lang);
        sc.nextLine(); // Consume newline
        String[] csvFiles = FileScanner.scanFilesInFolder();

        for (String fileName : csvFiles) {
            System.out.println(fileName);
            List<String[]> csvValues = CSVReader.csvReaderUtil(fileName);
            for (String[] values : csvValues) {
                medicalRecordController.updateMedicalRecord(values[0], // patientId
                        Arrays.asList(values[1].split(";")), // diagnoses
                        Arrays.asList(values[2].split(";")), // prescriptions
                        Arrays.asList(values[3].split(";")));
            }
        }
    }

    // Function to delete treatment plans by number
    /**
     * Deletes specified items from a medical record list (diagnoses, prescriptions,
     * or treatment plans).
     * <p>
     * This method allows the user to delete items from a specified list (diagnoses,
     * prescriptions, or treatment plans)
     * within a medical record. The user is prompted to provide the indices of the
     * items to delete, which are specified
     * by the list type passed as an argument. If no indices are provided, all items
     * in the list are deleted.
     * The items are identified by their index positions in the list, which are
     * provided by the user as comma-separated numbers.
     * </p>
     * 
     * <p>
     * If the user enters invalid indices or numbers, appropriate error messages are
     * displayed. After the deletion process,
     * the updated list is printed.
     * </p>
     * 
     * @param listType      The type of the list to be modified. Can be one of:
     *                      - "diagnoses"
     *                      - "prescriptions"
     *                      - "treatment plans"
     * @param medicalRecord The medical record from which the list will be modified.
     */
    public void deleteItems(String listType, MedicalRecord medicalRecord) {
        List<String> currentList = null;
        String listName = "";

        // Assign correct list and methods based on the type
        switch (listType) {
            case "diagnoses":
                currentList = medicalRecord.getDiagnoses();
                listName = "diagnoses";
                break;
            case "prescriptions":
                currentList = medicalRecord.getPrescriptions();
                listName = "prescriptions";
                break;
            case "treatment plans":
                currentList = medicalRecord.getTreatmentPlans();
                listName = "treatment plans";
                break;
            default:
                Printer.print("Invalid list type.", lang);
                return;
        }

        // Print current list
        printList(currentList, listName, medicalRecord);

        // Ask for user input for deletion
        Printer.print(
                "Enter the " + listName
                        + " to delete by their numbers, separated by semicolon (;), or press Enter to delete all: ",
                lang);
        String inputToDelete = sc.nextLine();

        // If input is empty, clear all items
        if (inputToDelete.isEmpty()) {
            currentList.clear();
            Printer.print("All " + listName + " have been deleted.", lang);
        } else {
            // Process input (comma separated numbers)
            String[] numbers = inputToDelete.split(";");
            List<Integer> indicesToDelete = new ArrayList<>();

            for (String num : numbers) {
                try {
                    int index = Integer.parseInt(num.trim()) - 1; // Convert to 0-based index
                    if (index >= 0 && index < currentList.size()) {
                        indicesToDelete.add(index);
                    } else {
                        Printer.print("Invalid index: " + (index + 1), lang);
                    }
                } catch (NumberFormatException e) {
                    Printer.print("Invalid number format: " + num, lang);
                }
            }

            // Sort indices in descending order to avoid issues while removing
            Collections.sort(indicesToDelete, Collections.reverseOrder());

            // Remove the items by indices
            for (int index : indicesToDelete) {
                currentList.remove(index);
                Printer.print("Item removed.", lang);
            }
        }

        // Print updated list
        printList(currentList, listName, medicalRecord);
    }

    /**
     * Prints the contents of a specified list (diagnoses, prescriptions, or
     * treatment plans) for a given medical record.
     * <p>
     * This method displays the current items in the list specified by the listName
     * (e.g., "diagnoses", "prescriptions", or "treatment plans")
     * for the patient associated with the provided medical record. If the list is
     * empty, a message indicating that no items are found is displayed.
     * Each item in the list is printed with a corresponding index, starting from 1.
     * </p>
     *
     * @param list          The list of items (diagnoses, prescriptions, or
     *                      treatment plans) to be printed.
     * @param listName      The name of the list being printed (e.g., "diagnoses",
     *                      "prescriptions", or "treatment plans").
     * @param medicalRecord The medical record containing the patient information,
     *                      including the patient ID.
     */
    private static void printList(List<String> list, String listName, MedicalRecord medicalRecord) {
        Printer.print("Current " + listName + " for patient " + medicalRecord.getPatientId() + ":", lang);
        if (list.isEmpty()) {
            Printer.print("No " + listName + " found.", lang);
        } else {
            for (int i = 0; i < list.size(); i++) {
                Printer.print((i + 1) + ". " + list.get(i), lang);
            }
        }
    }

    /**
     * Allows the user to update the personal information of a doctor.
     * <p>
     * This method presents various prompts to update the doctor's personal
     * information, such as name, username, password,
     * gender, contact number, and email address. The user can either provide new
     * values or skip each prompt by pressing Enter.
     * If any changes are made, the updated information is saved, and a success
     * message is displayed. If no changes are made,
     * a message indicating no updates were made is shown.
     * </p>
     *
     * @param doctor The doctor whose personal information is to be updated.
     * @see Printer#printHeader(String)
     * @see Input#inputName(String, boolean)
     * @see Input#inputUsername(String, String, String, boolean)
     * @see Input#inputPassword(String, boolean)
     * @see Input#inputGender(String, boolean)
     * @see Input#inputContactNumber(String, boolean)
     * @see Input#inputEmailAddress(String, boolean, String, String)
     */
    public void updatePersonalInfo(Doctor doctor) {
        Scanner sc = new Scanner(System.in);

        // Print header
        Printer.printHeader("----------------- Update Personal Info -----------------");

        boolean isUpdated = false; // Flag to track updates

        // Prompt for personal information update
        Printer.print("Enter new name (or press Enter to skip): ", lang);
        String name = Input.inputName(lang, true);
        if (!name.isEmpty()) {
            doctor.setName(name);
            isUpdated = true;
        }

        // Update Username
        Printer.print(
                "Enter new username (Enter 'a' to auto-generate or enter a valid username or Press Enter to skip): ",
                lang);
        String username = Input.inputUsername(lang, name, "staff", true);
        if (username != null) {
            doctor.setUsername(username);
            isUpdated = true;
        }

        // Update Password
        Printer.print("Enter new password (or press Enter to skip): ", lang);
        String password = Input.inputPassword(lang, true);
        if (!password.isEmpty()) {
            doctor.setPassword(Util.hashPassword(password));
            isUpdated = true;
        }

        // Update Gender
        Printer.print("Enter new gender (press Enter to skip): ", lang);
        String gender = Input.inputGender(lang, true);
        if (!gender.isEmpty()) {
            doctor.setGender(gender);
            isUpdated = true;
        }

        // Update Contact Number
        Printer.print("Enter new contact number (or press Enter to skip): ", lang);
        String contactNumber = Input.inputContactNumber(lang, true, "staff");
        if (!contactNumber.isEmpty()) {
            doctor.setContactNumber(contactNumber);
            isUpdated = true;
        }

        // Update Email Address
        Printer.print("Enter new email address (or press Enter to skip): ", lang);
        String emailAddress = Input.inputEmailAddress(lang, true, name, "staff");
        if (!emailAddress.isEmpty()) {
            doctor.setEmailAddress(emailAddress);
            isUpdated = true;
        }

        // Check if any updates were made
        if (isUpdated) {
            String response = doctorController.updatePersonalInfo(doctor.getId(), doctor, null);
            Printer.print(response, lang);
        } else {
            Printer.print("No information was updated.", lang);
        }

    }
}