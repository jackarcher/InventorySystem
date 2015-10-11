package controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import IO.IO;
import comparator.ComparatorForSection;
import entity.Administrator;
import entity.Customer;
import entity.Order;
import entity.Pallet;
import entity.Section;
import entity.User;

/**
 * Warehouse references the whole inventory system.
 * 
 * @author Archer
 *
 */
public class Warehouse implements Serializable {

	/**
	 * Line separator, for constructor a new line within a String or
	 * StringBuffer
	 */
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");

	/**
	 * Indicate the only one warehouse object.
	 */
	private static Warehouse warehouse;

	// Singleton pattern
	/**
	 * By call this method, an outside class can access the warehouse object.
	 * 
	 * @return The only warehouse object in the system.
	 */
	public static Warehouse getWarehouse() {
		if (warehouse == null) {
			warehouse = IO.readFromFile();
			if (warehouse == null) {
				warehouse = new Warehouse();
			}
		}
		return warehouse;
	}

	/**
	 * The administrator of the system.
	 */
	private Administrator administrator;
	/**
	 * The collection of all customers
	 */
	private HashMap<String, Customer> customerMap;
	/**
	 * The collection of all orders
	 */
	private HashMap<Integer, Order> orderMap;
	/**
	 * The collection of all sections
	 */
	private HashMap<Integer, Section> sectionMap;

	/**
	 * The constructor of this class, initialize all the collections data. Some
	 * map data are initialized by given capacity, which is recommended by the
	 * java API.
	 */
	private Warehouse() {
		administrator = new Administrator("admin", "admin", "J.Archer");
		orderMap = new HashMap<Integer, Order>(100);
		sectionMap = new HashMap<Integer, Section>(2000);
		customerMap = new HashMap<String, Customer>(20);
	}

	/**
	 * Allocate an order indicated by the given orderId. This method will first
	 * allocate the order to the pallets belong to its customer and not full. If
	 * still not enough, the method will allocate the rest part to the empty
	 * pallets in the warehouse. The rule is: allocate from the section that has
	 * most free space to the least free space one.
	 * 
	 * @param orderId
	 *            Refers the order
	 * @return The number of unallocated items, and 0 if allocated successfully
	 * @throws RuntimeException
	 *             This exception may be thrown for the following reason: 1. No
	 *             order match the given order ID. 2. Given order is not
	 *             suitable for allocate. 3. Given order has no match customer
	 * 
	 */
	public int allocateItem(int orderId) throws RuntimeException {
		Order order = findOrderById(orderId);
		if (!order.allocateable())
			throw new RuntimeException("Order is not sutiable for allocate operation");
		Customer customer = findCustomerByOrder(order);
		if (customer == null)
			throw new RuntimeException("No match info for this order");
		int unallocatedNumber = order.getUnallocatedNumber();
		// allocate to the existing pallet.
		// get the storage map for this customer
		HashMap<Integer, ArrayList<Integer>> storageMap = customer.getStorageMap();
		// go through every section
		for (Integer sectionId : storageMap.keySet()) {
			Section section = findSectionById(sectionId);
			ArrayList<Integer> palletIdList = storageMap.get(sectionId);
			// go through every pallet
			for (Integer palletId : palletIdList) {
				Pallet pallet = section.getPalletMap().get(palletId);
				unallocatedNumber = pallet.addItem(unallocatedNumber);
				if (unallocatedNumber == 0) {
					order.setUnallocatedNumber(0);
					IO.saveToFile();
					return 0;
				}
			}
		}
		// get values, make it list.
		ArrayList<Section> sectionList = new ArrayList<Section>(sectionMap.values());
		// sort it
		Collections.sort(sectionList, new ComparatorForSection());
		// existing not enough
		// for (Integer sectionId : sectionMap.keySet()) {
		// Section section = sectionMap.get(sectionId);
		for (Section section : sectionList) {
			int sectionId = section.getId();
			int newPalletId = section.createPallet();
			while (newPalletId != -1) {
				Pallet newPallet = section.getPalletMap().get(newPalletId);
				unallocatedNumber = newPallet.addItem(unallocatedNumber);
				// assign the storage data to the customer
				customer.storageMapPut(sectionId, newPalletId);
				if (unallocatedNumber == 0) {
					order.setUnallocatedNumber(0);
					IO.saveToFile();
					return 0;
				}
				newPalletId = section.createPallet();
			}
		}
		order.setUnallocatedNumber(unallocatedNumber);
		IO.saveToFile();
		return unallocatedNumber;
	}

	/**
	 * Convert the info of all orders to a single string for the UI to display
	 * 
	 * @return All the orders in a string.
	 */
	public String allOrdersToString() {
		StringBuffer sb = new StringBuffer();
		for (Order order : orderMap.values()) {
			sb.append(order);
			sb.append(LINE_SEPARATOR);
		}
		return sb.toString();
	}

	/**
	 * Convert all the storage info to a string, within which pallets info as an
	 * optional part
	 * 
	 * @param displayPallet
	 *            true to display the pallets info, false to not display
	 * @return The string contains all the info required.
	 */
	public String allSectionsToString(boolean displayPallet) {
		StringBuffer sb = new StringBuffer();
		for (Section section : sectionMap.values()) {
			sb.append(section);
			sb.append(LINE_SEPARATOR);
			if (displayPallet) {
				for (Integer palletId : section.getPalletMap().keySet()) {
					Pallet pallet = section.getPalletMap().get(palletId);
					sb.append("\tPallet ID: " + palletId + " " + pallet);
					sb.append(LINE_SEPARATOR);
				}
			}
		}
		return sb.toString();
	}

	/**
	 * Create a section has the required capacity
	 * 
	 * @param capacity
	 *            The capacity for the new section
	 * @return The section ID if section created successful, -1 if anything goes
	 *         wrong
	 */
	public int createSection(int capacity) {
		if (capacity > 0) {
			for (int i = 0; i <= sectionMap.size(); i++) {
				if (sectionMap.putIfAbsent(i, null) == null) {
					Section section = new Section(i, capacity);
					sectionMap.put(i, section);
					IO.saveToFile();
					return i;
				}
			}
		}
		return -1;
	}

	/**
	 * Edit the required section to the required new capacity
	 * 
	 * @param sectionId
	 *            Indicate the section that to be edited
	 * @param newCapacity
	 *            The new capacity for the section
	 * @return true if change successful, false if not
	 * @throws RuntimeException
	 *             This exception may be thrown for several reason: 1. The
	 *             section indicated by the section ID does not exist.2. The new
	 *             capacity is less than the used capacity in the specific
	 *             section3. The new capacity is less than 0.
	 */
	public boolean editSection(int sectionId, int newCapacity) throws RuntimeException {
		Section section = findSectionById(sectionId);
		if (section.setCapacity(newCapacity)) {
			IO.saveToFile();
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Find customer by his(her) ID.
	 * 
	 * @param customerId
	 *            The ID of the customer.
	 * @return The customer object for the specific customer ID.
	 * @throws RuntimeException
	 *             This exception may be thrown because there is no such
	 *             customer in the system.
	 */
	private Customer findCustomerById(String customerId) throws RuntimeException {
		Customer customer = customerMap.get(customerId);
		if (customer == null) {
			throw new RuntimeException("No such customer");
		} else
			return customer;
	}

	/**
	 * Find the customer who own the specific order
	 * 
	 * @param order
	 *            the order object.
	 * @return null if no owner, the customer object if found.
	 */
	private Customer findCustomerByOrder(Order order) {
		for (Customer customer : customerMap.values()) {
			if (customer.containsOrder(order))
				return customer;
		}
		return null;
	}

	/**
	 * Find order object by order ID
	 * 
	 * @param orderId
	 *            The order ID
	 * @return The order object indicated by the ID
	 * @throws RuntimeException
	 *             If no such order
	 */
	private Order findOrderById(int orderId) throws RuntimeException {
		if (!orderMap.containsKey(orderId)) {
			throw new RuntimeException("The indicated order does not exsit");
		} else {
			return orderMap.get(orderId);
		}
	}

	/**
	 * Find section by section ID
	 * 
	 * @param sectionId
	 *            The section ID
	 * @return The section object indicated by the ID
	 * @throws RuntimeException
	 *             If no such section
	 */
	private Section findSectionById(int sectionId) throws RuntimeException {
		if (!sectionMap.containsKey(sectionId)) {
			throw new RuntimeException("The indicated section does not exsit");
		} else {
			return sectionMap.get(sectionId);
		}
	}

	/**
	 * To check if the warehouse is empty
	 * 
	 * @return true if there is no section in the warehouse, false if it is not
	 *         empty.
	 */
	public boolean isEmpty() {
		if (sectionMap.size() == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Login for the specific user indicated by the id.
	 * 
	 * @param id
	 *            The user ID
	 * @param pwd
	 *            The password
	 * @return The user object if login successful
	 * @throws RuntimeException
	 *             This exception may be thrown because there is no such user
	 *             indicated by the ID
	 */
	public User login(String id, String pwd) throws RuntimeException {
		if (id.equals(administrator.getId()) && administrator.login(pwd)) {
			return administrator;
		} else if (!customerMap.containsKey(id)) {
			throw new RuntimeException("No such user");
		} else {
			Customer customer = findCustomerById(id);
			if (customer.login(pwd)) {
				return customer;
			} else {
				return null;
			}
		}
	}
	// TODO logout

	/**
	 * Make an order of the specific customer with specific item number
	 * 
	 * @param customer
	 *            The customer object indicated one specific customer
	 * @param itemNumber
	 *            The item number for the new order
	 * @return The new order's id if successful, -1 if fail
	 */
	public int makeOrder(Customer customer, int itemNumber) {
		if (itemNumber > 0) {
			int id = orderMap.size();
			Order order = new Order(id, itemNumber);
			orderMap.put(id, order);
			customer.addOrder(order);
			IO.saveToFile();
			return id;
		}
		return -1;
	}

	/**
	 * Register a new user by the ID and password
	 * 
	 * @param id
	 *            Indicate the user ID
	 * @param pwd
	 *            Indicate the password
	 * @return true if register successful, false if fail
	 */
	public boolean register(String id, String pwd) {
		if (id.equals("admin")) {
			return false;
		}
		// Customer customer = new Customer(id, pwd, name, tel, address);
		Customer customer = new Customer(id, pwd);
		if (customerMap.putIfAbsent(id, customer) == null) {
			IO.saveToFile();
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Execute the remove operation to the order indicated by the order ID.
	 * 
	 * @param orderId
	 *            Indicated the order
	 * @return true if remove operation is done, false if not
	 * @throws RuntimeException
	 *             This exception may be thrown for the following reason: 1. No
	 *             order match the given order ID. 2. Given order is not
	 *             suitable for remove. 3. Given order has no match customer
	 */
	public boolean removeItem(int orderId) throws RuntimeException {
		Order order = findOrderById(orderId);
		if (!order.removable())
			throw new RuntimeException("Order is not suitable for remove operation");
		Customer customer = findCustomerByOrder(order);
		if (customer == null)
			throw new RuntimeException("No match info for this order");
		int unremovedNumber = order.getUnremovedNumber();
		HashMap<Integer, ArrayList<Integer>> storageMap = customer.getStorageMap();
		// go through every section
		for (Integer sectionId : storageMap.keySet()) {
			Section section = findSectionById(sectionId);
			ArrayList<Integer> palletIdList = new ArrayList<>(storageMap.get(sectionId));
			// go through every pallet
			for (Integer palletId : palletIdList) {
				Pallet pallet = section.getPalletMap().get(palletId);
				unremovedNumber = pallet.removeItem(unremovedNumber);
				if (pallet.isEmpty()) {
					section.palletMapRemove(palletId);
					customer.storageMapRemove(sectionId, palletId);
				}
				if (unremovedNumber == 0) {
					order.setUnremovedNumber(0);
					// if (pallet.isEmpty()) {
					// section.palletMapRemove(palletId);
					// customer.storageMapRemove(sectionId, palletId);
					// }
					IO.saveToFile();
					return true;
				}
				// else {
				// section.palletMapRemove(palletId);
				// customer.storageMapRemove(sectionId, palletId);
				// }
			}
		}
		return false;
	}

	/**
	 * Remove a section indicated by the section ID
	 * 
	 * @param sectionId
	 *            ID of the section that to be removed
	 * @return true if remove successful, false if not
	 * @throws RuntimeException
	 *             This exception may be thrown for the following reason: 1.
	 *             there is no such section indicated by the ID. 2. The section
	 *             is not empty
	 */
	public boolean removeSection(int sectionId) throws RuntimeException {
		Section section = findSectionById(sectionId);
		if (!section.isEmpty()) {
			throw new RuntimeException("The indicated section is not empty");
		} else if (sectionMap.remove(sectionId, section)) {
			IO.saveToFile();
			return true;
		} else {
			return false;
		}
	}

}
