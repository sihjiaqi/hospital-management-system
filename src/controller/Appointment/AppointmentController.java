package controller.Appointment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import DTO.AppointmentAndOutcomeDTO;
import DTO.AppointmentOutcomeDTO;
import model.Appointment.Appointment;
import model.Appointment.Appointment.AppointmentStatus;
import model.Appointment.AppointmentOutcome;
import model.Appointment.AppointmentOutcome.BillingStatus;
import model.Appointment.AppointmentOutcome.PrescriptionStatus;
import model.Users.Doctor;
import model.Users.User;
import repository.Appointment.AppointmentDB;
import repository.Appointment.AppointmentOutcomeDB;
import repository.Users.StaffDB;

/**
 * Controller class for managing and interacting with appointment-related operations.
 * 
 * The {@link AppointmentController} class provides methods to manage and view appointments, including scheduling, rescheduling,
 * canceling, and viewing details of appointments for patients and doctors. Additionally, it supports functionality for managing appointment outcomes.
 * This class interacts with various database classes like {@link AppointmentDB} and {@link AppointmentOutcomeDB} to perform CRUD (Create, Read, Update, Delete) operations 
 * on appointment records and their associated outcomes.
 * 
 * Key functionalities provided by this class include:
 * <ul>
 *   <li>Scheduling, rescheduling, and canceling appointments</li>
 *   <li>Viewing upcoming, past, and scheduled appointments by patient or doctor</li>
 *   <li>Managing doctor availability and time slots</li>
 *   <li>Recording and viewing appointment outcomes</li>
 * </ul>
 * 
 * All appointment-related operations are designed to facilitate appointment management within a healthcare system, 
 * ensuring that doctors and patients can interact efficiently and that appointment statuses are properly tracked and updated.
 * 
 * @see Appointment
 * @see AppointmentDB
 * @see AppointmentOutcome
 * @see AppointmentOutcomeDB
 * @see Doctor
 * @see StaffDB
 */
public class AppointmentController {
    // ----------------------------------------------------------------
    // Admin Appointment Management
    // ----------------------------------------------------------------
    // Access real-time updates of scheduled appointments by patient Id
    /**
     * Retrieves a list of appointments and their corresponding outcomes for a specified patient.
     * 
     * This method iterates through all available appointments in the database, filters the appointments by the given patient ID,
     * and creates a list of {@link AppointmentAndOutcomeDTO} objects. For each completed appointment, the method also retrieves 
     * and includes the associated outcome data (if available).
     * 
     * @param patientId The unique identifier of the patient whose appointments and outcomes are to be retrieved.
     * @return A list of {@link AppointmentAndOutcomeDTO} objects containing appointment and outcome information for the given patient.
     *         If no appointments are found for the patient, or if the appointment database is empty, the method returns null.
     * 
     * @see Appointment
     * @see AppointmentAndOutcomeDTO
     * @see AppointmentOutcomeDTO
     * @see AppointmentOutcome
     */
    public List<AppointmentAndOutcomeDTO> viewAppointmentAndOutcomesByPatientId(String patientId) {
        List<AppointmentAndOutcomeDTO> appointmentAndOutcomeDTOs = new ArrayList<>();
        HashMap<Integer, Appointment> appointments = AppointmentDB.getAllAppointments();
    
        if (appointments.isEmpty()) {
            return null; // Return null if no appointments found
        }
    
        for (Appointment appointment : appointments.values()) {
            // Check if the appointment belongs to the specified patient
            if (appointment.getPatientId().equals(patientId)) {
                // Create a new DTO for each appointment
                AppointmentAndOutcomeDTO appointmentAndOutcomeDTO = new AppointmentAndOutcomeDTO();
                appointmentAndOutcomeDTO.setAppointmentId(appointment.getAppointmentId());
                appointmentAndOutcomeDTO.setPatientId(appointment.getPatientId());
                appointmentAndOutcomeDTO.setDoctorId(appointment.getDoctorId());
                appointmentAndOutcomeDTO.setStatus(appointment.getStatus().toString());
                appointmentAndOutcomeDTO.setDateTime(appointment.getDateTime());
    
                // If the appointment is completed, set the appointment outcome
                if (appointment.getStatus() == Appointment.AppointmentStatus.COMPLETED) {
                    AppointmentOutcomeDTO appointmentOutcomeDTO = new AppointmentOutcomeDTO();
                    AppointmentOutcome appointmentOutcome = AppointmentOutcomeDB
                            .getAppointmentOutcome(appointment.getAppointmentId());
    
                    if (appointmentOutcome != null) {
                        appointmentOutcomeDTO.setServiceType(appointmentOutcome.getServiceType());
                        appointmentOutcomeDTO.setMedicationId(appointmentOutcome.getMedicationId());
                        appointmentOutcomeDTO.setConsultationNotes(appointmentOutcome.getConsultationNotes());
                        appointmentOutcomeDTO.setStatus(appointmentOutcome.getStatus().toString());
                        appointmentAndOutcomeDTO.setAppointmentOutcomeDTO(appointmentOutcomeDTO);
                    }
                }
                // Add the current DTO to the list
                appointmentAndOutcomeDTOs.add(appointmentAndOutcomeDTO);
            }
        }
    
        return appointmentAndOutcomeDTOs; // Return the list of DTOs
    }
    

    // Access real-time updates of scheduled appointments by doctor Id
    /**
     * Retrieves a list of appointments and their corresponding outcomes for a specified doctor.
     * 
     * This method iterates through all available appointments in the database, filters the appointments by the given doctor ID,
     * and creates a list of {@link AppointmentAndOutcomeDTO} objects. For each completed appointment, the method also retrieves 
     * and includes the associated outcome data (if available).
     * 
     * @param doctorId The unique identifier of the doctor whose appointments and outcomes are to be retrieved.
     * @return A list of {@link AppointmentAndOutcomeDTO} objects containing appointment and outcome information for the given doctor.
     *         If no appointments are found for the doctor, or if the appointment database is empty, the method returns null.
     * 
     * @see Appointment
     * @see AppointmentAndOutcomeDTO
     * @see AppointmentOutcomeDTO
     * @see AppointmentOutcome
     */
    public List<AppointmentAndOutcomeDTO> viewAppointmentAndOutcomesByDoctorId(String doctorId) {
        List<AppointmentAndOutcomeDTO> appointmentAndOutcomeDTOs = new ArrayList<>();
        HashMap<Integer, Appointment> appointments = AppointmentDB.getAllAppointments();
        if (appointments.isEmpty()) {
            return null;
        }

        for (Appointment appointment : appointments.values()) {
            if (appointment.getDoctorId() == doctorId) {
                // Create a new DTO for each appointment
                AppointmentAndOutcomeDTO appointmentAndOutcomeDTO = new AppointmentAndOutcomeDTO();
                appointmentAndOutcomeDTO.setAppointmentId(appointment.getAppointmentId());
                appointmentAndOutcomeDTO.setPatientId(appointment.getPatientId());
                appointmentAndOutcomeDTO.setDoctorId(appointment.getDoctorId());
                appointmentAndOutcomeDTO.setStatus(appointment.getStatus().toString());
                appointmentAndOutcomeDTO.setDateTime(appointment.getDateTime());
    
                // If the appointment is completed, set the appointment outcome
                if (appointment.getStatus() == Appointment.AppointmentStatus.COMPLETED) {
                    AppointmentOutcomeDTO appointmentOutcomeDTO = new AppointmentOutcomeDTO();
                    AppointmentOutcome appointmentOutcome = AppointmentOutcomeDB
                            .getAppointmentOutcome(appointment.getAppointmentId());
    
                    if (appointmentOutcome != null) {
                        appointmentOutcomeDTO.setServiceType(appointmentOutcome.getServiceType());
                        appointmentOutcomeDTO.setMedicationId(appointmentOutcome.getMedicationId());
                        appointmentOutcomeDTO.setConsultationNotes(appointmentOutcome.getConsultationNotes());
                        appointmentOutcomeDTO.setStatus(appointmentOutcome.getStatus().toString());
                        appointmentAndOutcomeDTO.setAppointmentOutcomeDTO(appointmentOutcomeDTO);
                    }
                }
                // Add the current DTO to the list
                appointmentAndOutcomeDTOs.add(appointmentAndOutcomeDTO);
            }
        }
        return appointmentAndOutcomeDTOs;
    }

    // ----------------------------------------------------------------
    // Doctor Appointment Management
    // ----------------------------------------------------------------
    /**
     * Books a time slot for a specified doctor on a given date and time.
     * 
     * This method updates the doctor's availability by removing the selected time slot from their available slots for the specified
     * date. The availability data is updated in the doctor's profile and saved accordingly.
     * 
     * @param doctorId The unique identifier of the doctor for whom the slot is being booked.
     * @param date The date on which the time slot is to be booked.
     * @param timeSlot The specific time slot to be booked on the given date.
     * 
     * @see Doctor
     * @see StaffDB
     */
    // public void bookSlot(String doctorId, LocalDate date, LocalTime timeSlot) {
    //     // Get doctor by doctor Id
    //     Doctor doctor = (Doctor) StaffDB.getUserById(doctorId);
    //     Map<LocalDate, List<LocalTime>> monthlyAvailability = doctor.getMonthlyAvailability();
    //     List<LocalTime> availableSlots = monthlyAvailability.get(date);
    //     if (availableSlots != null) {
    //         availableSlots.remove(timeSlot); // Remove the slot from availability
    //         monthlyAvailability.put(date, availableSlots);
    //         doctor.setMonthlyAvailability(monthlyAvailability, false);
    //     }
    // }

    public String bookSlot(String doctorId, LocalDate date, LocalTime timeSlot) {
        // Get doctor by doctor Id
        Doctor doctor = (Doctor) StaffDB.getUserById(doctorId);
    
        if (doctor == null) {
            return "Error: Doctor not found.";
        }
    
        Map<LocalDate, List<LocalTime>> monthlyAvailability = doctor.getMonthlyAvailability();
    
        if (monthlyAvailability == null || !monthlyAvailability.containsKey(date)) {
            return "Error: No available slots for the specified date.";
        }
    
        List<LocalTime> availableSlots = monthlyAvailability.get(date);
    
        if (availableSlots == null || !availableSlots.contains(timeSlot)) {
            return "Error: The specified slot is not available.";
        }
    
        // Remove the slot from availability
        availableSlots.remove(timeSlot);
        monthlyAvailability.put(date, availableSlots);
        doctor.setMonthlyAvailability(monthlyAvailability, false);
    
        return "Slot successfully booked.";
    }
    

    /**
     * Unbooks a specific time slot for a doctor by adding it back to their availability.
     *
     * @param doctorId the unique ID of the doctor whose availability needs to be updated
     * @param date the specific date for which the slot needs to be unbooked
     * @param timeSlot the time slot to be unbooked and added back to availability
     * @return a message indicating the result of the operation:
     *         <ul>
     *           <li>"Slot added back to availability: [timeSlot]" if the slot was successfully added back.</li>
     *           <li>"Slot is already available or doesn't exist." if the slot was already available or invalid.</li>
     *         </ul>
     * @throws NullPointerException if the doctor ID does not correspond to a valid doctor
     */
    public String unbookSlot(String doctorId, LocalDate date, LocalTime timeSlot) {
        // Get doctor by doctor Id
        Doctor doctor = (Doctor) StaffDB.getUserById(doctorId);
        
        // Get the doctor's availability map
        Map<LocalDate, List<LocalTime>> monthlyAvailability = doctor.getMonthlyAvailability();
        
        // Get the list of available slots for the given date
        List<LocalTime> availableSlots = monthlyAvailability.get(date);
        
        if (availableSlots != null && !availableSlots.contains(timeSlot)) {
            // Add the time slot back to the available slots list
            availableSlots.add(timeSlot);
            availableSlots.sort(Comparator.naturalOrder());  // Sort to keep slots in order
            
            // Put the updated availability back into the map
            monthlyAvailability.put(date, availableSlots);
            
            // Update the doctor's availability
            doctor.setMonthlyAvailability(monthlyAvailability, false);
            
            return "Slot added back to availability: " + timeSlot;
        } else {
            return "Slot is already available or doesn't exist.";
        }
    }


    // Generate time slots for a given day (with a given start and end time and interval in minutes)
    /**
     * Generates a list of time slots between a specified start time and end time, with a defined interval between each slot.
     * 
     * This method generates a series of {@link LocalTime} objects starting from the specified start time, incremented by the 
     * given interval in minutes, until the end time is reached or exceeded.
     * 
     * @param start The starting time for the first time slot.
     * @param end The ending time for the last time slot.
     * @param intervalMinutes The interval in minutes between each consecutive time slot.
     * @return A list of {@link LocalTime} objects representing the generated time slots.
     * 
     * @see LocalTime
     */
    private List<LocalTime> generateTimeSlots(LocalTime start, LocalTime end, int intervalMinutes) {
        List<LocalTime> slots = new ArrayList<>();
        LocalTime currentTime = start;

        while (currentTime.isBefore(end) || currentTime.equals(end)) {
            slots.add(currentTime);
            currentTime = currentTime.plusMinutes(intervalMinutes);
        }
        return slots;
    }

    // Doctors can set their availability for appointments
    /**
     * Sets the monthly availability for a specified doctor, generating time slots for each day of the month and removing conflicting slots.
     * 
     * This method generates time slots for each day from the specified start date to the end of the month, using the provided start time, 
     * end time, and interval. It then removes any time slots that conflict with the doctor's existing appointments. The updated availability 
     * is stored in the doctor's profile.
     * 
     * @param doctor The {@link Doctor} whose monthly availability is being set.
     * @param startDate The starting date from which availability will be generated.
     * @param startTime The starting time for the first time slot each day.
     * @param endTime The ending time for the last time slot each day.
     * @param intervalMinutes The interval in minutes between each consecutive time slot.
     * @return A string indicating the result of setting the doctor's monthly availability, typically a success message.
     * 
     * @see Doctor
     * @see LocalDate
     * @see LocalTime
     */
    public String setMonthlyAvailability(Doctor doctor, LocalDate startDate, LocalTime startTime, LocalTime endTime, int intervalMinutes) {
        Map<LocalDate, List<LocalTime>> monthlyAvailability = new HashMap<>();
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        LocalDate date = startDate;

        while (!date.isAfter(endDate)) {
            List<LocalTime> slots = generateTimeSlots(startTime, endTime, intervalMinutes);
            monthlyAvailability.put(date, slots); // Assign availability for each day of the month
            removeConflictingSlots(doctor.getId(), date, slots); // Remove time slots that conflict with upcoming appointments
            date = date.plusDays(1);
        }
        return doctor.setMonthlyAvailability(monthlyAvailability, false);
    }

    /**
     * Removes time slots that conflict with the doctor's upcoming appointments on a specified date.
     * 
     * This method iterates through the provided list of time slots and compares each slot against the doctor's upcoming appointments.
     * If any time slot coincides with an existing appointment on the specified date, that slot is removed from the list of available slots.
     * 
     * @param doctorId The unique identifier of the doctor whose appointments are being checked for conflicts.
     * @param date The date on which the time slots should be checked for conflicts with existing appointments.
     * @param slots A list of {@link LocalTime} objects representing the available time slots to be checked.
     * 
     * @see Appointment
     * @see LocalDate
     * @see LocalTime
     */
    private void removeConflictingSlots(String doctorId, LocalDate date, List<LocalTime> slots) {
        List<Appointment> upcomingAppointments = viewUpcomingAppointments(doctorId);
    
        // Iterate over the provided time slots
        Iterator<LocalTime> iterator = slots.iterator();
        while (iterator.hasNext()) {
            LocalTime slot = iterator.next();
            for (Appointment appointment : upcomingAppointments) {
                if (appointment.getDateTime().toLocalDate().equals(date)
                        && appointment.getDateTime().toLocalTime().equals(slot)) {
                    // Remove the conflicting slot
                    iterator.remove();
                    break;
                }
            }
        }
    }
    
    // Get available time slots for a specific day
    /**
     * Retrieves the available time slots for a doctor on a specified date.
     * 
     * This method checks the doctor's monthly availability and returns the list of time slots for the given date. 
     * If no availability is found for the specified date, an empty list is returned.
     * 
     * @param doctor The {@link Doctor} whose availability is being viewed.
     * @param date The date for which the doctor's availability is to be retrieved.
     * @return A list of {@link LocalTime} objects representing the available time slots for the doctor on the specified date.
     *         If no availability exists for the date, an empty list is returned.
     * 
     * @see Doctor
     * @see LocalDate
     * @see LocalTime
     */
    public List<LocalTime> viewAvailability(Doctor doctor, LocalDate date) {
        return doctor.getMonthlyAvailability().getOrDefault(date, new ArrayList<>());
    }

    // View availability for a month
    /**
     * Retrieves the monthly availability of a doctor.
     * 
     * This method returns a map containing the doctor's availability for the entire month, where each entry consists of a date
     * (represented by {@link LocalDate}) and a corresponding list of available time slots for that date (represented by {@link LocalTime}).
     * 
     * @param doctor The {@link Doctor} whose monthly availability is being retrieved.
     * @return A map of {@link LocalDate} to a list of {@link LocalTime} representing the available time slots for each day of the month.
     * 
     * @see Doctor
     * @see LocalDate
     * @see LocalTime
     */
    public Map<LocalDate, List<LocalTime>> viewMonthlyAvailability(Doctor doctor) {
        return doctor.getMonthlyAvailability();
    }

    // Doctors can accept or decline appointment request
    // When patient schedules an appointment, by default the status is set as CONFIRMED
    // If decline, set status to CANCELED
    /**
     * Updates the status of an appointment to "CONFIRMED".
     * 
     * This method attempts to update the status of an appointment with the specified appointment ID to "CONFIRMED". 
     * If the update operation fails, an error message is returned. Otherwise, a success message is returned indicating the appointment 
     * status was updated successfully.
     * 
     * @param appointmentId The unique identifier of the appointment whose status is being updated.
     * @param status The status to be set for the appointment (this method specifically updates it to "CONFIRMED").
     * @return A message indicating whether the appointment status update was successful or failed.
     * 
     * @see AppointmentDB
     */
    public String updateAppointmentStatus(int appointmentId, String status) {
        if (!AppointmentDB.updateAppointmentStatus(appointmentId, status, false)) {
            return "Appointment status failed to be updated for ID: " + appointmentId;
        }
        return "Appointment status updated successfully for ID: " + appointmentId;    
    }

    public boolean isTimeSlotAvailable(String doctorId, String requestedDate, String requestedTime) {
        // Parse the requested date and time
        LocalDate date = LocalDate.parse(requestedDate);
        LocalTime time = LocalTime.parse(requestedTime);
        
        // Get the available slots for the doctor on that date
        Map<LocalDate, List<LocalTime>> monthlyAvailability = viewAvailableAppointmentSlots(doctorId);
        List<LocalTime> availableSlots = monthlyAvailability.get(date);
    
        // Check if the available slots are not null and if the requested time is available
        if (availableSlots != null && availableSlots.contains(time)) {
            return true;  // The requested time is available
        } else {
            return false; // The requested time is not available
        }
    }

    
    // Add-on: Accept all upcoming appointments
    /**
     * Accepts and updates the status of all upcoming appointments for a specified doctor to "CONFIRMED".
     * 
     * This method retrieves all upcoming appointments for the specified doctor and attempts to update each appointment's 
     * status to "CONFIRMED". If any update fails, an error message is returned. If all updates are successful, a success message 
     * is returned indicating that all appointment statuses were updated.
     * 
     * @param doctorId The unique identifier of the doctor whose upcoming appointments are to be accepted.
     * @return A message indicating whether the appointment status updates were successful or if any failed.
     * 
     * @see Appointment
     * @see AppointmentDB
     */
    public String acceptAllUpcomingAppointments(String doctorId) {
        List<Appointment> appointments = AppointmentDB.getUpcomingAppointments(doctorId);
        for (Appointment appointment : appointments) {
            if (!AppointmentDB.updateAppointmentStatus(appointment.getAppointmentId(), "CONFIRMED", false)) {
                return "Appointment status failed to be updated";
            }
        }
        return "Appointment status updated successfully";
    }

    // Add-on: Decline all upcoming appointments
    /**
     * Declines and updates the status of all upcoming appointments for a specified doctor to "CANCELED".
     * 
     * This method retrieves all upcoming appointments for the specified doctor and attempts to update each appointment's 
     * status to "CANCELED". If any update fails, an error message is returned. If all updates are successful, a success message 
     * is returned indicating that all appointment statuses were updated.
     * 
     * @param doctorId The unique identifier of the doctor whose upcoming appointments are to be declined.
     * @return A message indicating whether the appointment status updates were successful or if any failed.
     * 
     * @see Appointment
     * @see AppointmentDB
     */
    public String declineAllUpcomingAppointments(String doctorId) {
        List<Appointment> appointments = AppointmentDB.getUpcomingAppointments(doctorId);
        for (Appointment appointment : appointments) {
            if (!AppointmentDB.updateAppointmentStatus(appointment.getAppointmentId(), "CANCELED", false)) {
                return "Appointment status failed to be updated";
            }
        }
        return "Appointment status updated successfully";
    }

    // Doctors can view the list of their upcoming appointments including confirmed and pending
    /**
     * Retrieves all upcoming appointments for a specified doctor.
     * 
     * This method queries the database to retrieve a list of all upcoming appointments for the doctor identified by the given doctor ID.
     * The method returns the list of appointments, which can then be viewed or processed further.
     * 
     * @param doctorId The unique identifier of the doctor whose upcoming appointments are to be retrieved.
     * @return A list of {@link Appointment} objects representing all upcoming appointments for the specified doctor.
     *         If no upcoming appointments are found, the method returns an empty list.
     * 
     * @see Appointment
     * @see AppointmentDB
     */
    public List<Appointment> viewAllUpcomingAppointments(String doctorId) {
        return AppointmentDB.getAllUpcomingAppointments(doctorId);
    }

    // Doctors can view the list of their upcoming confirmed appointments
    /**
     * Retrieves the upcoming appointments for a specified doctor.
     * 
     * This method queries the database to retrieve a list of all upcoming appointments for the doctor identified by the given doctor ID.
     * The method returns the list of appointments, which can then be viewed or processed further.
     * 
     * @param doctorId The unique identifier of the doctor whose upcoming appointments are to be retrieved.
     * @return A list of {@link Appointment} objects representing the upcoming appointments for the specified doctor.
     *         If no upcoming appointments are found, the method returns an empty list.
     * 
     * @see Appointment
     * @see AppointmentDB
     */
    public List<Appointment> viewUpcomingAppointments(String doctorId) {
        return AppointmentDB.getUpcomingAppointments(doctorId);
    }

    // Doctors can view the list of their past appointments
    /**
     * Retrieves the past appointments for a specified doctor.
     * 
     * This method queries the database to retrieve a list of all past appointments for the doctor identified by the given doctor ID.
     * The method returns the list of appointments, which can be viewed or processed further.
     * 
     * @param doctorId The unique identifier of the doctor whose past appointments are to be retrieved.
     * @return A list of {@link Appointment} objects representing the past appointments for the specified doctor.
     *         If no past appointments are found, the method returns an empty list.
     * 
     * @see Appointment
     * @see AppointmentDB
     */
    public List<Appointment> viewPastAppointmentsByDoctorId(String doctorId) {
        return AppointmentDB.getPastAppointmentsByDoctorId(doctorId);
    }

    // After each appointment, the doctor will record the appointment outcome
    /**
     * Records the outcome of an appointment and updates the appointment status to "COMPLETED".
     * 
     * This method creates an {@link AppointmentOutcome} object with the provided details and attempts to add it to the database. 
     * Additionally, it updates the status of the corresponding appointment to "COMPLETED". If both operations are successful, 
     * a success message is returned. Otherwise, an error message is returned indicating the failure to add the appointment outcome.
     * 
     * @param appointmentId The unique identifier of the appointment for which the outcome is being recorded.
     * @param serviceType The type of service provided during the appointment.
     * @param medicationId A list of medication IDs prescribed during the appointment.
     * @param consultationNotes Notes from the consultation during the appointment.
     * @param status The status of the prescription (e.g., "PENDING", "COMPLETED").
     * @return A message indicating whether the appointment outcome was successfully recorded or if it failed.
     * 
     * @see AppointmentOutcome
     * @see AppointmentOutcomeDB
     * @see AppointmentDB
     */
    public String recordAppointmentOutcome(int appointmentId, String serviceType, List<String> medicationId,
            String consultationNotes, String status) {
        PrescriptionStatus prescriptionStatus = PrescriptionStatus.valueOf(status);
        AppointmentOutcome appointmentOutcome = new AppointmentOutcome(appointmentId, serviceType, medicationId,
                consultationNotes, prescriptionStatus);
        if (AppointmentOutcomeDB.addAppointmentOutcome(appointmentOutcome, false) &&
        AppointmentDB.updateAppointmentStatus(appointmentId, "COMPLETED", false)) {
            return "Appointment outcome added successfully for ID: " + appointmentOutcome.getAppointmentId();
        }
        return "Appointment outcome failed to be added";
    }

    // ----------------------------------------------------------------
    // Patient Appointment Management
    // ----------------------------------------------------------------
    // View available appointment slots with doctors
    /**
     * Retrieves the available appointment slots for a specified doctor.
     * 
     * This method checks if the specified user ID corresponds to a doctor. If the user is a doctor, it retrieves the doctor's 
     * monthly availability and returns the available appointment slots as a map, where the key is the date and the value is a 
     * list of available time slots for that day. If the user is not a doctor or the doctor is not found, the method returns null.
     * 
     * @param doctorId The unique identifier of the doctor whose available appointment slots are being retrieved.
     * @return A map of {@link LocalDate} to a list of {@link LocalTime} representing the available time slots for each day in the month,
     *         or null if the user is not a doctor or no availability is found.
     * 
     * @see Doctor
     * @see StaffDB
     * @see LocalDate
     * @see LocalTime
     */
    public Map<LocalDate, List<LocalTime>> viewAvailableAppointmentSlots(String doctorId) {
        // Get the object from StaffDB
        User staffMember = StaffDB.getUserById(doctorId);

        // Use instanceof to check if the staff member is a Doctor
        if (staffMember instanceof Doctor) {
            Doctor doctor = (Doctor) staffMember; // Safe downcasting
            // setMonthlyAvailability(doctor, LocalDate.parse("2024-11-01"), LocalTime.parse("08:00"),LocalTime.parse("18:00"), 30);
            return viewMonthlyAvailability(doctor);
        } else {
            return null;
        }
    }

    // Schedule appointments
    /**
     * Schedules a new appointment for a patient with a specified doctor at a given date and time.
     * 
     * This method creates a new appointment using the provided doctor ID, patient ID, date and time, and appointment status.
     * The appointment is added to the database. If the appointment is successfully scheduled, the method returns a success message;
     * otherwise, it returns a failure message.
     * 
     * @param doctorId The unique identifier of the doctor with whom the appointment is being scheduled.
     * @param patientId The unique identifier of the patient for whom the appointment is being scheduled.
     * @param dateTime The date and time of the appointment, formatted as "yyyy-MM-ddTHH:mm".
     * @param status The status of the appointment (e.g., "PENDING", "CONFIRMED", "CANCELLED").
     * @return A message indicating whether the appointment was successfully scheduled or if the operation failed.
     * 
     * @see Appointment
     * @see AppointmentDB
     * @see AppointmentStatus
     */
    public String scheduleAppointment(String doctorId, String patientId, String dateTime, String status) {
        // Parse the dateTime into LocalDateTime
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime);
    
        // Extract the date and time from the LocalDateTime object
        LocalDate date = localDateTime.toLocalDate();
        LocalTime time = localDateTime.toLocalTime();
    
        // Book the slot for the doctor
        String bookingResult = bookSlot(doctorId, date, time);
        if (!bookingResult.equals("Slot successfully booked.")) {
            return bookingResult; // Return the error if the slot is unavailable
        }

        // Convert status to AppointmentStatus
        AppointmentStatus appointmentStatus = AppointmentStatus.valueOf(status.toUpperCase());
    
        // Create the Appointment object
        Appointment appointment = new Appointment(doctorId, patientId, localDateTime, appointmentStatus);
    
        // Add the appointment to the database
        return AppointmentDB.addAppointment(appointment, false);
    }
    // Reschedule appointments
    /**
     * Reschedules an existing appointment to a new date and time.
     * 
     * This method updates the appointment's scheduled date and time by parsing the provided string into a {@link LocalDateTime} object. 
     * It then calls the database to update the appointment with the new date and time. If the appointment is successfully rescheduled, 
     * a success message is returned. Otherwise, an error message is returned indicating the failure.
     * 
     * @param appointmentId The unique identifier of the appointment to be rescheduled.
     * @param dateTime The new date and time for the appointment, formatted as "yyyy-MM-ddTHH:mm".
     * @return A message indicating whether the appointment was successfully rescheduled or if the operation failed.
     * 
     * @see Appointment
     * @see AppointmentDB
     * @see LocalDateTime
     */
    public String rescheduleAppointment(int appointmentId, String dateTime) {
        Appointment appointment = AppointmentDB.getAppointmentById(appointmentId);
        String doctorId = appointment.getDoctorId();

        LocalDateTime oldDateTime = appointment.getDateTime();
        unbookSlot(doctorId, oldDateTime.toLocalDate(), oldDateTime.toLocalTime());

        LocalDateTime newDateTime = LocalDateTime.parse(dateTime);
        bookSlot(doctorId, newDateTime.toLocalDate(), newDateTime.toLocalTime());
        return AppointmentDB.updateAppointment(appointmentId, newDateTime, false);
    }

    // Cancel appointment
    /**
     * Cancels an existing appointment.
     * 
     * This method deletes the appointment with the specified appointment ID from the database. 
     * If the appointment is successfully canceled, a success message is returned. Otherwise, an error message is returned indicating the failure.
     * 
     * @param appointmentId The unique identifier of the appointment to be canceled.
     * @return A message indicating whether the appointment was successfully canceled or if the operation failed.
     * 
     * @see Appointment
     * @see AppointmentDB
     */
    public String cancelAppointment(int appointmentId) {
        Appointment appointment = AppointmentDB.getAppointmentById(appointmentId);
        String doctorId = appointment.getDoctorId();
        LocalDateTime oldDateTime = appointment.getDateTime();
        unbookSlot(doctorId, oldDateTime.toLocalDate(), oldDateTime.toLocalTime());
        if (!AppointmentDB.updateAppointmentStatus(appointmentId, "CANCELED", false)) {
            return "Appointment status failed to be updated for ID: " + appointmentId;
        }
        return "Appointment status updated successfully for ID: " + appointmentId; 
    }

    // View status of scheduled appointment
    /**
     * Retrieves the details of an appointment, including its status.
     * 
     * This method fetches the entire appointment object, which includes various details such as the appointment status, date, 
     * time, doctor, and patient. It returns the appointment object corresponding to the specified appointment ID.
     * 
     * @param appointmentId The unique identifier of the appointment whose details are being retrieved.
     * @return The {@link Appointment} object corresponding to the given appointment ID, or null if no appointment is found.
     * 
     * @see Appointment
     * @see AppointmentDB
     */
    public Appointment viewAppointmentStatus(int appointmentId) {
        // Return entire appointment object as other apppointment details need to be
        // displayed too
        return AppointmentDB.getAppointmentById(appointmentId);
    }

    // Add-on: View all appointments by patientId
    /**
     * Retrieves all appointments for a specified patient.
     * 
     * This method queries the database to retrieve a list of all appointments associated with the specified patient ID.
     * It returns a list of appointments, which can be used to view or process further.
     * 
     * @param patientId The unique identifier of the patient whose appointments are being retrieved.
     * @return A list of {@link Appointment} objects representing the appointments for the specified patient.
     *         If no appointments are found, the method returns an empty list.
     * 
     * @see Appointment
     * @see AppointmentDB
     */
    public List<Appointment> viewAppointments(String patientId) {
        return AppointmentDB.getAppointmentsByPatientId(patientId);
    }

    // View scheduled appointments by patientId
    /**
     * Retrieves all scheduled appointments for a specified patient.
     * 
     * This method queries the database to retrieve a list of appointments that are currently scheduled for the specified patient ID.
     * It returns a list of scheduled appointments, which can be used to view or process further. The appointments are filtered 
     * to only include those that have not yet occurred.
     * 
     * @param patientId The unique identifier of the patient whose scheduled appointments are being retrieved.
     * @return A list of {@link Appointment} objects representing the scheduled appointments for the specified patient.
     *         If no scheduled appointments are found, the method returns an empty list.
     * 
     * @see Appointment
     * @see AppointmentDB
     */
    public List<Appointment> viewScheduledAppointments(String patientId) {
        return AppointmentDB.getScheduledAppointments(patientId);
    }

    // View past appointments by patientId
    /**
     * Retrieves all past appointments for a specified patient.
     * 
     * This method queries the database to retrieve a list of past appointments for the specified patient ID.
     * It returns a list of appointments that have already occurred, based on the current date. 
     * If no past appointments are found, it returns an empty list.
     * 
     * @param patientId The unique identifier of the patient whose past appointments are being retrieved.
     * @return A list of {@link Appointment} objects representing the past appointments for the specified patient.
     *         If no past appointments are found, the method returns an empty list.
     * 
     * @see Appointment
     * @see AppointmentDB
     */
    public List<Appointment> viewPastAppointmentsByPatientId(String patientId) {
        return AppointmentDB.getPastAppointmentsByPatientId(patientId);
    }
    

    /**
     * Retrieves the details of an appointment by its unique ID.
     *
     * @param apptId the unique ID of the appointment to be viewed
     * @return the {@link Appointment} object corresponding to the specified ID, 
     *         or {@code null} if no appointment is found with the given ID
     */
    public Appointment viewAppointmentById(int apptId) {
        return AppointmentDB.getAppointmentById(apptId);
    }

    // View appointment outcome records of past appointments
    /**
     * Retrieves the outcome details for a specified appointment.
     * 
     * This method queries the database to retrieve the outcome details of an appointment by its ID. The outcome includes
     * information such as the service type, medications prescribed, consultation notes, and the prescription status.
     * If no outcome is found for the given appointment ID, the method returns null.
     * 
     * @param appointmentId The unique identifier of the appointment whose outcome details are being retrieved.
     * @return The {@link AppointmentOutcome} object representing the outcome of the specified appointment, 
     *         or null if no outcome is found.
     * 
     * @see AppointmentOutcome
     * @see AppointmentOutcomeDB
     */
    public AppointmentOutcome viewAppointmentOutcome(int appointmentId) {
        return AppointmentOutcomeDB.getAppointmentOutcome(appointmentId);
    }

    /**
     * Updates the billing status of a specific appointment.
     *
     * @param appointmentId the unique ID of the appointment whose billing status needs to be updated
     * @param status the new billing status to be applied
     * @param init a flag indicating whether the update is part of an initialization process (always passed as {@code false} in this implementation)
     * @return a message indicating the result of the operation from the {@link AppointmentOutcomeDB#updateBillingStatus} method
     */
    public String payBill(int appointmentId, BillingStatus status, Boolean init) {
        return AppointmentOutcomeDB.updateBillingStatus(appointmentId, status, false);
    }

    // ----------------------------------------------------------------
    // Pharmacist Appointment Management
    // ----------------------------------------------------------------
    /**
     * Retrieves all appointment outcomes.
     * 
     * This method queries the database to retrieve a map of all appointment outcomes. Each outcome is identified by the
     * appointment ID, and the map contains the appointment outcomes associated with all appointments in the system.
     * 
     * @return A {@link HashMap} where the keys are appointment IDs (Integer) and the values are {@link AppointmentOutcome} 
     *         objects representing the outcomes for each appointment. If no outcomes are found, the map will be empty.
     * 
     * @see AppointmentOutcome
     * @see AppointmentOutcomeDB
     */
    public HashMap<Integer, AppointmentOutcome> viewAllAppointmentOutcomes() {
        return AppointmentOutcomeDB.getAllAppointmentOutcomes();
    }
}
