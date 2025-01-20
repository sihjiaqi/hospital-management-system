package utils;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import model.Appointment.Appointment;
import model.Appointment.AppointmentOutcome;
import model.MedicalRecord.MedicalRecord;
import model.Medication.Medication;
import model.Medication.ReplenishRequest;
import model.Users.Admin;
import model.Users.Doctor;
import model.Users.Patient;
import model.Users.Pharmacist;
import repository.Appointment.AppointmentDB;
import repository.Appointment.AppointmentOutcomeDB;
import repository.MedicalRecord.MedicalRecordDB;
import repository.Medication.MedicationDB;
import repository.Medication.ReplenishRequestDB;
import repository.Users.PatientDB;
import repository.Users.StaffDB;
import utils.Utility.CSVReader;
import utils.Utility.Util;

/**
 * The Database class is responsible for loading and initializing the hospital
 * data from CSV files. This includes loading information about users (staff and
 * patients), appointments, medical records, medications, appointment outcomes,
 * and replenish requests into respective repositories.
 * 
 * <p>This class uses multi-threading to load medications first, then loads
 * other data sequentially to ensure a proper flow of operations. It also links
 * doctor availability data to the appropriate doctor in the system.
 */
public class Database {
    /**
     * Initializes the hospital system by loading data from CSV files into the
     * respective databases.
     * 
     * @return 1 if the initialization is successful.
     */
    public int init() {
        // Load Users
        LocalDate dob = null;

        String[] userDataFiles = { "Hospital/src/resources/CSV/Staff_List.csv",
                "Hospital/src/resources/CSV/Patient_List.csv" };

        for (String dfile : userDataFiles) {
            List<String[]> csvValues = CSVReader.csvReaderUtil(dfile);

            for (int i = 0; i < csvValues.size(); i++) {
                String[] values = csvValues.get(i);
                String uid = values[0];

                // System.out.println(values);
                switch (uid.charAt(0)) {
                    case 'P':
                        dob = Util.convertStringToDate(values[5]);

                        // Assuming blood type is at index 8 in your CSV
                        Pharmacist newPharm = new Pharmacist(
                                values[0], // staffId
                                values[1], // name
                                values[2], // gender
                                values[3], // username
                                values[4], // password
                                dob, // dateOfBirth
                                values[6], // contactNumber
                                values[7], // emailAddress
                                values[8] // licenseNumber
                        );
                        StaffDB.addUser(newPharm, true);

                        break;
                    case 'D':

                        dob = Util.convertStringToDate(values[5]);
                        //System.out.println(values[9]);
                        Doctor newDoc = new Doctor(
                                values[0], // staffId
                                values[1], // name
                                values[2], // gender
                                values[3], // username
                                values[4], // password
                                dob, // dateOfBirth
                                values[6], // contactNumber,
                                values[7], // emailAddress
                                values[9] != null ? Arrays.asList(values[9].split(";")) : null);
                        StaffDB.addUser(newDoc, true);
                        Map<String, Map<LocalDate, List<LocalTime>>> loadedAvailability = CSVReader.loadAvailabilityFromCSV("Hospital/src/resources/CSV/Availability.csv");

                        // Link availability to doctors
                        addAvailabilityToDoctor(newDoc, loadedAvailability);
                        break;
                    case 'A':
                        dob = Util.convertStringToDate(values[5]);

                        Admin newAdmin = new Admin(values[0], // staffId
                                values[1], // name
                                values[2], // gender
                                values[3], // username
                                values[4], // password
                                dob, // dateOfBirth
                                values[6], // contactNumber
                                values[7] // emailAddress
                        );
                        StaffDB.addUser(newAdmin, true);
                        break;
                    case 'p':
                        // System.out.println("called");
                        dob = Util.convertStringToDate(values[5]);

                        Patient newPatient = new Patient(values[0], //
                                values[1], // name
                                values[2], // gender
                                values[3], // username
                                values[4], // password
                                dob, // dateOfBirth
                                values[6], // contactNumber
                                values[7], // emailAddress
                                values[8] // bloodType
                        );
                        PatientDB.addUser(newPatient, true);
                        break;
                }
            }

        }

        // Load Appointments
        loadAppointments("Hospital/src/resources/CSV/Appointment_List.csv");

        // Load Medications
        loadMedications("Hospital/src/resources/CSV/Medicine_List.csv");

        // Load Appointment Outcomes
        loadAppointmentOutcomes("Hospital/src/resources/CSV/AppointmentOutcome_List.csv");

        // Load Medical Records
        loadMedicalRecords("Hospital/src/resources/CSV/Medical_Records.csv");

        // Load Replenish Requests
        loadReplenishRequests("Hospital/src/resources/CSV/Replenishment_Request.csv");

        ExecutorService executorService = Executors.newSingleThreadExecutor(); 

        // Create Future to track the completion of loadMedications
        Future<?> loadMedicationsFuture = executorService
                .submit(() -> loadMedications("Hospital/src/resources/CSV/Medicine_List.csv"));

        try {
            // Wait for loadMedications to finish
            loadMedicationsFuture.get(); // This will block until loadMedications is complete

            // Once loadMedications is done, load appointment outcomes
            loadAppointmentOutcomes("Hospital/src/resources/CSV/AppointmentOutcome_List.csv");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown(); // Clean up the executor service
        }

        return 1;
    }

    /**
     * Loads medication data from a CSV file and adds them to the medication database.
     * 
     * @param dataFile The path to the CSV file containing medication data.
     */
    private void loadMedications(String dataFile) {
        List<String[]> csvValues = CSVReader.csvReaderUtil(dataFile);
        for (String[] values : csvValues) {
            // System.out.println(String.join(", ", values)); // Join elements with a comma
            // and space for readability

            Medication newMedication = new Medication(
                    values[0], // name
                    Integer.parseInt(values[1]), // initialStock
                    Integer.parseInt(values[2]),
                    Integer.parseInt(values[3]),
                    Double.parseDouble(values[4]));
            MedicationDB.addMedication(newMedication, true);
        }
    }

    /**
     * Loads appointment outcomes from a CSV file and adds them to the appointment
     * outcome database.
     * 
     * @param dataFile The path to the CSV file containing appointment outcome data.
     */
    private void loadAppointmentOutcomes(String dataFile) {
        List<String[]> csvValues = CSVReader.csvReaderUtil(dataFile);
        for (String[] values : csvValues) {
            AppointmentOutcome newAppointmentOutcome = new AppointmentOutcome(Integer.parseInt(values[0]), // appointmentId
                    values[1], // servieType
                    Arrays.asList(values[2].split(";")), // medicationIds
                    values[3], // consultationNotes
                    AppointmentOutcome.PrescriptionStatus.valueOf(values[4].trim().toUpperCase())); // status
            AppointmentOutcomeDB.addAppointmentOutcome(newAppointmentOutcome, true);
        }
    }

    /**
     * Loads appointment data from a CSV file and adds them to the appointment database.
     * 
     * @param dataFile The path to the CSV file containing appointment data.
     */
    private void loadAppointments(String dataFile) {
        List<String[]> csvValues = CSVReader.csvReaderUtil(dataFile);
        for (String[] values : csvValues) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            // LocalDateTime
            LocalDateTime dateTime = LocalDateTime.parse(values[3], formatter);

            Appointment newAppointment = new Appointment(values[1], // doctorId
                    values[2], // patientId
                    dateTime, // dateTime
                    Appointment.AppointmentStatus.valueOf(values[4].trim().toUpperCase())); // status
            AppointmentDB.addAppointment(newAppointment, true);
        }

        // AppointmentDB.printAllAppointments(); for debugging
    }

    /**
     * Loads medical records from a CSV file and adds them to the medical record database.
     * 
     * @param dataFile The path to the CSV file containing medical record data.
     */
    private void loadMedicalRecords(String dataFile) {
        List<String[]> csvValues = CSVReader.csvReaderUtil(dataFile);

        for (String[] values : csvValues) {

            MedicalRecord newMedicalRecord = new MedicalRecord(values[0], // patientId
                    Arrays.asList(values[1].split(";")), // diagnoses
                    Arrays.asList(values[2].split(";")), // prescriptions
                    Arrays.asList(values[3].split(";"))); // treatmentPlans
            MedicalRecordDB.addMedicalRecord(newMedicalRecord, true);
        }

        // MedicalRecordDB.printAllMedicalRecords(); for debugging
    }

    /**
     * Loads replenish requests from a CSV file and adds them to the replenish request database.
     * 
     * @param dataFile The path to the CSV file containing replenish request data.
     */
    private void loadReplenishRequests(String dataFile) {
        List<String[]> csvValues = CSVReader.csvReaderUtil(dataFile);
        for (String[] values : csvValues) {
            ReplenishRequest newReplenishRequest = new ReplenishRequest(
                    values[1], // staffId
                    values[2],
                    ReplenishRequest.ReplenishRequestStatus.valueOf(values[3].trim().toUpperCase()),
                    Integer.parseInt(values[4]),
                    Util.convertStringToDate(values[5]));
            ReplenishRequestDB.addReplenishRequest(newReplenishRequest, true);
        }
    }

    /**
     * Adds availability data to a doctor, linking their availability schedule.
     * 
     * @param doctor           The doctor to whom the availability data should be added.
     * @param availabilityData A map containing the availability data for all doctors.
     */
    public static void addAvailabilityToDoctor(Doctor doctor, Map<String, Map<LocalDate, List<LocalTime>>> availabilityData) {
        // Find availability data for the specific doctor by staff ID
        Map<LocalDate, List<LocalTime>> doctorAvailability = availabilityData.get(doctor.getId());
        
        if (doctorAvailability != null) {
            doctor.setMonthlyAvailability(doctorAvailability, true);
        }
    }
}