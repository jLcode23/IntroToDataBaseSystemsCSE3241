package dbs.project.options;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Scanner;
import java.sql.ResultSet;
import dbs.project.InventoryManagementSystem;


public class handleEquipmentInventoryMenu {
    
     public static void main(Scanner args, Connection connection) {
        handleEquipmentInventory(args, connection);
        args.close();
    }

    // ---------------- Equipment Inventory ------------------ //
	/**
	 * Handles all equipment inventory operations including:
	 *  - Add, Edit, Delete, Search, and View equipment 
	 * @param myObj Scanner for user input
	 */
	public static void handleEquipmentInventory(Scanner myObj, Connection connection) {

			int choice = 0;
			ArrayList<String> equipmentSubMenu = new ArrayList<String>();
			equipmentSubMenu.add("1. Add Equipment Information");
			equipmentSubMenu.add("2. Edit Equipmemt Information");
			equipmentSubMenu.add("3. Delete Equipment Information ");
			equipmentSubMenu.add("4. Search Equipment");
			equipmentSubMenu.add("5. View All Equipment");
			equipmentSubMenu.add("6. Back to Main Menu");
			
			while ( choice != 6) {
				System.out.println("\n====== MANAGE EQUIPMENT ======");
				choice = InventoryManagementSystem.showMenuAndGetChoice("Equipment Inventory Menu", equipmentSubMenu, myObj);
				
				switch(choice) {
				case 1:
					// Add Equipment
					try {
						System.out.print("Enter warehouse address: ");
						String warehouse = myObj.nextLine().trim();

						System.out.print("Enter serial number: ");
						String serial = myObj.nextLine().trim();

						System.out.print("Enter model: ");
						String model = myObj.nextLine().trim();

						System.out.print("Enter location: ");
						String location = myObj.nextLine().trim();

						System.out.print("Enter year(YYYY): ");
						String year = myObj.nextLine().trim();

						System.out.print("Enter status(Available, Rented, InTransit): ");
													
						String status = myObj.nextLine().trim();

						System.out.print("Enter warranty expiration (YYYY-MM-DD): ");
						String warranty = myObj.nextLine().trim();

						String sql = "INSERT INTO Equipment (WarehouseAddress, SerialNumber, Model, Location, Year, Status, WarrantyExpiration) "
								+ "VALUES (?, ?, ?, ?, ?, ?, ?)";

						PreparedStatement stmt = connection.prepareStatement(sql);
						stmt.setString(1, warehouse);
						stmt.setInt(2, Integer.parseInt(serial));
						stmt.setString(3, model);
						stmt.setString(4, location);
						stmt.setString(5, year);
						stmt.setString(6, status);
						stmt.setString(7, warranty);

						stmt.executeUpdate();
						stmt.close();

						System.out.println("Equipment added successfully!");

					} catch (Exception e) {
						System.out.println("Error adding equipment: " + e.getMessage());
					}
					break;
				case 2:
					// Edit Equipment
					try {
						System.out.print("Enter serial number of equipment to edit: ");
						String serialToEdit = myObj.nextLine().trim();

						String sqlSelect = "SELECT * FROM Equipment WHERE SerialNumber = ?";
						PreparedStatement stmt = connection.prepareStatement(sqlSelect);
						stmt.setInt(1, Integer.parseInt(serialToEdit));
						ResultSet rs = stmt.executeQuery();

						if (!rs.next()) {
							System.out.println("Equipment not found.");
							rs.close();
							stmt.close();
							break;
						}
						rs.close();
						stmt.close();

						System.out.print("Enter new warehouse address: ");
						String newWarehouse = myObj.nextLine().trim();

						System.out.print("Enter new model: ");
						String newModel = myObj.nextLine().trim();

						System.out.print("Enter new location: ");
						String newLocation = myObj.nextLine().trim();

						System.out.print("Enter new year(YYYY): ");
						String newYear = myObj.nextLine().trim();

						System.out.print("Enter new status(Available, Rented, InTransit): ");
						String newStatus = myObj.nextLine().trim();

						System.out.print("Enter new warranty expiration (YYYY-MM-DD): ");
						String newWarranty = myObj.nextLine().trim();

						String sqlUpdate = "UPDATE Equipment SET WarehouseAddress = ?, Model = ?, Location = ?, Year = ?, Status = ?, WarrantyExpiration = ? WHERE SerialNumber = ?";
						stmt = connection.prepareStatement(sqlUpdate);
						stmt.setString(1, newWarehouse);
						stmt.setString(2, newModel);
						stmt.setString(3, newLocation);
						stmt.setString(4, newYear);
						stmt.setString(5, newStatus);
						stmt.setString(6, newWarranty);
						stmt.setInt(7, Integer.parseInt(serialToEdit));

						stmt.executeUpdate();
						stmt.close();

						System.out.println("Equipment updated successfully!");

					} catch (Exception e) {
						System.out.println("Error editing equipment: " + e.getMessage());
					}
					break;
					
				case 3:
					// Delete Equipment
					try {
						System.out.print("Enter serial number of equipment to delete: ");
						String serialToDelete = myObj.nextLine().trim();

						String sqlDelete = "DELETE FROM Equipment WHERE SerialNumber = ?";
						PreparedStatement stmt = connection.prepareStatement(sqlDelete);
						stmt.setInt(1, Integer.parseInt(serialToDelete));

						int rowsAffected = stmt.executeUpdate();
						stmt.close();

						if (rowsAffected > 0) {
							System.out.println("Equipment deleted successfully!");
						} else {
							System.out.println("Equipment with Serial '" + serialToDelete + "' not found.");
						}

					} catch (Exception e) {
						System.out.println("Error deleting equipment: " + e.getMessage());
					}
					break;
				case 4:
					// Search Equipment by Serial Number
					try {
						System.out.print("Enter equipment serial number to search: ");
						String serialKeyword = myObj.nextLine().trim();

						String sqlSearch = "SELECT * FROM Equipment WHERE SerialNumber = ?";
						PreparedStatement stmt = connection.prepareStatement(sqlSearch);
						stmt.setInt(1, Integer.parseInt(serialKeyword));
						ResultSet rs = stmt.executeQuery();

						boolean found = false;
						System.out.println("Search results:");
						while (rs.next()) {
							System.out.println("Serial: " + rs.getInt("SerialNumber") +
											", Warehouse: " + rs.getString("WarehouseAddress") +
											", Model: " + rs.getString("Model") +
											", Location: " + rs.getString("Location") +
											", Year: " + rs.getString("Year") +
											", Status: " + rs.getString("Status") +
											", Warranty Expiration: " + rs.getString("WarrantyExpiration"));
							found = true;
						}

						if (!found) {
							System.out.println("No equipment found with that serial number.");
						}

						rs.close();
						stmt.close();

					} catch (Exception e) {
						System.out.println("Error searching equipment: " + e.getMessage());
					}
					break;
				case 5: 
					// View All Equipment
					try {
						String sqlView = "SELECT * FROM Equipment";
						PreparedStatement stmt = connection.prepareStatement(sqlView);
						ResultSet rs = stmt.executeQuery();

						System.out.println("All equipment in inventory:");
						boolean hasAny = false;
						while (rs.next()) {
							System.out.println(" - Serial: " + rs.getInt("SerialNumber") +
											", Warehouse: " + rs.getString("WarehouseAddress") +
											", Model: " + rs.getString("Model") +

											", Location: " + rs.getString("Location") +
											", Year: " + rs.getString("Year") +
											", Status: " + rs.getString("Status") +
											", Warranty Expiration: " + rs.getString("WarrantyExpiration"));
							hasAny = true;
						}

						if (!hasAny) {
							System.out.println("(none)");
						}

						rs.close();
						stmt.close();

					} catch (Exception e) {
						System.out.println("Error retrieving equipment: " + e.getMessage());
					}
					break;
				case 6:
					// Back to Main Menu
					System.out.println("Returning to main menu...");
					break;
				default:
					System.out.println("Invalid choice. Please try again.");
					break;
				}
			}
			
	}
	
	
}