package DTO;

import java.time.LocalDate;
import java.util.List;

/**
 * Data Transfer Object (DTO) representing a patient's medical record.
 * This class contains details about the patient, including personal information, medical history,
 * diagnoses, prescriptions, and treatment plans.
 */
public class PatientMedicalRecordDTO {

    private String patientId;
    private String name;
    private String gender;
    private LocalDate dateOfBirth;
    private String contactNumber;
    private String emailAddress;
    private String bloodType;
    private List<String> diagnoses;
    private List<String> prescriptions;
    private List<String> treatmentPlans;

    /**
     * Retrieves the unique patient ID.
     *
     * @return the patient ID
     */
    public String getPatientId() {
        return patientId;
    }

    /**
     * Retrieves the name of the patient.
     *
     * @return the patient's name
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the gender of the patient.
     *
     * @return the patient's gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * Retrieves the date of birth of the patient.
     *
     * @return the patient's date of birth
     */
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Retrieves the contact number of the patient.
     *
     * @return the patient's contact number
     */
    public String getContactNumber() {
        return contactNumber;
    }

    /**
     * Retrieves the email address of the patient.
     *
     * @return the patient's email address
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Retrieves the blood type of the patient.
     *
     * @return the patient's blood type
     */
    public String getBloodType() {
        return bloodType;
    }

    /**
     * Retrieves the list of diagnoses for the patient.
     *
     * @return the list of diagnoses
     */
    public List<String> getDiagnoses() {
        return diagnoses;
    }

    /**
     * Retrieves the list of prescriptions for the patient.
     *
     * @return the list of prescriptions
     */
    public List<String> getPrescriptions() {
        return prescriptions;
    }

    /**
     * Retrieves the list of treatment plans for the patient.
     *
     * @return the list of treatment plans
     */
    public List<String> getTreatmentPlans() {
        return treatmentPlans;
    }

    /**
     * Sets the unique patient ID.
     *
     * @param patientId the patient ID to set
     */
    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    /**
     * Sets the name of the patient.
     *
     * @param name the name to set for the patient
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the gender of the patient.
     *
     * @param gender the gender to set for the patient
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Sets the date of birth of the patient.
     *
     * @param dateOfBirth the date of birth to set for the patient
     */
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Sets the contact number of the patient.
     *
     * @param contactNumber the contact number to set for the patient
     */
    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    /**
     * Sets the email address of the patient.
     *
     * @param emailAddress the email address to set for the patient
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**
     * Sets the blood type of the patient.
     *
     * @param bloodType the blood type to set for the patient
     */
    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    /**
     * Sets the list of diagnoses for the patient.
     *
     * @param diagnoses the list of diagnoses to set for the patient
     */
    public void setDiagnosis(List<String> diagnoses) {
       this.diagnoses = diagnoses;
    }

    /**
     * Sets the list of prescriptions for the patient.
     *
     * @param prescriptions the list of prescriptions to set for the patient
     */
    public void setPrescription(List<String> prescriptions) {
        this.prescriptions = prescriptions;
    }

    /**
     * Sets the list of treatment plans for the patient.
     *
     * @param treatmentPlans the list of treatment plans to set for the patient
     */
    public void setTreatmentPlan(List<String> treatmentPlans) {
        this.treatmentPlans = treatmentPlans;
    }
}

