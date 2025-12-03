package dbs.project.options;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Scanner;

import dbs.project.InventoryManagementSystem;

public class handleReportsMenu {
    
    public static void main(Scanner args, Connection connection) {
        handleReports(args, connection);
        args.close();
    }

    
	// ---------------- USEFUL REPORTS ------------------ //
    /**
     * Handles the Useful Reports menu and calls the relevant report methods.
     * @param myObj Scanner for user input
     */
    public static void handleReports(Scanner myObj, Connection connection) {    
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
            choice = InventoryManagementSystem.showMenuAndGetChoice("Reports Menu", reportsSubMenu, myObj);

            switch (choice) {
                case 1:
                    rentingCheckouts(myObj, connection);
                    break;
                case 2:
                    popularItem(connection);
                    break;
                case 3:
                    popularManufacturer(myObj, connection);
                    break;
                case 4:
                    popularDrone(connection);
                    break;
                case 5:
                    itemsCheckedOut(connection);
                    break;
                case 6:
                    equipmentByTypeAndYear(myObj, connection);
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
	public static void rentingCheckouts(Scanner myObj, Connection connection)  {
		try {
			System.out.print("Enter Member ID to checkouts for: ");
			String memberId = myObj.nextLine().trim();

			String sql = "SELECT COUNT(EquipmentSN) AS TotalRentedItems FROM Rental WHERE CustomerID = ?";

			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setInt(1, Integer.parseInt(memberId));
			ResultSet rs = stmt.executeQuery();

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
	public static void popularItem(Connection connection) {
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

			PreparedStatement stmt = connection.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();

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
	public static void popularManufacturer(Scanner myObj, Connection connection) {
		try {
			String sql = "SELECT EM.Manufacturer, COUNT(R.EquipmentSN) AS TotalRentedUnits " +
						"FROM Rental R, Equipment E, EquipmentModel EM " +
						"WHERE R.EquipmentSN = E.SerialNumber " +
						"AND E.Model = EM.Model " +
						"GROUP BY EM.Manufacturer " +
						"ORDER BY TotalRentedUnits DESC, EM.Manufacturer ASC " +
						"LIMIT 1";

			PreparedStatement stmt = connection.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();

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
	public static void popularDrone(Connection connection)  {
		try {
			String sql = "SELECT DM.Model, DM.Manufacturer, DM.DistanceAutonomy AS MaxFlyingDistance, " +
						"COUNT(S.ShipmentID) AS DeliveryCount, " + 
						"(COUNT(S.ShipmentID) * DM.DistanceAutonomy) AS PopularityScore " + 
						"FROM DroneModel AS DM, Drone AS D, Shipment AS S " +
						"WHERE D.Model = DM.Model AND D.ShipmentID = S.ShipmentID " +
						"GROUP BY DM.Model, DM.Manufacturer, DM.DistanceAutonomy " + 
						"ORDER BY PopularityScore DESC LIMIT 1";

			PreparedStatement stmt = connection.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();

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
	public static void itemsCheckedOut(Connection connection) {
		try {
			String sql = "SELECT CustomerID, COUNT(EquipmentSN) AS TotalRentedItems " +
						"FROM Rental " +
						"GROUP BY CustomerID " +
						"ORDER BY TotalRentedItems DESC, CustomerID ASC " +
						"LIMIT 1";

			PreparedStatement stmt = connection.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();

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
	public static void equipmentByTypeAndYear(Scanner myObj, Connection connection)  {
		try {
			System.out.print("Enter Equipment Model (Type) Name (e.g., 'Camera'): ");
			String modelName = myObj.nextLine().trim();
			System.out.print("Enter Year (e.g., 2020) to find equipment released BEFORE: ");
			String year = myObj.nextLine().trim();

			String sql = "SELECT E.SerialNumber, EM.Description AS TypeDescription, E.Year " +
						"FROM Equipment E, EquipmentModel EM " +
						"WHERE E.Model = EM.Model AND E.Model = ? AND E.Year < ?";

			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, modelName); 
			stmt.setInt(2, Integer.parseInt(year));
			ResultSet rs = stmt.executeQuery();

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

}
