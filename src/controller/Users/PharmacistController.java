package controller.Users;

import java.util.List;

import model.Users.Pharmacist;
import repository.Users.StaffDB;
import model.Users.User;

/**
 * The PharmacistController class is responsible for managing the operations
 * related to Pharmacist users.
 * It extends the StaffController class and provides specific methods for
 * adding, updating, and
 * viewing Pharmacist users in the system.
 *
 * <p>
 * This class interacts with the StaffDB to perform CRUD operations for
 * Pharmacist users, including:
 * adding new Pharmacists, updating their personal information, and retrieving
 * all Pharmacist users.
 * </p>
 */
public class PharmacistController extends StaffController<Pharmacist> {
    /**
     * Adds a new pharmacist to the system by storing the provided pharmacist
     * details in the database.
     *
     * @param pharmacist The Pharmacist object containing the details of the
     *                   pharmacist to be added.
     * @return A String indicating the result of the operation. This could be a
     *         success message or an error message.
     */
    public String add(Pharmacist pharmacist, boolean init) {
        return StaffDB.addUser(pharmacist, init);
    }

    /**
     * Updates the personal information of a staff member in the system.
     * The method allows for updating the staff member's details with a new ID and
     * user information.
     *
     * @param staffId The current ID of the staff member whose information is to be
     *                updated.
     * @param user    The User object containing the new details for the staff
     *                member.
     * @param newId   The new ID to be assigned to the staff member.
     * @return A String indicating the result of the update operation, such as
     *         success or error message.
     */
    @Override
    public String updatePersonalInfo(String staffId, User user, String newId) {
        return StaffDB.updateUserInfo(staffId, newId, user);
    }

    // View pharmacist details
    /**
     * Retrieves a list of all Pharmacist users from the system.
     * This method fetches the users based on their role, which in this case is
     * 'Pharmacist'.
     *
     * @return A List of Pharmacist objects representing all the pharmacists in the
     *         system.
     */
    @Override
    public List<Pharmacist> viewAll() {
        return StaffDB.getUsersByRole(Pharmacist.class);
    }
}
