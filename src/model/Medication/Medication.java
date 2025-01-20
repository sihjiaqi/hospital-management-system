package model.Medication;

import model.ICSVExportable;

/**
 * Represents a medication item with details such as its name, stock levels, and price.
 * This class implements the ICSVExportable interface to allow for CSV export functionality.
 */
public class Medication implements ICSVExportable {
    private String name;
    private int initialStock;
    private int currentStock;
    private int lowStockLevelAlert;
    private double price;

    /**
     * Constructor for creating a new Medication object with initial stock, low stock alert level, and price.
     * The current stock is set to the initial stock value.
     * 
     * @param name The name of the medication.
     * @param initialStock The initial stock of the medication.
     * @param lowStockLevelAlert The stock level at which an alert is triggered.
     * @param price The price of the medication.
     */
    public Medication(String name, int initialStock, int lowStockLevelAlert, double price) {
        this.name = name;
        this.initialStock = initialStock;
        this.currentStock = initialStock;
        this.lowStockLevelAlert = lowStockLevelAlert;
        this.price = price;
    }

    /**
     * Constructor for creating a new Medication object with initial stock, low stock alert level,
     * current stock, and price.
     * 
     * @param name The name of the medication.
     * @param initialStock The initial stock of the medication.
     * @param lowStockLevelAlert The stock level at which an alert is triggered.
     * @param currentStock The current stock of the medication.
     * @param price The price of the medication.
     */
    public Medication(String name, int initialStock, int lowStockLevelAlert, int currentStock, double price) {
        this.name = name;
        this.initialStock = initialStock;
        this.currentStock = currentStock;
        this.lowStockLevelAlert = lowStockLevelAlert;
        this.price = price;
    }

    /**
     * Gets the name of the medication.
     * 
     * @return The name of the medication.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the current stock of the medication.
     * 
     * @return The current stock of the medication.
     */
    public int getCurrentStock() {
        return currentStock;
    }

    /**
     * Gets the initial stock of the medication.
     * 
     * @return The initial stock of the medication.
     */
    public int getInitialStock() {
        return initialStock;
    }

    /**
     * Gets the low stock level alert for the medication.
     * 
     * @return The low stock alert level.
     */
    public int getLowStockLevelAlert() {
        return lowStockLevelAlert;
    }

    /**
     * Gets the price of the medication.
     * 
     * @return The price of the medication.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the price of the medication.
     * 
     * @param price The new price of the medication.
     * @return The updated price of the medication.
     */
    public double setPrice(double price) {
        this.price = price;
        return price;
    }

    /**
     * Sets the name of the medication.
     * 
     * @param name The new name of the medication.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the current stock of the medication.
     * 
     * @param currentStock The new current stock of the medication.
     */
    public void setCurrentStock(int currentStock) {
        this.currentStock = currentStock;
    }

    /**
     * Sets the low stock level alert for the medication.
     * 
     * @param lowStockLevelAlert The new low stock alert level.
     */
    public void setLowStockLevelAlert(int lowStockLevelAlert) {
        this.lowStockLevelAlert = lowStockLevelAlert;
    }

    /**
     * Converts the medication details to a CSV record.
     * 
     * @return An array of strings representing the medication details in CSV format.
     */
    @Override
    public String[] toCSVRecord() {
        return new String[] {
                getName() != null ? getName() : "N/A", // Replace null with "N/A" for name (String type)
                initialStock != 0 ? String.valueOf(initialStock) : "0", // Replace 0 with "0" for initial stock (int
                lowStockLevelAlert != 0 ? String.valueOf(lowStockLevelAlert) : "0", // Replace 0 with "0" for low stock
                // alert (int type) // type)
                currentStock != 0 ? String.valueOf(currentStock) : "0", // Replace 0 with "0" for current stock (int
                                                                        // type)
                price != 0.0 ? String.format("%.2f", price) : "0.00" // Replace 0.0 with "0.00" for price (double type)
        };
    }

    /**
     * Returns a string representation of the medication object.
     * 
     * @return A string representing the medication.
     */
    @Override
    public String toString() {
        return "Medication{" +
        // "medicationId='" + medicationId + '\'' +
                ", name='" + name + '\'' +
                ", initialStock=" + initialStock +
                ", lowStockLevelAlert=" + lowStockLevelAlert +
                ", currentStock=" + currentStock +
                '}';
    }
}
