package dbs.project.options;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import dbs.project.InventoryManagementSystem;

public class handleEquipmentOperationsMenu {

    public static void main(Scanner args, Connection connection) {
        handleEquipmentOperations(args, connection);
    }

    // ---------------- Equipment Operations ------------------ //
    /**
     * Handles the main loop and user interaction for all equipment-related transactions, 
     * including renting, returning, delivering, and picking up equipment.
     * @param myObj Scanner for user input
     * @param connection Database connection object
     */
    public static void handleEquipmentOperations(Scanner myObj, Connection connection) {

        int choice = 0;
        ArrayList<String> subMenu = new ArrayList<>();
        subMenu.add("1. Rent Equipment");
        subMenu.add("2. Return Equipment");
        subMenu.add("3. Deliver Equipment");
        subMenu.add("4. Pickup Equipment");
        subMenu.add("5. Back to Main Menu");

        while (choice != 5) {
            choice = InventoryManagementSystem.showMenuAndGetChoice(
                    "Equipment Operations", subMenu, myObj);

            switch (choice) {
                case 1:
                    System.out.print("Enter Customer ID: ");
                    int custRent = Integer.parseInt(myObj.nextLine());

                    System.out.print("Enter Equipment Serial Number: ");
                     int eqRent = Integer.parseInt(myObj.nextLine());

                    rentEquipment(connection, custRent, eqRent);
                    break;

                case 2:
                    System.out.print("Enter Customer ID: ");
                    int custReturn = Integer.parseInt(myObj.nextLine());

                    System.out.print("Enter Equipment Serial Number: ");
                    int eqReturn = Integer.parseInt(myObj.nextLine());

                    returnEquipment(connection, custReturn, eqReturn);
                    break;

                case 3:
                    System.out.print("Enter Equipment SN: ");
                    int eqDeliver = Integer.parseInt(myObj.nextLine());

                    System.out.print("Enter Drone Serial: ");
                    String drDeliver = myObj.nextLine();

                    System.out.print("Enter Distance: ");
                    double distanceDelivery = myObj.nextDouble();
                    myObj.nextLine(); // Consume the newline character

                    deliverEquipment(connection, eqDeliver, drDeliver, distanceDelivery);
                    break;

                case 4:
                    System.out.print("Enter Equipment SN: ");
                    int eqPickup = Integer.parseInt(myObj.nextLine());

                    System.out.print("Enter Drone Serial: ");
                    String drPickup = myObj.nextLine();

                    System.out.print("Enter Distance: ");
                    double distancePickup = myObj.nextDouble();
                    myObj.nextLine(); // Consume the newline character

                    pickupEquipment(connection, eqPickup, drPickup, distancePickup);
                    break;

                case 5:
                    System.out.println("Returning to Main Menu...");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    /**
     * Registers a new equipment rental transaction in the database.
     * It checks equipment availability, prompts the user for rental details (days, fee),
     * inserts a new record into the Rental table, and updates the Equipment status to 'Rented'.
     * @param conn Database connection object
     * @param customerID The ID of the customer renting the equipment
     * @param equipmentSN The serial number of the equipment to rent
     */
    private static void rentEquipment(Connection conn, int customerID, int equipmentSN) {
        try {
            // Check availability
            PreparedStatement check = conn.prepareStatement(
                    "SELECT Status FROM Equipment WHERE SerialNumber = ?");
            check.setInt(1, equipmentSN);
            ResultSet rs = check.executeQuery();

            if (!rs.next()) {
                System.out.println("Equipment does not exist.");
                return;
            }
            if (!rs.getString("Status").equals("Available")) {
                System.out.println("Equipment is NOT available.");
                return;
            }

            Scanner sc = new Scanner(System.in);

            // Number of rental days
            System.out.print("Enter number of days to rent: ");
            int days = Integer.parseInt(sc.nextLine());

            // Rental fee
            System.out.print("Enter rental fee: ");
            double rentalFee = Double.parseDouble(sc.nextLine());

            // Insert rental record (matches ERD)
            PreparedStatement insert = conn.prepareStatement(
                "INSERT INTO Rental (" +
                "CustomerID, EquipmentSerialNumber, CheckoutDate, ExpectedReturnDate, RentalFee, ActualReturnDate" +
                ") VALUES (?, ?, DATE('now'), DATE('now', '+' || ? || ' days'), ?, NULL)"
            );

            insert.setInt(1, customerID);
            insert.setInt(2, equipmentSN);
            insert.setInt(3, days);
            insert.setDouble(4, rentalFee);
            insert.executeUpdate();

            // Update equipment status
            PreparedStatement update = conn.prepareStatement(
                    "UPDATE Equipment SET Status='Rented' WHERE SerialNumber=?");
            update.setInt(1, equipmentSN);
            update.executeUpdate();

            System.out.println("Equipment rented successfully!");

        } catch (SQLException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    /**
     * Processes the return of rented equipment.
     * It updates the active rental record in the Rental table by setting the ActualReturnDate 
     * to the current date and updates the Equipment status to 'Available'.
     * @param conn Database connection object
     * @param customerID The ID of the customer returning the equipment
     * @param equipmentSN The serial number of the equipment being returned
     */
    private static void returnEquipment(Connection conn, int customerID, int equipmentSN) {
        try {
            PreparedStatement updateRental = conn.prepareStatement(
                "UPDATE Rental SET ActualReturnDate = DATE('now') " +
                "WHERE CustomerID=? AND EquipmentSerialNumber=? AND ActualReturnDate IS NULL"
            );

            updateRental.setInt(1, customerID);
            updateRental.setInt(2, equipmentSN);

            int rows = updateRental.executeUpdate();
            if (rows == 0) {
                System.out.println("No active rental found.");
                return;
            }

            PreparedStatement updateEq = conn.prepareStatement(
                "UPDATE Equipment SET Status='Available' WHERE SerialNumber=?"
            );
            updateEq.setInt(1, equipmentSN);
            updateEq.executeUpdate();

            System.out.println("Equipment returned successfully!");

        } catch (SQLException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
  
    /**
     * Initiates the delivery process for a piece of equipment currently under an active rental.
     * It creates a new 'delivery' record in the Shipment table, links the shipment to the specified
     * drone, sets the drone's status to 'InUse', and records the distance.
     * @param conn Database connection object
     * @param equipmentSN The serial number of the equipment to be delivered
     * @param droneSN The serial number of the drone performing the delivery
     * @param distance The distance of the delivery trip
     */
    private static void deliverEquipment(Connection conn, int equipmentSN, String droneSN, double distance) {
        try {
            // Find the active rental for this equipment
            PreparedStatement getRental = conn.prepareStatement(
                "SELECT CustomerID FROM Rental WHERE EquipmentSerialNumber=? AND ActualReturnDate IS NULL"
            );
            getRental.setInt(1, equipmentSN);
            ResultSet rsRental = getRental.executeQuery();

            if (!rsRental.next()) {
                System.out.println("No active rental found to deliver.");
                return;
            }

            int customerID = rsRental.getInt("CustomerID");

            // Insert shipment record.
            PreparedStatement insertShipment = conn.prepareStatement(
                "INSERT INTO Shipment (CustomerID, EquipmentSerialNumber, ShipmentType, ScheduledDate, Distance) " +
                "VALUES (?, ?, 'delivery', DATE('now'), ?)"
            );
            insertShipment.setInt(1, customerID);
            insertShipment.setInt(2, equipmentSN);
            insertShipment.setDouble(3, distance); 
            insertShipment.executeUpdate();

            // Get the newly created ShipmentID
            ResultSet rsShipment = conn.prepareStatement("SELECT last_insert_rowid() AS id").executeQuery();
            int shipmentID = rsShipment.getInt("id");

            // Assign the drone to this shipment
            PreparedStatement updDrone = conn.prepareStatement(
                "UPDATE Drone SET ShipmentID=?, Status='InUse' WHERE SerialNumber=?"
            );
            updDrone.setInt(1, shipmentID);
            updDrone.setString(2, droneSN);
            updDrone.executeUpdate();

            System.out.println("Equipment delivery registered!");

        } catch (SQLException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    /**
     * Initiates the pickup process for a piece of equipment, typically at the end of a rental.
     * It creates a new 'pickup' record in the Shipment table, links the shipment to the specified
     * drone, sets the drone's status to 'InUse', 
     * updates the Equipment status to 'InTransit', and records the distance.
     * @param conn Database connection object
     * @param equipmentSN The serial number of the equipment to be picked up
     * @param droneSN The serial number of the drone performing the pickup
     * @param distance The distance of the pickup trip
     */
    private static void pickupEquipment(Connection conn, int equipmentSN, String droneSN, double distance) {
        try {
            // Find the active rental for this equipment
            PreparedStatement getRental = conn.prepareStatement(
                "SELECT CustomerID FROM Rental WHERE EquipmentSerialNumber=?"
            );
            getRental.setInt(1, equipmentSN);
            ResultSet rsRental = getRental.executeQuery();

            if (!rsRental.next()) {
                System.out.println("No rental record found for this equipment.");
                return;
            }

            int customerID = rsRental.getInt("CustomerID");

            // Insert shipment record for pickup.
            PreparedStatement insertShipment = conn.prepareStatement(
                "INSERT INTO Shipment (CustomerID, EquipmentSerialNumber, ShipmentType, ScheduledDate, Distance) " +
                "VALUES (?, ?, 'pickup', DATE('now'), ?)"
            );
            insertShipment.setInt(1, customerID);
            insertShipment.setInt(2, equipmentSN);
            insertShipment.setDouble(3, distance);
            insertShipment.executeUpdate();

            // Get the newly created ShipmentID
            ResultSet rsShipment = conn.prepareStatement("SELECT last_insert_rowid() AS id").executeQuery();
            int shipmentID = rsShipment.getInt("id");

            // Assign the drone to this shipment
            PreparedStatement updDrone = conn.prepareStatement(
                "UPDATE Drone SET ShipmentID=?, Status='InUse' WHERE SerialNumber=?"
            );
            updDrone.setInt(1, shipmentID);
            updDrone.setString(2, droneSN);
            updDrone.executeUpdate();

            // Update equipment status
            PreparedStatement updEquipment = conn.prepareStatement(
                "UPDATE Equipment SET Status='InTransit' WHERE SerialNumber=?"
            );
            updEquipment.setInt(1, equipmentSN);
            updEquipment.executeUpdate();

            System.out.println("Equipment pickup registered!");

        } catch (SQLException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
}