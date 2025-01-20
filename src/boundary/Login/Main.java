// package boundary.Login;

// import java.util.HashMap;
// import java.util.InputMismatchException;
// import java.util.Scanner;

// public class Main {
//     private static HashMap<String, User> users = new HashMap<>();
//     private static Scanner scanner = new Scanner(System.in);
//     private static boolean isRunning = true;

//     public static void main(String[] args) {
//         // Sample users
//         users.put("admin", new User("admin", "password123", "Staff"));
//         users.put("user", new User("user", "password456", "Patient"));

//         while (isRunning) {
//             displayLoginMenu();
//         }
//         scanner.close();
//     }

//     private static void displayLoginMenu() {
//         System.out.println("\n=== Login Menu ===");
//         System.out.println("1. Login");
//         System.out.println("2. Exit");
//         System.out.print("Choose an option: ");

//         int choice = getUserChoice();

//         switch (choice) {
//             case 1:
//                 chooseUserType();
//                 break;
//             case 2:
//                 System.out.println("Exiting the program...");
//                 isRunning = false;
//                 break;
//             default:
//                 System.out.println("Invalid option. Please try again.");
//         }
//     }

//     private static void chooseUserType() {
//         System.out.println("Are you logging in as: ");
//         System.out.println("1. Patient");
//         System.out.println("2. Staff");
//         System.out.println("3. Exit");
//         System.out.print("Choose an option: ");
        
//         int choice = getUserChoice();
//         String userType = null;

//         switch (choice) {
//             case 1:
//                 userType = "Patient";
//                 break;
//             case 2:
//                 userType = "Staff";
//                 break;
//             case 3:
//                 return;
//             default:
//                 System.out.println("Invalid option. Please try again");
//         }

//         User loggedInUser = login(userType);
//         if (loggedInUser != null) {
//             userDashboard(loggedInUser);
//         }
//     }

//     private static User login(String userType) {
//         System.out.print("Enter username: ");
//         String username = scanner.next();
//         System.out.print("Enter password: ");
//         String password = scanner.next();
            
//         if (users.containsKey(username)) {
//             User user = users.get(username);
//             if (user.getPassword().equals(password) && user.getUserType().equals(userType)) {
//                 System.out.println("Login successful! Welcome, " + username + "!");
//                 return new User(username, password, userType); // Create a User object on successful login
//             } else {
//                 System.out.println("Invalid credentials. Please try again.");
//             }
//         } else {
//             System.out.println("Username not found. Please register.");
//         }
//         return null;
//     }

//     private static void userDashboard(User user) {
//         while (true) {
//             System.out.println("\n=== User Dashboard ===");
//             System.out.println("1. View Profile");
//             System.out.println("2. Logout");
//             System.out.print("Choose an option: ");

//             int choice = getUserChoice();

//             switch (choice) {
//                 case 1:
//                     System.out.println("User Profile: " + user.getUsername());
//                     break;
//                 case 2:
//                     System.out.println("Logging out...");
//                     return; // Return to the login menu
//                 default:
//                     System.out.println("Invalid option. Please try again.");
//             }
//         }
//     }

//     private static int getUserChoice() {
//         while (true) {
//             try {
//                 return scanner.nextInt();
//             } catch (InputMismatchException e) {
//                 System.out.println("Invalid input. Please enter a number.");
//                 scanner.nextLine(); // Clear the invalid input
//             }
//         }
//     }
// }

// class User {
//     private String username;
//     private String password;
//     private String userType;

//     public User(String username, String password, String userType) {
//         this.username = username;
//         this.userType = userType;
//         this.password = password;
//     }

//     public String getUsername() {
//         return username;
//     }

//     public String getPassword() {
//         return password;
//     }

//     public String getUserType() {
//         return userType;
//     }

// }
