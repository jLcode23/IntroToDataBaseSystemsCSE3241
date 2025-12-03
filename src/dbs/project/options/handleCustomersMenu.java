package dbs.project.options;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Scanner;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

import dbs.project.InventoryManagementSystem;

public class handleCustomersMenu {

     public static void main(Scanner args, Connection connection) {
        handleCustomers(args, connection);
        args.close();
    }

    
	// ---------------- Customers ------------------ //
	/**
 	 * Handles all customer-related operations including:
	 *  - Add, Edit, Delete, Search, and View customers 
	 * @param myObj Scanner for user input
	 */
	public static void handleCustomers(Scanner myObj, Connection connection) { 
		int choice = 0;
		ArrayList<String> customerMenu = new ArrayList<>();
		customerMenu.add("1. Add Customer");
		customerMenu.add("2. Edit Customer");
		customerMenu.add("3. Delete Customer");
		customerMenu.add("4. Search Customers");
		customerMenu.add("5. View All Customers");
		customerMenu.add("6. Back to Main Menu");

		while (choice != 6) {
			choice = InventoryManagementSystem.showMenuAndGetChoice("Customer Management", customerMenu, myObj);

			switch (choice) {
				case 1:
					// Add Customer
					try {
						System.out.print("Enter first name: ");
						String first = myObj.nextLine().trim();
						System.out.print("Enter last name: ");
						String last = myObj.nextLine().trim();
						System.out.print("Enter email: ");
						String email = myObj.nextLine().trim();
						System.out.print("Enter address: ");
						String address = myObj.nextLine().trim();
						System.out.print("Enter start date (YYYY-MM-DD): ");
						String startDate = myObj.nextLine().trim();
						System.out.print("Enter distance from warehouse: ");
						String distance = myObj.nextLine().trim();
						System.out.print("Enter phone number: ");
						String phone = myObj.nextLine().trim();

						String sql = "INSERT INTO Customer (FirstName, LastName, Email, Address, StartDate, WarehouseDistance, PhoneNumber) " +
								"VALUES (?, ?, ?, ?, ?, ?, ?)";
						PreparedStatement stmt = connection.prepareStatement(sql);
						stmt.setString(1, first);
						stmt.setString(2, last);
						stmt.setString(3, email);
						stmt.setString(4, address);
						stmt.setString(5, startDate);
						stmt.setDouble(6, Double.parseDouble(distance));
						stmt.setString(7, phone);

						stmt.executeUpdate();
						stmt.close();
						System.out.println("Customer added successfully!");
					} catch (Exception e) {
						System.out.println("Error adding customer: " + e.getMessage());
					}
					break;

				case 2:
					// Edit Customer
					try {
						System.out.print("Enter Customer ID to edit: ");
						String userId = myObj.nextLine().trim();

						String sqlSelect = "SELECT * FROM Customer WHERE UserID = ?";
						PreparedStatement stmt = connection.prepareStatement(sqlSelect);
						stmt.setInt(1, Integer.parseInt(userId));
						ResultSet rs = stmt.executeQuery();

						if (!rs.next()) {
							System.out.println("Customer not found.");
							rs.close();
							stmt.close();
							break;
						}
						rs.close();
						stmt.close();

						System.out.print("Enter new first name: ");
						String firstNew = myObj.nextLine().trim();
						System.out.print("Enter new last name: ");
						String lastNew = myObj.nextLine().trim();
						System.out.print("Enter new email: ");
						String emailNew = myObj.nextLine().trim();
						System.out.print("Enter new address: ");
						String addressNew = myObj.nextLine().trim();
						System.out.print("Enter new start date (YYYY-MM-DD): ");
						String startDateNew = myObj.nextLine().trim();
						System.out.print("Enter new distance from warehouse: ");
						String distanceNew = myObj.nextLine().trim();
						System.out.print("Enter new phone number: ");
						String phoneNew = myObj.nextLine().trim();

						String sqlUpdate = "UPDATE Customer SET FirstName = ?, LastName = ?, Email = ?, Address = ?, StartDate = ?, WarehouseDistance = ?, PhoneNumber = ? " +
								"WHERE UserID = ?";
						stmt = connection.prepareStatement(sqlUpdate);
						stmt.setString(1, firstNew);
						stmt.setString(2, lastNew);
						stmt.setString(3, emailNew);
						stmt.setString(4, addressNew);
						stmt.setString(5, startDateNew);
						stmt.setDouble(6, Double.parseDouble(distanceNew));
						stmt.setString(7, phoneNew);
						stmt.setInt(8, Integer.parseInt(userId));

						stmt.executeUpdate();
						stmt.close();
						System.out.println("Customer updated successfully!");
					} catch (Exception e) {
						System.out.println("Error editing customer: " + e.getMessage());
					}
					break;

				case 3:
					// Delete Customer
					try {
						System.out.print("Enter Customer ID to delete: ");
						String userIdDel = myObj.nextLine().trim();

						String sqlDelete = "DELETE FROM Customer WHERE UserID = ?";
						PreparedStatement stmt = connection.prepareStatement(sqlDelete);
						stmt.setInt(1, Integer.parseInt(userIdDel));

						int rowsAffected = stmt.executeUpdate();
						stmt.close();
						if (rowsAffected > 0) {
							System.out.println("Customer deleted successfully!");
						} else {
							System.out.println("Customer not found.");
						}
					} catch (Exception e) {
						System.out.println("Error deleting customer: " + e.getMessage());
					}
					break;

				case 4:
					// Search Customers
					try {
						System.out.print("Enter last name to search: ");
						String lastSearch = myObj.nextLine().trim();

						String sqlSearch = "SELECT * FROM Customer WHERE LastName = ?";
						PreparedStatement stmt = connection.prepareStatement(sqlSearch);
						stmt.setString(1, lastSearch);
						ResultSet rs = stmt.executeQuery();

						boolean found = false;
						System.out.println("Search results:");
						while (rs.next()) {
							System.out.println("ID: " + rs.getInt("UserID") +
									", Name: " + rs.getString("FirstName") + " " + rs.getString("LastName") +
									", Email: " + rs.getString("Email") +
									", Address: " + rs.getString("Address") +
									", StartDate: " + rs.getString("StartDate") +
									", Distance: " + rs.getDouble("WarehouseDistance") +
									", Phone: " + rs.getString("PhoneNumber"));
							found = true;
						}
						if (!found) System.out.println("No customers found with that last name.");

						rs.close();
						stmt.close();
					} catch (Exception e) {
						System.out.println("Error searching customers: " + e.getMessage());
					}
					break;

				case 5:
					// View All Customers
					try {
						String sqlView = "SELECT * FROM Customer";
						PreparedStatement stmt = connection.prepareStatement(sqlView);
						ResultSet rs = stmt.executeQuery();

						System.out.println("All customers:");
						while (rs.next()) {
							System.out.println("ID: " + rs.getInt("UserID") +
									", Name: " + rs.getString("FirstName") + " " + rs.getString("LastName") +
									", Email: " + rs.getString("Email") +
									", Address: " + rs.getString("Address") +
									", StartDate: " + rs.getString("StartDate") +
									", Distance: " + rs.getDouble("WarehouseDistance") +
									", Phone: " + rs.getString("PhoneNumber"));
						}
						rs.close();
						stmt.close();
					} catch (Exception e) {
						System.out.println("Error retrieving customers: " + e.getMessage());
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
