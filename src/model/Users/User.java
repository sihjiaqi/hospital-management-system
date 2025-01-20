package model.Users;

import java.time.LocalDate;
import repository.Users.PatientDB;
import repository.Users.StaffDB;
import utils.Utility.CSVWriter;
import utils.Utility.IdGenerator;

/**
 * Represents a general user in the system, containing common properties such as 
 * user details (name, ID, username, etc.), and methods for login, user management, 
 * and updating user details. This class serves as the base class for various user 
 * types such as Patient, Doctor, Admin, and Pharmacist.
 */
public class User {
    
    private String username;
    private String password;
    private String id;
    private LocalDate dateOfBirth;
    private String gender;
    private String contactNumber;
    private String emailAddress;
    private String name;
    private Integer age;

    /**
     * Default constructor for creating a new User object.
     */
    public User() {
    };

    /**
     * Constructs a User object with the specified details.
     * This constructor will generate a user ID based on the user type.
     * 
     * @param id            The unique ID of the user (e.g., DOC for doctors, PHM for pharmacists).
     * @param name          The full name of the user.
     * @param gender        The gender of the user.
     * @param username      The username for the user's account.
     * @param password      The password for the user's account.
     * @param dateOfBirth   The date of birth of the user.
     * @param contactNumber The contact number of the user.
     * @param emailAddress  The email address of the user.
     */
    public User(String id, // this use case is for reading frm db
            String name,
            String gender,
            String username,
            String password,
            LocalDate dateOfBirth,
            String contactNumber,
            String emailAddress) {

        switch (id) {
            case "DOC":
                this.id = IdGenerator.generateId("DOC");
                break;
            case "PHM":
                this.id = IdGenerator.generateId("PHM");
                break;
            case "ADM":
                this.id = IdGenerator.generateId("ADM");
                break;
            case "p":
                this.id = IdGenerator.generateId("p");
                break;
            default:
                this.id = id;
        }

        this.name = name;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;

        LocalDate currentDate = LocalDate.now();
        Integer age;
        if (dateOfBirth == null || currentDate == null) {
            age = null;
        } else {
            // Calculate the age based on the difference in years
            age = currentDate.getYear() - dateOfBirth.getYear();

            // Check if the birthday has occurred yet this year
            if (currentDate.getDayOfYear() < dateOfBirth.getDayOfYear()) {
                age--; // Subtract one year if the birthday hasn't occurred yet this year
            }
        }

        this.age = age;
        this.username = username;
        this.password = password;
        this.contactNumber = contactNumber;
        this.emailAddress = emailAddress;
    }

    /**
     * Gets the user ID.
     * 
     * @return The unique ID of the user.
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the name of the user.
     * 
     * @return The full name of the user.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the gender of the user.
     * 
     * @return The gender of the user.
     */
    public String getGender() {
        return gender;
    }

    /**
     * Gets the age of the user.
     * 
     * @return The age of the user.
     */
    public Integer getAge() {
        return age;
    }

    /**
     * Gets the username of the user.
     * 
     * @return The username of the user.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the password of the user.
     * 
     * @return The password of the user.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets the date of birth of the user.
     * 
     * @return The date of birth of the user.
     */
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Gets the contact number of the user.
     * 
     * @return The contact number of the user.
     */
    public String getContactNumber() {
        return contactNumber;
    }

    /**
     * Gets the email address of the user.
     * 
     * @return The email address of the user.
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Sets the name of the user.
     * 
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the gender of the user.
     * 
     * @param gender The gender to set.
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Sets the age of the user.
     * 
     * @param age The age to set.
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Sets the username of the user.
     * 
     * @param username The username to set.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Sets the password of the user.
     * 
     * @param password The password to set.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Sets the date of birth of the user.
     * 
     * @param dateOfBirth The date of birth to set.
     */
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Sets the unique ID of the user.
     * 
     * @param id The ID to set.
     */
    public void setID(String id) {
        this.id = id;
    }

    /**
     * Sets the contact number of the user.
     * 
     * @param contactNumber The contact number to set.
     */
    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    /**
     * Sets the email address of the user.
     * 
     * @param emailAddress The email address to set.
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**
     * Updates the user details in the relevant database or file.
     * If the user is a {@link Patient}, it updates the patient records in the CSV file.
     * If the user is a {@link Doctor}, {@link Admin}, or {@link Pharmacist}, it updates the staff records.
     */
    public void updateUser() {

        // Detect if this object is of type Patient or Staff
        if (this instanceof Patient) {
            CSVWriter.writeCSV(PatientDB.path, PatientDB.getPatients(), PatientDB.headers);

        } else if (this instanceof Doctor || this instanceof Admin || this instanceof Pharmacist) {
            // Perform logic for Doctor, Admin, or Pharmacist
            CSVWriter.writeCSV("Hospital/Hospital/Hospital/src/resources/CSV/Staff_List.csv", StaffDB.getStaff(), StaffDB.headers);

        }

    }

    /**
     * Attempts to log in the user by checking if the provided password matches the stored password.
     * 
     * @param password The password to compare against the stored password.
     * @return True if the passwords match, false otherwise.
     */
    public boolean login(String password) {

        if (password.equals(this.password)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns a string representation of the user object, including all personal details.
     * Password is not included for security reasons.
     * 
     * @return A string representation of the user object.
     */
    @Override
    public String toString() {
        return "User{" +
                "userID='" + id + '\'' +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", age='" + age + '\'' +
                ", username='" + username + '\'' +
                // Not printing password for security reasons
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                '}';
    }
}
