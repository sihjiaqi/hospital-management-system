package utils.Utility;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import DTO.PatientDetailsDTO;
import DTO.PatientMedicalRecordDTO;
import DTO.AppointmentAndOutcomeDTO;
import DTO.AppointmentOutcomeDTO;
import model.Appointment.Appointment;
import model.Appointment.AppointmentOutcome;
import model.Appointment.Appointment.AppointmentStatus;
import model.MedicalRecord.MedicalRecord;
import model.Medication.Medication;
import model.Medication.ReplenishRequest;
import model.Users.Admin;
import model.Users.Doctor;
import model.Users.Patient;
import model.Users.Pharmacist;
import model.Users.User;
import utils.Translator.Translate;

/**
 * The {@code Printer} class provides utility methods to print various types of
 * formatted output
 * for different objects and data structures. It is designed to display
 * information in a human-readable
 * tabular format with dynamic column widths, separator lines, and custom
 * headers.
 * The class includes methods for printing detailed information for entities
 * such as staff, patients,
 * medications, appointments, and replenish requests, as well as a method to
 * print the doctor’s
 * schedule and monthly availability.
 * 
 * The output is customizable based on user language preferences, supporting
 * multiple languages
 * and formatting choices. The data is retrieved dynamically using reflection,
 * allowing flexibility
 * in handling different object types without the need to hardcode values.
 * 
 * Key features include:
 * <ul>
 * <li>Dynamic table formatting with customizable column widths</li>
 * <li>Support for displaying detailed entity information (e.g., staff,
 * medications, appointments)</li>
 * <li>Flexible handling of object properties using reflection and getter
 * methods</li>
 * <li>Support for different languages, with translation of headers and
 * options</li>
 * <li>Formatted output for displaying schedules and availability</li>
 * </ul>
 * 
 * The methods use the {@link Util} class for printing, managing language
 * preferences, and formatting.
 */
public class Printer {
    private static final int TOTAL_WIDTH = 80; // Total width of the table
    private static final int HEADER_WIDTH = 50; // Total width of the table
    private static final int LABEL_WIDTH = 20; // Width of the label column
    private static final int CONTENT_WIDTH = TOTAL_WIDTH - LABEL_WIDTH - 6; // Content width including borders
    private static String lang = Config.getLang();

    /**
     * Prints the provided text to the console in the specified language.
     * <p>
     * If the specified language is "en" (English), the method directly prints the
     * text as is.
     * For other languages, it attempts to translate the text using a translation
     * service before printing it.
     * If the text is null or empty, or if the language is null or empty, the method
     * prints an error message.
     * If the translation fails, it prints the original text.
     * </p>
     * 
     * @param text The text to be printed. If the text is null, an error message is
     *             printed.
     * @param lang The language code (e.g., "en" for English). If the language is
     *             null or empty, an error message is printed.
     */
    public static void print(String text, String lang) {
        if (text == null || lang == null || lang.isEmpty()) {
            System.out.println("Invalid input: text or language is null/empty.");
            return;
        }

        if (lang.equals("en")) {
            System.out.println(text);
        } else {
            try {
                // Assuming Translate.translate() is a method that translates text
                String translatedText = Translate.translate(text, lang);
                System.out.println(translatedText);
            } catch (Exception e) {
                // Print the exception or log it for debugging purposes
                System.out.println(text);
            }
        }
    }

    private static void printSeparatorLine() {
        String lang = Config.getLang();

        print("=".repeat(TOTAL_WIDTH + 1), lang);
    }

    // Function to print table row
    private static void printTableRow(String label, Object content) {
        String lang = Config.getLang();

        String contentString = "";

        // Handle case where content is a List<String>
        if (content instanceof List) {
            contentString = String.join(", ", (List<String>) content); // Join the list into a single string with commas
        }
        // Handle case where content is of any other type (LocalDateTime,
        // AppointmentStatus, etc.)
        else if (content instanceof LocalDateTime) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            contentString = ((LocalDateTime) content).format(formatter); // Format the LocalDateTime
        } else if (content instanceof AppointmentStatus) {
            contentString = ((AppointmentStatus) content).toString();
        } else {
            contentString = content.toString();
        }
        String formattedString;
        // Print the row with wrapping logic if content is too long
        if (contentString.length() <= CONTENT_WIDTH) {
            formattedString = String.format("| %-20s | %-54s |", label, contentString);

            print(formattedString, lang);
        } else {
            formattedString = String.format("| %-20s | %-54s |", label, contentString.substring(0, CONTENT_WIDTH)); 
            // Print the first part of the content if it's too long
            print(formattedString, lang);
            contentString = contentString.substring(CONTENT_WIDTH);

            // Print the subsequent parts of the content, wrapped to fit the column width
            while (contentString.length() > CONTENT_WIDTH) {
                formattedString = String.format("| %-20s | %-54s |", "", contentString.substring(0, CONTENT_WIDTH));
                print(formattedString, lang);
                contentString = contentString.substring(CONTENT_WIDTH);
            }

            // Print the remaining content, if any
            if (!contentString.isEmpty()) {
                formattedString = String.format("| %-20s | %-54s |", "", contentString);
                print(formattedString, lang);
            }
        }
    }

    /**
     * Prints the medical records of patients in a formatted table-like structure.
     * 
     * This method iterates over a list of medical records and prints the patient ID
     * followed by their diagnoses, prescriptions, and treatment plans. Each section
     * is displayed with proper alignment and is separated by lines for better
     * readability.
     *
     * @param medicalRecords A list of {@link MedicalRecord} objects to be printed.
     *                       Each record contains patient-specific medical
     *                       information.
     * @see MedicalRecord
     * @see Config
     * @see Util
     */
    public static void printMedicalRecords(List<PatientMedicalRecordDTO> patientMedicalRecordDTO) {
        String lang = Config.getLang();
        for (PatientMedicalRecordDTO record : patientMedicalRecordDTO) {
            printSeparatorLine();
            print(String.format("| %-77s |", "Patient Id: " + record.getPatientId()), lang);
            printSeparatorLine();
            printTableRow("Name", record.getName());
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = record.getDateOfBirth().format(dateFormatter);
            printTableRow("Date of Birth", formattedDate);
            printTableRow("Contact Number", record.getContactNumber());
            printTableRow("Email Address", record.getEmailAddress());
            printTableRow("Blood Type", record.getBloodType());
            printTableRow("Diagnoses", record.getDiagnoses());
            printTableRow("Prescriptions", record.getPrescriptions());
            printTableRow("Treatment Plans", record.getTreatmentPlans());
        }
        printSeparatorLine();
    }

    // Overloaded method to accept a single MedicalRecord
    /**
     * Prints the medical records of a patient in a formatted table-like structure.
     * 
     * This method prints detailed patient information including their ID, name,
     * date of birth, contact details, blood type, diagnoses, prescriptions, and
     * treatment plans. Each piece of information is displayed with proper alignment
     * and separated by lines for better readability.
     *
     * @param patientMedicalRecordDTO A {@link PatientMedicalRecordDTO} object
     *                                containing
     *                                the patient's medical information to be
     *                                printed.
     * @see PatientMedicalRecordDTO
     * @see Config
     * @see Util
     */
    public static void printMedicalRecords(PatientMedicalRecordDTO patientMedicalRecordDTO) {
        String lang = Config.getLang();

        printSeparatorLine();
        print(String.format("| %-77s |", "Patient Id: " + patientMedicalRecordDTO.getPatientId()), lang);
        printSeparatorLine();
        printTableRow("Name", patientMedicalRecordDTO.getName());
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = patientMedicalRecordDTO.getDateOfBirth().format(dateFormatter);
        printTableRow("Date of Birth", formattedDate);
        printTableRow("Contact Number", patientMedicalRecordDTO.getContactNumber());
        printTableRow("Email Address", patientMedicalRecordDTO.getEmailAddress());
        printTableRow("Blood Type", patientMedicalRecordDTO.getBloodType());
        printTableRow("Diagnoses", patientMedicalRecordDTO.getDiagnoses());
        printTableRow("Prescriptions", patientMedicalRecordDTO.getPrescriptions());
        printTableRow("Treatment Plans", patientMedicalRecordDTO.getTreatmentPlans());
        printSeparatorLine();
    }

    /**
     * Prints a block header with a border and formatted text.
     * <p>
     * This method clears the console, prints a border, and displays a header with
     * blocky ASCII characters.
     * It is typically used for displaying large section headers in the console.
     * 
     * @param header The header text to be displayed within the block.
     */
    public static void printBlockHeader(String header) {
        Util.clearConsole();
        String border = "\u003D".repeat(HEADER_WIDTH);
        System.out.println("\n" + border);
        printBlockText(header);
        System.out.println(border + "\n");
        // animateText(" Welcome to the Hospital Management System!\n");
    }

    /**
     * Prints the header text in a blocky ASCII format.
     * <p>
     * This method takes the provided text and converts it into a large blocky ASCII
     * representation
     * using predefined patterns for each character. The output is formatted to fit
     * within the specified width.
     * 
     * @param text The text to be converted into blocky ASCII art.
     */
    private static void printBlockText(String text) {
        // Blocky ASCII characters
        String[] bigFontLines = new String[] {
                "", // Blank line above
                convertToBigFontLine(text, 1),
                convertToBigFontLine(text, 2),
                convertToBigFontLine(text, 3),
                convertToBigFontLine(text, 4),
                convertToBigFontLine(text, 5),
                ""
        };
        for (String line : bigFontLines) {
            System.out.println(centerText(line, HEADER_WIDTH));
        }
    }

    /**
     * Converts a line of text to its corresponding blocky ASCII representation.
     * <p>
     * This method takes a line number (1-5) and returns the corresponding ASCII
     * representation for each character
     * in the text. The result is a formatted line for that specific row in the
     * blocky representation.
     * 
     * @param text The text to be converted.
     * @param line The line number (1 to 5) to convert for the given text.
     * @return A string representing the ASCII block of the given line.
     */
    private static String convertToBigFontLine(String text, int line) {
        StringBuilder builder = new StringBuilder();

        // Add blocky representations for each character
        for (char c : text.toUpperCase().toCharArray()) {
            builder.append(getBlockCharFor(c, line)).append(" "); // Space between characters
        }
        return builder.toString();
    }

    /**
     * Retrieves the ASCII block representation for a specific character at a
     * specific line.
     * <p>
     * This method returns a predefined string representing the ASCII art of a
     * character, based on
     * the given line number (1 to 5) and character (such as 'H', 'M', 'S').
     * It supports a basic set of characters like 'H', 'M', and 'S'.
     * 
     * @param c    The character for which the block representation is needed.
     * @param line The line number (1 to 5) to return the block representation for.
     * @return The ASCII block string for the character and line.
     */
    private static String getBlockCharFor(char c, int line) {
        // Define ASCII art for H, M, S
        return switch (c) {
            case 'H' -> switch (line) {
                case 1 -> "█   █";
                case 2 -> "█   █";
                case 3 -> "█████";
                case 4 -> "█   █";
                case 5 -> "█   █";
                default -> "";
            };
            case 'M' -> switch (line) {
                case 1 -> "█   █";
                case 2 -> "██ ██";
                case 3 -> "█ █ █";
                case 4 -> "█   █";
                case 5 -> "█   █";
                default -> "";
            };
            case 'S' -> switch (line) {
                case 1 -> " ████";
                case 2 -> "█    ";
                case 3 -> " ███ ";
                case 4 -> "    █";
                case 5 -> "████ ";
                default -> "";
            };
            case 'Z' -> switch (line) {
                case 1 -> "   ";
                case 2 -> "   ";
                case 3 -> "   ";
                case 4 -> "   ";
                case 5 -> "   ";
                default -> "";
            };
            default -> "";
        };
    }

    /**
     * Centers the given text within a specified width.
     * <p>
     * This method adds spaces to the left and right of the input text to center it
     * within
     * the given width, ensuring proper alignment when printed.
     * 
     * @param text  The text to be centered.
     * @param width The total width of the output line.
     * @return The centered text with padding.
     */
    private static String centerText(String text, int width) {
        int padding = (width - text.length()) / 2;
        return " ".repeat(padding) + text + " ".repeat(padding);
    }

    /**
     * Prints a list of menu options with numbering.
     * <p>
     * This method displays the provided list of options with numbers in front,
     * making it easy for the user to select an option.
     * 
     * @param options The list of menu options to be printed.
     */
    public static void printMenu(List<String> options) {
        int index = 1;
        for (String option : options) {
            print("(" + index + ")" + option, lang);
            index++;
        }
    }

    /**
     * Prints a header with centered title text.
     * <p>
     * This method prints the title string centered in the console. The width of the
     * title
     * is adjusted to ensure that the text is displayed in the center of the console
     * window.
     * 
     * @param title The title to be printed as the header.
     */
    public static void printHeader(String title) {
        String lang = Config.getLang();
        print("\n" + " ".repeat((TOTAL_WIDTH - title.length()) / 2) + title, lang);
    }

    /**
     * Prints a separator line with the specified width.
     * 
     * This method prints a line composed of a "+" at each end and "-" characters in
     * between,
     * where the number of "-" characters is determined by the specified width. The
     * output
     * is language-sensitive based on the configuration settings.
     *
     * @param width The total width of the separator line, including the "+"
     *              characters at both ends.
     * @see Config
     * @see Util
     */
    public static void printSeparatorLine(int width) {
        String lang = Config.getLang();

        print("+" + "-".repeat(width - 2) + "+", lang);
    }

    // Printer for user personal information
    /**
     * Prints the personal information of a user based on the provided user details.
     * 
     * This method prints detailed information for a patient if the provided object
     * is an instance of {@link PatientDetailsDTO}. It includes the patient's ID,
     * name, gender, date of birth, age, contact information, and blood type. Each
     * piece of information is displayed in a formatted table-like structure.
     * 
     * The output is language-sensitive based on the configuration settings.
     *
     * @param userDetailsDTO An object containing the user's personal information.
     *                       If the object is of type {@link PatientDetailsDTO},
     *                       the patient's details will be printed.
     * @see PatientDetailsDTO
     * @see Config
     * @see Util
     */
    public static void printPersonalInformation(Object userDetailsDTO) {
        printSeparatorLine();
        if (userDetailsDTO instanceof PatientDetailsDTO) {
            PatientDetailsDTO patient = (PatientDetailsDTO) userDetailsDTO; // Casting to PatientDetailsDTO
            System.out.printf("| %-77s |\n", "Patient Id: " + patient.getPatientId());
            printSeparatorLine();
            printTableRow("Name", patient.getName());
            printTableRow("Gender", patient.getGender());
            // Format the date to display only the date part (YYYY-MM-DD)
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = patient.getDateOfBirth().format(dateFormatter);
            printTableRow("Date of Birth", formattedDate);
            printTableRow("Age", patient.getAge());
            printTableRow("Contact Number", patient.getContactNumber());
            printTableRow("Email Address", patient.getEmailAddress());
            printTableRow("Blood Type", patient.getBloodType());
            printSeparatorLine();
        }
    }

    // Printer for a list of users
    /**
     * Prints the details of a list of users based on their specific types.
     * 
     * This method iterates over a list of users, determining the type of each user
     * (e.g., Patient, Doctor, Pharmacist, Admin) and prints their information
     * accordingly.
     * The user type is determined using `instanceof`, and the appropriate print
     * method
     * is called for each type. If a user is of an unknown type, a message is
     * printed.
     * 
     * The output is language-sensitive based on the configuration settings.
     *
     * @param users A list of users, where each user is an instance of a subclass of
     *              {@link User}.
     *              The list can contain different types of users, such as
     *              {@link Patient}, {@link Doctor},
     *              {@link Pharmacist}, and {@link Admin}.
     * @see User
     * @see Patient
     * @see Doctor
     * @see Pharmacist
     * @see Admin
     * @see Config
     * @see Util
     */
    public static void printUsers(List<? extends User> users) {
        if (users.isEmpty())
            return;

        // Iterate through the list and print the objects based on their actual type
        for (User user : users) {
            if (user instanceof Patient) {
                printPatient((Patient) user);
            } else if (user instanceof Doctor) {
                printDoctor((Doctor) user);
            } else if (user instanceof Pharmacist) {
                printPharmacist((Pharmacist) user);
            } else if (user instanceof Admin) {
                printAdmin((Admin) user);
            } else {
                System.out.println("Unknown user type.");
            }
        }
    }

    /**
     * Prints the details of a patient in a formatted table-like structure.
     * 
     * This method prints detailed information about a patient, including their ID,
     * name, gender, date of birth, contact information, email address, and blood
     * type.
     * Each field is printed with proper alignment and separated by lines for
     * readability.
     * 
     * The output is language-sensitive based on the configuration settings.
     *
     * @param patient The {@link Patient} object containing the patient's details to
     *                be printed.
     * @see Patient
     * @see Config
     * @see Util
     */
    private static void printPatient(Patient patient) {
        printSeparatorLine();
        System.out.printf("| %-77s |\n", "Patient Id: " + patient.getId());
        printSeparatorLine();
        printTableRow("Name", patient.getName());
        printTableRow("Gender", patient.getGender());
        printTableRow("Date of Birth", patient.getDateOfBirth());
        printTableRow("Contact Number", patient.getContactNumber());
        printTableRow("Email", patient.getEmailAddress());
        printTableRow("Blood Type", patient.getBloodType());
        printSeparatorLine();
    }

    /**
     * Prints the details of a doctor in a formatted table-like structure.
     * 
     * This method prints detailed information about a doctor, including their ID,
     * name, gender, date of birth, contact information, email address, and a list
     * of
     * patient IDs associated with the doctor. If the doctor has no patients, "No
     * patients"
     * will be printed instead. Each field is displayed with proper alignment and
     * separated
     * by lines for better readability.
     *
     * The output is language-sensitive based on the configuration settings.
     *
     * @param doctor The {@link Doctor} object containing the doctor's details to be
     *               printed.
     * @see Doctor
     * @see Config
     * @see Util
     */
    private static void printDoctor(Doctor doctor) {
        printSeparatorLine();
        System.out.printf("| %-77s |\n", "Doctor Id: " + doctor.getId());
        printSeparatorLine();
        printTableRow("Name", doctor.getName());
        printTableRow("Gender", doctor.getGender());
        printTableRow("Date of Birth", doctor.getDateOfBirth());
        printTableRow("Contact Number", doctor.getContactNumber());
        printTableRow("Email", doctor.getEmailAddress());

        String patients = (doctor.getPatientIds() == null || doctor.getPatientIds().isEmpty()
                || doctor.getPatientIds().equals("null"))
                        ? "No patients"
                        : String.join(", ", doctor.getPatientIds());
        printTableRow("Patients", patients);

        printSeparatorLine();
    }

    /**
     * Prints the details of a pharmacist in a formatted table-like structure.
     * 
     * This method prints detailed information about a pharmacist, including their
     * ID,
     * name, gender, date of birth, contact information, email address, and license
     * number.
     * Each field is displayed with proper alignment and separated by lines for
     * readability.
     * 
     * The output is language-sensitive based on the configuration settings.
     *
     * @param pharmacist The {@link Pharmacist} object containing the pharmacist's
     *                   details to be printed.
     * @see Pharmacist
     * @see Config
     * @see Util
     */
    private static void printPharmacist(Pharmacist pharmacist) {
        printSeparatorLine();
        System.out.printf("| %-77s |\n", "Pharmacist Id: " + pharmacist.getId());
        printSeparatorLine();
        printTableRow("Name", pharmacist.getName());
        printTableRow("Gender", pharmacist.getGender());
        printTableRow("Date of Birth", pharmacist.getDateOfBirth());
        printTableRow("Contact Number", pharmacist.getContactNumber());
        printTableRow("Email", pharmacist.getEmailAddress());
        printTableRow("License Number", pharmacist.getLicenseNum());
        printSeparatorLine();
    }

    /**
     * Prints the details of an admin in a formatted table-like structure.
     * 
     * This method prints detailed information about an admin, including their ID,
     * name, gender, date of birth, contact information, and email address. Each
     * field is printed with proper alignment and separated by lines for
     * readability.
     * 
     * The output is language-sensitive based on the configuration settings.
     *
     * @param admin The {@link Admin} object containing the admin's details to be
     *              printed.
     * @see Admin
     * @see Config
     * @see Util
     */
    private static void printAdmin(Admin admin) {
        printSeparatorLine();
        System.out.printf("| %-77s |\n", "Admin Id: " + admin.getId());
        printSeparatorLine();
        printTableRow("Name", admin.getName());
        printTableRow("Gender", admin.getGender());
        printTableRow("Date of Birth", admin.getDateOfBirth());
        printTableRow("Contact Number", admin.getContactNumber());
        printTableRow("Email", admin.getEmailAddress());
        printSeparatorLine();
    }

    // Printer for appointments
    /**
     * Prints the details of a list of appointments in a formatted table-like
     * structure.
     * 
     * This method iterates over a list of appointments and prints each
     * appointment's details,
     * including the appointment ID, doctor ID, patient ID, date and time, and
     * status. Each
     * field is printed with proper alignment and separated by lines for
     * readability.
     * 
     * The output is language-sensitive based on the configuration settings.
     *
     * @param appointments A list of {@link Appointment} objects containing the
     *                     details of the appointments to be printed.
     * @see Appointment
     * @see Config
     * @see Util
     */
    public static void printAppointments(List<Appointment> appointments) {
        for (Appointment appointment : appointments) {
            printSeparatorLine();
            System.out.printf("| %-77s |\n", "Appointment Id: " + appointment.getAppointmentId());
            printSeparatorLine();
            // Print each field with alignment
            printTableRow("Doctor Id", appointment.getDoctorId());
            printTableRow("Patient Id", appointment.getPatientId());
            printTableRow("Date Time", appointment.getDateTime());
            printTableRow("Status", appointment.getStatus().toString());
        }
        printSeparatorLine();
    }

    // Printer for appointment outcome
    /**
     * Prints the details of an appointment outcome in a formatted table-like
     * structure.
     * 
     * This method prints the details of an appointment outcome, including the
     * appointment ID,
     * service type, medication ID, consultation notes, and prescription status.
     * Each field
     * is printed with proper alignment and separated by lines for readability.
     * 
     * The output is language-sensitive based on the configuration settings.
     *
     * @param outcome The {@link AppointmentOutcome} object containing the details
     *                of the appointment outcome to be printed.
     * @see AppointmentOutcome
     * @see Config
     * @see Util
     */
    public static void printAppointmentOutcome(AppointmentOutcome outcome) {
        printSeparatorLine();
        System.out.printf("| %-77s |\n", "Appointment Id: " + outcome.getAppointmentId());
        printSeparatorLine();
        // Print each field with alignment
        printTableRow("Service Type", outcome.getServiceType());
        printTableRow("Medication Id", outcome.getMedicationId());
        printTableRow("Consultation Notes", outcome.getConsultationNotes());
        printTableRow("Prescription Status", outcome.getStatus().toString());
        printSeparatorLine();
    }

    // Printer for appointment and outcomes
    /**
     * Prints the details of a list of appointments and their outcomes in a
     * formatted table-like structure.
     * 
     * This method iterates over a list of `AppointmentAndOutcomeDTO` objects and
     * prints each appointment's details,
     * including appointment ID, patient ID, doctor ID, status, and date and time.
     * If the appointment status is "COMPLETED",
     * the method also prints the corresponding appointment outcome, including
     * service type, medication IDs, consultation notes,
     * and prescription status. Each field is printed with proper alignment and
     * separated by lines for readability.
     * 
     * The output is language-sensitive based on the configuration settings.
     *
     * @param appts A list of {@link AppointmentAndOutcomeDTO} objects containing
     *              the appointment and outcome details to be printed.
     * @see AppointmentAndOutcomeDTO
     * @see AppointmentOutcomeDTO
     * @see Config
     * @see Util
     */
    public static void printAppointmentAndOutcome(List<AppointmentAndOutcomeDTO> appts) {
        for (AppointmentAndOutcomeDTO appt : appts) {
            printSeparatorLine();
            System.out.printf("| %-77s |\n", "Appointment Id: " + appt.getAppointmentId());
            printSeparatorLine();
            printTableRow("Patient Id", appt.getPatientId());
            printTableRow("Doctor Id", appt.getDoctorId());
            printTableRow("Status", appt.getStatus());
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            String formattedDateTime = appt.getDateTime().format(dateTimeFormatter);
            printTableRow("Date and Time", formattedDateTime);

            // Print appointment outcome only if status is COMPLETED
            if ("COMPLETED".equals(appt.getStatus()) && appt.getAppointmentOutcomeDTO() != null) {
                AppointmentOutcomeDTO outcome = appt.getAppointmentOutcomeDTO();
                printSeparatorLine();
                printTableRow("Service Type", outcome.getServiceType());
                printTableRow("Medication Id(s)", String.join(", ", outcome.getMedicationId()));
                printTableRow("Consultation Notes", outcome.getConsultationNotes());
                printTableRow("Prescription Status", outcome.getStatus());
            }
            printSeparatorLine();
        }
    }

    // Printer for replenishment request
    /**
     * Prints the details of a list of replenish requests in a formatted table-like
     * structure.
     * 
     * This method iterates over a map of `ReplenishRequest` objects and prints each
     * request's details,
     * including the replenish request ID, staff ID, medication ID, status, and
     * requested amount.
     * Each field is printed with proper alignment and separated by lines for
     * readability.
     * 
     * The output is language-sensitive based on the configuration settings.
     *
     * @param requests A {@link HashMap} containing the replenish request details,
     *                 where the key is the request ID and the value is the
     *                 {@link ReplenishRequest} object.
     * @see ReplenishRequest
     * @see Config
     * @see Util
     */
    public static void printReplenishRequest(HashMap<Integer, ReplenishRequest> requests) {
        for (ReplenishRequest request : requests.values()) {
            printSeparatorLine();
            System.out.printf("| %-77s |\n", "Replenishment Request ID: " + request.getRequestId());
            printSeparatorLine();
            printTableRow("Staff ID", request.getStaffId());
            printTableRow("Medication ID", request.getMedicationId());
            printTableRow("Status", request.getStatus().toString());
            printTableRow("Requested Amount", String.valueOf(request.getAmount()));
            printSeparatorLine();
        }
        printSeparatorLine();
    }

    // Printer for medications
    /**
     * Prints the details of a list of medications in a formatted table-like
     * structure.
     * 
     * This method iterates over a map of `Medication` objects and prints each
     * medication's details,
     * including the medication name, initial stock, current stock, low stock level
     * alert, and price.
     * Each field is printed with proper alignment and separated by lines for
     * readability.
     * 
     * The output is language-sensitive based on the configuration settings.
     *
     * @param medications A {@link HashMap} containing medication details, where the
     *                    key is the medication name
     *                    and the value is the {@link Medication} object.
     * @see Medication
     * @see Config
     * @see Util
     */
    public static void printMedications(HashMap<String, Medication> medications) {
        for (Medication medication : medications.values()) {
            printSeparatorLine();
            System.out.printf("| %-77s |\n", "Medication Name: " + medication.getName());
            printSeparatorLine();
            printTableRow("Initial Stock", String.valueOf(medication.getInitialStock()));
            printTableRow("Current Stock", String.valueOf(medication.getCurrentStock()));
            printTableRow("Low Stock Level Alert", String.valueOf(medication.getLowStockLevelAlert()));
            printTableRow("Price", String.format("$%.2f", medication.getPrice()));
            printSeparatorLine();
        }
        printSeparatorLine();
    }

    /**
     * Prints a menu with a header and a list of options in a formatted structure.
     * 
     * This method prints a menu with a centered header surrounded by a border,
     * followed by a list of options.
     * The menu is language-sensitive, as the header and options are translated
     * based on the current language configuration.
     * The user is prompted to enter their choice after the options are displayed.
     * 
     * The header is padded to ensure it is centered within a box of a fixed width,
     * and the options are numbered sequentially.
     *
     * @param header  The title or header to display at the top of the menu.
     * @param options A list of options to display below the header, each one being
     *                a choice for the user.
     * @see Config
     * @see Util
     */
    public static void printMenu(String header, List<String> options) {

        String lang = Config.getLang();

        String translatedHeader = Translate.translate(header, lang);
        List<String> translatedOptions = Translate.translate(options, lang);

        // Adjust the border length based on the fixed width
        String border = "\u003D".repeat(HEADER_WIDTH - 2); // Subtract 2 for the side borders

        // Print the header with a border
        print("", lang); // Prints a blank line
        print("\u2554" + border + "\u2557", lang);

        // Center the header text in the box, padding with spaces
        int padding = (HEADER_WIDTH - 2 - translatedHeader.length()) / 2;
        String paddedHeader = " ".repeat(padding) + translatedHeader + " ".repeat(padding);

        // If the header length is odd, we need to add one more space to the right to
        // balance it
        if (paddedHeader.length() < HEADER_WIDTH - 2) {
            paddedHeader += " ";
        }

        print("\u2551" + paddedHeader + "\u2551", lang);
        print("\u255A" + border + "\u255D", lang);
        print("", lang); // Another blank line

        // Print each option with numbering
        for (int i = 0; i < translatedOptions.size(); i++) {
            print(String.format("(%d) %s", i + 1, translatedOptions.get(i)), lang);
        }

        print("Enter your choice: ", lang);
    }

    // Printer for monthly availability
    /**
     * Prints the availability schedule for a given month, displaying the available
     * times for each date.
     * 
     * This method formats and prints a table where each row represents a day in the
     * month, with the available time slots
     * listed next to the date. It calculates the appropriate column widths based on
     * the longest row to ensure consistent formatting.
     * If no times are available for a given date, the method prints "No available
     * times" in that row.
     * 
     * The output is language-sensitive based on the current language configuration.
     *
     * @param monthlyAvailability A {@link Map} where the key is a {@link LocalDate}
     *                            representing the date,
     *                            and the value is a list of {@link LocalTime}
     *                            objects representing the available time slots for
     *                            that day.
     * @see Config
     * @see Util
     */
    public static void printMonthlyAvailability(Map<LocalDate, List<LocalTime>> monthlyAvailability) {
        String lang = Config.getLang();

        int maxRowWidth = 18; // Initial width for date column and separator

        // Calculate max row width
        for (Map.Entry<LocalDate, List<LocalTime>> entry : monthlyAvailability.entrySet()) {
            int rowWidth = 18 + formatTimes(entry.getValue()).length();
            maxRowWidth = Math.max(maxRowWidth, rowWidth);
        }

        // Header
        print("\n", lang); // New line
        print("-".repeat(maxRowWidth), lang); // Separator line
        print(String.format("%-15s | %-60s", "Date", "Available Slots"), lang); // Header row

        // Generate separator line based on maxRowWidth
        print("-".repeat(maxRowWidth), lang); // Separator line

        for (Map.Entry<LocalDate, List<LocalTime>> entry : monthlyAvailability.entrySet()) {
            LocalDate date = entry.getKey();
            List<LocalTime> times = entry.getValue();

            // Convert time slots to a single string
            String timesString = times.isEmpty() ? "No available times" : formatTimes(times);

            // Print each row of the table
            print(String.format("%-15s | %s", date, timesString), lang); // Data row
        }
        print("-".repeat(maxRowWidth), lang); // Separator line
        print("\n", lang); // New line
    }

    // Helper method to format times as a comma-separated string
    /**
     * Formats a list of {@link LocalTime} objects into a single string, with times
     * separated by commas.
     * 
     * This method takes a list of available time slots and returns a string
     * representation where each time is separated by a comma and a space.
     * If the list is empty, an empty string is returned.
     * 
     * @param times A list of {@link LocalTime} objects representing the available
     *              time slots.
     * @return A string where the times are joined by a comma and a space.
     */
    private static String formatTimes(List<LocalTime> times) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < times.size(); i++) {
            sb.append(times.get(i));
            if (i < times.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    /**
     * Prints the doctor's schedule for the month, including availability and
     * upcoming appointments.
     * 
     * This method takes a map of the doctor's availability for the month and a list
     * of upcoming appointments.
     * It calculates the maximum column widths dynamically based on the content,
     * then prints the schedule in a table format.
     * The table includes columns for the date, availability slots, and any upcoming
     * appointments for each date.
     * If there are no appointments for a given date, the schedule will display
     * "None".
     *
     * @param monthlyAvailability  A map where the key is a {@link LocalDate}
     *                             representing the date,
     *                             and the value is a list of {@link LocalTime}
     *                             representing available time slots on that date.
     * @param upcomingAppointments A list of {@link Appointment} objects
     *                             representing the upcoming appointments for the
     *                             doctor.
     */
    public static void printSchedule(Map<LocalDate, List<LocalTime>> monthlyAvailability,
            List<Appointment> upcomingAppointments) {

        print("\nDoctor's Schedule for the Month:", lang);

        // Variables to track the maximum lengths of each column
        int maxDateLength = "Date".length();
        int maxAvailabilityLength = "Availability".length();
        int maxAppointmentLength = "Upcoming Appointment".length();

        // First pass: Find the maximum string lengths for each column
        for (Map.Entry<LocalDate, List<LocalTime>> entry : monthlyAvailability.entrySet()) {
            LocalDate date = entry.getKey();
            List<LocalTime> availability = entry.getValue();

            // Format availability as a space-separated string
            StringBuilder availabilityStr = new StringBuilder();
            for (LocalTime time : availability) {
                availabilityStr.append(time.toString()).append(" ");
            }

            String availabilityOutput = availabilityStr.toString().trim();
            String appointmentInfo = "None";

            // Find the upcoming appointment info for the current date
            for (Appointment appointment : upcomingAppointments) {
                if (appointment.getDateTime().toLocalDate().equals(date)) {
                    appointmentInfo = appointment.getDateTime().toLocalTime().toString();
                    break;
                }
            }

            // Update the max length for each column
            maxDateLength = Math.max(maxDateLength, date.toString().length());
            maxAvailabilityLength = Math.max(maxAvailabilityLength, availabilityOutput.length());
            maxAppointmentLength = Math.max(maxAppointmentLength, appointmentInfo.length());
        }

        // Print header with dynamic column widths and borders
        print("+" + "=".repeat(maxDateLength + 2) + "+" + "=".repeat(maxAvailabilityLength + 2) + "+"
                + "=".repeat(maxAppointmentLength + 2) + "+", lang);

        print(String.format(
                "| %-" + maxDateLength + "s | %-" + maxAvailabilityLength + "s | %-" + maxAppointmentLength + "s |",
                "Date", "Availability", "Upcoming Appointment"), lang);

        // Print separator line
        print("+" + "=".repeat(maxDateLength + 2) + "+" + "=".repeat(maxAvailabilityLength + 2) + "+"
                + "=".repeat(maxAppointmentLength + 2) + "+", lang);

        // Second pass: Print the table with dynamically adjusted column widths and
        // borders
        for (Map.Entry<LocalDate, List<LocalTime>> entry : monthlyAvailability.entrySet()) {
            LocalDate date = entry.getKey();
            List<LocalTime> availability = entry.getValue();

            // Format availability as a space-separated string
            StringBuilder availabilityStr = new StringBuilder();
            for (LocalTime time : availability) {
                availabilityStr.append(time.toString()).append(" ");
            }

            String availabilityOutput = availabilityStr.toString().trim();
            String appointmentInfo = "None";

            // Find the upcoming appointment info for the current date
            for (Appointment appointment : upcomingAppointments) {
                if (appointment.getDateTime().toLocalDate().equals(date)) {
                    appointmentInfo = appointment.getDateTime().toLocalTime().toString();
                    break;
                }
            }
        
            // Print each row with the dynamically calculated column widths and borders
            print(String.format("| %-" + maxDateLength + "s | %-" + maxAvailabilityLength + "s | %-"
                    + maxAppointmentLength + "s |", date, availabilityOutput, appointmentInfo), lang);
        }

        // Print footer separator line
        print("+" + "=".repeat(maxDateLength + 2) + "+" + "=".repeat(maxAvailabilityLength + 2) + "+"
                + "=".repeat(maxAppointmentLength + 2) + "+", lang);
    }

    /**
     * Prints a table of data based on the provided map and object type.
     * 
     * This method dynamically generates and prints a table based on the type of
     * data in the provided map (`data`).
     * The `objType` parameter determines the headers and getters for the specific
     * type of object contained in the map.
     * For each object in the map, the corresponding getter methods are invoked to
     * retrieve the data, which is then printed
     * in tabular format.
     * 
     * @param data    A map containing the data to be displayed in the table. The
     *                keys are generic and are not used in the table.
     *                The values are the objects for which the getters will be
     *                invoked.
     * @param objType A string representing the type of object in the map (e.g.,
     *                "MedicationDB", "PatientDB", etc.).
     *                This determines the column headers and the corresponding
     *                getter methods to call for each object.
     * 
     * @throws IllegalArgumentException if the `objType` is unsupported.
     */
    public static void printTable(HashMap<?, ?> data, String objType) {
        // Initialize headers and getters
        String[] headers = new String[0];
        String[] getters = new String[0];

        switch (objType) {
            case "MedicationDB":
                headers = new String[] { "Medicine Name", "Initial Stock",
                        "Current Stock", "Low Stock Level Alert", "price ($SGD)" };
                getters = new String[] {
                        "getName", "getInitialStock", "getCurrentStock", "getLowStockLevelAlert", "getPrice"
                };
                break;

            case "PatientDB":
                headers = new String[] {
                        "Patient ID", "Name", "Gender", "Age", "Username", "Date Of Birth", "Contact Number", "Email",
                        "Blood Type"
                };
                getters = new String[] {
                        "getId", "getName", "getGender", "getAge", "getUsername", "getDateOfBirth", "getContactNumber",
                        "getEmailAddress", "getBloodType"
                };
                break;

            case "ReplenishRequestDB":
                headers = new String[] {
                        "Request ID", "Staff ID", "Medication Name", "Status", "Request Amount" };
                getters = new String[] {
                        "getRequestId", "getStaffId", "getMedicationId", "getStatus", "getAmount"
                };
                break;

            case "Doctor":
                headers = new String[] {
                        "Staff ID", "Name", "Gender", "Age", "Date Of Birth", "Contact Number", "Email"
                };
                getters = new String[] {
                        "getId", "getName", "getGender", "getAge", "getDateOfBirth", "getContactNumber",
                        "getEmailAddress"
                };
                break;
            case "Pharmacist":
                headers = new String[] {
                        "Staff ID", "Name", "Gender", "Age", "Date Of Birth", "Contact Number", "Email",
                        "License Number"
                };
                getters = new String[] {
                        "getId", "getName", "getGender", "getAge", "getDateOfBirth", "getContactNumber",
                        "getEmailAddress", "getLicenseNum"
                };
                break;

            case "Admin":
                headers = new String[] {
                        "Staff ID", "Name", "Gender", "Age", "Date Of Birth", "Contact Number", "Email"
                };
                getters = new String[] {
                        "getId", "getName", "getGender", "getAge", "getDateOfBirth", "getContactNumber",
                        "getEmailAddress"
                };
                break;

            case "GeneralStaff":
                headers = new String[] {
                        "Staff ID", "Name", "Gender", "Age", "Date Of Birth", "Contact Number", "Email"
                };
                getters = new String[] {
                        "getId", "getName", "getGender", "getAge", "getDateOfBirth", "getContactNumber",
                        "getEmailAddress"
                };
                break;

            case "AppointmentsDB":
                headers = new String[] { "AppointmentId", "DoctorId", "PatientId", "DateTime", "Status" };
                getters = new String[] {
                        "getAppointmentId", "getDoctorId", "getPatientId", "getDateTime", "getStatus"
                };
                break;

            case "ReplenishmentRequest":
                headers = new String[] { "Request Id", "Staff Id", "Medication Id", "Status", "Request Amount",
                        "Date" };
                getters = new String[] {
                        "getRequestId", "getStaffId", "getMedicationId", "getStatus", "getAmount",
                        "getRequestDateAsString"
                };
                break;

            // Add other cases as needed
            default:
                print("Unsupported object type.", lang);
                return; // Exit if the object type is unsupported
        }

        // Check if the map is empty
        if (data.isEmpty()) {
            print("No data to display.", lang);
            return;
        }

        // Prepare data for printing
        List<Object[]> rows = new ArrayList<>();
        for (Map.Entry<?, ?> entry : data.entrySet()) {
            Object value = entry.getValue();
            Object[] row = new Object[headers.length];
            int index = 0;

            // Retrieve properties using getters dynamically
            if (value != null) {
                Class<?> clazz = value.getClass();
                for (String getter : getters) {
                    try {
                        Method method = clazz.getMethod(getter);
                        row[index++] = method.invoke(value);
                    } catch (NoSuchMethodException e) {
                        print("No such method: " + getter, lang);
                        row[index++] = "N/A"; // If no getter exists
                    } catch (Exception e) {
                        print("Error retrieving data for getter: " + getter, lang);
                        row[index++] = "Error"; // Handle other exceptions
                    }
                }
            }
            rows.add(row);
        }

        // Calculate column widths
        int[] widths = new int[headers.length];
        for (int i = 0; i < headers.length; i++) {
            widths[i] = headers[i].length();
        }

        for (Object[] row : rows) {
            for (int i = 0; i < row.length; i++) {
                if (row[i] != null) {
                    widths[i] = Math.max(widths[i], row[i].toString().length());
                }
            }
        }

        // Print the table
        printBorder(widths);
        printRow(headers, widths);
        printBorder(widths);
        for (Object[] row : rows) {
            printRow(row, widths);
            printBorder(widths);
        }
    }

    /**
     * Prints a single row of the table with the provided data and column widths.
     * 
     * This method formats the row data based on the specified column widths and
     * prints it in a tabular format.
     * Each value in the row is aligned according to its corresponding column width.
     * If a value is null, it is
     * displayed as the string "null".
     *
     * @param row    An array of objects representing the values in the row. Each
     *               object will be formatted and printed in its
     *               corresponding column.
     * @param widths An array of integers representing the width of each column. The
     *               column width determines the amount of
     *               space each value will occupy when printed.
     */
    private static void printRow(Object[] row, int[] widths) {
        print("|", lang);
        for (int i = 0; i < row.length; i++) {
            String formattedValue = (row[i] != null) ? row[i].toString() : "null";
            System.out.printf(" %-" + widths[i] + "s |", formattedValue);
        }
        System.out.println();

    }

    /**
     * Prints a border for the table based on the specified column widths.
     * 
     * This method prints a line that serves as a border for the table, where the
     * length of each segment in the
     * border corresponds to the width of the columns. The border is designed with
     * "+" at the intersections
     * and "-" filling the spaces between them.
     * 
     * @param widths An array of integers representing the width of each column. The
     *               length of each section of the border
     *               is determined by the respective column width, with additional
     *               space for the column separators.
     */
    private static void printBorder(int[] widths) {
        System.out.print("+");
        for (int width : widths) {
            System.out.print("-".repeat(width + 2) + "+");
        }
        System.out.println();
    }

}
