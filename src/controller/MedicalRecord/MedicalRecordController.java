package controller.MedicalRecord;

import java.util.ArrayList;
import java.util.List;

import DTO.PatientMedicalRecordDTO;
import model.MedicalRecord.MedicalRecord;
import model.Users.Doctor;
import model.Users.Patient;
import repository.MedicalRecord.MedicalRecordDB;
import repository.Users.PatientDB;

/**
 * Controller class responsible for managing and accessing medical records for patients.
 *
 * This class provides methods for retrieving and updating medical records. It allows fetching medical records 
 * for a specific patient or a list of records associated with a particular doctor. It also supports updating 
 * a patient's medical record with new diagnoses, prescriptions, and treatment plans. 
 * The class interacts with the database to retrieve or modify medical records and return the results in the form 
 * of data transfer objects (DTOs) or raw records.
 *
 * Key methods include:
 * <ul>
 *   <li>{@link #viewMedicalRecords(Doctor)}: Retrieves medical records for all patients associated with a doctor.</li>
 *   <li>{@link #viewMedicalRecordByPatientId(String)}: Retrieves a specific patient's medical record along with their personal details.</li>
 *   <li>{@link #viewMedicalRecord(String)}: Retrieves a medical record by patient ID.</li>
 *   <li>{@link #updateMedicalRecord(String, List, List, List)}: Updates a patient's medical record with new diagnoses, prescriptions, and treatment plans.</li>
 * </ul>
 *
 * This controller ensures that medical data can be accessed and modified securely and efficiently, facilitating
 * the interaction between the front-end and the medical record database.
 */
public class MedicalRecordController {
    // ----------------------------------------------------------------
    // Medical Record Management
    // ----------------------------------------------------------------
    // Doctors can view the medical records of patients under their care
    /**
     * Retrieves the medical records of patients associated with the given doctor.
     *
     * This method checks if the doctor has any associated patient IDs. If the doctor does
     * not have any patient IDs or if no medical records are found for those IDs, it will return null.
     * Otherwise, it returns a list of medical records for the associated patients.
     *
     * @param doctor The doctor whose associated patient's medical records are to be retrieved.
     * @return A list of {@link PatientMedicalRecordDTO} objects for the doctor’s patients, or null if no records are found.
     */
    public List<PatientMedicalRecordDTO> viewMedicalRecords(Doctor doctor) {
        List<PatientMedicalRecordDTO> medicalRecords = new ArrayList<>();
    
        // Check if the doctor has associated patient IDs
        if (doctor.getPatientIds() == null || doctor.getPatientIds().isEmpty()) {
            return null;
        }
        
        // Iterate through the patient IDs and retrieve their medical records
        for (String patientId : doctor.getPatientIds()) {
            MedicalRecord medicalRecord = MedicalRecordDB.getMedicalRecordByPatientId(patientId);

            // if (medicalRecord != null) {
            //     medicalRecords.add(medicalRecord);
            // }
            if (medicalRecord == null) {
                return null;
            }

            Patient patient = PatientDB.getUserById(patientId);
            PatientMedicalRecordDTO patientMedicalRecordDTO = new PatientMedicalRecordDTO();
            patientMedicalRecordDTO.setPatientId(patient.getId());
            patientMedicalRecordDTO.setName(patient.getName());
            patientMedicalRecordDTO.setGender(patient.getGender());
            patientMedicalRecordDTO.setDateOfBirth(patient.getDateOfBirth());
            patientMedicalRecordDTO.setContactNumber(patient.getContactNumber());
            patientMedicalRecordDTO.setEmailAddress(patient.getEmailAddress());
            patientMedicalRecordDTO.setBloodType(patient.getBloodType());
            patientMedicalRecordDTO.setDiagnosis(medicalRecord.getDiagnoses());
            patientMedicalRecordDTO.setPrescription(medicalRecord.getPrescriptions());
            patientMedicalRecordDTO.setTreatmentPlan(medicalRecord.getTreatmentPlans());
            medicalRecords.add(patientMedicalRecordDTO);
        }   
    
        // Return the list of medical records or null if none are found
        return medicalRecords.isEmpty() ? null : medicalRecords;
    }    

    // Add-on: Doctors can view the medical records of a patient under their care
    /**
     * Retrieves the medical record and associated details of a patient by their patient ID.
     *
     * This method fetches the patient's medical record from the database using the provided patient ID. If a valid
     * medical record is found, it constructs a {@link PatientMedicalRecordDTO} object containing the patient's
     * personal details (e.g., name, contact information) and medical information (e.g., diagnosis, prescriptions, treatment plans).
     * If no medical record is found for the given patient ID, it returns null.
     *
     * @param patientId The unique ID of the patient whose medical record is to be retrieved.
     * @return A {@link PatientMedicalRecordDTO} object containing the patient's details and medical record, 
     *         or null if no record is found for the given patient ID.
     */
    public PatientMedicalRecordDTO viewMedicalRecordByPatientId(String patientId) {
        MedicalRecord medicalRecord = MedicalRecordDB.getMedicalRecordByPatientId(patientId);

        if (medicalRecord == null) {
            return null;
        }
        
        Patient patient = PatientDB.getUserById(patientId);
        PatientMedicalRecordDTO patientMedicalRecordDTO = new PatientMedicalRecordDTO();
        patientMedicalRecordDTO.setPatientId(patient.getId());
        patientMedicalRecordDTO.setName(patient.getName());
        patientMedicalRecordDTO.setGender(patient.getGender());
        patientMedicalRecordDTO.setDateOfBirth(patient.getDateOfBirth());
        patientMedicalRecordDTO.setContactNumber(patient.getContactNumber());
        patientMedicalRecordDTO.setEmailAddress(patient.getEmailAddress());
        patientMedicalRecordDTO.setBloodType(patient.getBloodType());
        patientMedicalRecordDTO.setDiagnosis(medicalRecord.getDiagnoses());
        patientMedicalRecordDTO.setPrescription(medicalRecord.getPrescriptions());
        patientMedicalRecordDTO.setTreatmentPlan(medicalRecord.getTreatmentPlans());

        return patientMedicalRecordDTO;
    }

    /**
     * Retrieves the medical record of a patient by their patient ID.
     *
     * This method fetches the patient's medical record from the database using the provided patient ID. 
     * If no medical record is found for the given patient ID, it returns null.
     *
     * @param patientId The unique ID of the patient whose medical record is to be retrieved.
     * @return The {@link MedicalRecord} object containing the patient's medical details, 
     *         or null if no medical record is found for the given patient ID.
     */
    public MedicalRecord viewMedicalRecord(String patientId) {
        return MedicalRecordDB.getMedicalRecordByPatientId(patientId);
    }

    // Doctors can update the medical records of patients by adding new diagnoses,
    // presriptions and treatment plans
    /**
     * Updates the medical record of a patient with new diagnoses, prescriptions, and treatment plans.
     *
     * This method updates the medical record for a given patient using the provided patient ID. It takes in the 
     * updated list of diagnoses, prescriptions, and treatment plans and applies them to the patient's medical record.
     * The method returns a status message indicating whether the update was successful or if an error occurred.
     *
     * @param patientId The unique ID of the patient whose medical record is to be updated.
     * @param diagnose A list of diagnoses to be added to the patient's medical record.
     * @param prescription A list of prescriptions to be added to the patient's medical record.
     * @param treatmentPlan A list of treatment plans to be added to the patient's medical record.
     * @return A {@link String} message indicating the result of the update operation, 
     *         such as success or failure.
     */
    public String updateMedicalRecord(String patientId, List<String> diagnose, List<String> prescription,
            List<String> treatmentPlan) {
        return MedicalRecordDB.updateMedicalRecord(patientId, diagnose, prescription, treatmentPlan);
    }

}
