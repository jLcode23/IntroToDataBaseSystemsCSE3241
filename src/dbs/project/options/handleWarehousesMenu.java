package dbs.project.options;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Scanner;
import java.sql.ResultSet;
import dbs.project.InventoryManagementSystem;



public class handleWarehousesMenu {

     public static void main(Scanner args, Connection connection) {
        handleWarehouses(args, connection);
        args.close();
    }

    // ---------------- Warehouses ------------------ //
	/**
	 * Handles all warehouse inventory operations including:
	 *  - Add, Edit, Delete, Search, and View warehouses 
	 * @param myObj Scanner for user input
	 */
	public static void handleWarehouses(Scanner myObj, Connection connection) { 
		int choice = 0;
		ArrayList<String> warehouseMenu = new ArrayList<>();
		warehouseMenu.add("1. Add Warehouse");
		warehouseMenu.add("2. Edit Warehouse");
		warehouseMenu.add("3. Delete Warehouse");
		warehouseMenu.add("4. Search Warehouses");
		warehouseMenu.add("5. View All Warehouses");
		warehouseMenu.add("6. Back to Main Menu");

		while (choice != 6) {
			choice = InventoryManagementSystem.showMenuAndGetChoice("Warehouse Management", warehouseMenu, myObj);

			switch (choice) {
				case 1:
					// Add Warehouse
					try {
						System.out.print("Enter street address: ");
						String street = myObj.nextLine().trim();
						System.out.print("Enter city: ");
						String city = myObj.nextLine().trim();
						System.out.print("Enter phone number: ");
						String phone = myObj.nextLine().trim();
						System.out.print("Enter manager name: ");
						String manager = myObj.nextLine().trim();
						System.out.print("Enter storage capacity: ");
						String storage = myObj.nextLine().trim();
						System.out.print("Enter drone capacity: ");
						String droneCap = myObj.nextLine().trim();

						String sql = "INSERT INTO Warehouse (StreetAddress, City, PhoneNumber, ManagerName, StorageCapacity, DroneCapacity) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";

						PreparedStatement stmt = connection.prepareStatement(sql);
						stmt.setString(1, street);
						stmt.setString(2, city);
						stmt.setString(3, phone);
						stmt.setString(4, manager);
						stmt.setDouble(5, Double.parseDouble(storage));
						stmt.setDouble(6, Double.parseDouble(droneCap));

						stmt.executeUpdate();
						stmt.close();
						System.out.println("Warehouse added successfully!");
					} catch (Exception e) {
						System.out.println("Error adding warehouse: " + e.getMessage());
					}
					break;

				case 2:
					// Edit Warehouse
					try {
						System.out.print("Enter street address of warehouse to edit: ");
						String streetEdit = myObj.nextLine().trim();

						String sqlSelect = "SELECT * FROM Warehouse WHERE StreetAddress = ?";
						PreparedStatement stmt = connection.prepareStatement(sqlSelect);
						stmt.setString(1, streetEdit);
						ResultSet rs = stmt.executeQuery();

						if (!rs.next()) {
							System.out.println("Warehouse not found.");
							rs.close();
							stmt.close();
							break;
						}
						rs.close();
						stmt.close();

						System.out.print("Enter new city: ");
						String cityEdit = myObj.nextLine().trim();
						System.out.print("Enter new phone number: ");
						String newPhone = myObj.nextLine().trim();
						System.out.print("Enter new manager name: ");
						String newManager = myObj.nextLine().trim();
						System.out.print("Enter new storage capacity: ");
						String newStorage = myObj.nextLine().trim();
						System.out.print("Enter new drone capacity: ");
						String newDroneCap = myObj.nextLine().trim();

						String sqlUpdate = "UPDATE Warehouse SET PhoneNumber = ?, ManagerName = ?, StorageCapacity = ?, DroneCapacity = ? " +
								"WHERE StreetAddress = ? AND City = ?";
						stmt = connection.prepareStatement(sqlUpdate);
						stmt.setString(1,newPhone);
						stmt.setString(2, newManager);
						stmt.setDouble(3, Double.parseDouble(newStorage));
						stmt.setDouble(4, Double.parseDouble(newDroneCap));
						stmt.setString(5, streetEdit);
						stmt.setString(6, cityEdit);

						stmt.executeUpdate();
						stmt.close();
						System.out.println("Warehouse updated successfully!");
					} catch (Exception e) {
						System.out.println("Error editing warehouse: " + e.getMessage());
					}
					break;

				case 3:
					// Delete Warehouse
					try {
						System.out.print("Enter street address of warehouse to delete: ");
						String streetDel = myObj.nextLine().trim();

						String sqlDelete = "DELETE FROM Warehouse WHERE StreetAddress = ?";
						PreparedStatement stmt = connection.prepareStatement(sqlDelete);
						stmt.setString(1, streetDel);

						int rowsAffected = stmt.executeUpdate();
						stmt.close();
						if (rowsAffected > 0) {
							System.out.println("Warehouse deleted successfully!");
						} else {
							System.out.println("Warehouse not found.");
						}
					} catch (Exception e) {
						System.out.println("Error deleting warehouse: " + e.getMessage());
					}
					break;

				case 4:
					// Search Warehouses
					try {
						System.out.print("Enter street address of warehouse to delete: ");
						String streetSearch = myObj.nextLine().trim();


						String sqlSearch = "SELECT * FROM Warehouse WHERE StreetAddress = ?";
						PreparedStatement stmt = connection.prepareStatement(sqlSearch);
						stmt.setString(1, streetSearch);
						ResultSet rs = stmt.executeQuery();

						boolean found = false;
						System.out.println("Search results:");
						while (rs.next()) {
							System.out.println("Street: " + rs.getString("StreetAddress") +
									", City: " + rs.getString("City") +
									", Phone: " + rs.getInt("PhoneNumber") +
									", Manager: " + rs.getString("ManagerName") +
									", Storage Capacity: " + rs.getDouble("StorageCapacity") +
									", Drone Capacity: " + rs.getDouble("DroneCapacity"));
							found = true;
						}
						if (!found) System.out.println("No warehouses found in that city.");

						rs.close();
						stmt.close();
					} catch (Exception e) {
						System.out.println("Error searching warehouses: " + e.getMessage());
					}
					break;

				case 5:
					// View All Warehouses
					try {
						String sqlView = "SELECT * FROM Warehouse";
						PreparedStatement stmt = connection.prepareStatement(sqlView);
						ResultSet rs = stmt.executeQuery();

						System.out.println("All warehouses:");
						while (rs.next()) {
							System.out.println("Street: " + rs.getString("StreetAddress") +
									", City: " + rs.getString("City") +
									", Phone: " + rs.getInt("PhoneNumber") +
									", Manager: " + rs.getString("ManagerName") +
									", Storage Capacity: " + rs.getDouble("StorageCapacity") +
									", Drone Capacity: " + rs.getDouble("DroneCapacity"));
						}
						rs.close();
						stmt.close();
					} catch (Exception e) {
						System.out.println("Error retrieving warehouses: " + e.getMessage());
					}
					break;

				case 6:
					// Back to Main Menu
					System.out.println("Returning to main menu...");
					break;

				default:
					System.out.println("Invalid choice. Please try again.");
			}
		}
	}
    
}
