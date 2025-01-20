package repository.Appointment;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.time.LocalDateTime;

import model.Appointment.Appointment;
import model.Appointment.Appointment.AppointmentStatus;
import utils.Utility.CSVWriter;
import utils.Utility.Printer;

/**
 * AppointmentDB handles CRUD operations and retrieval logic for managing
 * appointments.
 */
public class AppointmentDB {
    /**
     * A map that stores appointments, indexed by their unique appointment ID.
     * <p>
     * Each key-value pair in the map represents an appointment, where the key is
     * the
     * appointment's ID (an integer), and the value is the corresponding
     * {@link Appointment} object.
     */
    public static HashMap<Integer, Appointment> appointments = new HashMap<>();

    /**
     * The file path to the CSV file that contains the appointment list.
     * <p>
     * This path points to a CSV file containing data about appointments, which may
     * be used
     * for importing or exporting appointment information.
     */
    public static String path = "Hospital/src/resources/CSV/Appointment_List.csv";

    /**
     * An array of header names that correspond to the columns in the CSV file.
     * <p>
     * This array contains the column headers in the CSV file, which are used to map
     * the CSV data to appropriate fields when reading or writing appointments.
     */
    public static String[] headers = {
            "AppointmentId",
            "DoctorId",
            "PatientId",
            "DateTime",
            "Status"
    };

    /**
     * Adds an appointment to the database.
     * 
     * @param appointment the appointment to add
     * @param init        whether the operation is during initialization
     * @return result message
     */
    public static String addAppointment(Appointment appointment, Boolean init) {
        try {
            appointments.put(appointment.getAppointmentId(), appointment);
            if (!init) {
                CSVWriter.writeCSV(path, appointments, headers);
            }
            return "Appointment added successfully with ID: " + appointment.getAppointmentId();
        } catch (Exception e) {
            return "Error adding appointment: " + e.getMessage();
        }
    }

    /**
     * Updates the status of an appointment.
     * 
     * @param appointmentId the ID of the appointment to update
     * @param status        the new status
     * @param init          whether the operation is during initialization
     * @return true if the status is updated, false otherwise
     */
    public static Boolean updateAppointmentStatus(int appointmentId, String status, Boolean init) {
        try {
            Appointment appointment = appointments.get(appointmentId);
            if (appointment != null) {
                AppointmentStatus updatedStatus = AppointmentStatus.valueOf(status.toUpperCase());
                appointment.setStatus(updatedStatus);
                appointments.put(appointmentId, appointment);
                if (!init) {
                    CSVWriter.writeCSV(path, appointments, headers);
                }
                return true;
            } else {
                return false;
            }
        } catch (IllegalArgumentException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Updates the date and time of an appointment.
     * 
     * @param appointmentId the ID of the appointment to update
     * @param dateTime      the new date and time
     * @param init          whether the operation is during initialization
     * @return result message
     */
    public static String updateAppointment(int appointmentId, LocalDateTime dateTime, Boolean init) {
        try {
            Appointment appointment = appointments.get(appointmentId);
            if (appointment != null) {
                appointment.setDateTime(dateTime);
                appointments.put(appointmentId, appointment);
                if (!init) {
                    CSVWriter.writeCSV(path, appointments, headers);
                }
                return "Appointment updated successfully for ID: " + appointmentId;
            } else {
                return "Error: Appointment with ID " + appointmentId + " not found.";
            }
        } catch (Exception e) {
            return "Error updating appointment: " + e.getMessage();
        }
    }

    /**
     * Retrieves scheduled appointments for a patient.
     * 
     * @param patientId the ID of the patient
     * @return list of scheduled appointments or null if none
     */
    public static List<Appointment> getScheduledAppointments(String patientId) {
        List<Appointment> scheduledAppointments = new ArrayList<>();
        for (Appointment appointment : appointments.values()) {
            if (appointment.getPatientId().equals(patientId)) {
                scheduledAppointments.add(appointment);
            }
        }
        return scheduledAppointments.isEmpty() ? null : scheduledAppointments;
    }

    /**
     * Retrieves past appointments for a patient.
     * 
     * @param patientId the ID of the patient
     * @return list of past appointments
     */
    public static List<Appointment> getPastAppointmentsByPatientId(String patientId) {
        List<Appointment> pastAppointments = new ArrayList<>();
        for (Appointment appointment : appointments.values()) {
            if (appointment.getPatientId().equals(patientId)
                    && appointment.getStatus().equals(Appointment.AppointmentStatus.COMPLETED)) {
                pastAppointments.add(appointment);
            }
        }
        return pastAppointments;
    }

    /**
     * Retrieves past appointments for a doctor.
     * 
     * @param doctorId the ID of the doctor
     * @return list of past appointments
     */
    public static List<Appointment> getPastAppointmentsByDoctorId(String doctorId) {
        List<Appointment> pastAppointments = new ArrayList<>();
        for (Appointment appointment : appointments.values()) {
            if (appointment.getDoctorId().equals(doctorId)
                    && appointment.getStatus().equals(Appointment.AppointmentStatus.COMPLETED)) {
                pastAppointments.add(appointment);
            }
        }
        return pastAppointments;
    }

    /**
     * Retrieves confirmed appointments for a doctor.
     * 
     * @param doctorId the ID of the doctor
     * @return list of confirmed appointments
     */
    public static List<Appointment> getUpcomingAppointments(String doctorId) {
        List<Appointment> doctorAppointments = new ArrayList<>();
        for (Appointment appointment : appointments.values()) {
            if (appointment.getDoctorId().equals(doctorId)
                    && appointment.getStatus().equals(Appointment.AppointmentStatus.CONFIRMED)) {
                doctorAppointments.add(appointment);
            }
        }
        return doctorAppointments;
    }

    /**
     * Retrieves all upcoming appointments (confirmed and pending) for a doctor.
     * 
     * @param doctorId the ID of the doctor
     * @return list of upcoming appointments
     */
    public static List<Appointment> getAllUpcomingAppointments(String doctorId) {
        List<Appointment> doctorAppointments = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (Appointment appointment : appointments.values()) {
            if (appointment.getDoctorId().equals(doctorId)
                    && (appointment.getStatus().equals(Appointment.AppointmentStatus.CONFIRMED)
                            || appointment.getStatus().equals(Appointment.AppointmentStatus.PENDING))
                    && appointment.getDateTime().isAfter(now)) {
                doctorAppointments.add(appointment);
            }
        }
        return doctorAppointments;
    }

    /**
     * Retrieves all appointments for a patient.
     * 
     * @param patientId the ID of the patient
     * @return list of appointments or null if none
     */
    public static List<Appointment> getAppointmentsByPatientId(String patientId) {
        List<Appointment> patientAppointments = new ArrayList<>();
        for (Appointment appointment : appointments.values()) {
            if (appointment.getPatientId().equals(patientId)) {
                patientAppointments.add(appointment);
            }
        }
        return patientAppointments.isEmpty() ? null : patientAppointments;
    }

    /**
     * Retrieves all appointments for a doctor.
     * 
     * @param doctorId the ID of the doctor
     * @return list of appointments or null if none
     */
    public static List<Appointment> getAppointmentsByDoctorId(String doctorId) {
        List<Appointment> patientAppointments = new ArrayList<>();
        for (Appointment appointment : appointments.values()) {
            if (appointment.getDoctorId().equals(doctorId)) {
                patientAppointments.add(appointment);
            }
        }
        return patientAppointments.isEmpty() ? null : patientAppointments;
    }

    /**
     * Retrieves an appointment by its ID.
     * 
     * @param appointmentId the ID of the appointment
     * @return the appointment or null if not found
     */
    public static Appointment getAppointmentById(int appointmentId) {
        return appointments.get(appointmentId);
    }

    /**
     * Retrieves all appointments in the database.
     * 
     * @return all appointments
     */
    public static HashMap<Integer, Appointment> getAllAppointments() {
        return appointments;
    }

    /**
     * Prints all appointments in the database.
     */
    public static void printAllAppointments() {
        Printer.printTable(appointments, "AppointmentsDB");
    }

    public static String updatePatientIdInAppointment(String oldPatientId, String newPatientId) {
        try {
            // Retrieve the list of appointments for the old patient ID
            List<Appointment> appointmentsToUpdate = getAppointmentsByPatientId(oldPatientId);

            // Check if the list of appointments is empty
            if (appointmentsToUpdate != null && !appointmentsToUpdate.isEmpty()) {
                // Check if the new patient ID is valid and not already in use
                if (newPatientId != null && !newPatientId.isEmpty() && !newPatientId.equals(oldPatientId)) {
                    // Ensure the new patient ID is not already present in the appointments database
                    for (Appointment appointment : appointmentsToUpdate) {
                        if (appointment.getPatientId().equals(newPatientId)) {
                            return "Error: The new patient ID (" + newPatientId
                                    + ") already exists in the appointments. Please choose another one.";
                        }
                    }

                    // Update the patient ID for each appointment
                    for (Appointment appointment : appointmentsToUpdate) {
                        appointment.setPatientId(newPatientId);
                    }

                    // Remove the old appointments and add the updated ones with the new patient ID
                    for (Appointment appointment : appointmentsToUpdate) {
                        appointments.remove(oldPatientId);
                        appointments.put(appointment.getAppointmentId(), appointment);
                    }

                    // Optionally, save the updated appointments (e.g., to CSV)
                    CSVWriter.writeCSV(path, appointments, headers);

                    return "Patient ID updated successfully in the appointments for patient ID: " + oldPatientId;
                } else {
                    return "Error: Invalid new patient ID or same as the old ID.";
                }
            } else {
                return "Error: No appointments found for patient ID: " + oldPatientId;
            }
        } catch (Exception e) {
            return "Error updating appointment for patient ID " + oldPatientId + ": " + e.getMessage();
        }
    }

   
    public static String updateDoctorIdInAppointments(String oldDoctorId, String newDoctorId) {
        try {
            // Retrieve the list of all appointments (assuming appointments is the data structure storing appointments)
            List<Appointment> appointmentsToUpdate = new ArrayList<>(appointments.values());
    
            // Check if the list of appointments is empty
            if (appointmentsToUpdate != null && !appointmentsToUpdate.isEmpty()) {
                // Check if the old doctor ID exists in any of the appointments
                boolean doctorIdExists = false;
                for (Appointment appointment : appointmentsToUpdate) {
                    if (appointment.getDoctorId().equals(oldDoctorId)) {
                        doctorIdExists = true;
                        break;
                    }
                }
    
                if (!doctorIdExists) {
                    return "Error: No appointments found with the old doctor ID (" + oldDoctorId + ").";
                }
    
                // Check if the new doctor ID is valid
                if (newDoctorId != null && !newDoctorId.isEmpty()) {
                    // Update the doctor ID for each appointment that has the old doctor ID
                    for (Appointment appointment : appointmentsToUpdate) {
                        if (appointment.getDoctorId().equals(oldDoctorId)) {
                            appointment.setDoctorId(newDoctorId);  // Set the new doctor ID
                        }
                    }
    
                    // Optionally, save the updated appointments (e.g., to CSV or database)
                    CSVWriter.writeCSV(path, appointments, headers);
    
                    return "Doctor ID updated successfully from " + oldDoctorId + " to " + newDoctorId + " for all appointments.";
                } else {
                    return "Error: Invalid new doctor ID.";
                }
            } else {
                return "Error: No appointments found.";
            }
        } catch (Exception e) {
            return "Error updating doctor ID: " + e.getMessage();
        }
    }
    
    
}
