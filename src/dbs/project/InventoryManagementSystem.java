package dbs.project;
import java.util.ArrayList;
import java.util.Scanner;

import dbs.project.options.handleDroneInventoryMenu;
import dbs.project.options.handleEquipmentInventoryMenu;
import dbs.project.options.handleEquipmentOperationsMenu;
import dbs.project.options.handleWarehousesMenu;
import dbs.project.options.handleCustomersMenu;
import dbs.project.options.handleReportsMenu;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;



public class InventoryManagementSystem {

	private static final String DATABASE= "company.db";
	private static Connection conn = null;
	

	/**
     * Connects to the database if it exists, creates it if it does not, and
     * returns the connection object.
     *
     * @param databaseFileName
     *            the database file name
     * @return a connection object to the designated database
     */
    public static Connection initializeDB(String databaseFileName) {
        /**
         * The "Connection String" or "Connection URL".
         *
         * "jdbc:sqlite:" is the "subprotocol". (If this were a SQL Server
         * database it would be "jdbc:sqlserver:".)
         */
        String url = "jdbc:sqlite:" + databaseFileName;
        Connection conn = null; 
        try {
            conn = DriverManager.getConnection(url);
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out
                        .println("The driver name is " + meta.getDriverName());
                System.out.println(
                        "The connection to the database was successful.");
            } else {
                System.out.println("Null Connection");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out
                    .println("There was a problem connecting to the database.");
        }
        return conn;
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
	
	
	public static void mainMenu(Scanner myObj, Connection conn) {
				
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
				handleDroneInventoryMenu.handleDroneInventory(myObj, conn);
				break;
			case 2:
				handleEquipmentInventoryMenu.handleEquipmentInventory(myObj, conn);
				break;
			case 3:
				handleEquipmentOperationsMenu.handleEquipmentOperations(myObj, conn);
				break;
			case 4:
				handleWarehousesMenu.handleWarehouses(myObj, conn);
				break;
			case 5:
				handleCustomersMenu.handleCustomers(myObj, conn);
				break;
			case 6: 
				handleReportsMenu.handleReports(myObj, conn);
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

	public static void main(String[] args) {
		conn = initializeDB(DATABASE);

        Scanner cin = new Scanner(System.in);
        mainMenu(cin, conn);

        cin.close();
        try {
            conn.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        System.out.println("Bye");
    }
		

}
