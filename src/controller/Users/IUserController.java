package controller.Users;

import model.Users.User;

/**
 * Interface representing the operations for managing user information.
 *
 * @param <T> The type of user, which must extend the {@link User} class.
 */
public interface IUserController<T extends User> {
    // Method to retrieve a user by their ID
    /**
     * Retrieves a user by their unique identifier.
     *
     * @param userId The unique identifier of the user to be retrieved.
     * @return The user associated with the provided ID, or null if no such user exists.
     * @throws IllegalArgumentException If the userId is null or invalid.
     */
    T getUserById(String userId);

    // Update user personal details
    /**
     * Updates the personal information of a user based on the provided user ID.
     *
     * @param userId The unique identifier of the user whose information is to be updated.
     * @param updatedUserv The updated user object containing the new information.
     * @param newId The new unique identifier to be assigned to the user, if applicable.
     * @return A string message indicating the success or failure of the update operation.
     * @throws IllegalArgumentException If the provided userId or newId is null or invalid.
     */
    String updatePersonalInfo(String userId, T updatedUserv, String newId);
}