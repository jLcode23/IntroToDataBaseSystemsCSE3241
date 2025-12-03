package dbs.project.options;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Scanner;
import java.sql.ResultSet;

import dbs.project.InventoryManagementSystem;



public class handleEquipmentOperationsMenu {
    
     public static void main(Scanner args, Connection connection) {
        handleEquipmentOperations(args, connection);
        args.close();
    }

    /**
	 * Handles operations related to equipment transactions:
	 * Renting, Returning, Delivering, and Picking up equipment 
	 * 
	 * @param myObj  Scanner for user input 
	 */
	// ---------------- EQUIPMENT OPERARIONS ------------------ //
	public static void handleEquipmentOperations(Scanner myObj, Connection connection) { 
		int choice = 0;
		ArrayList<String> equipmentsOpsSubMenu = new ArrayList<String>();
		equipmentsOpsSubMenu.add("1. Rent Equipment");
		equipmentsOpsSubMenu.add("2. Return Equipment");
		equipmentsOpsSubMenu.add("3. Deliver Equipment");
		equipmentsOpsSubMenu.add("4. Pickup Equipment");
		equipmentsOpsSubMenu.add("5. Back to Main Menu");
		
		while ( choice != 5) {
			choice = InventoryManagementSystem.showMenuAndGetChoice("Equipment Operations", equipmentsOpsSubMenu, myObj);
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
}
