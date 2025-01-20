package controller.Users;

import java.util.List;

import model.Users.User;
import repository.Users.StaffDB;

/**
 * Controller class for managing staff-related operations.
 *
 * This class implements the {@link IUserController} interface and provides various methods 
 * for interacting with staff data, such as viewing all users, retrieving a user by ID, 
 * updating personal information, and filtering staff based on various criteria.
 *
 * The class works with a generic type {@code T} that extends the {@link User} class, 
 * allowing for flexibility in handling different types of users.
 *
 * @param <T> the type of user managed by this controller, which must be a subclass of {@link User}
 */
public class StaffController<T extends User> implements IUserController<User> {
    // View all staff
    /**
     * Retrieves a list of all users from the StaffDB.
     *
     * This method calls the `getAllUsers` method from the `StaffDB` class to fetch a list of users.
     * It returns a list of type {@code T} containing all user data stored in the database.
     *
     * @return a {@link List} containing all users from the database
     */
    public List<T> viewAll() {
        return StaffDB.getAllUsers();
    }

    // View staff by staff Id
    /**
     * Retrieves a user by their unique identifier.
     *
     * This method calls the `getUserById` method from the `StaffDB` class to fetch a specific user 
     * based on the provided {@code userId}.
     *
     * @param userId the unique identifier of the user to retrieve
     * @return the {@link User} object corresponding to the given {@code userId}, or {@code null} if no user is found
     */
    public User getUserById(String userId) {
        return StaffDB.getUserById(userId);
    }

    // Update staff details
    /**
     * Updates the personal information of a staff member.
     *
     * This method calls the `updateUserInfo` method from the `StaffDB` class to update the personal 
     * information of the staff member identified by the given {@code staffId}. The method also allows 
     * updating the staff's ID with a new {@code newstaffId}.
     *
     * @param staffId the current ID of the staff member to update
     * @param staff the {@link User} object containing the updated personal information of the staff member
     * @param newstaffId the new ID to assign to the staff member (can be the same as {@code staffId} if no change is needed)
     * @return a {@link String} message indicating the result of the update operation
     */
    public String updatePersonalInfo(String staffId, User staff, String newstaffId) {
        return StaffDB.updateUserInfo(staffId, newstaffId, staff);
    }

    // Remove staff
    /**
     * Removes a staff member from the system.
     *
     * This method calls the `removeUser` method from the `StaffDB` class to remove the staff member 
     * identified by the provided {@code staffId} from the system.
     *
     * @param staffId the unique identifier of the staff member to remove
     * @return a {@link String} message indicating the result of the removal operation
     */
    public String removeStaff(String staffId) {
        return StaffDB.removeUser(staffId);
    }

    // Display a list of staff filtered by role, gender, age etc
    /**
     * Displays the list of staff members that match the specified filter criteria.
     *
     * This method filters the staff members based on the provided {@code role}, {@code gender}, and 
     * {@code ageRange} parameters. If a parameter is {@code null}, it will not be used as a filter.
     * The staff members that match all non-null filter criteria are printed to the console.
     *
     * @param role the role of the staff member to filter by (can be {@code null} to ignore this filter)
     * @param gender the gender of the staff member to filter by (can be {@code null} to ignore this filter)
     * @param ageRange the age range of the staff member to filter by (can be {@code null} to ignore this filter)
     */
    public void displayFilteredStaff(String role, String gender, String ageRange) {
        List<User> allStaff = StaffDB.getAllUsers();
        for (User staff : allStaff) {
            boolean matchRole = role == null || staff.getClass().getSimpleName().equals(role);
            boolean matchGender = gender == null || staff.getGender().equalsIgnoreCase(gender);
            boolean matchAge = ageRange == null || isInAgeRange(staff.getAge(), ageRange);

            if (matchRole && matchGender && matchAge) {
                System.out.println(staff);
            }
        }
    }

    // Helper method to check if staff falls within the given age range (eg.
    // "20-30")
    /**
     * Checks if the staff member's age falls within the specified age range.
     *
     * This method splits the provided {@code ageRange} string into a lower and upper bound, 
     * then compares the {@code staffAge} to determine if it lies within the specified range.
     *
     * @param staffAge the age of the staff member to check
     * @param ageRange a string representing the age range in the format "lower-upper" (e.g., "30-40")
     * @return {@code true} if the staff member's age is within the specified range, {@code false} otherwise
     */
    private boolean isInAgeRange(int staffAge, String ageRange) {
        String[] range = ageRange.split("-");
        int lower = Integer.parseInt(range[0]);
        int upper = Integer.parseInt(range[1]);
        int age = staffAge;
        return age >= lower && age <= upper;
    }
}
