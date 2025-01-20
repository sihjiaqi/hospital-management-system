package repository.Users;

import java.util.List;

import model.Users.User;

/**
 * Abstract class providing static methods for managing User objects in a database-like structure.
 * This class is designed for operations such as adding, removing, retrieving, and updating users.
 *
 * @param <T> the type of User that this database manages
 */
public abstract class UserDB<T extends User> {

    /**
     * Adds a new user to the database.
     *
     * @param user the user object to be added
     * @param <T>  the type of the user
     * @return a message indicating the result of the operation
     */
    public static <T extends User> String addUser(T user) {
        // Abstract methods can't be static, so you can't call them directly.
        // Static methods operate on static data or static context.
        return null;
    }

    /**
     * Removes a user from the database using their unique ID.
     *
     * @param userId the unique ID of the user to be removed
     * @param <T>    the type of the user
     * @return a message indicating the result of the operation
     */
    public static <T extends User> String removeUser(String userId) {
        return null;
    }

    /**
     * Retrieves a user by their unique ID.
     *
     * @param userId the unique ID of the user
     * @param <T>    the type of the user
     * @return the user object with the specified ID, or null if not found
     */
    public static <T extends User> T getUserById(String userId) {
        // Implement logic to get a user by ID
        return null;
    }

    /**
     * Retrieves a user by their username.
     *
     * @param username the username of the user
     * @param <T>      the type of the user
     * @return the user object with the specified username, or null if not found
     */
    public static <T extends User> T getUserByUsername(String username) {
        // Implement logic to get a user by username
        return null; 
    }

    /**
     * Retrieves a user by their contact number.
     *
     * @param contactNum the contact number of the user
     * @param <T>      the type of the user
     * @return the user object with the specified contact number, or null if not found
     */
    public static <T extends User> T getUserByContactNum(String contactNum) {
        // Implement logic to get a user by username
        return null; 
    }

    /**
     * Updates a user's information in the database.
     *
     * @param oldId       the current ID of the user to be updated
     * @param newId       the new ID for the user
     * @param updatedUser the updated user object
     * @param <T>         the type of the user
     * @return a message indicating the result of the operation
     */
    public static <T extends User> String updateUserInfo(String oldId, String newId, T updatedUser) {
        // Implement logic to update user info statically
        return null;
    }

    /**
     * Retrieves a list of all users in the database.
     *
     * @param <T> the type of the user
     * @return a list of all user objects
     */
    public static <T extends User> List<T> getAllUsers() {
        // Implement logic to get all users
        return null;
    }

    /**
     * Prints all user objects in the database.
     *
     * @param <T> the type of the user
     */
    public static <T extends User> void printAllUsers() {
        // Implement logic to print all users
    }
}
