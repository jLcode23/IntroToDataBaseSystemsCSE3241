package dbs.project.options;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Scanner;
import dbs.project.InventoryManagementSystem;


public class handleDroneInventoryMenu {




    public static void main(Scanner args, Connection connection) {
        handleDroneInventory(args, connection);
        args.close();
    }

    // ---------------- Drone Inventory ------------------ //
	/**
	 * Handles all drone inventory operations including:
	 * - Add, Edit, Delete, Search, and View drones
	 * 
	 * @param myObj   Scanner for user input
	 * @param connection   Database connection
	 */
	public static void handleDroneInventory(Scanner myObj, Connection connection) {
		
		int choice = 0;
		ArrayList<String> droneSubMenu = new ArrayList<String>();
		droneSubMenu.add("1. Add Drone");
		droneSubMenu.add("2. Edit Drone");
		droneSubMenu.add("3. Delete Drone");
		droneSubMenu.add("4. Search Drones");
		droneSubMenu.add("5. View all Drones");
		droneSubMenu.add("6. Back to Main Menu");
		
		// Loop until the user chooses to go back to the main menu
		while ( choice != 6 ) {
			System.out.println("\n====== MANAGE DRONES ======");
			choice = InventoryManagementSystem.showMenuAndGetChoice("Drone Inventory Menu", droneSubMenu, myObj);
			
			switch(choice) {
			case 1:
				// Add new drone
				try{
				System.out.print("Enter serial number: ");
				String serial = myObj.nextLine().trim();
				
				System.out.print("Enter model: ");
				String model = myObj.nextLine().trim();
				
				System.out.print("Enter warehouse address: ");
				String warehouseAddress = myObj.nextLine().trim();
				
				System.out.print("Enter shipment Id:");
				String shipmentId = myObj.nextLine().trim();
				
				System.out.print("Enter Location: ");
				String location = myObj.nextLine().trim();

				System.out.print("Enter Status(Available, InUse): ");
				String status = myObj.nextLine().trim();

				System.out.print("Enter Year(YYYY):");
				String year = myObj.nextLine().trim();

				String sql = "INSERT INTO Drone (SerialNumber, Model, WarehouseAddress, ShipmentID, Location, Status, Year) VALUES(?, ?, ?, ?, ?, ?, ?)";
				
				PreparedStatement stmt = connection.prepareStatement(sql);
				stmt.setString(1, serial);
				stmt.setString(2, model);
				stmt.setString(3, warehouseAddress);
				stmt.setInt(4, Integer.parseInt(shipmentId));
				stmt.setString(5, location);
				stmt.setString(6, status);
				stmt.setString(7, year);

				stmt.executeUpdate();
				stmt.close();

				System.out.println("Drone added successfully!");

				} catch (Exception e) {
					System.out.println("Error adding drone: " + e.getMessage());
				}
				break;
				
			
			case 2:
				try{
					// Edit existing drone 
					System.out.println("Enter serial number of drone to edit: ");
					String serialToEdit = myObj.nextLine().trim();

					String sqlSelect = "SELECT * FROM Drone WHERE SerialNumber = ?";
					PreparedStatement stmt = connection.prepareStatement(sqlSelect);
					stmt.setString(1, serialToEdit);
					ResultSet rs = stmt.executeQuery();

					if (!rs.next()) {
						System.out.println("Drone not found.");
						rs.close();
						stmt.close();
						break;
                	}
					rs.close();
					stmt.close();

					System.out.print("Enter new model: ");
					String updateModel = myObj.nextLine().trim();
					
					System.out.print("Enter new warehouse address: ");
					String updateWarehouseAddress = myObj.nextLine().trim();
					
					System.out.print("Enter new shipment Id:");
					String updateShipmentId = myObj.nextLine().trim();
					
					System.out.print("Enter new Location: ");
					String updateLocation = myObj.nextLine().trim();

					System.out.print("Enter new Status(Available, InUse): ");
					String updateStatus = myObj.nextLine().trim();

					System.out.print("Enter new Year(YYYY):");
					String updateYear = myObj.nextLine().trim();

					String sql = "UPDATE Drone SET Model = ?, WarehouseAddress = ?, ShipmentID = ?, Location = ?, Status = ?, Year = ? WHERE SerialNumber = ?";
					
			        stmt = connection.prepareStatement(sql);
					stmt.setString(1, updateModel);
					stmt.setString(2, updateWarehouseAddress);
					stmt.setInt(3, Integer.parseInt(updateShipmentId));
					stmt.setString(4, updateLocation);
					stmt.setString(5, updateStatus);
					stmt.setString(6, updateYear);
					stmt.setString(7, serialToEdit); 
				

					stmt.executeUpdate();
					stmt.close();
					
					
					System.out.println("Drone updated successfully!");


				} catch (Exception e) {
					System.out.println("Error adding drone: " + e.getMessage());
				}
				break;
				
	
			case 3:
				// Delete drone by serial number
				try {
					System.out.print("Enter serial number of drone to delete: ");
					String serialToDelete = myObj.nextLine().trim();

					String sqlDelete = "DELETE FROM Drone WHERE SerialNumber = ?";
					PreparedStatement stmt = connection.prepareStatement(sqlDelete);
					stmt.setString(1, serialToDelete);

					int rowsAffected = stmt.executeUpdate();
					stmt.close();

					if (rowsAffected > 0) {
						System.out.println("Drone deleted successfully!");
					} else {
						System.out.println("Drone with Serial '" + serialToDelete + "' not found.");
					}
				} catch (Exception e) {
					System.out.println("Error deleting drone: " + e.getMessage());
				}
				break;		

			case 4:
				// Search for drones by serial number
				try {
					System.out.print("Enter drone serial number to search: ");
					String keyword = myObj.nextLine().trim();

					String sqlSelect = "SELECT * FROM Drone WHERE SerialNumber = ?";
					PreparedStatement stmt = connection.prepareStatement(sqlSelect);
					stmt.setString(1, keyword);
					ResultSet rs = stmt.executeQuery();

					boolean found = false;
					System.out.println("Search results:");
					while (rs.next()) {
						String serial = rs.getString("SerialNumber");
						String model = rs.getString("Model");
						String warehouse = rs.getString("WarehouseAddress");
						int shipment = rs.getInt("ShipmentID");
						String location = rs.getString("Location");
						String status = rs.getString("Status");
						String year = rs.getString("Year");

						System.out.println("Serial: " + serial + ", Model: " + model + ", Warehouse: " + warehouse +
										", Shipment: " + shipment + ", Location: " + location + ", Status: " + status + ", Year: " + year);
						found = true;
					}

					if (!found) {
						System.out.println("No drones found with that model.");
					}

					rs.close();
					stmt.close();

				} catch (Exception e) {
					System.out.println("Error searching drones: " + e.getMessage());
				}
				break;
			case 5: 
				// View all drones
				try {
					String sqlView = "SELECT * FROM Drone";
					PreparedStatement stmt = connection.prepareStatement(sqlView);
					ResultSet rs = stmt.executeQuery();

					System.out.println("All drones in inventory:");
					boolean hasAny = false;
					while (rs.next()) {
						System.out.println(" - Serial: " + rs.getString("SerialNumber") +
										", Model: " + rs.getString("Model") +
										", Warehouse: " + rs.getString("WarehouseAddress") +
										", Shipment: " + rs.getInt("ShipmentID") +
										", Location: " + rs.getString("Location") +
										", Status: " + rs.getString("Status") +
										", Year: " + rs.getString("Year"));
						hasAny = true;
					}

					if (!hasAny) {
						System.out.println("(none)");
					}
					rs.close();
					stmt.close();

				} catch (Exception e) {
					System.out.println("Error retrieving drones: " + e.getMessage());
				}
				break;
			case 6:
				System.out.println("Returning to main menu...");
				break;
			default:
				System.out.println("Invalid choice. Please try again.");
				break;
			}
		}
		
	}
}
