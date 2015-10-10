package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Indicate a customer in the system.
 * 
 * @author Archer
 *
 */
public class Customer extends User implements Serializable {

	/**
	 * The address of the customer
	 */
	private String address;
	/**
	 * The name of the customer
	 */
	private String name;
	/**
	 * The order in a list format belong to the customer
	 */
	private ArrayList<Order> orderList;
	/**
	 * The storage info in a map format for the customer
	 * 
	 * Inside the map, the key, Integer, indicate the section ID in the
	 * warehouse, the value, a list of integer indicate every pallet id that
	 * belong to the customer in the every specific key.
	 */
	private HashMap<Integer, ArrayList<Integer>> storageMap;
	/**
	 * The telephone number of the customer
	 */
	private String tel;

	/**
	 * The default constructor with the least 2 params.
	 * 
	 * @param id
	 *            The user id
	 * @param pwd
	 *            The password
	 */
	public Customer(String id, String pwd) {
		super(id, pwd);
		storageMap = new HashMap<>();
		orderList = new ArrayList<>();
	}

	/**
	 * The full constructors with all the fields mentioned above.
	 * 
	 * @param id
	 *            The user id of the customer
	 * @param pwd
	 *            The password of the customer
	 * @param name
	 *            The name of the customer
	 * @param tel
	 *            The telephone number of the customer
	 * @param address
	 *            The address of the customer
	 */
	public Customer(String id, String pwd, String name, String tel, String address) {
		super(id, pwd);
		this.name = name;
		this.tel = tel;
		this.address = address;
		storageMap = new HashMap<>();
		orderList = new ArrayList<>();
	}

	/**
	 * Add an order to the order list.
	 * 
	 * @param order
	 *            The order to be added.
	 * @return true if add successful, false if fail
	 */
	public boolean addOrder(Order order) {
		return orderList.add(order);
	}

	/**
	 * Exam if an order belong the customer
	 * 
	 * @param order
	 *            The order to be examed
	 * @return true if the order belong to the customer, false if not.
	 */
	public boolean containsOrder(Order order) {
		return orderList.contains(order);
	}

	/**
	 * The getter for the address attribute
	 * 
	 * @return The field address of the customer
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * The getter for the name attribute
	 * 
	 * @return The field name of the customer
	 */
	public String getName() {
		return name;
	}

	/**
	 * The getter for the orderList attribute
	 * 
	 * @return A copy of the order list, to prevent the list from illegal change
	 */
	public ArrayList<Order> getOrderList() {
		return new ArrayList<>(orderList);
	}

	/**
	 * The getter for the storageMap attribute
	 * 
	 * @return A copy of the storage map, to prevent the map from illegal change
	 */
	public HashMap<Integer, ArrayList<Integer>> getStorageMap() {
		return new HashMap<Integer, ArrayList<Integer>>(storageMap);
	}

	/**
	 * The getter for the tel attribute
	 * 
	 * @return The telephone number of the customer
	 */
	public String getTel() {
		return tel;
	}

	/**
	 * The setter for the address attribute
	 * 
	 * @param address
	 *            The new address
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * The setter for the name attribute
	 * 
	 * @param address
	 *            The new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * The setter for the tel attribute
	 * 
	 * @param address
	 *            The new telephone number
	 */
	public void setTel(String tel) {
		this.tel = tel;
	}

	/**
	 * To put a new record into the storage map
	 * 
	 * @param sectionId
	 *            The section ID for new record.
	 * @param palletId
	 *            The pallet ID for new record.
	 */
	public void storageMapPut(int sectionId, int palletId) {
		ArrayList<Integer> palletList;
		if (storageMap.get(sectionId) == null) {
			palletList = new ArrayList<>();
			palletList.add(palletId);
			storageMap.put(sectionId, palletList);
		} else {
			palletList = storageMap.get(sectionId);
			palletList.add(palletId);
		}

	}

	/**
	 * To remove a record from the storage map
	 * 
	 * @param sectionId
	 *            The section ID of the record
	 * @param palletId
	 *            The pallet ID of the record
	 */
	public void storageMapRemove(int sectionId, int palletId) {
		ArrayList<Integer> palletIdList = storageMap.get(sectionId);
		for (int i = 0; i < palletIdList.size(); i++) {
			if (palletIdList.get(i) == palletId) {
				palletIdList.remove(i);
				break;
			}
		}
		if (palletIdList.size() == 0) {
			storageMap.remove(sectionId);
		}
	}
}
