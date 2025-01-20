package boundary.Applications;

import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.time.LocalTime;
import java.time.LocalDate;

import model.Appointment.Appointment;
import model.Appointment.AppointmentOutcome;
import model.Appointment.AppointmentOutcome.BillingStatus;
import model.Users.Doctor;
import model.Users.Patient;
import model.Users.User;
import repository.Users.StaffDB;
import utils.Utility.Config;
import utils.Utility.Input;
import utils.Utility.Printer;
import utils.Utility.Util;
import utils.Utility.Validation;
import DTO.PatientDetailsDTO;
import DTO.PatientMedicalRecordDTO;
import controller.Appointment.AppointmentController;
import controller.MedicalRecord.MedicalRecordController;
import controller.Users.PatientController;
import controller.Users.AdminController;
import controller.Users.DoctorController;

/**
 * Represents the main application interface for patients in the hospital management system.
 * 
 * The `PatientApp` class provides a menu-driven interface for patients to manage their personal 
 * information, medical records, appointments, and billing details. It allows patients to interact with 
 * the system by selecting options from a list of available actions. The app also handles language selection 
 * and ensures that patient-specific data is updated accordingly.
 * 
 * Key functionalities include:
 * <ul>
 *     <li>Viewing and updating personal information (e.g., name, contact info, password)</li>
 *     <li>Viewing medical records and appointment histories</li>
 *     <li>Scheduling, rescheduling, and canceling appointments</li>
 *     <li>Viewing and paying bills</li>
 *     <li>Logging out of the system</li>
 * </ul>
 * The app also allows patients to interact with doctors by viewing available appointment slots, scheduling 
 * new appointments, and managing past and upcoming appointments.
 * 
 * The app utilizes a console interface to display options and interact with the patient through inputs.
 * It leverages various utility classes and controllers to handle the underlying functionality and data 
 * processing.
 * 
 * The application supports multiple languages, allowing patients to choose between English, Spanish, and French.
 */
public class PatientApp {
    private static String lang = "en";
    private static PatientApp instance;
    private static Scanner sc = new Scanner(System.in);

    // Controllers instantiation
    private DoctorController doctorController = new DoctorController();
    private AdminController adminController = new AdminController();
    private PatientController patientController = new PatientController();
    private AppointmentController appointmentController = new AppointmentController();
    private MedicalRecordController medicalRecordController = new MedicalRecordController();

    /**
     * Constructs a new instance of the PatientApp.
     * 
     * This constructor is used for initializing the PatientApp object. Any required 
     * setup or initialization tasks for the app can be performed here.
     */
    private PatientApp() {
        // Initialization or any required setup can be done here
    }

    /**
     * Returns the singleton instance of the PatientApp.
     * 
     * This method implements the Singleton design pattern to ensure that only one 
     * instance of the PatientApp class is created throughout the application's lifecycle. 
     * The instance is created only when it is first requested (lazy initialization).
     *
     * @return the singleton instance of the PatientApp
     */
    public static PatientApp getInstance() {
        if (instance == null) {
            // Create the instance only when it's needed (Lazy initialization)
            instance = new PatientApp();
        }
        return instance;
    }

    // Public method to provide access to the private startPatientApp method
    /**
     * Initializes the PatientApp with the given patient.
     * 
     * This method sets up the app by starting the `PatientApp` for the specified patient.
     * It can be used to configure the app or perform any necessary setup specific to the patient.
     * 
     * @param patient the patient object that will be used to initialize the app
     */
    public void initializeApp(Patient patient) {
        startPatientApp(patient);
    }

    /**
     * Starts the PatientApp for the given patient.
     * 
     * This method presents a menu to the patient with various options such as viewing medical records, 
     * scheduling appointments, and managing personal information. The menu allows the patient to 
     * interact with the system, make choices, and perform different tasks related to their healthcare.
     * It also handles language selection and patient-specific configurations.
     * 
     * @param patient the patient object that the app will interact with
     */
    private void startPatientApp(Patient patient) {
        int choice;
        String menuHeader;
        List<String> menuOptions;
        ArrayList<String> doctorIds = new ArrayList<String>();
        ArrayList<String> doctorNames = new ArrayList<String>();
        HashMap<String, User> staffDB = StaffDB.getStaff();
        for (Map.Entry<String, User> entry : staffDB.entrySet()) {
            String staffId = entry.getKey();
            User user = entry.getValue();
            if (user instanceof Doctor) {
                doctorIds.add(staffId);
                doctorNames.add(user.getName());
            }
        }

        System.out.println("Please select a language (1 - English, 2 - Spanish, 3 - French): ");

        String input = sc.nextLine().trim();

        // Check if the input is empty (i.e., user pressed Enter without entering
        // anything)
        if (input.isEmpty()) {
            lang = "en"; // Default to English
            System.out.println("No input detected, defaulting to English.");
        } else {
            switch (input) {
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
            // Display menu
            menuHeader = "Patient Menu";
            menuOptions = Arrays.asList(
                    "View Medical Record",
                    "View Personal Information",
                    "Update Personal Information",
                    "View Available Appointment Slots",
                    "Schedule an Appointment",
                    "Reschedule an Appointment",
                    "Cancel an Appointment",
                    "View Scheduled Appointments",
                    "View Past Appointment Outcome Records",
                    "View All Appointments (including canceled and past)",
                    "View and Pay Bills",
                    "Logout");
            Printer.printMenu(menuHeader, menuOptions);

            Printer.print("Please choose an option (1-12): ", lang);
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    // View Medical Record
                    Printer.printHeader("----------------- Patient Medical Record -----------------");
                    PatientMedicalRecordDTO medicalRecord = medicalRecordController
                            .viewMedicalRecordByPatientId(patient.getId());

                    if (medicalRecord == null) {
                        Printer.print("\nNo Medical Record found.", lang);
                        break;
                    } else {
                        Printer.printMedicalRecords(medicalRecord);
                    }
                    break;
                case 2:
                    // View Personal Info
                    Printer.printHeader("----------------- Personal Information -----------------");
                    PatientDetailsDTO patientDetailsDTO2 = patientController.viewPersonalInfo(patient.getId());
                    Printer.printPersonalInformation(patientDetailsDTO2);
                    Printer.print("\nEnter 3 if you need to update your personal info.", lang);
                    break;
                case 3:
                    // Update Personal Info
                    updatePersonalInfo(patient);
                    break;

                case 4:
                    // View Available Appointment Slots
                    Printer.printHeader("----------------- View Available Appointment Slots -----------------");
                    String dString;
                    do {
                        Printer.print("\nEnter your preferred doctor ID or enter 'q' to quit):", lang);
                        int num = 0;
                        for (String doctorName : doctorNames) {
                            num++;
                            Printer.print(num + ": " + doctorName, lang);
                        }
                        dString = sc.next();
                        // Check if the user pressed 'q' to quit
                        if (dString.equalsIgnoreCase("q")) {
                            Printer.print("Cancelled. No further action taken.", lang);
                            break;
                        } else {
                            int d = Integer.parseInt(dString);
                            if (d > doctorIds.size() || (d < 1 && d != -1)) {
                                Printer.print("Invalid number. Please choose from the options above.", lang);
                                continue;
                            }
                            String doctorId = doctorIds.get(d - 1);
                            Map<LocalDate, List<LocalTime>> monthlyAvailability = appointmentController
                                    .viewAvailableAppointmentSlots(doctorId);
                            Printer.printMonthlyAvailability(monthlyAvailability);
                        }
                    } while (dString.equalsIgnoreCase("q"));
                    break;

                case 5:
                    // Schedule an Appointment
                    Printer.printHeader("----------------- Schedule An Appointemnt -----------------");
                    int d = -1;
                    String doctorId = "";
                    do {
                        Printer.print("\nEnter your preferred doctor ID or enter 'q' to quit):", lang);
                        int num = 0;
                        for (String doctorName : doctorNames) {
                            num++;
                            Printer.print(num + ": " + doctorName, lang);
                        }
                        dString = sc.next();
                        // Check if the user pressed 'q' to quit
                        if (dString.equalsIgnoreCase("q")) {
                            Printer.print("Cancelled. No further action taken.", lang);
                            break;
                        } else {
                            d = Integer.parseInt(dString);
                            if (d > doctorIds.size() || (d < 1 && d != -1)) {
                                Printer.print("Invalid number. Please choose from the options above.", lang);
                                continue;
                            }
                        }
                    } while (dString.equalsIgnoreCase("q"));

                    doctorId = doctorIds.get(d - 1);
                    Map<LocalDate, List<LocalTime>> monthlyAvailability = appointmentController
                            .viewAvailableAppointmentSlots(doctorId);
                    Printer.printMonthlyAvailability(monthlyAvailability);

                    String date;
                    do {
                        Printer.print("Enter your preferred date (yyyy-MM-dd) or enter 'q' to quit: ", lang);
                        date = sc.next();
                        // Check if the user pressed 'q' to quit
                        if (date.equalsIgnoreCase("q")) {
                            Printer.print("Cancelled. No further action taken.", lang);
                            break;
                        }
                        if (!Validation.validateDate(date)) {
                            Printer.print("Invalid input. Date must be in the format yyyy-MM-dd.", lang);
                        }
                    } while (!Validation.validateDate(date));
                    String time;
                    do {
                        Printer.print("Enter your preferred time (HH:mm) or enter 'q' to quit:", lang);
                        time = sc.next();
                        // Check if the user pressed 'q' to quit
                        if (time.equalsIgnoreCase("q")) {
                            Printer.print("Cancelled. No further action taken.", lang);
                            break;
                        }
                        if (!Validation.validateTime(time)) {
                            Printer.print("Invalid input. Time must be in the format HH:mm.", lang);
                        }
                    } while (!Validation.validateTime(time));
                    String dateTime = date + "T" + time;
                    String response = appointmentController.scheduleAppointment(doctorId, patient.getId(), dateTime, "PENDING");
                    Printer.print(response, lang);
                    break;

                    case 6:
                    // Reschedule an Appointment
                    ArrayList<Integer> appointmentIds1 = new ArrayList<Integer>();
                    Printer.printHeader("----------------- Scheduled Appointments -----------------");
                    List<Appointment> appointments1 = appointmentController.viewScheduledAppointments(patient.getId());
                    if (appointments1 == null) {
                        Printer.print("\nNo Scheduled Appointment found.", lang);
                        break;
                    }
                    Printer.printAppointments(appointments1);
                    for (Appointment appointment : appointments1) {
                        appointmentIds1.add(appointment.getAppointmentId());
                    }
                    int appointmentID;
                    do {
                        Printer.print("Enter Appointment ID to reschedule: ", lang);
                        appointmentID = sc.nextInt();
                        sc.nextLine();
                        if (!appointmentIds1.contains(appointmentID)) {
                            Printer.print("Invalid Appointment ID. Please choose from the above options.", lang);
                        }
                    } while (!appointmentIds1.contains(appointmentID));
                
                    Appointment apptToReschedule = appointmentController.viewAppointmentById(appointmentID);
                    // Print doctor availability
                    String docId = apptToReschedule.getDoctorId();
                    monthlyAvailability = appointmentController.viewAvailableAppointmentSlots(docId);
                    Printer.printMonthlyAvailability(monthlyAvailability);
                
                    Printer.print("Enter new date (yyyy-MM-dd) or enter 'q' to quit: ", lang);
                    String newDate = sc.nextLine();
                    // Check if the user pressed 'q' to quit
                    if (newDate.equalsIgnoreCase("q")) {
                        Printer.print("Cancelled. No further action taken.", lang);
                        break;
                    }
                
                    Printer.print("Enter new time (HH:mm) or enter 'q' to quit: ", lang);
                    String newTime = sc.nextLine();
                    // Check if the user pressed 'q' to quit
                    if (newTime.equalsIgnoreCase("q")) {
                        Printer.print("Cancelled. No further action taken.", lang);
                        break;
                    }
                
                    // Validate if the new time is available
                    if (!appointmentController.isTimeSlotAvailable(docId, newDate, newTime)) {
                        Printer.print("The requested time slot is not available. Please choose a different time.", lang);
                        break;
                    }
                
                    // Create the new date-time string
                    String newDateTime = newDate + "T" + newTime;
                    // Reschedule the appointment
                    appointmentController.rescheduleAppointment(appointmentID, newDateTime);
                
                    Printer.print("Appointment rescheduled successfully.", lang);
                    // AppointmentDB.printAllAppointments();
                    break;
                
                case 7:
                    // Cancel an Appointment
                    ArrayList<Integer> appointmentIds2 = new ArrayList<Integer>();
                    Printer.printHeader("----------------- Scheduled Appointemnts -----------------");
                    List<Appointment> appointments2 = appointmentController.viewScheduledAppointments(patient.getId());
                    if (appointments2 == null) {
                        Printer.print("\nNo Scheduled Appointment found.", lang);
                        break;
                    }

                    Printer.printAppointments(appointments2);

                    for (Appointment appointment : appointments2) {
                        appointmentIds2.add(appointment.getAppointmentId());
                    }
                    int cancelAppointmentID = -1;
                    do {
                        Printer.print("Enter Appointment ID to cancel or enter 'q' to quit: ", lang);
                        String apptIDStr = sc.next();
                        // Check if the user pressed 'q' to quit
                        if (apptIDStr.equalsIgnoreCase("q")) {
                            Printer.print("Cancelled. No further action taken.", lang);
                            break;
                        } else {
                            cancelAppointmentID = Integer.parseInt(apptIDStr);
                        }
                        if (!appointmentIds2.contains(cancelAppointmentID)) {
                            Printer.print("Invalid Appointment ID. Please choose from the above options.", lang);
                        }
                    } while (!appointmentIds2.contains(cancelAppointmentID));

                    appointmentController.cancelAppointment(cancelAppointmentID);
                    Printer.print("Appointment cancelled successfully.", lang);
                    // AppointmentDB.printAllAppointments();
                    break;

                case 8:
                    // View Scheduled Appointments
                    List<Appointment> appointments3 = appointmentController.viewScheduledAppointments(patient.getId());
                    if (appointments3 == null) {
                        Printer.print("\nNo Scheduled Appointment found.", lang);
                        break;
                    }

                    Printer.printAppointments(appointments3);
                    break;

                case 9:
                    // View Past Appointment Outcome Records
                    ArrayList<Integer> appointmentIds3 = new ArrayList<Integer>();
                    Printer.printHeader("----------------- Past Appointemnts -----------------");
                    List<Appointment> appointments4 = appointmentController.viewPastAppointmentsByPatientId(patient.getId());
                    if (appointments4.isEmpty()) {
                        Printer.print("\nNo Past Appointment found.", lang);
                        break;
                    }

                    Printer.printAppointments(appointments4);

                    for (Appointment appointment : appointments4) {
                        appointmentIds3.add(appointment.getAppointmentId());
                    }
                    int pastAppointmentID = -1;
                    do {
                        Printer.print("Enter Appointment ID to view outcome or enter 'q' to quit: ", lang);
                        String pastApptIDStr = sc.next();
                        // Check if the user pressed 'q' to quit
                        if (pastApptIDStr.equalsIgnoreCase("q")) {
                            Printer.print("Cancelled. No further action taken.", lang);
                            break;
                        } else {
                            pastAppointmentID = Integer.parseInt(pastApptIDStr);
                        }
                        if (!appointmentIds3.contains(pastAppointmentID)) {
                            Printer.print("Invalid Appointment ID. Please choose from the above options.", lang);
                        }
                    } while (!appointmentIds3.contains(pastAppointmentID));
                    Printer.printHeader("----------------- Appointment Outcome -----------------");
                    AppointmentOutcome outcome = appointmentController.viewAppointmentOutcome(pastAppointmentID);
                    if (outcome == null) {
                        Printer.print("\nAppointment outcome cannot be found.", lang);
                        break;
                    }
                    Printer.printAppointmentOutcome(outcome);
                    break;

                case 10:
                    // View All Appointments (including cancelled and past)
                    Printer.printHeader("----------------- View All Appointemnts -----------------");
                    List<Appointment> appointments5 = appointmentController.viewAppointments(patient.getId());
                    if (appointments5 == null) {
                        Printer.print("\nNo Appointment found.", lang);
                        break;
                    }
                    Printer.printAppointments(appointments5);
                    break;

                case 11:
                    // View and Pay Bills
                    Printer.printHeader("----------------- Outstanding Bills -----------------\n");
                    ArrayList<Integer> appointmentIds5 = new ArrayList<Integer>();
                    ArrayList<String> doctorNameList = new ArrayList<String>();
                    ArrayList<String> dateList = new ArrayList<String>();
                    List<Appointment> appointments6 = appointmentController.viewPastAppointmentsByPatientId(patient.getId());
                    if (appointments6.isEmpty()) {
                        Printer.print("\nNo bills to view.", lang);
                        break;
                    }

                    for (Appointment appointment : appointments6) {
                        appointmentIds5.add(appointment.getAppointmentId());
                        String doctorID = appointment.getDoctorId();
                        String doctorName = doctorController.getUserById(doctorID).getName();
                        doctorNameList.add(doctorName);
                        dateList.add(appointment.getDateTime().toLocalDate().toString());
                    }

                    for (int i = 0; i < appointmentIds5.size(); i++) {
                        int appointmentId = appointmentIds5.get(i);
                        String doctorName = doctorNameList.get(i);
                        String dateString = dateList.get(i);
                        AppointmentOutcome apptOutcome = appointmentController.viewAppointmentOutcome(appointmentId);
                        List<Double> medicationFeeList = apptOutcome.getMedicationFee();

                        // Convert the list to a comma-delimited string
                        String medicationFeeString = medicationFeeList.stream()
                            .map(String::valueOf)  // Convert each item to a string
                            .collect(Collectors.joining(", "));  // Join items with a comma and space

                        Printer.print(String.format("Bills for Appointment ID %d with Doctor %s:%n",
                                appointmentId, doctorName, dateString), lang);
                        Printer.print("Date : " + dateString + " \n"
                                + "Billing status : " + apptOutcome.getBillingStatus() + " \n"
                                + "Consulation fee : " + apptOutcome.getConsultationFee() + " \n"
                                + "Medication fee : " + medicationFeeString + " \n"
                                + "Total amount : " + apptOutcome.getTotalAmount() + " \n", lang);
                    }

                    int payAppointmentID = -1;
                    String creditCard = "";
                    String email = "";
                    boolean exitLoop = false; // Flag to indicate when to exit the loop
                    
                    do {
                        Printer.print("Enter Appointment ID to pay or enter 'q' to quit:", lang);
                        String payApptIDStr = sc.nextLine().trim(); // Trim input to remove extra spaces
                    
                        // Check if the user pressed 'q' to quit
                        if (payApptIDStr.equalsIgnoreCase("q")) {
                            Printer.print("Cancelled. No further action taken.", lang);
                            exitLoop = true; // Set the flag to true to exit the loop
                            break; // Break the loop, don't exit the method
                        }
                    
                        try {
                            payAppointmentID = Integer.parseInt(payApptIDStr);
                            if (!appointmentIds5.contains(payAppointmentID)) {
                                Printer.print("Invalid Appointment ID. Please choose from the above options.", lang);
                            } else {
                                // Fetch the appointment outcome
                                AppointmentOutcome apptOutcome = appointmentController.viewAppointmentOutcome(payAppointmentID);
                                if (apptOutcome.getBillingStatus() != BillingStatus.UNPAID) {
                                    Printer.print("Payment has already been made for Appointment ID " + payAppointmentID, lang);
                                    exitLoop = true; // Set the flag to exit the loop
                                    break; // Break the loop, don't exit the method
                                }
                    
                                // Valid appointment ID and unpaid status, proceed with further inputs
                                Printer.print("Enter credit card details:", lang);
                                creditCard = sc.nextLine();
                    
                                Printer.print("Enter email (to receive receipt):", lang);
                                email = sc.nextLine();
                    
                                Printer.print("Enter password to authenticate:", lang);
                                String password = sc.nextLine();
                    
                                // Validate password
                                if (patient.getPassword().equals(Util.hashPassword(password))) {
                                    Printer.print("Password is correct.", lang);
                    
                                    // Mark the appointment as paid
                                    appointmentController.payBill(payAppointmentID, BillingStatus.PAID, false);
                    
                                    // Print success message
                                    Printer.print(String.format("Payment successful for Appointment ID %d using credit card %s", 
                                        payAppointmentID, creditCard), lang);
                                    Printer.print(String.format("Receipt will be sent to the email %s", email), lang);
                    
                                    exitLoop = true; // Set the flag to exit the loop after payment
                                    break; // Break the loop, don't exit the method
                                } else {
                                    Printer.print("Password is incorrect. Payment unsuccessful.", lang);
                                }
                            }
                        } catch (NumberFormatException e) {
                            Printer.print("Invalid input. Please enter a valid Appointment ID or 'q' to quit.", lang);
                        }
                    } while (!exitLoop); // Continue until the flag is set to true
                    
                    break;

                case 12:
                    // Logout
                    Printer.print("Exiting...", lang);
                    return;

                default:
                    Printer.print("Invalid option. Please choose a number between 1 and 11.", lang);
            }
        } while (choice != 12);

        sc.close();
        Printer.print("Thank you for using the Hospital Management System!", lang);
    }

    /**
     * Updates the personal information of the given patient.
     * 
     * This method allows the patient to update various personal details including their name, 
     * username, password, gender, contact number, email address, and date of birth. For each field, 
     * the patient is prompted to provide new information, with the option to skip the update by pressing Enter. 
     * If any changes are made, the patient's information is updated, and a confirmation message is displayed.
     * 
     * @param patient the patient whose personal information is to be updated
     */
    public void updatePersonalInfo(Patient patient) {
        boolean isUpdated = false; // Flag to track updates

        // Update Name
        Printer.print("Enter new name (press Enter to skip): ", lang);
        String name = Input.inputName(lang, true);
        if (!name.isEmpty()) {
            patient.setName(name);
            isUpdated = true;
        }

        // Update Username
        Printer.print("Enter new username (Enter 'a' to auto-generate or enter a valid username or Press Enter to skip): ",
                lang);
        String username = Input.inputUsername(lang, name, "patient", true);
        if (username != null) {
            patient.setUsername(username);
            isUpdated = true;
        }

        // Update Password
        Printer.print("Enter new password (press Enter to skip): ", lang);
        String password = Input.inputPassword(lang, true);
        if (!password.isEmpty()) {
            patient.setPassword(Util.hashPassword(password));
            isUpdated = true;
        }

        // Update Gender
        Printer.print("Enter new gender (press Enter to skip): ", lang);
        String gender = Input.inputGender(lang, true);
        if (!gender.isEmpty()) {
            patient.setGender(gender);
            isUpdated = true;
        }

        // Update Contact Number
        Printer.print("Enter new contact number (press Enter to skip): ", lang);
        String contactNumber = Input.inputContactNumber(lang, true, "staff");
        if (!contactNumber.isEmpty()) {
            patient.setContactNumber(contactNumber);
            isUpdated = true;
        }

        // Update Email Address
        Printer.print("Enter new email address (press Enter to skip): ", lang);
        String email = sc.nextLine();
        if (!email.isEmpty()) {
            patient.setEmailAddress(email);
            isUpdated = true;
        }

        // Update Date of Birth
        Printer.print("Enter new date of birth (yyyy-MM-dd) (press Enter to skip): ", lang);
        LocalDate newDOB = Input.inputDOB(lang, true);
        if (newDOB != null) {
            patient.setDateOfBirth(newDOB);
            isUpdated = true;
        }

        // Check if any updates were made
        if (isUpdated) {
            String response = patientController.updatePersonalInfo(patient.getId(), patient, null);
            Printer.print(response, lang);
        } else {
            Printer.print("No information was updated.", lang);
        }
    }

}
