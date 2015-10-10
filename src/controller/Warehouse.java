package controller;

import java.util.*;

import comparator.ComparatorForSection;
import entity.*;

public class Warehouse {
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");

	// TODO reportList
	private static Warehouse warehouse;

	// Singleton pattern
	public static Warehouse getWarehouse() {
		if (warehouse == null) {
			warehouse = new Warehouse();
		}
		return warehouse;
	}

	private Administrator administrator;
	private HashMap<String, Customer> customerMap;
	private HashMap<Integer, Order> orderMap;
	private HashMap<Integer, Section> sectionMap;

	private Warehouse() {
		administrator = new Administrator("admin", "admin");
		orderMap = new HashMap<Integer, Order>(100);
		sectionMap = new HashMap<Integer, Section>(2000);
		customerMap = new HashMap<String, Customer>(20);
	}

	public int allocateItem(int orderId) throws RuntimeException {
		Order order = findOrderById(orderId);
		if (!order.allocateable())
			throw new RuntimeException("Order is not sutible for allocate operation");
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
					return 0;
				}
			}
		}
		// TODO sort the map, allocate the
		// get values, make it list.
		ArrayList<Section> sectionList = new ArrayList<Section>(sectionMap.values());
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
					return 0;
				}
				newPalletId = section.createPallet();
			}
		}
		order.setUnallocatedNumber(unallocatedNumber);
		return unallocatedNumber;
	}

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
			ArrayList<Integer> palletIdList = storageMap.get(sectionId);
			// go through every pallet
			for (Integer palletId : palletIdList) {
				Pallet pallet = section.getPalletMap().get(palletId);
				unremovedNumber = pallet.removeItem(unremovedNumber);
				if (unremovedNumber == 0) {
					order.setUnremovedNumber(0);
					return true;
				}
			}
		}
		return false;
	}

	public String allOrdersToString() {
		StringBuffer sb = new StringBuffer();
		for (Order order : orderMap.values()) {
			sb.append(order);
			sb.append(LINE_SEPARATOR);
		}
		return sb.toString();
	}

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

	// return type may changed to int,
	// indicate the new section id.
	public int createSection(int capacity) {
		if (capacity > 0) {
			for (int i = 0; i <= sectionMap.size(); i++) {
				if (sectionMap.putIfAbsent(i, null) == null) {
					Section section = new Section(i, capacity);
					sectionMap.put(i, section);
					return i;
				}
			}
		}
		return -1;
	}

	public boolean editSection(int sectionId, int newCapacity) throws RuntimeException {
		Section section = findSectionById(sectionId);
		return section.setCapacity(newCapacity);
	}

	private Customer findCustomerById(String customerId) throws RuntimeException {
		Customer customer = customerMap.get(customerId);
		if (customer == null) {
			throw new RuntimeException("No such customer");
		} else
			return customer;
	}

	private Customer findCustomerByOrder(Order order) {
		for (Customer customer : customerMap.values()) {
			if (customer.containsOrder(order))
				return customer;
		}
		return null;
	}

	private Order findOrderById(int orderId) throws RuntimeException {
		if (!orderMap.containsKey(orderId)) {
			throw new RuntimeException("The indicated order does not exsit");
		} else {
			return orderMap.get(orderId);
		}
	}

	private Section findSectionById(int sectionId) throws RuntimeException {
		if (!sectionMap.containsKey(sectionId)) {
			throw new RuntimeException("The indicated section does not exsit");
		} else {
			return sectionMap.get(sectionId);
		}
	}

	public boolean isEmpty() {
		if (sectionMap.size() == 0) {
			return true;
		} else {
			return false;
		}
	}

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

	public int makeOrder(Customer customer, int itemNumber) {
		if (itemNumber > 0) {
			int id = orderMap.size();
			Order order = new Order(id, itemNumber);
			orderMap.put(id, order);
			customer.addOrder(order);
			return id;
		}
		return -1;
	}

	public boolean register(String id, String pwd) {
		if (id.equals("admin")) {
			return false;
		}
		// Customer customer = new Customer(id, pwd, name, tel, address);
		Customer customer = new Customer(id, pwd);
		if (customerMap.putIfAbsent(id, customer) == null) {
			return true;
		} else {
			return false;
		}
	}

	public boolean removeSection(int sectionId) throws RuntimeException {
		Section section = findSectionById(sectionId);
		if (!section.isEmpty()) {
			throw new RuntimeException("The indicated section is not empty");
		} else {
			return sectionMap.remove(sectionId, section);
		}
	}

}
