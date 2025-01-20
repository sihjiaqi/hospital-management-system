package controller.Users;

import java.util.List;

import model.Users.Doctor;
import repository.Users.StaffDB;
import model.Users.User;

/**
 * The DoctorController class is responsible for managing the operations related to Doctor users.
 * It extends the StaffController class and provides specific methods for adding, updating, and 
 * viewing Doctor users in the system.
 *
 * <p>This class interacts with the StaffDB to perform CRUD operations for Doctor users, including:
 * adding new Doctors, updating their personal information, and retrieving all Doctor users.</p>
 * 
 */
public class DoctorController extends StaffController<Doctor> {
    /**
     * Adds a new doctor to the staff database.
     * <p>
     * This method calls the {@link StaffDB#addUser(Doctor, boolean)} method to add the specified doctor to the staff database.
     * If the `init` parameter is true, the doctor is initialized with default values, otherwise, the doctor is added as is.
     * </p>
     *
     * @param doctor The doctor to be added to the staff database. It must be a valid Doctor object.
     * @param init A boolean flag indicating whether to initialize the doctor's data. If true, the doctor is initialized with default values.
     * @return A string message indicating the result of the addition, such as a success or failure message.
     */
    public String add(Doctor doctor, boolean init) {
        return StaffDB.addUser(doctor,  init);
    }

    /**
     * Updates the personal information of a staff member in the system.
     * The method allows for updating the staff member's details with a new ID and user information.
     *
     * @param staffId The current ID of the staff member whose information is to be updated.
     * @param user The User object containing the new details for the staff member.
     * @param newId The new ID to be assigned to the staff member.
     * @return A String indicating the result of the update operation, such as success or error message.
     */
    @Override
    public String updatePersonalInfo(String staffId, User user, String newId) {
        return StaffDB.updateUserInfo(staffId, newId, user);
    }

    // View doctor details
    /**
     * Retrieves a list of all Doctor users from the system.
     * This method fetches the users based on their role, which in this case is 'Doctor'.
     *
     * @return A List of Doctor objects representing all the doctors in the system.
     */
    @Override
    public List<Doctor> viewAll() {
        return StaffDB.getUsersByRole(Doctor.class);
    }
}
