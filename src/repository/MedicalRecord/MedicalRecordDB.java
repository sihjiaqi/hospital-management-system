package repository.MedicalRecord;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.MedicalRecord.MedicalRecord;
import utils.Utility.CSVWriter;

/**
 * The {@code MedicalRecordDB} class is responsible for managing medical records
 * in memory and persisting them to a CSV file. It provides methods to add,
 * update, retrieve, and display medical records.
 * <p>
 * This class uses a {@link HashMap} to store the medical records, keyed by
 * patient IDs, ensuring efficient lookups and updates.
 * </p>
 */
public class MedicalRecordDB {
    /**
     * A HashMap that stores medical records of patients. Each medical record is associated with a unique patient ID.
     * The medical record contains details such as diagnosis, prescriptions, and treatment plans.
     * This map is used for efficient lookup and management of records by patient ID.
     */
    private static HashMap<String, MedicalRecord> medicalRecords = new HashMap<>();

    // private static ArrayList<MedicalRecord> medicalRecords = new ArrayList<>();

    /**
     * The path to the CSV file that stores the medical records.
     * This file contains details such as patient ID, diagnosis, prescriptions, and treatment plans.
     */
    public static String path = "Hospital/src/resources/CSV/Medical_Records.csv";

    /**
     * The headers of the CSV file that contains the medical records.
     * These headers define the structure of each record in the CSV file.
     */
    public static String[] headers = {
            "PatientId",
            "Diagnosis",
            "Prescriptions",
            "Treatment Plans"
    };

    // Add a medical record
    /**
     * Adds a new medical record to the database and optionally writes it to a CSV file.
     *
     * @param medicalRecord the medical record to be added
     * @param init          {@code true} if the method is called during initialization
     *                      (bypasses CSV writing), {@code false} otherwise
     * @return a success message if the record was added, or an error message if an exception occurred
     */
    public static String addMedicalRecord(MedicalRecord medicalRecord, boolean init) {
        try {
            // Add the medical record to the map
            medicalRecords.put(medicalRecord.getPatientId(), medicalRecord);

            // Write to CSV if not initializing
            if (!init) {
                CSVWriter.writeCSV(path, medicalRecords, headers);
            }
            return "Medical record added successfully for patient ID: " + medicalRecord.getPatientId();
        } catch (Exception e) {
            return "Error adding medical record: " + e.getMessage();
        }
    }

    /**
     * Retrieves a medical record by the patient ID.
     *
     * @param patientId the unique identifier of the patient
     * @return the {@link MedicalRecord} object if found, or {@code null} if not
     */
    public static MedicalRecord getMedicalRecordByPatientId(String patientId) {
        return medicalRecords.get(patientId);
    }

    // Update a medical record
    /**
     * Updates the medical record of a specific patient by adding new diagnosis, prescription, or treatment plans.
     *
     * @param patientId     the unique identifier of the patient
     * @param diagnosis     a list of new diagnoses to add (can be {@code null})
     * @param prescription  a list of new prescriptions to add (can be {@code null})
     * @param treatmentPlan a list of new treatment plans to add (can be {@code null})
     * @return a success message if the update was successful, or an error message if an exception occurred or the record was not found
     */
    public static String updateMedicalRecord(String patientId, List<String> diagnosis, List<String> prescription, List<String> treatmentPlan) {
        try {
            // Retrieve the medical record
            MedicalRecord recordToUpdate = getMedicalRecordByPatientId(patientId);

            // Check if the record exists
            if (recordToUpdate != null) {
                if (diagnosis != null) {
                    recordToUpdate.addDiagnosis(diagnosis);
                }
                if (prescription != null) {
                    recordToUpdate.addPrescription(prescription);
                }
                if (treatmentPlan != null) {
                    recordToUpdate.addTreatmentPlan(treatmentPlan);
                }
                medicalRecords.put(patientId, recordToUpdate);
                CSVWriter.writeCSV(path, medicalRecords, headers);

                return "Medical record updated successfully for patient ID: " + patientId;
            } else {
                return "Error: No medical record found for patient ID: " + patientId;
            }
        } catch (Exception e) {
            return "Error updating medical record for patient ID " + patientId + ": " + e.getMessage();
        }
    }
    public static String updatePatientIdInMedicalRecord(String oldPatientId, String newPatientId) {
        try {
            // Retrieve the medical record for the old patient ID
            MedicalRecord recordToUpdate = getMedicalRecordByPatientId(oldPatientId);
    
            // Check if the medical record exists for the old patient ID
            if (recordToUpdate != null) {
                // Check if the new patient ID is valid and not already in use
                if (newPatientId != null && !newPatientId.isEmpty() && !newPatientId.equals(oldPatientId)) {
                    // Ensure the new patient ID is not already present in the database
                    if (medicalRecords.containsKey(newPatientId)) {
                        return "Error: The new patient ID (" + newPatientId + ") already exists. Please choose another one.";
                    }
    
                    // Update the patient ID in the medical record
                    recordToUpdate.setPatientId(newPatientId);
    
                    // Remove the old record and add the updated record with the new ID
                    medicalRecords.remove(oldPatientId);
                    medicalRecords.put(newPatientId, recordToUpdate);
    
                    // Update the CSV
                    CSVWriter.writeCSV(path, medicalRecords, headers);
    
                    return "Patient ID updated successfully in the medical record for patient ID: " + oldPatientId;
                } else {
                    return "Error: Invalid new patient ID or same as the old ID.";
                }
            } else {
                return "Error: No medical record found for patient ID: " + oldPatientId;
            }
        } catch (Exception e) {
            return "Error updating medical record for patient ID " + oldPatientId + ": " + e.getMessage();
        }
    }
    


    /**
     * Prints all the medical records stored in the database to the console.
     * <p>
     * Each record displays:</p>
     * <ul>
     *   <li>Patient ID</li>
     *   <li>Diagnoses</li>
     *   <li>Prescriptions</li>
     *   <li>Treatment Plans</li>
     * </ul>
     * 
     */
    public static void printAllMedicalRecords() {
        for (Map.Entry<String, MedicalRecord> entry : medicalRecords.entrySet()) {
            MedicalRecord record = entry.getValue(); // Get the MedicalRecord object
            System.out.println("Patient ID: " + record.getPatientId());
            System.out.println("Diagnoses: " + String.join(", ", record.getDiagnoses()));
            System.out.println("Prescriptions: " + String.join(", ", record.getPrescriptions())); // Use prescriptions list here
            System.out.println("Treatment Plans: " + String.join(", ", record.getTreatmentPlans()));
            System.out.println("----------");
        }
    }
}
