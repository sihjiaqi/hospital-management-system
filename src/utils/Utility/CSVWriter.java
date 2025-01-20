package utils.Utility;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.ICSVExportable;

/**
 * Utility class for writing CSV files related to staff availability and general
 * records.
 * Provides methods for appending availability data and exporting data to CSV in
 * a structured format.
 */
public class CSVWriter {
    /**
     * Writes a staff member's availability data to a CSV file. If the file already
     * contains
     * data for the given staff ID, the existing data is preserved, and only the new
     * availability
     * is added.
     *
     * @param filePath            The path of the CSV file where the availability
     *                            data will be saved.
     * @param staffId             The unique identifier of the staff member.
     * @param monthlyAvailability A map where each entry represents a date and the
     *                            list of times
     *                            the staff member is available on that date.
     */
    public static void writeAvailabilityToCSV(String filePath, String staffId,
            Map<LocalDate, List<LocalTime>> monthlyAvailability) {
        File csvFile = new File(filePath);
        List<String> updatedLines = new ArrayList<>();

        // Check if the file exists and read it to avoid duplicate headers
        if (csvFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
                String line;
                boolean headerSkipped = false;
                while ((line = reader.readLine()) != null) {
                    // Skip the header only once
                    if (!headerSkipped) {
                        headerSkipped = true;
                        continue; // Skip the header line
                    }
                    updatedLines.add(line); // Add other existing lines
                }
            } catch (IOException e) {
                System.err.println("Error reading from availability CSV: " + e.getMessage());
            }
        }

        // Add the new availability for the given staff ID
        if (monthlyAvailability != null) {
            for (Map.Entry<LocalDate, List<LocalTime>> entry : monthlyAvailability.entrySet()) {
                LocalDate date = entry.getKey();
                List<LocalTime> times = entry.getValue();
                String timeString = (times != null)
                        ? String.join(",", times.stream().map(LocalTime::toString).toList())
                        : "";
                updatedLines.add(staffId + "," + date + "," + timeString);
            }
        }

        // Write the updated content, including the header only once
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile))) {
            // Write the header once
            writer.write("Staff ID,Date,Times\n");

            // Write the rest of the data
            for (String line : updatedLines) {
                writer.write(line + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error writing to availability CSV: " + e.getMessage());
        }
    }

    /**
     * Writes a collection of records to a CSV file. The records should be in the
     * form of a
     * {@link HashMap} with the key being some identifier and the value being an
     * object that
     * implements the {@link ICSVExportable} interface.
     * Each object in the map will be exported using its
     * {@link ICSVExportable#toCSVRecord()} method.
     *
     * @param path    The path where the CSV file will be saved.
     * @param records A map of records to write to the CSV file.
     * @param headers An array of strings representing the column headers for the
     *                CSV file.
     */
    public static void writeCSV(String path, HashMap<?, ?> records, String[] headers) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            // Write the header to the file
            writer.write(String.join(",", headers));
            writer.newLine();

            // Iterate over the records
            for (Map.Entry<?, ?> entry : records.entrySet()) {
                Object value = entry.getValue();

                if (value instanceof ICSVExportable) {
                    String[] record = ((ICSVExportable) value).toCSVRecord();
                    writer.write(String.join(",", record));
                    writer.newLine();
                } else {
                    // Log and continue if the type is not supported
                    System.err.println("Unknown record type: " + value.getClass().getName());
                }
            }
        } catch (IOException e) {
            System.err.println("Error writing the CSV file: " + e.getMessage());
        }
    }

}
