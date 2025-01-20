package controller.Medication;

import java.util.HashMap;

import model.Medication.ReplenishRequest;
import repository.Medication.ReplenishRequestDB;

/**
 * Controller class for managing replenishment requests.
 *
 * The `ReplenishRequestController` class acts as an intermediary between the user interface
 * and the database. It provides methods for adding, viewing, and updating replenishment requests.
 * The class interacts with the `ReplenishRequestDB` class to perform operations on replenishment 
 * request data, such as adding new requests, retrieving active requests, and updating request statuses.
 */
public class ReplenishRequestController {
    // Add replenishment request
    /**
     * Adds a replenishment request to the database.
     *
     * This method interacts with the `ReplenishRequestDB` class to add a replenishment request 
     * based on the provided `ReplenishRequest` object and the specified flag.
     *
     * @param replenishRequest the replenishment request to be added
     * @param add a boolean flag indicating whether the request should be added (true) or not (false)
     * @return a string message indicating the result of the operation, such as success or failure
     */
    public String addReplenishRequest(ReplenishRequest replenishRequest, Boolean add) {
        return ReplenishRequestDB.addReplenishRequest(replenishRequest, add);
    }

    // View all pending replenish requests
    /**
     * Retrieves all active replenishment requests from the database.
     *
     * This method interacts with the `ReplenishRequestDB` class to fetch all currently active
     * replenishment requests. The requests are returned in a `HashMap` where the key is the 
     * request's unique identifier and the value is the `ReplenishRequest` object.
     *
     * @return a `HashMap` containing all active replenishment requests, where the key is the 
     *         request's unique ID and the value is the corresponding `ReplenishRequest` object.
     */
    public HashMap<Integer, ReplenishRequest> viewAllActiveRequests() {
        return ReplenishRequestDB.getAllActiveRequests();
    }

    // Update replenishment request from pharmacists
    /**
     * Updates the status of a replenishment request in the database.
     *
     * This method updates the status of an existing replenishment request identified by the 
     * given `requestId`. It interacts with the `ReplenishRequestDB` class to modify the status 
     * of the request. The method also passes a flag to indicate whether the update should 
     * trigger additional behavior (e.g., notifications or logging), with the default value set 
     * to `false`.
     *
     * @param requestId the unique identifier of the replenishment request to be updated
     * @param status the new status to be set for the replenishment request
     * @return a `String` message indicating the result of the operation, such as success or failure
     */
    public String updateReplenishmentRequest(int requestId, String status) {
        return ReplenishRequestDB.updateReplenishRequestStatus(requestId, status,false);
    }
}