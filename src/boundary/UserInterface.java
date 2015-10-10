package boundary;

import java.util.*;

import controller.*;

import entity.*;

public class UserInterface {

	/**
	 * Indicate the keyboard
	 */
	private static final Scanner KEYBOARD = new Scanner(System.in);

	/**
	 * Indicate the warehouse object
	 */
	private static final Warehouse WAREHOUSE = Warehouse.getWarehouse();

	/**
	 * Indicate the current user, either administrator or the customer
	 */
	private User user;

	/**
	 * default constructor, make the current user null
	 */
	public UserInterface() {
		user = null;
	}

	/**
	 * To perform a menu screen for administrator to interact with
	 */
	private void adminMainMenuScreen() {
		String choice;
		do {
			printLine();
			System.out.println(user.getId() + ", Welcom to the XXX Warehouse(admin)");
			System.out.println("Menu:");
			System.out.println("\t1.Create section");
			System.out.println("\t2.Edit Section");
			System.out.println("\t3.Remove Section");
			System.out.println("\t4.View sections");
			System.out.println("\t5.Allocate items");
			System.out.println("\t6.Remove items");
			System.out.println("\tx.Logout");
			System.out.print("your choice:");
			choice = readUserInput();
			switch (choice) {
			case "1":
				createSection();
				break;
			case "2":
				showAllSection(false);
				editSection();
				break;
			case "3":
				showAllSection(false);
				removeSection();
				break;
			case "4":
				showAllSection(true);
				break;
			case "5":
				allocateItem();
				break;
			case "6":
				removeItem();
				break;
			case "x":
				logout();
				return;
			default:
				System.out.println("Invalid input, please do it again");
				break;
			}
		} while (!choice.equals("x"));
	}

	/**
	 * To perform an input interface for collect necessary info for allocate
	 * operation
	 */
	private void allocateItem() {
		if (WAREHOUSE.isEmpty()) {
			System.out.println("There is no section in this warehouse right now.");
			return;
		}
		// TODO change to allocatable ones.
		showAllOrders();
		System.out.print("Please input an order id: ");
		int orderId = readUserInputInt();
		try {
			int unallocateNumber = WAREHOUSE.allocateItem(orderId);
			if (unallocateNumber == 0) {
				System.out.println("Allocate successful");
			} else {
				int palletNumber = (unallocateNumber % 75 == 0 ? unallocateNumber / 75 : unallocateNumber / 75 + 1);
				System.out.println("Still need " + palletNumber + " pallet(s)");
				System.out.println("Allocate not finished, no space in any section, please create new section");
			}
		} catch (RuntimeException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * To perform an input interface for collect necessary info for create
	 * section
	 */
	private void createSection() {
		System.out.print("Please input section capacity:");
		int capacity = readUserInputInt();
		int sectionId = WAREHOUSE.createSection(capacity);
		if (sectionId == -1) {
			System.out.println("System error, please try later");
		} else {
			System.out.println("Create section successful!");
			System.out.println("The section id is " + sectionId);
		}
	}

	/**
	 * To perform a menu screen for customer to interact with
	 */
	private void customerMainMenuScreen() {
		String choice;
		do {
			printLine();
			System.out.println(user.getId() + ", Welcom to the XXX Warehouse");
			System.out.println("Menu:");
			System.out.println("\t1.Make order");
			System.out.println("\t2.View order status");
			System.out.println("\tx.Logout");
			System.out.print("your choice:");
			choice = readUserInput();
			switch (choice) {
			case "1":
				makeOrder();
				break;
			case "2":
				viewOrderScreen();
				break;
			case "x":
				logout();
				return;
			default:
				System.out.println("Invalid input, please do it again");
				break;
			}
		} while (!choice.equals("x"));
	}

	/**
	 * To perform an input interface for collect necessary info for edit Section
	 */
	private void editSection() {
		if (WAREHOUSE.isEmpty()) {
			return;
		}
		System.out.print("Please input section id:");
		int sectionId = readUserInputInt();
		System.out.print("Please input the new capacity");
		int newCapacity = readUserInputInt();

		try {
			WAREHOUSE.editSection(sectionId, newCapacity);
			System.out.println("Edit successfully!");
		} catch (RuntimeException e) {
			System.out.println("Error:");
			System.out.println(e.getMessage());
		}
	}

	/**
	 * To perform a login screen for every user(including administrator as well
	 * as customers) to interact with
	 */
	private void loginScreen() {
		printLine();
		System.out.println("Enter your ID & password to login");
		System.out.print("ID:");
		String id = readUserInput();
		System.out.print("Password:");
		String pwd = readUserInput();
		try {
			if ((user = WAREHOUSE.login(id, pwd)) != null) {
				if (user instanceof Customer) {
					System.out.println("Login as customer successful!");
					customerMainMenuScreen();
				} else if (user instanceof Administrator) {
					System.out.println("Login as administrator successful!");
					adminMainMenuScreen();
				}
			} else {
				System.out.println("Wrong password");
			}
		} catch (RuntimeException e) {
			System.out.println(e.getMessage());
		}

	}

	/**
	 * Perform a logout function, show a message and clear the current user
	 */
	private void logout() {
		user.logout();
		System.out.println("Thanks for using our service, have a nice day!");
		user = null;
	}

	/**
	 * To perform an input interface for collect necessary info for make order
	 */
	private void makeOrder() {
		System.out.print("Please input the number of item(s): ");
		int itemNumber = readUserInputInt();
		int orderId = WAREHOUSE.makeOrder((Customer) user, itemNumber);
		if (orderId == -1) {
			System.out.println("Please input a number greater than 0(0 not include).");
		} else {
			System.out.println("Your order id is: " + orderId);
		}
	}

	/**
	 * print a split line, can be changed to clear screen if run in other
	 * platform
	 */
	private void printLine() {
		System.out.println("===============================");
	}

	/**
	 * Read the user input for a boolean value, with appropriate instructions,
	 * make sure the input can be change to a boolean
	 * 
	 * @return The boolean value that the user indicate.
	 */
	private boolean readUserBoolean() {
		String[] yes = { "y", "yes", "ok" };
		String[] no = { "n", "no" };
		String choice;
		while (true) {
			choice = readUserInput().toLowerCase();
			if (Arrays.asList(yes).contains(choice)) {
				return true;
			} else if (Arrays.asList(no).contains(choice)) {
				return false;
			} else {
				System.out.println("Invalid input, the valid inputs are:");
				System.out.println("to agree");
				for (int i = 0; i < yes.length; i++) {
					System.out.print(yes[i] + " ");
				}
				System.out.println("or to disagree");
				for (int i = 0; i < no.length; i++) {
					System.out.println(no[i] + " ");
				}
			}
		}
	}

	/**
	 * Read the user input for a String value, with appropriate instructions,
	 * make sure the input valid and not empty
	 * 
	 * @return The String value that the user indicate.
	 */
	private String readUserInput() {
		try {
			String str = KEYBOARD.nextLine();
			if (!str.isEmpty())
				return str;
			else {
				System.out.println("No empty input please");
				return readUserInput();
			}
		} catch (NoSuchElementException e) {
			System.out.println(e.getMessage());
			return readUserInput();
		}
	}

	/**
	 * Read the user input for an integer value, with appropriate instructions,
	 * make sure the input can be change to a integer
	 * 
	 * @return The integer value that the user indicate.
	 */
	private int readUserInputInt() {
		try {
			return Integer.parseInt(readUserInput());
		} catch (NumberFormatException e) {
			System.out.println("Please input just integer");
			return readUserInputInt();
		}
	}

	/**
	 * To perform a register screen for customer to interact with
	 */
	private void registerScreen() {
		printLine();
		System.out.print("ID:");
		String userinput = readUserInput();
		String id = userinput.trim();
		if (!userinput.equals(id)) {
			System.out.println("Leading and(or) trailing whitespace has been removed");
		}
		System.out.print("Password:");
		String pwd = readUserInput();
		if (WAREHOUSE.register(id, pwd)) {
			System.out.println("Register successfully");
			loginScreen();
		} else {
			System.out.println("ID already exist, please try another one");
		}
	}

	/**
	 * To perform an input interface for collect necessary info for remove item
	 */
	private void removeItem() {
		// TODO change condition
		if (WAREHOUSE.isEmpty()) {
			System.out.println("There is nothing to be removed in this warehouse right now.");
			return;
		}
		// Change to removeable ones
		showAllOrders();
		System.out.print("Please input an order id: ");
		int orderId = readUserInputInt();
		try {
			if (WAREHOUSE.removeItem(orderId))
				System.out.println("Revmoe operation done!");
			else
				System.out.println("Error");
		} catch (RuntimeException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * To perform an input interface for collect necessary info for remove
	 * section
	 */
	private void removeSection() {
		if (WAREHOUSE.isEmpty()) {
			return;
		}
		System.out.print("Please input the section ID: ");
		int sectionId = readUserInputInt();
		try {
			if (WAREHOUSE.removeSection(sectionId)) {
				System.out.println("Removed successfully");
			} else {
				System.out.println("Remove fail.");
			}
		} catch (RuntimeException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Display all orders in the system
	 */
	private void showAllOrders() {
		System.out.println(WAREHOUSE.allOrdersToString());
	}

	/**
	 * Display all Sections, and an optional pallte info to the user.
	 * 
	 * @param displayPallet
	 *            Indicate whether to dispaly the pallet info, being true to
	 *            display, false to not display
	 */
	private void showAllSection(boolean displayPallet) {
		if (WAREHOUSE.isEmpty()) {
			System.out.println("There is no section in this warehouse right now.");
		} else {
			System.out.println(WAREHOUSE.allSectionsToString(displayPallet));
		}
	}

	/**
	 * Start the system.
	 */
	public void start() {
		welcomeScreen();
	}

	/**
	 * Display all orders in the param orderMap
	 * 
	 * @param orderMap
	 *            The orders for the current user in a map format to be
	 *            displayed
	 */
	private void viewAllOrders(HashMap<Integer, Order> orderMap) {
		for (Order order : orderMap.values()) {
			System.out.println(order);
		}
	}

	/**
	 * To perform a view order screen for customer to interact with
	 */
	private void viewOrderScreen() {
		String choice;
		HashMap<Integer, Order> orderMap = new HashMap<Integer, Order>();
		for (Order order : ((Customer) user).getOrderList()) {
			orderMap.putIfAbsent(order.getId(), order);
		}
		if (orderMap.size() == 0) {
			System.out.println("Sorry, you have no order");
			return;
		}
		do {
			printLine();
			System.out.println("View Order");
			System.out.println("Menu: ");
			System.out.println("\t1.View All");
			System.out.println("\t2.Input Order Id");
			System.out.println("\tx.Cancel");
			System.out.print("your choice:");
			choice = readUserInput();
			switch (choice) {
			case "1":
				viewAllOrders(orderMap);
				break;
			case "2":
				viewSelectedOrder(orderMap);
				break;
			case "x":
				break;
			default:
				System.out.println("invalid input, please do it again");
				break;
			}
		} while (!choice.equals("x"));

	}

	/**
	 * Perform an input for the user to specific an order to display
	 * 
	 * @param orderMap
	 *            The orders for the current user in a map format to be
	 *            displayed
	 */
	private void viewSelectedOrder(HashMap<Integer, Order> orderMap) {
		do {
			System.out.println("Please input order id");
			int orderId = readUserInputInt();
			if (!orderMap.containsKey(orderId)) {
				System.out.println("Sorry, you have no such order");
			} else {
				System.out.println("The order detail:");
				System.out.println(orderMap.get(orderId));
			}
			System.out.println("Continue check another?");
		} while (readUserBoolean());
	}

	/**
	 * To perform a welcome screen for users to register or login
	 */
	private void welcomeScreen() {
		String choice;
		do {
			printLine();
			System.out.println("Welcome to the XXX Warehouse");
			System.out.println("\tplease login or regist");
			System.out.println("\t1.Login");
			System.out.println("\t2.Register as customer");
			System.out.print("your choice:");
			choice = readUserInput().toLowerCase();
			switch (choice) {
			case "1":
				loginScreen();
				break;
			case "2":
				registerScreen();
				break;
			case "x":
				System.out.println("Shutting down.");
				break;
			default:
				System.out.println("Invalid input, please do it again");
				break;
			}
		} while (!choice.equals("x"));
	}

}
