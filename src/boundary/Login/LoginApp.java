package boundary.Login;

import boundary.Applications.AdminApp;
import boundary.Applications.DoctorApp;
import boundary.Applications.PatientApp;
import boundary.Applications.PharmacistApp;

import model.Users.Admin;
import model.Users.Doctor;
import model.Users.Patient;
import model.Users.Pharmacist;
import model.Users.User;
import utils.Utility.Printer;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * The LoginApp class handles the user login process for a healthcare application. 
 * It provides a menu for users to select their role (Patient, Doctor, Pharmacist, Administrator), 
 * prompts for a username and password, and authenticates the user based on the selected role. 
 * After successful authentication, the appropriate application (Patient, Doctor, Pharmacist, or Admin) is launched.
 * 
 * The login process involves:
 * <ul>
 *     <li>Displaying a menu with role options (Patient, Doctor, Pharmacist, Administrator, Return to Main Menu).</li>
 *     <li>Prompting the user to enter their username and password.</li>
 *     <li>Authenticating the credentials and ensuring they match the selected role.</li>
 *     <li>Launching the appropriate user application upon successful login.</li>
 *     <li>Allowing the user to return to the main menu if they choose the "Return to Main Menu" option.</li>
 * </ul>
 * 
 * The authentication process checks the username and password for different user types and verifies
 * the user's role before granting access to the respective application.
 * 
 * <p>Example:</p>
 * <pre>
 *     LoginApp.start();
 * </pre>
 * 
 */
public class LoginApp {
    private static Scanner scanner = new Scanner(System.in);
    private static String lang = "en";

    /**
     * This method handles the user login process for different types of users 
     * (Patient, Doctor, Pharmacist, Administrator) and routes them to the respective 
     * application interface upon successful login. It presents a menu to the user 
     * to choose a role, prompts for username and password, and verifies the user's 
     * credentials. Depending on the user's choice, the appropriate app instance 
     * is initialized.
     *
     * <p> The method follows the following logic:
     * <ul>
     *     <li>If the user selects "Patient", it authenticates with patient credentials and starts the Patient application.</li>
     *     <li>If the user selects "Doctor", it authenticates with doctor credentials and starts the Doctor application.</li>
     *     <li>If the user selects "Pharmacist", it authenticates with pharmacist credentials and starts the Pharmacist application.</li>
     *     <li>If the user selects "Administrator", it authenticates with administrator credentials and starts the Admin application.</li>
     *     <li>If the user selects "Return to Main Menu", the method terminates and returns to the previous menu.</li>
     * </ul>
     *
     * @throws IllegalArgumentException if an invalid menu option is selected.
     */
    public static void start() {
        // LocalDate dob = LocalDate.of(1990, 5, 15);
        // Patient patient = new Patient("P1", "john", "male", "john", "doe", dob, "123-456-7890", "O+");
        
        String menuHeader = "Log In Menu";
        List<String> menuOptions = Arrays.asList(
            "Patient", 
            "Doctor", 
            "Pharmacist", 
            "Administrator",
            "Return to Main Menu"
        );            
        Printer.printMenu(menuHeader, menuOptions);

        int choice = getUserChoice();

        Printer.print("Enter username: ", lang);
        String username = scanner.next();
        Printer.print("Enter password: ", lang);
        String password = scanner.next();
        switch (choice) {
            case 1: // Patient
                User userPatient = Auth.patientLogin(username, password);
                Patient patient = (Patient) userPatient;
                if (patient != null && patient.getUsername().equals(username) && patient.getPassword().equals(password)) {
                    Printer.print("Welcome Patient " + username + "!", lang);
                    PatientApp patientApp = PatientApp.getInstance();
                    patientApp.initializeApp(patient);
                }
                break;
            case 2: // Doctor
                User userDoctor = Auth.staffLogin(username, password);
                Doctor doctor = (Doctor) userDoctor;
                if (doctor != null && doctor.getUsername().equals(username) && doctor.getPassword().equals(password)) {
                    Printer.print("Welcome Doctor " + username + "!", lang);
                    DoctorApp doctorApp = DoctorApp.getInstance();
                    doctorApp.initializeApp(doctor);
                }
                break;
            case 3: // Pharmacist
                User userPharmacist = Auth.staffLogin(username, password);
                Pharmacist pharmacist = (Pharmacist) userPharmacist;
                if (pharmacist != null && pharmacist.getUsername().equals(username) && pharmacist.getPassword().equals(password)) {
                    Printer.print("Welcome Pharmacist " + username + "!", lang);
                    PharmacistApp pharmacistApp = PharmacistApp.getInstance();
                    pharmacistApp.initializeApp(pharmacist);
                }
                break;
            case 4: // Admin
                User userAdmin = Auth.staffLogin(username, password);
                Admin admin = (Admin) userAdmin;
                if (admin != null && admin.getUsername().equals(username) && admin.getPassword().equals(password)) {
                    Printer.print("Welcome Admin " + username + "!", lang);
                    AdminApp adminApp = AdminApp.getInstance();
                    adminApp.initializeApp(admin);
                }
                break;
            case 5:
                return;
            default: Printer.print("Invalid option, please try again", lang);
            }
    }

    /**
     * Prompts the user to input a valid number and returns the user's choice as an integer.
     * This method repeatedly requests input until a valid integer is entered. 
     * If the user enters a non-integer value, an error message is displayed and the input is requested again.
     *
     * @return The valid integer input from the user.
     * @throws InputMismatchException If the user enters a non-integer input, it catches the exception and prompts again.
     */
    private static int getUserChoice() {
        while (true) {
            try {
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
            }
        }
    }
}
