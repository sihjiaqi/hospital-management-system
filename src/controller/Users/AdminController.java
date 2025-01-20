package controller.Users;

import java.util.List;

import model.Users.Admin;
import model.Users.User;
import repository.Users.StaffDB;

/**
 * The AdminController class is responsible for managing the operations related to Admin users.
 * It extends the StaffController class and provides specific methods for adding, updating, and 
 * viewing Admin users in the system.
 *
 * <p>This class interacts with the StaffDB to perform CRUD operations for Admin users, including:
 * adding new Admins, updating their personal information, and retrieving all Admin users.</p>
 */
public class AdminController extends StaffController<Admin> {
    /**
     * Adds a new admin to the system by storing the provided admin details in the database.
     *
     * @param admin The Admin object containing the details of the admin to be added.
     * @return A String indicating the result of the operation. This could be a success message or an error message.
     */
    public String add(Admin admin, boolean init) {
        return StaffDB.addUser(admin,  init);
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

    // View admin details
    /**
     * Retrieves a list of all Admin users from the system.
     * This method fetches the users based on their role, which in this case is 'Admin'.
     *
     * @return A List of Admin objects representing all the admins in the system.
     */
    @Override
    public List<Admin> viewAll() {
        return StaffDB.getUsersByRole(Admin.class);
    }
}