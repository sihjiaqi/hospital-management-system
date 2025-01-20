package repository.Medication;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import model.Medication.Medication;
import utils.Utility.CSVWriter;
import utils.Utility.Printer;
import utils.Utility.SimilarityCalculator;

/**
 * The MedicationDB class provides an in-memory database for managing
 * medications
 * and their details, including stock, prices, and alerts. It allows adding,
 * retrieving, updating, and deleting medication records, and persists the data
 * to a CSV file.
 */
public class MedicationDB {
    /**
     * A HashMap that stores medications. Each medication is associated with a
     * unique medication name.
     * The medication contains details such as stock levels, price, and alerts for
     * low stock.
     * This map is used for efficient lookup and management of medications by their
     * name.
     */
    private static HashMap<String, Medication> medications = new HashMap<>();

    /**
     * The path to the CSV file that stores the medication list.
     * This file contains details such as medication name, initial stock, low stock
     * alert level, current stock, and price.
     */
    public static String path = "Hospital/src/resources/CSV/Medicine_List.csv";

    /**
     * The headers of the CSV file that contains the medication list.
     * These headers define the structure of each record in the CSV file.
     */
    public static String[] headers = {
            "Medicine Name",
            "Initial Stock",
            "Low Stock Level Alert",
            "Current Stock",
            "Price"
    };

    // Add medication to the HashMap
    /**
     * Adds a medication to the database and optionally persists the data to the CSV
     * file.
     *
     * @param medication The medication to add.
     * @param init       If true, the operation does not write to the CSV file.
     * @return A message indicating the result of the operation.
     */
    public static String addMedication(Medication medication, boolean init) {
        try {
            medications.put(medication.getName(), medication);

            if (!init) {
                CSVWriter.writeCSV(path, medications, headers);
            }
            return "Medication added successfully: " + medication.getName();
        } catch (Exception e) {
            return "Error adding medication: " + e.getMessage();
        }
    }

    /**
     * Retrieves all medications in the database.
     *
     * @return A HashMap containing all medications.
     */
    public static HashMap<String, Medication> getAllMedications() {
        return medications;
    }

    // Delete medication by ID
    /**
     * Deletes a medication by its ID and updates the CSV file.
     *
     * @param medicationId The ID of the medication to delete.
     * @return A message indicating the result of the operation.
     */
    public static String deleteMedication(String medicationId) {
        try {
            if (medications.containsKey(medicationId)) {
                medications.remove(medicationId);
                CSVWriter.writeCSV(path, medications, headers);
                return "Medication deleted successfully for ID: " + medicationId;
            } else {
                return "Medication not found with ID: " + medicationId;
            }
        } catch (Exception e) {
            return "Error deleting medication: " + e.getMessage();
        }
    }

    public static String updateMedication(String medicationId, Medication updatedMedication) {
        try {
            // Check if the medication exists in the database
            if (medications.containsKey(medicationId)) {
                // Fetch the current medication details
                Medication existingMedication = medications.get(medicationId);

                existingMedication.setLowStockLevelAlert(updatedMedication.getLowStockLevelAlert());

                // Save the updated medications list to the CSV file
                CSVWriter.writeCSV(path, medications, headers);

                // Return success message
                return "Medication updated successfully for ID: " + medicationId;
            } else {
                // Return error message if medication ID is not found
                return "Medication not found with ID: " + medicationId;
            }
        } catch (Exception e) {
            // Return error message if an exception occurs during the process
            return "Error updating medication: " + e.getMessage();
        }
    }

    /**
     * Finds a medication by its name.
     *
     * @param name The name of the medication to find.
     * @return The medication object if found, otherwise null.
     */
    public static Medication findMedicationByName(String name) {
        return medications.get(name);
    }

    // Method to find medication prices by a list of medication IDs
    /**
     * Finds the prices of medications by a list of their IDs.
     *
     * @param medicationIds A list of medication IDs.
     * @return A list of prices for the specified medications.
     */
    public static List<Double> findMedicationPricesByIds(List<String> medicationIds) {
        List<Double> prices = new ArrayList<>();

        for (String id : medicationIds) {
            Medication medication = medications.get(id);
            if (medication != null) {
                prices.add(medication.getPrice());
            }
        }
        return prices;
    }

    // Increase medication stock by 1
    /**
     * Increases the stock of a medication by 1.
     *
     * @param medicationId The ID of the medication to update.
     * @return A message indicating the result of the operation.
     */
    public static String addMedicationStock(String medicationId, int stockCount) {
        try {
            // Retrieve the medication object from the medications map
            Medication medication = medications.get(medicationId);

            // Check if the medication exists
            if (medication != null) {
                // Increase the stock by the provided stock count
                medication.setCurrentStock(medication.getCurrentStock() + stockCount);

                // Update the medication record in the map
                medications.put(medicationId, medication);

                // Write the updated data to the CSV file
                CSVWriter.writeCSV(path, medications, headers);

                return "Stock increased by " + stockCount + " for Medication ID: " + medicationId;
            } else {
                return "Medication not found with ID: " + medicationId;
            }
        } catch (Exception e) {
            return "Error increasing medication stock: " + e.getMessage();
        }
    }

    // Decrease medication stock by 1
    /**
     * Decreases the stock of a medication by 1.
     *
     * @param medicationId The ID of the medication to update.
     * @return A message indicating the result of the operation.
     */
    public static String removeMedicationStock(String medicationId, int stockCount) {
        try {
            // Retrieve the medication from the map
            Medication medication = medications.get(medicationId);
    
            // Check if the medication exists
            if (medication != null) {
                // Ensure the stock count is not greater than the current stock
                if (medication.getCurrentStock() >= stockCount) {
                    // Decrease the stock by the specified count
                    medication.setCurrentStock(medication.getCurrentStock() - stockCount);
                    medications.put(medicationId, medication);
    
                    // Update the CSV file with the updated stock
                    CSVWriter.writeCSV(path, medications, headers);
    
                    return "Stock decreased for Medication ID: " + medicationId + " by " + stockCount
                            + " units. New stock: " + medication.getCurrentStock();
                } else {
                    return "Cannot decrease stock. Medication ID: " + medicationId + " does not have enough stock."
                            + " Current stock: " + medication.getCurrentStock();
                }
            } else {
                return "Medication not found with ID: " + medicationId;
            }
        } catch (Exception e) {
            return "Error decreasing medication stock: " + e.getMessage();
        }
    }
    

    // Update stock level for a specific medication
    /**
     * Updates the stock level of a specific medication.
     *
     * @param medicationId The ID of the medication to update.
     * @param stockLevel   The new stock level.
     * @return A message indicating the result of the operation.
     */
    public static String updateStockLevel(String medicationId, int stockLevel) {
        try {
            Medication medication = medications.get(medicationId);
            if (medication != null) {
                medication.setCurrentStock(stockLevel);
                medications.put(medicationId, medication);
                CSVWriter.writeCSV(path, medications, headers);
                return "Stock level updated for Medication ID: " + medicationId + " to " + stockLevel;
            } else {
                return "Medication not found with ID: " + medicationId;
            }
        } catch (Exception e) {
            return "Error updating stock level: " + e.getMessage();
        }
    }



    // Update low stock alert
    /**
     * Updates the low stock level alert value for a medication.
     *
     * @param medicationId The ID of the medication to update.
     * @param newValue     The new low stock alert value.
     * @return A message indicating the result of the operation.
     */
    public static String updateLowStockLevelAlert(String medicationId, int newValue) {
        try {

            Medication medication = findMedicationByName(medicationId);

            if (medication != null) {
                medication.setLowStockLevelAlert(newValue);
                medications.put(medicationId, medication);
                CSVWriter.writeCSV(path, medications, headers);
                return "Low stock level alert updated for Medication ID: " + medicationId + " to " + newValue;
            } else {
                return "Medication not found with ID: " + medicationId;
            }
        } catch (Exception e) {
            return "Error updating low stock level alert: " + e.getMessage();
        }
    }

    /**
     * Displays all medications with similar names based on a similarity threshold.
     *
     * @param name The name to compare against existing medication names.
     */
    public static void displayAllSimilarMedications(String name) {
        HashMap<String, Medication> similarMed = SimilarityCalculator.findSimilarObjects(name, medications, "getName",
                0.65);

        if (similarMed.isEmpty()) {
            System.out.println("These are the similar medications found.");
            // Printer.printTable(staffDB, "GeneralStaff");
        } else {
            Printer.printTable(similarMed, "MedicationDB");
        }
    }
}
