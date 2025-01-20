package model.Schedule;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/**
 * Represents an appointment scheduled for a doctor.
 */
class Appointment {
    private LocalDate date;
    private String patientName;
    private String reason;

    /**
     * Creates a new appointment.
     * 
     * @param date The date of the appointment.
     * @param patientName The name of the patient.
     * @param reason The reason for the appointment.
     */
    public Appointment(LocalDate date, String patientName, String reason) {
        this.date = date;
        this.patientName = patientName;
        this.reason = reason;
    }

    /**
     * Returns the date of the appointment.
     * 
     * @return The date of the appointment.
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Returns the name of the patient.
     * 
     * @return The name of the patient.
     */
    public String getPatientName() {
        return patientName;
    }

    /**
     * Returns the reason for the appointment.
     * 
     * @return The reason for the appointment.
     */
    public String getReason() {
        return reason;
    }

    /**
     * Returns a string representation of the appointment.
     * 
     * @return A string containing the patient name and reason for the appointment.
     */
    @Override
    public String toString() {
        return patientName + " (" + reason + ")";
    }
}

/**
 * Represents a doctor's schedule containing a list of appointments.
 */
class DoctorSchedule {
    private String doctorName;
    private List<Appointment> appointments;

    /**
     * Creates a schedule for the given doctor.
     * 
     * @param doctorName The name of the doctor.
     */
    public DoctorSchedule(String doctorName) {
        this.doctorName = doctorName;
        this.appointments = new ArrayList<>();
    }

    /**
     * Adds an appointment to the doctor's schedule.
     * 
     * @param appointment The appointment to be added.
     */
    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
    }

    /**
     * Retrieves the list of appointments for a specific month and year.
     * 
     * @param year The year of the appointments.
     * @param month The month of the appointments.
     * @return A list of appointments for the specified month and year.
     */
    public List<Appointment> getAppointmentsForMonth(int year, Month month) {
        List<Appointment> monthlyAppointments = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getDate().getYear() == year && appointment.getDate().getMonth() == month) {
                monthlyAppointments.add(appointment);
            }
        }
        return monthlyAppointments;
    }

    /**
     * Returns the name of the doctor.
     * 
     * @return The name of the doctor.
     */
    public String getDoctorName() {
        return doctorName;
    }
}

/**
 * A class that prints the calendar for a given month and displays appointments for a doctor.
 */
public class CalendarPrinter {

    /**
     * Main method to run the program.
     * This method interacts with the user to display the calendar for the current or selected month
     * and allows navigation to the next or previous month.
     * 
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        LocalDate currentDate = LocalDate.now(); // Start with the current date

        // Sample doctor schedule with more appointments
        DoctorSchedule doctorSchedule = new DoctorSchedule("Dr. Alice Smith");
        
        // Appointments for November 2024
        doctorSchedule.addAppointment(new Appointment(LocalDate.of(2024, 11, 5), "John Doe", "Routine Checkup"));
        doctorSchedule.addAppointment(new Appointment(LocalDate.of(2024, 11, 10), "Jane Roe", "Follow-up Visit"));
        doctorSchedule.addAppointment(new Appointment(LocalDate.of(2024, 11, 15), "Tom Brown", "Consultation"));
        doctorSchedule.addAppointment(new Appointment(LocalDate.of(2024, 11, 20), "Emily White", "Initial Consultation"));
        doctorSchedule.addAppointment(new Appointment(LocalDate.of(2024, 11, 25), "Michael Green", "Physical Exam"));

        // Appointments for October 2024
        doctorSchedule.addAppointment(new Appointment(LocalDate.of(2024, 10, 1), "Alice Johnson", "Annual Checkup"));
        doctorSchedule.addAppointment(new Appointment(LocalDate.of(2024, 10, 12), "Bob Smith", "Vaccination"));
        doctorSchedule.addAppointment(new Appointment(LocalDate.of(2024, 10, 15), "Charlie Brown", "Blood Test"));

        // Appointments for December 2024
        doctorSchedule.addAppointment(new Appointment(LocalDate.of(2024, 12, 2), "David White", "X-Ray Review"));
        doctorSchedule.addAppointment(new Appointment(LocalDate.of(2024, 12, 5), "Ella Green", "Skin Consultation"));
        doctorSchedule.addAppointment(new Appointment(LocalDate.of(2024, 12, 15), "Fiona Black", "Follow-up Visit"));

        // Appointments for January 2025
        doctorSchedule.addAppointment(new Appointment(LocalDate.of(2025, 1, 10), "George Blue", "Dental Checkup"));
        doctorSchedule.addAppointment(new Appointment(LocalDate.of(2025, 1, 20), "Hannah Red", "Eye Examination"));

        while (true) {
            printCalendar(currentDate.getYear(), currentDate.getMonth(), doctorSchedule);

            System.out.println("\nOptions: ");
            System.out.println("1. Next Month");
            System.out.println("2. Previous Month");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1: // Next Month
                    currentDate = currentDate.plusMonths(1);
                    break;
                case 2: // Previous Month
                    currentDate = currentDate.minusMonths(1);
                    break;
                case 3: // Exit
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    /**
     * Prints the calendar for the specified year and month, including appointments for that month.
     * 
     * @param year The year to display the calendar for.
     * @param month The month to display the calendar for.
     * @param doctorSchedule The doctor's schedule containing appointments.
     */
    public static void printCalendar(int year, Month month, DoctorSchedule doctorSchedule) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDay = yearMonth.atDay(1);
        int lengthOfMonth = yearMonth.lengthOfMonth();

        // Print the header
        System.out.println("    " + month.getDisplayName(TextStyle.FULL, Locale.getDefault()) + " " + year);
        System.out.println("Su Mo Tu We Th Fr Sa");

        // Print leading spaces for the first week
        int startDay = firstDay.getDayOfWeek().getValue();
        for (int i = 1; i <= startDay % 7; i++) {
            System.out.print("   "); // Three spaces for empty days
        }

        // Print the days of the month
        for (int day = 1; day <= lengthOfMonth; day++) {
            LocalDate current = LocalDate.of(year, month, day);
            String display = String.format("%2d", day);

            // Check for appointments on the current day
            List<Appointment> appointments = doctorSchedule.getAppointmentsForMonth(year, month);
            for (Appointment appointment : appointments) {
                if (appointment.getDate().equals(current)) {
                    display += "*"; // Indicate an appointment with an asterisk
                }
            }

            System.out.print(display + " ");
            if ((startDay + day) % 7 == 0) {
                System.out.println(); // New line after Saturday
            }
        }
        System.out.println(); // Final new line at the end of the calendar

        // Print the appointments for the month
        System.out.println("\nAppointments for " + month.getDisplayName(TextStyle.FULL, Locale.getDefault()) + " " + year + ":");
        for (Appointment appointment : doctorSchedule.getAppointmentsForMonth(year, month)) {
            System.out.println(appointment);
        }
    }
}
