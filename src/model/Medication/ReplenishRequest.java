package model.Medication;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import model.ICSVExportable;

/**
 * Represents a replenishment request for a medication, including details 
 * about the request, such as the staff member initiating it, the medication 
 * involved, and the request status.
 */
public class ReplenishRequest implements ICSVExportable {

    /**
     * Enumeration of possible statuses for a replenishment request.
     */
    public enum ReplenishRequestStatus {
        /**
         * The replenishment request has been approved.
         * 
         * This status indicates that the request for replenishment has been reviewed and approved by the 
         * responsible authority.
         */
        APPROVED,

        /**
         * The replenishment request is pending review or action.
         * 
         * This is the default status, indicating that the request has not yet been processed or approved.
         */
        PENDING, // default

        /**
         * The replenishment request has been denied.
         * 
         * This status indicates that the replenishment request was reviewed and rejected due to various reasons.
         */
        DENIED
    }

    private static int requestIdCounter = 0;
    private final int requestId;
    private String staffId;
    private String medicationId;
    private ReplenishRequestStatus status;
    private int requestAmount;
    private LocalDate requestDate; // New date property

    /**
     * Constructs a new ReplenishRequest.
     *
     * @param staffId       the ID of the staff member making the request.
     * @param medicationId  the ID of the medication being requested.
     * @param status        the current status of the request.
     * @param requestAmount the amount of medication requested.
     * @param requestDate   the date when the request was made.
     */
    public ReplenishRequest(String staffId,
            String medicationId,
            ReplenishRequestStatus status,
            int requestAmount,
            LocalDate requestDate) {
        this.requestId = ++requestIdCounter;
        this.staffId = staffId;
        this.medicationId = medicationId;
        this.status = status;
        this.requestAmount = requestAmount;
        this.requestDate = requestDate; // Initialize the date
    }

    /**
     * Returns the request date as a string formatted as "yyyy-MM-dd".
     *
     * @return a string representation of the request date.
     */
    public String getRequestDateAsString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return requestDate.format(formatter); // Returns in the format dd-MM-yyyy
    }

    /**
     * Gets the amount of medication requested.
     *
     * @return the amount of medication requested.
     */
    public int getAmount() {
        return requestAmount;
    }

    /**
     * Gets the unique request ID.
     *
     * @return the unique request ID.
     */
    public int getRequestId() {
        return requestId;
    }

    /**
     * Gets the ID of the staff member making the request.
     *
     * @return the staff member ID.
     */
    public String getStaffId() {
        return staffId;
    }

    /**
     * Gets the ID of the medication being requested.
     *
     * @return the medication ID.
     */
    public String getMedicationId() {
        return medicationId;
    }

    /**
     * Gets the current status of the request.
     *
     * @return the status of the request.
     */
    public ReplenishRequestStatus getStatus() {
        return status;
    }

    /**
     * Gets the date when the request was made.
     *
     * @return the date of the request.
     */
    public LocalDate getRequestDate() {
        return requestDate;
    }

    /**
     * Sets the staff member ID for the request.
     *
     * @param staffId the new staff member ID.
     */
    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }


    /**
     * Sets the medication ID for the request.
     *
     * @param medicationId the new medication ID.
     */
    public void setMedicationId(String medicationId) {
        this.medicationId = medicationId;
    }

    /**
     * Sets the status of the replenishment request.
     *
     * @param status the new status of the request.
     */
    public void setStatus(ReplenishRequestStatus status) {
        this.status = status;
    }

    /**
     * Sets the date when the request was made.
     *
     * @param requestDate the new request date.
     */
    public void setRequestDate(LocalDate requestDate) {
        this.requestDate = requestDate;
    }

    /**
     * Converts the ReplenishRequest to a CSV record format.
     * This includes the request ID, staff ID, medication ID, status, 
     * requested amount, and request date.
     *
     * @return an array of strings representing the CSV record.
     */
    @Override
    public String[] toCSVRecord() {
        return new String[] {
                String.valueOf(getRequestId()),
                getStaffId(),
                getMedicationId(),
                getStatus().toString(), 
                String.valueOf(getAmount()),
                getRequestDate().toString()
        };
    }

}
