package entity;

import java.util.ArrayList;
import java.util.HashMap;

public class Customer extends User {

	private String name;
	private String tel;
	private String address;
	private HashMap<Integer, ArrayList<Integer>> storageMap;
	private ArrayList<Order> orderList;

	public Customer(String id, String pwd) {
		super(id, pwd);
		storageMap = new HashMap<>();
		orderList = new ArrayList<>();
	}

	public Customer(String id, String pwd, String name, String tel, String address) {
		super(id, pwd);
		this.name = name;
		this.tel = tel;
		this.address = address;
		storageMap = new HashMap<>();
		orderList = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public boolean containsOrder(Order order) {
		return orderList.contains(order);
	}

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

	public void storageMapRemove() {
		// TODO finish this method
	}

	// TODO improve
	public HashMap<Integer, ArrayList<Integer>> getStorageMap() {
		return new HashMap<Integer, ArrayList<Integer>>(storageMap);
	}

	public boolean addOrder(Order order) {
		return orderList.add(order);
	}

	public ArrayList<Order> getOrderList() {
		return new ArrayList<>(orderList);
	}
}
