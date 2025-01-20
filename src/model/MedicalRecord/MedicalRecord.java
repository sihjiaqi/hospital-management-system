package model.MedicalRecord;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import model.ICSVExportable;

/**
 * Represents a patient's medical record, including their diagnoses,
 * prescriptions, and treatment plans.
 * Implements ICSVExportable for exporting the record to a CSV format.
 */
public class MedicalRecord implements ICSVExportable {
    private String patientId;
    private List<String> diagnoses;
    private List<String> prescriptions;
    private List<String> treatmentPlans;

    /**
     * Constructor for creating a new MedicalRecord with the given patient ID,
     * diagnoses, prescriptions, and treatment plans.
     * 
     * @param patientId      The unique identifier for the patient.
     * @param diagnoses      The list of diagnoses associated with the patient.
     * @param prescriptions  The list of prescriptions given to the patient.
     * @param treatmentPlans The list of treatment plans for the patient.
     */
    public MedicalRecord(String patientId, List<String> diagnoses, List<String> prescriptions,
            List<String> treatmentPlans) {
        this.patientId = patientId;
        this.diagnoses = new ArrayList<>(diagnoses);
        this.prescriptions = new ArrayList<>(prescriptions);
        this.treatmentPlans = new ArrayList<>(treatmentPlans);
    }

    /**
     * Gets the patient ID.
     * 
     * @return The patient ID.
     */
    public String getPatientId() {
        return patientId;
    }

    /**
     * Gets the list of diagnoses.
     * 
     * @return The list of diagnoses.
     */
    public List<String> getDiagnoses() {
        return diagnoses;
    }

    /**
     * Gets the list of prescriptions.
     * 
     * @return The list of prescriptions.
     */
    public List<String> getPrescriptions() {
        return prescriptions;
    }

    /**
     * Gets the list of treatment plans.
     * 
     * @return The list of treatment plans.
     */
    public List<String> getTreatmentPlans() {
        return treatmentPlans;
    }

    // Setter method for updating the patientId
    public void setPatientId(String newPatientId) {
        if (newPatientId != null && !newPatientId.isEmpty()) {
            this.patientId = newPatientId; // Update the patientId field
        } else {
            throw new IllegalArgumentException("Invalid patient ID");
        }
    }

    /**
     * Adds a list of diagnoses to the current record, ensuring no duplicates.
     * 
     * @param diagnosis The list of diagnoses to be added.
     */
    public void addDiagnosis(List<String> diagnosis) {
        // Iterate over each diagnosis in the input list
        for (String item : diagnosis) {
            // Add the item to this.diagnoses if it's not already present
            if (!this.diagnoses.contains(item)) {
                this.diagnoses.add(item);
            }
        }
    }

    /**
     * Adds a list of prescriptions to the current record, ensuring no duplicates.
     * 
     * @param prescription The list of prescriptions to be added.
     */
    public void addPrescription(List<String> prescription) {
        // Iterate over each diagnosis in the input list
        for (String item : prescription) {
            // Add the item to this.diagnoses if it's not already present
            if (!this.prescriptions.contains(item)) {
                this.prescriptions.add(item);
            }
        }
    }

    /**
     * Adds a list of treatment plans to the current record, ensuring no duplicates.
     * 
     * @param treatmentPlan The list of treatment plans to be added.
     */
    public void addTreatmentPlan(List<String> treatmentPlan) {
        // Iterate over each treatment plan in the input list
        for (String item : treatmentPlan) {
            // Add the item to this.treatmentPlans if it's not already present
            if (!this.treatmentPlans.contains(item)) {
                this.treatmentPlans.add(item);
            }
        }
    }

    /**
     * Displays the patient's medical record details, including diagnoses,
     * prescriptions, and treatment plans.
     */
    public void display() {
        System.out.println("Patient ID: " + patientId);
        System.out.println("Diagnoses:");
        for (String diagnosis : diagnoses) {
            System.out.println(" - " + diagnosis);
        }
        System.out.println("Treatment Plans:");
        for (String treatmentPlan : treatmentPlans) {
            System.out.println(" - " + treatmentPlan);
        }
        System.out.println("Prescriptions:");
        for (String prescription : prescriptions) {
            System.out.println(" - " + prescription);
        }
    }

    /**
     * Deletes a treatment plan by its index in the list.
     * 
     * @param id The index of the treatment plan to be removed.
     */
    public void deleteTreatmentPlansbyId(int id) {
        if (id >= 0 && id < treatmentPlans.size()) {
            treatmentPlans.remove(id);
        }
    }

    /**
     * Prints the list of current treatment plans for the patient.
     */
    public void printTreatmentPlans() {
        System.out.println("Current treatment plans for patient " + patientId + ":");
        if (treatmentPlans.isEmpty()) {
            System.out.println("No treatment plans found.");
        } else {
            for (int i = 0; i < treatmentPlans.size(); i++) {
                System.out.println((i + 1) + ". " + treatmentPlans.get(i));
            }
        }
    }

    /**
     * Deletes one or more diagnoses from the list based on the input.
     * 
     * @param diagnosesToDelete A string containing diagnoses to be deleted,
     *                          separated by semicolons.
     */
    public void deleteDiagnoses(String diagnosesToDelete) {
        if (diagnosesToDelete.isEmpty()) {
            // If the input is an empty string, remove all diagnoses
            diagnoses.clear();
            System.out.println("All diagnoses have been deleted.");
        } else {
            // Split the user input into a list of diagnoses
            List<String> diagnosesListToDelete = Arrays.asList(diagnosesToDelete.split(";"));

            // Remove each diagnosis from the list
            for (String diagnosisToDelete : diagnosesListToDelete) {
                if (diagnoses.contains(diagnosisToDelete)) {
                    diagnoses.remove(diagnosisToDelete);
                    System.out.println("Diagnosis removed: " + diagnosisToDelete);
                } else {
                    System.out.println("Diagnosis not found: " + diagnosisToDelete);
                }
            }
        }
    }

    /**
     * Deletes a diagnosis by its index in the list.
     * 
     * @param id The index of the diagnosis to be removed.
     */
    public void deleteDiagnosisbyId(int id) {
        if (id >= 0 && id < diagnoses.size()) {
            diagnoses.remove(id);
        }
    }

    /**
     * Deletes one or more prescriptions from the list based on the input.
     * 
     * @param prescriptionsToDelete A string containing prescriptions to be deleted,
     *                              separated by semicolons.
     */
    public void deletePrescriptions(String prescriptionsToDelete) {
        if (prescriptionsToDelete.isEmpty()) {
            // If the input is an empty string, remove all prescriptions
            prescriptions.clear();
            System.out.println("All prescriptions have been deleted.");
        } else {
            // Split the user input into a list of prescriptions
            List<String> prescriptionsListToDelete = Arrays.asList(prescriptionsToDelete.split(";"));

            // Remove each prescription from the list
            for (String prescriptionToDelete : prescriptionsListToDelete) {
                if (prescriptions.contains(prescriptionToDelete)) {
                    prescriptions.remove(prescriptionToDelete);
                    System.out.println("Prescription removed: " + prescriptionToDelete);
                } else {
                    System.out.println("Prescription not found: " + prescriptionToDelete);
                }
            }
        }
    }

    /**
     * Deletes a prescription by its index in the list.
     * 
     * @param id The index of the prescription to be removed.
     */
    public void deletePrescriptionbyId(int id) {
        if (id >= 0 && id < prescriptions.size()) {
            prescriptions.remove(id);
        }
    }

    /**
     * Deletes one or more treatment plans from the list based on the input.
     * 
     * @param treatmentPlansToDelete A string containing treatment plans to be
     *                               deleted, separated by semicolons.
     */
    public void deleteTreatmentPlans(String treatmentPlansToDelete) {
        if (treatmentPlansToDelete.isEmpty()) {
            // If the input is an empty string, remove all treatment plans
            treatmentPlans.clear();
            System.out.println("All treatment plans have been deleted.");
        } else {
            // Split the user input into a list of treatment plans
            List<String> treatmentPlansListToDelete = Arrays.asList(treatmentPlansToDelete.split(";"));

            // Remove each treatment plan from the list
            for (String treatmentPlanToDelete : treatmentPlansListToDelete) {
                if (treatmentPlans.contains(treatmentPlanToDelete)) {
                    treatmentPlans.remove(treatmentPlanToDelete);
                    System.out.println("Treatment plan removed: " + treatmentPlanToDelete);
                } else {
                    System.out.println("Treatment plan not found: " + treatmentPlanToDelete);
                }
            }
        }
    }

    /**
     * Exports the medical record as a CSV record with patient ID, diagnoses,
     * prescriptions, and treatment plans.
     * 
     * @return A string array representing the medical record in CSV format.
     */
    @Override
    public String[] toCSVRecord() {
        return new String[] {
                patientId != null ? patientId : "N/A",
                diagnoses != null && !diagnoses.isEmpty() ? String.join(";", diagnoses) : "None",
                prescriptions != null && !prescriptions.isEmpty() ? String.join(";", prescriptions) : "None",
                treatmentPlans != null && !treatmentPlans.isEmpty() ? String.join(";", treatmentPlans) : "None"
        };
    }

    /**
     * Returns a string representation of the medical record, including patient ID,
     * diagnoses, prescriptions, and treatment plans.
     * 
     * @return A string representation of the medical record.
     */
    @Override
    public String toString() {
        // Join the lists into a single string with each item separated by a semicolon
        String diagnosesStr = String.join("; ", diagnoses);
        String prescriptionsStr = String.join("; ", prescriptions);
        String treatmentPlansStr = String.join("; ", treatmentPlans);

        return "Patient ID: " + patientId + "\n" +
                "Diagnoses: " + diagnosesStr + "\n" +
                "Prescriptions: " + prescriptionsStr + "\n" +
                "Treatment Plans: " + treatmentPlansStr;
    }

}
