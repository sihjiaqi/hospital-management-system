package repository.Medication;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.Medication.ReplenishRequest;
import utils.Utility.CSVWriter;

/**
 * This class manages replenish requests in a hospital medication system.
 * It provides functionality for adding, updating, and retrieving replenish
 * requests
 * and interacting with a CSV file to persist the data.
 */
public class ReplenishRequestDB {
    /**
     * A static HashMap to store the replenish requests.
     * 
     * This map holds the replenish requests, using the request ID as the key and
     * the corresponding
     * `ReplenishRequest` object as the value. It allows efficient retrieval and
     * management of replenish requests.
     */
    private static HashMap<Integer, ReplenishRequest> replenishRequests = new HashMap<>();

    /**
     * Path to the CSV file that stores the replenishment requests.
     * 
     * This file contains the data for replenishment requests, including information
     * such as the request ID,
     * pharmacist ID, medicine ID, status, quantity, and the date of the request.
     */
    public static String path = "Hospital/src/resources/CSV/Replenishment_Request.csv";

    /**
     * The headers for the CSV file representing replenish requests.
     * 
     * These are the column headers in the CSV file and correspond to the attributes
     * of a replenishment request.
     * The columns are: "Request Id", "PharmacistId", "MedicineId", "Status",
     * "Quantity", and "Date".
     */
    public static String[] headers = {
            "Request Id",
            "PharmacistId",
            "MedicineId",
            "Status",
            "Quantity",
            "Date"
    };

    // Add a new replenish request to the HashMap
    /**
     * Adds a new replenish request to the database and optionally writes it to a
     * CSV file.
     *
     * @param replenishRequest The replenish request to add.
     * @param init             Whether the operation is part of initialization
     *                         (avoiding CSV writes).
     * @return A message indicating success or failure of the operation.
     */
    public static String addReplenishRequest(ReplenishRequest replenishRequest, boolean init) {
        try {
            // Add the replenish request to the map
            replenishRequests.put(replenishRequest.getRequestId(), replenishRequest);

            // Write to CSV if not initializing
            if (!init) {
                CSVWriter.writeCSV(path, replenishRequests, headers);
            }
            return "Replenish request added successfully for Request ID: " + replenishRequest.getRequestId();
        } catch (Exception e) {
            return "Error adding replenish request: " + e.getMessage();
        }
    }

    // Update the status of a replenish request
    /**
     * Updates the status of a specified replenish request.
     *
     * @param requestId The ID of the replenish request to update.
     * @param newStatus The new status to set for the replenish request.
     * @param init      Whether the operation is part of initialization (avoiding
     *                  CSV writes).
     * @return A message indicating success or failure of the operation.
     */
    public static String updateReplenishRequestStatus(int requestId, String newStatus, boolean init) {
        try {
            // Get the replenish request from the map
            ReplenishRequest replenishRequest = replenishRequests.get(requestId);

            if (replenishRequest != null) {
                // Validate and update the status
                try {
                    replenishRequest
                            .setStatus(ReplenishRequest.ReplenishRequestStatus.valueOf(newStatus.toUpperCase()));
                } catch (IllegalArgumentException e) {
                    return "Invalid status: " + newStatus + ". Please provide a valid status.";
                }

                // Write changes to the map
                replenishRequests.put(requestId, replenishRequest);

                // Write to CSV if not initializing
                if (!init) {
                    CSVWriter.writeCSV(path, replenishRequests, headers);
                }
                return "Replenish request status updated successfully for Request ID: " + requestId + " to "
                        + newStatus;
            } else {
                return "Replenish request with Request ID: " + requestId + " not found.";
            }
        } catch (Exception e) {
            return "Error updating replenish request status: " + e.getMessage();
        }
    }

    public static String updatePharmacistIdInReplenishRequest(String oldPharmacistId, String newPharmacistId) {
        try {
            // Check if the new pharmacist ID is valid
            if (newPharmacistId != null && !newPharmacistId.trim().isEmpty()) {
                // Iterate over all replenish requests
                boolean found = false;
                for (ReplenishRequest replenishRequest : replenishRequests.values()) {
                    // Check if the pharmacist ID matches the old one
                    if (replenishRequest.getStaffId().equals(oldPharmacistId)) {
                        // Update the pharmacist ID
                        replenishRequest.setStaffId(newPharmacistId);
                        found = true;
                        // After updating, write the changes to the map
                        replenishRequests.put(replenishRequest.getRequestId(), replenishRequest);
                    }
                }
    
                if (found) {
                    // Write to CSV
                    CSVWriter.writeCSV(path, replenishRequests, headers);
                    return "Pharmacist ID updated successfully from " + oldPharmacistId + " to " + newPharmacistId;
                } else {
                    return "No replenish request found with the specified old pharmacist ID: " + oldPharmacistId;
                }
            } else {
                return "Error: Invalid new pharmacist ID.";
            }
        } catch (Exception e) {
            return "Error updating pharmacist ID: " + e.getMessage();
        }
    }
    
    
    // View all replenish requests
    /**
     * Retrieves a list of all replenish requests in the database.
     *
     * @return A list of all replenish requests.
     */
    public static List<ReplenishRequest> viewAllReplenishRequests() {
        return new ArrayList<>(replenishRequests.values());
    }

    // View all pending replenish requests
    /**
     * Retrieves all active (pending) replenish requests from the database.
     *
     * @return A HashMap of active replenish requests with their request IDs as
     *         keys.
     */
    public static HashMap<Integer, ReplenishRequest> getAllActiveRequests() {
        HashMap<Integer, ReplenishRequest> repReq = new HashMap<>();

        for (Map.Entry<Integer, ReplenishRequest> entry : replenishRequests.entrySet()) {
            if (entry.getValue().getStatus() == ReplenishRequest.ReplenishRequestStatus.PENDING) {
                repReq.put(entry.getKey(), entry.getValue());
            }
        }

        return repReq;
    }
}
