package controller.Medication;

import java.util.HashMap;

import model.Medication.Medication;
import repository.Medication.MedicationDB;

/**
 * The {@code MedicationController} class provides methods to manage
 * medication-related operations
 * in the application, such as viewing, adding, updating, and deleting
 * medications and their stock levels.
 * It interacts with the {@link MedicationDB} class to perform database
 * operations.
 * 
 * <p>
 * The following actions are available:
 * </p>
 * <ul>
 * <li>View all medications or a specific medication by ID.</li>
 * <li>Add a new medication or increase/decrease its stock.</li>
 * <li>Update stock levels and low stock level alerts for medications.</li>
 * <li>Delete medications from the database.</li>
 * </ul>
 * 
 * <p>
 * This controller is responsible for managing the flow of medication data,
 * ensuring that the business
 * logic is applied correctly, and passing data to and from the
 * {@link MedicationDB} for persistence.
 * </p>
 */
public class MedicationController {
    // ----------------------------------------------------------------
    // Inventory Management
    // ----------------------------------------------------------------
    // View inventory of medication
    /**
     * Retrieves a map of all medications from the database.
     * 
     * @return A {@link HashMap} containing the medication names as the key and the
     *         corresponding
     *         {@link Medication} objects as the value. The map is sourced from the
     *         {@link MedicationDB#getAllMedications()} method.
     */
    public HashMap<String, Medication> viewMedications() {
        return MedicationDB.getAllMedications();
    }

    // View medication by medication Id
    /**
     * Retrieves a medication from the database by its ID.
     * 
     * @param medicationId The unique identifier of the medication to retrieve.
     * @return The {@link Medication} object corresponding to the specified
     *         medication ID,
     *         or {@code null} if no medication is found with the given ID.
     *         This is sourced from the
     *         {@link MedicationDB#findMedicationByName(String)} method.
     */
    public Medication viewMedicationById(String medicationId) {
        return MedicationDB.findMedicationByName(medicationId);
    }

    // Add new medicine
    /**
     * Adds a medication to the database based on the specified conditions.
     * 
     * @param medication The {@link Medication} object to be added to the database.
     * @param add        A boolean flag indicating whether to add the medication.
     *                   If {@code true}, the medication will be added; if
     *                   {@code false}, the operation is skipped.
     */
    public void addMedication(Medication medication, Boolean add) {
        MedicationDB.addMedication(medication, add);
    }

    // Add medication stock level
    /**
     * Increases the stock of a medication in the database.
     * 
     * @param medicationId The unique identifier of the medication whose stock is to
     *                     be increased.
     *                     This medication's stock will be updated by the
     *                     {@link MedicationDB#addMedicationStock(String)} method.
     */
    public void increaseMedicationStock(String medicationId, int stockCount) {
        MedicationDB.addMedicationStock(medicationId,stockCount);
    }

    // Reduce medication stock level
    /**
     * Decreases the stock of a medication in the database.
     * 
     * @param medicationId The unique identifier of the medication whose stock is to
     *                     be decreased.
     *                     This medication's stock will be updated by the
     *                     {@link MedicationDB#removeMedicationStock(String)}
     *                     method.
     */
    public void decreaseMedicationStock(String medicationId,int stockCount) {
        MedicationDB.removeMedicationStock(medicationId,  stockCount);
    }

  
    // Update stock level
    /**
     * Updates the stock level of a medication in the database.
     * 
     * @param medicationId The unique identifier of the medication whose stock level
     *                     is to be updated.
     * @param stockLevel   The new stock level to set for the specified medication.
     *                     The stock level is updated using the
     *                     {@link MedicationDB#updateStockLevel(String, int)}
     *                     method.
     */
    public void updateStockLevel(String medicationId, int stockLevel) {
        MedicationDB.updateStockLevel(medicationId, stockLevel);
    }

    // Update low stock level alert line of each medicine
    /**
     * Updates the low stock level alert threshold for a specific medication in the
     * database.
     * 
     * @param medicationId The unique identifier of the medication for which the low
     *                     stock level alert threshold is being updated.
     * @param newValue     The new value to set as the low stock alert threshold for
     *                     the specified medication.
     *                     This value is updated using the
     *                     {@link MedicationDB#updateLowStockLevelAlert(String, int)}
     *                     method.
     */
    public void updateLowStockLevelAlert(String medicationId, int newValue) {
        MedicationDB.updateLowStockLevelAlert(medicationId, newValue);
    }

    // Delete a medicine
    /**
     * Deletes a medication from the database.
     * 
     * @param medicationId The unique identifier of the medication to be deleted.
     *                     This medication will be removed using the
     *                     {@link MedicationDB#deleteMedication(String)} method.
     */
    public void deleteMedication(String medicationId) {
        MedicationDB.deleteMedication(medicationId);
    }
}
