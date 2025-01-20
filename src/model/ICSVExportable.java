package model;

/**
 * This interface defines the contract for classes that need to export their data as a CSV record.
 * <p>
 * Any class that implements this interface must provide an implementation for the {@link #toCSVRecord()} method,
 * which converts the object's data into a format suitable for CSV export.
 * </p>
 */
public interface ICSVExportable {
    /**
     * Converts the object into a CSV record.
     * 
     * @return An array of strings representing the CSV fields.
     */
    String[] toCSVRecord();
}