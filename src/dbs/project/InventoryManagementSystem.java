package dbs.project;

import java.util.ArrayList;
import java.util.Scanner;


public class InventoryManagementSystem {
	private static ArrayList<String> droneList = new ArrayList<String>();
	private static ArrayList<String> equipmentList = new ArrayList<String>();
	
	
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
				// Add new drone
				System.out.print("Enter drone name (e.g., DroneA): ");
				String newDrone = myObj.nextLine().trim();
				System.out.print("Enter model: ");
				String model = myObj.nextLine().trim();
				System.out.print("Enter serial number: ");
				String serial = myObj.nextLine().trim();
				droneList.add(newDrone + " | " + model + " | " + serial);
				break;
			
			case 2:
				// Edit existing drone 
				System.out.println("Enter serial number of drone to edit: ");
				String serialToEdit = myObj.nextLine().trim();
				boolean edited = false;
				for ( int i = 0; i < droneList.size(); i++) {
					if ( droneList.get(i).contains(serialToEdit)) {
						System.out.println("Found: "+ droneList.get(i));
						System.out.print("Enter new drone name: ");
						String updateName = myObj.nextLine().trim();
						System.out.print("Enter new model: ");
						String updateModel = myObj.nextLine().trim();
						droneList.set(i, updateName + " | " + updateModel + " | " + serialToEdit);
						System.out.println("Drone updated successfully!");
						edited = true;
						break;
					}
				}
				if (!edited) {
					System.out.println("Drone with Serial '" + serialToEdit + " ' not found.");
				}
				break;
				
			case 3:
				// Delete drone by serial number
				System.out.println("Enter serial number of drone to delete: ");
				String serialToDelete = myObj.nextLine().trim();
				boolean removed = false;
				for ( int i = 0; i < droneList.size(); i++) {
					if ( droneList.get(i).contains(serialToDelete)) {
						droneList.remove(i);
						System.out.println("Drone deleted successfully!");
						removed = true;
						break;
					}
				}
				if ( !removed) {
					System.out.println("Drone with Serial '" + serialToDelete + " ' not found.");
				}
				break;
				
			case 4:
				// Search for drones by keyword
				System.out.println("Enter keyword to search (name or model or serial): ");
				String keyword = myObj.nextLine().trim().toLowerCase();
				System.out.println("Search results: ");
				boolean found = false;
				for ( int i = 0; i < droneList.size(); i++) {
					String rec = droneList.get(i);
					if ( rec.toLowerCase().contains(keyword)) {
						System.out.println(" - "+ rec);
						found = true;
					}
				}
				if ( !found) {
					System.out.println("No matches found.");
				}
				break;
			case 5: 
				// View all drones
				System.out.println("All drone in inventory");
				if ( droneList.isEmpty()) {
					System.out.println("(none)");
				} else {
					for ( int i = 0; i < droneList.size(); i++) {
						System.out.println((i+1) + ". "+ droneList.get(i));
					}
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
					// Add new equipment 
					System.out.print("Enter equipment description (e.g., Drill): ");
					String desc = myObj.nextLine().trim();
					System.out.print("Enter equipment type (e.g., Tools): ");
					String type = myObj.nextLine().trim();
					System.out.print("Enter serial number: ");
					String serial = myObj.nextLine().trim();
					equipmentList.add(desc + " | " + type + " | " + serial);
					break;
				case 2:
					// Edit existing equipment 
					System.out.println("Enter serial number of equipment to edit: ");
					String serialToEdit = myObj.nextLine().trim();
					boolean edited = false;
					for ( int i = 0; i < equipmentList.size(); i++) {
						if ( equipmentList.get(i).contains(serialToEdit)) {
							System.out.println("Found: "+ equipmentList.get(i));
							System.out.print("Enter new description: ");
							String newDesc = myObj.nextLine().trim();
							System.out.print("Enter new type: ");
							String newType = myObj.nextLine().trim();
							equipmentList.set(i, newDesc + " | " + newType + " | " + serialToEdit);
							System.out.println("Equipment updated successfully!");
							edited = true;
							break;
						}
					}
					if (!edited) {
						System.out.println("Equipment with Serial '" + serialToEdit + " ' not found.");
					}
					break;
					
				case 3:
					// Delete equipment by serial number 
					System.out.println("Enter serial number of equipment to delete: ");
					String serialToDelete = myObj.nextLine().trim();
					boolean removed = false;
					for ( int i = 0; i < equipmentList.size(); i++) {
						if ( equipmentList.get(i).contains(serialToDelete)) {
							equipmentList.remove(i);
							System.out.println("Equipment deleted successfully!");
							removed = true;
							break;
						}
					}
					if ( !removed) {
						System.out.println("Equipment with Serial '" + serialToDelete + " ' not found.");
					}
					break;
				case 4:
					//Search equipment by keyword 
					System.out.println("Enter keyword to search equipment: ");
					String searchKeyword = myObj.nextLine().trim().toLowerCase();
					System.out.println("Search results: ");
					boolean found = false;
					for ( int i = 0; i < equipmentList.size(); i++) {
						String eq = equipmentList.get(i);
						if ( eq.toLowerCase().contains(searchKeyword)) {
							System.out.println(" - "+ eq);
							found = true;
						}
					}
					if ( !found) {
						System.out.println("No matches found.");
					}
					break;
				case 5: 
					//View all equipment 
					System.out.println("All equipment in inventory");
					if ( equipmentList.isEmpty()) {
						System.out.println("(none)");
					} else {
						for ( int i = 0; i < equipmentList.size(); i++) {
							System.out.println((i+1) + ". "+ equipmentList.get(i));
						}
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
				System.out.println("Equipment rented successfully!");
				break;
			case 2:
				System.out.println("Equipment returned sucessfully!");
				break;
				
			case 3:
				System.out.println("Equipment delivered sucessfully!");
				break;
			case 4:
				System.out.println("Equipment picked up sucessfully!");
				break;
			case 5: 
				System.out.println("Returning to main menu...");
				break;
			default:
				System.out.println("Invalid choice. Please try again.");
				break;
			}
			
		}
	}
	
	/**
	 * Placeholder for managing warehouses 
	 * (Future feature development)
	 * 
	 * @param myObj Scanner for user input 
	 */
	// ---------------- Warehouses ------------------ //
	public static void handleWarehousesMenu(Scanner myObj) {
	
		System.out.println("\nManaging Warehouses...");
		System.out.println("Feature Coming Soon! ");
	}
	
	/**
	 * Placeholder for managing customer-related functionality 
	 * (Future feature under development)
	 * 
	 * @param myObj
	 */
	// ---------------- Customers ------------------ //
	public static void handleCustomersMenu(Scanner myObj) {
		System.out.println("\nManaging Customers...");
		System.out.println("Feature Coming Soon! ");
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
		mainMenu.add("6. Exit");
		
		int choice = 0;
		
		
		while ( choice != 6) {
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
