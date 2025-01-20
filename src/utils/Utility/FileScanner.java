package utils.Utility;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A utility class for scanning files within a folder.
 * <p>
 * This class provides a method to scan a specific folder for CSV files and return 
 * their absolute paths as an array of strings.
 */
public class FileScanner {
    /**
     * Scans a folder for CSV files and returns their absolute paths.
     * <p>
     * This method checks the specified folder for any CSV files. It ensures the folder exists 
     * and is a directory, then filters for regular CSV files. If CSV files are found, their 
     * absolute paths are collected into an array and returned.
     * 
     * @return an array of strings containing the absolute paths of CSV files in the folder, 
     *         or {@code null} if no CSV files are found or an error occurs
     */
    public static String[] scanFilesInFolder() {
        String folderPath = "Hospital/src/resources/Upload";
        // Resolve relative path into an absolute path for better clarity
        Path path = Paths.get(folderPath).toAbsolutePath();
        // Ensure the folder exists and is a directory
        if (!Files.exists(path)) {
            return null;
        }

        if (!Files.isDirectory(path)) {
            return null;
        }

        try {
            // Walk through the directory and filter for CSV files
            List<String> csvFilesList = Files.walk(path)
                    .filter(Files::isRegularFile) // Only regular files
                    .filter(file -> file.toString().endsWith(".csv")) // Filter for CSV files
                    .map(file -> file.toAbsolutePath().toString()) // Map to absolute path
                    .collect(Collectors.toList()); // Collect into a list

            // If no CSV files found, return null
            if (csvFilesList.isEmpty()) {
                return null;
            }

            // Convert the list to an array and return it
            return csvFilesList.toArray(new String[0]);

        } catch (IOException e) {
            return null;
        }
    }
}
