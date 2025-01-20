package DTO;

import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) representing the details of a patient.
 * This class contains information related to a patient such as their ID, name, gender, age, 
 * contact details, and medical information like date of birth and blood type.
 */
public class PatientDetailsDTO {
    private String patientId;
    private String name;
    private String gender;
    private int age;
    private String username;
    private LocalDate dateOfBirth;
    private String contactNumber;
    private String emailAddress;
    private String bloodType;

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
     * Retrieves the age of the patient.
     *
     * @return the patient's age
     */
    public int getAge() {
        return age;
    }

    /**
     * Retrieves the username associated with the patient's account.
     *
     * @return the patient's username
     */
    public String getUsername() {
        return username;
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
     * Sets the age of the patient.
     *
     * @param age the age to set for the patient
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Sets the username associated with the patient's account.
     *
     * @param username the username to set for the patient
     */
    public void setUsername(String username) {
        this.username = username;
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
}