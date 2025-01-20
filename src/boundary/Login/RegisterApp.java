// package boundary.Login;

// import model.Users.Patient;
// import model.Users.User;
// import utils.Utility.Printer;
// import utils.Utility.Input;
// import utils.Utility.Validation;
// import controller.Users.PatientController;

// import java.time.LocalDate;
// import java.util.Scanner;

// public class RegisterApp {
//     private static Scanner scanner;
//     private static String lang = "en";
    
//     public static User start() {
//         scanner = new Scanner(System.in);
//         scanner.nextLine();

//         Printer.print("Enter patient details:", lang);
//         String name = Input.inputName(lang, false);

//         Printer.print("Gender (Male/Female): ", lang);
//         String gender = Input.inputGender(lang, false);

//         Printer.print("Username: ", lang);
//         String username = Input.inputUsername(lang, name, "patient", false);
        
//         Printer.print("Password: ", lang);
//         String password = Input.inputPassword(lang, false);

//         Printer.print("Date of Birth: ", lang);
//         LocalDate dateOfBirth = Input.inputDOB(lang, false);

//         Printer.print("Contact Number: ", lang);
//         String contactNumber = Input.inputContactNumber(lang, false);

//         Printer.print("Email: ", lang);
//         String email = Input.inputEmailAddress(lang, false, name, "patient");

//         String bloodType;
//         while (true) {
//             Printer.print("Blood Type (e.g., A+, O-, etc.): ", lang);
//             bloodType = scanner.nextLine();
//             if (!Validation.validateBloodType(bloodType)) {
//                 System.out.println(
//                         "Invalid input. Blood Type must be one of the following: A+, A-, B+, B-, O+, O-, AB+, AB-.");
//             } else {
//                 break;
//             }
//         }

//         String patientId = "P" + username;
//         Patient newPatient = new Patient(patientId, name, gender, username, password, dateOfBirth, contactNumber, email,
//                 bloodType);
//         PatientController.add(newPatient);

//         return newPatient;
//     }
// }
