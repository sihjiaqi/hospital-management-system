package utils.Utility;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A utility class for reading and processing CSV files.
 * <p>
 * This class provides helper methods to read CSV data and convert it into various formats, 
 * such as lists or maps, for easier processing.
 */
public class CSVReader {
    // Helper function to read CSV files
    /**
     * Reads a CSV file and returns its contents as a list of string arrays.
     * <p>
     * This method reads a CSV file line by line, processes the rows, and splits them into 
     * string arrays. The first row is treated as the header, and the remaining rows are 
     * parsed into columns.
     * 
     * @param path the path to the CSV file
     * @return a list of string arrays, where each array represents a row in the CSV file
     */
    public static List<String[]> csvReaderUtil(String path) {
        List<String[]> values = new ArrayList<>();
        int expectedColumns = 0; // Variable to store the number of columns based on the header

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            boolean isHeader = true;

            // Read the file line by line
            while ((line = br.readLine()) != null) {
                // Skip the header line
                if (isHeader) {
                    // Split the header line by commas to count columns
                    String[] header = line.split(",");
                    expectedColumns = header.length; // Set expected columns from the header
                    isHeader = false; // After processing the header, process the data rows
                    continue;
                }

                // Split the current data line by commas
                String[] splitLine = line.split(",");

                if (splitLine.length < expectedColumns) {
                    String[] expandedLine = Arrays.copyOf(splitLine, expectedColumns);

                    // Fill any missing columns with null values
                    for (int i = splitLine.length; i < expectedColumns; i++) {
                        expandedLine[i] = null;
                    }

                    values.add(expandedLine);
                } else {
                    values.add(splitLine);
                }
            }
        } catch (IOException e) {
            // Better error handling (consider logging)
            System.err.println("Error reading the CSV file: " + e.getMessage());
        }
        return values;
    }

    /**
     * Loads doctor availability from a CSV file into a nested map.
     * <p>
     * This method reads a CSV file where each row contains a staff ID, a date, and a list of times. 
     * It processes the file and stores the data in a nested map, where the outer map's key is the 
     * staff ID, the inner map's key is a {@link LocalDate}, and the value is a list of {@link LocalTime}.
     * 
     * @param filePath the path to the CSV file containing doctor availability data
     * @return a nested map where the outer map's key is the staff ID, the inner map's key is a date, 
     *         and the value is a list of available times on that date
     */
    public static Map<String, Map<LocalDate, List<LocalTime>>> loadAvailabilityFromCSV(String filePath) {
        Map<String, Map<LocalDate, List<LocalTime>>> doctorAvailability = new HashMap<>();
    
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine(); // Skip header
    
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 3); // Split into Staff ID, Date, and Times
                String staffId = parts[0];
                LocalDate date = LocalDate.parse(parts[1]);
                List<LocalTime> times = List.of(parts[2].split(",")).stream()
                        .map(LocalTime::parse)
                        .collect(Collectors.toList());
    
                doctorAvailability
                        .computeIfAbsent(staffId, k -> new HashMap<>())
                        .put(date, times);
            }
        } catch (IOException e) {
            System.err.println("Error reading availability CSV: " + e.getMessage());
        }
    
        return doctorAvailability;
    }    
}
