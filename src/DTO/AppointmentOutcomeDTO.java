package DTO;
import java.util.List;

/**
 * Data Transfer Object (DTO) representing the outcome of an appointment.
 * This class contains details related to the service provided during the appointment,
 * prescribed medications, consultation notes, and the outcome status.
 */
public class AppointmentOutcomeDTO {
    private String serviceType;
    private List<String> medicationId;
    private String consultationNotes;
    private String status;

    /**
     * Retrieves the type of service provided during the appointment.
     *
     * @return the service type
     */
    public String getServiceType() {
        return serviceType;
    }

    /**
     * Retrieves the list of medication IDs prescribed during the appointment.
     *
     * @return the list of medication IDs
     */
    public List<String> getMedicationId() {
        return medicationId;
    }

    /**
     * Retrieves the consultation notes recorded during the appointment.
     *
     * @return the consultation notes
     */
    public String getConsultationNotes() {
        return consultationNotes;
    }

    /**
     * Retrieves the status of the appointment outcome.
     *
     * @return the status of the appointment outcome
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the type of service provided during the appointment.
     *
     * @param serviceType the service type to set
     */
    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    /**
     * Sets the list of medication IDs prescribed during the appointment.
     *
     * @param medicationId the list of medication IDs to set
     */
    public void setMedicationId(List<String> medicationId) {
        this.medicationId = medicationId;
    }

    /**
     * Sets the consultation notes recorded during the appointment.
     *
     * @param consultationNotes the consultation notes to set
     */
    public void setConsultationNotes(String consultationNotes) {
        this.consultationNotes = consultationNotes;
    }

    /**
     * Sets the status of the appointment outcome.
     *
     * @param status the status to set for the appointment outcome
     */
    public void setStatus(String status) {
        this.status = status;
    }
}
