package dbs.project;
import java.util.ArrayList;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



public class InventoryManagementSystem {

	private static final String DB_FILE = "company.db";
	private static Connection connection = null;
	private static PreparedStatement stmt = null;
	private static ResultSet rs = null;


	public static Connection getConnection() {
		if (connection != null) {
			return connection; 
		}

		try {
			Class.forName("org.sqlite.JDBC");

			String url = "jdbc:sqlite:" + DB_FILE;

			connection = DriverManager.getConnection(url);

			if (connection != null) {
				DatabaseMetaData meta = connection.getMetaData();
				System.out.println("The driver name is " + meta.getDriverName());
				System.out.println("Connection to " + DB_FILE + " successful!");
			} else {
				System.out.println("Connection returned null. Something went wrong.");
			}

		} catch (ClassNotFoundException e) {
			System.out.println("SQLite JDBC driver not found. Make sure the jar is on the classpath!");
			System.out.println(e.getMessage());
		} catch (SQLException e) {
			System.out.println("Database connection failed: " + e.getMessage());
		}

		return connection;
	}


	/**
	 * Displays a menu with a given title and options, 
	 * and asks the user for a numeric choice.
	 * 
	 * @param title      The title of the menu to display
	 * @param menuOptions   The list of menu options
	 * @param myObj          The Scanner object for user input
	 * @return   The integer choice returned by the user
	 */
	public static int showMenuAndGetChoice(String title, ArrayList<String> menuOptions, Scanner myObj) {
		System.out.println();
		System.out.println("==== " + title + " ====");
		for ( int i = 0; i < menuOptions.size(); i++) {
			System.out.println(menuOptions.get(i));
		}
		System.out.print("Enter a number to continue: ");
		int choice = myObj.nextInt();
		myObj.nextLine();  // consume newline
		return choice;
		
	}
	
	
	// ---------------- Drone Inventory ------------------ //
	/**
	 * Handles all drone inventory operations including:
	 * - Add, Edit, Delete, Search, and View drones
	 * 
	 * @param myObj   Scanner for user input
	 */
	public static void handleDroneInventoryMenu(Scanner myObj) {
		
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
			choice = showMenuAndGetChoice("Drone Inventory Menu", droneSubMenu, myObj);
			
			switch(choice) {
			case 1:
				try{
				
				// Add new drone
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

				System.out.print("Enter Status: ");
				String status = myObj.nextLine().trim();

				System.out.print("Enter Year:");
				String year = myObj.nextLine().trim();

				String sql = "INSERT INTO Drone (SerialNumber, Model, WarehouseAddress, ShipmentID, Location, Status, Year) VALUES(?, ?, ?, ?, ?, ?, ?)";
				
				stmt = getConnection().prepareStatement(sql);
				stmt.setString(1, serial);
				stmt.setString(2, model);
				stmt.setString(3, warehouseAddress);
				stmt.setString(4, shipmentId);
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
					stmt = getConnection().prepareStatement(sqlSelect);
					stmt.setString(1, serialToEdit);
					rs = stmt.executeQuery();

					if (!rs.next()) {
						System.out.println("Drone not found.");
						rs.close();
						stmt.close();
						break;
                	}
					rs.close();
					stmt.close();

					System.out.print("Enter model: ");
					String updateModel = myObj.nextLine().trim();
					
					System.out.print("Enter warehouse address: ");
					String updateWarehouseAddress = myObj.nextLine().trim();
					
					System.out.print("Enter shipment Id:");
					String updateShipmentId = myObj.nextLine().trim();
					
					System.out.print("Enter Location: ");
					String updateLocation = myObj.nextLine().trim();

					System.out.print("Enter Status: ");
					String updateStatus = myObj.nextLine().trim();

					System.out.print("Enter Year:");
					String updateYear = myObj.nextLine().trim();

					String sql = "UPDATE Drone SET Model = ?, WarehouseAddress = ?, ShipmentID = ?, Location = ?, Status = ?, Year = ? WHERE SerialNumber = ?";
					
					stmt = getConnection().prepareStatement(sql);
					stmt.setString(1, updateModel);
					stmt.setString(2, updateWarehouseAddress);
					stmt.setString(3, updateShipmentId);
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
					stmt = getConnection().prepareStatement(sqlDelete);
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
					stmt = getConnection().prepareStatement(sqlSelect);
					stmt.setString(1, keyword);
					rs = stmt.executeQuery();

					boolean found = false;
					System.out.println("Search results:");
					while (rs.next()) {
						String serial = rs.getString("SerialNumber");
						String model = rs.getString("Model");
						String warehouse = rs.getString("WarehouseAddress");
						String shipment = rs.getString("ShipmentID");
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
					stmt = getConnection().prepareStatement(sqlView);
					rs = stmt.executeQuery();

					System.out.println("All drones in inventory:");
					boolean hasAny = false;
					while (rs.next()) {
						System.out.println(" - Serial: " + rs.getString("SerialNumber") +
										", Model: " + rs.getString("Model") +
										", Warehouse: " + rs.getString("WarehouseAddress") +
										", Shipment: " + rs.getString("ShipmentID") +
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
	
	// ---------------- Equipment Inventory ------------------ //
	/**
	 * Handles all equipment inventory operations including:
	 *  - Add, Edit, Delete, Search, and View equipment 
	 * @param myObj Scanner for user input
	 */
	public static void handleEquipmentInventoryMenu(Scanner myObj) {

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
				choice = showMenuAndGetChoice("Equipment Inventory Menu", equipmentSubMenu, myObj);
				
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

						System.out.print("Enter year: ");
						String year = myObj.nextLine().trim();

						System.out.print("Enter status: ");
						String status = myObj.nextLine().trim();

						System.out.print("Enter warranty expiration (YYYY-MM-DD): ");
						String warranty = myObj.nextLine().trim();

						String sql = "INSERT INTO Equipment (WarehouseAddress, SerialNumber, Model, Location, Year, Status, WarrantyExpiration) "
								+ "VALUES (?, ?, ?, ?, ?, ?, ?)";

						stmt = getConnection().prepareStatement(sql);
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
						stmt = getConnection().prepareStatement(sqlSelect);
						stmt.setInt(1, Integer.parseInt(serialToEdit));
						rs = stmt.executeQuery();

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

						System.out.print("Enter new year: ");
						String newYear = myObj.nextLine().trim();

						System.out.print("Enter new status: ");
						String newStatus = myObj.nextLine().trim();

						System.out.print("Enter new warranty expiration (YYYY-MM-DD): ");
						String newWarranty = myObj.nextLine().trim();

						String sqlUpdate = "UPDATE Equipment SET WarehouseAddress = ?, Model = ?, Location = ?, Year = ?, Status = ?, WarrantyExpiration = ? WHERE SerialNumber = ?";
						stmt = getConnection().prepareStatement(sqlUpdate);
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
						stmt = getConnection().prepareStatement(sqlDelete);
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
						stmt = getConnection().prepareStatement(sqlSearch);
						stmt.setInt(1, Integer.parseInt(serialKeyword));
						rs = stmt.executeQuery();

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
						stmt = getConnection().prepareStatement(sqlView);
						rs = stmt.executeQuery();

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
					System.out.println("Returning to main menu...");
					break;
				default:
					System.out.println("Invalid choice. Please try again.");
					break;
				}
			}
			
	}
	
	/**
	 * Handles operations related to equipment transactions:
	 * Renting, Returning, Delivering, and Picking up equipment 
	 * 
	 * @param myObj  Scanner for user input 
	 */
	// ---------------- EQUIPMENT OPERARIONS ------------------ //
	public static void handleEquipmentOperationsMenu( Scanner myObj) {
		int choice = 0;
		ArrayList<String> equipmentsOpsSubMenu = new ArrayList<String>();
		equipmentsOpsSubMenu.add("1. Rent Equipment");
		equipmentsOpsSubMenu.add("2. Return Equipment");
		equipmentsOpsSubMenu.add("3. Deliver Equipment");
		equipmentsOpsSubMenu.add("4. Pickup Equipment");
		equipmentsOpsSubMenu.add("5. Back to Main Menu");
		
		while ( choice != 5) {
			choice = showMenuAndGetChoice("Equipment Operations", equipmentsOpsSubMenu, myObj);
			switch(choice) {
			 case 1: 
			 	// Rent
                System.out.print("Enter Member ID: ");
                String memberIdRent = myObj.nextLine();

                System.out.print("Enter Equipment ID: ");
                String equipmentIdRent = myObj.nextLine();

                System.out.println("Equipment rented successfully!");
                break;

            case 2: 
				// Return
                System.out.print("Enter Member ID: ");
                String memberIdReturn = myObj.nextLine();

                System.out.print("Enter Equipment ID: ");
                String equipmentIdReturn = myObj.nextLine();

                System.out.println("Equipment returned successfully!");
                break;

            case 3: 
				// Deliver
                System.out.print("Enter Equipment ID: ");
                String equipmentIdDeliver = myObj.nextLine();

                System.out.print("Enter Drone ID: ");
                String droneIdDeliver = myObj.nextLine();

                System.out.println("Equipment delivered successfully!");
                break;

            case 4: 
				// Pickup
                System.out.print("Enter Equipment ID: ");
                String equipmentIdPickup = myObj.nextLine();

                System.out.print("Enter Drone ID: ");
                String droneIdPickup = myObj.nextLine();

                System.out.println("Equipment picked up successfully!");
                break;

            case 5:
                System.out.println("Returning to main menu...");
                break;

            default:
                System.out.println("Invalid choice. Please try again.");
			}
			
		}
	}
	
	// ---------------- Warehouses ------------------ //
	/**
	 * Handles all warehouse inventory operations including:
	 *  - Add, Edit, Delete, Search, and View warehouses 
	 * @param myObj Scanner for user input
	 */
	public static void handleWarehousesMenu(Scanner myObj) {
		int choice = 0;
		ArrayList<String> warehouseMenu = new ArrayList<>();
		warehouseMenu.add("1. Add Warehouse");
		warehouseMenu.add("2. Edit Warehouse");
		warehouseMenu.add("3. Delete Warehouse");
		warehouseMenu.add("4. Search Warehouses");
		warehouseMenu.add("5. View All Warehouses");
		warehouseMenu.add("6. Back to Main Menu");

		while (choice != 6) {
			choice = showMenuAndGetChoice("Warehouse Management", warehouseMenu, myObj);

			switch (choice) {
				case 1:
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

						String sql = "INSERT INTO Warehouse (StreetAddress, City, Phone, ManagerName, StorageCapacity, DroneCapacity) " +
								"VALUES (?, ?, ?, ?, ?, ?)";
						stmt = getConnection().prepareStatement(sql);
						stmt.setString(1, street);
						stmt.setString(2, city);
						stmt.setInt(3, Integer.parseInt(phone));
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
					try {
						System.out.print("Enter street address of warehouse to edit: ");
						String streetEdit = myObj.nextLine().trim();
						System.out.print("Enter city of warehouse to edit: ");
						String cityEdit = myObj.nextLine().trim();

						String sqlSelect = "SELECT * FROM Warehouse WHERE StreetAddress = ? AND City = ?";
						stmt = getConnection().prepareStatement(sqlSelect);
						stmt.setString(1, streetEdit);
						stmt.setString(2, cityEdit);
						rs = stmt.executeQuery();

						if (!rs.next()) {
							System.out.println("Warehouse not found.");
							rs.close();
							stmt.close();
							break;
						}
						rs.close();
						stmt.close();

						System.out.print("Enter new phone number: ");
						String newPhone = myObj.nextLine().trim();
						System.out.print("Enter new manager name: ");
						String newManager = myObj.nextLine().trim();
						System.out.print("Enter new storage capacity: ");
						String newStorage = myObj.nextLine().trim();
						System.out.print("Enter new drone capacity: ");
						String newDroneCap = myObj.nextLine().trim();

						String sqlUpdate = "UPDATE Warehouse SET Phone = ?, ManagerName = ?, StorageCapacity = ?, DroneCapacity = ? " +
								"WHERE StreetAddress = ? AND City = ?";
						stmt = getConnection().prepareStatement(sqlUpdate);
						stmt.setInt(1, Integer.parseInt(newPhone));
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
					try {
						System.out.print("Enter street address of warehouse to delete: ");
						String streetDel = myObj.nextLine().trim();
						System.out.print("Enter city of warehouse to delete: ");
						String cityDel = myObj.nextLine().trim();

						String sqlDelete = "DELETE FROM Warehouse WHERE StreetAddress = ? AND City = ?";
						stmt = getConnection().prepareStatement(sqlDelete);
						stmt.setString(1, streetDel);
						stmt.setString(2, cityDel);

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
					try {
						System.out.print("Enter city to search warehouses: ");
						String citySearch = myObj.nextLine().trim();

						String sqlSearch = "SELECT * FROM Warehouse WHERE City = ?";
						stmt = getConnection().prepareStatement(sqlSearch);
						stmt.setString(1, citySearch);
						rs = stmt.executeQuery();

						boolean found = false;
						System.out.println("Search results:");
						while (rs.next()) {
							System.out.println("Street: " + rs.getString("StreetAddress") +
									", City: " + rs.getString("City") +
									", Phone: " + rs.getInt("Phone") +
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
					try {
						String sqlView = "SELECT * FROM Warehouse";
						stmt = getConnection().prepareStatement(sqlView);
						rs = stmt.executeQuery();

						System.out.println("All warehouses:");
						while (rs.next()) {
							System.out.println("Street: " + rs.getString("StreetAddress") +
									", City: " + rs.getString("City") +
									", Phone: " + rs.getInt("Phone") +
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
					System.out.println("Returning to main menu...");
					break;

				default:
					System.out.println("Invalid choice. Please try again.");
			}
		}
	}
	
	// ---------------- Customers ------------------ //
	/**
 	 * Handles all customer-related operations including:
	 *  - Add, Edit, Delete, Search, and View customers 
	 * @param myObj Scanner for user input
	 */
	public static void handleCustomersMenu(Scanner myObj) {
		int choice = 0;
		ArrayList<String> customerMenu = new ArrayList<>();
		customerMenu.add("1. Add Customer");
		customerMenu.add("2. Edit Customer");
		customerMenu.add("3. Delete Customer");
		customerMenu.add("4. Search Customers");
		customerMenu.add("5. View All Customers");
		customerMenu.add("6. Back to Main Menu");

		while (choice != 6) {
			choice = showMenuAndGetChoice("Customer Management", customerMenu, myObj);

			switch (choice) {
				case 1:
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

						String sql = "INSERT INTO Customer (FirstName, LastName, Email, Address, StartDate, WarehouseDistance, Phone) " +
								"VALUES (?, ?, ?, ?, ?, ?, ?)";
						stmt = getConnection().prepareStatement(sql);
						stmt.setString(1, first);
						stmt.setString(2, last);
						stmt.setString(3, email);
						stmt.setString(4, address);
						stmt.setString(5, startDate);
						stmt.setDouble(6, Double.parseDouble(distance));
						stmt.setInt(7, Integer.parseInt(phone));

						stmt.executeUpdate();
						stmt.close();
						System.out.println("Customer added successfully!");
					} catch (Exception e) {
						System.out.println("Error adding customer: " + e.getMessage());
					}
					break;

				case 2:
					try {
						System.out.print("Enter Customer ID to edit: ");
						String userId = myObj.nextLine().trim();

						String sqlSelect = "SELECT * FROM Customer WHERE UserID = ?";
						stmt = getConnection().prepareStatement(sqlSelect);
						stmt.setInt(1, Integer.parseInt(userId));
						rs = stmt.executeQuery();

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

						String sqlUpdate = "UPDATE Customer SET FirstName = ?, LastName = ?, Email = ?, Address = ?, StartDate = ?, WarehouseDistance = ?, Phone = ? " +
								"WHERE UserID = ?";
						stmt = getConnection().prepareStatement(sqlUpdate);
						stmt.setString(1, firstNew);
						stmt.setString(2, lastNew);
						stmt.setString(3, emailNew);
						stmt.setString(4, addressNew);
						stmt.setString(5, startDateNew);
						stmt.setDouble(6, Double.parseDouble(distanceNew));
						stmt.setInt(7, Integer.parseInt(phoneNew));
						stmt.setInt(8, Integer.parseInt(userId));

						stmt.executeUpdate();
						stmt.close();
						System.out.println("Customer updated successfully!");
					} catch (Exception e) {
						System.out.println("Error editing customer: " + e.getMessage());
					}
					break;

				case 3:
					try {
						System.out.print("Enter Customer ID to delete: ");
						String userIdDel = myObj.nextLine().trim();

						String sqlDelete = "DELETE FROM Customer WHERE UserID = ?";
						stmt = getConnection().prepareStatement(sqlDelete);
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
					try {
						System.out.print("Enter last name to search: ");
						String lastSearch = myObj.nextLine().trim();

						String sqlSearch = "SELECT * FROM Customer WHERE LastName = ?";
						stmt = getConnection().prepareStatement(sqlSearch);
						stmt.setString(1, lastSearch);
						rs = stmt.executeQuery();

						boolean found = false;
						System.out.println("Search results:");
						while (rs.next()) {
							System.out.println("ID: " + rs.getInt("UserID") +
									", Name: " + rs.getString("FirstName") + " " + rs.getString("LastName") +
									", Email: " + rs.getString("Email") +
									", Address: " + rs.getString("Address") +
									", StartDate: " + rs.getString("StartDate") +
									", Distance: " + rs.getDouble("WarehouseDistance") +
									", Phone: " + rs.getInt("Phone"));
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
					try {
						String sqlView = "SELECT * FROM Customer";
						stmt = getConnection().prepareStatement(sqlView);
						rs = stmt.executeQuery();

						System.out.println("All customers:");
						while (rs.next()) {
							System.out.println("ID: " + rs.getInt("UserID") +
									", Name: " + rs.getString("FirstName") + " " + rs.getString("LastName") +
									", Email: " + rs.getString("Email") +
									", Address: " + rs.getString("Address") +
									", StartDate: " + rs.getString("StartDate") +
									", Distance: " + rs.getDouble("WarehouseDistance") +
									", Phone: " + rs.getInt("Phone"));
						}
						rs.close();
						stmt.close();
					} catch (Exception e) {
						System.out.println("Error retrieving customers: " + e.getMessage());
					}
					break;

				case 6:
					System.out.println("Returning to main menu...");
					break;

				default:
					System.out.println("Invalid choice. Please try again.");
			}
		}

	}

	// ---------------- USEFUL REPORTS ------------------ //
    /**
     * Handles the Useful Reports menu and calls the relevant report methods.
     * @param myObj Scanner for user input
     */
    public static void handleReportsMenu(Scanner myObj) {
        int choice = 0;
        ArrayList<String> reportsSubMenu = new ArrayList<String>();
        reportsSubMenu.add("1. Renting Checkouts for a Member");
        reportsSubMenu.add("2. Most Popular Item");
        reportsSubMenu.add("3. Most Popular Manufacturer");
        reportsSubMenu.add("4. Most Used Drone");
        reportsSubMenu.add("5. Member Who Rented the Most Items");
        reportsSubMenu.add("6. Equipment by Type and Year");
        reportsSubMenu.add("7. Back to Main Menu");

        while (choice != 7) {
            System.out.println("\n====== USEFUL REPORTS ======");
            choice = showMenuAndGetChoice("Reports Menu", reportsSubMenu, myObj);

            switch (choice) {
                case 1:
                    rentingCheckouts(myObj);
                    break;
                case 2:
                    popularItem();
                    break;
                case 3:
                    popularManufacturer();
                    break;
                case 4:
                    popularDrone();
                    break;
                case 5:
                    itemsCheckedOut();
                    break;
                case 6:
                    equipmentByTypeAndYear(myObj);
                    break;
                case 7:
                    System.out.println("Returning to main menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }
    
    /**
	 * Report 1: Find the total number of equipment items rented by a single member patron.
	 * @param myObj Scanner for user input
	 */
	public static void rentingCheckouts(Scanner myObj) {
		try {
			System.out.print("Enter Member ID to checkouts for: ");
			String memberId = myObj.nextLine().trim();

			String sql = "SELECT COUNT(EquipmentSN) AS TotalRentedItems FROM Rental WHERE CustomerID = ?";

			stmt = getConnection().prepareStatement(sql);
			stmt.setInt(1, Integer.parseInt(memberId));
			rs = stmt.executeQuery();

			if (rs.next()) {
				int total = rs.getInt("TotalRentedItems");
				System.out.println("\n--- Renting Checkouts Report ---");
				System.out.println("Member ID: " + memberId);
				System.out.println("Total equipment items rented: " + total);
			} else {
				System.out.println("Member ID not found or no items rented."); 
			}

			rs.close();
			stmt.close();

		} catch (NumberFormatException e) {
			System.out.println("Invalid Member ID entered: " + e.getMessage());
		} catch (Exception e) {
			System.out.println("Error running Renting Checkouts report: " + e.getMessage());
		}
	}


	/**
	 * Report 2: Find the most popular item in the database 
	 * (use the renting time of the item and number of times the item has been rented out to calculate).
	 */
	public static void popularItem() {
		try {
			String sql = "SELECT R.EquipmentSN, EM.Model, EM.Description, " +
						"SUM(JULIANDAY(R.DueDate) - JULIANDAY(R.Checkout)) AS TotalRentalDays, " +
						"COUNT(R.EquipmentSN) AS NumTimesRented, " +
						"(SUM(JULIANDAY(R.DueDate) - JULIANDAY(R.Checkout)) * COUNT(R.EquipmentSN)) AS PopularityScore " +
						"FROM Rental R, Equipment E, EquipmentModel EM " +
						"WHERE R.EquipmentSN = E.SerialNumber AND E.Model = EM.Model " +
						"GROUP BY R.EquipmentSN, EM.Model, EM.Description " +
						"ORDER BY PopularityScore DESC, NumTimesRented DESC " +
						"LIMIT 1";

			stmt = getConnection().prepareStatement(sql);
			rs = stmt.executeQuery();

			System.out.println("\n--- Most Popular Item Report ---");
			if (rs.next()) {
				// Retrieve the correctly aliased columns
				String equipmentSN = rs.getString("EquipmentSN");
				String model = rs.getString("Model");
				String description = rs.getString("Description");
				int numTimesRented = rs.getInt("NumTimesRented");

				System.out.println("Most Popular Item:");
				System.out.println("  - Equipment Serial Number: " + equipmentSN);
				System.out.println("  - Model Name: " + model + " (" + description + ")");
				System.out.println("  - Times Rented: " + numTimesRented);
			} else {
				System.out.println("No equipment rentals found.");
			}

			rs.close();
			stmt.close();

		} catch (Exception e) {
			System.out.println("Error running Most Popular Item report: " + e.getMessage());
		}
	}


	/**
	 * Report 3: Find the most frequent equipment manufacturer in the database 
	 * (i.e. the one who has had the most rented units).
	 */
	public static void popularManufacturer() {
		try {
			String sql = "SELECT EM.Manufacturer, COUNT(R.EquipmentSN) AS TotalRentedUnits " +
						"FROM Rental R, Equipment E, EquipmentModel EM " +
						"WHERE R.EquipmentSN = E.SerialNumber " +
						"AND E.Model = EM.Model " +
						"GROUP BY EM.Manufacturer " +
						"ORDER BY TotalRentedUnits DESC, EM.Manufacturer ASC " +
						"LIMIT 1";

			stmt = getConnection().prepareStatement(sql);
			rs = stmt.executeQuery();

			System.out.println("\n--- Most Popular Manufacturer Report ---");
			if (rs.next()) {
				String manufacturer = rs.getString("Manufacturer");
				int totalRentedUnits = rs.getInt("TotalRentedUnits");

				System.out.println("Most Frequent Equipment Manufacturer:");
				System.out.println("  - Manufacturer: " + manufacturer);
				System.out.println("  - Total Rented Units: " + totalRentedUnits);
			} else {
				System.out.println("No equipment rentals found to determine manufacturer popularity.");
			}

			rs.close();
			stmt.close();

		} catch (Exception e) {
			System.out.println("Error running Most Popular Manufacturer report: " + e.getMessage());
		}
	}


	/**
	 * Report 4: Find the most used drone in the database 
	 */
	public static void popularDrone() {
		try {
			String sql = "SELECT DM.Model, DM.Manufacturer, DM.DistanceAutonomy AS MaxFlyingDistance, " +
						"COUNT(S.ShipmentID) AS DeliveryCount, " + 
						"(COUNT(S.ShipmentID) * DM.DistanceAutonomy) AS PopularityScore " + 
						"FROM DroneModel AS DM, Drone AS D, Shipment AS S " +
						"WHERE D.Model = DM.Model AND D.ShipmentID = S.ShipmentID " +
						"GROUP BY DM.Model, DM.Manufacturer, DM.DistanceAutonomy " + 
						"ORDER BY PopularityScore DESC LIMIT 1";

			stmt = getConnection().prepareStatement(sql);
			rs = stmt.executeQuery();

			System.out.println("\n--- Most Used Drone Report ---");
			if (rs.next()) {
				// Retrieve the correct columns that the SQL query outputs
				String model = rs.getString("Model");
				String manufacturer = rs.getString("Manufacturer");
				int deliveryCount = rs.getInt("DeliveryCount"); 
				
				System.out.println("Most Popular Drone Model:");
				System.out.println("  - Model: " + model + " (" + manufacturer + ")");
				System.out.println("  - Total Deliveries: " + deliveryCount);
			} else {
				System.out.println("No drone delivery data found.");
			}

			rs.close();
			stmt.close();

		} catch (Exception e) {
			System.out.println("Error running Most Used Drone report: " + e.getMessage());
		}
	}

	/**
	 * Report 5: Find the member who has rented out the most items and the total number of items they have rented out.
	 */
	public static void itemsCheckedOut() {
		try {
			String sql = "SELECT CustomerID, COUNT(EquipmentSN) AS TotalRentedItems " +
						"FROM Rental " +
						"GROUP BY CustomerID " +
						"ORDER BY TotalRentedItems DESC, CustomerID ASC " +
						"LIMIT 1";

			stmt = getConnection().prepareStatement(sql);
			rs = stmt.executeQuery();

			System.out.println("\n--- Top Renting Member Report ---");
			if (rs.next()) {
				int customerId = rs.getInt("CustomerID");
				int total = rs.getInt("TotalRentedItems");

				System.out.println("Member who has rented the most items:");
				System.out.println("  - Member ID (CustomerID): " + customerId);
				System.out.println("  - Total Items Rented: " + total);
			} else {
				System.out.println("No rental history found.");
			}

			rs.close();
			stmt.close();

		} catch (Exception e) {
			System.out.println("Error running Top Renting Member report: " + e.getMessage());
		}
	}


	/**
	 * Report 6: Find the description (name) of equipment by type released before YEAR.
	 */
	public static void equipmentByTypeAndYear(Scanner myObj) {
		try {
			System.out.print("Enter Equipment Model (Type) Name (e.g., 'Camera'): ");
			String modelName = myObj.nextLine().trim();
			System.out.print("Enter Year (e.g., 2020) to find equipment released BEFORE: ");
			String year = myObj.nextLine().trim();

			String sql = "SELECT E.SerialNumber, EM.Description AS TypeDescription, E.Year " +
						"FROM Equipment E, EquipmentModel EM " +
						"WHERE E.Model = EM.Model AND E.Model = ? AND E.Year < ?";

			stmt = getConnection().prepareStatement(sql);
			stmt.setString(1, modelName); 
			stmt.setInt(2, Integer.parseInt(year));
			rs = stmt.executeQuery();

			System.out.println("\n--- Equipment of Model '" + modelName + "' Released Before " + year + " ---");
			boolean found = false;
			while (rs.next()) {
				System.out.println("  - Equipment Serial Number: " + rs.getString("SerialNumber") +
								" (Type: " + rs.getString("TypeDescription") +
								", Release Year: " + rs.getInt("Year") + ")");
				found = true;
			}

			if (!found) {
				System.out.println("No equipment of Model '" + modelName + "' found released before year " + year + ".");
			}

			rs.close();
			stmt.close();

		} catch (NumberFormatException e) {
			System.out.println("Invalid Year entered: " + e.getMessage());
		} catch (Exception e) {
			System.out.println("Error running Equipment by Type and Year report: " + e.getMessage());
		}
	}
	
	public static void main(String[] args) {
		
		Scanner myObj = new Scanner(System.in);
		
		// Build main menu options
		ArrayList<String> mainMenu = new ArrayList<String>();
		mainMenu.add("1. Manage Drone Inventory");
		mainMenu.add("2. Manage Equipment Inventory");
		mainMenu.add("3. Equipment Operations ");
		mainMenu.add("4. Manage Warehouses");
		mainMenu.add("5. Manage Customers");
		mainMenu.add("6. Reports");
		mainMenu.add("7. Exit");
		
		int choice = 0;
		
		
		while ( choice != 7) {
			choice = showMenuAndGetChoice ("Main Menu", mainMenu, myObj);
			
			switch(choice) {
			case 1:
				handleDroneInventoryMenu(myObj);
				break;
			case 2:
				handleEquipmentInventoryMenu(myObj);
				break;
			case 3:
				handleEquipmentOperationsMenu(myObj);
				break;
			case 4:
				handleWarehousesMenu(myObj);
				break;
			case 5:
				handleCustomersMenu(myObj);
				break;
			case 6: 
				handleReportsMenu(myObj);
				break;
			case 7:
				System.out.println("Exited the Program!!");
				break;
			default:
				System.out.println("Invalid choice. Please try again.");
				break;
			}
		}
		myObj.close();
	}

}
