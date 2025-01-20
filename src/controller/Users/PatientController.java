package controller.Users;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import DTO.PatientDetailsDTO;
import model.Users.Doctor;
import model.Users.Patient;
import repository.Users.PatientDB;

/**
 * The {@link PatientController} class is responsible for handling operations related to {@link Patient} objects.
 * It implements the {@link IUserController} interface, providing methods to manage patient information,
 * including adding, updating, and retrieving patient details.
 * 
 * This class serves as a controller for interacting with the underlying {@link PatientDB} to perform CRUD operations
 * (Create, Read, Update, Delete) for {@link Patient} objects, as well as interacting with {@link PatientDetailsDTO} 
 * to retrieve patient details in a structured format.
 * 
 * Methods include:
 * <ul>
 *     <li>viewPersonalInfo: Retrieves the personal information of a patient by ID and returns it as a {@link PatientDetailsDTO} object.</li>
 *     <li>add: Adds a new patient to the database.</li>
 *     <li>updatePersonalInfo: Updates the personal information of an existing patient.</li>
 *     <li>viewPatientsByDoctor: Retrieves the list of patients assigned to a particular doctor.</li>
 * </ul>
 * 
 */
public class PatientController implements IUserController<Patient> {
    // View patient by patient Id
    /**
     * Retrieves a {@link Patient} object based on the provided user ID.
     *
     * @param userId The unique identifier of the user to be fetched.
     * @return The {@link Patient} object corresponding to the specified user ID.
     *         If no patient is found with the given ID, returns {@code null}.
     */
    @Override
    public Patient getUserById(String userId) {
        return PatientDB.getUserById(userId);
    }

    /**
     * Adds a new {@link Patient} to the database.
     *
     * @param patient The {@link Patient} object to be added.
     * @return A {@link String} containing a message indicating the result of the operation,
     *         such as a success message or an error message.
     */
    public static String add(Patient patient) {
        return PatientDB.addUser(patient);
    }

    // View patients under the doctor's care
    /**
     * Retrieves a list of {@link Patient} objects associated with a specific {@link Doctor}.
     * 
     * This method checks the list of patient IDs associated with the given doctor and fetches
     * the corresponding {@link Patient} objects from the database. If no patients are found, 
     * it returns {@code null}.
     *
     * @param doctor The {@link Doctor} object whose associated patients are to be retrieved.
     * @return A {@link List} of {@link Patient} objects associated with the given doctor.
     *         Returns {@code null} if no patients are found or if the doctor has no associated patients.
     */
    public List<Patient> viewPatientsByDoctor(Doctor doctor) {
        if (doctor.getPatientIds() == null) {
            return null;
        }
        
        List<Patient> patients = new ArrayList<>();
        for (String patientId : doctor.getPatientIds()) {
            Patient patient = PatientDB.getUserById(patientId);
            if (patient != null) {
                patients.add(patient);
            }
        }
        return patients.isEmpty() ? null : patients;
    }

    // Update non-medical personal info
    // In patientController class
    /**
     * Updates the personal information of a {@link Patient} in the database.
     * 
     * This method updates the information for a patient based on the provided patient ID and new ID.
     * It calls the {@link PatientDB#updateUserInfo(String, String, Patient)} method to perform the update operation.
     *
     * @param patientId The current ID of the patient whose information is to be updated.
     * @param patient The {@link Patient} object containing the new information to update.
     * @param newId The new ID to be assigned to the patient.
     * @return A {@link String} indicating the result of the update operation, such as a success or error message.
     */
    public String updatePersonalInfo(String patientId, Patient patient,String newId) {
        return PatientDB.updateUserInfo(patientId,newId, patient);
    }

    /**
     * Retrieves the personal information of a {@link Patient} and returns it as a {@link PatientDetailsDTO} object.
     * 
     * This method fetches the {@link Patient} object based on the provided patient ID and then maps 
     * the patient's details to a {@link PatientDetailsDTO} object, which is returned to the caller.
     *
     * @param patientId The unique identifier of the patient whose personal information is to be retrieved.
     * @return A {@link PatientDetailsDTO} object containing the personal details of the patient.
     *         If no patient is found with the given ID, this method may return {@code null}.
     */
    public PatientDetailsDTO viewPersonalInfo(String patientId) {
        // Get patient by patientId
        Patient patient = PatientDB.getUserById(patientId);

        PatientDetailsDTO patientDetailsDTO = new PatientDetailsDTO();
        patientDetailsDTO.setPatientId(patientId);
        patientDetailsDTO.setName(patient.getName());
        patientDetailsDTO.setGender(patient.getGender());
        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(patient.getDateOfBirth(), currentDate);
        patientDetailsDTO.setAge(period.getYears());
        patientDetailsDTO.setDateOfBirth(patient.getDateOfBirth());
        patientDetailsDTO.setContactNumber(patient.getContactNumber());
        patientDetailsDTO.setEmailAddress(patient.getEmailAddress());
        patientDetailsDTO.setBloodType(patient.getBloodType());

        return patientDetailsDTO;
    }
}
